#!/bin/bash
# C++20 compilation
g++ -std=c++20 -pthread -Wall -Wextra -Wpedantic -g PortfolioMod7.cpp -o PortfolioMod7

# With additional security flags
#g++ -std=c++20 -pthread -Wall -Wextra -Wpedantic -Werror -g -O2 -D_FORTIFY_SOURCE=2 PortfolioMod7.cpp -o PortfolioMod7

# Run the program
./PortfolioMod7