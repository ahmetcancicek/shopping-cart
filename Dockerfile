FROM openjdk:11

ADD ./target/shopping-cart.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/shopping-cart.jar"]

EXPOSE 8090