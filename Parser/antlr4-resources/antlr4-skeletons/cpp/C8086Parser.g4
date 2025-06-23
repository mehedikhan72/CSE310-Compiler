parser grammar C8086Parser;

options {
    tokenVocab = C8086Lexer;
}

@parser::header {
    #include <iostream>
    #include <fstream>
    #include <string>
    #include <cstdlib>
    #include "C8086Lexer.h"
	using namespace std;

    extern std::ofstream parserLogFile;
    extern std::ofstream errorFile;

    extern int syntaxErrorCount;
}

@parser::members {
    void writeIntoparserLogFile(const std::string message) {
        if (!parserLogFile) {
            std::cout << "Error opening parserLogFile.txt" << std::endl;
            return;
        }

        parserLogFile << message << std::endl;
        parserLogFile.flush();
    }

    void writeIntoErrorFile(const std::string message) {
        if (!errorFile) {
            std::cout << "Error opening errorFile.txt" << std::endl;
            return;
        }
        errorFile << message << std::endl;
        errorFile.flush();
    }
}


start : program
	{
        writeIntoparserLogFile("Parsing completed successfully with " + std::to_string(syntaxErrorCount) + " syntax errors.");
	}
	;

program returns [std::string text]
    : p=program u=unit {
        $text = $p.text + "\n" + $u.text;
        writeIntoparserLogFile("Line " + std::to_string($u.start->getLine()) + ": program : program unit\n");
        writeIntoparserLogFile($text + "\n");
    }
    | u=unit {
        $text = $u.text;
        writeIntoparserLogFile("Line " + std::to_string($u.start->getLine()) + ": program : unit\n");
        writeIntoparserLogFile($text + "\n");
    }
    ;

unit returns [std::string text]  : vd=var_declaration {
		$text = $vd.text;
		writeIntoparserLogFile("Line " + to_string($vd.start->getLine()) + ": unit : var_declaration\n");
		writeIntoparserLogFile($text + "\n");
}
     | func_declaration
     | func_definition
     ;
     
func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON
		| type_specifier ID LPAREN RPAREN SEMICOLON
		;
		 
func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement
		| type_specifier ID LPAREN RPAREN compound_statement
 		;				


parameter_list  : parameter_list COMMA type_specifier ID
		| parameter_list COMMA type_specifier
 		| type_specifier ID
		| type_specifier
 		;

 		
compound_statement : LCURL statements RCURL
 		    | LCURL RCURL
 		    ;
 		    
var_declaration returns [std::string text]
    : t=type_specifier dl=declaration_list sm=SEMICOLON {
		$text = $t.name_line + " " + $dl.text + ";";
		writeIntoparserLogFile("Line " + std::to_string($sm->getLine()) + ": var_declaration : type_specifier declaration_list SEMICOLON\n");
		writeIntoparserLogFile($text + "\n");
      }

    | t=type_specifier de=declaration_list_err sm=SEMICOLON {
        writeIntoErrorFile(
            std::string("Line# ") + std::to_string($sm->getLine()) +
            " with error name: " + $de.error_name +
            " - Syntax error at declaration list of variable declaration"
        );

        syntaxErrorCount++;
      }
    ;

declaration_list_err returns [std::string error_name]: {
        $error_name = "Error in declaration list";
    };

 		 
type_specifier returns [std::string name_line]	
        : INT {
            $name_line = "int";
			writeIntoparserLogFile("Line " + to_string($INT->getLine()) + ": type_specifier : INT\n");
			writeIntoparserLogFile($INT->getText() + "\n");
        }
 		| FLOAT {
            $name_line = "float";
			writeIntoparserLogFile("Line " + to_string($FLOAT->getLine()) + ": type_specifier : FLOAT\n");
			writeIntoparserLogFile($FLOAT->getText() + "\n");
        }
 		| VOID {
            $name_line = "void";
			writeIntoparserLogFile("Line " + to_string($VOID->getLine()) + ": type_specifier : VOID\n");
			writeIntoparserLogFile($VOID->getText() + "\n");
        }
 		;
 		
declaration_list returns [std::string text]
		: d=declaration_list COMMA ID {
			$text = $d.text + "," + $ID->getText();
			writeIntoparserLogFile("Line " + to_string($d.stop->getLine()) + ": declaration_list : declaration_list COMMA ID\n");
			writeIntoparserLogFile($text + "\n");
		}
		| d=declaration_list COMMA ID LTHIRD CONST_INT RTHIRD {
			$text = $d.text + "," + $ID->getText() + "[" + $CONST_INT->getText() + "]";
			writeIntoparserLogFile("Line " + to_string($d.stop->getLine()) + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
			writeIntoparserLogFile($text + "\n");
		}
		| ID {
			$text = $ID->getText();
			writeIntoparserLogFile("Line " + to_string($ID->getLine()) + ": declaration_list : ID\n");
			writeIntoparserLogFile($text + "\n");
		}
		| ID LTHIRD CONST_INT RTHIRD {
			$text = $ID->getText() + "[" + $CONST_INT->getText() + "]";
			writeIntoparserLogFile("Line " + to_string($ID->getLine()) + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
			writeIntoparserLogFile($text + "\n");
		}
		;
 		  
statements : statement
	   | statements statement
	   ;
	   
statement : var_declaration
	  | expression_statement
	  | compound_statement
	  | FOR LPAREN expression_statement expression_statement expression RPAREN statement
	  | IF LPAREN expression RPAREN statement
	  | IF LPAREN expression RPAREN statement ELSE statement
	  | WHILE LPAREN expression RPAREN statement
	  | PRINTLN LPAREN ID RPAREN SEMICOLON
	  | RETURN expression SEMICOLON
	  ;
	  
expression_statement 	: SEMICOLON			
			| expression SEMICOLON 
			;
	  
variable : ID 		
	 | ID LTHIRD expression RTHIRD 
	 ;
	 
 expression : logic_expression	
	   | variable ASSIGNOP logic_expression 	
	   ;
			
logic_expression : rel_expression 	
		 | rel_expression LOGICOP rel_expression 	
		 ;
			
rel_expression	: simple_expression 
		| simple_expression RELOP simple_expression	
		;
				
simple_expression : term 
		  | simple_expression ADDOP term 
		  ;
					
term :	unary_expression
     |  term MULOP unary_expression
     ;

unary_expression : ADDOP unary_expression  
		 | NOT unary_expression 
		 | factor 
		 ;
	
factor	: variable 
	| ID LPAREN argument_list RPAREN
	| LPAREN expression RPAREN
	| CONST_INT 
	| CONST_FLOAT
	| variable INCOP 
	| variable DECOP
	;
	
argument_list : arguments
			  |
			  ;
	
arguments : arguments COMMA logic_expression
	      | logic_expression
	      ;
