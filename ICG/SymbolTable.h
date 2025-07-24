#pragma once

#include <iostream>
#include <sstream>
#include <string>
#include <fstream>

using namespace std;

// ===== Hash function =====

inline unsigned int sdbmHash(const char *p) {
    unsigned int hash = 0;
    auto *str = (unsigned char *)p;
    int c{};
    while ((c = *str++)) {
        hash = c + (hash << 6) + (hash << 16) - hash;
    }
    return hash;
}

// ===== SymbolInfo class =====

class SymbolInfo {
private:
    string name;
    string type;

    // ICG
    bool is_stack_var;
    int stack_offset;

    string func_ret_type;
    string func_args;
    int func_args_count;
    SymbolInfo *next;

public:
    SymbolInfo(string name, string raw_type, bool is_stack_var, int stack_offset = -1, string func_ret_type = "", string func_args = "", int func_args_count = 0) {
        this->name = name;
        this->next = nullptr;
        this->is_stack_var = is_stack_var;
        this->stack_offset = stack_offset;
        this->func_ret_type = func_ret_type;
        this->func_args = func_args;
        this->func_args_count = func_args_count;

        this->is_stack_var = is_stack_var;
        this->stack_offset = stack_offset;

        istringstream ss(raw_type);
        string base_type;
        ss >> base_type;

        if (base_type == "FUNCTION") {
            string return_type;
            ss >> return_type;

            string param, params_formatted = "";
            bool first = true;

            while (ss >> param) {
                if (!first)
                    params_formatted += ",";
                params_formatted += param;
                first = false;
            }

            this->type = "FUNCTION," + return_type + "<==(" + params_formatted + ")";
        } else if (base_type == "STRUCT" || base_type == "UNION") {
            string t, v;
            string struct_formatted = "{";
            bool first = true;

            while (ss >> t >> v) {
                if (!first)
                    struct_formatted += ",";
                struct_formatted += "(" + t + "," + v + ")";
                first = false;
            }

            struct_formatted += "}";
            this->type = base_type + "," + struct_formatted;
        } else {
            this->type = base_type;
        }
    }

    ~SymbolInfo() {}

    string getName() { return name; }
    string getType() { return type; }
    bool isStackVar() { return is_stack_var; }
    int getStackOffset() { return stack_offset; }
    string getFuncRetType() { return func_ret_type; }
    string getFuncArgs() { return func_args; }
    int getFuncArgsCount() { return func_args_count; }
    SymbolInfo *getNext() { return next; }

    void setName(string name) { this->name = name; }
    void setType(string type) { this->type = type; }
    void setIsStackVar(bool is_stack_var) { this->is_stack_var = is_stack_var; }
    void setStackOffset(int stack_offset) { this->stack_offset = stack_offset; }
    void setFuncRetType(string func_ret_type) { this->func_ret_type = func_ret_type; }
    void setFuncArgs(string func_args) { this->func_args = func_args; }
    void setFuncArgsCount(int func_args_count) { this->func_args_count = func_args_count; }
    void setNext(SymbolInfo *next) { this->next = next; }

    string toString() { return "<" + name + "," + type + ">"; }
};

// ===== ScopeTable class =====

class ScopeTable {
private:
    string id; // Change from int to string
    SymbolInfo **hash_table;
    string hash_function_name;
    int num_buckets;
    ScopeTable *parent;
    ofstream *log_output;
    int child_count = 0; // Track number of direct child scopes

public:
    ScopeTable(string hash_function_name, int num_buckets, ScopeTable *parent, ofstream *log_output) {
        this->hash_function_name = hash_function_name;
        this->num_buckets = num_buckets;
        this->parent = parent;
        this->log_output = log_output;

        if (parent == nullptr) {
            this->id = "1";
        } else {
            parent->child_count++;
            this->id = parent->id + "." + to_string(parent->child_count);
        }

        this->hash_table = new SymbolInfo *[num_buckets];
        for (int i = 0; i < num_buckets; i++) {
            hash_table[i] = nullptr;
        }
    }

    ~ScopeTable() {
        for (int i = 0; i < num_buckets; i++) {
            SymbolInfo *curr = hash_table[i];
            while (curr != nullptr) {
                SymbolInfo *temp = curr;
                curr = curr->getNext();
                delete temp;
            }
        }
        delete[] hash_table;
    }

    string getId() { return id; }
    ScopeTable *getParent() { return parent; }
    void setParent(ScopeTable *parent) { this->parent = parent; }

    bool insert(string name, string type, bool is_stack_var = false, int stack_offset = -1, string func_ret_type = "", string func_args = "", int func_args_count = 0) {
        SymbolInfo *symbol = new SymbolInfo(name, type, is_stack_var, stack_offset, func_ret_type, func_args, func_args_count);
        int index = sdbmHash(symbol->getName().c_str()) % num_buckets;

        SymbolInfo *head = hash_table[index];
        if (head == nullptr) {
            hash_table[index] = symbol;
            return true;
        } else {
            SymbolInfo *curr = head;
            int list_position = 1;
            while (true) {
                if (curr->getName() == symbol->getName()) {
                    // (*log_output) << "< " << symbol->getName() << " : " << symbol->getType() << " > already exists in ScopeTable# "
                    //               << id << " at position " << index << ", " << list_position - 1 << endl;
                    delete symbol;
                    return false;
                }

                if (curr->getNext() == nullptr) {
                    break;
                }
                curr = curr->getNext();
                list_position++;
            }

            curr->setNext(symbol);
            return true;
        }
    }

    SymbolInfo *lookup(string name) {
        int index = sdbmHash(name.c_str()) % num_buckets;

        SymbolInfo *curr = hash_table[index];
        while (curr != nullptr) {
            if (curr->getName() == name) {
                return curr;
            }
            curr = curr->getNext();
        }
        return nullptr;
    }

    bool remove(string name) {
        int index = sdbmHash(name.c_str()) % num_buckets;

        SymbolInfo *curr = hash_table[index];
        SymbolInfo *prev = nullptr;

        while (curr != nullptr) {
            if (curr->getName() == name) {
                if (prev == nullptr) {
                    hash_table[index] = curr->getNext();
                } else {
                    prev->setNext(curr->getNext());
                }

                delete curr;
                return true;
            }
            prev = curr;
            curr = curr->getNext();
        }

        return false;
    }

    bool print() {
        (*log_output) << "ScopeTable # " << id << endl;

        for (int i = 0; i < num_buckets; i++) {
            string line = to_string(i) + " --> ";

            SymbolInfo *curr = hash_table[i];
            while (curr != nullptr) {
                line += "< " + curr->getName() + " : " + curr->getType() + " >";
                curr = curr->getNext();
            }
            if (line != to_string(i) + " --> ") {
                (*log_output) << line << endl;
            }
        }
        return true;
    }

    string getCurrentScopeId() {
        return id;
    }
};

// ===== SymbolTable class =====
class SymbolTable {
private:
    string hash_function_name;
    int num_buckets;
    ScopeTable *current_scope;
    ofstream *log_output;

public:
    SymbolTable(string hash_function_name, int num_buckets, ofstream *log_output) {
        this->hash_function_name = hash_function_name;
        this->num_buckets = num_buckets;
        this->log_output = log_output;
        this->current_scope = new ScopeTable(hash_function_name, num_buckets, nullptr, log_output);
    }

    ~SymbolTable() {
        while (current_scope != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    void enterScope() {
        this->current_scope = new ScopeTable(this->hash_function_name, this->num_buckets, this->current_scope, this->log_output);
    }

    void exitScope() {
        if (current_scope->getParent() != nullptr) {
            ScopeTable *temp = current_scope;
            current_scope = current_scope->getParent();
            temp->setParent(nullptr);
            delete temp;
        }
    }

    bool insert(string name, string type, bool is_stack_var = false, int stack_offset = -1, string func_ret_type = "", string func_args = "", int func_args_count = 0) {
        return current_scope->insert(name, type, is_stack_var, stack_offset, func_ret_type, func_args, func_args_count);
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
        ScopeTable *curr = current_scope;

        while (curr != nullptr) {
            curr->print();
            curr = curr->getParent();
        }
        (*log_output) << endl;
    }

    string getCurrentScopeId() {
        return current_scope->getCurrentScopeId();
    }
};
