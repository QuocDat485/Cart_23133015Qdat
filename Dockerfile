FROM tomcat:9.0-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY Cart_23133015.war /usr/local/tomcat/webapps/ROOT.war

# Expose cổng 8080
EXPOSE 8080

# Khởi động Tomcat
CMD ["catalina.sh", "run"]