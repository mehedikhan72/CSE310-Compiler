#ifndef SCOPETABLE_HEADER
#define SCOPETABLE_HEADER

#include "2105141_SymbolInfo.hpp"
#include "2105141_hash.hpp"
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

class ScopeTable {
private:
    int id;
    SymbolInfo **hash_table;
    string hash_function_name;
    HashFunction hash_func;
    int num_buckets;
    ScopeTable *parent;
    ofstream *os;
    static int next_id;

public:
    ScopeTable(string hash_function_name, int num_buckets, ScopeTable *parent, ofstream &output_stream) {
        if (parent == nullptr) {
            this->id = 1;
            next_id = 2;
        } else {
            this->id = next_id++;
        }

        this->hash_function_name = hash_function_name;
        this->hash_func = getHashFunction(hash_function_name);
            this->num_buckets = num_buckets;
        this->parent = parent;
        this->hash_table = new SymbolInfo *[num_buckets];
        this->os = &output_stream;

        for (int i = 0; i < num_buckets; i++) {
            hash_table[i] = nullptr;
        }

        *os << "\tScopeTable# " << id << " created\n";
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

        *os << "\tScopeTable# " << id << " removed\n";
    }

    HashFunction getHashFunction(string hash_name) {
        if (hash_name == "djb2") {
            return djb2_hash;
        } else if (hash_name == "fnv1a") {
            return fnv1a_hash;
        } else {
            return sdbm_hash;
        }
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

    void setOutputStream(ofstream &output_stream) {
        this->os = &output_stream;
    }

    bool insert(string name, string type, fstream &rs) {
        SymbolInfo *symbol = new SymbolInfo(name, type);
        int index = hash_func(symbol->getName(), num_buckets);
        // read collision from rs
        int collisions = 0;
        rs.seekg(0);
        rs >> collisions;

        SymbolInfo *head = hash_table[index];
        if (head == nullptr) {
            hash_table[index] = symbol;
            *os << "\tInserted in ScopeTable# " << id << " at position " << index + 1 << ", 1\n";
            return true;
        } else {
            SymbolInfo *curr = head;
            int list_position = 1;
            while (true) {
                if (curr->getName() == symbol->getName()) {
                    *os << "\t'" << name << "' already exists in the current ScopeTable\n";
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
            *os << "\tInserted in ScopeTable# " << id << " at position " << index + 1 << ", " << list_position << "\n";
            collisions++;

            rs.seekp(0);
            rs.clear();
            rs << collisions << '\n';
            rs.flush();

            return true;
        }
    }

    SymbolInfo *lookup(string name) {
        int index = sdbm_hash(name, num_buckets);

        SymbolInfo *curr = hash_table[index];
        int list_position = 1;
        while (curr != nullptr) {
            if (curr->getName() == name) {
                *os << "\t'" << name << "' found in ScopeTable# " << id << " at position " << index + 1 << ", " << list_position << "\n";
                return curr;
            }
            curr = curr->getNext();
            list_position++;
        }

        // not found.
        return nullptr;
    }

    bool remove(string name) {
        int index = sdbm_hash(name, num_buckets);

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

                *os << "\tDeleted '" << name << "' from ScopeTable# " << id << " at position " << index + 1 << ", " << list_position << "\n";
                curr->setNext(nullptr);
                delete curr;
                return true;
            }
            prev = curr;
            curr = curr->getNext();
            list_position++;
        }
        // Not found
        *os << "\tNot found in the current ScopeTable\n";
        return false;
    }

    bool print(int indentLevel = 1) {
        string indent(indentLevel, '\t');

        *os << indent << "ScopeTable# " << id << "\n";

        for (int i = 0; i < num_buckets; i++) {
            *os << indent << i + 1 << "--> ";

            SymbolInfo *curr = hash_table[i];
            while (curr != nullptr) {
                *os << curr->toString() << " ";
                curr = curr->getNext();
            }
            *os << "\n";
        }

        return true;
    }
};

int ScopeTable::next_id = 2;

#endif