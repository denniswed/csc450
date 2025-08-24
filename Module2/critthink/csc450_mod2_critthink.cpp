/*
Program: Create a simple C++ console application that will write a program that
will take two string inputs from a user. Your program should concatenate the two
strings and then print the resulting output to the screen. Take the two string
inputs from the user 3 times for varying string lengths.
 */

#include <iostream>
#include <string>

int main() {
  // Program introduction
  std::cout << "This program will take two strings from you and combine them."
            << std::endl;
  std::cout << "It will do this three times." << std::endl;
  std::cout << std::string(50, '-') << std::endl;

  // Loop to get input 3 times
  for (int iteration = 1; iteration <= 3; iteration++) {
    std::cout << "\n--- Round " << iteration << " of 3 ---" << std::endl;

    // Declare variables for this iteration
    std::string firstString;
    std::string secondString;
    std::string concatenatedResult;

    // Get first string from user
    std::cout << "Enter the first string: ";
    std::getline(std::cin, firstString);

    // Get second string from user
    std::cout << "Enter the second string: ";
    std::getline(std::cin, secondString);

    // Concatenate the strings using the + operator
    concatenatedResult = firstString + secondString;

    // Display the results with formatting
    std::cout << "\n--- Results for Round " << iteration << " ---" << std::endl;
    std::cout << "First string:       \"" << firstString << "\"" << std::endl;
    std::cout << "Second string:      \"" << secondString << "\"" << std::endl;
    std::cout << "Concatenated:       \"" << concatenatedResult << "\""
              << std::endl;

    // Show string length information
    std::cout << "\n--- String Length Analysis ---" << std::endl;
    std::cout << "First string length:  " << firstString.length()
              << " characters" << std::endl;
    std::cout << "Second string length: " << secondString.length()
              << " characters" << std::endl;
    std::cout << "Total length:         " << concatenatedResult.length()
              << " characters" << std::endl;

    // Add separator between iterations
    if (iteration < 3) {
      std::cout << std::string(50, '=') << std::endl;
    }
  }

  // Program conclusion
  std::cout << "\n" << std::string(50, '-') << std::endl;
  std::cout << "End of program" << std::endl;

  return 0;
}