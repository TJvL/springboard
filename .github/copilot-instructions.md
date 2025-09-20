# Copilot Instructions for Springboard

## Project Architecture

**Springboard** is a multi-module Kotlin/Gradle project implementing a data-driven inference engine with expression language processing. The architecture follows a layered approach:

- **`springboard-core`**: Core value types and utilities - implements a type-safe `Value` sealed class with immutable implementations for primitives, temporal values, currencies, and collections
- **`springboard-expression`**: ANTLR-based expression parser and evaluator - parses a domain-specific language for business rules and data transformations
- **`springboard-inference`**: Logic inference and reasoning engine - uses expression language for automatic truth maintenance and attribute inference

## General Coding Standards

### Code Organization

- **Split functionality**: Use interfaces and composition instead of large methods and huge files with many classes/objects
- **Many small files over large files** - prefer focused, single-responsibility files
- **One class per file** as a general rule, with exceptions for closely related types

### Data Handling

- **Use data classes** as much as possible for immutable data structures. This means leveraging Kotlin's `data class` for all data and separating behavior into services or utility classes and top-level functions.
- **Prefer immutability** - make properties `val` and collections read-only where possible
- **Immutable collections** - use `List`, `Set`, `Map` over mutable variants in public APIs

### Function Design

- **Top-level functions** for utilities or pure/static functions that don't need to be in a class
- **Pure functions** should be stateless and side-effect free when possible
- **Small, focused functions** over large, complex methods

### Exception Handling

- **Domain-specific exceptions**: Create custom exception types with structured data beyond just messages - include relevant context, error codes, and actionable information for upstream callers
- **Rich exception data**: Exception classes should carry typed properties (not just strings) that allow programmatic handling and recovery
- **Avoid generic exceptions**: Never let generic Java/Kotlin exceptions or third-party library exceptions leak to public APIs - wrap them in domain exceptions
- **Use Kotlin stdlib for simple cases**: For basic validation and precondition checking, prefer `require()`, `check()`, `error()`, and `requireNotNull()` over throwing exceptions directly
- **Exception hierarchies**: Organize domain exceptions in hierarchies under module-specific base exceptions (e.g., `InferenceException`, `ExpressionException`, `ValueException`)

### Factory and Builder Patterns

- **Static factory functions** on the type itself - add companion object methods to the class being created
- **Builder pattern** using inner class/object on the type being built
- **Example**: `MyClass.Builder()` or `MyClass.builder()` as inner class

### Testing Philosophy

- **Tests can be verbose and long** - prioritize clarity and comprehensive coverage over brevity
- **Descriptive test names** that clearly explain the scenario being tested
- **Comprehensive assertions** - test all relevant aspects of the behavior

### Documentation Standards

- **JavaDoc on public interfaces** - document all public classes, interfaces, and methods with proper KDoc/JavaDoc
- **Markdown for general documentation** - use `.md` files for architecture, guides, and specifications
- **Mermaid diagrams** for visual documentation - flowcharts, sequence diagrams, class relationships
- **Link markdown files** when needed - create cross-references between related documentation files

### Module Design and Package Organization

- **Clear public API** - each Gradle module should expose a well-defined public interface to external consumers
- **Careful package crafting** - organize packages to reflect module boundaries and access patterns
- **Proper access modifiers** - use `private`, `internal`, `protected`, and `public` correctly to enforce encapsulation
- **Public elements at package root** - facades, models, services, exceptions should be closer to package root (e.g., `dev.tjvl.springboard.core`)
- **Internal implementation deeper** - internal utilities, helpers, and implementation details should be in deeper package structures (e.g., `dev.tjvl.springboard.core.internal.arithmetic`)
- **Package-private by default** - prefer `internal` visibility for implementation details within modules

## Key Development Patterns

### Value System Architecture

- **`Value`** sealed class is **only for expression/inference contexts** - provides unified type system for expression language evaluation
- Individual value types are split across files: `PrimitiveValues.kt`, `TemporalValues.kt`, `CurrencyValue.kt`, etc.
- Factory methods in `Value.kt` companion object: `Value.of(42)`, `Value.listOf()`, `Value.optionalOf()`
- **Use regular Kotlin types everywhere else** - `String`, `Int`, `BigDecimal`, `LocalDate`, etc. to avoid type checking overhead
- Convert to `Value` only at expression/inference boundaries using factory methods

### Expression Language Integration

- **ANTLR grammar** files in `springboard-expression/src/main/antlr/` define the DSL syntax
- **Generated ANTLR sources** go to `build/generated-src/antlr/main/` (configured in build.gradle.kts)
- **ExpressionParser** provides high-level API: `evaluate(expression, variables)` and `parse(expression)` for reuse
- **ExpressionEvaluator** (visitor pattern) walks ANTLR parse trees and returns `Value` objects
- Expression language supports business calculations, collection operations, and inference rules (see `expression-language.md`)

### Inference Engine Pattern

- **InferenceEngine** maintains attribute registry with dependency tracking and automatic re-computation
- **Attributes** have states: `UNKNOWN`, `KNOWN`, `INFERRING`, `ERROR`
- **Rules** and **attribute expressions** drive inference via expression language evaluation
- **Circular dependency detection** prevents infinite loops during inference
- **Thread-safe** with read/write locks for concurrent access

## Essential Build Commands

```bash
# Build all modules and run quality checks
./gradlew build

# Generate ANTLR sources (expression module dependency)
./gradlew :springboard-expression:generateGrammarSource

# Apply code formatting and run static analysis
./gradlew formatAndLint
# or just formatting
./gradlew fix

# Run tests with detailed output
./gradlew test

# Build specific module
./gradlew :springboard-core:build
```

## Code Quality Standards

### Detekt Configuration

- **Comprehensive detekt.yml** with zero-tolerance policy (`maxIssues: 0`)
- **Forbidden suppressions** - most detekt rules cannot be suppressed, forcing proper fixes
- **AssertJ exclusive** - other assertion libraries are forbidden (see `ForbiddenImport` rules)
- **Strict naming** and **complexity limits** enforced across all modules

### Spotless Formatting

- **Kotlin code**: ktfmt with kotlinlang style
- **Build files**: Same ktfmt formatting for `.gradle.kts` files
- **Multiple formats**: ANTLR (`.g4`), JSON, Markdown, TOML, YAML all auto-formatted
- **Pre-commit integration** via `spotlessCheck` dependency in build process

### Dependency Management

- **Version catalog** in `gradle/libs.versions.toml` centralizes all dependency versions
- **Bundle definitions** for common dependency groups (e.g., `libs.bundles.test`)
- **Type-safe project accessors** enabled via `projects.springboardCore` syntax
- **Plugin aliases** via `alias(libs.plugins.kotlin.jvm)` pattern

### Version Control Standards

- **Conventional Commits** - all commit messages must strictly follow the [Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/#specification)
- **Format**: `<type>[optional scope]: <description>` with optional body and footer
- **Examples**: `feat(core): add currency value support`, `fix(expression): resolve parsing error for nested expressions`
- **Semantic Versioning** - project versioning follows [Semantic Versioning](https://semver.org/) (MAJOR.MINOR.PATCH)

## Module-Specific Guidelines

### springboard-core

- **Value implementations** are immutable data classes for expression/inference layer only
- **Use regular Kotlin types** (`String`, `Int`, `LocalDate`, etc.) for business logic and data models
- **Exception hierarchy** under `dev.tjvl.springboard.core.exception` with specific types
- **Convert at boundaries**: Transform regular types to `Value` objects only when entering expression evaluation
- **Arithmetic/comparison** operations delegated to separate utility classes within `Value` system

### springboard-expression

- **ANTLR task dependencies**: `generateGrammarSource` must complete before compilation and Spotless
- **Error handling**: Custom `ThrowingErrorListener` converts ANTLR errors to domain exceptions
- **Expression evaluation**: Visitor pattern with `ExpressionEvaluator` converting parse trees to `Value` objects
- **Variable contexts**: Support for dynamic variable binding during evaluation

### springboard-inference

- **State management**: Attributes transition through well-defined states with validation
- **Dependency analysis**: Automatic extraction of variable dependencies from expressions
- **Inference loops**: Iterative evaluation with progress tracking and max iteration limits
- **Thread safety**: All operations protected by read/write locks

## Testing Patterns

- **AssertJ assertions** exclusively (other assertion libraries forbidden by detekt)
- **JUnit 5** with parameterized tests for comprehensive value type coverage
- **Mockito** for dependency mocking in complex scenarios
- **Integration tests** validate ANTLR grammar with real expression parsing
- Test files organized to mirror main source structure
