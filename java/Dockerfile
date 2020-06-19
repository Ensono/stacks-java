FROM azul/zulu-openjdk-alpine:11 as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN ./gradlew dependencies

COPY src src
RUN ./gradlew build -x test
RUN mkdir -p build/dependency && (cd build/dependency; ${JAVA_HOME}/bin/jar -xf ../libs/*.jar)

FROM azul/zulu-openjdk-alpine:11-jre

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.xxAMIDOxx.xxSTACKSxx.Application"]
EXPOSE 9000 9001
