# Compiler and flags
CXX = g++
CXXFLAGS = -std=c++17 -fsanitize=address

# Source and output
SRC = main.cpp
EXEC = main

# Input and output files
INPUT = in.txt
OUTPUT = out.txt

# Default target: build and run
all:
	$(CXX) $(CXXFLAGS) -o $(EXEC) $(SRC) -fsanitize=address
	./$(EXEC) $(INPUT) $(OUTPUT)

# Build only
build:
	$(CXX) $(CXXFLAGS) -o $(EXEC) $(SRC) -fsanitize=address

# Run with memory leak detection
run:
	./$(EXEC) $(INPUT) $(OUTPUT)

# Run with detailed leak report to console
run-verbose:
	./$(EXEC) $(INPUT) $(OUTPUT)

# Clean up
clean:
	rm -f $(EXEC) $(OUTPUT) asan.log.*