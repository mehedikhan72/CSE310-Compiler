# Compiler and flags
CXX = g++

# Source and output
SRC = 2105141_main.cpp
EXEC = main

# Input and output files
INPUT = 2105141_in.txt
OUTPUT = 2105141_out.txt

REPORT_INPUT = Reports/2105141_report_in.txt
REPORT_OUTPUT = Reports/2105141_report_out.txt
FINAL_REPORT = Reports/2105141_final_report.txt # Report for any hash function, anytime.

HASH_FUNCTION_NAME ?= sdbm

# Default target: build and run
all:
	$(CXX) -o $(EXEC) $(SRC)
	./$(EXEC) $(INPUT) $(OUTPUT) ${FINAL_REPORT} ${HASH_FUNCTION_NAME}

# Build only
build:
	$(CXX) -o $(EXEC) $(SRC)

# Run with memory leak detection
run:
	./$(EXEC) $(INPUT) $(OUTPUT) ${FINAL_REPORT} ${HASH_FUNCTION_NAME}

# Run with detailed leak report to console
run-verbose:
	./$(EXEC) $(INPUT) $(OUTPUT) ${FINAL_REPORT} ${HASH_FUNCTION_NAME}

# Clean up
clean:
	rm -f $(EXEC) $(OUTPUT) asan.log.*

check-leaks:
	$(CXX) -o $(EXEC) $(SRC) -fsanitize=address
	./$(EXEC) $(INPUT) $(OUTPUT) ${FINAL_REPORT} ${HASH_FUNCTION_NAME}

# Report
report:
	$(CXX) -o $(EXEC) $(SRC)
	./$(EXEC) $(REPORT_INPUT) $(REPORT_OUTPUT) ${FINAL_REPORT} ${HASH_FUNCTION_NAME}

SDBM_REPORT = Reports/2105141_sdbm_report.txt
DJB2_REPORT = Reports/2105141_djb2_report.txt
FNV1A_REPORT = Reports/2105141_fnv1a_report.txt

run-all-hash:
	$(CXX) -o $(EXEC) $(SRC)
	@echo "Running with sdbm hash function..."
	./$(EXEC) $(REPORT_INPUT) $(REPORT_OUTPUT) $(SDBM_REPORT) sdbm
	@echo "Running with djb2 hash function..."
	./$(EXEC) $(REPORT_INPUT) $(REPORT_OUTPUT) $(DJB2_REPORT) djb2
	@echo "Running with fnv1a hash function..."
	./$(EXEC) $(REPORT_INPUT) $(REPORT_OUTPUT) $(FNV1A_REPORT) fnv1a
	@echo "All hash functions completed."

