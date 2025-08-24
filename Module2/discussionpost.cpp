#include <cstring>
#include <iostream>
#include <string>

int main() {
  std::cout << "=== Buffer Overflow Demonstration ===" << std::endl;

  // VULNERABLE CODE - Buffer Overflow Example
  std::cout << "\n1. VULNERABLE VERSION (Buffer Overflow Risk):" << std::endl;
  char vulnerable_buffer[16];
  char adjacent_buffer[16] = "DONOTOVERWRITE!";
  std::cout << "Enter your name (vulnerable): ";
  std::cin >> vulnerable_buffer; // No bounds checking!
  std::cout << "You entered: " << vulnerable_buffer << std::endl;
  std::cout << "Adjacent buffer before overflow: " << adjacent_buffer
            << std::endl;

  // SECURE CODE - With Input Validation
  std::cout << "\n2. SECURE VERSION (With Input Validation):" << std::endl;
  char secure_buffer[16];
  char adjacent_secure_buffer[16] = "DONOTOVERWRITE!";
  std::string temp_input;
  std::cout << "Enter your name (secure): ";
  std::cin >> temp_input;

  // Truncate to fit buffer size (15 chars + null terminator)
  if (temp_input.length() >= 16) {
    temp_input = temp_input.substr(0, 15);
    std::cout << "Input truncated to fit buffer size." << std::endl;
  }

  strcpy(secure_buffer, temp_input.c_str());
  std::cout << "You entered: " << secure_buffer << std::endl;
  std::cout << "Adjacent secure buffer before overflow: "
            << adjacent_secure_buffer << std::endl;

  std::cout << "\nBuffer size in both cases: 16 characters" << std::endl;

  return 0;
}