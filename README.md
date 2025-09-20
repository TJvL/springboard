# Springboard Project

A multi-module Kotlin/Gradle project implementing a data-driven inference engine with expression language processing. The architecture follows a layered approach with type-safe value handling, ANTLR-based expression parsing, and automatic attribute inference.

## Project Structure

```text
springboard/
├── docs/                           # Project documentation
│   ├── architecture/               # Architecture documentation and diagrams
│   ├── expression-language.md      # Expression language specification
│   └── *.md                        # Additional documentation files
├── gradle/
│   ├── libs.versions.toml          # Version catalog for dependency management
│   └── wrapper/                    # Gradle wrapper files
├── modules/                        # All project modules
│   ├── springboard-core/           # Core value types module
│   │   ├── src/main/kotlin/        # Core value system and utilities
│   │   └── src/test/kotlin/        # Core module tests
│   ├── springboard-expression/     # Expression parsing and evaluation module
│   │   ├── src/main/kotlin/        # Expression evaluator and parser
│   │   ├── src/main/antlr/         # ANTLR grammar files
│   │   └── src/test/kotlin/        # Expression module tests
│   ├── springboard-inference/      # Logic inference engine module
│   │   ├── src/main/kotlin/        # Inference engine and attribute management
│   │   └── src/test/kotlin/        # Inference module tests
│   └── springboard-specification/  # BDD integration testing module
│       ├── src/test/kotlin/        # Cucumber step definitions
│       └── src/test/resources/     # Gherkin feature files
├── .github/
│   └── copilot-instructions.md     # GitHub Copilot coding guidelines
├── build.gradle.kts                # Root build configuration
├── settings.gradle.kts             # Project settings and module inclusion
├── detekt.yml                      # Static analysis configuration
├── gradlew                         # Gradle wrapper (Unix)
└── gradlew.bat                     # Gradle wrapper (Windows)
```

## Modules

### springboard-core

- **Package**: `dev.tjvl.springboard.core`
- **Description**: Core value types and utilities - implements a type-safe `Value` sealed class with immutable implementations for primitives, temporal values, currencies, and collections. The `Value` system is designed specifically for expression/inference contexts, while regular Kotlin types are used elsewhere.
- **Key Features**: Immutable value types, type-safe operations, currency handling with JavaMoney

### springboard-expression

- **Package**: `dev.tjvl.springboard.expression`
- **Description**: ANTLR-based expression parser and evaluator - parses a domain-specific language for business rules and data transformations. Provides high-level APIs for expression evaluation with automatic `Value` conversion.
- **Key Features**: ANTLR 4 grammar, visitor-based evaluation, variable binding, error handling

### springboard-inference

- **Package**: `dev.tjvl.springboard.inference`
- **Description**: Logic inference and reasoning engine - uses expression language for automatic truth maintenance and attribute inference. Provides thread-safe attribute management with dependency tracking and circular dependency detection.
- **Key Features**: Attribute state management, dependency analysis, inference loops, thread safety

### springboard-specification

- **Package**: `dev.tjvl.springboard.specification`
- **Description**: Behavior-Driven Development (BDD) integration testing module using Cucumber and Gherkin feature files. Provides end-to-end testing of all modules working together with human-readable specifications.
- **Key Features**: Gherkin feature files, Cucumber step definitions, end-to-end integration testing, no mocking (real implementations only)

## Documentation

Comprehensive documentation is available in the `docs/` folder:

- **Architecture documentation** with diagrams and design decisions
- **Expression language specification** detailing syntax and capabilities
- **Module-specific guides** and API documentation
- **Development guidelines** and coding standards

See the [documentation folder](docs/) for detailed information about the project architecture and usage.

## Dependencies

All dependency versions are managed through the version catalog (`gradle/libs.versions.toml`):

## Building the Project

### Build all modules and run quality checks

```bash
./gradlew build
```

### Generate ANTLR sources and format code

```bash
./gradlew :springboard-expression:generateGrammarSource
./gradlew formatAndLint
```

### Run tests with detailed output

```bash
./gradlew test
```

### Run BDD specifications

```bash
./gradlew :modules:springboard-specification:test
```

### Build specific module

```bash
./gradlew :modules:springboard-core:build
./gradlew :modules:springboard-expression:build
./gradlew :modules:springboard-inference:build
./gradlew :modules:springboard-specification:build
```

### Apply formatting and run static analysis

```bash
./gradlew formatAndLint
# or just formatting
./gradlew fix
```

## Development Workflow

The project follows these key development patterns:

1. **Value System**: Use `Value` types only for expression/inference contexts, regular Kotlin types elsewhere
2. **Expression Language**: ANTLR-based DSL for business rules and calculations
3. **Inference Engine**: Automatic attribute computation with dependency tracking
4. **Immutability**: Prefer immutable data structures and pure functions
5. **Module Boundaries**: Clear public APIs with proper encapsulation

## Getting Started

1. Ensure you have Java 21 installed
2. Clone the repository
3. Run `./gradlew build` to build all modules and verify setup
4. Run `./gradlew test` to execute all tests
5. Explore the [documentation](docs/) for detailed architecture information

The Gradle wrapper is included, so you don't need to install Gradle separately.
