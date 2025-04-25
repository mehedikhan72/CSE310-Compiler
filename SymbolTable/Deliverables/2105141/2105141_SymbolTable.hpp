#ifndef SYMBOlTABLE_HEADER
#define SYMBOlTABLE_HEADER

#include "fstream"
#include "2105141_ScopeTable.hpp"

using namespace std;

class SymbolTable {
private:
    ScopeTable *current_scope;
    ofstream* os;
    fstream* rs; // report stream

public:
    SymbolTable(string hash_function_name, int num_buckets, ofstream& output_stream, fstream& report_stream) {
        this->os = &output_stream;
        this->rs = &report_stream;
        current_scope = new ScopeTable(hash_function_name, num_buckets, nullptr, output_stream);
    }

    ~SymbolTable() {
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }   
    }

    void setOutputStream(ofstream& output_stream) {
        this->os = &output_stream;
        if (current_scope != nullptr) {
            current_scope->setOutputStream(output_stream);
        }
    }

    void enterScope(string hash_function_name, int num_buckets) {
        current_scope = new ScopeTable(hash_function_name, num_buckets, current_scope, *os);
    }

    void exitScope() {
        if (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        } else {
            *os << "\tCannot exit from the global ScopeTable\n";
        }
    }

    bool insert(string name, string type) {
        return current_scope->insert(name, type, *rs);
    }

    bool remove(string name) {
        return current_scope->remove(name);
    }

    void clearAllScopes() {
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    SymbolInfo *lookup(string name) {
        ScopeTable *curr = current_scope;
        while (curr != nullptr) {
            SymbolInfo *symbol = curr->lookup(name);
            if (symbol != nullptr) {
                return symbol;
            }
            curr = curr->getParent();
        }

        *os << "\t'" << name << "' not found in any of the ScopeTables\n";
        return nullptr;
    }

    void printCurrentScope() {
        if (current_scope != nullptr)
            current_scope->print();
    }

    void printAllScopes() {
        ScopeTable* curr = current_scope;
        int indent = 1;

        while (curr != nullptr) {
            curr->print(indent);
            curr = curr->getParent();
            indent++;
        }
    }
};

#endif