#include "SymbolTable.hpp"
#include <fstream>
#include <iostream>
#include <limits>
#include <sstream>

using namespace std;

int main(int argc, char *argv[]) {
    if (argc != 3) {
        cerr << "Usage: " << argv[0] << " <input_file> <output_file>" << endl;
        return 1;
    }

    ifstream input_file(argv[1]);
    ofstream output_file(argv[2]);

    if (!input_file.is_open()) {
        cerr << "Error opening input file: " << argv[1] << endl;
        return 1;
    }

    if (!output_file.is_open()) {
        cerr << "Error opening output file: " << argv[2] << endl;
        return 1;
    }

    int num_buckets;
    input_file >> num_buckets;
    input_file.ignore(numeric_limits<streamsize>::max(), '\n');

    // TODO: later put to global var.
    string hash_function = "sdbm";
    SymbolTable symbol_table(hash_function, num_buckets);
    int command_number = 1;
    string line;

    while (getline(input_file, line)) {
        if (line.empty())
            continue;

        output_file << "Cmd " << command_number++ << ": " << line << "\n";

        istringstream iss(line);
        string cmd;
        iss >> cmd;

        if (cmd == "I") {
            string name, raw_type;
            iss >> name >> raw_type;

            string rest, full_type = raw_type;
            while (iss >> rest) {
                full_type += " " + rest;
            }

            bool inserted = symbol_table.insert(name, full_type, output_file);
        }

        else if (cmd == "L") {
            string name, extra;
            iss >> name >> extra;

            if (!extra.empty()) {
                output_file << "\tNumber of parameters mismatch for the command L\n";
                continue;
            }

            SymbolInfo *result = symbol_table.lookup(name, output_file);
        }

        else if (cmd == "D") {
            string name, extra;
            iss >> name >> extra;

            if (name.empty() || !extra.empty()) {
                output_file << "\tNumber of parameters mismatch for the command D\n";
                continue;
            }

            bool deleted = symbol_table.remove(name, output_file);
            if (!deleted) {
                output_file << "\tNot found in the current ScopeTable\n";
            }
        }

        else if (cmd == "S") {
            symbol_table.enterScope(hash_function, num_buckets, output_file);
        }

        else if (cmd == "E") {
            symbol_table.exitScope(output_file);
        }

        else if (cmd == "P") {
            string option;
            iss >> option;

            if (option == "A") {
                symbol_table.printAllScopes(output_file);
            } else if (option == "C") {
                symbol_table.printCurrentScope(output_file);
            } else {
                output_file << "\tInvalid print option\n";
            }
        }

        else if (cmd == "Q") {
            symbol_table.clearAllScopes(output_file);
            break;
        }

        else {
            output_file << "\tUnrecognized command\n";
        }
    }

    input_file.close();
    output_file.close();
    return 0;
}
