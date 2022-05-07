FROM node:15.10.0-alpine3.10 AS build-node
COPY ./app/web /app
WORKDIR /app
RUN npm install
RUN npm run build

FROM openjdk:11-jdk
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD [ "curl", "127.0.0.1:8080/health" ]

RUN mkdir /app
ADD ./app /app/app
RUN rm -rf /app/app/web
ADD ./gradle /app/gradle
ADD ./gradlew /app/
ADD ./settings.gradle.kts /app/
COPY --from=build-node /app/public /app/app/web/public
WORKDIR /app
RUN ./gradlew build

CMD [ "./gradlew", "run" ]
