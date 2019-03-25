FROM openjdk:8u181-jre-alpine
LABEL maintainer="Rafael (Otto) <rafael.dealmeida@adp.com>"

ENV http_proxy=http://usproxy.es.oneadp.com:8080 \
    https_proxy=http://usproxy.es.oneadp.com:8080 \
    no_proxy=.adp.com \
    JAVA_OPTS="" \
    JKS_PASSWORD="changeit" \
    TZ=EST

WORKDIR /app
EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Djava.security.egd=file:/dev/./urandom -jar dashboard-api.jar" ]

ADD adp_internal_root_ca.cer .
RUN keytool -importcert -noprompt -trustcacerts \
            -alias adp-internal \
            -file adp_internal_root_ca.cer \
            -storepass $JKS_PASSWORD \
            -keystore $JAVA_HOME/lib/security/cacerts

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD build/libs/dashboard-api.jar .
