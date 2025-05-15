%option noyywrap

%{
#include<stdio.h>
#include<stdlib.h>
#include "ReferenceSymbolTable/SymbolTable.cpp"

int line_count=1;

FILE *log_output;
FILE *token_output;

SymbolTable symbol_table("sdbm_hash", 7);

%}

WHITESPACE [ \t\f\r\v]+ 
LETTER [a-zA-Z]
DIGIT [0-9]
NEWLINE \n

%%
{NEWLINE} { line_count++; }

"if" {
    fprintf(token_output,"<IF> ");
    fprintf(log_output,"Line no %d: TOKEN <IF> Lexeme %s found\n",line_count,yytext);
}
"for" {
    fprintf(token_output,"<FOR> ");
    fprintf(log_output,"Line no %d: TOKEN <FOR> Lexeme %s found\n",line_count,yytext);
}
"do" {
    fprintf(token_output,"<DO> ");
    fprintf(log_output,"Line no %d: TOKEN <DO> Lexeme %s found\n",line_count,yytext);
}
"int" {
    fprintf(token_output,"<INT> ");
    fprintf(log_output,"Line no %d: TOKEN <INT> Lexeme %s found\n",line_count,yytext);
}
"float" {
    fprintf(token_output,"<FLOAT> ");
    fprintf(log_output,"Line no %d: TOKEN <FLOAT> Lexeme %s found\n",line_count,yytext);
}
"void" {
    fprintf(token_output,"<VOID> ");
    fprintf(log_output,"Line no %d: TOKEN <VOID> Lexeme %s found\n",line_count,yytext);
}
"switch" {
    fprintf(token_output,"<SWITCH> ");
    fprintf(log_output,"Line no %d: TOKEN <SWITCH> Lexeme %s found\n",line_count,yytext);
}
"default" {
    fprintf(token_output,"<DEFAULT> ");
    fprintf(log_output,"Line no %d: TOKEN <DEFAULT> Lexeme %s found\n",line_count,yytext);
}
"else" {
    fprintf(token_output,"<ELSE> ");
    fprintf(log_output,"Line no %d: TOKEN <ELSE> Lexeme %s found\n",line_count,yytext);
}
"while" {
    fprintf(token_output,"<WHILE> ");
    fprintf(log_output,"Line no %d: TOKEN <WHILE> Lexeme %s found\n",line_count,yytext);
}
"break" {
    fprintf(token_output,"<BREAK> ");
    fprintf(log_output,"Line no %d: TOKEN <BREAK> Lexeme %s found\n",line_count,yytext);
}
"char" {
    fprintf(token_output,"<CHAR> ");
    fprintf(log_output,"Line no %d: TOKEN <CHAR> Lexeme %s found\n",line_count,yytext);
}
"double" {
    fprintf(token_output,"<DOUBLE>");
    fprintf(log_output,"Line no %d: TOKEN <DOUBLE> Lexeme %s found\n",line_count,yytext);
}
"return" {
    fprintf(token_output,"<RETURN>");
    fprintf(log_output,"Line no %d: TOKEN <RETURN> Lexeme %s found\n",line_count,yytext);
}
"case" {
    fprintf(token_output,"<CASE>");
    fprintf(log_output,"Line no %d: TOKEN <CASE> Lexeme %s found\n",line_count,yytext);
}
"continue" {
    fprintf(token_output,"<CONTINUE>");
    fprintf(log_output,"Line no %d: TOKEN <CONTINUE> Lexeme %s found\n",line_count,yytext);
}
"goto" {
    fprintf(token_output,"<GOTO>");
    fprintf(log_output,"Line no %d: TOKEN <GOTO> Lexeme %s found\n",line_count,yytext);
}
"long" {
    fprintf(token_output,"<LONG>");
    fprintf(log_output,"Line no %d: TOKEN <LONG> Lexeme %s found\n",line_count,yytext);
}
"short" {
    fprintf(token_output,"<SHORT>");
    fprintf(log_output,"Line no %d: TOKEN <SHORT> Lexeme %s found\n",line_count,yytext);
}
"static" {
    fprintf(token_output,"<STATIC>");
    fprintf(log_output,"Line no %d: TOKEN <STATIC> Lexeme %s found\n",line_count,yytext);
}
"unsigned" {
    fprintf(token_output,"<UNSIGNED>");
    fprintf(log_output,"Line no %d: TOKEN <UNSIGNED> Lexeme %s found\n",line_count,yytext);
}

{DIGIT}+ {
    fprintf(token_output, "<CONST_INT,%s> ", yytext);
    symbol_table.insert(yytext, "CONST_INT");
}
%%

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

    log_output= fopen("log.txt","w");
    token_output= fopen("token.txt","w");

    yyin= fin;
    yylex();
    fclose(yyin);
    fclose(token_output);
    fclose(log_output);
    return 0;
}