# First stage: complete build environment
FROM maven:3.9.6-sapmachine-21 AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/

# package jar
RUN mvn clean package -Dmaven.test.skip

# Second stage: minimal runtime environment
# FROM eclipse-temurin:18-jre-alpine
FROM ubuntu:bionic-20220531

ENV openjdkversion=jdk-21.0.2
ENV openjdkurl=https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz
ENV openjdktar=openjdk-21.0.2_linux-x64_bin.tar.gz

# Install OpenJDK-11
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
		bzip2 \
		unzip \
		xz-utils \
        ca-certificates p11-kit \
        binutils \
        fontconfig libfreetype6 \
        wget tar gzip ca-certificates-java && \
    wget -q ${openjdkurl} -P /opt/jdk \
    && tar -xzf /opt/jdk/${openjdktar} -C /opt/jdk \
    && rm /opt/jdk/${openjdktar} \
    && apt-get clean; \
    rm -rf /var/lib/apt/lists/*

# ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
# ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN ls -la /opt/jdk

ENV JAVA_HOME=/opt/jdk/${openjdkversion}
ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN jlink \
    # --add-modules java.xml.bind,java.sql,java.naming,java.management,java.instrument,java.security.jgss \
    --add-modules java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql \
    --verbose \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /opt/jre-minimal

# copy jar from the first stage
COPY --from=builder target/scg-comments-0.0.1-SNAPSHOT.jar scg-comments-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "scg-comments-0.0.1-SNAPSHOT.jar"]