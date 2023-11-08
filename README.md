# Migrate opentracing-quickstart to OpenTelemetry

This project uses Quarkus, the Supersonic Subatomic Java Framework. It provides guidance on how to migrate from OpenTracing to OpenTelemetry.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

The starting point is an automatically generated application from https://code.quarkus.io/ using `resteasy-reactive`and `quarkus-smallrye-opentracing.

You can follow the steps described in this Quarkus tutorial: [Migrate from OpenTracing to OpenTelemetry](https://quarkus.io/tutorials/telemetry-opentracing-to-otel-tutorial).

The steps in the tutorial relate to the branches in this repository, in the following way:

| Tutorial Step | Branch | Commit                                                                                                                                                                                                                                                                                           |
| ------------- | ------ |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1. Starting point | [main](https://github.com/quarkusio/opentracing-quickstart-migration) | [1. Starting point](https://github.com/quarkusio/opentracing-quickstart-migration/commit/27193f700258b05c03708282ea1cba5783ab8e9b)                                                                                                                                                                          |
| 2. Big bang change from OpenTracing to OpenTelemetry | [2-big-bang-change](https://github.com/quarkusio/opentracing-quickstart-migration/tree/2-big-bang-change)| [2. Big Bang change](https://github.com/quarkusio/opentracing-quickstart-migration/commit/a5b6b5e5bc15cb7bbaaefee3b10f1129bad51df6)                                                                                                                                                                         |
| 3. The big bang replacement, when you have manual instrumentation | [3-big-bang-replacement](https://github.com/quarkusio/opentracing-quickstart-migration/tree/3-big-bang-replacement)| [3.a OpenTracing manual instrumentation](https://github.com/quarkusio/opentracing-quickstart-migration/commit/d92dba5f3edb6e55b330721a1f9bacd48f2c74ec) <br/>[3.b OpenTelemetry manual instrumentation](https://github.com/quarkusio/opentracing-quickstart-migration/commit/c279b713857901c03c5cf22afcec2f19080bca1b) |
| 4. The OpenTracing shim | [4-opentracing-shim](https://github.com/quarkusio/opentracing-quickstart-migration/tree/4-opentracing-shim)| [4.a OpenTracing shim - before](https://github.com/quarkusio/opentracing-quickstart-migration/commit/e4ff72d4bab4ea358efab2d6bb1685fef785e901) <br/> [4.b OpenTracing shim - after](https://github.com/quarkusio/opentracing-quickstart-migration/commit/2814337563bc9d4acc6ba172f2db9aa28557f270) |

You can follow the different commits and see the relates changes.

## Starting Jaeger

Let's start the Jaeger-all-in-one Docker image, where we will retrieve and see the captured traces:

```bash
docker run -e COLLECTOR_OTLP_ENABLED=true -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 4317:4317 -p 4318:4318 -p 14250:14250 -p 14268:14268 -p 14269:14269 -p 9411:9411 jaegertracing/all-in-one:latest
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/opentracing-quickstart-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- SmallRye OpenTracing ([guide](https://quarkus.io/guides/opentracing)): Trace your services with SmallRye OpenTracing
- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
