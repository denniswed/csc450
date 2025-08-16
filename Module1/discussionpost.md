In this module, we will be introduced to working with the C++ programming language. What are some of the issues that you had installing and running the Eclipse IDE for C/C++ developers? Working with the C++ language, discuss the different data types available. What are some of the differences, if any, between data types in C++ versus Java? What tips can be utilized to identify possible vulnerabilities using C++ data types? Be sure to provide an appropriate source code example to illustrate your points.

Hello Class!
There a few data types in C++ more common appears to be int (whole integer), double (ability to use decimal), char (characters), string (which is any character, number or symbol), boolean (true/false or 0/1). String is technically a user defined (as you can see the short program below).

One of the more clear differences between Java and C++ data types appears to be Java has fixed lengths for integer types, but C++ are implementation defined with some defaults. This directly relates to common security issues as with being able to define lengths or even have open ended, you can get overflow errors which can lead to exploitation.

Another issue is immutability. Strings are immutable in Java, they are not, by default in C++, which makes them susceptible to other kinds of exploits. A way around that is using "const" to declare them as a constant, which prevents them from being easily reassigned. The snippet below doesn't expose much, but it is an example of the data types using the 'const' construct for making the data types immutable.
