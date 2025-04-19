#ifndef SCOPETABLE_HPP
#define SCOPETABLE_HPP

#include "SymbolInfo.hpp"
#include "hash.hpp"
#include <fstream>
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
        if (parent == nullptr) {
            this->id = 1;
        } else {
            this->id = parent->id + 1;
        }

        this->hash_function = hash_function;
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
                temp->setNext(nullptr); // Avoids dangling pointer
                delete temp;
            }
        }

        delete hash_table;
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

    ScopeTable *getParent() {
        return parent;
    }

    void setNumBuckets(int num_buckets) {
        this->num_buckets = num_buckets;
    }

    void setParent(ScopeTable *parent) {
        this->parent = parent;
    }

    bool insert(string name, string type, ofstream &os) {
        SymbolInfo *symbol = new SymbolInfo(name, type);
        int index = sdbm_hash(symbol->getName()) % num_buckets;

        SymbolInfo *head = hash_table[index];
        if (head == nullptr) {
            hash_table[index] = symbol;
            return true;
        } else {
            SymbolInfo *curr = head;
            int list_position = 1;
            while (true) {
                if (curr->getName() == symbol->getName()) {
                    os << "\t'" << name << "' already exists in ScopeTable\n";
                    delete symbol;
                    return false;
                }

                if (curr->getNext() == nullptr) {
                    break; // Reached the end of the linked list
                }
                curr = curr->getNext();
                list_position++;
            }

            curr->setNext(symbol);
            os << "\tInserted in ScopeTable# " << id << " at position" << index + 1 << ", " << list_position << "\n";
            return true;
        }
    }

    SymbolInfo *lookup(string name, ofstream &os) {
        int index = sdbm_hash(name) % num_buckets;

        SymbolInfo *curr = hash_table[index];
        int list_position = 1;
        while (curr != nullptr) {
            if (curr->getName() == name) {
                os << "\t'" << name << "' found in ScopeTable# " << id << " at position " << index + 1 << ", " << list_position << "\n";
                return curr;
            }
            curr = curr->getNext();
            list_position++;
        }

        // not found.
        os << "\t'" << name << "' not found in any of the ScopeTables\n";
        return nullptr;
    }

    bool remove(string name, ofstream &os) {
        int index = sdbm_hash(name) % num_buckets;

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

                os << "\tDeleted '" << name << "' from ScopeTable# " << id << " at position " << index + 1 << ", " << list_position << "\n";
                curr->setNext(nullptr);
                delete curr;
                return true;
            }
            prev = curr;
            curr = curr->getNext();
            list_position++;
        }
        // Not found
        os << "Not found in the current ScopeTable\n";
        return false; 
    }

    bool print(ofstream &os, int indentLevel = 0) {
        string indent(indentLevel, '\t');

        os << indent << "ScopeTable# " << id << "\n";

        for (int i = 0; i < num_buckets; i++) {
            os << indent << i + 1 << "--> ";

            SymbolInfo *curr = hash_table[i];
            while (curr != nullptr) {
                os << curr->toString() << " ";
                curr = curr->getNext();
            }
            os << "\n";
        }

        return true;
    }
};

#endif