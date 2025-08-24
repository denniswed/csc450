/*
 * Simple Buffer Overflow Demonstration
 *
 * Purpose: Educational demonstration of buffer overflow vulnerabilities
 * Based on: Classic buffer overflow research (Aleph One, 1996)
 * References:
 *   - CERT Secure Coding Standards (STR50-CPP)
 *   - OWASP Buffer Overflow Guidelines
 *   - "Smashing The Stack For Fun And Profit" (Phrack 49)
 *
 * WARNING: This code contains intentional vulnerabilities for educational
 * purposes only. DO NOT use in production environments.
 *
 * Educational Value:
 *   - Demonstrates why input validation is critical
 *   - Shows memory corruption in action
 *   - Illustrates real-world attack vectors
 */

#include <cstring>
#include <iostream>

int main() {
  std::cout << "=== WHY UNRESTRICTED INPUT IS DANGEROUS ===" << std::endl;

  // Show what happens with your current vulnerable approach
  std::cout << "\n🚨 DEMONSTRATING THE VULNERABILITY:" << std::endl;
  std::cout << "Your current code uses: std::cin >> vulnerable_buffer;"
            << std::endl;
  std::cout << "This has NO bounds checking whatsoever!" << std::endl;

  // Recreate your vulnerable scenario
  char buffer[16]; // Same as your original
  char adjacent_memory[64] =
      "This is important data that should not be overwritten!";

  std::cout << "\nBefore input:" << std::endl;
  std::cout << "Buffer contents: [empty]" << std::endl;
  std::cout << "Adjacent memory: " << adjacent_memory << std::endl;

  std::cout << "\nNow enter something longer than 15 characters:" << std::endl;
  std::cout << "Try: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" << std::endl;
  std::cout << "Enter input: ";

  // This is the DANGEROUS line (same as your original)
  std::cin >> buffer; // NO BOUNDS CHECKING!

  std::cout << "\nAfter input:" << std::endl;
  std::cout << "Buffer contents: " << buffer << std::endl;
  std::cout << "Adjacent memory: " << adjacent_memory << std::endl;

  // Check if we corrupted adjacent memory
  if (strncmp(adjacent_memory, "This is important", 17) != 0) {
    std::cout << "\n💥 BUFFER OVERFLOW DETECTED!" << std::endl;
    std::cout << "Adjacent memory was corrupted!" << std::endl;
    std::cout << "In a real attack, this could:" << std::endl;
    std::cout << "- Corrupt program data" << std::endl;
    std::cout << "- Overwrite return addresses" << std::endl;
    std::cout << "- Execute malicious code" << std::endl;
    std::cout << "- Crash the program" << std::endl;
  } else {
    std::cout << "\n✅ No overflow detected (you entered short input)"
              << std::endl;
  }

  std::cout << "\n=== THE REAL DANGERS ===" << std::endl;
  std::cout << "1. STACK SMASHING: Overwrite return addresses" << std::endl;
  std::cout << "2. DATA CORRUPTION: Overwrite variables" << std::endl;
  std::cout << "3. CODE INJECTION: Execute malicious code" << std::endl;
  std::cout << "4. DENIAL OF SERVICE: Crash the application" << std::endl;
  std::cout << "5. PRIVILEGE ESCALATION: Gain unauthorized access" << std::endl;

  return 0;
}
