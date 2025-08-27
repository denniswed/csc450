#include <iostream>

int main() {
  int arr[3] = {5, 10, 15};
  int *p = arr; // arr decays into pointer to first element

  std::cout << "First element using pointer: " << *p << "\n"; // 5
  std::cout << "Second element via pointer arithmetic: " << *(p + 1)
            << "\n"; // 10

  int x = 42;
  int *px = &x;                                           // & gives address
  std::cout << "Value of x via pointer: " << *px << "\n"; // * dereferences

  *px = 100; // change x through pointer
  std::cout << "New value of x: " << x << "\n";

  return 0;
}