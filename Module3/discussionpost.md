In this module, we will explore working with pointers in the C++ programming language. How are pointers used with arrays, and what are the benefits to utilizing pointers? Are arrays necessary when using a pointer? Additionally, what is the difference between the address-of operator and the dereference operator in C++? Be sure to provide an appropriate example to illustrate your viewpoints.

Pointers are essentially used to reference a specific memory space. They don't need to reference an array, but the benefits of using a pointer become more apparent with arrays because you can use the pointer to iterate through the array directly.

## Operators

- `&` - **address of** - gives the memory address of the variable
- `*` - **dereference** - gives the value of the variable at the provided memory space/pointer

As an analogy, the "address of" is the same as asking what is the apartment number of the person. The "dereference" is asking who lives at a specific apartment number.

## Example

The below code gives an example of using a pointer to access variable's value and memory address space. It is straightforward and you can use the pointer to replace the value directly. In the case of `*p` you are setting it equal to the address space of the array `arr`. When you reference `*p` you are going to get the reference of the first value in the array, but because it is tied to the address space by incrementing p by 1 `*(p + 1)` you are telling it to retrieve the value of the second item in the array.

Similarly, the `*px` is equal to the memory space that `x` is assigned (by using `&x`). This dereferences or tells you where the value is stored in memory, as you can see in the below example printing `px` (which is equal to `&x`) gives you a memory address space, but printing `*px` it is giving you the value. When you set `*px = 100`, you are then setting the value of `x` to a new value.

## Reference

Microsoft. (n.d.). Pointers (C++). Microsoft Docs. https://docs.microsoft.com/en-us/cpp/cpp/pointers-cpp
