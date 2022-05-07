# Architecture With Kotlin-Ktor and SvelteJS


## Features
* PlaceHolder  - Why Ktor 
* Placeholder - Why SvelteJS
* AI/ ML 
* REST API

## Stack
* Front end
  * [`sveltejs`](https://svelte.dev/) Compliled version / Faster than Vue and React .
  * [`carbon-components-svelte`](https://github.com/IBM/carbon-components-svelte) ui components
  * `app/web`
* Back end
  * [`ktor`](https://ktor.io/) non I/O blocking  microservice framework 
  * `app/src/main/kotlin/archstyle`
* Ops
  * scripts in `bin`
  * single `Dockerfile` for the web tier
  * compose setup for web and postgres db

## Deploying
```sh
# setup app/web/.env
./bin/set-environment.sh
./bin/docker-build.sh
docker-compose up
# first signup will be an admin
```
app available on port 8080

## Running locally
### Configuration
Environment variables
```sh
ARCHSTYLE_PORT = 8080
ARCHSTYLE_ENVIRONMENT = testing
```

### API
```sh
# boot up a local db
./dev-db-run.sh
./gradlew run
```
health [endpoint](http://localhost:8080/health)

web bundle is also being served on this port at the webroot

[api spec](./app/src/test/kotlin/archstyle/AppTest.kt) `¯\_(ツ)_/¯`

### Web
create `app/web/.env`
```sh
API_ENDPOINT=http://localhost:8080
```
then run
```sh
# from ./app/web
npm install
npm run dev
```
available on [port 5000](http://localhost:5000/)

### Tests
```
./gradlew test
```

## Contributors
[Bidit Pakrashi](https://github.com/BiditPakrashi/)
