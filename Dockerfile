FROM openjdk:11
EXPOSE 8081
ADD target/kidzone-1.0.war kidzone-1.0.war
ENTRYPOINT ["java","-jar","kidzone-1.0.war"]