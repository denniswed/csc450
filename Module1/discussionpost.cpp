#include <iostream>
#include <string>

int main() {
  // Different data types demonstration
  const std::string name = "John Doe";        // string type
  const std::string birthdate = "1990-05-15"; // string type for date
  const int age = 33;                         // integer type
  const double height = 5.9;                  // double type for decimal
  const char grade = 'A';                     // character type
  const bool isStudent = true;                // boolean type

  // Display the results
  std::cout << "Personal Information:" << std::endl;
  std::cout << "Name: " << name << std::endl;
  std::cout << "Birthdate: " << birthdate << std::endl;
  std::cout << "Age: " << age << " years old" << std::endl;
  std::cout << "Height: " << height << " feet" << std::endl;
  std::cout << "Grade: " << grade << std::endl;
  std::cout << "Is Student: " << (isStudent ? "Yes" : "No") << std::endl;

  return 0;
}