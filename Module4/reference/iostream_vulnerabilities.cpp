/*
 * iostream Vulnerability Demonstration Program
 *
 * This program demonstrates various formatting vulnerabilities
 * in C++ iostream library for educational purposes.
 *
 * Compile with: g++ -std=c++17 -Wall -Wextra -g iostream_vulnerabilities.cpp -o
 * iostream_demo
 *
 * WARNING: Contains intentional vulnerabilities for educational demonstration
 * only!
 */

#include <iomanip>
#include <iostream>
#include <limits>
#include <string>

// Vulnerability 2: Stream State Corruption
void demonstrate_stream_state_corruption() {
  std::cout << "\n=== VULNERABILITY 2: Stream State Corruption ===\n";

  std::cout << "Normal decimal output: " << 12345 << "\n";

  // VULNERABLE: Changing stream state without restoration
  std::cout << std::hex << std::uppercase;
  std::cout << "Processing hex value: " << 255 << "\n";

  // Stream state is now corrupted for subsequent output
  std::cout << "This should be decimal but shows as: " << 12345 << "\n";

  // Fix the stream state
  std::cout << std::dec << std::nouppercase;
  std::cout << "Fixed decimal output: " << 12345 << "\n";
}

// Vulnerability 3: Precision/Width Manipulation
void demonstrate_precision_vulnerabilities() {
  std::cout << "\n=== VULNERABILITY 3: Precision/Width Issues ===\n";

  double sensitive_value = 3.141592653589793;

  std::cout << "Normal precision: " << sensitive_value << "\n";

  // VULNERABLE: Excessive precision can reveal internal representation
  std::cout << "High precision (may reveal internal data): "
            << std::setprecision(50) << sensitive_value << "\n";

  // Reset precision
  std::cout << std::setprecision(6);

  // VULNERABLE: Excessive field width (performance issue)
  std::cout << "Testing large field width...\n";
  // Commented out to avoid actual performance issues in demo
  // std::cout << std::setw(10000) << "test" << "\n";
  std::cout << "Large width can cause memory/performance issues\n";
}

// Vulnerability 4: Input Validation Issues
void demonstrate_input_validation_issues() {
  std::cout << "\n=== VULNERABILITY 4: Input Validation ===\n";

  int age;
  std::cout << "Enter your age: ";

  // VULNERABLE: No validation of input type or range
  if (std::cin >> age) {
    std::cout << "Age entered: " << age << "\n";

    // This could be problematic with negative or extremely large values
    if (age < 0) {
      std::cout << "🚨 Negative age detected!\n";
    } else if (age > 200) {
      std::cout << "🚨 Unrealistic age detected!\n";
    }
  } else {
    std::cout << "🚨 Invalid input - not a number!\n";
    std::cin.clear();
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
  }
}

// SECURE ALTERNATIVE: Safe input handling
void demonstrate_secure_practices() {
  std::cout << "\n=== SECURE ALTERNATIVES ===\n";

  // Secure string input
  std::string safe_input;
  std::cout << "Enter text (secure): ";
  std::cin.ignore(); // Clear any remaining input
  std::getline(std::cin, safe_input);

  // Validate length
  const size_t MAX_LENGTH = 50;
  if (safe_input.length() > MAX_LENGTH) {
    std::cout << "⚠️ Input too long, truncating...\n";
    safe_input = safe_input.substr(0, MAX_LENGTH);
  }

  std::cout << "Safely processed: " << safe_input << "\n";

  // Secure stream state management with RAII
  {
    // Save current state
    auto saved_flags = std::cout.flags();
    auto saved_precision = std::cout.precision();

    // Temporary formatting
    std::cout << std::hex << std::uppercase;
    std::cout << "Temporary hex output: " << 255 << "\n";

    // Automatic restoration when scope ends
    std::cout.flags(saved_flags);
    std::cout.precision(saved_precision);
  }

  std::cout << "Stream state restored: " << 255 << "\n";
}

// Test function to check for vulnerabilities
void run_vulnerability_tests() {
  std::cout << "=== IOSTREAM VULNERABILITY DEMONSTRATION ===\n";
  std::cout << "This program demonstrates common iostream vulnerabilities.\n";
  std::cout << "For educational purposes only!\n";

  int choice;

  do {
    std::cout << "\nSelect demonstration:\n";
    std::cout << "1. Buffer Overflow (operator>>)\n";
    std::cout << "2. Stream State Corruption\n";
    std::cout << "3. Precision/Width Issues\n";
    std::cout << "4. Input Validation Problems\n";
    std::cout << "5. Secure Alternatives\n";
    std::cout << "0. Exit\n";
    std::cout << "Choice: ";

    if (!(std::cin >> choice)) {
      std::cin.clear();
      std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      std::cout << "Invalid input!\n";
      continue;
    }

    switch (choice) {
    case 2:
      demonstrate_stream_state_corruption();
      break;
    case 3:
      demonstrate_precision_vulnerabilities();
      break;
    case 4:
      demonstrate_input_validation_issues();
      break;
    case 5:
      demonstrate_secure_practices();
      break;
    case 0:
      std::cout << "Exiting...\n";
      break;
    default:
      std::cout << "Invalid choice!\n";
      break;
    }

  } while (choice != 0);
}

int main() {
  run_vulnerability_tests();
  return 0;
}
