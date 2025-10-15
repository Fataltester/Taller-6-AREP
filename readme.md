# Secure Application Design
## Resumen
En este taller se desarrollo la seguridad de autenticación por medio de certificados por "Let's Encode", DNS gratuitos dados por duckdns y TLS 

## PreRequisitos
Para el proyecto debemos configurar un entorno maven para armar el proyecto, para esto hay diferentes opciones como visual studio, intellij, etc. Si se quiere trabajar sobre estos se debe configurar e instalar maven para su funcionamiento, pero, para este laboratorio y facilitarme este proceso utilicé Netbeans el cual me ahorra ese proceso, puede instalar el ambiente de desarrollo en su página oficial [aquí](https://netbeans.apache.org/front/main/index.html). También debemos tener java 17 instalado y git para poder clonar el repositorio actual.
Además de tener instalado maven, debe tener docker instalado en su computador para lograr ejecutar el laboratorio y los contenedores
## Instalación

1. Vamos a clonar el repositorio con el siguiente comando
```bash
git clone https://github.com/Fataltester/Taller-6-AREP.git]
```

2. Construimos el proyecto con mvn clean package
   
3. corremos el docker
```bash
docker run -d   --name lab6arep   -p 8443:8443   -e SPRING_DATASOURCE_URL=jdbc:mysql://13.217.196.150:3306/properties?createDatabaseIfNotExist=true   -e SPRING_DATASOURCE_USERNAME=root   -e SPRING_DATASOURCE_PASSWORD=TuContraseñaSegura123!   fataltester229/lab6:latest
mvn spring-boot:run
```
Ya con eso podemos acceder a la página del login
<img width="645" height="522" alt="image" src="https://github.com/user-attachments/assets/f832d663-5010-4d45-a44e-cbb3a8782e12" />

La maquina de apache puede traer archivos estáticos como
* auth.html
* index.html
* style.css
* styles.css
* auth.js
Y para acceder al los servicio rest de propiedades utilizamos las mismas rutas del taller 5

## Arquitectura
Tenemosla siguiente arquitectura



## Inicializar con docker y AWS

# Docker
Es importante tener en cuenta los dos archivos docker que creamos dentro del proyecto
Docker file:

<img width="864" height="350" alt="image" src="https://github.com/user-attachments/assets/cb0d3262-2c95-4e70-a22c-6e559a5812af" />

Docker compose:

<img width="822" height="661" alt="image" src="https://github.com/user-attachments/assets/a7736791-d902-4b5d-bdd6-f5312d3921fd" />


Desde la maquina se suben los cambios a dockerhub para poder bajarlos dentro de la instancia ec2 que contiene el back

```bash
docker build -t fataltester229/lab6:latest .
docker push fataltester229/lab6:latest  
```
<img width="1104" height="755" alt="image" src="https://github.com/user-attachments/assets/4d4a9d71-264a-4c18-aba0-1d0839993f61" />


# AWS
Para AWS creamos las instancia EC2 para el back y mysql(springserver) y otra que sirve los archivos estáticos del front(apacheserver), ingresamos a la instancia de springserver e instalamos docker y mysql

```bash
sudo yum update -y
sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo docker pull fataltester229/lab6:latest 
sudo docker run -d   --name lab6arep   -p 8080:8080   -e SPRING_DATASOURCE_URL=jdbc:mysql://13.217.196.150:3306/properties?createDatabaseIfNotExist=true   -e SPRING_DATASOURCE_USERNAME=root   -e SPRING_DATASOURCE_PASSWORD=TuContraseñaSegura123!   fataltester229/lab6:latest
```
Luego definimos los DNS

<img width="1259" height="441" alt="image" src="https://github.com/user-attachments/assets/9069fe4f-cc02-498a-b45c-190f9103a46b" />

Verificamos que el grupo de seguridad permita las comunicaciones, cabe aclarar que las dos instancias pertenecen al mismo grupo

<img width="1126" height="398" alt="image" src="https://github.com/user-attachments/assets/54313a5e-956f-470d-a658-7078b8c17d0f" />

Luego se ingresa a la instancia apacheserver por ssh y se inicializa la bd, traemos los cambios de dockerhub y ejecutamos el contenedor
```bash
 docker pull fataltester229/lab6
 docker run -d -p 8443:8443 --name lab6 fataltester229/lab6:latest
 docker ps
```
Y como podemos ver la conexión segura esta establecida

<img width="583" height="233" alt="image" src="https://github.com/user-attachments/assets/182db267-b3e0-4854-9e5f-801a00dd3e2a" />


### Contruido con

[Maven](https://maven.apache.org) Maven

[Netbeans](https://netbeans.apache.org/front/main/index.html) Netbeans 

[Git](https://git-scm.com) Git

[docker](https://www.docker.com) Docker

[AWS](https://aws.amazon.com/es/free/?trk=d467a1e4-ef7e-4b01-9632-5fc46ff30fb0&sc_channel=ps&ef_id=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE:G:s&s_kwcid=AL!4422!3!647999789205!e!!g!!aws!19685287144!146461596896&gad_campaignid=19685287144&gclid=CjwKCAjwlaTGBhANEiwAoRgXBSZykrEBt9z7thDt-V49nT_IO1KIvJXazolkoGAiyE1Bzko3wk7V8RoCdX4QAvD_BwE) AWS

### Autor
Juan David Martínez Mendez - [Fataltester](https://github.com/Fataltester)


