# Springboard Inference Web Module

This module provides a web API layer for the Springboard inference engine, exposing inference capabilities through reactive web endpoints.

## Dependencies

### Internal Modules

- **springboard-inference**: Core inference engine and reasoning capabilities
- **springboard-expression**: Expression parsing and evaluation for inference rules
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

This module serves as the web interface to the inference engine, allowing clients to:

- Submit inference requests via REST API
- Define and manage inference rules through web endpoints
- Monitor inference execution and results
- Manage attribute definitions and relationships

The module leverages Spring WebFlux for reactive, non-blocking request processing, making it suitable for high-throughput inference workloads.
