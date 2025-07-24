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
    #include <algorithm>
    #include "C8086Lexer.h"
    #include "SymbolTable.h"

	using namespace std;

    extern ofstream asmFile;

    extern SymbolTable symbol_table;
}

@parser::members {
    string last_instruction = "";
    void writeIntoAsmFile(const string message) {
        if (!asmFile) {
            cout << "Error opening file: code.asm" << endl;
            return;
        }

        if(message == "    POP AX" && last_instruction == message) {
            return; // Avoid writing duplicate instructions
        }

        asmFile << message << endl;
        asmFile.flush();
        last_instruction = message;
    }

    void writeSegmentHeader() {
        writeIntoAsmFile(".MODEL SMALL");
        writeIntoAsmFile(".STACK 1000H");
        writeIntoAsmFile(".DATA");
        writeIntoAsmFile("    number DB \"00000$\"");
    }

    void writeStartMain() {
        writeIntoAsmFile("main PROC");
        writeIntoAsmFile("    MOV AX, @DATA");
        writeIntoAsmFile("    MOV DS, AX");
        writeIntoAsmFile("    PUSH BP");
        writeIntoAsmFile("    MOV BP, SP");
    }

    void writePrintRoutines() {
        writeIntoAsmFile("print_output proc");
        writeIntoAsmFile("    push ax");
        writeIntoAsmFile("    push bx");
        writeIntoAsmFile("    push cx");
        writeIntoAsmFile("    push dx");
        writeIntoAsmFile("    push si");
        writeIntoAsmFile("    lea si,number");
        writeIntoAsmFile("    mov bx,10");
        writeIntoAsmFile("    add si,4");
        writeIntoAsmFile("    cmp ax,0");
        writeIntoAsmFile("    jnge negate");
        writeIntoAsmFile("print:");
        writeIntoAsmFile("    xor dx,dx");
        writeIntoAsmFile("    div bx");
        writeIntoAsmFile("    mov [si],dl");
        writeIntoAsmFile("    add [si],'0'");
        writeIntoAsmFile("    dec si");
        writeIntoAsmFile("    cmp ax,0");
        writeIntoAsmFile("    jne print");
        writeIntoAsmFile("    inc si");
        writeIntoAsmFile("    lea dx,si");
        writeIntoAsmFile("    mov ah,9");
        writeIntoAsmFile("    int 21h");
        writeIntoAsmFile("    pop si");
        writeIntoAsmFile("    pop dx");
        writeIntoAsmFile("    pop cx");
        writeIntoAsmFile("    pop bx");
        writeIntoAsmFile("    pop ax");
        writeIntoAsmFile("    ret");
        writeIntoAsmFile("negate:");
        writeIntoAsmFile("    push ax");
        writeIntoAsmFile("    mov ah,2");
        writeIntoAsmFile("    mov dl,'-'");
        writeIntoAsmFile("    int 21h");
        writeIntoAsmFile("    pop ax");
        writeIntoAsmFile("    neg ax");
        writeIntoAsmFile("    jmp print");
        writeIntoAsmFile("print_output endp");
        writeIntoAsmFile("new_line proc");
        writeIntoAsmFile("    push ax");
        writeIntoAsmFile("    push dx");
        writeIntoAsmFile("    mov ah,2");
        writeIntoAsmFile("    mov dl,0Dh");
        writeIntoAsmFile("    int 21h");
        writeIntoAsmFile("    mov ah,2");
        writeIntoAsmFile("    mov dl,0Ah");
        writeIntoAsmFile("    int 21h");
        writeIntoAsmFile("    pop dx");
        writeIntoAsmFile("    pop ax");
        writeIntoAsmFile("    ret");
        writeIntoAsmFile("new_line endp");
    }

    void writePrintlnAssembly(const string& id) {
        // writeIntoAsmFile("    POP AX");
        SymbolInfo *symbol = symbol_table.lookup(id);
        if (symbol == nullptr) {
            cout << "Error: Variable '" << id << "' not declared." << endl;
            exit(1);
        }
        if (symbol->isStackVar()) {
            writeIntoAsmFile("    MOV AX, [BP - " + to_string(symbol->getStackOffset()) + "]");
        } else {
            writeIntoAsmFile("    MOV AX, " + id);
        }
        writeIntoAsmFile("    CALL print_output");
        writeIntoAsmFile("    CALL new_line");
    }

    void writeEpilogue() {
        writePrintRoutines();
        writeIntoAsmFile("END main");
    }

    bool isCurrScopeGlobal() {
        string curr_scope_id = symbol_table.getCurrentScopeId();
        cout << "Current Scope ID: " << curr_scope_id << endl;
        if(curr_scope_id == "1") {
            return true; // Global scope
        }
        return false; // Local scope
    }

    int stack_offset = 2;
    string current_type = "";
    string current_func = "";
    string current_func_type = "";

    int func_arg_read = 0; // from right to left.

    bool code_segment_started = false; // in the inputs, once a function appears, we start the code segment
    int label_counter = 1;

    bool entered_scope_for_function = false;

    string getNewLabel() {
        return "L" + to_string(label_counter++);
    }

    void writeFunctionEpilogue() {
        transform(current_func_type.begin(), current_func_type.end(), current_func_type.begin(), ::tolower);
        if(current_func_type != "void") {
            // writeIntoAsmFile("    POP AX");
            writeIntoAsmFile(current_proc_exit_label + ":");
        } else{
            writeIntoAsmFile(getNewLabel() + ":");
        }
        
        current_proc_exit_label = "";
        writeIntoAsmFile("    ADD SP, " + to_string(stack_offset - 2));
        writeIntoAsmFile("    POP BP");

        bool isMain = (current_func == "main");

        if (isMain) {
            writeIntoAsmFile("    MOV AX, 4CH");
            writeIntoAsmFile("    INT 21H");
            writeIntoAsmFile("main ENDP");
        } else {
            SymbolInfo *symbol = symbol_table.lookup(current_func);
            if(symbol == nullptr) {
                cout << "Function not in symboltable." << endl;
                exit(1);
            }
            int ret_value = 2 * symbol->getFuncArgsCount();
            if(ret_value == 0) {
                writeIntoAsmFile("    RET");
            } else {
                writeIntoAsmFile("    RET " + to_string(ret_value));
            }
            writeIntoAsmFile(current_func + " ENDP");
        }
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

    bool isVariableAnArray(const string &id) {
        SymbolInfo *symbol = symbol_table.lookup(id);
        if (symbol == nullptr) {
            cout << "Error: Variable '" << id << "' not declared." << endl;
            exit(1);
        }
        return symbol->getType() == "array";
    }

    string getArrayBaseName(const string &id) {
        size_t bracketPos = id.find('[');
        if (bracketPos != std::string::npos) {
            return id.substr(0, bracketPos);
        }
        return id; // If no brackets, return the original ID
    }

    vector<pair<string, string>> function_params;
    string current_proc_exit_label = "";
}


start   : {
    writeSegmentHeader();
} program {
    writeEpilogue();
};

program : program unit
        | unit
        ;

unit    : var_declaration
        | func_declaration
        | func_definition
        ;

func_declaration    : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON
                    | type_specifier ID LPAREN RPAREN SEMICOLON
                    ;

func_definition : ts=type_specifier ID LPAREN {
                        symbol_table.insert($ID->getText(), "function", false, -1, $ts.text);
                        entered_scope_for_function = true;
                        symbol_table.enterScope();
                        current_func = $ID->getText();
                        func_arg_read = 0; // reset for each function definition
                        function_params.clear(); // clear previous function parameters
                        current_func_type = $ts.text;
                    } pl=parameter_list RPAREN {
                        int param_count = getParamCount($pl.text);
                        // Insert the function parameters into the symbol table
                        for (const auto &param : function_params) {
                            string param_type = param.first;
                            string param_name = param.second;
                            int offset = -(4 + 2 * (param_count - func_arg_read - 1)); // stack offset for parameters
                            symbol_table.insert(param_name, param_type, true, offset);
                            func_arg_read++;
                        }
                        SymbolInfo *symbol = symbol_table.lookup($ID->getText());
                        if(symbol == nullptr) {
                            cout << "Function not in symboltable." << endl;
                            exit(1);
                        }
                        symbol->setFuncArgsCount(param_count);
                        // main function does not have a parameter list(at least in our test cases)
                        if(!code_segment_started) {
                            code_segment_started = true;
                            writeIntoAsmFile(".CODE");
                        }
                        writeIntoAsmFile($ID->getText() + " PROC");
                        writeIntoAsmFile("    PUSH BP");
                        writeIntoAsmFile("    MOV BP, SP");
                } compound_statement {
                    writeFunctionEpilogue();
                    current_func = "";
                    current_func_type = "";
                    stack_offset = 2; // reset stack offset for the next function
                }
                | ts=type_specifier ID LPAREN RPAREN {
                    symbol_table.insert($ID->getText(), "function", false);
                    current_func_type = $ts.text;
                    if(!code_segment_started) {
                        code_segment_started = true;
                        writeIntoAsmFile(".CODE");
                    } 
                    if($ID->getText() == "main") {
                        writeStartMain();
                        current_func = "main";
                    } else{
                        current_func = $ID->getText();
                        writeIntoAsmFile($ID->getText() + " PROC");
                        writeIntoAsmFile("    PUSH BP");
                        writeIntoAsmFile("    MOV BP, SP");
                    } 
                } compound_statement {
                        writeFunctionEpilogue();
                        current_func = "";
                        current_func_type="";
                        stack_offset = 2; // reset stack offset for the next function
                }
                ;

parameter_list returns [string text]  : pl=parameter_list COMMA ts=type_specifier ID {
                    $text = $pl.text + "," + $ts.text + " " + $ID->getText();
                    // SymbolInfo *symbol = symbol_table.lookup(current_func);
                    // if(symbol == nullptr) {
                    //     cout << "Function not in symboltable." << endl;
                    //     exit(1);
                    // }
                    // int arg_count = symbol->getFuncArgsCount();
                    // cout << "current_func: " << current_func << ", arg_count: " << arg_count << endl;
                    // symbol_table.insert($ID->getText(), $ts.text, true, -(4 + 2 * (arg_count - func_arg_read - 1)));
                    // func_arg_read++;
                    function_params.push_back(make_pair($ts.text, $ID->getText()));
                }
                | pl=parameter_list COMMA ts=type_specifier {
                    $text = $pl.text + "," + $ts.text;
                }
                | ts=type_specifier ID {
                    $text = $ts.text + $ID->getText();
                    // SymbolInfo *symbol = symbol_table.lookup(current_func);
                    // if(symbol == nullptr) {
                    //     cout << "Function not in symboltable." << endl;
                    //     exit(1);
                    // }
                    // int arg_count = symbol->getFuncArgsCount();
                    // cout << "current_func: " << current_func << ", arg_count: " << arg_count << endl;
                    // symbol_table.insert($ID->getText(), $ts.text, true, -(4 + 2 * (arg_count - func_arg_read - 1)));
                    // func_arg_read++;
                    function_params.push_back(make_pair($ts.text, $ID->getText()));
                }
                | ts=type_specifier {
                    $text = $ts.text;
                }
                ;
                
compound_statement  : LCURL {
                        if(entered_scope_for_function) {
                            entered_scope_for_function = false;
                        } else {
                            symbol_table.enterScope();
                        }
                    } statements RCURL {
                        symbol_table.exitScope();
                    }
                    | LCURL RCURL
                    ;

var_declaration : type_specifier declaration_list SEMICOLON
                ;

type_specifier returns [string text]  : INT {
                    current_type = "INT";
                    $text = "INT";
                }
                | FLOAT {
                    current_type = "FLOAT";
                    $text = "FLOAT";
                }
                | VOID {
                    current_type = "VOID";
                    $text = "VOID";
                }
                ;

declaration_list    : declaration_list COMMA ID {
                        if (isCurrScopeGlobal()) {
                            symbol_table.insert($ID->getText(), current_type);
                            writeIntoAsmFile("    " + $ID->getText() + " DW 1 DUP (0000H)");
                        } else {
                            symbol_table.insert($ID->getText(), current_type, true, stack_offset);
                            writeIntoAsmFile("    SUB SP, 2");
                            stack_offset += 2;
                        }
                    }
                    | declaration_list COMMA ID LTHIRD CONST_INT RTHIRD
                    | ID {
                        if (isCurrScopeGlobal()) {
                            symbol_table.insert($ID->getText(), current_type);
                            writeIntoAsmFile("    " + $ID->getText() + " DW 1 DUP (0000H)");
                        } else {
                            symbol_table.insert($ID->getText(), current_type, true, stack_offset);
                            writeIntoAsmFile("    SUB SP, 2");
                            stack_offset += 2;
                        }
                    }
                    | ID LTHIRD CONST_INT RTHIRD {
                        int size = stoi($CONST_INT->getText());
                        symbol_table.insert($ID->getText(), "array", !isCurrScopeGlobal(), stack_offset);
                        if (isCurrScopeGlobal()) {
                            writeIntoAsmFile("    " + $ID->getText() + " DW " + to_string(size) + " DUP (0000H)");
                        } else {
                            int totalSize = 2 * size;
                            writeIntoAsmFile("    SUB SP, " + to_string(totalSize));
                            stack_offset += totalSize;
                        }
                    };

statements  : statement
            | statements statement
            ;

statement   : var_declaration
            | expression_statement
            | compound_statement
            | FOR LPAREN expression statement {
                string condLabel = getNewLabel();
                string incLabel = getNewLabel();
                string bodyLabel = getNewLabel();
                string exitLabel = getNewLabel();

                writeIntoAsmFile(condLabel + ":");
            } expression statement {
                writeIntoAsmFile("    POP AX");
                writeIntoAsmFile("    CMP AX, 0");
                writeIntoAsmFile("    JE " + exitLabel);
                writeIntoAsmFile("    JMP " + bodyLabel);
                writeIntoAsmFile(incLabel + ":");
            } expression RPAREN {
                writeIntoAsmFile("    JMP " + condLabel);
                writeIntoAsmFile(bodyLabel + ":");
            } statement {
                writeIntoAsmFile("    JMP " + incLabel);
                writeIntoAsmFile(exitLabel + ":");
            }

            | IF LPAREN expression RPAREN {
                string elseLabel = getNewLabel();
                writeIntoAsmFile("    POP AX");
                writeIntoAsmFile("    CMP AX, 0");
                writeIntoAsmFile("    JE " + elseLabel);
            } statement {
                writeIntoAsmFile(elseLabel + ":");
            }
            | IF LPAREN expression RPAREN {
                string elseLabel = getNewLabel();
                string endLabel = getNewLabel();
                writeIntoAsmFile("    POP AX");
                writeIntoAsmFile("    CMP AX, 0");
                writeIntoAsmFile("    JE " + elseLabel);
            } statement ELSE {
                writeIntoAsmFile("    JMP " + endLabel);
                writeIntoAsmFile(elseLabel + ":");
            } statement {
                writeIntoAsmFile(endLabel + ":");
            }
    
            | WHILE LPAREN {
                string condLabel = getNewLabel();
                string exitLabel = getNewLabel();
                writeIntoAsmFile(condLabel + ":");
            } expression RPAREN {
                writeIntoAsmFile("    POP AX");
                writeIntoAsmFile("    CMP AX, 0");
                writeIntoAsmFile("    JE " + exitLabel);
            } statement {
                writeIntoAsmFile("    JMP " + condLabel);
                writeIntoAsmFile(exitLabel + ":");
            }

            | PRINTLN LPAREN ID RPAREN SEMICOLON {
                writePrintlnAssembly($ID->getText());
            }
            | RETURN expression SEMICOLON {
                // writeFunctionEpilogue();
                writeIntoAsmFile("    POP AX");
                string newLabel = getNewLabel();
                writeIntoAsmFile("    JMP " + newLabel);
                current_proc_exit_label = newLabel;
            }
            ;

expression_statement    : SEMICOLON
                        | expression SEMICOLON
                        ;

variable    : ID
            | ID LTHIRD expression RTHIRD 
            ;

expression  : logic_expression
            | variable ASSIGNOP logic_expression {
                writeIntoAsmFile("    POP AX");
                string varName = $variable.text;

                // If it's an array access like "w[0]", extract "w"
                size_t bracketPos = varName.find('[');
                if (bracketPos != std::string::npos) {
                    varName = varName.substr(0, bracketPos);
                }
                SymbolInfo *symbol = symbol_table.lookup(varName);
                if (symbol == nullptr) {
                    cout << "Error: Variable '" << varName << "' not declared." << endl;
                    exit(1);
                }
                if (symbol->isStackVar()) {
                    if (symbol->getStackOffset() < 0) {
                        writeIntoAsmFile("    MOV [BP + " + to_string(-symbol->getStackOffset()) + "], AX");
                    } else {
                        if(isVariableAnArray(varName)) {
                            writeIntoAsmFile("    POP BX");
                            writeIntoAsmFile("    SHL BX, 1"); // Multiply index by 2 for word size
                            writeIntoAsmFile("    MOV SI, BX");
                            writeIntoAsmFile("    MOV [BP + SI - " + to_string(stack_offset - 2) + "], AX");
                        } else {
                            writeIntoAsmFile("    MOV [BP - " + to_string(symbol->getStackOffset()) + "], AX");
                        }
                    }
                } else {
                    if(isVariableAnArray(varName)) {
                        writeIntoAsmFile("    POP BX");
                        writeIntoAsmFile("    SHL BX, 1"); // Multiply index by 2 for word size
                        writeIntoAsmFile("    MOV " + getArrayBaseName(varName) + "[BX], AX");
                    } else {
                        writeIntoAsmFile("    MOV " + $variable.text + ", AX");
                    }
                }
                writeIntoAsmFile("    PUSH AX");
                writeIntoAsmFile("    POP AX");
            }
            ;

logic_expression    : rel_expression
                    | rel_expression LOGICOP rel_expression {
                            writeIntoAsmFile("    POP BX");
                            writeIntoAsmFile("    POP AX");

                            string label_true = getNewLabel();
                            string label_end = getNewLabel();
                            string op = $LOGICOP->getText();

                            if (op == "||") {
                                writeIntoAsmFile("    CMP AX, 0");
                                writeIntoAsmFile("    JNE " + label_true);
                                writeIntoAsmFile("    CMP BX, 0");
                                writeIntoAsmFile("    JNE " + label_true);
                                writeIntoAsmFile("    PUSH 0");
                                writeIntoAsmFile("    JMP " + label_end);
                                writeIntoAsmFile(label_true + ":");
                                writeIntoAsmFile("    PUSH 1");
                                writeIntoAsmFile(label_end + ":");
                            } else if (op == "&&") {
                                writeIntoAsmFile("    CMP AX, 0");
                                writeIntoAsmFile("    JE " + label_end);
                                writeIntoAsmFile("    CMP BX, 0");
                                writeIntoAsmFile("    JE " + label_end);
                                writeIntoAsmFile("    PUSH 1");
                                writeIntoAsmFile("    JMP " + label_true);
                                writeIntoAsmFile(label_end + ":");
                                writeIntoAsmFile("    PUSH 0");
                                writeIntoAsmFile(label_true + ":");
                            }
                        }
                        ;

rel_expression  : simple_expression
                | simple_expression RELOP simple_expression {
                        writeIntoAsmFile("    POP BX");
                        writeIntoAsmFile("    POP AX");
                        writeIntoAsmFile("    CMP AX, BX");

                        string label_true = getNewLabel();
                        string label_end = getNewLabel();
                        string op = $RELOP->getText();

                        if (op == "==") {
                            writeIntoAsmFile("    JE " + label_true);
                        } else if (op == "!=") {
                            writeIntoAsmFile("    JNE " + label_true);
                        } else if (op == "<") {
                            writeIntoAsmFile("    JL " + label_true);
                        } else if (op == "<=") {
                            writeIntoAsmFile("    JLE " + label_true);
                        } else if (op == ">") {
                            writeIntoAsmFile("    JG " + label_true);
                        } else if (op == ">=") {
                            writeIntoAsmFile("    JGE " + label_true);
                        }

                        writeIntoAsmFile("    PUSH 0");
                        writeIntoAsmFile("    JMP " + label_end);
                        writeIntoAsmFile(label_true + ":");
                        writeIntoAsmFile("    PUSH 1");
                        writeIntoAsmFile(label_end + ":");
                    }
                    ;

simple_expression   : term
                    | simple_expression ADDOP term {
                        writeIntoAsmFile("    POP BX");
                        writeIntoAsmFile("    POP AX");
                        if ($ADDOP->getText() == "+") {
                            writeIntoAsmFile("    ADD AX, BX");
                        } else if ($ADDOP->getText() == "-") {
                            writeIntoAsmFile("    SUB AX, BX");
                        }
                        writeIntoAsmFile("    PUSH AX");
                    }
                    ;

term    : unary_expression
        | term MULOP unary_expression {
            writeIntoAsmFile("    POP BX");
            writeIntoAsmFile("    POP AX");
            string op = $MULOP->getText();
            if (op == "*") {
                writeIntoAsmFile("    CWD");
                writeIntoAsmFile("    MUL BX");
                writeIntoAsmFile("    PUSH AX");
            } else if (op == "/") {
                writeIntoAsmFile("    CWD");
                writeIntoAsmFile("    XOR DX, DX");
                writeIntoAsmFile("    DIV BX");
                writeIntoAsmFile("    PUSH AX");
            } else if (op == "%") {
                writeIntoAsmFile("    CWD");
                writeIntoAsmFile("    XOR DX, DX");
                writeIntoAsmFile("    DIV BX");
                writeIntoAsmFile("    MOV AX, DX");
                writeIntoAsmFile("    PUSH AX");
            }
        }
        ;
        
unary_expression    : ADDOP unary_expression {
                        if ($ADDOP->getText() == "-") {
                            writeIntoAsmFile("    POP AX");
                            writeIntoAsmFile("    NEG AX");
                            writeIntoAsmFile("    PUSH AX");
                        }
                    }
                    | NOT unary_expression
                    | factor
                    ;

factor  : variable {
            string varName = $variable.text;

            // If it's an array access like "w[0]", extract "w"
            size_t bracketPos = varName.find('[');
            if (bracketPos != std::string::npos) {
                varName = varName.substr(0, bracketPos);
            }

            SymbolInfo *symbol = symbol_table.lookup(varName);
            if (symbol == nullptr) {
                cout << "Error: Variable '" << varName << "' not declared." << endl;
                exit(1);
            }
            if (symbol->isStackVar()) {
                if(symbol->getStackOffset() < 0) {
                    writeIntoAsmFile("    MOV AX, [BP + " + to_string(-symbol->getStackOffset()) + "]");
                } else {
                    if(isVariableAnArray(varName)) {
                        writeIntoAsmFile("    POP BX");
                        writeIntoAsmFile("    SHL BX, 1"); // Multiply index by 2 for word size
                        writeIntoAsmFile("    MOV SI, BX");
                        writeIntoAsmFile("    MOV AX, [BP + SI - " + to_string(stack_offset - 2) + "]");
                    } else {
                        writeIntoAsmFile("    MOV AX, [BP - " + to_string(symbol->getStackOffset()) + "]");
                    }
                }
            } else {
                if(isVariableAnArray(varName)) {
                    writeIntoAsmFile("    POP BX");
                    writeIntoAsmFile("    SHL BX, 1"); // Multiply index by 2 for word size
                    writeIntoAsmFile("    MOV AX, " + getArrayBaseName(varName) + "[BX]");
                } else {
                    writeIntoAsmFile("    MOV AX, " + $variable.text);
                }
            }
            writeIntoAsmFile("    PUSH AX");
        }
        | ID LPAREN argument_list RPAREN {
            writeIntoAsmFile("    CALL " + $ID->getText());
            writeIntoAsmFile("    PUSH AX");
        }
        | LPAREN expression RPAREN 
        | CONST_INT {
            writeIntoAsmFile("    MOV AX, " + $CONST_INT->getText());
            writeIntoAsmFile("    PUSH AX");
        }
        | CONST_FLOAT
        | variable INCOP {
            string varName = $variable.text;

            // If it's an array access like "w[0]", extract "w"
            size_t bracketPos = varName.find('[');
            if (bracketPos != std::string::npos) {
                varName = varName.substr(0, bracketPos);
            }
            SymbolInfo *symbol = symbol_table.lookup(varName);
            if (symbol == nullptr) {
                cout << "Error: Variable '" << varName << "' not declared." << endl;
                exit(1);
            }
            if (symbol->isStackVar()) {
                if(symbol->getStackOffset() < 0) {
                    writeIntoAsmFile("    MOV AX, [BP + " + to_string(-symbol->getStackOffset()) + "]");
                    writeIntoAsmFile("    PUSH AX"); // push before increment
                    writeIntoAsmFile("    INC AX");
                    writeIntoAsmFile("    MOV [BP + " + to_string(-symbol->getStackOffset()) + "], AX");
                    writeIntoAsmFile("    POP AX"); // pop after increment
                } else {
                    writeIntoAsmFile("    MOV AX, [BP - " + to_string(symbol->getStackOffset()) + "]");
                    writeIntoAsmFile("    PUSH AX");
                    writeIntoAsmFile("    INC AX");
                    writeIntoAsmFile("    MOV [BP - " + to_string(symbol->getStackOffset()) + "], AX");
                    writeIntoAsmFile("    POP AX");
                }
            } else {
                if(isVariableAnArray(varName)) {
                    writeIntoAsmFile("    POP BX");
                    writeIntoAsmFile("    SHL BX, 1"); // Multiply index by 2 for word size
                    writeIntoAsmFile("    MOV AX, " + getArrayBaseName(varName) + "[BX]");
                    writeIntoAsmFile("    PUSH AX"); // push before increment
                    writeIntoAsmFile("    INC AX");
                    writeIntoAsmFile("    MOV " + getArrayBaseName(varName) + "[BX], AX");
                }
                else {
                    writeIntoAsmFile("    MOV AX, " + $variable.text);
                    writeIntoAsmFile("    PUSH AX");
                    writeIntoAsmFile("    INC AX");
                    writeIntoAsmFile("    MOV " + $variable.text + ", AX");
                    writeIntoAsmFile("    POP AX");
                }
            }
        }
        | variable DECOP {
            string varName = $variable.text;
            // If it's an array access like "w[0]", extract "w"
            size_t bracketPos = varName.find('[');
            if (bracketPos != std::string::npos) {  
                varName = varName.substr(0, bracketPos);
            }
            SymbolInfo *symbol = symbol_table.lookup(varName);
            if (symbol == nullptr) {
                cout << "Error: Variable '" << varName << "' not declared." << endl;
                exit(1);
            }
            if (symbol->isStackVar()) {
                writeIntoAsmFile("    MOV AX, [BP - " + to_string(symbol->getStackOffset()) + "]");
                writeIntoAsmFile("    PUSH AX"); // push before decrement
                writeIntoAsmFile("    DEC AX");
                writeIntoAsmFile("    MOV [BP - " + to_string(symbol->getStackOffset()) + "], AX");
                writeIntoAsmFile("    POP AX"); // pop after decrement
            } else {
                writeIntoAsmFile("    MOV AX, " + $variable.text);
                writeIntoAsmFile("    PUSH AX");
                writeIntoAsmFile("    DEC AX");
                writeIntoAsmFile("    MOV " + $variable.text + ", AX");
                 writeIntoAsmFile("    POP AX");
            }
        }
        ;

argument_list   : arguments
                |
                ;

arguments   : arguments COMMA logic_expression {

            }
            | logic_expression
            ;