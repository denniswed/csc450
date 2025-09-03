#include <iomanip>
#include <iostream>
#include <string>

class StreamStateVulnerability {
public:
  void process_user_data(const std::string &data) {
    // Save original stream state
    std::ios_base::fmtflags original_flags = std::cout.flags();

    // VULNERABILITY: Stream state changes persist
    std::cout << std::hex << std::uppercase; // Changes global state

    // Process data...
    std::cout << "Processing: " << data << std::endl;

    // showing the change prior to the stream state change:
    std::cout << "Before restoring state: \n";
    display_account_info();

    // Restore original stream state
    std::cout.flags(original_flags);
  }

  void display_account_info() {
    int account_number = 12345;
    // BUG: This will print in hex due to previous function
    std::cout << "Account: " << account_number << std::endl; // Prints "3039"
  }
};

void demonstrate_precision_vulnerability() {
  double sensitive_value = 123.456789123456;
  int user_precision = 50; // User-controlled value

  // VULNERABILITY: Excessive precision can reveal internal representation
  std::cout << std::setprecision(user_precision) << sensitive_value
            << std::endl;

  // Can also cause performance issues with extremely large field widths
  int user_width = 1000; // User-controlled
  std::cout << std::setw(user_width) << "test"
            << std::endl; // Memory/performance issue
}

void demonstrate_safe_precision() {
  double sensitive_value = 123.456789123456;
  int user_precision = 50; // User-controlled value
  int user_width = 1000;   // User-controlled

  // Save original stream state
  std::ios_base::fmtflags original_flags = std::cout.flags();
  std::streamsize original_precision = std::cout.precision();

  // SAFE: Validate and limit user input
  const int MAX_PRECISION = 12; // Reasonable limit for double
  const int MAX_WIDTH = 50;     // Reasonable limit for width

  int safe_precision = std::min(user_precision, MAX_PRECISION);
  int safe_width = std::min(user_width, MAX_WIDTH);

  std::cout << "Safe output with validated parameters:" << std::endl;
  std::cout << std::setprecision(safe_precision) << std::fixed
            << sensitive_value << std::endl;

  std::cout << std::setw(safe_width) << "test" << std::endl;

  // Always restore original state
  std::cout.flags(original_flags);
  std::cout.precision(original_precision);
}

int main() {
  StreamStateVulnerability demo;

  demo.display_account_info();

  demo.process_user_data("user entered info");

  // This will now be affected by the changed stream state
  std::cout << "Adjusted output because of stream state:" << std::endl;
  demo.display_account_info();

  std::cout << "Showing precision vulnerability:" << std::endl;
  demonstrate_precision_vulnerability();

  std::cout << "Showing safe precision handling:" << std::endl;
  demonstrate_safe_precision();

  return 0;
}
