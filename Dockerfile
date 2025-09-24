FROM openjdk:17
 
WORKDIR /app
 
EXPOSE 8080

ENV SPRING_DATASOURCE_USERNAME root
ENV SPRING_DATASOURCE_PASSWORD secret
ENV SPRING_DATASOURCE_URL jdbc:mysql://34.224.216.43:3306/properties?createDatabaseIfNotExist=true

# Copiar el jar empaquetado con todas las dependencias
COPY target/Taller5arep-0.0.1-SNAPSHOT.jar app.jar

# Ejecutar el jar
ENTRYPOINT ["java","-jar","app.jar"]