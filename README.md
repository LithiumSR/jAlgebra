# algebra_ws

A webservice to solve algebraic expressions

## JSON format
Specification:
- Each operation is a JSON object (let's call it _**AlgebraNode**_ from now on) composed of up to 4 parameters: type (enum), name (enum), op1 (AlgebraValue), op2 (AlgebraValue).
- Operands op1 and op2 are represented using a JSON object (_**AlgebraValue**_) composed by a 2 required parameters: _value_ and _literal_.
  - _value_ parameter in AlgebraValue is an integer or string
  - the _literal_ part is an array of JSON objects (_**LiteralPart**_).
    - LiteralPart object has two required parameters: _value_ (string) and _exponent_ (integer).
- An _**AlgebraNode**_ _type_ can be one of the following strings: "plus", "minus", "multiply", "divide" or "function" (case insensitive).
  - If _type_ is different than "function" then both _op1_ and _op2_ are required and must be either an AlgebraNode or ValueNode object. Otherwise, depending on the function, only _op1_ or both _op1_ and _op2_ are required.
  - When AlgebraNode type is "function" an optional "name" parameter is required. At this point, the _name_ parameter can either be "abs" or "sizeof".
  - sizeOf() function can work only on one operand that is an AlgebraValue with a string type value.
  - abs() function can work only on one operand that is an AlgebraValue with an integer type value (literals are allowed).
  
### Example

- (2*2a^2) + sizeof("hello")
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
More examples can be found [here](EXAMPLES.md)

## Build and deploy
In order to build and deploy the webservice use the following command:
```gradlew -PhttpPort=8888 appRunWar```

The appRunWar task will take care compiling a war file and deploying in a Tomcat9 instance accessible from ```http://localhost:PORT```

If you are only interested in building a war file use the command: ```gradlew war```. The output war can be found in the ```build/libs``` folder.

## How to use
1. Send a post request following the proposed specification at ```http://localhost:PORT/algebra/solver```. The body of the request must contain the JSON object representing the algebraic exprassion you wish to solve.
2. Read the response of the server (the body is a JSON object represeting the result of the algebraic expression)