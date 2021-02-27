# algebra_ws

A webservice to solve algebraic expressions

## JSON format
Specification:
- Each operation is a JSON object (let's call it AlgebraNode from now on) composed by up to 4 parameters: type (enum), name (enum), op1 (AlgebraValue), op2 (AlgebraValue).
- Operands op1 and op2 are expressed using a JSON object (AlgebraValue) composed by a 2 required parameter: value and literal.
    - value parameter in AlgebraValue is an integer or string
    - the literal part is an array of JSON objects (LiteralPart).
        - LiteralPart object has two required parameters: value (string) and exponent (integer).
- An AlgebraNode type can be one of the following: plus, minus, multiply, divide or function
    - If type is different than "function" then both op1 and op2 are required and must be either an AlgebraNode or ValueNode objects. Otherwise, depending on the function, only op1 or both op1 and op2 are required.
    - When AlgebraNode type is "function" an optional "name" parameter is required. At this point, name parameter can either be "abs" or "sizeof".
    - SizeOf function can work only on one operand that is an AlgebraValue with a string type value.
    - Abs function can work only on one operand that is an AlgebraValue with a integer type value (literals are allowed).
    
### Example

- (2*2a^2) + sizeof("ciao")
```
{
    "type": "plus",
    "op1": {
        "type": "multiply",
        "op1": {
            "type": "plus",
            "op1": {
                "value": "2",
                "literal": []
            },
            "op2": {
                "value": "2",
                "literal": [
                    {
                        "value": "a",
                        "exponent": 2
                    }
                ]
            }
        }
    },
    "op2": {
        "type": "function",
        "name": "sizeof",
        "op1": {
            "value": "hello",
            "literal": []
        }
    }
}
```