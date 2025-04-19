#ifndef SYMBOLINFO_HPP
#define SYMBOLINFO_HPP

#include <iostream>
#include <sstream>
#include <string>

using namespace std;

class SymbolInfo {
private:
    string name;
    string type;
    SymbolInfo *next;

public:
    SymbolInfo(string name, string rawType) {
        this->name = name;
        this->next = nullptr;

        istringstream ss(rawType);
        string baseType;
        ss >> baseType;

        if (baseType == "FUNCTION") {
            string returnType;
            ss >> returnType;

            string param, paramsFormatted = "";
            bool first = true;

            while (ss >> param) {
                if (!first)
                    paramsFormatted += ",";
                paramsFormatted += param;
                first = false;
            }

            this->type = "FUNCTION," + returnType + "<==(" + paramsFormatted + ")";

        } else if (baseType == "STRUCT" || baseType == "UNION") {
            string t, v;
            string structFormatted = "{";
            bool first = true;

            while (ss >> t >> v) {
                if (!first)
                    structFormatted += ",";
                structFormatted += "(" + t + "," + v + ")";
                first = false;
            }

            structFormatted += "}";
            this->type = baseType + "," + structFormatted;
        } else {
            this->type = baseType;
        }
    }

    ~SymbolInfo() {
        SymbolInfo *curr = this;
        SymbolInfo *temp;
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

    SymbolInfo *getNext() {
        return next;
    }

    void setName(string name) {
        this->name = name;
    }

    void setType(string type) {
        this->type = type;
    }

    void setNext(SymbolInfo *next) {
        this->next = next;
    }

    string toString() {
        return "<" + name + "," + type + ">";
    }
};

#endif