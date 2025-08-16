/* Corrections:
    - Missing ending comment characters for header comments - adding
    - Missing comment characters for Main Function label - adding
    - conio.h is not needed, nor findable - removing
    - Missing comment for namespace declaration - removing
    - pulling in an entire namespace is not best practice - removing
    - adjusting lines to accommodate standard formatting
    - adding $ to line to be more accurate
*/

#include <iostream>

// Main Function
int main() {

  double myMoney = 1000.50; // this should be printed out

  // Standard Output Statement
  std::cout << "Please be sure to correct all syntax errors in this program"
            << std::endl;

  std::cout << "I have corrected all errors for this program." << std::endl;

  std::cout << " The total amount of money available is = $" << myMoney
            << std::endl;

  // Wait For Output Screen

  // Main Function return Statement
  return 0;
}
