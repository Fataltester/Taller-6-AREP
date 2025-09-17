package edu.eci.arep.concurrencia;

import edu.eci.arep.concurrencia.annotations.GetMapping;
import edu.eci.arep.concurrencia.annotations.RequestParam;
import edu.eci.arep.concurrencia.annotations.RestController;
import java.net.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class HTTPServer {
    private static final Map<String, Method> services = new HashMap<>();
    private static String staticFilesDir = "/app/public";

    private static volatile boolean running = true; 
    private static ServerSocket serverSocket;
    private static ExecutorService threadPool;

    public static void startServer(String basePackage) throws Exception {
        loadRestControllers(basePackage);

        serverSocket = new ServerSocket(35000);
        threadPool = Executors.newFixedThreadPool(10); // 10 hilos 

        System.out.println("Servidor escuchando en puerto 35000...");

        try {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(clientSocket));
                } catch (SocketException se) {
                    if (running) {
                        System.err.println("Error de socket: " + se.getMessage());
                    }
                }
            }
        } finally {
            stopServer(); 
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            boolean isFirstLine = true;
            URI requestUri = null;

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    requestUri = new URI(inputLine.split(" ")[1]);
                    isFirstLine = false;
                }
                if (!in.ready()) break;
            }

            OutputStream rawOut = clientSocket.getOutputStream();
            if (requestUri != null && requestUri.getPath().startsWith("/app")) {
                String response = processRequest(requestUri);
                rawOut.write(response.getBytes());
                rawOut.flush();
            } else {
                sendStaticFile(rawOut, requestUri != null ? requestUri.getPath() : "/");
            }
        } catch (Exception e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    public static void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (threadPool != null) {
                threadPool.shutdown();
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            }
            System.out.println("Servidor apagado elegantemente.");
        } catch (Exception e) {
            System.err.println("Error apagando servidor: " + e.getMessage());
        }
    }

    private static String processRequest(URI requestUri) throws IllegalAccessException, InvocationTargetException {
        String servicePath = requestUri.getPath().substring(4);
        HttpRequest req = new HttpRequest(requestUri);
        Method m = services.get(servicePath);

        if (m == null) {
            return "HTTP/1.1 404 Not Found\r\n\r\nServicio no encontrado";
        }

        RequestParam rp = (RequestParam) m.getParameterAnnotations()[0][0];
        String[] argsValues;

        if (requestUri.getQuery() == null) {
            argsValues = new String[]{rp.defaultValue()};
        } else {
            String queryParamName = rp.value();
            argsValues = new String[]{req.getValue(queryParamName)};
        }

        String header = "HTTP/1.1 200 OK\r\n" +
                        "content-type: application/json\r\n\r\n";
        return header + m.invoke(null, argsValues);
    }

    public static void staticfiles(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        staticFilesDir = path;
    }

    private static void sendStaticFile(OutputStream out, String requestPath) throws IOException {
    String relativePath = requestPath.startsWith("/") ? requestPath.substring(1) : requestPath;
    if (relativePath.isEmpty()) {
        relativePath = "index.html"; 
    }

    InputStream resourceStream = HTTPServer.class.getClassLoader().getResourceAsStream("public/" + relativePath);

    if (resourceStream != null) {
        byte[] fileBytes = resourceStream.readAllBytes();
        String mimeType = guessMimeType(relativePath);

        String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + fileBytes.length + "\r\n\r\n";

        out.write(header.getBytes());
        out.write(fileBytes);
        out.flush();
    } else {
        String error = "HTTP/1.1 404 Not Found\r\n\r\nFile not found";
        out.write(error.getBytes());
        out.flush();
    }
}


    public static String guessMimeType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }

    public static void loadRestControllers(String basePackage) throws Exception {
        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File dir = new File(resource.toURI());
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = basePackage + "." + file.getName().replace(".class", "");
                    Class<?> c = Class.forName(className);
                    if (c.isAnnotationPresent(RestController.class)) {
                        for (Method m : c.getDeclaredMethods()) {
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                String mapping = m.getAnnotation(GetMapping.class).value();
                                services.put(mapping, m);
                                System.out.println("Registrado endpoint: " + mapping + " -> " + m);
                            }
                        }
                    }
                }
            }
        }
    }
}
