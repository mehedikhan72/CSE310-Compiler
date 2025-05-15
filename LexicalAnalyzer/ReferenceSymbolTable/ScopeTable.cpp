#include "SymbolInfo.cpp"
#include "hash.cpp"
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

class ScopeTable {
private:
    int id;
    SymbolInfo **hash_table;
    string hash_function_name;
    int num_buckets;
    ScopeTable *parent;
    static int next_id;

public:
    ScopeTable(string hash_function_name, int num_buckets, ScopeTable *parent) {
        if (parent == nullptr) {
            this->id = 1;
            next_id = 2;
        } else {
            this->id = next_id++;
        }

        this->hash_function_name = hash_function_name;
        this->num_buckets = num_buckets;
        this->parent = parent;
        this->hash_table = new SymbolInfo *[num_buckets];

        for (int i = 0; i < num_buckets; i++) {
            hash_table[i] = nullptr;
        }
    }

    ~ScopeTable() {
        for (int i = 0; i < num_buckets; i++) {
            SymbolInfo *curr = hash_table[i];
            SymbolInfo *temp;

            while (curr != nullptr) {
                temp = curr;
                curr = curr->getNext();
                temp->setNext(nullptr);
                delete temp;
            }
        }

        delete[] hash_table;
    }

    int getId() {
        return id;
    }

    string getHashFunctionName() {
        return hash_function_name;
    }

    int getNumBuckets() {
        return num_buckets;
    }

    ScopeTable *getParent() {
        return parent;
    }

    void setNumBuckets(int num_buckets) {
        this->num_buckets = num_buckets;
    }

    void setParent(ScopeTable *parent) {
        this->parent = parent;
    }

    bool insert(string name, string type) {
        SymbolInfo *symbol = new SymbolInfo(name, type);
        int index = sdbmHash(symbol->getName().c_str()) % num_buckets;
        // read collision from rs
        int collisions = 0;

        SymbolInfo *head = hash_table[index];
        if (head == nullptr) {
            hash_table[index] = symbol;
            return true;
        } else {
            SymbolInfo *curr = head;
            int list_position = 1;
            while (true) {
                if (curr->getName() == symbol->getName()) {
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
            list_position++;
            collisions++;

            return true;
        }
    }

    SymbolInfo *lookup(string name) {
        int index = sdbmHash(name.c_str()) % num_buckets;

        SymbolInfo *curr = hash_table[index];
        int list_position = 1;
        while (curr != nullptr) {
            if (curr->getName() == name) {
                return curr;
            }
            curr = curr->getNext();
            list_position++;
        }

        // not found.
        return nullptr;
    }

    bool remove(string name) {
        int index = sdbmHash(name.c_str()) % num_buckets;

        SymbolInfo *curr = hash_table[index];
        SymbolInfo *prev = nullptr;
        int list_position = 1;

        while (curr != nullptr) {
            if (curr->getName() == name) {
                if (prev == nullptr) {
                    hash_table[index] = curr->getNext();
                } else {
                    prev->setNext(curr->getNext());
                }

                curr->setNext(nullptr);
                delete curr;
                return true;
            }
            prev = curr;
            curr = curr->getNext();
            list_position++;
        }
        // Not found
        return false;
    }
};

int ScopeTable::next_id = 2;