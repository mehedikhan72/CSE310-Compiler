// Generated from /Users/mehedikhan/Academics/3-1/CSE310-Compiler/Parser/Deliverables/2105141/C8086Parser.g4 by ANTLR 4.13.1

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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class C8086Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LINE_COMMENT=1, BLOCK_COMMENT=2, STRING=3, WS=4, IF=5, ELSE=6, FOR=7, 
		WHILE=8, PRINTLN=9, RETURN=10, INT=11, FLOAT=12, VOID=13, LPAREN=14, RPAREN=15, 
		LCURL=16, RCURL=17, LTHIRD=18, RTHIRD=19, SEMICOLON=20, COMMA=21, ADDOP=22, 
		SUBOP=23, MULOP=24, INCOP=25, DECOP=26, NOT=27, RELOP=28, LOGICOP=29, 
		ASSIGNOP=30, ID=31, CONST_INT=32, CONST_FLOAT=33, ERROR_CHAR=34;
	public static final int
		RULE_start = 0, RULE_program = 1, RULE_unit = 2, RULE_func_declaration = 3, 
		RULE_func_definition = 4, RULE_parameter_list = 5, RULE_parameter_list_err = 6, 
		RULE_compound_statement = 7, RULE_var_declaration = 8, RULE_declaration_list_err = 9, 
		RULE_type_specifier = 10, RULE_declaration_list = 11, RULE_statements = 12, 
		RULE_statement = 13, RULE_expression_statement = 14, RULE_variable = 15, 
		RULE_expression = 16, RULE_logic_expression = 17, RULE_rel_expression = 18, 
		RULE_simple_expression = 19, RULE_simple_expression_err = 20, RULE_term = 21, 
		RULE_unary_expression = 22, RULE_factor = 23, RULE_argument_list = 24, 
		RULE_arguments = 25;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "program", "unit", "func_declaration", "func_definition", "parameter_list", 
			"parameter_list_err", "compound_statement", "var_declaration", "declaration_list_err", 
			"type_specifier", "declaration_list", "statements", "statement", "expression_statement", 
			"variable", "expression", "logic_expression", "rel_expression", "simple_expression", 
			"simple_expression_err", "term", "unary_expression", "factor", "argument_list", 
			"arguments"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'if'", "'else'", "'for'", "'while'", null, 
			"'return'", "'int'", "'float'", "'void'", "'('", "')'", "'{'", "'}'", 
			"'['", "']'", "';'", "','", null, null, null, "'++'", "'--'", "'!'", 
			null, null, "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LINE_COMMENT", "BLOCK_COMMENT", "STRING", "WS", "IF", "ELSE", 
			"FOR", "WHILE", "PRINTLN", "RETURN", "INT", "FLOAT", "VOID", "LPAREN", 
			"RPAREN", "LCURL", "RCURL", "LTHIRD", "RTHIRD", "SEMICOLON", "COMMA", 
			"ADDOP", "SUBOP", "MULOP", "INCOP", "DECOP", "NOT", "RELOP", "LOGICOP", 
			"ASSIGNOP", "ID", "CONST_INT", "CONST_FLOAT", "ERROR_CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "C8086Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


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

	public C8086Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public ProgramContext program;
		public ProgramContext program() {
			return getRuleContext(ProgramContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			((StartContext)_localctx).program = program(0);

			        writeIntoParserLogFile("Line " + to_string((((StartContext)_localctx).program!=null?(((StartContext)_localctx).program.stop):null)->getLine()) + ": start : program\n");
			        symbol_table.printAllScopes();
			        writeIntoParserLogFile("Total number of lines: " + to_string((((StartContext)_localctx).program!=null?(((StartContext)_localctx).program.stop):null)->getLine()));
			        writeIntoParserLogFile("Total number of errors: " + to_string(syntaxErrorCount));
			        symbol_table.testPrint();
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public string text;
		public ProgramContext p;
		public UnitContext u;
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
		public ProgramContext program() {
			return getRuleContext(ProgramContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		return program(0);
	}

	private ProgramContext program(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ProgramContext _localctx = new ProgramContext(_ctx, _parentState);
		ProgramContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_program, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(56);
			((ProgramContext)_localctx).u = unit();

			        ((ProgramContext)_localctx).text =  ((ProgramContext)_localctx).u.text;
			        writeIntoParserLogFile("Line " + to_string((((ProgramContext)_localctx).u!=null?(((ProgramContext)_localctx).u.stop):null)->getLine()) + ": program : unit\n");
			        writeIntoParserLogFile(_localctx.text + "\n");
			    
			}
			_ctx.stop = _input.LT(-1);
			setState(65);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ProgramContext(_parentctx, _parentState);
					_localctx.p = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_program);
					setState(59);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(60);
					((ProgramContext)_localctx).u = unit();

					                  ((ProgramContext)_localctx).text =  ((ProgramContext)_localctx).p.text + "\n" + ((ProgramContext)_localctx).u.text;
					                  writeIntoParserLogFile("Line " + to_string((((ProgramContext)_localctx).u!=null?(((ProgramContext)_localctx).u.stop):null)->getLine()) + ": program : program unit\n");
					                  writeIntoParserLogFile(_localctx.text + "\n");
					              
					}
					} 
				}
				setState(67);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitContext extends ParserRuleContext {
		public string text;
		public Var_declarationContext vd;
		public Func_declarationContext fd;
		public Func_definitionContext fde;
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public Func_declarationContext func_declaration() {
			return getRuleContext(Func_declarationContext.class,0);
		}
		public Func_definitionContext func_definition() {
			return getRuleContext(Func_definitionContext.class,0);
		}
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_unit);
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				((UnitContext)_localctx).vd = var_declaration();

						((UnitContext)_localctx).text =  ((UnitContext)_localctx).vd.text;
						writeIntoParserLogFile("Line " + to_string((((UnitContext)_localctx).vd!=null?(((UnitContext)_localctx).vd.stop):null)->getLine()) + ": unit : var_declaration\n");
						writeIntoParserLogFile(_localctx.text + "\n");

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				((UnitContext)_localctx).fd = func_declaration();

						((UnitContext)_localctx).text =  ((UnitContext)_localctx).fd.text;
						writeIntoParserLogFile("Line " + to_string((((UnitContext)_localctx).fd!=null?(((UnitContext)_localctx).fd.stop):null)->getLine()) + ": unit : func_declaration\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(74);
				((UnitContext)_localctx).fde = func_definition();

				        ((UnitContext)_localctx).text =  ((UnitContext)_localctx).fde.text;
				        writeIntoParserLogFile("Line " + to_string((((UnitContext)_localctx).fde!=null?(((UnitContext)_localctx).fde.stop):null)->getLine()) + ": unit : func_definition\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				     
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Func_declarationContext extends ParserRuleContext {
		public string text;
		public Type_specifierContext ts;
		public Token ID;
		public Parameter_listContext parameter_list;
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C8086Parser.LPAREN, 0); }
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(C8086Parser.RPAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(C8086Parser.SEMICOLON, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Func_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_declaration; }
	}

	public final Func_declarationContext func_declaration() throws RecognitionException {
		Func_declarationContext _localctx = new Func_declarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_func_declaration);
		try {
			setState(96);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				((Func_declarationContext)_localctx).ts = type_specifier();
				setState(80);
				((Func_declarationContext)_localctx).ID = match(ID);
				setState(81);
				match(LPAREN);

				            function_declaration_reading = true;
				    
				setState(83);
				((Func_declarationContext)_localctx).parameter_list = parameter_list(0);
				setState(84);
				match(RPAREN);
				setState(85);
				match(SEMICOLON);

							((Func_declarationContext)_localctx).text =  ((Func_declarationContext)_localctx).ts.text + " " + ((Func_declarationContext)_localctx).ID->getText() + "(" + ((Func_declarationContext)_localctx).parameter_list.text + ");";
				            int param_count = getParamCount(((Func_declarationContext)_localctx).parameter_list.text);
				            if(symbol_table.insert(((Func_declarationContext)_localctx).ID->getText(), "ID", ((Func_declarationContext)_localctx).ts.text, ((Func_declarationContext)_localctx).ts.text, ((Func_declarationContext)_localctx).parameter_list.text, param_count) == false) {
				                string error_text = "Error at line " + to_string(((Func_declarationContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Func_declarationContext)_localctx).ID->getText() + "\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            };
							writeIntoParserLogFile("Line " + to_string((((Func_declarationContext)_localctx).ts!=null?(((Func_declarationContext)_localctx).ts.start):null)->getLine()) + ": func_declaration : type_specifier ID LPAREN parameter_list RPAREN SEMICOLON\n");
							writeIntoParserLogFile(_localctx.text + "\n");
				            function_declaration_reading = false; 
						
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				((Func_declarationContext)_localctx).ts = type_specifier();
				setState(89);
				((Func_declarationContext)_localctx).ID = match(ID);
				setState(90);
				match(LPAREN);

				            function_declaration_reading = true;
				        
				setState(92);
				match(RPAREN);
				setState(93);
				match(SEMICOLON);

							((Func_declarationContext)_localctx).text =  ((Func_declarationContext)_localctx).ts.text + " " + ((Func_declarationContext)_localctx).ID->getText() + "();";
				            if(symbol_table.insert(((Func_declarationContext)_localctx).ID->getText(), "ID", ((Func_declarationContext)_localctx).ts.text, ((Func_declarationContext)_localctx).ts.text, "", 0) == false) {
				                string error_text = "Error at line " + to_string(((Func_declarationContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Func_declarationContext)_localctx).ID->getText() + "\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            };
							writeIntoParserLogFile("Line " + to_string((((Func_declarationContext)_localctx).ts!=null?(((Func_declarationContext)_localctx).ts.start):null)->getLine()) + ": func_declaration : type_specifier ID LPAREN RPAREN SEMICOLON\n");
							writeIntoParserLogFile(_localctx.text + "\n");
				            function_declaration_reading = false;
						
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Func_definitionContext extends ParserRuleContext {
		public string text;
		public Type_specifierContext ts;
		public Token ID;
		public Parameter_listContext pl;
		public Compound_statementContext cs;
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C8086Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C8086Parser.RPAREN, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public Func_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_definition; }
	}

	public final Func_definitionContext func_definition() throws RecognitionException {
		Func_definitionContext _localctx = new Func_definitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_func_definition);
		try {
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(98);
				((Func_definitionContext)_localctx).ts = type_specifier();
				setState(99);
				((Func_definitionContext)_localctx).ID = match(ID);
				setState(100);
				match(LPAREN);

				            if(symbol_table.insert(((Func_definitionContext)_localctx).ID->getText(), "ID", ((Func_definitionContext)_localctx).ts.text, ((Func_definitionContext)_localctx).ts.text) == false) {
				                // string error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                // writeIntoErrorFile(error_text);
				                // writeIntoParserLogFile(error_text);
				                // syntaxErrorCount++;
				            } else {
				                // this function did not have a declaration.
				                func_without_declaration = true;
				                func_without_declaration_id = ((Func_definitionContext)_localctx).ID->getText();
				            }

				            // return type mismatch check
				            SymbolInfo *symbol = symbol_table.lookup(((Func_definitionContext)_localctx).ID->getText());
				            if(symbol != nullptr) {
				                string func_return_type = symbol->getFuncRetType();
				                cout << "Function return type: " << func_return_type << endl;
				                cout << "Expected return type: " << ((Func_definitionContext)_localctx).ts.text << endl;
				                if(func_return_type != ((Func_definitionContext)_localctx).ts.text) {
				                    return_type_mismatch = true;
				                }
				            }

				            symbol_table.enterScope();
				            fp_and_no_lcurl_found = true; 
				            current_func_id = ((Func_definitionContext)_localctx).ID->getText(); 
				        
				setState(102);
				((Func_definitionContext)_localctx).pl = parameter_list(0);

				            if(return_type_mismatch) {
				                string error_text = "";
				                SymbolInfo *symbol = symbol_table.lookup(((Func_definitionContext)_localctx).ID->getText());
				                if(symbol != nullptr) {
				                    if(symbol->getFuncRetType() == "") {
				                        // not a return type mismatch, rather redeclaration. since no ret type was found hence not a function.
				                        error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                        skip_func_validation = true;
				                    } else {
				                        error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                    }
				                }
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				                return_type_mismatch = false;
				            }
				            if(!func_without_declaration && !skip_func_validation) {
				                SymbolInfo *symbol2 = symbol_table.lookup(((Func_definitionContext)_localctx).ID->getText());
				                if(symbol2 != nullptr) {
				                    string actual_func_param_list = symbol2->getFuncArgs();
				                    if(actual_func_param_list != ((Func_definitionContext)_localctx).pl.text) {
				                        string error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Total number of arguments mismatch with declaration in function " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                        writeIntoErrorFile(error_text);
				                        writeIntoParserLogFile(error_text);
				                        syntaxErrorCount++;
				                    }
				                } 
				            }
				            current_func_param_list = ((Func_definitionContext)_localctx).pl.text;
				        
				setState(104);
				match(RPAREN);
				setState(105);
				((Func_definitionContext)_localctx).cs = compound_statement();

				            if(error_text_1 != "") {
				                writeIntoErrorFile("Error at line " + to_string((((Func_definitionContext)_localctx).cs!=null?(((Func_definitionContext)_localctx).cs.stop):null)->getLine()) + ": " + error_text_1);
				                writeIntoParserLogFile("Error at line " + to_string((((Func_definitionContext)_localctx).cs!=null?(((Func_definitionContext)_localctx).cs.stop):null)->getLine()) + ": " + error_text_1);
				                syntaxErrorCount++;
				                error_text_1 = "";
				            }
				            ((Func_definitionContext)_localctx).text =  ((Func_definitionContext)_localctx).ts.text + " " + ((Func_definitionContext)_localctx).ID->getText() + "(" + ((Func_definitionContext)_localctx).pl.text + ")" + ((Func_definitionContext)_localctx).cs.text;
				            writeIntoParserLogFile("Line " + to_string((((Func_definitionContext)_localctx).cs!=null?(((Func_definitionContext)_localctx).cs.stop):null)->getLine()) + ": func_definition : type_specifier ID LPAREN parameter_list RPAREN compound_statement\n");
				            writeIntoParserLogFile(_localctx.text + "\n");
				        
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				((Func_definitionContext)_localctx).ts = type_specifier();
				setState(109);
				((Func_definitionContext)_localctx).ID = match(ID);
				setState(110);
				match(LPAREN);

				            if(symbol_table.insert(((Func_definitionContext)_localctx).ID->getText(), "ID", ((Func_definitionContext)_localctx).ts.text, ((Func_definitionContext)_localctx).ts.text) == false) {
				                // string error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                // writeIntoErrorFile(error_text);
				                // writeIntoParserLogFile(error_text);
				                // syntaxErrorCount++;
				            };

				            // return type mismatch check
				            SymbolInfo *symbol = symbol_table.lookup(((Func_definitionContext)_localctx).ID->getText());
				            if(symbol != nullptr) {
				                string func_return_type = symbol->getFuncRetType();
				                if(func_return_type != ((Func_definitionContext)_localctx).ts.text) {
				                    string error_text = "Error at line " + to_string(((Func_definitionContext)_localctx).ID->getLine()) + ": Return type mismatch of " + ((Func_definitionContext)_localctx).ID->getText() + "\n";
				                    writeIntoErrorFile(error_text);
				                    writeIntoParserLogFile(error_text);
				                    syntaxErrorCount++;
				                }
				            }
				            
				            symbol_table.enterScope();
				            fp_and_no_lcurl_found = true;
				            current_func_id = ((Func_definitionContext)_localctx).ID->getText();
				        
				setState(112);
				match(RPAREN);
				setState(113);
				((Func_definitionContext)_localctx).cs = compound_statement();

				            ((Func_definitionContext)_localctx).text =  ((Func_definitionContext)_localctx).ts.text + " " + ((Func_definitionContext)_localctx).ID->getText() + "()" + ((Func_definitionContext)_localctx).cs.text;
				            writeIntoParserLogFile("Line " + to_string((((Func_definitionContext)_localctx).cs!=null?(((Func_definitionContext)_localctx).cs.stop):null)->getLine()) + ": func_definition : type_specifier ID LPAREN RPAREN compound_statement\n");
				            writeIntoParserLogFile(_localctx.text + "\n");
				        
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Parameter_listContext extends ParserRuleContext {
		public string text;
		public Parameter_listContext pl;
		public Type_specifierContext ts;
		public Token ID;
		public Parameter_list_errContext ple;
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Parameter_list_errContext parameter_list_err() {
			return getRuleContext(Parameter_list_errContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(C8086Parser.COMMA, 0); }
		public Parameter_listContext parameter_list() {
			return getRuleContext(Parameter_listContext.class,0);
		}
		public Parameter_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_list; }
	}

	public final Parameter_listContext parameter_list() throws RecognitionException {
		return parameter_list(0);
	}

	private Parameter_listContext parameter_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Parameter_listContext _localctx = new Parameter_listContext(_ctx, _parentState);
		Parameter_listContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_parameter_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(119);
				((Parameter_listContext)_localctx).ts = type_specifier();
				setState(120);
				((Parameter_listContext)_localctx).ID = match(ID);

							((Parameter_listContext)_localctx).text =  ((Parameter_listContext)_localctx).ts.text + " " + ((Parameter_listContext)_localctx).ID->getText();
							if(!function_declaration_reading && symbol_table.insert(((Parameter_listContext)_localctx).ID->getText(), "ID", ((Parameter_listContext)_localctx).ts.text) == false) {
				                string error_text = "Error at line " + to_string(((Parameter_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Parameter_listContext)_localctx).ID->getText() + " in parameter\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            };
							writeIntoParserLogFile("Line " + to_string((((Parameter_listContext)_localctx).ts!=null?(((Parameter_listContext)_localctx).ts.start):null)->getLine()) + ": parameter_list : type_specifier ID\n");
							writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			case 2:
				{
				setState(123);
				((Parameter_listContext)_localctx).ts = type_specifier();

							((Parameter_listContext)_localctx).text =  ((Parameter_listContext)_localctx).ts.text;
							writeIntoParserLogFile("Line " + to_string((((Parameter_listContext)_localctx).ts!=null?(((Parameter_listContext)_localctx).ts.start):null)->getLine()) + ": parameter_list : type_specifier\n");
							writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			case 3:
				{
				setState(126);
				((Parameter_listContext)_localctx).ple = parameter_list_err();

				            writeIntoParserLogFile(((Parameter_listContext)_localctx).ple.error);
				            writeIntoErrorFile(((Parameter_listContext)_localctx).ple.error);
				            syntaxErrorCount++;
				        
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(142);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(140);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new Parameter_listContext(_parentctx, _parentState);
						_localctx.pl = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_parameter_list);
						setState(131);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(132);
						match(COMMA);
						setState(133);
						((Parameter_listContext)_localctx).ts = type_specifier();
						setState(134);
						((Parameter_listContext)_localctx).ID = match(ID);

						          			((Parameter_listContext)_localctx).text =  ((Parameter_listContext)_localctx).pl.text + "," + ((Parameter_listContext)_localctx).ts.text + " " + ((Parameter_listContext)_localctx).ID->getText();
						          			if(!function_declaration_reading && symbol_table.insert(((Parameter_listContext)_localctx).ID->getText(), "ID", ((Parameter_listContext)_localctx).ts.text) == false) {
						                          string error_text = "Error at line " + to_string(((Parameter_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Parameter_listContext)_localctx).ID->getText() + " in parameter\n";
						                          writeIntoErrorFile(error_text);
						                          writeIntoParserLogFile(error_text);
						                          syntaxErrorCount++;
						                      };
						          			writeIntoParserLogFile("Line " + to_string((((Parameter_listContext)_localctx).ts!=null?(((Parameter_listContext)_localctx).ts.start):null)->getLine()) + ": parameter_list : parameter_list COMMA type_specifier ID\n");
						          			writeIntoParserLogFile(_localctx.text + "\n");
						          		
						}
						break;
					case 2:
						{
						_localctx = new Parameter_listContext(_parentctx, _parentState);
						_localctx.pl = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_parameter_list);
						setState(137);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(138);
						match(COMMA);

						          			((Parameter_listContext)_localctx).text =  ((Parameter_listContext)_localctx).pl.text + ",";
						          			writeIntoParserLogFile("Line " + to_string((((Parameter_listContext)_localctx).pl!=null?(((Parameter_listContext)_localctx).pl.stop):null)->getLine()) + ": parameter_list : parameter_list COMMA\n");
						          			writeIntoParserLogFile(_localctx.text + "\n");
						          		
						}
						break;
					}
					} 
				}
				setState(144);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Parameter_list_errContext extends ParserRuleContext {
		public string error;
		public Type_specifierContext ts;
		public TerminalNode ADDOP() { return getToken(C8086Parser.ADDOP, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Parameter_list_errContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_list_err; }
	}

	public final Parameter_list_errContext parameter_list_err() throws RecognitionException {
		Parameter_list_errContext _localctx = new Parameter_list_errContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parameter_list_err);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			((Parameter_list_errContext)_localctx).ts = type_specifier();
			setState(146);
			match(ADDOP);

			        ((Parameter_list_errContext)_localctx).error =  "Error at line " + to_string((((Parameter_list_errContext)_localctx).ts!=null?(((Parameter_list_errContext)_localctx).ts.start):null)->getLine()) + ": syntax error, unexpected ADDOP, expecting RPAREN or COMMA\n";
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Compound_statementContext extends ParserRuleContext {
		public string text;
		public StatementsContext statements;
		public Token RCURL;
		public TerminalNode LCURL() { return getToken(C8086Parser.LCURL, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(C8086Parser.RCURL, 0); }
		public Compound_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_statement; }
	}

	public final Compound_statementContext compound_statement() throws RecognitionException {
		Compound_statementContext _localctx = new Compound_statementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_compound_statement);
		try {
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
				match(LCURL);

				        if(fp_and_no_lcurl_found) {
				            fp_and_no_lcurl_found = false;
				        } else {
				            symbol_table.enterScope();
				        }
				    
				setState(151);
				((Compound_statementContext)_localctx).statements = statements(0);
				setState(152);
				((Compound_statementContext)_localctx).RCURL = match(RCURL);

						((Compound_statementContext)_localctx).text =  "{\n" + ((Compound_statementContext)_localctx).statements.text + "\n}";
						writeIntoParserLogFile("Line " + to_string(((Compound_statementContext)_localctx).RCURL->getLine()) + ": compound_statement : LCURL statements RCURL\n");
						writeIntoParserLogFile(_localctx.text + "\n");
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
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				match(LCURL);
				setState(156);
				((Compound_statementContext)_localctx).RCURL = match(RCURL);

						((Compound_statementContext)_localctx).text =  "{}";
						writeIntoParserLogFile("Line " + to_string(((Compound_statementContext)_localctx).RCURL->getLine()) + ": compound_statement : LCURL RCURL\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Var_declarationContext extends ParserRuleContext {
		public string text;
		public Type_specifierContext t;
		public Declaration_listContext dl;
		public Token sm;
		public Declaration_list_errContext dle;
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Declaration_listContext declaration_list() {
			return getRuleContext(Declaration_listContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(C8086Parser.SEMICOLON, 0); }
		public Declaration_list_errContext declaration_list_err() {
			return getRuleContext(Declaration_list_errContext.class,0);
		}
		public Var_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_declaration; }
	}

	public final Var_declarationContext var_declaration() throws RecognitionException {
		Var_declarationContext _localctx = new Var_declarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_var_declaration);
		try {
			setState(173);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				((Var_declarationContext)_localctx).t = type_specifier();
				setState(161);
				((Var_declarationContext)_localctx).dl = declaration_list(0);
				setState(162);
				((Var_declarationContext)_localctx).sm = match(SEMICOLON);

						((Var_declarationContext)_localctx).text =  ((Var_declarationContext)_localctx).t.text + " " + ((Var_declarationContext)_localctx).dl.text + ";";
						writeIntoParserLogFile("Line " + to_string(((Var_declarationContext)_localctx).sm->getLine()) + ": var_declaration : type_specifier declaration_list SEMICOLON\n");
				        if(((Var_declarationContext)_localctx).t.text == "void") {
				            string error_text = "Error at line " + to_string((((Var_declarationContext)_localctx).t!=null?(((Var_declarationContext)_localctx).t.start):null)->getLine()) + ": Variable type cannot be void\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        }
				        writeIntoParserLogFile(_localctx.text + "\n");
				      
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				((Var_declarationContext)_localctx).t = type_specifier();
				setState(166);
				((Var_declarationContext)_localctx).dle = declaration_list_err();
				setState(167);
				((Var_declarationContext)_localctx).sm = match(SEMICOLON);

				        writeIntoErrorFile(((Var_declarationContext)_localctx).dle.error);
				        writeIntoParserLogFile(((Var_declarationContext)_localctx).dle.error);
				        syntaxErrorCount++;
				      
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(170);
				((Var_declarationContext)_localctx).dle = declaration_list_err();

				        writeIntoErrorFile(((Var_declarationContext)_localctx).dle.error);
				        writeIntoParserLogFile(((Var_declarationContext)_localctx).dle.error);
				        syntaxErrorCount++;
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Declaration_list_errContext extends ParserRuleContext {
		public string error;
		public Type_specifierContext ts;
		public VariableContext v;
		public TerminalNode ADDOP() { return getToken(C8086Parser.ADDOP, 0); }
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public Declaration_list_errContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_list_err; }
	}

	public final Declaration_list_errContext declaration_list_err() throws RecognitionException {
		Declaration_list_errContext _localctx = new Declaration_list_errContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_declaration_list_err);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			((Declaration_list_errContext)_localctx).ts = type_specifier();
			setState(176);
			((Declaration_list_errContext)_localctx).v = variable();
			setState(177);
			match(ADDOP);

			        ((Declaration_list_errContext)_localctx).error =  "Error at line " + to_string((((Declaration_list_errContext)_localctx).ts!=null?(((Declaration_list_errContext)_localctx).ts.start):null)->getLine()) + ": syntax error, unexpected ADDOP, expecting COMMA or SEMICOLON\n";
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Type_specifierContext extends ParserRuleContext {
		public string text;
		public Token INT;
		public Token FLOAT;
		public Token VOID;
		public TerminalNode INT() { return getToken(C8086Parser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(C8086Parser.FLOAT, 0); }
		public TerminalNode VOID() { return getToken(C8086Parser.VOID, 0); }
		public Type_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_specifier; }
	}

	public final Type_specifierContext type_specifier() throws RecognitionException {
		Type_specifierContext _localctx = new Type_specifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_type_specifier);
		try {
			setState(186);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(180);
				((Type_specifierContext)_localctx).INT = match(INT);

				            ((Type_specifierContext)_localctx).text =  "int";
							writeIntoParserLogFile("Line " + to_string(((Type_specifierContext)_localctx).INT->getLine()) + ": type_specifier : INT\n");
							writeIntoParserLogFile(((Type_specifierContext)_localctx).INT->getText() + "\n");
				            current_type = "int";
				        
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(182);
				((Type_specifierContext)_localctx).FLOAT = match(FLOAT);

				            ((Type_specifierContext)_localctx).text =  "float";
							writeIntoParserLogFile("Line " + to_string(((Type_specifierContext)_localctx).FLOAT->getLine()) + ": type_specifier : FLOAT\n");
							writeIntoParserLogFile(((Type_specifierContext)_localctx).FLOAT->getText() + "\n");
				            current_type = "float";
				        
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 3);
				{
				setState(184);
				((Type_specifierContext)_localctx).VOID = match(VOID);

				            ((Type_specifierContext)_localctx).text =  "void";
							writeIntoParserLogFile("Line " + to_string(((Type_specifierContext)_localctx).VOID->getLine()) + ": type_specifier : VOID\n");
							writeIntoParserLogFile(((Type_specifierContext)_localctx).VOID->getText() + "\n");
				            current_type = "void";
				        
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Declaration_listContext extends ParserRuleContext {
		public string text;
		public Declaration_listContext d;
		public Token ID;
		public Token CONST_INT;
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode LTHIRD() { return getToken(C8086Parser.LTHIRD, 0); }
		public TerminalNode CONST_INT() { return getToken(C8086Parser.CONST_INT, 0); }
		public TerminalNode RTHIRD() { return getToken(C8086Parser.RTHIRD, 0); }
		public TerminalNode COMMA() { return getToken(C8086Parser.COMMA, 0); }
		public Declaration_listContext declaration_list() {
			return getRuleContext(Declaration_listContext.class,0);
		}
		public Declaration_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_list; }
	}

	public final Declaration_listContext declaration_list() throws RecognitionException {
		return declaration_list(0);
	}

	private Declaration_listContext declaration_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Declaration_listContext _localctx = new Declaration_listContext(_ctx, _parentState);
		Declaration_listContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_declaration_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(189);
				((Declaration_listContext)_localctx).ID = match(ID);

							((Declaration_listContext)_localctx).text =  ((Declaration_listContext)_localctx).ID->getText();
							if(symbol_table.insert(((Declaration_listContext)_localctx).ID->getText(), "ID", current_type) == false) {
				                string error_text = "Error at line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Declaration_listContext)_localctx).ID->getText() + "\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            };
							writeIntoParserLogFile("Line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": declaration_list : ID\n");
							writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			case 2:
				{
				setState(191);
				((Declaration_listContext)_localctx).ID = match(ID);
				setState(192);
				match(LTHIRD);
				setState(193);
				((Declaration_listContext)_localctx).CONST_INT = match(CONST_INT);
				setState(194);
				match(RTHIRD);

							((Declaration_listContext)_localctx).text =  ((Declaration_listContext)_localctx).ID->getText() + "[" + ((Declaration_listContext)_localctx).CONST_INT->getText() + "]";
							if(symbol_table.insert(((Declaration_listContext)_localctx).ID->getText(), "ID", "array") == false) {
				                string error_text = "Error at line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Declaration_listContext)_localctx).ID->getText() + "\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            };
							writeIntoParserLogFile("Line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": declaration_list : ID LTHIRD CONST_INT RTHIRD\n");
							writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(211);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(209);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new Declaration_listContext(_parentctx, _parentState);
						_localctx.d = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_declaration_list);
						setState(198);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(199);
						match(COMMA);
						setState(200);
						((Declaration_listContext)_localctx).ID = match(ID);

						          			((Declaration_listContext)_localctx).text =  ((Declaration_listContext)_localctx).d.text + "," + ((Declaration_listContext)_localctx).ID->getText();
						                      if(symbol_table.insert(((Declaration_listContext)_localctx).ID->getText(), "ID", current_type) == false) {
						                          string error_text = "Error at line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Declaration_listContext)_localctx).ID->getText() + "\n";
						                          writeIntoErrorFile(error_text);
						                          writeIntoParserLogFile(error_text);
						                          syntaxErrorCount++;
						                      };
						          			writeIntoParserLogFile("Line " + to_string((((Declaration_listContext)_localctx).d!=null?(((Declaration_listContext)_localctx).d.stop):null)->getLine()) + ": declaration_list : declaration_list COMMA ID\n");
						          			writeIntoParserLogFile(_localctx.text + "\n");
						          		
						}
						break;
					case 2:
						{
						_localctx = new Declaration_listContext(_parentctx, _parentState);
						_localctx.d = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_declaration_list);
						setState(202);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(203);
						match(COMMA);
						setState(204);
						((Declaration_listContext)_localctx).ID = match(ID);
						setState(205);
						match(LTHIRD);
						setState(206);
						((Declaration_listContext)_localctx).CONST_INT = match(CONST_INT);
						setState(207);
						match(RTHIRD);

						          			((Declaration_listContext)_localctx).text =  ((Declaration_listContext)_localctx).d.text + "," + ((Declaration_listContext)_localctx).ID->getText() + "[" + ((Declaration_listContext)_localctx).CONST_INT->getText() + "]";
						          			if(symbol_table.insert(((Declaration_listContext)_localctx).ID->getText(), "ID", "array") == false) {
						                          string error_text = "Error at line " + to_string(((Declaration_listContext)_localctx).ID->getLine()) + ": Multiple declaration of " + ((Declaration_listContext)_localctx).ID->getText() + "\n";
						                          writeIntoErrorFile(error_text);
						                          writeIntoParserLogFile(error_text);
						                          syntaxErrorCount++;
						                      };
						          			writeIntoParserLogFile("Line " + to_string((((Declaration_listContext)_localctx).d!=null?(((Declaration_listContext)_localctx).d.stop):null)->getLine()) + ": declaration_list : declaration_list COMMA ID LTHIRD CONST_INT RTHIRD\n");
						          			writeIntoParserLogFile(_localctx.text + "\n");
						          		
						}
						break;
					}
					} 
				}
				setState(213);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsContext extends ParserRuleContext {
		public string text;
		public StatementsContext st;
		public StatementContext s;
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
	}

	public final StatementsContext statements() throws RecognitionException {
		return statements(0);
	}

	private StatementsContext statements(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatementsContext _localctx = new StatementsContext(_ctx, _parentState);
		StatementsContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_statements, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(215);
			((StatementsContext)_localctx).s = statement();

			        ((StatementsContext)_localctx).text =  ((StatementsContext)_localctx).s.text;
			        writeIntoParserLogFile("Line " + to_string((((StatementsContext)_localctx).s!=null?(((StatementsContext)_localctx).s.stop):null)->getLine()) + ": statements : statement\n");
			        writeIntoParserLogFile(_localctx.text + "\n");
			    
			}
			_ctx.stop = _input.LT(-1);
			setState(224);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new StatementsContext(_parentctx, _parentState);
					_localctx.st = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_statements);
					setState(218);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(219);
					((StatementsContext)_localctx).s = statement();

					                  ((StatementsContext)_localctx).text =  ((StatementsContext)_localctx).st.text + "\n" + ((StatementsContext)_localctx).s.text;
					                  writeIntoParserLogFile("Line " + to_string((((StatementsContext)_localctx).s!=null?(((StatementsContext)_localctx).s.stop):null)->getLine()) + ": statements : statements statement\n");
					                  writeIntoParserLogFile(_localctx.text + "\n");
					              
					}
					} 
				}
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public string text;
		public Var_declarationContext vd;
		public Expression_statementContext es;
		public Compound_statementContext cs;
		public Expression_statementContext es1;
		public Expression_statementContext es2;
		public ExpressionContext e;
		public StatementContext s;
		public StatementContext s1;
		public StatementContext s2;
		public Token id;
		public Token RPAREN;
		public Token RETURN;
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public List<Expression_statementContext> expression_statement() {
			return getRuleContexts(Expression_statementContext.class);
		}
		public Expression_statementContext expression_statement(int i) {
			return getRuleContext(Expression_statementContext.class,i);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public TerminalNode FOR() { return getToken(C8086Parser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(C8086Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C8086Parser.RPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode IF() { return getToken(C8086Parser.IF, 0); }
		public TerminalNode ELSE() { return getToken(C8086Parser.ELSE, 0); }
		public TerminalNode WHILE() { return getToken(C8086Parser.WHILE, 0); }
		public TerminalNode PRINTLN() { return getToken(C8086Parser.PRINTLN, 0); }
		public TerminalNode SEMICOLON() { return getToken(C8086Parser.SEMICOLON, 0); }
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode RETURN() { return getToken(C8086Parser.RETURN, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statement);
		try {
			setState(279);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(227);
				((StatementContext)_localctx).vd = var_declaration();

				        ((StatementContext)_localctx).text =  ((StatementContext)_localctx).vd.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).vd!=null?(((StatementContext)_localctx).vd.start):null)->getLine()) + ": statement : var_declaration\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				((StatementContext)_localctx).es = expression_statement();

				        ((StatementContext)_localctx).text =  ((StatementContext)_localctx).es.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).es!=null?(((StatementContext)_localctx).es.start):null)->getLine()) + ": statement : expression_statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(233);
				((StatementContext)_localctx).cs = compound_statement();

				        ((StatementContext)_localctx).text =  ((StatementContext)_localctx).cs.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).cs!=null?(((StatementContext)_localctx).cs.stop):null)->getLine()) + ": statement : compound_statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(236);
				match(FOR);
				setState(237);
				match(LPAREN);
				setState(238);
				((StatementContext)_localctx).es1 = expression_statement();
				setState(239);
				((StatementContext)_localctx).es2 = expression_statement();
				setState(240);
				((StatementContext)_localctx).e = expression();
				setState(241);
				match(RPAREN);
				setState(242);
				((StatementContext)_localctx).s = statement();

				        ((StatementContext)_localctx).text =  "for(" + ((StatementContext)_localctx).es1.text + ((StatementContext)_localctx).es2.text + ((StatementContext)_localctx).e.text + ")" + ((StatementContext)_localctx).s.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).s!=null?(((StatementContext)_localctx).s.stop):null)->getLine()) + ": statement : FOR LPAREN expression_statement expression_statement expression RPAREN statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(245);
				match(IF);
				setState(246);
				match(LPAREN);
				setState(247);
				((StatementContext)_localctx).e = expression();
				setState(248);
				match(RPAREN);
				setState(249);
				((StatementContext)_localctx).s = statement();

				        ((StatementContext)_localctx).text =  "if(" + ((StatementContext)_localctx).e.text + ")" + ((StatementContext)_localctx).s.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).s!=null?(((StatementContext)_localctx).s.stop):null)->getLine()) + ": statement : IF LPAREN expression RPAREN statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(252);
				match(IF);
				setState(253);
				match(LPAREN);
				setState(254);
				((StatementContext)_localctx).e = expression();
				setState(255);
				match(RPAREN);
				setState(256);
				((StatementContext)_localctx).s1 = statement();
				setState(257);
				match(ELSE);
				setState(258);
				((StatementContext)_localctx).s2 = statement();

				        ((StatementContext)_localctx).text =  "if(" + ((StatementContext)_localctx).e.text + ")" + ((StatementContext)_localctx).s1.text + "else " + ((StatementContext)_localctx).s2.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).s2!=null?(((StatementContext)_localctx).s2.stop):null)->getLine()) + ": statement : IF LPAREN expression RPAREN statement ELSE statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(261);
				match(WHILE);
				setState(262);
				match(LPAREN);
				setState(263);
				((StatementContext)_localctx).e = expression();
				setState(264);
				match(RPAREN);
				setState(265);
				((StatementContext)_localctx).s = statement();

				        ((StatementContext)_localctx).text =  "while(" + ((StatementContext)_localctx).e.text + ")" + ((StatementContext)_localctx).s.text;
				        writeIntoParserLogFile("Line " + to_string((((StatementContext)_localctx).s!=null?(((StatementContext)_localctx).s.stop):null)->getLine()) + ": statement : WHILE LPAREN expression RPAREN statement\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(268);
				match(PRINTLN);
				setState(269);
				match(LPAREN);
				setState(270);
				((StatementContext)_localctx).id = match(ID);
				setState(271);
				((StatementContext)_localctx).RPAREN = match(RPAREN);
				setState(272);
				match(SEMICOLON);

				        ((StatementContext)_localctx).text =  "printf(" + ((StatementContext)_localctx).id->getText() + ");";
				        writeIntoParserLogFile("Line " + to_string(((StatementContext)_localctx).RPAREN->getLine()) + ": statement : PRINTLN LPAREN ID RPAREN SEMICOLON\n");
				        SymbolInfo *symbol = symbol_table.lookup(((StatementContext)_localctx).id->getText());
				        if(symbol == nullptr) {
				            string error_text = "Error at line " + to_string(((StatementContext)_localctx).id->getLine()) + ": Undeclared variable " + ((StatementContext)_localctx).id->getText() + "\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        } 
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(274);
				((StatementContext)_localctx).RETURN = match(RETURN);
				setState(275);
				((StatementContext)_localctx).e = expression();
				setState(276);
				match(SEMICOLON);

				        SymbolInfo *symbol = symbol_table.lookup(current_func_id);
				        if(symbol != nullptr) {
				            string func_return_type = symbol->getFuncRetType();
				            if(func_return_type == "void") {
				                error_text_1 = "Cannot return value from function " + current_func_id + " with void return type \n";
				            }
				        }
				        ((StatementContext)_localctx).text =  "return " + ((StatementContext)_localctx).e.text + ";";
				        writeIntoParserLogFile("Line " + to_string(((StatementContext)_localctx).RETURN->getLine()) + ": statement : RETURN expression SEMICOLON\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expression_statementContext extends ParserRuleContext {
		public string text;
		public Token SEMICOLON;
		public ExpressionContext e;
		public TerminalNode SEMICOLON() { return getToken(C8086Parser.SEMICOLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression_statement; }
	}

	public final Expression_statementContext expression_statement() throws RecognitionException {
		Expression_statementContext _localctx = new Expression_statementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_expression_statement);
		try {
			setState(287);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(281);
				((Expression_statementContext)_localctx).SEMICOLON = match(SEMICOLON);

				        ((Expression_statementContext)_localctx).text =  ";";
				        writeIntoParserLogFile("Line " + to_string(((Expression_statementContext)_localctx).SEMICOLON->getLine()) + ": expression_statement : SEMICOLON\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case LPAREN:
			case ADDOP:
			case NOT:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(283);
				((Expression_statementContext)_localctx).e = expression();
				setState(284);
				((Expression_statementContext)_localctx).SEMICOLON = match(SEMICOLON);

				        ((Expression_statementContext)_localctx).text =  ((Expression_statementContext)_localctx).e.text + ";";
				        writeIntoParserLogFile("Line " + to_string(((Expression_statementContext)_localctx).SEMICOLON->getLine()) + ": expression_statement : expression SEMICOLON\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public string text;
		public Token ID;
		public ExpressionContext e;
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode LTHIRD() { return getToken(C8086Parser.LTHIRD, 0); }
		public TerminalNode RTHIRD() { return getToken(C8086Parser.RTHIRD, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_variable);
		try {
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(289);
				((VariableContext)_localctx).ID = match(ID);

						((VariableContext)_localctx).text =  ((VariableContext)_localctx).ID->getText();
						writeIntoParserLogFile("Line " + to_string(((VariableContext)_localctx).ID->getLine()) + ": variable : ID\n");
				        SymbolInfo *symbol = symbol_table.lookup(((VariableContext)_localctx).ID->getText());
				        if(symbol == nullptr) {
				            string error_text = "Error at line " + to_string(((VariableContext)_localctx).ID->getLine()) + ": Undeclared variable " + ((VariableContext)_localctx).ID->getText() + "\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        } else{
				            string var_type = symbol->getVarType();
				            if(var_type == "array") {
				                string error_text = "Error at line " + to_string(((VariableContext)_localctx).ID->getLine()) + ": Type mismatch, " + ((VariableContext)_localctx).ID->getText() + " is an array\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            }
				        }
						writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(291);
				((VariableContext)_localctx).ID = match(ID);
				setState(292);
				match(LTHIRD);
				setState(293);
				((VariableContext)_localctx).e = expression();
				setState(294);
				match(RTHIRD);
				 
						((VariableContext)_localctx).text =  ((VariableContext)_localctx).ID->getText() + "[" + ((VariableContext)_localctx).e.text + "]";

						writeIntoParserLogFile("Line " + to_string(((VariableContext)_localctx).ID->getLine()) + ": variable : ID LTHIRD expression RTHIRD\n");
				        if(!isInteger(((VariableContext)_localctx).e.text)) {
				            string error_text = "Error at line " + to_string((((VariableContext)_localctx).e!=null?(((VariableContext)_localctx).e.start):null)->getLine()) + ": Expression inside third brackets not an integer\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        }

				        // var may not be array
				        SymbolInfo *symbol = symbol_table.lookup(((VariableContext)_localctx).ID->getText());
				        if(symbol != nullptr) {
				            string var_type = symbol->getVarType();
				            if(var_type != "array") {
				                string error_text = "Error at line " + to_string(((VariableContext)_localctx).ID->getLine()) + ": " + ((VariableContext)_localctx).ID->getText() + " not an array\n";
				                writeIntoErrorFile(error_text);
				                writeIntoParserLogFile(error_text);
				                syntaxErrorCount++;
				            }
				        }
						writeIntoParserLogFile(_localctx.text + "\n");
					  
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public string text;
		public Logic_expressionContext le;
		public VariableContext var;
		public Token ASSIGNOP;
		public Logic_expressionContext logic_expression() {
			return getRuleContext(Logic_expressionContext.class,0);
		}
		public TerminalNode ASSIGNOP() { return getToken(C8086Parser.ASSIGNOP, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_expression);
		try {
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(299);
				((ExpressionContext)_localctx).le = logic_expression();

				        ((ExpressionContext)_localctx).text =  ((ExpressionContext)_localctx).le.text;
				        writeIntoParserLogFile("Line " + to_string((((ExpressionContext)_localctx).le!=null?(((ExpressionContext)_localctx).le.start):null)->getLine()) + ": expression : logic_expression\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				((ExpressionContext)_localctx).var = variable();
				setState(303);
				((ExpressionContext)_localctx).ASSIGNOP = match(ASSIGNOP);
				setState(304);
				((ExpressionContext)_localctx).le = logic_expression();

				        ((ExpressionContext)_localctx).text =  ((ExpressionContext)_localctx).var.text + ((ExpressionContext)_localctx).ASSIGNOP->getText() + ((ExpressionContext)_localctx).le.text;
				        writeIntoParserLogFile("Line " + to_string(((ExpressionContext)_localctx).ASSIGNOP->getLine()) + ": expression : variable ASSIGNOP logic_expression\n");
				        SymbolInfo *symbol = symbol_table.lookup(((ExpressionContext)_localctx).var.text);
				        if(symbol == nullptr) {
				            //todo: handle error
				        } else {
				            string var_type = symbol->getVarType();
				            if(var_type == "int") {
				                if(((ExpressionContext)_localctx).le.text.find(".") != string::npos && ((ExpressionContext)_localctx).le.text.find("%") == string::npos) {    
				                    string error_text = "Error at line " + to_string(((ExpressionContext)_localctx).ASSIGNOP->getLine()) + ": Type Mismatch\n";
				                    writeIntoErrorFile(error_text);
				                    writeIntoParserLogFile(error_text);
				                    syntaxErrorCount++;
				                }
				            }
				        }

				        // c[3] = 2.7 but c is an int array. capture this type mismatch.
				        string var_name = "";
				        string s1 = ((ExpressionContext)_localctx).var.text;
				        if(s1.find("[") != string::npos) {
				            // get the var name only.
				            size_t pos = s1.find("[");
				            string var_name = s1.substr(0, pos);

				            SymbolInfo *symbol = symbol_table.lookup(var_name);
				            if(symbol != nullptr) {
				                string var_type = symbol->getVarType();
				                if(var_type == "array") {
				                    // check if the expression is an integer.
				                    if(((ExpressionContext)_localctx).le.text.find(".") != string::npos && ((ExpressionContext)_localctx).le.text.find("%") == string::npos) {
				                        string error_text = "Error at line " + to_string(((ExpressionContext)_localctx).ASSIGNOP->getLine()) + ": Type Mismatch\n";
				                        writeIntoErrorFile(error_text);
				                        writeIntoParserLogFile(error_text);
				                        syntaxErrorCount++;
				                    }
				                }
				            }
				        }


				        // can't have void func assigned to a variable.
				        string func_name = getFuncNameFromExpression(((ExpressionContext)_localctx).le.text);
				        SymbolInfo *func_symbol = symbol_table.lookup(func_name);
				        if(func_symbol != nullptr && func_symbol->getFuncRetType() == "void") {
				            string error_text = "Error at line " + to_string(((ExpressionContext)_localctx).ASSIGNOP->getLine()) + ": Void function used in expression\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        }
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Logic_expressionContext extends ParserRuleContext {
		public string text;
		public Rel_expressionContext re;
		public Rel_expressionContext re1;
		public Token LOGICOP;
		public Rel_expressionContext re2;
		public List<Rel_expressionContext> rel_expression() {
			return getRuleContexts(Rel_expressionContext.class);
		}
		public Rel_expressionContext rel_expression(int i) {
			return getRuleContext(Rel_expressionContext.class,i);
		}
		public TerminalNode LOGICOP() { return getToken(C8086Parser.LOGICOP, 0); }
		public Logic_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic_expression; }
	}

	public final Logic_expressionContext logic_expression() throws RecognitionException {
		Logic_expressionContext _localctx = new Logic_expressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_logic_expression);
		try {
			setState(317);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(309);
				((Logic_expressionContext)_localctx).re = rel_expression();

				        ((Logic_expressionContext)_localctx).text =  ((Logic_expressionContext)_localctx).re.text;
				        writeIntoParserLogFile("Line " + to_string((((Logic_expressionContext)_localctx).re!=null?(((Logic_expressionContext)_localctx).re.start):null)->getLine()) + ": logic_expression : rel_expression\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(312);
				((Logic_expressionContext)_localctx).re1 = rel_expression();
				setState(313);
				((Logic_expressionContext)_localctx).LOGICOP = match(LOGICOP);
				setState(314);
				((Logic_expressionContext)_localctx).re2 = rel_expression();

				        ((Logic_expressionContext)_localctx).text =  ((Logic_expressionContext)_localctx).re1.text + ((Logic_expressionContext)_localctx).LOGICOP->getText() + ((Logic_expressionContext)_localctx).re2.text;
				        writeIntoParserLogFile("Line " + to_string(((Logic_expressionContext)_localctx).LOGICOP->getLine()) + ": logic_expression : rel_expression LOGICOP rel_expression\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Rel_expressionContext extends ParserRuleContext {
		public string text;
		public Simple_expressionContext se;
		public Simple_expressionContext se1;
		public Token RELOP;
		public Simple_expressionContext se2;
		public List<Simple_expressionContext> simple_expression() {
			return getRuleContexts(Simple_expressionContext.class);
		}
		public Simple_expressionContext simple_expression(int i) {
			return getRuleContext(Simple_expressionContext.class,i);
		}
		public TerminalNode RELOP() { return getToken(C8086Parser.RELOP, 0); }
		public Rel_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rel_expression; }
	}

	public final Rel_expressionContext rel_expression() throws RecognitionException {
		Rel_expressionContext _localctx = new Rel_expressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_rel_expression);
		try {
			setState(327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(319);
				((Rel_expressionContext)_localctx).se = simple_expression(0);

				        ((Rel_expressionContext)_localctx).text =  ((Rel_expressionContext)_localctx).se.text;
				        writeIntoParserLogFile("Line " + to_string((((Rel_expressionContext)_localctx).se!=null?(((Rel_expressionContext)_localctx).se.start):null)->getLine()) + ": rel_expression : simple_expression\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(322);
				((Rel_expressionContext)_localctx).se1 = simple_expression(0);
				setState(323);
				((Rel_expressionContext)_localctx).RELOP = match(RELOP);
				setState(324);
				((Rel_expressionContext)_localctx).se2 = simple_expression(0);

				        ((Rel_expressionContext)_localctx).text =  ((Rel_expressionContext)_localctx).se1.text + ((Rel_expressionContext)_localctx).RELOP->getText() + ((Rel_expressionContext)_localctx).se2.text;
				        writeIntoParserLogFile("Line " + to_string(((Rel_expressionContext)_localctx).RELOP->getLine()) + ": rel_expression : simple_expression RELOP simple_expression\n");
				        writeIntoParserLogFile(_localctx.text + "\n");
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_expressionContext extends ParserRuleContext {
		public string text;
		public Simple_expressionContext se;
		public TermContext term;
		public Simple_expression_errContext see;
		public Token ADDOP;
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public Simple_expression_errContext simple_expression_err() {
			return getRuleContext(Simple_expression_errContext.class,0);
		}
		public TerminalNode ADDOP() { return getToken(C8086Parser.ADDOP, 0); }
		public Simple_expressionContext simple_expression() {
			return getRuleContext(Simple_expressionContext.class,0);
		}
		public Simple_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_expression; }
	}

	public final Simple_expressionContext simple_expression() throws RecognitionException {
		return simple_expression(0);
	}

	private Simple_expressionContext simple_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Simple_expressionContext _localctx = new Simple_expressionContext(_ctx, _parentState);
		Simple_expressionContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_simple_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(330);
				((Simple_expressionContext)_localctx).term = term(0);

							((Simple_expressionContext)_localctx).text =  ((Simple_expressionContext)_localctx).term.text;
							writeIntoParserLogFile("Line " + to_string((((Simple_expressionContext)_localctx).term!=null?(((Simple_expressionContext)_localctx).term.start):null)->getLine()) + ": simple_expression : term\n");
							writeIntoParserLogFile(_localctx.text + "\n");
						  
				}
				break;
			case 2:
				{
				setState(333);
				((Simple_expressionContext)_localctx).see = simple_expression_err();

				            writeIntoParserLogFile(((Simple_expressionContext)_localctx).see.error);
				            writeIntoErrorFile(((Simple_expressionContext)_localctx).see.error);
				            syntaxErrorCount++;
				          
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(345);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Simple_expressionContext(_parentctx, _parentState);
					_localctx.se = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_simple_expression);
					setState(338);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(339);
					((Simple_expressionContext)_localctx).ADDOP = match(ADDOP);
					setState(340);
					((Simple_expressionContext)_localctx).term = term(0);

					          			((Simple_expressionContext)_localctx).text =  ((Simple_expressionContext)_localctx).se.text + ((Simple_expressionContext)_localctx).ADDOP->getText() + ((Simple_expressionContext)_localctx).term.text;
					          			writeIntoParserLogFile("Line " + to_string(((Simple_expressionContext)_localctx).ADDOP->getLine()) + ": simple_expression : simple_expression ADDOP term\n");
					          			writeIntoParserLogFile(_localctx.text + "\n");
					          		  
					}
					} 
				}
				setState(347);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_expression_errContext extends ParserRuleContext {
		public string error;
		public TermContext term;
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode ADDOP() { return getToken(C8086Parser.ADDOP, 0); }
		public TerminalNode ASSIGNOP() { return getToken(C8086Parser.ASSIGNOP, 0); }
		public Simple_expression_errContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_expression_err; }
	}

	public final Simple_expression_errContext simple_expression_err() throws RecognitionException {
		Simple_expression_errContext _localctx = new Simple_expression_errContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_simple_expression_err);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			((Simple_expression_errContext)_localctx).term = term(0);
			setState(349);
			match(ADDOP);
			setState(350);
			match(ASSIGNOP);

			        ((Simple_expression_errContext)_localctx).error =  "Error at line " + to_string((((Simple_expression_errContext)_localctx).term!=null?(((Simple_expression_errContext)_localctx).term.start):null)->getLine()) + ": syntax error, unexpected ASSIGNOP\n";
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public string text;
		public TermContext t;
		public Unary_expressionContext ue;
		public Token MULOP;
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public TerminalNode MULOP() { return getToken(C8086Parser.MULOP, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	}

	public final TermContext term() throws RecognitionException {
		return term(0);
	}

	private TermContext term(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TermContext _localctx = new TermContext(_ctx, _parentState);
		TermContext _prevctx = _localctx;
		int _startState = 42;
		enterRecursionRule(_localctx, 42, RULE_term, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(354);
			((TermContext)_localctx).ue = unary_expression();

					((TermContext)_localctx).text =  ((TermContext)_localctx).ue.text;
					writeIntoParserLogFile("Line " + to_string((((TermContext)_localctx).ue!=null?(((TermContext)_localctx).ue.start):null)->getLine()) + ": term : unary_expression\n");
					writeIntoParserLogFile(_localctx.text + "\n");
				
			}
			_ctx.stop = _input.LT(-1);
			setState(364);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TermContext(_parentctx, _parentState);
					_localctx.t = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_term);
					setState(357);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(358);
					((TermContext)_localctx).MULOP = match(MULOP);
					setState(359);
					((TermContext)_localctx).ue = unary_expression();

					          		((TermContext)_localctx).text =  ((TermContext)_localctx).t.text + ((TermContext)_localctx).MULOP->getText() + ((TermContext)_localctx).ue.text;
					          		writeIntoParserLogFile("Line " + to_string(((TermContext)_localctx).MULOP->getLine()) + ": term : term MULOP unary_expression\n");
					                  if(((TermContext)_localctx).MULOP->getText() == "%") {
					                      if(!isInteger(((TermContext)_localctx).ue.text)) {
					                          string error_text = "Error at line " + to_string(((TermContext)_localctx).MULOP->getLine()) + ": Non-Integer operand on modulus operator\n";
					                          writeIntoErrorFile(error_text);
					                          writeIntoParserLogFile(error_text);
					                          syntaxErrorCount++;
					                      } 
					                      if(((TermContext)_localctx).ue.text == "0") {
					                          string error_text = "Error at line " + to_string(((TermContext)_localctx).MULOP->getLine()) + ": Modulus by Zero\n";
					                          writeIntoErrorFile(error_text);
					                          writeIntoParserLogFile(error_text);
					                          syntaxErrorCount++;
					                      }
					                  }
					                  string func_name = getFuncNameFromExpression(((TermContext)_localctx).ue.text);
					                  SymbolInfo *func_symbol = symbol_table.lookup(func_name);
					                  if(func_symbol != nullptr && func_symbol->getFuncRetType() == "void") {
					                      string error_text = "Error at line " + to_string(((TermContext)_localctx).MULOP->getLine()) + ": Void function used in expression\n";
					                      writeIntoErrorFile(error_text);
					                      writeIntoParserLogFile(error_text);
					                      syntaxErrorCount++;
					                  }
					          		writeIntoParserLogFile(_localctx.text + "\n");
					          	 
					}
					} 
				}
				setState(366);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Unary_expressionContext extends ParserRuleContext {
		public string text;
		public Token ADDOP;
		public Unary_expressionContext ue;
		public Token NOT;
		public FactorContext factor;
		public TerminalNode ADDOP() { return getToken(C8086Parser.ADDOP, 0); }
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(C8086Parser.NOT, 0); }
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public Unary_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expression; }
	}

	public final Unary_expressionContext unary_expression() throws RecognitionException {
		Unary_expressionContext _localctx = new Unary_expressionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_unary_expression);
		try {
			setState(378);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ADDOP:
				enterOuterAlt(_localctx, 1);
				{
				setState(367);
				((Unary_expressionContext)_localctx).ADDOP = match(ADDOP);
				setState(368);
				((Unary_expressionContext)_localctx).ue = unary_expression();

				            ((Unary_expressionContext)_localctx).text =  ((Unary_expressionContext)_localctx).ADDOP->getText() + ((Unary_expressionContext)_localctx).ue.text;
				            writeIntoParserLogFile("Line " + to_string(((Unary_expressionContext)_localctx).ADDOP->getLine()) + ": unary_expression : ADDOP unary_expression\n");
				            writeIntoParserLogFile(_localctx.text + "\n");
						
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(371);
				((Unary_expressionContext)_localctx).NOT = match(NOT);
				setState(372);
				((Unary_expressionContext)_localctx).ue = unary_expression();

				            ((Unary_expressionContext)_localctx).text =  ((Unary_expressionContext)_localctx).NOT->getText() + ((Unary_expressionContext)_localctx).ue.text;
				            writeIntoParserLogFile("Line " + to_string(((Unary_expressionContext)_localctx).NOT->getLine()) + ": unary_expression : NOT unary_expression\n");
				            writeIntoParserLogFile(_localctx.text + "\n");
				         
				}
				break;
			case LPAREN:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 3);
				{
				setState(375);
				((Unary_expressionContext)_localctx).factor = factor();

							((Unary_expressionContext)_localctx).text =  ((Unary_expressionContext)_localctx).factor.text;
							writeIntoParserLogFile("Line " + to_string((((Unary_expressionContext)_localctx).factor!=null?(((Unary_expressionContext)_localctx).factor.start):null)->getLine()) + ": unary_expression : factor\n");
							writeIntoParserLogFile(_localctx.text + "\n");
							
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FactorContext extends ParserRuleContext {
		public string text;
		public VariableContext variable;
		public Token ID;
		public Argument_listContext al;
		public Token LPAREN;
		public ExpressionContext expression;
		public Token CONST_INT;
		public Token CONST_FLOAT;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ID() { return getToken(C8086Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(C8086Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(C8086Parser.RPAREN, 0); }
		public Argument_listContext argument_list() {
			return getRuleContext(Argument_listContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode CONST_INT() { return getToken(C8086Parser.CONST_INT, 0); }
		public TerminalNode CONST_FLOAT() { return getToken(C8086Parser.CONST_FLOAT, 0); }
		public TerminalNode INCOP() { return getToken(C8086Parser.INCOP, 0); }
		public TerminalNode DECOP() { return getToken(C8086Parser.DECOP, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_factor);
		try {
			setState(406);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				((FactorContext)_localctx).variable = variable();

						((FactorContext)_localctx).text =  ((FactorContext)_localctx).variable.text;
						writeIntoParserLogFile("Line " + to_string((((FactorContext)_localctx).variable!=null?(((FactorContext)_localctx).variable.start):null)->getLine()) + ": factor : variable\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(383);
				((FactorContext)_localctx).ID = match(ID);
				setState(384);
				match(LPAREN);
				setState(385);
				((FactorContext)_localctx).al = argument_list();
				setState(386);
				match(RPAREN);

						((FactorContext)_localctx).text =  ((FactorContext)_localctx).ID->getText() + "(" + ((FactorContext)_localctx).al.text + ")";
						writeIntoParserLogFile("Line " + to_string(((FactorContext)_localctx).ID->getLine()) + ": factor : ID LPAREN argument_list RPAREN\n");
				        SymbolInfo *symbol = symbol_table.lookup(((FactorContext)_localctx).ID->getText());
				        if(symbol != nullptr) {
				            string func_arg_list = symbol->getFuncArgs();
				            cout << "Function argument list: " << func_arg_list << endl;
				            vector<string> func_args = getFuncArgsFromList(func_arg_list);
				            vector<string> call_args = getFuncArgsFromCall(((FactorContext)_localctx).al.text);

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
				                string error_text = "Error at line " + to_string(((FactorContext)_localctx).ID->getLine()) + ": Total number of arguments mismatch with declaration in function " + ((FactorContext)_localctx).ID->getText() + "\n";
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
				                            string error_text = "Error at line " + to_string(((FactorContext)_localctx).ID->getLine()) + ": " + to_string(i + 1) + "th argument mismatch in function " + ((FactorContext)_localctx).ID->getText() + "\n";
				                            writeIntoErrorFile(error_text);
				                            writeIntoParserLogFile(error_text);
				                            syntaxErrorCount++;
				                            break; // no need to check further if one argument is already mismatched
				                        }
				                    }
				                }
				            }
				            
				        } else {
				            string error_text = "Error at line " + to_string(((FactorContext)_localctx).ID->getLine()) + ": Undefined function " + ((FactorContext)_localctx).ID->getText() + "\n";
				            writeIntoErrorFile(error_text);
				            writeIntoParserLogFile(error_text);
				            syntaxErrorCount++;
				        }
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(389);
				((FactorContext)_localctx).LPAREN = match(LPAREN);
				setState(390);
				((FactorContext)_localctx).expression = expression();
				setState(391);
				match(RPAREN);

						((FactorContext)_localctx).text =  "(" + ((FactorContext)_localctx).expression.text + ")";
						writeIntoParserLogFile("Line " + to_string(((FactorContext)_localctx).LPAREN->getLine()) + ": factor : LPAREN expression RPAREN\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(394);
				((FactorContext)_localctx).CONST_INT = match(CONST_INT);

						((FactorContext)_localctx).text =  ((FactorContext)_localctx).CONST_INT->getText();
						writeIntoParserLogFile("Line " + to_string(((FactorContext)_localctx).CONST_INT->getLine()) + ": factor : CONST_INT\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(396);
				((FactorContext)_localctx).CONST_FLOAT = match(CONST_FLOAT);

						((FactorContext)_localctx).text =  ((FactorContext)_localctx).CONST_FLOAT->getText();
						writeIntoParserLogFile("Line " + to_string(((FactorContext)_localctx).CONST_FLOAT->getLine()) + ": factor : CONST_FLOAT\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(398);
				((FactorContext)_localctx).variable = variable();
				setState(399);
				match(INCOP);

						((FactorContext)_localctx).text =  ((FactorContext)_localctx).variable.text + "++";
						writeIntoParserLogFile("Line " + to_string((((FactorContext)_localctx).variable!=null?(((FactorContext)_localctx).variable.start):null)->getLine()) + ": factor : variable INCOP\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(402);
				((FactorContext)_localctx).variable = variable();
				setState(403);
				match(DECOP);
				 
						((FactorContext)_localctx).text =  ((FactorContext)_localctx).variable.text + "--";
						writeIntoParserLogFile("Line " + to_string((((FactorContext)_localctx).variable!=null?(((FactorContext)_localctx).variable.start):null)->getLine()) + ": factor : variable DECOP\n");
						writeIntoParserLogFile(_localctx.text + "\n");
					
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Argument_listContext extends ParserRuleContext {
		public string text;
		public ArgumentsContext a;
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public Argument_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument_list; }
	}

	public final Argument_listContext argument_list() throws RecognitionException {
		Argument_listContext _localctx = new Argument_listContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_argument_list);
		try {
			setState(412);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
			case ADDOP:
			case NOT:
			case ID:
			case CONST_INT:
			case CONST_FLOAT:
				enterOuterAlt(_localctx, 1);
				{
				setState(408);
				((Argument_listContext)_localctx).a = arguments(0);

				            ((Argument_listContext)_localctx).text =  ((Argument_listContext)_localctx).a.text;
				            writeIntoParserLogFile("Line " + to_string((((Argument_listContext)_localctx).a!=null?(((Argument_listContext)_localctx).a.stop):null)->getLine()) + ": argument_list : arguments\n");
				            writeIntoParserLogFile(_localctx.text + "\n");
				        
				}
				break;
			case RPAREN:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public string text;
		public ArgumentsContext a;
		public Logic_expressionContext le;
		public Logic_expressionContext logic_expression() {
			return getRuleContext(Logic_expressionContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(C8086Parser.COMMA, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		return arguments(0);
	}

	private ArgumentsContext arguments(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, _parentState);
		ArgumentsContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_arguments, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(415);
			((ArgumentsContext)_localctx).le = logic_expression();

			            ((ArgumentsContext)_localctx).text =  ((ArgumentsContext)_localctx).le.text;
			            writeIntoParserLogFile("Line " + to_string((((ArgumentsContext)_localctx).le!=null?(((ArgumentsContext)_localctx).le.start):null)->getLine()) + ": arguments : logic_expression\n");
			            writeIntoParserLogFile(_localctx.text + "\n");
			        
			}
			_ctx.stop = _input.LT(-1);
			setState(425);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArgumentsContext(_parentctx, _parentState);
					_localctx.a = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_arguments);
					setState(418);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(419);
					match(COMMA);
					setState(420);
					((ArgumentsContext)_localctx).le = logic_expression();

					                      ((ArgumentsContext)_localctx).text =  ((ArgumentsContext)_localctx).a.text + "," + ((ArgumentsContext)_localctx).le.text;
					                      writeIntoParserLogFile("Line " + to_string((((ArgumentsContext)_localctx).a!=null?(((ArgumentsContext)_localctx).a.stop):null)->getLine()) + ": arguments : arguments COMMA logic_expression\n");
					                      writeIntoParserLogFile(_localctx.text + "\n");
					                  
					}
					} 
				}
				setState(427);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return program_sempred((ProgramContext)_localctx, predIndex);
		case 5:
			return parameter_list_sempred((Parameter_listContext)_localctx, predIndex);
		case 11:
			return declaration_list_sempred((Declaration_listContext)_localctx, predIndex);
		case 12:
			return statements_sempred((StatementsContext)_localctx, predIndex);
		case 19:
			return simple_expression_sempred((Simple_expressionContext)_localctx, predIndex);
		case 21:
			return term_sempred((TermContext)_localctx, predIndex);
		case 25:
			return arguments_sempred((ArgumentsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean program_sempred(ProgramContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean parameter_list_sempred(Parameter_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 5);
		case 2:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean declaration_list_sempred(Declaration_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean statements_sempred(StatementsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean simple_expression_sempred(Simple_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean term_sempred(TermContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean arguments_sempred(ArgumentsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\"\u01ad\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001@\b\u0001\n\u0001\f\u0001C\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002N\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003a\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004"+
		"u\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005\u0082\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"\u008d\b\u0005\n\u0005\f\u0005\u0090\t\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u009f"+
		"\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u00ae\b\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0003\n\u00bb\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00c5\b\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u00d2"+
		"\b\u000b\n\u000b\f\u000b\u00d5\t\u000b\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00df\b\f\n\f\f\f\u00e2\t\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0118\b\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003"+
		"\u000e\u0120\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u012a\b\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0003\u0010\u0134\b\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003"+
		"\u0011\u013e\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0148\b\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u0151\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0005\u0013\u0158\b\u0013\n\u0013\f\u0013\u015b\t\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0005\u0015\u016b\b\u0015\n\u0015\f\u0015\u016e"+
		"\t\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u017b\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0197\b\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u019d\b\u0018\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u01a8\b\u0019\n\u0019\f\u0019"+
		"\u01ab\t\u0019\u0001\u0019\u0000\u0007\u0002\n\u0016\u0018&*2\u001a\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02\u0000\u0000\u01be\u00004\u0001\u0000\u0000\u0000\u0002"+
		"7\u0001\u0000\u0000\u0000\u0004M\u0001\u0000\u0000\u0000\u0006`\u0001"+
		"\u0000\u0000\u0000\bt\u0001\u0000\u0000\u0000\n\u0081\u0001\u0000\u0000"+
		"\u0000\f\u0091\u0001\u0000\u0000\u0000\u000e\u009e\u0001\u0000\u0000\u0000"+
		"\u0010\u00ad\u0001\u0000\u0000\u0000\u0012\u00af\u0001\u0000\u0000\u0000"+
		"\u0014\u00ba\u0001\u0000\u0000\u0000\u0016\u00c4\u0001\u0000\u0000\u0000"+
		"\u0018\u00d6\u0001\u0000\u0000\u0000\u001a\u0117\u0001\u0000\u0000\u0000"+
		"\u001c\u011f\u0001\u0000\u0000\u0000\u001e\u0129\u0001\u0000\u0000\u0000"+
		" \u0133\u0001\u0000\u0000\u0000\"\u013d\u0001\u0000\u0000\u0000$\u0147"+
		"\u0001\u0000\u0000\u0000&\u0150\u0001\u0000\u0000\u0000(\u015c\u0001\u0000"+
		"\u0000\u0000*\u0161\u0001\u0000\u0000\u0000,\u017a\u0001\u0000\u0000\u0000"+
		".\u0196\u0001\u0000\u0000\u00000\u019c\u0001\u0000\u0000\u00002\u019e"+
		"\u0001\u0000\u0000\u000045\u0003\u0002\u0001\u000056\u0006\u0000\uffff"+
		"\uffff\u00006\u0001\u0001\u0000\u0000\u000078\u0006\u0001\uffff\uffff"+
		"\u000089\u0003\u0004\u0002\u00009:\u0006\u0001\uffff\uffff\u0000:A\u0001"+
		"\u0000\u0000\u0000;<\n\u0002\u0000\u0000<=\u0003\u0004\u0002\u0000=>\u0006"+
		"\u0001\uffff\uffff\u0000>@\u0001\u0000\u0000\u0000?;\u0001\u0000\u0000"+
		"\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000AB\u0001\u0000"+
		"\u0000\u0000B\u0003\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000"+
		"DE\u0003\u0010\b\u0000EF\u0006\u0002\uffff\uffff\u0000FN\u0001\u0000\u0000"+
		"\u0000GH\u0003\u0006\u0003\u0000HI\u0006\u0002\uffff\uffff\u0000IN\u0001"+
		"\u0000\u0000\u0000JK\u0003\b\u0004\u0000KL\u0006\u0002\uffff\uffff\u0000"+
		"LN\u0001\u0000\u0000\u0000MD\u0001\u0000\u0000\u0000MG\u0001\u0000\u0000"+
		"\u0000MJ\u0001\u0000\u0000\u0000N\u0005\u0001\u0000\u0000\u0000OP\u0003"+
		"\u0014\n\u0000PQ\u0005\u001f\u0000\u0000QR\u0005\u000e\u0000\u0000RS\u0006"+
		"\u0003\uffff\uffff\u0000ST\u0003\n\u0005\u0000TU\u0005\u000f\u0000\u0000"+
		"UV\u0005\u0014\u0000\u0000VW\u0006\u0003\uffff\uffff\u0000Wa\u0001\u0000"+
		"\u0000\u0000XY\u0003\u0014\n\u0000YZ\u0005\u001f\u0000\u0000Z[\u0005\u000e"+
		"\u0000\u0000[\\\u0006\u0003\uffff\uffff\u0000\\]\u0005\u000f\u0000\u0000"+
		"]^\u0005\u0014\u0000\u0000^_\u0006\u0003\uffff\uffff\u0000_a\u0001\u0000"+
		"\u0000\u0000`O\u0001\u0000\u0000\u0000`X\u0001\u0000\u0000\u0000a\u0007"+
		"\u0001\u0000\u0000\u0000bc\u0003\u0014\n\u0000cd\u0005\u001f\u0000\u0000"+
		"de\u0005\u000e\u0000\u0000ef\u0006\u0004\uffff\uffff\u0000fg\u0003\n\u0005"+
		"\u0000gh\u0006\u0004\uffff\uffff\u0000hi\u0005\u000f\u0000\u0000ij\u0003"+
		"\u000e\u0007\u0000jk\u0006\u0004\uffff\uffff\u0000ku\u0001\u0000\u0000"+
		"\u0000lm\u0003\u0014\n\u0000mn\u0005\u001f\u0000\u0000no\u0005\u000e\u0000"+
		"\u0000op\u0006\u0004\uffff\uffff\u0000pq\u0005\u000f\u0000\u0000qr\u0003"+
		"\u000e\u0007\u0000rs\u0006\u0004\uffff\uffff\u0000su\u0001\u0000\u0000"+
		"\u0000tb\u0001\u0000\u0000\u0000tl\u0001\u0000\u0000\u0000u\t\u0001\u0000"+
		"\u0000\u0000vw\u0006\u0005\uffff\uffff\u0000wx\u0003\u0014\n\u0000xy\u0005"+
		"\u001f\u0000\u0000yz\u0006\u0005\uffff\uffff\u0000z\u0082\u0001\u0000"+
		"\u0000\u0000{|\u0003\u0014\n\u0000|}\u0006\u0005\uffff\uffff\u0000}\u0082"+
		"\u0001\u0000\u0000\u0000~\u007f\u0003\f\u0006\u0000\u007f\u0080\u0006"+
		"\u0005\uffff\uffff\u0000\u0080\u0082\u0001\u0000\u0000\u0000\u0081v\u0001"+
		"\u0000\u0000\u0000\u0081{\u0001\u0000\u0000\u0000\u0081~\u0001\u0000\u0000"+
		"\u0000\u0082\u008e\u0001\u0000\u0000\u0000\u0083\u0084\n\u0005\u0000\u0000"+
		"\u0084\u0085\u0005\u0015\u0000\u0000\u0085\u0086\u0003\u0014\n\u0000\u0086"+
		"\u0087\u0005\u001f\u0000\u0000\u0087\u0088\u0006\u0005\uffff\uffff\u0000"+
		"\u0088\u008d\u0001\u0000\u0000\u0000\u0089\u008a\n\u0004\u0000\u0000\u008a"+
		"\u008b\u0005\u0015\u0000\u0000\u008b\u008d\u0006\u0005\uffff\uffff\u0000"+
		"\u008c\u0083\u0001\u0000\u0000\u0000\u008c\u0089\u0001\u0000\u0000\u0000"+
		"\u008d\u0090\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000"+
		"\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u000b\u0001\u0000\u0000\u0000"+
		"\u0090\u008e\u0001\u0000\u0000\u0000\u0091\u0092\u0003\u0014\n\u0000\u0092"+
		"\u0093\u0005\u0016\u0000\u0000\u0093\u0094\u0006\u0006\uffff\uffff\u0000"+
		"\u0094\r\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u0010\u0000\u0000\u0096"+
		"\u0097\u0006\u0007\uffff\uffff\u0000\u0097\u0098\u0003\u0018\f\u0000\u0098"+
		"\u0099\u0005\u0011\u0000\u0000\u0099\u009a\u0006\u0007\uffff\uffff\u0000"+
		"\u009a\u009f\u0001\u0000\u0000\u0000\u009b\u009c\u0005\u0010\u0000\u0000"+
		"\u009c\u009d\u0005\u0011\u0000\u0000\u009d\u009f\u0006\u0007\uffff\uffff"+
		"\u0000\u009e\u0095\u0001\u0000\u0000\u0000\u009e\u009b\u0001\u0000\u0000"+
		"\u0000\u009f\u000f\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003\u0014\n\u0000"+
		"\u00a1\u00a2\u0003\u0016\u000b\u0000\u00a2\u00a3\u0005\u0014\u0000\u0000"+
		"\u00a3\u00a4\u0006\b\uffff\uffff\u0000\u00a4\u00ae\u0001\u0000\u0000\u0000"+
		"\u00a5\u00a6\u0003\u0014\n\u0000\u00a6\u00a7\u0003\u0012\t\u0000\u00a7"+
		"\u00a8\u0005\u0014\u0000\u0000\u00a8\u00a9\u0006\b\uffff\uffff\u0000\u00a9"+
		"\u00ae\u0001\u0000\u0000\u0000\u00aa\u00ab\u0003\u0012\t\u0000\u00ab\u00ac"+
		"\u0006\b\uffff\uffff\u0000\u00ac\u00ae\u0001\u0000\u0000\u0000\u00ad\u00a0"+
		"\u0001\u0000\u0000\u0000\u00ad\u00a5\u0001\u0000\u0000\u0000\u00ad\u00aa"+
		"\u0001\u0000\u0000\u0000\u00ae\u0011\u0001\u0000\u0000\u0000\u00af\u00b0"+
		"\u0003\u0014\n\u0000\u00b0\u00b1\u0003\u001e\u000f\u0000\u00b1\u00b2\u0005"+
		"\u0016\u0000\u0000\u00b2\u00b3\u0006\t\uffff\uffff\u0000\u00b3\u0013\u0001"+
		"\u0000\u0000\u0000\u00b4\u00b5\u0005\u000b\u0000\u0000\u00b5\u00bb\u0006"+
		"\n\uffff\uffff\u0000\u00b6\u00b7\u0005\f\u0000\u0000\u00b7\u00bb\u0006"+
		"\n\uffff\uffff\u0000\u00b8\u00b9\u0005\r\u0000\u0000\u00b9\u00bb\u0006"+
		"\n\uffff\uffff\u0000\u00ba\u00b4\u0001\u0000\u0000\u0000\u00ba\u00b6\u0001"+
		"\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u0015\u0001"+
		"\u0000\u0000\u0000\u00bc\u00bd\u0006\u000b\uffff\uffff\u0000\u00bd\u00be"+
		"\u0005\u001f\u0000\u0000\u00be\u00c5\u0006\u000b\uffff\uffff\u0000\u00bf"+
		"\u00c0\u0005\u001f\u0000\u0000\u00c0\u00c1\u0005\u0012\u0000\u0000\u00c1"+
		"\u00c2\u0005 \u0000\u0000\u00c2\u00c3\u0005\u0013\u0000\u0000\u00c3\u00c5"+
		"\u0006\u000b\uffff\uffff\u0000\u00c4\u00bc\u0001\u0000\u0000\u0000\u00c4"+
		"\u00bf\u0001\u0000\u0000\u0000\u00c5\u00d3\u0001\u0000\u0000\u0000\u00c6"+
		"\u00c7\n\u0004\u0000\u0000\u00c7\u00c8\u0005\u0015\u0000\u0000\u00c8\u00c9"+
		"\u0005\u001f\u0000\u0000\u00c9\u00d2\u0006\u000b\uffff\uffff\u0000\u00ca"+
		"\u00cb\n\u0003\u0000\u0000\u00cb\u00cc\u0005\u0015\u0000\u0000\u00cc\u00cd"+
		"\u0005\u001f\u0000\u0000\u00cd\u00ce\u0005\u0012\u0000\u0000\u00ce\u00cf"+
		"\u0005 \u0000\u0000\u00cf\u00d0\u0005\u0013\u0000\u0000\u00d0\u00d2\u0006"+
		"\u000b\uffff\uffff\u0000\u00d1\u00c6\u0001\u0000\u0000\u0000\u00d1\u00ca"+
		"\u0001\u0000\u0000\u0000\u00d2\u00d5\u0001\u0000\u0000\u0000\u00d3\u00d1"+
		"\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4\u0017"+
		"\u0001\u0000\u0000\u0000\u00d5\u00d3\u0001\u0000\u0000\u0000\u00d6\u00d7"+
		"\u0006\f\uffff\uffff\u0000\u00d7\u00d8\u0003\u001a\r\u0000\u00d8\u00d9"+
		"\u0006\f\uffff\uffff\u0000\u00d9\u00e0\u0001\u0000\u0000\u0000\u00da\u00db"+
		"\n\u0001\u0000\u0000\u00db\u00dc\u0003\u001a\r\u0000\u00dc\u00dd\u0006"+
		"\f\uffff\uffff\u0000\u00dd\u00df\u0001\u0000\u0000\u0000\u00de\u00da\u0001"+
		"\u0000\u0000\u0000\u00df\u00e2\u0001\u0000\u0000\u0000\u00e0\u00de\u0001"+
		"\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u0019\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e4\u0003"+
		"\u0010\b\u0000\u00e4\u00e5\u0006\r\uffff\uffff\u0000\u00e5\u0118\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e7\u0003\u001c\u000e\u0000\u00e7\u00e8\u0006"+
		"\r\uffff\uffff\u0000\u00e8\u0118\u0001\u0000\u0000\u0000\u00e9\u00ea\u0003"+
		"\u000e\u0007\u0000\u00ea\u00eb\u0006\r\uffff\uffff\u0000\u00eb\u0118\u0001"+
		"\u0000\u0000\u0000\u00ec\u00ed\u0005\u0007\u0000\u0000\u00ed\u00ee\u0005"+
		"\u000e\u0000\u0000\u00ee\u00ef\u0003\u001c\u000e\u0000\u00ef\u00f0\u0003"+
		"\u001c\u000e\u0000\u00f0\u00f1\u0003 \u0010\u0000\u00f1\u00f2\u0005\u000f"+
		"\u0000\u0000\u00f2\u00f3\u0003\u001a\r\u0000\u00f3\u00f4\u0006\r\uffff"+
		"\uffff\u0000\u00f4\u0118\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\u0005"+
		"\u0000\u0000\u00f6\u00f7\u0005\u000e\u0000\u0000\u00f7\u00f8\u0003 \u0010"+
		"\u0000\u00f8\u00f9\u0005\u000f\u0000\u0000\u00f9\u00fa\u0003\u001a\r\u0000"+
		"\u00fa\u00fb\u0006\r\uffff\uffff\u0000\u00fb\u0118\u0001\u0000\u0000\u0000"+
		"\u00fc\u00fd\u0005\u0005\u0000\u0000\u00fd\u00fe\u0005\u000e\u0000\u0000"+
		"\u00fe\u00ff\u0003 \u0010\u0000\u00ff\u0100\u0005\u000f\u0000\u0000\u0100"+
		"\u0101\u0003\u001a\r\u0000\u0101\u0102\u0005\u0006\u0000\u0000\u0102\u0103"+
		"\u0003\u001a\r\u0000\u0103\u0104\u0006\r\uffff\uffff\u0000\u0104\u0118"+
		"\u0001\u0000\u0000\u0000\u0105\u0106\u0005\b\u0000\u0000\u0106\u0107\u0005"+
		"\u000e\u0000\u0000\u0107\u0108\u0003 \u0010\u0000\u0108\u0109\u0005\u000f"+
		"\u0000\u0000\u0109\u010a\u0003\u001a\r\u0000\u010a\u010b\u0006\r\uffff"+
		"\uffff\u0000\u010b\u0118\u0001\u0000\u0000\u0000\u010c\u010d\u0005\t\u0000"+
		"\u0000\u010d\u010e\u0005\u000e\u0000\u0000\u010e\u010f\u0005\u001f\u0000"+
		"\u0000\u010f\u0110\u0005\u000f\u0000\u0000\u0110\u0111\u0005\u0014\u0000"+
		"\u0000\u0111\u0118\u0006\r\uffff\uffff\u0000\u0112\u0113\u0005\n\u0000"+
		"\u0000\u0113\u0114\u0003 \u0010\u0000\u0114\u0115\u0005\u0014\u0000\u0000"+
		"\u0115\u0116\u0006\r\uffff\uffff\u0000\u0116\u0118\u0001\u0000\u0000\u0000"+
		"\u0117\u00e3\u0001\u0000\u0000\u0000\u0117\u00e6\u0001\u0000\u0000\u0000"+
		"\u0117\u00e9\u0001\u0000\u0000\u0000\u0117\u00ec\u0001\u0000\u0000\u0000"+
		"\u0117\u00f5\u0001\u0000\u0000\u0000\u0117\u00fc\u0001\u0000\u0000\u0000"+
		"\u0117\u0105\u0001\u0000\u0000\u0000\u0117\u010c\u0001\u0000\u0000\u0000"+
		"\u0117\u0112\u0001\u0000\u0000\u0000\u0118\u001b\u0001\u0000\u0000\u0000"+
		"\u0119\u011a\u0005\u0014\u0000\u0000\u011a\u0120\u0006\u000e\uffff\uffff"+
		"\u0000\u011b\u011c\u0003 \u0010\u0000\u011c\u011d\u0005\u0014\u0000\u0000"+
		"\u011d\u011e\u0006\u000e\uffff\uffff\u0000\u011e\u0120\u0001\u0000\u0000"+
		"\u0000\u011f\u0119\u0001\u0000\u0000\u0000\u011f\u011b\u0001\u0000\u0000"+
		"\u0000\u0120\u001d\u0001\u0000\u0000\u0000\u0121\u0122\u0005\u001f\u0000"+
		"\u0000\u0122\u012a\u0006\u000f\uffff\uffff\u0000\u0123\u0124\u0005\u001f"+
		"\u0000\u0000\u0124\u0125\u0005\u0012\u0000\u0000\u0125\u0126\u0003 \u0010"+
		"\u0000\u0126\u0127\u0005\u0013\u0000\u0000\u0127\u0128\u0006\u000f\uffff"+
		"\uffff\u0000\u0128\u012a\u0001\u0000\u0000\u0000\u0129\u0121\u0001\u0000"+
		"\u0000\u0000\u0129\u0123\u0001\u0000\u0000\u0000\u012a\u001f\u0001\u0000"+
		"\u0000\u0000\u012b\u012c\u0003\"\u0011\u0000\u012c\u012d\u0006\u0010\uffff"+
		"\uffff\u0000\u012d\u0134\u0001\u0000\u0000\u0000\u012e\u012f\u0003\u001e"+
		"\u000f\u0000\u012f\u0130\u0005\u001e\u0000\u0000\u0130\u0131\u0003\"\u0011"+
		"\u0000\u0131\u0132\u0006\u0010\uffff\uffff\u0000\u0132\u0134\u0001\u0000"+
		"\u0000\u0000\u0133\u012b\u0001\u0000\u0000\u0000\u0133\u012e\u0001\u0000"+
		"\u0000\u0000\u0134!\u0001\u0000\u0000\u0000\u0135\u0136\u0003$\u0012\u0000"+
		"\u0136\u0137\u0006\u0011\uffff\uffff\u0000\u0137\u013e\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0003$\u0012\u0000\u0139\u013a\u0005\u001d\u0000\u0000"+
		"\u013a\u013b\u0003$\u0012\u0000\u013b\u013c\u0006\u0011\uffff\uffff\u0000"+
		"\u013c\u013e\u0001\u0000\u0000\u0000\u013d\u0135\u0001\u0000\u0000\u0000"+
		"\u013d\u0138\u0001\u0000\u0000\u0000\u013e#\u0001\u0000\u0000\u0000\u013f"+
		"\u0140\u0003&\u0013\u0000\u0140\u0141\u0006\u0012\uffff\uffff\u0000\u0141"+
		"\u0148\u0001\u0000\u0000\u0000\u0142\u0143\u0003&\u0013\u0000\u0143\u0144"+
		"\u0005\u001c\u0000\u0000\u0144\u0145\u0003&\u0013\u0000\u0145\u0146\u0006"+
		"\u0012\uffff\uffff\u0000\u0146\u0148\u0001\u0000\u0000\u0000\u0147\u013f"+
		"\u0001\u0000\u0000\u0000\u0147\u0142\u0001\u0000\u0000\u0000\u0148%\u0001"+
		"\u0000\u0000\u0000\u0149\u014a\u0006\u0013\uffff\uffff\u0000\u014a\u014b"+
		"\u0003*\u0015\u0000\u014b\u014c\u0006\u0013\uffff\uffff\u0000\u014c\u0151"+
		"\u0001\u0000\u0000\u0000\u014d\u014e\u0003(\u0014\u0000\u014e\u014f\u0006"+
		"\u0013\uffff\uffff\u0000\u014f\u0151\u0001\u0000\u0000\u0000\u0150\u0149"+
		"\u0001\u0000\u0000\u0000\u0150\u014d\u0001\u0000\u0000\u0000\u0151\u0159"+
		"\u0001\u0000\u0000\u0000\u0152\u0153\n\u0002\u0000\u0000\u0153\u0154\u0005"+
		"\u0016\u0000\u0000\u0154\u0155\u0003*\u0015\u0000\u0155\u0156\u0006\u0013"+
		"\uffff\uffff\u0000\u0156\u0158\u0001\u0000\u0000\u0000\u0157\u0152\u0001"+
		"\u0000\u0000\u0000\u0158\u015b\u0001\u0000\u0000\u0000\u0159\u0157\u0001"+
		"\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\'\u0001\u0000"+
		"\u0000\u0000\u015b\u0159\u0001\u0000\u0000\u0000\u015c\u015d\u0003*\u0015"+
		"\u0000\u015d\u015e\u0005\u0016\u0000\u0000\u015e\u015f\u0005\u001e\u0000"+
		"\u0000\u015f\u0160\u0006\u0014\uffff\uffff\u0000\u0160)\u0001\u0000\u0000"+
		"\u0000\u0161\u0162\u0006\u0015\uffff\uffff\u0000\u0162\u0163\u0003,\u0016"+
		"\u0000\u0163\u0164\u0006\u0015\uffff\uffff\u0000\u0164\u016c\u0001\u0000"+
		"\u0000\u0000\u0165\u0166\n\u0001\u0000\u0000\u0166\u0167\u0005\u0018\u0000"+
		"\u0000\u0167\u0168\u0003,\u0016\u0000\u0168\u0169\u0006\u0015\uffff\uffff"+
		"\u0000\u0169\u016b\u0001\u0000\u0000\u0000\u016a\u0165\u0001\u0000\u0000"+
		"\u0000\u016b\u016e\u0001\u0000\u0000\u0000\u016c\u016a\u0001\u0000\u0000"+
		"\u0000\u016c\u016d\u0001\u0000\u0000\u0000\u016d+\u0001\u0000\u0000\u0000"+
		"\u016e\u016c\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u0016\u0000\u0000"+
		"\u0170\u0171\u0003,\u0016\u0000\u0171\u0172\u0006\u0016\uffff\uffff\u0000"+
		"\u0172\u017b\u0001\u0000\u0000\u0000\u0173\u0174\u0005\u001b\u0000\u0000"+
		"\u0174\u0175\u0003,\u0016\u0000\u0175\u0176\u0006\u0016\uffff\uffff\u0000"+
		"\u0176\u017b\u0001\u0000\u0000\u0000\u0177\u0178\u0003.\u0017\u0000\u0178"+
		"\u0179\u0006\u0016\uffff\uffff\u0000\u0179\u017b\u0001\u0000\u0000\u0000"+
		"\u017a\u016f\u0001\u0000\u0000\u0000\u017a\u0173\u0001\u0000\u0000\u0000"+
		"\u017a\u0177\u0001\u0000\u0000\u0000\u017b-\u0001\u0000\u0000\u0000\u017c"+
		"\u017d\u0003\u001e\u000f\u0000\u017d\u017e\u0006\u0017\uffff\uffff\u0000"+
		"\u017e\u0197\u0001\u0000\u0000\u0000\u017f\u0180\u0005\u001f\u0000\u0000"+
		"\u0180\u0181\u0005\u000e\u0000\u0000\u0181\u0182\u00030\u0018\u0000\u0182"+
		"\u0183\u0005\u000f\u0000\u0000\u0183\u0184\u0006\u0017\uffff\uffff\u0000"+
		"\u0184\u0197\u0001\u0000\u0000\u0000\u0185\u0186\u0005\u000e\u0000\u0000"+
		"\u0186\u0187\u0003 \u0010\u0000\u0187\u0188\u0005\u000f\u0000\u0000\u0188"+
		"\u0189\u0006\u0017\uffff\uffff\u0000\u0189\u0197\u0001\u0000\u0000\u0000"+
		"\u018a\u018b\u0005 \u0000\u0000\u018b\u0197\u0006\u0017\uffff\uffff\u0000"+
		"\u018c\u018d\u0005!\u0000\u0000\u018d\u0197\u0006\u0017\uffff\uffff\u0000"+
		"\u018e\u018f\u0003\u001e\u000f\u0000\u018f\u0190\u0005\u0019\u0000\u0000"+
		"\u0190\u0191\u0006\u0017\uffff\uffff\u0000\u0191\u0197\u0001\u0000\u0000"+
		"\u0000\u0192\u0193\u0003\u001e\u000f\u0000\u0193\u0194\u0005\u001a\u0000"+
		"\u0000\u0194\u0195\u0006\u0017\uffff\uffff\u0000\u0195\u0197\u0001\u0000"+
		"\u0000\u0000\u0196\u017c\u0001\u0000\u0000\u0000\u0196\u017f\u0001\u0000"+
		"\u0000\u0000\u0196\u0185\u0001\u0000\u0000\u0000\u0196\u018a\u0001\u0000"+
		"\u0000\u0000\u0196\u018c\u0001\u0000\u0000\u0000\u0196\u018e\u0001\u0000"+
		"\u0000\u0000\u0196\u0192\u0001\u0000\u0000\u0000\u0197/\u0001\u0000\u0000"+
		"\u0000\u0198\u0199\u00032\u0019\u0000\u0199\u019a\u0006\u0018\uffff\uffff"+
		"\u0000\u019a\u019d\u0001\u0000\u0000\u0000\u019b\u019d\u0001\u0000\u0000"+
		"\u0000\u019c\u0198\u0001\u0000\u0000\u0000\u019c\u019b\u0001\u0000\u0000"+
		"\u0000\u019d1\u0001\u0000\u0000\u0000\u019e\u019f\u0006\u0019\uffff\uffff"+
		"\u0000\u019f\u01a0\u0003\"\u0011\u0000\u01a0\u01a1\u0006\u0019\uffff\uffff"+
		"\u0000\u01a1\u01a9\u0001\u0000\u0000\u0000\u01a2\u01a3\n\u0002\u0000\u0000"+
		"\u01a3\u01a4\u0005\u0015\u0000\u0000\u01a4\u01a5\u0003\"\u0011\u0000\u01a5"+
		"\u01a6\u0006\u0019\uffff\uffff\u0000\u01a6\u01a8\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a2\u0001\u0000\u0000\u0000\u01a8\u01ab\u0001\u0000\u0000\u0000"+
		"\u01a9\u01a7\u0001\u0000\u0000\u0000\u01a9\u01aa\u0001\u0000\u0000\u0000"+
		"\u01aa3\u0001\u0000\u0000\u0000\u01ab\u01a9\u0001\u0000\u0000\u0000\u001b"+
		"AM`t\u0081\u008c\u008e\u009e\u00ad\u00ba\u00c4\u00d1\u00d3\u00e0\u0117"+
		"\u011f\u0129\u0133\u013d\u0147\u0150\u0159\u016c\u017a\u0196\u019c\u01a9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}