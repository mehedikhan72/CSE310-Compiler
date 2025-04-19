#ifndef SYMBOlTABLE_HPP
#define SYMBOlTABLE_HPP

#include "fstream"
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
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }   
    }

    void enterScope(string hash_function, int num_buckets, ofstream& os) {
        current_scope = new ScopeTable(hash_function, num_buckets, current_scope);
        os << "\tScopeTable# " << current_scope->getId() << " created\n";
    }

    void exitScope(ofstream& os) {
        if (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
            os << "\tScopeTable# " << temp->getId() << " removed\n";
        } else {
            os << "\tCannot exit from the global ScopeTable\n";
        }
    }

    bool insert(string name, string type, ofstream& os) {
        return current_scope->insert(name, type, os);
    }

    bool remove(string name, ofstream& os) {
        return current_scope->remove(name, os);
    }

    void clearAllScopes(ofstream& os) {
        while (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            os << "\tScopeTable# " << temp->getId() << " removed\n";
            temp->setParent(nullptr);
            delete temp;
        }
    }

    SymbolInfo *lookup(string name, ofstream& os) {
        ScopeTable *curr = current_scope;
        while (curr != nullptr) {
            SymbolInfo *symbol = curr->lookup(name, os);
            if (symbol != nullptr) {
                return symbol;
            }
            curr = curr->getParent();
        }

        os << "\t'" << name << "' not found in any of the ScopeTables\n";
        return nullptr;
    }

    void printCurrentScope(ofstream& os) {
        if (current_scope != nullptr)
            current_scope->print(os);
    }

    void printAllScopes(ofstream& os) {
        ScopeTable* curr = current_scope;
        int indent = 0;

        while (curr != nullptr) {
            curr->print(os, indent);
            curr = curr->getParent();
            indent++;
        }
    }

};

#endif