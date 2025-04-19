#ifndef SYMBOLINFO_HPP
#define SYMBOLINFO_HPP

#include <iostream>
#include <string>

using namespace std;

class SymbolInfo {
private:
    string name;
    string type;
    SymbolInfo* next;

public:
    SymbolInfo(string name, string type) {
        this->name = name;
        this->type = type;
        this->next = nullptr;
    }

    ~SymbolInfo() {
        SymbolInfo* curr = this;
        SymbolInfo* temp;
        while (curr != nullptr) {
            temp = curr;
            curr = curr->next;
            delete temp;
        }
    }


    string getName() {
        return name;
    }

    string getType() {
        return type;
    }

    SymbolInfo* getNext() {
        return next;
    }

    void setName(string name) {
        this->name = name;
    }

    void setType(string type) {
        this->type = type;
    }

    void setNext(SymbolInfo* next) {
        this->next = next;
    }
};

#endif