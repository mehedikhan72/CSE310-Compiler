#include "ScopeTable.cpp"
#include "fstream"

using namespace std;

class SymbolTable {
private:
    ScopeTable *current_scope;

public:
    SymbolTable(string hash_function_name, int num_buckets) {
        current_scope = new ScopeTable(hash_function_name, num_buckets, nullptr);
    }

    ~SymbolTable() {
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    void enterScope(string hash_function_name, int num_buckets) {
        current_scope = new ScopeTable(hash_function_name, num_buckets, current_scope);
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
};