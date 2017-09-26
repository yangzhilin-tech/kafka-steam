FROM @docker.env.ip@:5000/alpine-java
RUN sh -c 'touch /etc/localtime'
RUN sh -c 'touch /etc/timezone'
RUN sh -c 'mkdir -p /opt/conf'
#RUN sh -c 'touch /etc/sysconfig/clock'
#RUN echo 'ZONE="Asia/Shanghai"' > /etc/sysconfig/clock
#RUN echo 'UTC=false' >> /etc/sysconfig/clock
#RUN echo 'ARC=false' >> /etc/sysconfig/clock
#ENV TZ=Asia/Shanghai
RUN echo $TZ > /etc/timezone
#USER tomcat
WORKDIR /opt/ppagent/
RUN adduser -Ds /sbin/nologin tomcat
WORKDIR /opt/spring-boot-maven-docker/
RUN chown -R tomcat:tomcat /opt/spring-boot-maven-docker
USER tomcat
VOLUME /tmp
#VOLUME /tmp
ADD @project.build.finalName@.jar /opt/spring-boot-maven-docker/
#RUN sh -c 'touch /app.jar'
#RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
#RUN echo "Asia/shanghai" > /etc/timezone
#"-Duser.timezone=GMT+8"
#ENV PATH /usr/bin:$PATH
ENV JAVA_OPTS="-Xmx2800M -Xms1880M -XX:PermSize=128M -XX:MaxPermSize=888M -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -XX:LargePageSizeInBytes=256M -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+ExitOnOutOfMemoryError -XX:+CrashOnOutOfMemoryError -XX:+UseCompressedOops -server"
#ENTRYPOINT ["java",$JAVA_OPTS,"-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+8","-javaagent:/opt/ppagent/pinpoint-bootstrap-1.5.2.jar", "-Dpinpoint.agentId=@project.build.finalName@_@pinpoint.agentId@", "-Dpinpoint.applicationName=@project.build.finalName@","-Druntime.env=@docker.env@","-Dspring.config.location=classpath:/@docker.env@/application.yml", "-jar","@project.build.finalName@.jar"]
ENTRYPOINT ["sh", "-c", "java  $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT+8 -javaagent:/opt/ppagent/pinpoint-bootstrap-1.5.2.jar -Dpinpoint.agentId=@project.build.finalName@_@pinpoint.agentId@ -Dpinpoint.applicationName=@project.build.finalName@ -Druntime.env=@docker.env@ -Dspring.config.location=classpath:/@docker.env@/application.yml -jar @project.build.finalName@.jar" ]
