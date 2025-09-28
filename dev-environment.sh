#!/bin/bash

# VS Code Development Environment Setup
# This script is automatically sourced when opening terminals in VS Code

# Only run if we're in a VS Code terminal
if [ "$VSCODE_DEV_ENV" = "1" ] || [ "$TERM_PROGRAM" = "vscode" ]; then
    
    # C++ Development Environment
    export CXX="g++"
    export CC="gcc"
    export CXXFLAGS="-std=c++20 -Wall -Wextra -g"
    export CFLAGS="-std=c11 -Wall -Wextra -g"

    # Java Development Environment (OpenJDK 21)
    export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
    export PATH="$JAVA_HOME/bin:$PATH"

    # Useful aliases for development
    alias ll='ls -alF'
    alias la='ls -A'
    alias l='ls -CF'
    alias ..='cd ..'
    alias ...='cd ../..'

    # C++ compilation shortcuts
    alias cpp-compile='g++ -std=c++20 -Wall -Wextra -g'
    alias cpp-debug='g++ -std=c++20 -Wall -Wextra -g -DDEBUG'
    alias cpp-release='g++ -std=c++20 -Wall -Wextra -O2 -DNDEBUG'

    # Java compilation and execution aliases (FIXED)
    alias jcompile='javac -cp . --release 21'
    alias jrun='java -cp .'
    alias jcompile-preview='javac -cp . --release 21 --enable-preview'
    alias jrun-preview='java -cp . --enable-preview'
    alias jversion='java --version && javac --version'

    # Git shortcuts
    alias gs='git status'
    alias ga='git add'
    alias gc='git commit'
    alias gp='git push'
    alias gl='git pull'
    alias gd='git diff'

    echo "🚀 VS Code C++ and Java development environment loaded!"
    echo "📝 Available shortcuts:"
    echo "   C++: cpp-compile, cpp-debug, cpp-release"
    echo "   Java: jcompile, jrun, jcompile-preview, jrun-preview, jversion"
    echo "   Git: gs, ga, gc, gp, gl, gd"
    echo "   Navigation: ll, la, l, .., ..."
    
    # Java Environment Status
    echo ""
    echo "☕ Java Development Environment:"
    echo "  JAVA_HOME: $JAVA_HOME"
    if command -v java &> /dev/null; then
        echo "  Java Version: $(java --version | head -n1)"
    else
        echo "  ⚠️  Java not found in PATH"
    fi
fi