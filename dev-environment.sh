#!/bin/bash

# VS Code Development Environment Setup
# This script is automatically sourced when opening terminals in VS Code

# Only run if we're in a VS Code terminal
if [ "$VSCODE_DEV_ENV" = "1" ] || [ "$TERM_PROGRAM" = "vscode" ]; then
    
    # C++ Development Environment
    export CXX="g++"
    export CC="gcc"
    export CXXFLAGS="-std=c++17 -Wall -Wextra -g"
    export CFLAGS="-std=c11 -Wall -Wextra -g"

    # Java Development Environment
    # Make sure JAVA_HOME is set (adjust path as needed for your system)
    # export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
    # export PATH=$JAVA_HOME/bin:$PATH

    # Useful aliases for development
    alias ll='ls -alF'
    alias la='ls -A'
    alias l='ls -CF'
    alias ..='cd ..'
    alias ...='cd ../..'

    # C++ compilation shortcuts
    alias cpp-compile='g++ -std=c++17 -Wall -Wextra -g'
    alias cpp-debug='g++ -std=c++17 -Wall -Wextra -g -DDEBUG'
    alias cpp-release='g++ -std=c++17 -Wall -Wextra -O2 -DNDEBUG'

    # Java compilation shortcuts
    alias javac-compile='javac -cp .'
    alias java-run='java -cp .'

    # Git shortcuts
    alias gs='git status'
    alias ga='git add'
    alias gc='git commit'
    alias gp='git push'
    alias gl='git pull'
    alias gd='git diff'

    # Make development
    alias make-clean='make clean'
    alias make-debug='make DEBUG=1'

    echo "🚀 VS Code C++ and Java development environment loaded!"
    echo "📝 Available shortcuts:"
    echo "   C++: cpp-compile, cpp-debug, cpp-release"
    echo "   Java: javac-compile, java-run"
    echo "   Git: gs, ga, gc, gp, gl, gd"
    echo "   Navigation: ll, la, l, .., ..."
    
fi
