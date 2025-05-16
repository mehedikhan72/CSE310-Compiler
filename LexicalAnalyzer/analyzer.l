%option noyywrap

%x CHAR_STATE
%x STRING_STATE
%x COMMENT_STATE_ONE
%x COMMENT_STATE_TWO

%{
#include<stdio.h>
#include<stdlib.h>
#include "SymbolTable.cpp"

int line_count=1;
int error_count=0;

FILE *token_output = fopen("token.txt", "w");
FILE* log_output = fopen("log.txt", "w");

string curr_char;
string curr_string;
string curr_comment;

SymbolTable symbol_table("sdbm_hash", 7, log_output);

char processCharLiteral(char *lexeme);
char* toUpperCase(const char* str);
%}

WHITESPACE [ \t\f\r\v]+ 
LETTER [a-zA-Z]
NEWLINE \n
DIGIT [0-9]
EXPONENT [eE][+-]?{DIGIT}+

INVALID_FLOAT_ONE {DIGIT}*\.
INVALID_FLOAT_TWO {DIGIT}*\.{DIGIT}*((\.)+{DIGIT}*)+
INVALID_FLOAT_THREE {DIGIT}+(\.{DIGIT}+)?{EXPONENT}\..*

INVALID_IDENTIFIER {DIGIT}+\..*

UNFINISHED_CHAR ([\'][^\']|\'\\\')
EMPTY_CHAR \'\'

INVAILD_VAR {DIGIT}+[a-zA-Z_]+

FLOAT_ONE {DIGIT}*\.{DIGIT}+{EXPONENT}?
FLOAT_TWO {DIGIT}*\.{DIGIT}*{EXPONENT}
FLOAT_THREE {DIGIT}+{EXPONENT}?

KEYWORD (if|for|do|int|float|void|switch|default|else|while|break|char|double|return|case|continue|goto|long|short|static|unsigned)

ESCAPE_CHAR [\'][\\][abfnrtv0\\'\"?][\']
NORMAL_CHAR ['][^\\\"\n][']
CHAR_LITERAL {ESCAPE_CHAR}|{NORMAL_CHAR}

%%
{NEWLINE} { line_count++; }
{WHITESPACE} ;

\/\/ {
    printf("Comment state one began\n");
    BEGIN COMMENT_STATE_ONE;
}

\/\* {
    printf("Comment state two began\n");
    BEGIN COMMENT_STATE_TWO;
}

{INVALID_FLOAT_ONE}|{INVALID_FLOAT_TWO}|{INVALID_FLOAT_THREE} {
    error_count++;
}

{INVALID_IDENTIFIER} {
    error_count++;
}

{INVALID_CHAR} {
    error_count++;
}

{UNFINISHED_CHAR} {
    error_count++;
}

{EMPTY_CHAR} {
    error_count++;
}

{INVAILD_VAR} {
    error_count++;
}

{KEYWORD} {
    fprintf(token_output, "<%s> ", toUpperCase(yytext));
    fprintf(log_output, "Line no %d: Token <%s> Lexeme %s found\n\n", line_count, toUpperCase(yytext), yytext);
}

{DIGIT}+ {
    fprintf(token_output, "<CONST_INT, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <CONST_INT> Lexeme %s found\n\n", line_count, yytext);
    bool inserted = symbol_table.insert(yytext, "CONST_INT");
    if (inserted){symbol_table.printAllScopes();}
}

{FLOAT_ONE}|{FLOAT_TWO}|{FLOAT_THREE} {
    fprintf(token_output, "<CONST_FLOAT, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <CONST_FLOAT> Lexeme %s found\n\n", line_count, yytext);
    bool inserted = symbol_table.insert(yytext, "CONST_FLOAT");
    if (inserted){symbol_table.printAllScopes();}
}

{CHAR_LITERAL} {
    char ch = processCharLiteral(yytext);
    fprintf(token_output, "<CONST_CHAR, %c> ", ch);
    fprintf(log_output, "Line no %d: Token <CONST_CHAR> Lexeme %s found\n\n", line_count, yytext);
    bool inserted = symbol_table.insert(yytext, "CONST_CHAR");
    if (inserted){symbol_table.printAllScopes();}
}

(\+|-) {
    fprintf(token_output, "<ADDOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <ADDOP> Lexeme %s found\n\n", line_count, yytext);
}

(\*|\/|\%) {
    fprintf(token_output, "<MULOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <MULOP> Lexeme %s found\n\n", line_count, yytext);
}

(\+\+|--) {
    fprintf(token_output, "<INCOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <INCOP> Lexeme %s found\n\n", line_count, yytext);
}

(<|<=|>|>=|==|!=) {
    fprintf(token_output, "<RELOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <RELOP> Lexeme %s found\n\n", line_count, yytext);
}

= {
    fprintf(token_output, "<ASSIGNOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <ASSIGNOP> Lexeme %s found\n\n", line_count, yytext);
}

(&&|\|\|) {
    fprintf(token_output, "<LOGICOP, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <LOGICOP> Lexeme %s found\n\n", line_count, yytext);
}

! {
    fprintf(token_output, "<NOT, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <NOT> Lexeme %s found\n\n", line_count, yytext);
}

\( {
    fprintf(token_output, "<LPAREN, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <LPAREN> Lexeme %s found\n\n", line_count, yytext);
}

\) {
    fprintf(token_output, "<RPAREN, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <RPAREN> Lexeme %s found\n\n", line_count, yytext);
}

\{ {
    fprintf(token_output, "<LCURL, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <LCURL> Lexeme %s found\n\n", line_count, yytext);
    symbol_table.enterScope("sdbm_hash", 7, log_output);
}

\} {
    fprintf(token_output, "<RCURL, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <RCURL> Lexeme %s found\n\n", line_count, yytext);
    symbol_table.exitScope();
}

\[ {
    fprintf(token_output, "<LTHIRD, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <LTHIRD> Lexeme %s found\n\n", line_count, yytext);
}

\] {
    fprintf(token_output, "<RTHIRD, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <RTHIRD> Lexeme %s found\n\n", line_count, yytext);
}

, {
    fprintf(token_output, "<COMMA, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <COMMA> Lexeme %s found\n\n", line_count, yytext);
}

; {
    fprintf(token_output, "<SEMICOLON, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <SEMICOLON> Lexeme %s found\n\n", line_count, yytext);
} 

[a-zA-Z_][a-zA-Z0-9_]* {
    fprintf(token_output, "<ID, %s> ", yytext);
    fprintf(log_output, "Line no %d: Token <ID> Lexeme %s found\n\n", line_count, yytext);
    bool inserted = symbol_table.insert(yytext, "ID");
    if (inserted){symbol_table.printAllScopes();}
}

\" {
    printf("String state began\n");
    BEGIN STRING_STATE;
}

\' {
    printf("Char state began\n");
    BEGIN CHAR_STATE;
}

<INITIAL><<EOF>> {
    symbol_table.printAllScopes();
    fprintf(log_output, "Total lines: %d\n", line_count);
    fprintf(log_output, "Total errors: %d\n", error_count);
    yyterminate();
}

. {
    error_count++;
}

<CHAR_STATE> {

}


<STRING_STATE>{
    \\[\n] {
        line_count++;
    }

    \\[\"] {
        curr_string += '"';
    }

    \" {
        fprintf(token_output, "<STRING, %s> ", curr_string.c_str());
        fprintf(log_output, "Line no %d: Token <STRING> Lexeme \"%s\" found --> <STRING, %s>\n\n", line_count, curr_string.c_str(), curr_string.c_str());
        curr_string = "";
        BEGIN INITIAL;
    }

    [^"] {
        curr_string += yytext;
    }

    <<EOF>> {
        error_count++;
        yyterminate();
    }
}

<COMMENT_STATE_ONE>{
    \\[\n] {
        line_count++;
        curr_comment += '\\';
    }

    [\n] {
        line_count++;
        fprintf(log_output, "Line no: %d, Token <COMMENT> Lexeme %s found\n\n",line_count, curr_comment.c_str());
        curr_comment = "";
        BEGIN INITIAL;
    }

    [^\n] {
        curr_comment += yytext;
    }
}

<COMMENT_STATE_TWO>{
    \*\/ {
        printf("Comment state two ended\n");
        fprintf(log_output, "Line no: %d, Token <COMMENT> Lexeme %s found\n\n",line_count, curr_comment.c_str());
        curr_comment = "";
        BEGIN INITIAL;
    }

    \n {
        line_count++;
        curr_comment += '\n';
    }

    . {
        curr_comment += yytext;
    }

    <<EOF>> {
        error_count++;
        yyterminate();
    }
}
%%

char processCharLiteral(char *lexeme) {
    char ch = lexeme[1];
    
    // Not an escape sequence
    if (ch != '\\') {
        return ch;
    }
    
    // Handle escape sequences
    char escape_ch = lexeme[2];
    switch (escape_ch) {
        case 'n': return '\n';  // Newline (10)
        case 't': return '\t';  // Tab (9)
        case '\\': return '\\'; // Backslash (92)
        case '\'': return '\''; // Single quote (39)
        case 'a': return '\a';  // Alert/Bell (7)
        case 'f': return '\f';  // Form feed (12)
        case 'r': return '\r';  // Carriage return (13)
        case 'b': return '\b';  // Backspace (8)
        case 'v': return '\v';  // Vertical tab (11)
        case '0': return '\0';  // Null character (0)
        default: return escape_ch; // Other escaped character
    }
}

char* toUpperCase(const char* str) {
    static char buffer[256];
    int i = 0;
    while(str[i] && i < 255) {
        buffer[i] = toupper(str[i]);
        i++;
    }
    buffer[i] = '\0';
    return buffer;
}

int main(int argc,char *argv[]){

    if(argc!=2){
        printf("Please provide input file name and try again\n");
        return 0;
    }

    FILE *fin=fopen(argv[1],"r");
    if(fin==NULL){
        printf("Cannot open specified file\n");
        return 0;
    }

    yyin= fin;
    yylex();
    fclose(yyin);
    fclose(token_output);
    fclose(log_output);
    return 0;
}