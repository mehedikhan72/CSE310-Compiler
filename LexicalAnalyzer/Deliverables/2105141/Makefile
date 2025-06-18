# Default input file
INPUT_FILE ?= TestIO/input3.txt

all: build run

build:
	flex analyzer.l
	g++ lex.yy.c -o analyzer

run:
	./analyzer $(INPUT_FILE)	

clean:
	rm -f lex.yy.c analyzer