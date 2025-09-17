# Modularización con virtualización e Introducción a Docker
La intención del laboratorio es implementar concurrencia en el servidor que actua como framework previamente implementado, para lograr recibir diferentes peticiones y detenerlas, ofreciendo tanto servicios o archivos estáticos. También utilizamos docker para desplegarlos en una instancia EC2 en AWS
## Objetivos 
* Implementar concurrencia en nuestro servidor 
* Introducirnos a docker y su funcionamiento
* Crear nuestri primera instancia en AWS
* Utilizar contenedores para exponer nuestros servicios de java
* Utilizar instancias de AWS para exponer nuestros docker
* Realizar Inversion de control

## PreRequisitos
Para el proyecto debemos configurar un entorno maven para armar el proyecto, para esto hay diferentes opciones como visual studio, intellij, etc. Si se quiere trabajar sobre estos se debe configurar e instalar maven para su funcionamiento, pero, para este laboratorio y facilitarme este proceso utilicé Netbeans el cual me ahorra ese proceso, puede instalar el ambiente de desarrollo en su página oficial [aquí](https://netbeans.apache.org/front/main/index.html). También debemos tener java 17 instalado y git para poder clonar el repositorio actual.
Además de tener instalado maven, debe tener docker instalado en su computador para lograr ejecutar el laboratorio y los contenedores
## Instalación

1. Vamos a clonar el repositorio con el siguiente comando
```bash
git clone https://github.com/Fataltester/Taller-2-AREP.git
```

2. Como estamos utilizando Netbeans simplemente abrimos el proyecto en el ambiente y ejecutamos el archivo "MicroSpringboot", una vez iniciado, en la consola aparecerá "Listo para recibir ..."
   
3. Procedemos a ir al navegador e insertar la siguiente dirección
```bash
http://localhost:35000/....
```
Ya dependiendo la solicitud si es de archivo estático o cargar los métodos del controlador definimos la URI

Para consultar tenemos:
Controlador:
```bash
http://localhost:35000/app/greeting?name=..
```
Para archivos estáticos:
```bash
http://localhost:35000/cat.jpg
http://localhost:35000/index.html
http://localhost:35000/response.js
http://localhost:35000/style.css
```

## Arquitectura
Tenemos una arquitectura en capas:

Capa de Presentación -> el main que esta encargado de levantar el servidor.

Capa de Infraestructura -> la clase "HttpServer" donde se levantan los socket, reciben solicitudes, generan respuestas e invoca los métodos.

Capa de Controladores -> los controladores definidos @RestController.

Capa de Pruebas -> pruebas JUnit.

## Concurrencia
Para manejar la concurrencia con el servidor, tengo un ExecutorService con 10 hilos creados

<img width="621" height="272" alt="image" src="https://github.com/user-attachments/assets/ddb343da-384e-4296-b623-5ec92b6911d5" />

Para el detener de manera elegante se implemento el siguiente método

<img width="612" height="308" alt="image" src="https://github.com/user-attachments/assets/dc402e6b-dc0e-4bf1-b635-a1caebf481f4" />

## Inicializar con docker y AWS

# Docker
Es importante tener en cuenta los dos archivos docker que creamos dentro del proyecto
Docker file:

<img width="781" height="277" alt="image" src="https://github.com/user-attachments/assets/a80c99e7-b590-4f1a-82cf-5bc0d25fed73" />

Docker compose:

<img width="535" height="495" alt="image" src="https://github.com/user-attachments/assets/a584676c-6fd2-4361-b07e-8c1145fc2919" />

Para construir el proyecto en docker utilizamos el siguiente comando

```bash
docker build -t fataltester229/dockerconcurrencia .    
```
<img width="960" height="393" alt="image" src="https://github.com/user-attachments/assets/70b17e80-1b40-41d5-9720-6867ba8830a0" />

Luego creamos las 3 instancias docker localmente
```bash
docker run -d -p 34000:35000 --name firstdockercontainer fataltester229/dockerconcurrencia
docker run -d -p 34001:35000 --name seconddockercontainer fataltester229/dockerconcurrencia
docker run -d -p 34002:35000 --name thirddockercontainer fataltester229/dockerconcurrencia   
```

<img width="961" height="218" alt="image" src="https://github.com/user-attachments/assets/c7020400-7482-4324-a529-8dca3975a475" />


Finalmente para subir los cambios al dockerhub utilizamos

```bash
docker push fataltester229/dockerconcurrencia:latest  
```

<img width="962" height="202" alt="image" src="https://github.com/user-attachments/assets/d1bb4c5b-9410-4f5e-82d0-63b144ca9494" />

<img width="1072" height="620" alt="image" src="https://github.com/user-attachments/assets/58b1005e-f4e4-4164-bb8b-cc7162d06733" />

Verificamos su funcionamiento

<img width="732" height="535" alt="image" src="https://github.com/user-attachments/assets/8275d3ff-ae1f-41f0-8d0f-9b97194c03ac" />

<img width="702" height="510" alt="image" src="https://github.com/user-attachments/assets/bbbaaa75-78ea-4c5c-86bc-c05786f69672" />

<img width="375" height="138" alt="image" src="https://github.com/user-attachments/assets/e3c4ae45-eb03-4bf6-a8b6-6f020c04e0e5" />

# AWS
Para AWS creamos una instancia EC2, ingresamos a la instancia por ssh y ejecutamos los siguientes comandos

```bash
sudo yum update -y
sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
docker run -d -p 42000:35000 --name firstdockerimageaws fataltester229/dockerconcurrencia
```

Para verificar el funcionamiento dentro de la instancia realizamos la consulta a la siguiente URL

```bash
http://ec2-54-80-65-160.compute-1.amazonaws.com:42000/index.html
http://ec2-54-80-65-160.compute-1.amazonaws.com:42000/app/greeting
```

<img width="792" height="621" alt="image" src="https://github.com/user-attachments/assets/abecbfc7-d03c-4d79-9406-3f49bc5bf7e8" />

<img width="874" height="329" alt="image" src="https://github.com/user-attachments/assets/1b87cee2-93f2-4ce4-9fcd-2cc15fb759bc" />

## Reporte de pruebas
## Unitarias
Al ejecutar el archivo de pruebas obtenemos lo siguiente:

<img width="950" height="334" alt="image" src="https://github.com/user-attachments/assets/efa72db4-cfb1-44e7-86cb-4960d345a337" />

Tenemos las siguientes pruebas:
* testGuessMimeTypeHtml -> se reconocen las extensiones html.
* testGuessMimeTypePng -> se reconocen las extensiones png.
* testGuessMimeTypeUnknown -> al pedir una extension que no exista, retorne text/plain con una extension desconocida.
* testLoadRestControllers -> prueba que los controladores y sus métodos son cargados correctamente
## Aceptación
Ejecute el proyecto de manera local

<img width="757" height="529" alt="image" src="https://github.com/user-attachments/assets/76be62b2-f96d-43e3-8b6d-08b11fc555a7" />

<img width="466" height="204" alt="image" src="https://github.com/user-attachments/assets/d77535bc-908f-40a8-8f1a-7329ac10d359" />

<img width="422" height="183" alt="image" src="https://github.com/user-attachments/assets/ee70a96b-652e-4f7d-a492-e514a429915d" />

### Contruido con

[Maven](https://maven.apache.org) Maven

[Netbeans](https://netbeans.apache.org/front/main/index.html) Netbeans 

[Git](https://git-scm.com) Git

[docker](https://www.docker.com) Docker

[AWS](https://aws.amazon.com/es/free/?trk=d467a1e4-ef7e-4b01-9632-5fc46ff30fb0&sc_channel=ps&ef_id=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE:G:s&s_kwcid=AL!4422!3!647999789205!e!!g!!aws!19685287144!146461596896&gad_campaignid=19685287144&gclid=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE) AWS

### Autor
Juan David Martínez Mendez - [Fataltester](https://github.com/Fataltester)


