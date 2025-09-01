#include <iostream>
#include <limits> // for std::numeric_limits
#include <memory> // for std::unique_ptr, std::make_unique

int oldway() {
  std::cout << "Using raw pointers for dynamic memory management (old way).\n";
  int a{}, b{}, c{}; // Initialize to 0 to prevent undefined behavior

  // Input validation loop to ensure safe integer input
  std::cout << "Enter three integer values separated by spaces: \n";
  std::cout << "(Input validation in place to ensure integers are entered) \n";
  std::cout << "(range: -2147483648 to 2147483647)\n";
  while (!(std::cin >> a >> b >> c)) {
    std::cin.clear(); // clear error state
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(),
                    '\n'); // discard invalid input
    std::cout << "Invalid input. Please enter three integers: ";
  }

  // Dynamically allocate memory for three integers
  int *pa = nullptr;
  int *pb = nullptr;
  int *pc = nullptr;

  try {
    pa = new int(a); // allocate and initialize with value
    pb = new int(b);
    pc = new int(c);
  } catch (const std::bad_alloc &e) {
    std::cerr << "Memory allocation failed: " << e.what() << '\n';
    return 1; // terminate program safely
  }

  // Display values stored in variables
  std::cout << "\nValues stored in variables:" << std::endl;
  std::cout << "a = " << a << ", b = " << b << ", c = " << c << '\n';

  // Display values stored in dynamically allocated memory
  std::cout << "Values stored in dynamic memory through pointers:" << std::endl;
  std::cout << "*pa = " << *pa << ", *pb = " << *pb << ", *pc = " << *pc
            << '\n';

  // Clean up allocated memory
  delete pa;
  delete pb;
  delete pc;

  // Avoid dangling pointers
  pa = pb = pc = nullptr;

  std::cout << "\nMemory deallocated successfully.\n";
  return 0;
}

int newway() {
  std::cout << "Using smart pointers for automatic memory management.\n";
  int a{}, b{}, c{}; // safely initialized

  // Input validation
  std::cout << "Enter three integer values separated by spaces: \n";
  std::cout << "(Input validation in place to ensure integers are entered) \n";
  std::cout << "(range: -2147483648 to 2147483647)\n";
  while (!(std::cin >> a >> b >> c)) {
    std::cin.clear();
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    std::cout << "Invalid input. Please enter three integers: ";
  }

  // Use smart pointers (automatic memory management, no delete needed)
  auto pa = std::make_unique<int>(a);
  auto pb = std::make_unique<int>(b);
  auto pc = std::make_unique<int>(c);

  // Display values stored in variables
  std::cout << "\nValues stored in variables:\n";
  std::cout << "a = " << a << ", b = " << b << ", c = " << c << '\n';

  // Display values stored via smart pointers
  std::cout << "Values stored in dynamic memory (via unique_ptr):\n";
  std::cout << "*pa = " << *pa << ", *pb = " << *pb << ", *pc = " << *pc
            << '\n';

  // No explicit delete — memory is released automatically when pa, pb, pc go
  // out of scope
  std::cout << "\nMemory automatically deallocated when smart pointers go out "
               "of scope.\n";
  return 0;
}

int main() {
  std::cout << "Demonstrating old way with raw pointers:\n";
  if (oldway() != 0) {
    return 1; // exit if oldway failed
  }

  std::cout << "\n" << std::string(50, '=') << "\n\n";

  std::cout << "Demonstrating new way with smart pointers:\n";
  if (newway() != 0) {
    return 1; // exit if newway failed
  }

  return 0;
}