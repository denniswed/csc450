# iostream Vulnerability Quick Reference

## Key Vulnerabilities Summary

### 🚨 **Critical Vulnerabilities**

1. **Buffer Overflow (Most Dangerous)**

   ```cpp
   char buf[16];
   std::cin >> buf;  // ❌ No bounds checking
   ```

2. **Stream State Corruption**

   ```cpp
   std::cout << std::hex;  // ❌ State persists globally
   ```

3. **Input Validation Bypass**
   ```cpp
   int value;
   std::cin >> value;  // ❌ No validation
   ```

### ✅ **Secure Alternatives**

1. **Safe Input Handling**

   ```cpp
   std::string input;
   std::getline(std::cin, input);  // ✅ Dynamic sizing
   ```

2. **Stream State Management**

   ```cpp
   auto saved = std::cout.flags();
   // ... formatting ...
   std::cout.flags(saved);  // ✅ Restore state
   ```

3. **Input Validation**
   ```cpp
   if (std::cin >> value && value >= 0 && value <= 100) {
       // ✅ Validated input
   }
   ```

## Testing Commands

```bash
# Compile demonstration
g++ -std=c++17 -Wall -Wextra -g iostream_vulnerabilities.cpp -o iostream_demo

# Run demonstration
./iostream_demo

# Test buffer overflow
echo "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" | ./iostream_demo

# Compile with AddressSanitizer for detection
g++ -std=c++17 -fsanitize=address iostream_vulnerabilities.cpp -o iostream_demo_asan
```

## Key References

- **CERT C++ Standard**: STR50-CPP
- **CWE-120**: Buffer Copy without Checking Size of Input
- **OWASP**: Input Validation Guidelines
