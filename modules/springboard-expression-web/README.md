# Springboard Expression Web Module

This module provides a web API layer for the Springboard expression language, exposing expression parsing and evaluation capabilities through reactive web endpoints.

## Dependencies

### Internal Modules

- **springboard-expression**: ANTLR-based expression parsing and evaluation engine
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

This module serves as the web interface to the expression engine, allowing clients to:

- Parse and validate expressions via REST API
- Evaluate expressions with provided variable contexts
- Inspect expression syntax trees and dependencies
- Test expression logic through interactive endpoints

The module leverages Spring WebFlux for reactive, non-blocking request processing, making it suitable for high-throughput expression evaluation workloads.
