FROM daocloud.io/gizwits2015/java8:centos-java8

ADD target/*.jar /tmp/app.jar
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV TZ Asia/Shanghai
EXPOSE 8089
ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/tmp/app.jar"]