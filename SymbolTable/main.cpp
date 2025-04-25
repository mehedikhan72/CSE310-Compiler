#include "SymbolTable.hpp"
#include <fstream>
#include <iostream>
#include <limits>
#include <sstream>

using namespace std;

string rtrim(const string &s) {
    size_t end = s.find_last_not_of(" \n\r\t\f\v");
    return (end == string::npos) ? "" : s.substr(0, end + 1);
}

int main(int argc, char *argv[]) {
    if (argc != 4 && argc != 5) {
        cerr << "Usage: " << argv[0] << " <input_file> <output_file> <report_file(report.txt)> <hash_function_name(optional)>" << endl;
        return 1;
    }

    ifstream input_file(argv[1]);
    ofstream output_file(argv[2]);
    fstream report_file(argv[3]);

    string hash_function_name = "sdbm";
    if (argc == 5) {
        hash_function_name = argv[4];
    }

    // initially collision count is 0
    report_file << 0 << '\n';

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

    SymbolTable symbol_table(hash_function_name, num_buckets, output_file, report_file);
    int command_number = 1;
    string line;

    while (getline(input_file, line)) {
        line = rtrim(line);
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

            bool inserted = symbol_table.insert(name, full_type);
        }

        else if (cmd == "L") {
            string name, extra;
            iss >> name >> extra;

            if (!extra.empty()) {
                output_file << "\tNumber of parameters mismatch for the command L\n";
                continue;
            }

            SymbolInfo *result = symbol_table.lookup(name);
        }

        else if (cmd == "D") {
            string name, extra;
            iss >> name >> extra;

            if (name.empty() || !extra.empty()) {
                output_file << "\tNumber of parameters mismatch for the command D\n";
                continue;
            }

            bool deleted = symbol_table.remove(name);
        }

        else if (cmd == "S") {
            symbol_table.enterScope(hash_function_name, num_buckets);
        }

        else if (cmd == "E") {
            symbol_table.exitScope();
        }

        else if (cmd == "P") {
            string option;
            iss >> option;

            if (option == "A") {
                symbol_table.printAllScopes();
            } else if (option == "C") {
                symbol_table.printCurrentScope();
            } else {
                output_file << "\tInvalid print option\n";
            }
        }

        else if (cmd == "Q") {
            symbol_table.clearAllScopes();
            break;
        }

        else {
            output_file << "\tUnrecognized command\n";
        }
    }

    // update report based on the total collision count.
    int collisions = 0;
    report_file.seekg(0);
    report_file >> collisions;

    double mean_ratio = static_cast<double>(collisions) / num_buckets;

    report_file.seekp(0);
    report_file.clear();

    // cout << "hash function: " << hash_function_name << endl;
    // cout << "total collisions: " << collisions << endl;
    // cout << "total number of buckets: " << num_buckets << endl;
    // cout << "mean ratio: " << mean_ratio << endl;

    report_file << "hash function: " << hash_function_name << endl;
    report_file << "total collisions: " << collisions << endl;
    report_file << "total number of buckets: " << num_buckets << endl;
    report_file << "mean ratio: " << mean_ratio << endl;

    report_file.close();
    input_file.close();
    output_file.close();
    return 0;
}
