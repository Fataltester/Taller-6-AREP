FROM openjdk:17
 
WORKDIR /app
 
ENV PORT=35000
 
COPY /target/classes ./classes
COPY /target/dependency /usrapp/bin/dependency
COPY src/main/resources/public ./public

 
CMD ["java","-cp","./classes:./dependency/*","edu.eci.arep.concurrencia.MicroSpringboot"]