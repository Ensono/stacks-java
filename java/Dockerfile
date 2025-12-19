FROM azul/zulu-openjdk-alpine:17.0.9 as build
WORKDIR /workspace/app

# Copy the m2 directory if it exists to re-use an external cache.
# The `mvnw` file is also copied so that if an `.m2` directory isn't present
# the command won't fail.
COPY mvnw .m2* /root/.m2/repository/

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ls -la /root/.m2/repository

RUN ./mvnw dependency:go-offline --quiet \
    && ./mvnw process-resources --quiet \
    && ./mvnw clean package -Dmaven.default-test.skip=true -Dmaven.test.skip=true -DskipTests --no-transfer-progress --quiet \
    && find target -maxdepth 1 -type f -name '*.jar' ! -name '*-javadoc.jar' ! -name '*-sources.jar' -exec cp {} app.jar \;


FROM azul/zulu-openjdk-alpine:17-jre

VOLUME /tmp

# Use the standard Spring Boot launcher; avoid hardcoded classpaths that depend on layer layout.
COPY --from=build /workspace/app/app.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 9000
