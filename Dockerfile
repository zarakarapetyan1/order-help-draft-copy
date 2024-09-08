FROM openjdk:17-alpine
COPY components/services/implementation/build/libs/order-service-implementation.jar /order-service-implementation.jar
ENTRYPOINT ["java","-jar","/order-service-implementation.jar"]