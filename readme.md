# Taller de trabajo individual en patrones arquitecturales
Talleres relacionados con la clase https://github.com/Fataltester/Taller-5-AREPJPA
## Resumen
En este taller se desarrollo un CRUD con springboot el cual esta contenido en una instacia EC2 que esta conectada a otra instancia que ofrece el servicio mySQL para la persistencia, en el sistema se administra un CRUD para propiedades, donde cada propiedad cuenta con un ID generado automáticamente, dirección, precio, tamaño y descripción.

## PreRequisitos
Para el proyecto debemos configurar un entorno maven para armar el proyecto, para esto hay diferentes opciones como visual studio, intellij, etc. Si se quiere trabajar sobre estos se debe configurar e instalar maven para su funcionamiento, pero, para este laboratorio y facilitarme este proceso utilicé Netbeans el cual me ahorra ese proceso, puede instalar el ambiente de desarrollo en su página oficial [aquí](https://netbeans.apache.org/front/main/index.html). También debemos tener java 17 instalado y git para poder clonar el repositorio actual.
Además de tener instalado maven, debe tener docker instalado en su computador para lograr ejecutar el laboratorio y los contenedores
## Instalación

1. Vamos a clonar el repositorio con el siguiente comando
```bash
git clone https://github.com/Fataltester/Taller-2-AREP.git]
```

2. Construimos el proyecto con mvn clean package
   
3. corremos el docker
```bash
docker run -d   --name lab5arep   -p 8080:8080   -e SPRING_DATASOURCE_URL=jdbc:mysql://34.224.216.43:3306/properties?createDatabaseIfNotExist=true   -e SPRING_DATASOURCE_USERNAME=root   -e SPRING_DATASOURCE_PASSWORD=secret   fataltester229/lab5:latest
```

<img width="960" height="98" alt="image" src="https://github.com/user-attachments/assets/ae8d7de1-b72e-474b-a769-4c5ee1456756" />


<img width="1876" height="544" alt="image" src="https://github.com/user-attachments/assets/d32f257a-7053-4b94-a413-7b0554b22267" />

aquí ofrecemos los servicios get, post, put y delete para las propiedades y cada propiedad
## Arquitectura
Tenemosla siguiente arquitectura

Capa de Presentación -> el main que esta encargado de levantar el servidor.

Capa de Negocio -> capa donde presentamos el modelo(estructura de los objetos principales), repositorio(persistencia) y servicio que implementa la lógica.

Componente de persistencia y despliegue -> la persistencia contenida en la instancia de aws y los servicios ofrecidos por las instancias.

Capa de Controladores -> los controladores definidos @RestController.

## Inicializar con docker y AWS

# Docker
Es importante tener en cuenta los dos archivos docker que creamos dentro del proyecto
Docker file:

<img width="875" height="338" alt="image" src="https://github.com/user-attachments/assets/710d563f-53b4-4b04-af1c-363805e95234" />

Docker compose:

<img width="937" height="434" alt="image" src="https://github.com/user-attachments/assets/c60dafc4-9eee-4872-964d-5f66f2dab321" />

Desde la maquina se suben los cambios a dockerhub para poder bajarlos dentro de la instancia ec2 que contiene el back

```bash
docker build -t fataltester229/lab5:latest .
docker push fataltester229/lab5:latest  
```

# AWS
Para AWS creamos una instancia EC2 para el back y otra para mySQL, ingresamos a la instancia del back por ssh y ejecutamos los siguientes comandos, tener en cuenta que mySQL ya esta instalado en la otra instancia

```bash
sudo yum update -y
sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo docker pull fataltester229/lab5:latest 
sudo docker run -d   --name lab5arep   -p 8080:8080   -e SPRING_DATASOURCE_URL=jdbc:mysql://34.224.216.43:3306/properties?createDatabaseIfNotExist=true   -e SPRING_DATASOURCE_USERNAME=root   -e SPRING_DATASOURCE_PASSWORD=secret   fataltester229/lab5:latest

```

Para verificar el funcionamiento dentro de la instancia realizamos la consulta a la siguiente URL

```bash
http://ec2-34-227-221-17.compute-1.amazonaws.com:8080/?
```
## Caputas del funcionamiento

<img width="1761" height="485" alt="image" src="https://github.com/user-attachments/assets/16a33dd7-d8df-4bcc-9fca-1af734f4094f" />

[video del funcionamiento](https://github.com/Fataltester/Taller-5-AREP/blob/main/despliegue%20lab%205.mp4)

### Contruido con

[Maven](https://maven.apache.org) Maven

[Netbeans](https://netbeans.apache.org/front/main/index.html) Netbeans 

[Git](https://git-scm.com) Git

[docker](https://www.docker.com) Docker

[AWS](https://aws.amazon.com/es/free/?trk=d467a1e4-ef7e-4b01-9632-5fc46ff30fb0&sc_channel=ps&ef_id=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE:G:s&s_kwcid=AL!4422!3!647999789205!e!!g!!aws!19685287144!146461596896&gad_campaignid=19685287144&gclid=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE) AWS

### Autor
Juan David Martínez Mendez - [Fataltester](https://github.com/Fataltester)


