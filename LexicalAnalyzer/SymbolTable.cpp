#include "ScopeTable.cpp"
#include "fstream"

using namespace std;

class SymbolTable {
private:
    ScopeTable *current_scope;
    FILE *log_output;

public:
    SymbolTable(string hash_function_name, int num_buckets, FILE *log_output) {
        this->log_output = log_output;
        current_scope = new ScopeTable(hash_function_name, num_buckets, nullptr, log_output);
    }

    ~SymbolTable() {
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    void enterScope(string hash_function_name, int num_buckets, FILE *log_output) {
        current_scope = new ScopeTable(hash_function_name, num_buckets, current_scope, log_output);
    }

    void exitScope() {
        if (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    bool insert(string name, string type) {
        return current_scope->insert(name, type);
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

        return nullptr;
    }

    void printAllScopes() {
        ScopeTable* curr = current_scope;

        while (curr != nullptr) {
            curr->print();
            curr = curr->getParent();
        }
        fprintf(log_output, "\n");
    }
};