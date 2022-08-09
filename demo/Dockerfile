FROM amazoncorretto:11.0.12 as builder

WORKDIR application

ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:11.0.12
WORKDIR application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./

EXPOSE 8804

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
