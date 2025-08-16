#include <iostream>
#include <string>

int main() {
  // Different data types demonstration
  const std::string firstname = "Sigfroid";
  const std::string lastname = "Habberson";
  const std::string streetaddress = "144 Dead Elm St";
  const std::string city = "Springfield";
  const std::string state = "IL";
  const std::string zipcode = "62704";

  // Display the results
  std::cout << "Personal Information:" << std::endl;
  std::cout << "First Name: " << firstname << std::endl;
  std::cout << "Last Name: " << lastname << std::endl;
  std::cout << "Street Address: " << streetaddress << std::endl;
  std::cout << "City: " << city << std::endl;
  std::cout << "State: " << state << std::endl;
  std::cout << "Zip Code: " << zipcode << std::endl;

  return 0;
}