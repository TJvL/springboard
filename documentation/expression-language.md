# Expression Language

## Overview

The Expression Language is a powerful, declarative domain-specific language (DSL) for evaluating data and computing results. It provides a type-safe, null-safe expression evaluation system with support for arithmetic, boolean logic, collection operations, and data transformations.

## Definition

**Expression Language**: A domain-specific language (DSL) that evaluates data to compute results, perform calculations, and make logical decisions. It provides a unified way to express business logic, data transformations, and conditional operations in a declarative manner.

## Null Safety and Optional Values

**CRITICAL**: The Expression Language is strictly null-safe. No expression can ever return `null` or accept `null` as input. All potentially missing or undefined values must be explicitly handled using the `Optional` type.

### Null Safety Rules

1. **No null inputs**: Variables passed to expressions must never be `null`
2. **No null outputs**: Expressions must never return `null` values
3. **Optional required**: Use `Optional<T>` for any value that might be absent
4. **Explicit handling**: Missing values must be explicitly handled with Optional operations
5. **Type safety**: All operations must be type-safe with no implicit null conversions

### Optional Usage

```text
// Correct - using Optional for potentially missing values
user.email.isPresent()                    // Check if email exists
user.email.orElse("no-email@example.com") // Provide default
user.email.map(e -> e.toUpperCase())      // Transform if present

// Incorrect - these patterns are forbidden
user.email == null                        // null comparison not allowed
user.email ?: "default"                   // null coalescing not supported
if (user.email) { ... }                   // null checking not supported
```

## Value Types

The Expression Language supports a comprehensive type system. For detailed specifications, see [Value Types Documentation](value-types.md).

### Supported Types

- **Primitives**: Boolean, Integer, Decimal, Text, Date, DateTime, Timestamp, Period, Duration, Currency
- **Collections**: List, Set
- **Specialized**: Optional, UUID

All values are immutable and strongly typed with no implicit conversions. **No type can ever be null** - use Optional for potentially missing values.

## Expression Syntax

### Literal Values

Literal values allow inline definition of values within expressions. **All literals represent actual values - never null**.

**Input Requirements**: None (literals are self-contained)
**Output Guarantees**: Always returns the exact value specified, never Optional

#### Text Literals

```text
"any amount of UTF-8 characters"
"Hello, World!"
"Special chars: \n \t \""
```

**Output**: Always returns Text value, never Optional

#### Numeric Literals

```text
// Integer - no leading zeros except for 0
42
0
-1000

// Decimal - no leading/trailing zeros except 0.x
3.14159
0.001
-42.5
```

**Output**: Always returns Integer or Decimal value, never Optional

#### Boolean Literals

```text
true
false
```

**Output**: Always returns Boolean value, never Optional

#### Date and Time Literals

```text
// Date (ISO-8601)
2024-01-15

// DateTime with timezone (ISO-8601)
2024-01-15T14:30:00Z
2024-01-15T14:30:00+02:00

// Timestamp (ISO-8601 UTC instant)
2024-01-15T14:30:00.123Z
2024-01-15T14:30:00Z
```

**Output**: Always returns Date/DateTime/Timestamp value, never Optional

#### Period Literals

```text
// ISO-8601 duration format
P1Y2M3D        // 1 year, 2 months, 3 days
PT4H30M        // 4 hours, 30 minutes
P1DT12H        // 1 day and 12 hours
```

**Output**: Always returns Period value, never Optional

#### Currency Literals

```text
// Currency code followed by amount
USD100.00
EUR-50.50
JPY1000
```

**Output**: Always returns Currency value, never Optional

#### Collection Literals

```text
// List - ordered, allows duplicates
[1 1 3 4]
["apple" "banana" "apple"]

// Set - unordered, unique values only
#[1 2 3]
#["apple" "banana" "orange"]
```

**Output**: Always returns List/Set value (can be empty), never Optional or null

### Variable References

Variables reference values in the evaluation context. **All variables must have actual values - no null variables allowed**.

**Input Requirements**: Variable must exist and have a non-null value (use Optional if value might be missing)
**Output Guarantees**: Returns the variable's value, which can be Optional but never null

```text
// Variable naming rules:
// - Must start with alphabetic character
// - Can contain alphanumeric, underscore, or dot
// - Must end with alphabetic character

validNames:
  userName                    // Required: Text value
  user_id                     // Required: UUID or Text value
  user.profile.name           // Required: Text value
  account.createdDate         // Required: DateTime value
  user.email                  // Can be: Optional<Text>
  user.profile.middleName     // Can be: Optional<Text>

invalidNames:
  123invalid    // starts with number
  name_         // ends with underscore
  _private      // starts with underscore
```

## Operators

### Arithmetic Operators

Arithmetic operations on numeric types. **All operands must be non-null values**.

**Input Requirements**: Both operands must be actual numeric values (Integer, Decimal, Currency)
**Output Guarantees**: Always returns a numeric result, never Optional

```text
// Unary operations
+42            // unary plus: 42 (Integer)
-42            // unary minus: -42 (Integer)
+3.14          // unary plus: 3.14 (Decimal)
-3.14          // unary minus: -3.14 (Decimal)
+USD100.00     // unary plus: USD100.00 (Currency)
-USD100.00     // unary minus: USD-100.00 (Currency)
+P1D           // unary plus: P1D (Period)
-P1D           // unary minus: -P1D (Period)
+PT2H          // unary plus: PT2H (Duration)
-PT2H          // unary minus: -PT2H (Duration)

// Basic arithmetic
10 + 5         // addition: 15
10 - 5         // subtraction: 5
10 * 5         // multiplication: 50
10 / 5         // division: 2
2 ^ 3          // power: 8
10 % 3         // modulo: 1

// Arithmetic precedence with parentheses
(10 + 5) * 2   // 30 (addition first, then multiplication)
10 + 5 * 2     // 20 (multiplication first, then addition)
(2 + 3) ^ 2    // 25 (addition first, then power)
2 + 3 ^ 2      // 11 (power first, then addition)
(100 - 20) / 4 // 20 (subtraction first, then division)
100 - 20 / 4   // 95 (division first, then subtraction)

// Complex nested parentheses
((10 + 5) * 2) - (3 ^ 2)     // 21 ((15 * 2) - 9)
(USD100.00 + USD50.00) / 3   // USD50.00 (currency addition first, then division)
-(10 + 5)                    // -15 (unary minus applied to parenthesized expression)

// Advanced arithmetic
25 | sqrt      // square root: 5.0
16.0 | sqrt    // square root: 4.0
2 | pow 8      // power: 256
3.5 | pow 2    // power: 12.25

// Mixed Integer/Decimal (result is Decimal)
10 + 5.5       // 15.5
20 / 3         // 6.666...
10 % 3.5       // 3.0 (10 - 2 * 3.5)

// Currency arithmetic (same currency only)
USD100.00 + USD50.00    // USD150.00
EUR100.00 - EUR25.50    // EUR74.50

// Currency multiplication and division by numbers
USD100.00 * 2           // USD200.00
EUR50.00 * 1.5          // EUR75.00
USD100.00 / 4           // USD25.00
GBP200.00 / 2.5         // GBP80.00

// Date/Period arithmetic
2024-01-15 + P1D         // 2024-01-16
2024-01-15T10:00:00Z - PT2H  // 2024-01-15T08:00:00Z
P1D + PT12H              // P1DT12H
```

**Precedence Rules**:
1. **Parentheses `()`**: Highest precedence - expressions in parentheses are evaluated first
2. **Unary operators `+`, `-`**: Applied to single operands
3. **Power `^`**: Right-associative (2^3^2 = 2^(3^2) = 512)
4. **Multiplication `*`, Division `/`, Modulo `%`**: Left-associative
5. **Addition `+`, Subtraction `-`**: Left-associative

### Text Operations

Text manipulation operations. **All operands must be non-null text values**.

**Input Requirements**: All operands must be actual Text values
**Output Guarantees**: Always returns a Text result for transformations, Boolean for checks, never Optional

```text
// Text concatenation
"Hello" + " " + "World"     // "Hello World"
"User: " + userName         // "User: John"
firstName + " " + lastName  // "John Doe"

// Text transformations using pipe operator
"\tHello, World!   " | trim        // "Hello, World!"
"HELLO" | lowercase                // "hello"
"hello" | uppercase                // "HELLO"

// Text checks using pipe operator (returns Boolean)
person.name | contains "Bobby"     // true or false
"test string" | contains "str"     // true

// Chaining text operations
"Helmond" | lowercase | contains "hel"   // true
"  MIXED case  " | trim | lowercase      // "mixed case"
userName | uppercase | contains "ADMIN"  // check if username contains "ADMIN"
```

**Operation Details**:
- `trim`: Removes leading and trailing whitespace - Input: Text (required), Output: Text (required)
- `lowercase`: Converts text to lowercase - Input: Text (required), Output: Text (required)
- `uppercase`: Converts text to uppercase - Input: Text (required), Output: Text (required)
- `contains`: Checks if text contains substring - Input: Text (required) + Text (required), Output: Boolean (required)

### Boolean Operators

Logical operations for boolean expressions. **All operands must be actual boolean values**.

**Input Requirements**: All operands must be actual Boolean values or comparison results
**Output Guarantees**: Always returns Boolean result, never Optional

```text
// Logical operators
not true               // false
true and false         // false
true or false          // true

// Comparison operators (same type required)
5 == 5                 // true
"hello" != "world"     // true
10 > 5                 // true
3 < 7                  // true
5 >= 5                 // true
3 <= 2                 // false

// Date comparisons
2024-01-15 > 2024-01-14     // true
2024-01-15T10:00:00Z < 2024-01-15T11:00:00Z  // true

// Timestamp comparisons
2024-01-15T10:15:30.123Z > 2024-01-15T10:15:30.000Z  // true

// Period comparisons
PT2H > PT1H            // true
P1D < P2D              // true

// Collection equality
[1 2 3] == [1 2 3]  // true (order matters)
#[1 2 3] == #[3 2 1]  // true (sets ignore order)
```

## Collection Operations

Collections support chainable operations using the pipe operator (`|`). **All collection operations preserve null safety**.

**Input Requirements**: Starting collection must be actual List/Set value (can be empty, never null)
**Output Guarantees**: Operations return actual values or Optional results as specified

### Basic Operations

```text
// distinct - removes duplicates, returns set
[1 1 2 3 3] | distinct     // #[1 2 3]
// Input: List<T> (required), Output: Set<T> (required)

// sort - orders elements
[3 1 4 1 5] | sort >        // [1 1 3 4 5] ascending
[3 1 4 1 5] | sort <        // [5 4 3 1 1] descending
// Input: List<T> (required), Output: List<T> (required)

// count - returns number of elements
[1 2 3 4 5] | count         // 5
#["a" "b" "c"] | count        // 3
// Input: List<T> or Set<T> (required), Output: Integer (required)

// exists - checks if element exists
[1 2 3 4 5] | exists 3        // true
["apple" "banana"] | exists "orange"  // false
#[1 2 3] | exists 2          // true
// Input: Collection (required) + Element (required), Output: Boolean (required)

// get - retrieves by index (Always returns an Optional)
[1 2 3] | get 0               // Optional.of(1) (by index)
[1 2 3] | get 10              // Optional.empty() (index out of bounds)
// Input: Collection (required) + Index (required), Output: Optional<T> (required)

// text - converts all elements to text
[1 2 3] | text                // ["1" "2" "3"]
// Input: Collection<T> (required), Output: Collection<Text> (required)

// merge - merges parameter collection with starting collection
[1 2] | merge [3 4]            // [1 2 3 4]
#[1 2] | merge #[2 3]          // #[1 2 3]
// Input: Collection<T> (required) + Collection<T> (required), Output: Collection<T> (required)
```

### Chaining Operations

Operations can be chained when types match. **All intermediate results are guaranteed non-null**.

**Input Requirements**: Each operation in chain requires previous result to be non-null
**Output Guarantees**: Final result follows the output guarantee of the last operation

```text
// Complex chains with literals
[1 1 2 3 3 4]
  | distinct           // #[1 2 3 4] - required Set<Integer>
  | sort >             // [1 2 3 4] - required List<Integer>
  | count              // 4 - required Integer

// Using variable references as starting points
documents
  | exists "passport"  // Boolean (required) - check if passport document exists

properties
  | exists "priority"  // Boolean (required) - check if priority property is set

// Variables in expression parameters
input_documents
  | filter (d -> d.verified == true)
  | count              // Integer (required) - count verified documents from input parameter

// Complex variable-based chains
user_tasks
  | filter (t -> t.status == "pending")
  | sort t.priority
  | get 0              // Optional<Task> - get highest priority pending task (might be empty)

// Text processing
task_names
  | distinct           // Set<Text> (required) - remove duplicate names
  | sort >             // List<Text> (required) - alphabetical order
  | count              // Integer (required) - count unique names
```

### Optional Handling in Collections

When dealing with Optional values in collections, explicit handling is required:

```text
// Working with Optional elements - these patterns show proper handling
users | filter (u -> u.email.isPresent())  // Filter users who have email
users | map (u -> u.email.orElse("no-email"))  // Convert Optional to actual values

// Invalid patterns - these are forbidden
users | filter (u -> u.email != null)      // null comparison not allowed
users | map (u -> u.email ?: "default")    // null coalescing not supported
```

## Examples

### Financial Calculations

```text
// Calculate loan eligibility based on criteria
// Input: All fields required (non-null), credit_score can be Optional<Integer>
// Output: Boolean (required) - never null, always true or false
loan_eligible:
  client.credit_score.map(score -> score > 650).orElse(false) and
  client.monthly_income * 0.3 >= loan.monthly_payment and
  client.debt_to_income < 0.43

// Calculate processing fee based on amount
// Input: loan.amount required Currency value
// Output: Currency (required) - never null, always a currency amount
processing_fee:
  loan.amount < USD10000.00 and USD100.00 or
  loan.amount < USD50000.00 and USD250.00 or
  USD500.00
```

### Collection Processing

```text
// Get unique document types
// Input: documents required List (can be empty)
// Output: Set<Text> (required) - never null, can be empty set
unique_doc_types:
  documents
  | get "type"         // Returns Optional<Text> for each document
  | filter (t -> t.isPresent())  // Keep only documents with types
  | map (t -> t.get())  // Extract the actual type values
  | distinct

// Check if high-priority task exists
// Input: tasks required List (can be empty)
// Output: Boolean (required) - never null
has_high_priority:
  tasks
  | filter (t -> t.priority == "high")
  | count > 0

// Count completed items
// Input: items required List (can be empty)
// Output: Integer (required) - never null, can be 0
completed_items:
  items
  | filter (item -> item.completed == true)
  | count

// Check if specific configuration exists
// Input: configuration required collection
// Output: Boolean (required) - never null
has_auto_config:
  configuration | exists "auto_process"

// Verify required fields are present
// Input: record required object with Optional fields
// Output: Boolean (required) - never null
all_required_fields_present:
  ["name" "email" "phone"]
  | all (field -> record | get field | isPresent())
```

### Date and Period Operations

```text
// Calculate age between dates
// Input: both dates required (non-null DateTime values)
// Output: Duration (required) - never null
age:
  current_date - created_date

// Check if within time limit
// Input: age required Duration
// Output: Boolean (required) - never null
within_limit:
  age < P5D

// Calculate next scheduled date
// Input: last_date can be Optional<DateTime>
// Output: Optional<DateTime> - explicit handling of missing last date
next_date:
  last_date.map(date -> date + P30D)

// Calculate elapsed time
// Input: both times required (non-null DateTime values)
// Output: Decimal (required) - never null
elapsed_hours:
  (end_time - start_time) / PT1H
```
