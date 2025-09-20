# Expression Language Value Types

## Overview

This document defines all value types used in the Expression Language library for type-safe expression evaluation and data processing. The value types provide a unified interface that abstracts different data types while maintaining type safety and null safety throughout expression evaluation.

## Primitive Value Types

### Boolean

**Definition**: A logical value representing true or false.

**Characteristics**:

- Binary state only (true/false)
- No null or undefined state
- Used in conditional logic and expressions

**Examples**:

```text
true
false
```

### Integer

**Definition**: A signed 64-bit integer value.

**Characteristics**:

- Range: -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
- No decimal component
- Exact precision within range

**Examples**:

```text
0
42
-1000
9223372036854775807
```

### Decimal

**Definition**: An arbitrary-precision decimal number.

**Characteristics**:

- Implemented as BigDecimal to avoid precision loss
- Supports financial and scientific calculations
- No rounding errors in arithmetic operations
- Unlimited precision (implementation-dependent)

**Examples**:

```text
3.14159
-0.001
1000000.000001
0.333333333333333333333333
```

### Text

**Definition**: A sequence of UTF-8 encoded characters of arbitrary length.

**Characteristics**:

- Full Unicode support
- No practical length limit (implementation-dependent)
- Immutable once created
- Supports all international characters

**Examples**:

```text
"Hello, World!"
"DCM System"
"用户名"
"🎉 Celebration!"
""  // empty string
```

### Date

**Definition**: A calendar date without time component.

**Characteristics**:

- ISO-8601 format (YYYY-MM-DD)
- No timezone information
- Represents a full day (00:00:00 to 23:59:59)
- Range depends on implementation

**Examples**:

```text
2024-01-15
2000-12-31
1970-01-01
```

### DateTime

**Definition**: A specific point in time with date, time, and timezone information.

**Characteristics**:

- ISO-8601 format with timezone
- Microsecond precision
- Timezone-aware for global operations
- Unambiguous point in time

**Examples**:

```text
2024-01-15T14:30:00Z
2024-01-15T14:30:00+02:00
2024-01-15T14:30:00.123456-05:00
```

### Timestamp

**Definition**: A precise instant in time, typically represented as nanoseconds since Unix epoch.

**Characteristics**:

- ISO-8601 format in UTC (always ends with Z)
- Nanosecond precision
- Always in UTC timezone
- Unambiguous point in time globally

**Examples**:

```text
2024-01-15T14:30:00.123Z
2024-01-15T14:30:00Z
2024-01-15T14:30:00.123456789Z
```

### Period

**Definition**: A date-based amount of time, such as years, months, and days.

**Characteristics**:

- ISO-8601 period format
- Date-based components only (years, months, days)
- Can represent both positive and negative periods
- Used for date arithmetic

**Examples**:

```text
P1Y2M3D           // 1 year, 2 months, 3 days
P1Y               // 1 year
P30D              // 30 days
-P1M              // negative 1 month
```

### Duration

**Definition**: A time-based amount of time, such as hours, minutes, and seconds.

**Characteristics**:

- ISO-8601 duration format
- Time-based components only (hours, minutes, seconds)
- Can represent both positive and negative durations
- Used for time arithmetic

**Examples**:

```text
PT4H30M           // 4 hours, 30 minutes
PT1H              // 1 hour
PT30S             // 30 seconds
PT0.001S          // 1 millisecond
-PT2H             // negative 2 hours
```

### Currency

**Definition**: A monetary amount with associated currency code.

**Characteristics**:

- ISO 4217 currency codes
- Arbitrary precision for amount
- Supports negative values (debts, credits)
- Currency-aware arithmetic

**Examples**:

```text
USD100.00
EUR-50.50
JPY1000
GBP0.01
CHF1234.567
```

## Collection Types

### List

**Definition**: An ordered collection of values of the same type, allowing duplicates.

**Characteristics**:

- Maintains insertion order
- Allows duplicate values
- Zero-indexed access
- Dynamic size
- Homogeneous (all elements same type)

**Examples**:

```text
[1 2 3 4 5]
["apple" "banana" "apple"]
[true false true true]
[]  // empty list
```

### Set

**Definition**: An unordered collection of unique values of the same type.

**Characteristics**:

- No duplicate values
- No guaranteed order
- Fast membership testing
- Dynamic size
- Homogeneous (all elements same type)

**Examples**:

```text
#[1 2 3 4 5]
#["apple" "banana" "orange"]
#[true false]
#[]  // empty set
```

## Specialized Types

### Optional

**Definition**: A container that may or may not contain a value, providing explicit null handling. This type prevents any other types from having a null value and must be used for non-required values.

**Characteristics**:

- Represents the presence or absence of a value
- Type-safe null handling
- Prevents null pointer exceptions
- Must be explicitly checked before use

**Examples**:

```text
// Optional with value
Optional.of("hello")
Optional.of(42)

// Empty optional
Optional.empty()

// Usage in expressions
user.email.isPresent()
user.email.orElse("no-email@example.com")
```

### UUID

**Definition**: A universally unique identifier, typically used for entity identification.

**Characteristics**:

- 128-bit value
- Standardized format (8-4-4-4-12 hexadecimal digits)
- Globally unique with high probability
- Used for entity identification and references

**Examples**:

```text
550e8400-e29b-41d4-a716-446655440000
123e4567-e89b-12d3-a456-426614174000
f47ac10b-58cc-4372-a567-0e02b2c3d479
```

## Type Characteristics

### Type Safety

- **Strong typing**: No implicit type conversions
- **Type consistency**: Collections must contain same-type elements
- **Null handling**: Explicit null/empty handling required through the Optional type.

### Immutability

- All values are immutable once created
- Operations create new values rather than modifying existing ones
- Thread-safe by design

### Equality

- Value equality (not reference equality)
- Deep equality for collections
- Type-sensitive (no cross-type equality)

### Comparability

- Values that make sense to compare should be comparable
- Use Comparable interface
- Some cross-type comparability allowed (i.e. decimal vs integer)

### Arithmetic Compatibility

- **+Integer**: Result is Integer (unary plus operation)
- **-Integer**: Result is Integer (unary minus operation)
- **+Decimal**: Result is Decimal (unary plus operation)
- **-Decimal**: Result is Decimal (unary minus operation)
- **+Currency**: Result is Currency (unary plus operation)
- **-Currency**: Result is Currency (unary minus operation)
- **+Period**: Result is Period (unary plus operation)
- **-Period**: Result is Period (unary minus operation)
- **+Duration**: Result is Duration (unary plus operation)
- **-Duration**: Result is Duration (unary minus operation)
- **Integer + Decimal**: Result is Decimal
- **Integer + Integer**: Result is Integer
- **Decimal + Decimal**: Result is Decimal
- **Integer % Decimal**: Result is Decimal (modulo operation)
- **Integer % Integer**: Result is Integer (modulo operation)
- **Decimal % Decimal**: Result is Decimal (modulo operation)
- **Integer | sqrt**: Result is Decimal (square root operation)
- **Decimal | sqrt**: Result is Decimal (square root operation)
- **Integer | pow Integer/Decimal**: Result is Integer/Decimal (power operation)
- **Decimal | pow Integer/Decimal**: Result is Decimal (power operation)
- **Currency + Currency**: Only if same currency, result is Currency
- **Currency * Integer/Decimal**: Result is Currency (e.g., USD100.00 * 2 = USD200.00)
- **Currency / Integer/Decimal**: Result is Currency (e.g., USD100.00 / 2 = USD50.00)
- **Integer/Decimal * Currency**: Not allowed (would be ambiguous)
- **Integer/Decimal / Currency**: Not allowed (would be ambiguous)
- **Date/DateTime ± Period**: Result is Date/DateTime
- **DateTime/Timestamp ± Duration**: Result is DateTime/Timestamp
- **Period ± Period**: Result is Period
- **Duration ± Duration**: Result is Duration

### Collection Restrictions

- **No nested collections**: Collections cannot contain other collections
- **Type homogeneity**: All elements must be of the same type

## Implementation Considerations

### Precision Requirements

- **Decimal**: Use BigDecimal or equivalent
- **Currency**: Use specialized money libraries
- **DateTime/Timestamp**: Use timezone-aware implementations

### Performance Considerations

- **Immutability**: Consider persistent data structures
- **Collections**: Use efficient immutable implementations
- **Type checking**: Compile-time where possible

### Serialization

- **Compatibility**: All types must be serializable to different formats
- **ISO standards**: Use ISO formats for dates, times, currencies
- **Round-trip guarantee**: Serialization must be lossless
