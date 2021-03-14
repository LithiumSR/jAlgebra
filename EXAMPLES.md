- 2 + 3
```
{
    "type": "plus",
    "op1": {
        "value": 2,
        "literal": []
    },
    "op2": {
        "value": 3,
        "literal": []
    }
}
```
- 2a - 3a 
```
{
    "type": "minus",
    "op1": {
        "value": 2,
        "literal": [{
                "value": "a",
                "exponent": 1
            }]
    },
    "op2": {
        "value": 3,
        "literal": [
            {
                "value": "a",
                "exponent": 1
            }
        ]
    }
}
```

- (3/a) + (sizeof("test")/b)
```
{
    "type": "plus",
    "op1": {
        "type": "divide",
        "op1": {
            "value": 3,
            "literal": []
        },
        "op2": {
            "value": 1,
            "literal": [
                {
                    "value": "a",
                    "exponent": 1
                }
            ]
        }
    },
    "op2": {
        "type": "divide",
        "op1": {
            "type": "function",
            "name": "sizeof",
            "op1": {
                "value": "test",
                "literal": []
            }
        },
        "op2": {
            "value": 2,
            "literal": [
                {
                    "value": "a",
                    "exponent": 1
                }
            ]
        }
    }
}
```
- 4/((2a/b)*(b/a))
```
{
    "type": "divide",
    "op1": {
        "value": 4,
        "literal": []
    },
    "op2": {
        "type": "multiply",
        "op1": {
            "type": "divide",
            "op1": {
                "value": 2,
                "literal": [
                    {
                        "value": "a",
                        "exponent": 1
                    }
                ]
            },
            "op2": {
                "value": 1,
                "literal": [
                    {
                        "value": "b",
                        "exponent": 1
                    }
                ]
            }
        },
        "op2": {
            "type": "divide",
            "op1": {
                "value": 1,
                "literal": [
                    {
                        "value": "b",
                        "exponent": 1
                    }
                ]
            },
            "op2": {
                "value": 1,
                "literal": [
                    {
                        "value": "a",
                        "exponent": 1
                    }
                ]
            }
        }
    }
}
```
