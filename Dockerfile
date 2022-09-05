FROM maven:3.8.6-jdk-11-slim AS build

RUN mkdir -p /project

COPY . /project

WORKDIR /project

RUN mvn install:install-file -Dfile=/project/libs/better-jieba-1.0.0-SNAPSHOT.jar -DgroupId=org.manlier -DartifactId=better-jieba -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar && mvn clean package -Dmaven.test.skip

FROM openjdk:11

RUN mkdir /app

COPY --from=build /project/testingocr-331615-5004a2226dc5.json /app/testingocr-331615-5004a2226dc5.json

# RUN addgroup -g 1001 -S tecogroup

# RUN adduser -S teco -u 1001

COPY --from=build /project/target/NoteShareDevBackend-0.0.1-SNAPSHOT.jar /app/NoteShareDevBackend-0.0.1-SNAPSHOT.jar

WORKDIR /app

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/testingocr-331615-5004a2226dc5.json

# RUN chown -R teco:tecogroup /app

CMD java -jar NoteShareDevBackend-0.0.1-SNAPSHOT.jar