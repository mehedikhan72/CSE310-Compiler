parser grammar C8086Parser;

options {
    tokenVocab = C8086Lexer;
}

@parser::header {
    #include <iostream>
    #include <fstream>
    #include <string>
    #include <sstream>
    #include <cstdlib>
    #include "C8086Lexer.h"
    #include "SymbolTable.h"

	using namespace std;

    extern ofstream parserLogFile;
    extern ofstream errorFile;

    extern int syntaxErrorCount;

    extern SymbolTable symbol_table;
}

@parser::members {
    void writeIntoParserLogFile(const string message) {
        if (!parserLogFile) {
            cout << "Error opening parserLogFile.txt" << endl;
            return;
        }

        parserLogFile << message << endl;
        parserLogFile.flush();
    }

    void writeIntoErrorFile(const string message) {
        if (!errorFile) {
            cout << "Error opening errorFile.txt" << endl;
            return;
        }
        errorFile << message << endl;
        errorFile.flush();
    }

    bool isInteger(const string &str) {
        for (char c : str) {
            if (!isdigit(c)) {
                return false;
            }
        }
        return true;
    }

    int getParamCount(const string &str) {
        int count = 0;
        size_t pos = 0;
        while ((pos = str.find(',', pos)) != string::npos) {
            count++;
            pos++;
        }
        return count + 1; // +1 for the last parameter
    }

    // from symbol table list. eg int a, int b
    vector<string> getFuncArgsFromList(const string &str) {
        vector<string> args;
        stringstream ss(str);
        string arg;

        while (getline(ss, arg, ',')) { // Split by comma
            // Trim leading spaces
            arg.erase(0, arg.find_first_not_of(' '));
            // Extract type (first word)
            stringstream arg_ss(arg);
            string type;
            arg_ss >> type; // Get the first word (the type)
            args.push_back(type);
        }

        return args;
    }

    // from arg list. eg func(a, b), when calling aarki.
    vector<string> getFuncArgsFromCall(const string &str) {
        // cut whitespaces from str
        string trimmed_str = str;
        trimmed_str.erase(remove(trimmed_str.begin(), trimmed_str.end(), ' '), trimmed_str.end());
        vector<string> args;
        size_t pos = 0;
        while ((pos = trimmed_str.find(',')) != string::npos) {
            args.push_back(trimmed_str.substr(0, pos));
            trimmed_str.erase(0, pos + 1);
        }
        if (!trimmed_str.empty()) {
            args.push_back(trimmed_str); // add the last argument
        }
        return args;
    }

    // foo4(c[1) -> we wanna return foo4 only.
    string getFuncNameFromExpression(const string &expr) {
        size_t pos = expr.find('(');
        if (pos != string::npos) {
            return expr.substr(0, pos); // Return the substring before the '('
        }
        return expr; // If no '(' found, return the whole expression
    }

    string current_type;
    bool fp_and_no_lcurl_found = false; // keeps track of the first lcurl after a function parameter list
    bool function_declaration_reading = false; // since we don't wanna store declaration's parameter list in the symbol table
    bool return_type_mismatch = false;

    string current_func_param_list = ""; // when we're reading a function definition which did not have a
    // declaration, we store the current function's parameter list here to use it later to update the
    // symbol table entry for the function, in its own scope. (since parameter list is in a child scope)
    bool func_without_declaration = false;
    string func_without_declaration_id = "";
    bool skip_func_validation = false; // needed when a defined function was named as a variable earlier.

    // cannot return from void function.
    string current_func_id = "";
    string error_text_1 = "";
}


start : program
	{
        writeIntoParserLogFile("Line " + to_string($program.stop->getLine()) + ": start : program\n");
        symbol_table.printAllScopes();
        writeIntoParserLogFile("Total number of lines: " + to_string($program.stop->getLine()));
        writeIntoParserLogFile("Total number of errors: " + to_string(syntaxErrorCount));
        symbol_table.testPrint();
	}
	;

program returns [string text]
    : p=program u=unit {
        $text = $p.text + "\n" + $u.text;
        writeIntoParserLogFile("Line " + to_string($u.stop->getLine()) + ": program : program unit\n");
        writeIntoParserLogFile($text + "\n");
    }
    | u=unit {
        $text = $u.text;
        writeIntoParserLogFile("Line " + to_string($u.stop->getLine()) + ": program : unit\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;

unit returns [string text]  : vd=var_declaration {
		$text = $vd.text;
		writeIntoParserLogFile("Line " + to_string($vd.stop->getLine()) + ": unit : var_declaration\n");
		writeIntoParserLogFile($text + "\n");
}
     | fd=func_declaration {
		$text = $fd.text;
		writeIntoParserLogFile("Line " + to_string($fd.stop->getLine()) + ": unit : func_declaration\n");
		writeIntoParserLogFile($text + "\n");
	 }
     | fde=func_definition {
        $text = $fde.text;
        writeIntoParserLogFile("Line " + to_string($fde.stop->getLine()) + ": unit : func_definition\n");
        writeIntoParserLogFile($text + "\n");
     }
     ;

func_declaration returns [string text] : ts=type_specifier ID LPAREN {
            function_declaration_reading = true;
    } parameter_list RPAREN SEMICOLON {
			$text = $ts.text + " " + $ID->getText() + "(" + $parameter_list.text + ");";
            int param_count = getParamCount($parameter_list.text);
            if(symbol_table.insert($ID->getText(), "ID", $ts.text, $ts.text, $parameter_list.text, param_count) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ts.start->getLine()) + ": func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON\n");
			writeIntoParserLogFile($text + "\n");
            function_declaration_reading = false; 
		}
		| ts=type_specifier ID LPAREN {
            function_declaration_reading = true;
        } RPAREN SEMICOLON {
			$text = $ts.text + " " + $ID->getText() + "();";
            if(symbol_table.insert($ID->getText(), "ID", $ts.text, $ts.text, "", 0) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ts.start->getLine()) + ": func_declaration : type_specifier ID LPAREN RPAREN SEMICOLON\n");
			writeIntoParserLogFile($text + "\n");
            function_declaration_reading = false;
		}
		;
		 
func_definition returns [string text]
        : ts=type_specifier ID LPAREN {
            if(symbol_table.insert($ID->getText(), "ID", $ts.text, $ts.text) == false) {
                // string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                // writeIntoErrorFile(error_text);
                // writeIntoParserLogFile(error_text);
                // syntaxErrorCount++;
            } else {
                // this function did not have a declaration.
                func_without_declaration = true;
                func_without_declaration_id = $ID->getText();
            }

            // return type mismatch check
            SymbolInfo *symbol = symbol_table.lookup($ID->getText());
            if(symbol != nullptr) {
                string func_return_type = symbol->getFuncRetType();
                cout << "Function return type: " << func_return_type << endl;
                cout << "Expected return type: " << $ts.text << endl;
                if(func_return_type != $ts.text) {
                    return_type_mismatch = true;
                }
            }

            symbol_table.enterScope();
            fp_and_no_lcurl_found = true; 
            current_func_id = $ID->getText(); 
        } pl=parameter_list {
            if(return_type_mismatch) {
                string error_text = "";
                SymbolInfo *symbol = symbol_table.lookup($ID->getText());
                if(symbol != nullptr) {
                    if(symbol->getFuncRetType() == "") {
                        // not a return type mismatch, rather redeclaration. since no ret type was found hence not a function.
                        error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                        skip_func_validation = true;
                    } else {
                        error_text = "Error at line " + to_string($ID->getLine()) + ": Return type mismatch of " + $ID->getText() + "\n";
                    }
                }
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
                return_type_mismatch = false;
            }
            if(!func_without_declaration && !skip_func_validation) {
                SymbolInfo *symbol2 = symbol_table.lookup($ID->getText());
                if(symbol2 != nullptr) {
                    string actual_func_param_list = symbol2->getFuncArgs();
                    if(actual_func_param_list != $pl.text) {
                        string error_text = "Error at line " + to_string($ID->getLine()) + ": Total number of arguments mismatch with declaration in function " + $ID->getText() + "\n";
                        writeIntoErrorFile(error_text);
                        writeIntoParserLogFile(error_text);
                        syntaxErrorCount++;
                    }
                } 
            }
            current_func_param_list = $pl.text;
        } RPAREN cs=compound_statement {
            if(error_text_1 != "") {
                writeIntoErrorFile("Error at line " + to_string($cs.stop->getLine()) + ": " + error_text_1);
                writeIntoParserLogFile("Error at line " + to_string($cs.stop->getLine()) + ": " + error_text_1);
                syntaxErrorCount++;
                error_text_1 = "";
            }
            $text = $ts.text + " " + $ID->getText() + "(" + $pl.text + ")" + $cs.text;
            writeIntoParserLogFile("Line " + to_string($cs.stop->getLine()) + ": func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement\n");
            writeIntoParserLogFile($text + "\n");
        }
        | ts=type_specifier ID LPAREN {
            if(symbol_table.insert($ID->getText(), "ID", $ts.text, $ts.text) == false) {
                // string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                // writeIntoErrorFile(error_text);
                // writeIntoParserLogFile(error_text);
                // syntaxErrorCount++;
            };

            // return type mismatch check
            SymbolInfo *symbol = symbol_table.lookup($ID->getText());
            if(symbol != nullptr) {
                string func_return_type = symbol->getFuncRetType();
                if(func_return_type != $ts.text) {
                    string error_text = "Error at line " + to_string($ID->getLine()) + ": Return type mismatch of " + $ID->getText() + "\n";
                    writeIntoErrorFile(error_text);
                    writeIntoParserLogFile(error_text);
                    syntaxErrorCount++;
                }
            }
            
            symbol_table.enterScope();
            fp_and_no_lcurl_found = true;
            current_func_id = $ID->getText();
        } RPAREN cs=compound_statement {
            $text = $ts.text + " " + $ID->getText() + "()" + $cs.text;
            writeIntoParserLogFile("Line " + to_string($cs.stop->getLine()) + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
            writeIntoParserLogFile($text + "\n");
        }	
        ;			

parameter_list returns [string text]  : pl=parameter_list COMMA ts=type_specifier ID {
			$text = $pl.text + "," + $ts.text + " " + $ID->getText();
			if(!function_declaration_reading && symbol_table.insert($ID->getText(), "ID", $ts.text) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + " in parameter\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ts.start->getLine()) + ": parameter_list : parameter_list COMMA type_specifier ID\n");
			writeIntoParserLogFile($text + "\n");
		}
		| pl=parameter_list COMMA {
			$text = $pl.text + ",";
			writeIntoParserLogFile("Line " + to_string($pl.stop->getLine()) + ": parameter_list : parameter_list COMMA\n");
			writeIntoParserLogFile($text + "\n");
		}
 		| ts=type_specifier ID {
			$text = $ts.text + " " + $ID->getText();
			if(!function_declaration_reading && symbol_table.insert($ID->getText(), "ID", $ts.text) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + " in parameter\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ts.start->getLine()) + ": parameter_list : type_specifier ID\n");
			writeIntoParserLogFile($text + "\n");
		}
		| ts=type_specifier {
			$text = $ts.text;
			writeIntoParserLogFile("Line " + to_string($ts.start->getLine()) + ": parameter_list : type_specifier\n");
			writeIntoParserLogFile($text + "\n");
		}
        | ple=parameter_list_err {
            writeIntoParserLogFile($ple.error);
            writeIntoErrorFile($ple.error);
            syntaxErrorCount++;
        }
 		;

parameter_list_err returns [string error]: ts=type_specifier ADDOP{
        $error = "Error at line " + to_string($ts.start->getLine()) + ": syntax error, unexpected ADDOP, expecting RPAREN or COMMA\n";
    };

 		
compound_statement returns [string text]
	: LCURL {
        if(fp_and_no_lcurl_found) {
            fp_and_no_lcurl_found = false;
        } else {
            symbol_table.enterScope();
        }
    } statements RCURL {
		$text = "{\n" + $statements.text + "\n}";
		writeIntoParserLogFile("Line " + to_string($RCURL->getLine()) + ": compound_statement : LCURL statements RCURL\n");
		writeIntoParserLogFile($text + "\n");
        symbol_table.printAllScopes();
        symbol_table.exitScope();

        if(func_without_declaration) {
            func_without_declaration = false;
            SymbolInfo *symbol = symbol_table.lookup(func_without_declaration_id);
            if(symbol != nullptr) {
                symbol->setFuncArgs(current_func_param_list);
                symbol->setFuncArgsCount(getParamCount(current_func_param_list));
            }
        }
        skip_func_validation = false; // reset this for the next function definition
        current_func_id = ""; // reset current function id
	}
	| LCURL RCURL {
		$text = "{}";
		writeIntoParserLogFile("Line " + to_string($RCURL->getLine()) + ": compound_statement : LCURL RCURL\n");
		writeIntoParserLogFile($text + "\n");
	}
	;
 		    
var_declaration returns [string text]
    : t=type_specifier dl=declaration_list sm=SEMICOLON {
		$text = $t.text + " " + $dl.text + ";";
		writeIntoParserLogFile("Line " + to_string($sm->getLine()) + ": var_declaration : type_specifier declaration_list SEMICOLON\n");
        if($t.text == "void") {
            string error_text = "Error at line " + to_string($t.start->getLine()) + ": Variable type cannot be void\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        }
        writeIntoParserLogFile($text + "\n");
      }

    | t=type_specifier dle=declaration_list_err sm=SEMICOLON {
        writeIntoErrorFile($dle.error);
        writeIntoParserLogFile($dle.error);
        syntaxErrorCount++;
      }
    | dle=declaration_list_err{
        writeIntoErrorFile($dle.error);
        writeIntoParserLogFile($dle.error);
        syntaxErrorCount++;
    }
    ;

declaration_list_err returns [string error]: ts=type_specifier v=variable ADDOP{
        $error = "Error at line " + to_string($ts.start->getLine()) + ": syntax error, unexpected ADDOP, expecting COMMA or SEMICOLON\n";
    };

 		 
type_specifier returns [string text]	
        : INT {
            $text = "int";
			writeIntoParserLogFile("Line " + to_string($INT->getLine()) + ": type_specifier : INT\n");
			writeIntoParserLogFile($INT->getText() + "\n");
            current_type = "int";
        }
 		| FLOAT {
            $text = "float";
			writeIntoParserLogFile("Line " + to_string($FLOAT->getLine()) + ": type_specifier : FLOAT\n");
			writeIntoParserLogFile($FLOAT->getText() + "\n");
            current_type = "float";
        }
 		| VOID {
            $text = "void";
			writeIntoParserLogFile("Line " + to_string($VOID->getLine()) + ": type_specifier : VOID\n");
			writeIntoParserLogFile($VOID->getText() + "\n");
            current_type = "void";
        }
 		;
 		
declaration_list returns [string text]
		: d=declaration_list COMMA ID {
			$text = $d.text + "," + $ID->getText();
            if(symbol_table.insert($ID->getText(), "ID", current_type) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($d.stop->getLine()) + ": declaration_list : declaration_list COMMA ID\n");
			writeIntoParserLogFile($text + "\n");
		}
		| d=declaration_list COMMA ID LTHIRD CONST_INT RTHIRD {
			$text = $d.text + "," + $ID->getText() + "[" + $CONST_INT->getText() + "]";
			if(symbol_table.insert($ID->getText(), "ID", "array") == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($d.stop->getLine()) + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
			writeIntoParserLogFile($text + "\n");
		}
		| ID {
			$text = $ID->getText();
			if(symbol_table.insert($ID->getText(), "ID", current_type) == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ID->getLine()) + ": declaration_list : ID\n");
			writeIntoParserLogFile($text + "\n");
		}
		| ID LTHIRD CONST_INT RTHIRD {
			$text = $ID->getText() + "[" + $CONST_INT->getText() + "]";
			if(symbol_table.insert($ID->getText(), "ID", "array") == false) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Multiple declaration of " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            };
			writeIntoParserLogFile("Line " + to_string($ID->getLine()) + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
			writeIntoParserLogFile($text + "\n");
		}
		;
 		  
statements returns [string text]
    : s=statement {
        $text = $s.text;
        writeIntoParserLogFile("Line " + to_string($s.stop->getLine()) + ": statements : statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | st=statements s=statement {
        $text = $st.text + "\n" + $s.text;
        writeIntoParserLogFile("Line " + to_string($s.stop->getLine()) + ": statements : statements statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;
	   
statement returns [string text]
    : vd=var_declaration {
        $text = $vd.text;
        writeIntoParserLogFile("Line " + to_string($vd.start->getLine()) + ": statement : var_declaration\n");
        writeIntoParserLogFile($text + "\n");
    }
    | es=expression_statement {
        $text = $es.text;
        writeIntoParserLogFile("Line " + to_string($es.start->getLine()) + ": statement : expression_statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | cs=compound_statement {
        $text = $cs.text;
        writeIntoParserLogFile("Line " + to_string($cs.stop->getLine()) + ": statement : compound_statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | FOR LPAREN es1=expression_statement es2=expression_statement e=expression RPAREN s=statement {
        $text = "for(" + $es1.text + $es2.text + $e.text + ")" + $s.text;
        writeIntoParserLogFile("Line " + to_string($s.stop->getLine()) + ": statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | IF LPAREN e=expression RPAREN s=statement {
        $text = "if(" + $e.text + ")" + $s.text;
        writeIntoParserLogFile("Line " + to_string($s.stop->getLine()) + ": statement : IF LPAREN expression RPAREN statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | IF LPAREN e=expression RPAREN s1=statement ELSE s2=statement {
        $text = "if(" + $e.text + ")" + $s1.text + "else " + $s2.text;
        writeIntoParserLogFile("Line " + to_string($s2.stop->getLine()) + ": statement : IF LPAREN expression RPAREN statement ELSE statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | WHILE LPAREN e=expression RPAREN s=statement {
        $text = "while(" + $e.text + ")" + $s.text;
        writeIntoParserLogFile("Line " + to_string($s.stop->getLine()) + ": statement : WHILE LPAREN expression RPAREN statement\n");
        writeIntoParserLogFile($text + "\n");
    }
    | PRINTLN LPAREN id=ID RPAREN SEMICOLON {
        $text = "printf(" + $id->getText() + ");";
        writeIntoParserLogFile("Line " + to_string($RPAREN->getLine()) + ": statement : PRINTLN LPAREN ID RPAREN SEMICOLON\n");
        SymbolInfo *symbol = symbol_table.lookup($id->getText());
        if(symbol == nullptr) {
            string error_text = "Error at line " + to_string($id->getLine()) + ": Undeclared variable " + $id->getText() + "\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        } 
        writeIntoParserLogFile($text + "\n");
    }
    | RETURN e=expression SEMICOLON {
        SymbolInfo *symbol = symbol_table.lookup(current_func_id);
        if(symbol != nullptr) {
            string func_return_type = symbol->getFuncRetType();
            if(func_return_type == "void") {
                error_text_1 = "Cannot return value from function " + current_func_id + " with void return type \n";
            }
        }
        $text = "return " + $e.text + ";";
        writeIntoParserLogFile("Line " + to_string($RETURN->getLine()) + ": statement : RETURN expression SEMICOLON\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;

expression_statement returns [string text]
    : SEMICOLON {
        $text = ";";
        writeIntoParserLogFile("Line " + to_string($SEMICOLON->getLine()) + ": expression_statement : SEMICOLON\n");
        writeIntoParserLogFile($text + "\n");
    }
    | e=expression SEMICOLON {
        $text = $e.text + ";";
        writeIntoParserLogFile("Line " + to_string($SEMICOLON->getLine()) + ": expression_statement : expression SEMICOLON\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;
	  
variable returns [string text] : ID {
		$text = $ID->getText();
		writeIntoParserLogFile("Line " + to_string($ID->getLine()) + ": variable : ID\n");
        SymbolInfo *symbol = symbol_table.lookup($ID->getText());
        if(symbol == nullptr) {
            string error_text = "Error at line " + to_string($ID->getLine()) + ": Undeclared variable " + $ID->getText() + "\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        } else{
            string var_type = symbol->getVarType();
            if(var_type == "array") {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Type mismatch, " + $ID->getText() + " is an array\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            }
        }
		writeIntoParserLogFile($text + "\n");
		}		
	  | ID LTHIRD e=expression RTHIRD { 
		$text = $ID->getText() + "[" + $e.text + "]";

		writeIntoParserLogFile("Line " + to_string($ID->getLine()) + ": variable : ID LTHIRD expression RTHIRD\n");
        if(!isInteger($e.text)) {
            string error_text = "Error at line " + to_string($e.start->getLine()) + ": Expression inside third brackets not an integer\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        }

        // var may not be array
        SymbolInfo *symbol = symbol_table.lookup($ID->getText());
        if(symbol != nullptr) {
            string var_type = symbol->getVarType();
            if(var_type != "array") {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": " + $ID->getText() + " not an array\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            }
        }
		writeIntoParserLogFile($text + "\n");
	  }
	  ;
	 
expression returns [string text]
    : le=logic_expression {
        $text = $le.text;
        writeIntoParserLogFile("Line " + to_string($le.start->getLine()) + ": expression : logic_expression\n");
        writeIntoParserLogFile($text + "\n");
    }
    | var=variable ASSIGNOP le=logic_expression {
        $text = $var.text + $ASSIGNOP->getText() + $le.text;
        writeIntoParserLogFile("Line " + to_string($ASSIGNOP->getLine()) + ": expression : variable ASSIGNOP logic_expression\n");
        SymbolInfo *symbol = symbol_table.lookup($var.text);
        if(symbol == nullptr) {
            //todo: handle error
        } else {
            string var_type = symbol->getVarType();
            if(var_type == "int") {
                if($le.text.find(".") != string::npos && $le.text.find("%") == string::npos) {    
                    string error_text = "Error at line " + to_string($ASSIGNOP->getLine()) + ": Type Mismatch\n";
                    writeIntoErrorFile(error_text);
                    writeIntoParserLogFile(error_text);
                    syntaxErrorCount++;
                }
            }
        }

        // c[3] = 2.7 but c is an int array. capture this type mismatch.
        string var_name = "";
        string s1 = $var.text;
        if(s1.find("[") != string::npos) {
            // get the var name only.
            size_t pos = s1.find("[");
            string var_name = s1.substr(0, pos);

            SymbolInfo *symbol = symbol_table.lookup(var_name);
            if(symbol != nullptr) {
                string var_type = symbol->getVarType();
                if(var_type == "array") {
                    // check if the expression is an integer.
                    if($le.text.find(".") != string::npos && $le.text.find("%") == string::npos) {
                        string error_text = "Error at line " + to_string($ASSIGNOP->getLine()) + ": Type Mismatch\n";
                        writeIntoErrorFile(error_text);
                        writeIntoParserLogFile(error_text);
                        syntaxErrorCount++;
                    }
                }
            }
        }


        // can't have void func assigned to a variable.
        string func_name = getFuncNameFromExpression($le.text);
        SymbolInfo *func_symbol = symbol_table.lookup(func_name);
        if(func_symbol != nullptr && func_symbol->getFuncRetType() == "void") {
            string error_text = "Error at line " + to_string($ASSIGNOP->getLine()) + ": Void function used in expression\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        }
        writeIntoParserLogFile($text + "\n");
    }
    ;

logic_expression returns [string text]
    : re=rel_expression {
        $text = $re.text;
        writeIntoParserLogFile("Line " + to_string($re.start->getLine()) + ": logic_expression : rel_expression\n");
        writeIntoParserLogFile($text + "\n");
    }
    | re1=rel_expression LOGICOP re2=rel_expression {
        $text = $re1.text + $LOGICOP->getText() + $re2.text;
        writeIntoParserLogFile("Line " + to_string($LOGICOP->getLine()) + ": logic_expression : rel_expression LOGICOP rel_expression\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;

rel_expression returns [string text]
    : se=simple_expression {
        $text = $se.text;
        writeIntoParserLogFile("Line " + to_string($se.start->getLine()) + ": rel_expression : simple_expression\n");
        writeIntoParserLogFile($text + "\n");
    }
    | se1=simple_expression RELOP se2=simple_expression {
        $text = $se1.text + $RELOP->getText() + $se2.text;
        writeIntoParserLogFile("Line " + to_string($RELOP->getLine()) + ": rel_expression : simple_expression RELOP simple_expression\n");
        writeIntoParserLogFile($text + "\n");
    }
    ;
				
simple_expression returns [string text] : term {
			$text = $term.text;
			writeIntoParserLogFile("Line " + to_string($term.start->getLine()) + ": simple_expression : term\n");
			writeIntoParserLogFile($text + "\n");
		  }
		  | se=simple_expression ADDOP term {
			$text = $se.text + $ADDOP->getText() + $term.text;
			writeIntoParserLogFile("Line " + to_string($ADDOP->getLine()) + ": simple_expression : simple_expression ADDOP term\n");
			writeIntoParserLogFile($text + "\n");
		  } | see=simple_expression_err {
            writeIntoParserLogFile($see.error);
            writeIntoErrorFile($see.error);
            syntaxErrorCount++;
          }
		  ;

simple_expression_err returns [string error] : term ADDOP ASSIGNOP {
        $error = "Error at line " + to_string($term.start->getLine()) + ": syntax error, unexpected ASSIGNOP\n";
    };
					
term returns [string text] : ue=unary_expression {
		$text = $ue.text;
		writeIntoParserLogFile("Line " + to_string($ue.start->getLine()) + ": term : unary_expression\n");
		writeIntoParserLogFile($text + "\n");
	}
     |  t=term MULOP ue=unary_expression {
		$text = $t.text + $MULOP->getText() + $ue.text;
		writeIntoParserLogFile("Line " + to_string($MULOP->getLine()) + ": term : term MULOP unary_expression\n");
        if($MULOP->getText() == "%") {
            if(!isInteger($ue.text)) {
                string error_text = "Error at line " + to_string($MULOP->getLine()) + ": Non-Integer operand on modulus operator\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            } 
            if($ue.text == "0") {
                string error_text = "Error at line " + to_string($MULOP->getLine()) + ": Modulus by Zero\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            }
        }
        string func_name = getFuncNameFromExpression($ue.text);
        SymbolInfo *func_symbol = symbol_table.lookup(func_name);
        if(func_symbol != nullptr && func_symbol->getFuncRetType() == "void") {
            string error_text = "Error at line " + to_string($MULOP->getLine()) + ": Void function used in expression\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        }
		writeIntoParserLogFile($text + "\n");
	 }
     ;

unary_expression returns [string text] : ADDOP ue=unary_expression {
            $text = $ADDOP->getText() + $ue.text;
            writeIntoParserLogFile("Line " + to_string($ADDOP->getLine()) + ": unary_expression : ADDOP unary_expression\n");
            writeIntoParserLogFile($text + "\n");
		}
		 | NOT ue=unary_expression {
            $text = $NOT->getText() + $ue.text;
            writeIntoParserLogFile("Line " + to_string($NOT->getLine()) + ": unary_expression : NOT unary_expression\n");
            writeIntoParserLogFile($text + "\n");
         }
		 | factor {
			$text = $factor.text;
			writeIntoParserLogFile("Line " + to_string($factor.start->getLine()) + ": unary_expression : factor\n");
			writeIntoParserLogFile($text + "\n");
			}
		 ;
	
factor returns [string text] : variable {
		$text = $variable.text;
		writeIntoParserLogFile("Line " + to_string($variable.start->getLine()) + ": factor : variable\n");
		writeIntoParserLogFile($text + "\n");
	}
	| ID LPAREN al=argument_list RPAREN {
		$text = $ID->getText() + "(" + $al.text + ")";
		writeIntoParserLogFile("Line " + to_string($ID->getLine()) + ": factor : ID LPAREN argument_list RPAREN\n");
        SymbolInfo *symbol = symbol_table.lookup($ID->getText());
        if(symbol != nullptr) {
            string func_arg_list = symbol->getFuncArgs();
            cout << "Function argument list: " << func_arg_list << endl;
            vector<string> func_args = getFuncArgsFromList(func_arg_list);
            vector<string> call_args = getFuncArgsFromCall($al.text);

            // print the function args and call args for debugging
            cout << "Function args: ";
            for(const auto &arg : func_args) {
                cout << arg << " ";
            }
            cout << endl;
            cout << "Call args: ";
            for(const auto &arg : call_args) {
                cout << arg << " ";
            }
            cout << endl;

            if(func_args.size() != call_args.size()) {
                string error_text = "Error at line " + to_string($ID->getLine()) + ": Total number of arguments mismatch with declaration in function " + $ID->getText() + "\n";
                writeIntoErrorFile(error_text);
                writeIntoParserLogFile(error_text);
                syntaxErrorCount++;
            } else {
                for(size_t i = 0; i < func_args.size(); i++) {
                    if(func_args[i] == "int") {
                        // check call_args[i] in the symbol table first.
                        string call_arg_type = "";

                        // skip complex expressions like a*b
                        if(call_args[i].find("*") != string::npos || call_args[i].find("/") != string::npos || call_args[i].find("+") != string::npos || call_args[i].find("-") != string::npos) {
                            continue; // skip this argument, it will be checked later.
                        }

                        // get the variable name if it's an array like c[1]
                        bool arg_type_found = false;
                        if(call_args[i].find("[") != string::npos) {
                            size_t pos = call_args[i].find("[");
                            string var_name = call_args[i].substr(0, pos);
                            SymbolInfo *call_arg_symbol = symbol_table.lookup(var_name);
                            if(call_arg_symbol != nullptr) {
                                call_arg_type = call_arg_symbol->getVarType();
                                cout << "call arg type lmao: " << call_arg_type << endl;
                                arg_type_found = true;
                            }
                        }
                        
                        if(!arg_type_found) {
                            SymbolInfo *call_arg_symbol = symbol_table.lookup(call_args[i]);
                            if(call_arg_symbol != nullptr) {
                                call_arg_type = call_arg_symbol->getVarType();
                            } else {
                                if(isInteger(call_args[i])) {
                                    call_arg_type = "int";
                                } 
                            }
                        }
                        if(call_arg_type != "int" && call_arg_type != "array") {
                            string error_text = "Error at line " + to_string($ID->getLine()) + ": " + to_string(i + 1) + "th argument mismatch in function " + $ID->getText() + "\n";
                            writeIntoErrorFile(error_text);
                            writeIntoParserLogFile(error_text);
                            syntaxErrorCount++;
                            break; // no need to check further if one argument is already mismatched
                        }
                    }
                }
            }
            
        } else {
            string error_text = "Error at line " + to_string($ID->getLine()) + ": Undefined function " + $ID->getText() + "\n";
            writeIntoErrorFile(error_text);
            writeIntoParserLogFile(error_text);
            syntaxErrorCount++;
        }
		writeIntoParserLogFile($text + "\n");
	}
	| LPAREN expression RPAREN {
		$text = "(" + $expression.text + ")";
		writeIntoParserLogFile("Line " + to_string($LPAREN->getLine()) + ": factor : LPAREN expression RPAREN\n");
		writeIntoParserLogFile($text + "\n");
	}
	| CONST_INT {
		$text = $CONST_INT->getText();
		writeIntoParserLogFile("Line " + to_string($CONST_INT->getLine()) + ": factor : CONST_INT\n");
		writeIntoParserLogFile($text + "\n");
	} 
	| CONST_FLOAT {
		$text = $CONST_FLOAT->getText();
		writeIntoParserLogFile("Line " + to_string($CONST_FLOAT->getLine()) + ": factor : CONST_FLOAT\n");
		writeIntoParserLogFile($text + "\n");
	}
	| variable INCOP {
		$text = $variable.text + "++";
		writeIntoParserLogFile("Line " + to_string($variable.start->getLine()) + ": factor : variable INCOP\n");
		writeIntoParserLogFile($text + "\n");
	}
	| variable DECOP { 
		$text = $variable.text + "--";
		writeIntoParserLogFile("Line " + to_string($variable.start->getLine()) + ": factor : variable DECOP\n");
		writeIntoParserLogFile($text + "\n");
	}
	;
	
argument_list returns [string text] :
        a=arguments {
            $text = $a.text;
            writeIntoParserLogFile("Line " + to_string($a.stop->getLine()) + ": argument_list : arguments\n");
            writeIntoParserLogFile($text + "\n");
        }
		|
		;
	
arguments returns [string text] :
        a=arguments COMMA le=logic_expression {
            $text = $a.text + "," + $le.text;
            writeIntoParserLogFile("Line " + to_string($a.stop->getLine()) + ": arguments : arguments COMMA logic_expression\n");
            writeIntoParserLogFile($text + "\n");
        }
	    | le=logic_expression {
            $text = $le.text;
            writeIntoParserLogFile("Line " + to_string($le.start->getLine()) + ": arguments : logic_expression\n");
            writeIntoParserLogFile($text + "\n");
        }
	    ;