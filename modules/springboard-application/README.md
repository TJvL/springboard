# Springboard Application

This is the main Spring Boot application module that orchestrates and runs the complete Springboard system. It serves as the entry point that brings together all the web and core modules into a single runnable application.

## Dependencies

### Web Modules

- **springboard-inference-web**: Web API for inference engine capabilities
- **springboard-expression-web**: Web API for expression parsing and evaluation

### Core Modules (inherited through web modules)

- **springboard-inference**: Core inference engine and reasoning capabilities
- **springboard-expression**: ANTLR-based expression parsing and evaluation
- **springboard-core**: Common utilities and base functionality

### Spring Boot Stack

- **Spring WebFlux**: Reactive web framework for non-blocking HTTP endpoints
- **Spring Security**: Authentication and authorization for API endpoints
- **Spring Session Redis**: Distributed session management using Redis
- **Spring Modulith**: Modular application architecture support

### Kotlin Integration

- **Kotlin Coroutines**: Reactive programming with kotlinx-coroutines-reactor
- **Jackson Kotlin Module**: JSON serialization support for Kotlin data classes
- **Kotlin Reflection**: Runtime reflection capabilities for Spring

## Purpose

This module:

- Contains the main Spring Boot application class with `@SpringBootApplication`
- Configures component scanning across all module packages
- Provides the single executable JAR for the entire Springboard system
- Orchestrates the integration of inference and expression web APIs
- Serves as the deployment artifact for the complete system

## Architecture

The application follows a modular architecture where:

- Web modules provide REST API controllers and web configuration
- Core modules provide business logic and domain functionality
- This application module wires everything together into a cohesive system

The `@SpringBootApplication` annotation is configured to scan all relevant packages across modules, ensuring proper Spring component discovery and auto-configuration.
