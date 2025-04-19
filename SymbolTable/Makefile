# Compiler and flags
CXX = g++
CXXFLAGS = -std=c++17

# Source and output
SRC = main.cpp
EXEC = main

# Input and output files
INPUT = in.txt
OUTPUT = out.txt

# Default target: build and run
all:
	$(CXX) $(CXXFLAGS) -o $(EXEC) $(SRC)
	./$(EXEC) $(INPUT) $(OUTPUT)

# Clean up
clean:
	rm -f $(EXEC) $(OUTPUT)
