#ifndef SCOPETABLE_HPP
#define SCOPETABLE_HPP

#include "SymbolInfo.hpp"
#include "hash.hpp"
#include <iostream>

using namespace std;

class ScopeTable {
private:
    int id;
    SymbolInfo **hash_table;
    string hash_function;
    int num_buckets;
    ScopeTable *parent;

public:
    ScopeTable(string hash_function, int num_buckets, ScopeTable *parent = nullptr) {
        if(parent == nullptr) {
            this->id = 1;
        } else {
            this->id = parent->id + 1;
        }

        this->hash_function = hash_function;
        this->num_buckets = num_buckets;
        this->parent = parent;
        this->hash_table = new SymbolInfo*[num_buckets];

        for(int i = 0; i < num_buckets; i++) {
            hash_table[i] = nullptr;
        }
    }

    ~ScopeTable() {
        for(int i = 0; i < num_buckets; i++) {
            SymbolInfo* curr = hash_table[i];
            SymbolInfo* temp;

            while(curr != nullptr) {
                temp = curr;
                curr = curr->getNext();
                delete temp;
            }
        }

        delete hash_table;
        if(parent != nullptr) delete parent;
    }

    int getId() {
        return id;
    }

    string getHashFunction() {
        return hash_function;
    }

    int getNumBuckets() {
        return num_buckets;
    }

    ScopeTable* getParent() {
        return parent;
    }

    void setNumBuckets(int num_buckets) {
        this->num_buckets = num_buckets;
    }

    bool insert(string name, string type) {
        SymbolInfo* symbol = new SymbolInfo(name, type);
        int index = sdbm_hash(symbol->getName()) % num_buckets;

        SymbolInfo* head = hash_table[index];
        if(head == nullptr) {
            hash_table[index] = symbol;
            return true;
        } else {
            SymbolInfo* curr = head;
            while(true) {
                if(curr->getName() == symbol->getName()) {
                    return false; // Symbol already exists
                }

                if(curr->getNext() == nullptr) {
                    break; // Reached the end of the linked list
                }
                curr = curr->getNext();
            }

            curr->setNext(symbol);
            return true;
        }
    }

    SymbolInfo* lookup(string name) {
        int index = sdbm_hash(name) % num_buckets;

        SymbolInfo* curr = hash_table[index];
        while(curr != nullptr) {
            if(curr->getName() == name) {
                return curr;
            }
            curr = curr->getNext();
        }

        return nullptr; // Not found
    }

    bool remove(string name) {
        int index = sdbm_hash(name) % num_buckets;

        SymbolInfo* curr = hash_table[index];
        SymbolInfo* prev = nullptr;

        while(curr != nullptr) {
            if(curr->getName() == name) {
                if(prev == nullptr) {
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

        return false; // Not found
    }

    bool print() {
        cout << "ScopeTable #" << id << endl;
        for(int i = 0; i < num_buckets; i++) {
            SymbolInfo* curr = hash_table[i];
            if(curr != nullptr) {
                cout << i << " --> ";
                while(curr != nullptr) {
                    cout << "<" << curr->getName() << ", " << curr->getType() << "> ";
                    curr = curr->getNext();
                }
                cout << endl;
            }
        }
        return true;
    }


};

#endif