/*
 * Advanced Buffer Overflow Demonstration with Memory Visualization
 *
 * Purpose: Comprehensive educational demonstration of buffer overflow
 * vulnerabilities Methodology: Based on classic security research and CERT
 * guidelines
 *
 * Key References:
 *   - Aleph One (1996). "Smashing The Stack For Fun And Profit"
 *   - Cowan et al. (1998). "StackGuard: Automatic Adaptive Detection"
 *   - CERT C++ Secure Coding Standard (STR50-CPP, ARR30-C)
 *   - OWASP Buffer Overflow Prevention Cheat Sheet
 *
 * Educational Objectives:
 *   - Visualize memory layout and corruption
 *   - Demonstrate stack canary detection
 *   - Show real-world attack patterns
 *   - Contrast with secure coding practices
 *
 * SECURITY WARNING: Contains intentional vulnerabilities for educational
 * demonstration only. Never use these patterns in production code.
 *
 * Academic Use: Suitable for computer science security courses,
 * penetration testing training, and secure coding workshops.
 */

#include <cstring>
#include <iomanip>
#include <iostream>
#include <string>

// Function to display memory contents (for demonstration purposes)
void displayMemory(const char *buffer, size_t size, const char *label) {
  std::cout << "\n" << label << " Memory contents:" << std::endl;
  std::cout << "Address: " << static_cast<const void *>(buffer) << std::endl;
  std::cout << "Hex dump: ";
  for (size_t i = 0; i < size; ++i) {
    std::cout << std::hex << std::setw(2) << std::setfill('0')
              << (unsigned char)buffer[i] << " ";
    if ((i + 1) % 8 == 0)
      std::cout << " ";
  }
  std::cout << std::dec << std::endl;
  std::cout << "String: \"" << buffer << "\"" << std::endl;
}

// Vulnerable function that demonstrates stack corruption
void vulnerableFunction() {
  char important_data[16] = "SENSITIVE_DATA!";
  char user_buffer[16];
  char canary[16] = "CANARY_VALUE__";

  std::cout << "\n=== STACK LAYOUT BEFORE INPUT ===" << std::endl;
  std::cout << "Stack layout (top to bottom):" << std::endl;
  std::cout << "1. important_data[16]: " << important_data << std::endl;
  std::cout << "2. user_buffer[16]:    [empty]" << std::endl;
  std::cout << "3. canary[16]:         " << canary << std::endl;

  displayMemory(important_data, 16, "Important Data");
  displayMemory(user_buffer, 16, "User Buffer");
  displayMemory(canary, 16, "Canary");

  std::cout << "\n=== VULNERABILITY DEMONSTRATION ===" << std::endl;
  std::cout << "Enter your name (NO BOUNDS CHECKING): ";

  // DANGEROUS: No bounds checking!
  std::cin >> user_buffer;

  std::cout << "\n=== STACK LAYOUT AFTER INPUT ===" << std::endl;
  displayMemory(important_data, 16, "Important Data (AFTER)");
  displayMemory(user_buffer, 16, "User Buffer (AFTER)");
  displayMemory(canary, 16, "Canary (AFTER)");

  // Check if adjacent memory was corrupted
  if (strcmp(canary, "CANARY_VALUE__") != 0) {
    std::cout << "\n🚨 SECURITY BREACH DETECTED! 🚨" << std::endl;
    std::cout << "Stack canary was overwritten!" << std::endl;
    std::cout << "This indicates a buffer overflow attack!" << std::endl;
  }

  if (strcmp(important_data, "SENSITIVE_DATA!") != 0) {
    std::cout << "\n💥 CRITICAL: SENSITIVE DATA CORRUPTED! 💥" << std::endl;
    std::cout << "Important data has been overwritten!" << std::endl;
  }
}

// Demonstrate different types of malicious inputs
void demonstrateAttacks() {
  std::cout << "\n=== COMMON ATTACK PATTERNS ===" << std::endl;
  std::cout << "Here are examples of dangerous inputs to try:" << std::endl;
  std::cout << "1. Simple overflow: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            << std::endl;
  std::cout << "2. Pattern attack:  ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
            << std::endl;
  std::cout << "3. Null byte injection: AAAAAAAA\\x00BBBBBBBB" << std::endl;
  std::cout << "4. Format string: %x%x%x%x%x%x%x%x" << std::endl;
  std::cout << "\nTry entering more than 15 characters to see the overflow!"
            << std::endl;
}

// Safe alternative using modern C++
void secureAlternative() {
  std::cout << "\n=== SECURE ALTERNATIVE ===" << std::endl;

  std::string safe_input;
  const size_t MAX_LENGTH = 15;

  std::cout << "Enter your name (SECURE version): ";
  std::getline(std::cin, safe_input);

  // Input validation and sanitization
  if (safe_input.length() > MAX_LENGTH) {
    std::cout << "⚠️  Input too long! Truncating to " << MAX_LENGTH
              << " characters." << std::endl;
    safe_input = safe_input.substr(0, MAX_LENGTH);
  }

  // Additional validation - remove dangerous characters
  for (char &c : safe_input) {
    if (c < 32 || c > 126) { // Remove non-printable characters
      c = '?';
    }
  }

  std::cout << "✅ Safely processed input: \"" << safe_input << "\""
            << std::endl;
  std::cout << "✅ Length: " << safe_input.length() << " characters"
            << std::endl;
}

int main() {
  std::cout << "=== BUFFER OVERFLOW SECURITY DEMONSTRATION ===" << std::endl;
  std::cout << "This program demonstrates why input validation is critical!"
            << std::endl;

  demonstrateAttacks();

  int choice;
  std::cout << "\nChoose demonstration:" << std::endl;
  std::cout << "1. Vulnerable version (shows buffer overflow)" << std::endl;
  std::cout << "2. Secure version (shows proper input handling)" << std::endl;
  std::cout << "Enter choice (1 or 2): ";
  std::cin >> choice;
  std::cin.ignore(); // Clear the newline from buffer

  if (choice == 1) {
    vulnerableFunction();
  } else if (choice == 2) {
    secureAlternative();
  } else {
    std::cout << "Invalid choice. Running both versions..." << std::endl;
    vulnerableFunction();
    secureAlternative();
  }

  std::cout << "\n=== SECURITY LESSONS ===" << std::endl;
  std::cout << "❌ NEVER use gets(), scanf(), or cin >> with fixed-size buffers"
            << std::endl;
  std::cout << "❌ NEVER trust user input without validation" << std::endl;
  std::cout << "✅ ALWAYS validate input length before processing" << std::endl;
  std::cout << "✅ USE std::string for dynamic string handling" << std::endl;
  std::cout << "✅ SANITIZE input to remove dangerous characters" << std::endl;
  std::cout << "✅ IMPLEMENT bounds checking for all array operations"
            << std::endl;

  return 0;
}
