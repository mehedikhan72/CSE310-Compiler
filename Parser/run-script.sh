#!/bin/bash

# Generate parser and lexer
antlr4 -v 4.13.2 -Dlanguage=Cpp C8086Lexer.g4
antlr4 -v 4.13.2 -Dlanguage=Cpp C8086Parser.g4

# Compile with the correct include path
g++ -std=c++17 -w -I/usr/local/include/antlr4-runtime -c C8086Lexer.cpp C8086Parser.cpp Ctester.cpp

# Link with rpath so the dynamic linker can find the ANTLR runtime
g++ -std=c++17 -w C8086Lexer.o C8086Parser.o Ctester.o -L/usr/local/lib -lantlr4-runtime -o Ctester.out -pthread -Wl,-rpath,/usr/local/lib

# macOS uses DYLD_LIBRARY_PATH instead of LD_LIBRARY_PATH
DYLD_LIBRARY_PATH=/usr/local/lib ./Ctester.out $1
