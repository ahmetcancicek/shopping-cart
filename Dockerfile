FROM openjdk:11

ADD ./target/ecommerce-starter.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/ecommerce-starter.jar"]

EXPOSE 8090