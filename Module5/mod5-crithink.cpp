/*
* Program: Create a C++ program that will obtain input from a user and store it
  into the provided CSC450_CT5_mod5.txt.

* Your program should append it to the provided text file, without deleting the
  existing data:
  a. Store the provided data in the CSC450_CT5_mod5.txt file.
  b. Create a reversal method that will reverse all of the characters in the CSC450_CT5_mod5.txt file and store the  result in a CSC450-mod5-reverse.txt file.
 */

#include <algorithm>
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <string>

class FileProcessor {
 private:
  static constexpr const char *INPUT_FILE = "CSC450_CT5_mod5.txt";
  static constexpr const char *OUTPUT_FILE = "CSC450-mod5-reverse.txt";

 public:
  /**
   * Appends user input to the specified file
   * @return true if successful, false otherwise
   */
  bool appendUserInput() {
    try {
      std::cout << "=== File Input Program ===\n";
      std::cout << "Enter text to append to " << INPUT_FILE << "\n";
      std::cout << "(Press Enter twice to finish):\n\n";

      // Open file in append mode
      std::ofstream outFile(INPUT_FILE, std::ios::app);
      if (!outFile.is_open()) {
        throw std::runtime_error("Cannot open file for writing: " + std::string(INPUT_FILE));
      }

      std::string line;
      bool hasContent = false;

      // Read multiple lines until empty line
      while (std::getline(std::cin, line)) {
        if (line.empty() && hasContent) {
          break;  // Stop on empty line after content
        }

        if (!line.empty()) {
          hasContent = true;
          outFile << line << '\n';
          std::cout << "Added: " << line << '\n';
        }
      }

      outFile.close();

      if (hasContent) {
        std::cout << "\nText successfully appended to " << INPUT_FILE << '\n';
        return true;
      } else {
        std::cout << "No content entered.\n";
        return false;
      }

    } catch (const std::exception &e) {
      std::cerr << "Error appending to file: " << e.what() << '\n';
      return false;
    }
  }

  /**
   * Reads the input file, reverses all characters, and writes to output file
   * @return true if successful, false otherwise
   */
  bool reverseFileContent() {
    try {
      std::cout << "\n=== File Reversal Process ===\n";

      // Read entire file content
      std::ifstream inFile(INPUT_FILE);
      if (!inFile.is_open()) {
        throw std::runtime_error("Cannot open input file: " + std::string(INPUT_FILE));
      }

      // Read all content into a string
      std::string content;
      std::string line;

      while (std::getline(inFile, line)) {
        content += line + '\n';
      }
      inFile.close();

      if (content.empty()) {
        std::cout << "Input file is empty. Nothing to reverse.\n";
        return false;
      }

      // Remove trailing newline for clean reversal
      if (!content.empty() && content.back() == '\n') {
        content.pop_back();
      }

      std::cout << "Original content (" << content.length() << " characters):\n";
      std::cout << "\"" << content << "\"\n\n";

      // Reverse all characters
      std::reverse(content.begin(), content.end());

      std::cout << "Reversed content:\n";
      std::cout << "\"" << content << "\"\n\n";

      // Write reversed content to output file
      std::ofstream outFile(OUTPUT_FILE);
      if (!outFile.is_open()) {
        throw std::runtime_error("Cannot create output file: " + std::string(OUTPUT_FILE));
      }

      outFile << content << '\n';
      outFile.close();

      std::cout << "Reversed content written to " << OUTPUT_FILE << '\n';
      return true;

    } catch (const std::exception &e) {
      std::cerr << "Error during reversal process: " << e.what() << '\n';
      return false;
    }
  }

  /**
   * Displays the contents of both files for verification
   */
  void displayFileContents() {
    std::cout << "\n=== File Contents Verification ===\n";

    // Display original file
    std::cout << "\nContents of " << INPUT_FILE << ":\n";
    std::cout << std::string(50, '-') << '\n';

    std::ifstream inFile(INPUT_FILE);
    if (inFile.is_open()) {
      std::string line;
      int lineNum = 1;
      while (std::getline(inFile, line)) {
        std::cout << lineNum++ << ": " << line << '\n';
      }
      inFile.close();
    } else {
      std::cout << "Could not open file for reading.\n";
    }

    // Display reversed file
    std::cout << "\nContents of " << OUTPUT_FILE << ":\n";
    std::cout << std::string(50, '-') << '\n';

    std::ifstream revFile(OUTPUT_FILE);
    if (revFile.is_open()) {
      std::string line;
      int lineNum = 1;
      while (std::getline(revFile, line)) {
        std::cout << lineNum++ << ": " << line << '\n';
      }
      revFile.close();
    } else {
      std::cout << "Reversed file not found or could not be opened.\n";
    }
  }
};

int main() {
  try {
    FileProcessor processor;

    // Step 1: Get user input and append to file
    bool inputSuccess = processor.appendUserInput();

    if (inputSuccess) {
      // Step 2: Reverse the file content
      bool reverseSuccess = processor.reverseFileContent();

      if (reverseSuccess) {
        // Step 3: Display results
        processor.displayFileContents();

        std::cout << "\n=== Program Completed Successfully ===\n";
        std::cout << "✓ User input appended to CSC450_CT5_mod5.txt\n";
        std::cout << "✓ Reversed content saved to CSC450-mod5-reverse.txt\n";
      }
    }

    return 0;

  } catch (const std::exception &e) {
    std::cerr << "Fatal error: " << e.what() << '\n';
    return 1;
  }
}