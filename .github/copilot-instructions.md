# GitHub Copilot Instructions for DCM Stack Monorepository

## Project Overview
This is the main monorepository for the complete Dynamic Case Management (DCM) stack implementation. This repository contains all the code, configuration, and infrastructure for the DCM system defined in the design documentation.

## Architecture Overview
The DCM system follows a **two-monolithic-application design** with distinct responsibilities:

### Application Structure
- **Sentinel** (Gateway Service): Backend-for-frontend security and session management
- **Casey** (Case Operations): Complete DCM functionality and business logic
- **Stow Away** (Data Layer): High-performance data persistence and caching
- **Shared Libraries**: Common domain models, utilities, and configuration

### Technology Stack
- **Language**: Java 21 LTS
- **Framework**: Spring Boot 3.3+ with full reactive stack
- **Build Tool**: Gradle 8.5+ with Kotlin DSL
- **Web Framework**: Spring WebFlux (reactive)
- **Security**: Spring Security + Keycloak (OAuth2/OIDC)
- **Data Access**: Hibernate Reactive
- **Messaging**: Spring Kafka
- **Expression Engine**: ANTLR 4.13+
- **Database**: PostgreSQL with JSONB
- **Caching**: Redis (reactive)
- **File Storage**: MinIO (S3-compatible)
- **Version Control**: Git (for case type models)

## Communication Architecture

### Inter-Application Communication
- **Primary**: Apache Kafka with Spring Kafka for event-driven communication
- **Session Management**: Shared Redis-based sessions accessible by both Sentinel and Casey
- **Data Access**: Unified reactive protocol through Stow Away

### Communication Flows
1. **User Authentication**: UI → Sentinel → Keycloak → Session in Redis
2. **Authenticated Requests**: UI → Sentinel → Casey (both read shared session)
3. **Data Operations**: Casey → Stow Away (via Kafka, session-agnostic)
4. **Session Updates**: Casey → Redis → Real-time sync to Sentinel

## Data Storage Strategy

### PostgreSQL with JSONB
- **All case data** stored in PostgreSQL using JSONB for flexible schemas
- **Hibernate Reactive** for full async ORM operations
- **Core tables**: Cases, users, tasks with fixed schema
- **Dynamic data**: Runtime case data in JSONB columns based on YAML case type definitions

### Redis Caching and Sessions
- **Hot case caching**: Frequently accessed case data
- **Shared session storage**: User sessions accessible by Sentinel and Casey
- **Query result caching**: Complex queries and computed data
- **Real-time notifications**: Pub/Sub for session updates

### MinIO Binary Storage
- **S3-compatible API** with reactive client integration
- **File attachments**: Documents, images, and binary data
- **Streaming support**: Large file upload/download
- **Presigned URLs**: Secure direct client access

### Git-Based Case Type Management
- **File-based case types**: YAML definitions stored in Git remote repositories
- **Version control**: Complete history and branching for case type models
- **Hot reload**: Casey monitors Git changes and reloads case types
- **Environment management**: Different branches for dev/test/prod

## Code Organization

### Multi-Module Gradle Project
```
/
├── gradle/                    # Gradle wrapper and build scripts
├── sentinel/                  # Gateway service application
│   ├── src/main/java/        # Sentinel source code
│   └── build.gradle.kts      # Sentinel dependencies and config
├── casey/                     # Case operations application
│   ├── src/main/java/        # Casey source code
│   └── build.gradle.kts      # Casey dependencies and config
├── stow-away/                 # Data layer application
│   ├── src/main/java/        # Stow Away source code
│   └── build.gradle.kts      # Stow Away dependencies and config
├── shared-models/             # Common domain models and DTOs
├── shared-config/             # Common configuration and utilities
├── infrastructure/            # Docker Compose, K8s configs
├── case-type-models/          # Sample case type YAML definitions
└── build.gradle.kts          # Root build configuration
```

## Development Guidelines

### Reactive Programming with Spring WebFlux
- **Non-blocking operations**: Use reactive streams throughout
- **Proper backpressure**: Handle backpressure between services
- **Error handling**: Use reactive operators for error management
- **Testing**: Utilize WebTestClient and reactive test frameworks

### Expression Language
- **ANTLR-based**: Custom grammar for case-specific expressions
- **Context-aware**: Evaluation with case data and external APIs
- **Type-safe**: Strong typing with validation
- **Performance**: Compiled expressions cached for reuse

### Session Management
- **Shared Redis store**: Both Sentinel and Casey access same session data
- **Session structure**: User identity, roles, permissions, case contexts
- **Optimistic locking**: For concurrent session modifications
- **TTL management**: Automatic cleanup with expiration

### Case Type Model Management
- **YAML structure**: Follows specification in design/concepts/case-type-modeling.md
- **Git integration**: Casey pulls from remote repositories
- **Validation**: YAML structure and case type integrity checks
- **Hot reload**: Detect changes and reload without restart

## Security Architecture

### Keycloak Integration
- **OIDC authentication**: OAuth 2.0 and OpenID Connect
- **SSO support**: Single sign-on across applications
- **Role management**: Fine-grained authorization with realm roles
- **Token validation**: JWT validation and refresh in Sentinel

### Application Security
- **Sentinel**: OIDC flow, session management, request routing
- **Casey**: Session access, DCM operations, business logic security
- **Stow Away**: Session-agnostic, data-level security, audit logging

## Build and Deployment

### Gradle Configuration
- **Multi-module**: Shared dependencies and version management
- **Spring Boot plugin**: Optimized builds and container images
- **Native compilation**: GraalVM support for minimal footprint
- **Development mode**: Hot reload capabilities

### Infrastructure Requirements
- **Java 21 runtime** (or GraalVM native images)
- **PostgreSQL cluster** for all case data
- **Redis cluster** for caching and sessions
- **MinIO cluster** for binary storage
- **Kafka cluster** for messaging
- **Keycloak instance** for identity management
- **Git repositories** for case type models

### Cloud-Native Deployment
- **Container images**: Optimized Spring Boot containers
- **Kubernetes**: Service orchestration with service mesh
- **Monitoring**: Micrometer + Prometheus via Spring Boot Actuator
- **Health checks**: Liveness/readiness endpoints

## Code Quality Standards

### Java 21 Features
- **Virtual threads**: For improved concurrency
- **Pattern matching**: Enhanced switch expressions
- **Records**: Immutable data classes
- **Sealed classes**: Restricted inheritance hierarchies

### Spring Boot Best Practices
- **Reactive principles**: Non-blocking throughout the stack
- **Configuration externalization**: Environment-specific properties
- **Actuator endpoints**: Health checks and metrics
- **Auto-configuration**: Leverage Spring Boot starters

### Testing Strategy
- **Unit tests**: JUnit 5 with reactive test support
- **Integration tests**: TestContainers for database/Redis/Kafka
- **Contract tests**: API contract verification
- **End-to-end tests**: Full workflow testing

## Development Workflow

### Branch Strategy
- **main**: Production-ready code
- **develop**: Integration branch for features
- **feature/***: Individual feature development
- **hotfix/***: Production bug fixes

### Case Type Model Development
- **Separate repositories**: Case type models in dedicated Git repos
- **Environment branches**: dev/test/prod for different environments
- **Casey integration**: Automatic pulling and validation
- **Versioning**: Git tags for case type model releases

## Monitoring and Observability

### Metrics and Logging
- **Micrometer**: Application metrics collection
- **Structured logging**: JSON format with correlation IDs
- **Distributed tracing**: Request tracing across applications
- **Alerting**: Integration with monitoring systems

### Performance Monitoring
- **Reactive streams**: Monitor backpressure and throughput
- **Database performance**: Query optimization and connection pooling
- **Cache efficiency**: Redis hit rates and eviction policies
- **Session lifecycle**: Session creation, access, and cleanup

## Common Patterns

### Event-Driven Communication
- **Domain events**: Well-defined event schemas
- **Kafka topics**: Organized by domain (case-events, user-events)
- **Event sourcing**: Optional for audit requirements
- **Error handling**: Dead letter queues and retry policies

### Data Access Patterns
- **Repository pattern**: Reactive repositories with Spring Data
- **Cache-aside**: Smart caching strategies in Stow Away
- **CQRS**: Command and query separation where beneficial
- **Transaction management**: Reactive transaction boundaries

### Security Patterns
- **JWT tokens**: Stateless authentication with session enhancement
- **Role-based access**: Fine-grained permissions
- **Audit logging**: Complete audit trail for compliance
- **Data encryption**: Sensitive data protection

## Integration Points

### External Systems
- **REST APIs**: Reactive WebClient for external service calls
- **Database integrations**: JDBC reactive drivers
- **File systems**: Reactive file I/O operations
- **Message queues**: Kafka integration patterns

### UI Integration
- **SPA support**: Single-page application backends
- **API-first**: OpenAPI specifications
- **Real-time updates**: WebSocket or Server-Sent Events
- **Progressive enhancement**: Graceful degradation patterns

## Performance Considerations

### Reactive Performance
- **Connection pooling**: Optimized pool sizes for reactive connections
- **Backpressure management**: Proper flow control
- **Memory management**: Avoid memory leaks in reactive chains
- **CPU utilization**: Efficient use of virtual threads

### Caching Strategy
- **Multi-level caching**: Application and database level
- **Cache invalidation**: Event-driven cache updates
- **Cache warming**: Proactive cache population
- **Cache monitoring**: Performance metrics and optimization

## Development Environment Setup

### Prerequisites
- Java 21 JDK
- Docker and Docker Compose
- Git
- IDE with Spring Boot support (IntelliJ IDEA recommended)

### Local Development
- **Docker Compose**: Local infrastructure (PostgreSQL, Redis, Kafka, MinIO, Keycloak)
- **Hot reload**: Development mode with automatic restart
- **Test data**: Seed data for development and testing
- **Mock services**: Local mocks for external dependencies

## Documentation Standards

### Code Documentation
- **JavaDoc**: Comprehensive API documentation
- **README files**: Module-specific setup and usage
- **Architecture decisions**: ADR (Architecture Decision Records)
- **API documentation**: OpenAPI/Swagger specifications

### Design Documentation
- **Reference implementation**: Code that follows design specifications
- **Integration with design repo**: Links to design documentation
- **Change management**: Updates to reflect implementation decisions
- **Examples**: Working examples and sample case types

Remember: This is the main implementation repository for the DCM stack. Always refer to the design documentation in the `/design` submodule for architectural guidance and specifications.
