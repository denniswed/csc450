// Debug Example - Enhanced Hello World

#include <iostream>
#include <string>

using std::cout;
using std::endl;
using std::string;

int main() {
  // Debug output to trace program execution
  cout << "[DEBUG] Program started" << endl;

  string message = "Hello World..";
  cout << "[DEBUG] Message variable created: " << message << endl;

  cout << message << endl;

  cout << "[DEBUG] Program ending" << endl;

  return 0;
}
