FROM tomcat:9.0.16-jre8

ADD target/ca2_Web_exploded.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]