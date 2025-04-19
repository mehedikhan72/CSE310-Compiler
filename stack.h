#ifndef STACK_H
#define STACK_H

#include <iostream>

template <typename T>
class Stack {
private:
    int top;
    int current_memory_chunk;
    T *arr;

public:
    bool getUserInput() {
        // true for success and false for failure

        int memory_chunk;
        int initial_len;

        std::cout << "Memory chunk: ";
        std::cin >> memory_chunk;

        std::cout << "Initial Stack Length: ";
        std::cin >> initial_len;

        if (initial_len > memory_chunk) {
            std::cout << "Memory chunk must be greater than or equal to initial length." << std::endl;
            return false;
        }

        current_memory_chunk = memory_chunk;
        top = initial_len;

        return true;
    }

    void initialInsert() {
        if (top == 0)
            return;

        std::cout << "Enter the elements of the stack: " << std::endl;
        for (int i = 0; i < top; i++) {
            std::cin >> arr[i];
        }
    }

    void allocateMemory() {
        arr = new T[current_memory_chunk];
        if (arr == nullptr) {
            std::cout << "Memory allocation failed!";
            exit(EXIT_FAILURE);
        }

        initialInsert();
    }

    void deleteMemory() {
        delete[] arr;
    }

    void doubleTheMemory() {
        current_memory_chunk = current_memory_chunk * 2;
        T *temp_arr = new T[current_memory_chunk];

        for (int i = 0; i < top; i++) {
            temp_arr[i] = arr[i];
        }

        delete[] arr;

        arr = temp_arr;
    }

    Stack() {
        bool input_successful = getUserInput();
        if (input_successful) {
            allocateMemory();
        }
    }

    // Constructor for the operators and operands in task 2
    Stack(int memory_chunk, int initial_len) {
        if (initial_len > memory_chunk) {
            std::cout << "Memory chunk must be greater than or equal to initial length." << std::endl;
            exit(EXIT_FAILURE);
        }

        current_memory_chunk = memory_chunk;
        top = initial_len;

        arr = new T[current_memory_chunk];
        if (arr == nullptr) {
            std::cout << "Memory allocation failed!";
            exit(EXIT_FAILURE);
        }
    }

    ~Stack() {
        delete[] arr;
    }

    void displayStack() {
        std::cout << "Current Stack: " << std::endl
                  << "< ";

        for (int i = 0; i < top; i++) {
            if (i == top - 1) {
                std::cout << arr[i] << " ";
            } else {
                std::cout << arr[i] << ", ";
            }
        }
        std::cout << ">" << std::endl;
    }

    void clear() {
        if (top != 0) {
            top = 0;
        } else {
            std::cout << "Client Error: Cannot clear an empty stack." << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    void push(T item) {
        if (top >= current_memory_chunk) {
            doubleTheMemory();
        }

        arr[top] = item;
        top++;
    }

    T pop() {
        if (top == 0) {
            std::cout << "Client Error: Stack is already empty." << std::endl;
            exit(EXIT_FAILURE);
        }

        T popped_elem = arr[top - 1];
        top--;

        return popped_elem;
    }

    int length() {
        return top;
    }

    T topValue() {
        if (top != 0) {
            return arr[top - 1];
        } else {
            std::cout << "Client Error: Stack is empty. Cannot return top value." << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    bool isEmpty() {
        return top == 0;
    }
};

#endif
