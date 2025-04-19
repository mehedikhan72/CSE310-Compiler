#ifndef SYMBOlTABLE_HPP
#define SYMBOlTABLE_HPP

#include "ScopeTable.hpp"

using namespace std;

class SymbolTable {
private:
    ScopeTable *current_scope;

public:
    SymbolTable(string hash_function, int num_buckets) {
        current_scope = new ScopeTable(hash_function, num_buckets);
    }

    ~SymbolTable() {
        delete current_scope;
    }

    void enterScope(string hash_function, int num_buckets) {
        current_scope = new ScopeTable(hash_function, num_buckets, current_scope);
    }

    void exitScope() {
        if (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            delete temp;
        } else {
            cout << "Client Error: Cannot exit the global scope." << endl;
        }
    }

    bool insert(string name, string type) {
        return current_scope->insert(name, type);
    }

    bool remove(string name) {
        return current_scope->remove(name);
    }

    SymbolInfo *lookup(string name) {
        ScopeTable* curr = current_scope;
        while (curr != nullptr) {
            SymbolInfo* symbol = curr->lookup(name);
            if(symbol != nullptr) {
                return symbol;
            }
            curr = curr->getParent();
        }

        cout << "Client Error: " << name << " not found in the symbol table." << endl;
        return nullptr;
    }

    void printCurrentScope() {
        current_scope->print();
    }

    void printAllScope() {
        ScopeTable* curr = current_scope;
        while (curr != nullptr) {
            curr->print();
            curr = curr->getParent();
        }
    }
};

#endif