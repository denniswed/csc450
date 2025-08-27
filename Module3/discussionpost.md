In this module, we will explore working with pointers in the C++ programming language. How are pointers used with arrays, and what are the benefits to utilizing pointers? Are arrays necessary when using a pointer? Additionally, what is the difference between the address-of operator and the dereference operator in C++? Be sure to provide an appropriate example to illustrate your viewpoints.

Pointers are essentially used to reference a specific memory space. They don't
need to reference an array, but the benefits of using a pointer become more apparent with arrays because you can use the pointer to iterate through the array directly.

& - address of - gives the memory address of the variable (in the case of an array,
is it giving the memory address the specific variable in the array)

- - dereference - gives the value of the variable at the provided memory space/
    pointer.

As an analogy, the "address of" is the same as asking what is the apartment number
of the person. The "derefertence" is asking who lives at a specific apartment
number.

References:

cppreference.com. (n.d.). Smart pointers. In Cppreference.com. Retrieved August 26, 2025, from https://en.cppreference.com/w/cpp/memory

Microsoft. (2021, August 3). Smart pointers (Modern C++). In Microsoft Learn. Retrieved August 26, 2025, from
https://learn.microsoft.com/en-us/cpp/cpp/smart-pointers-modern-cpp?view=msvc-170

Fertig, A. (2024, September 3). Understanding the inner workings of C++ smart pointers – The shared_ptr. Andreas Fertig blog. Retrieved August 26, 2025, from
https://andreasfertig.com/blog/2024/09/understanding-the-inner-workings-of-cpp-smart-pointers-the-shared_ptr/
