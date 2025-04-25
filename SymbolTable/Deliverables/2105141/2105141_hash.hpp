typedef unsigned int (*HashFunction)(string, unsigned int);

unsigned int sdbm_hash(string str, unsigned int num_buckets) {
    unsigned int hash = 0;
    unsigned int len = str.length();

    for (unsigned int i = 0; i < len; i++) {
        hash = ((str[i]) + (hash << 6) + (hash << 16) - hash) %
               num_buckets;
    }

    return hash;
}

// 1. DJB2 Hash Function
// Created by Dan Bernstein, this is a simple but effective hash function
unsigned int djb2_hash(string str, unsigned int num_buckets) {
    unsigned int hash = 5381; // Initial value: a prime number
    unsigned int len = str.length();

    for (unsigned int i = 0; i < len; i++) {
        // hash * 33 + character
        hash = ((hash << 5) + hash) + str[i];
    }

    return hash % num_buckets;
}

// 2. FNV-1a Hash Function
// Fowler-Noll-Vo is a non-cryptographic hash function created by 
// Glenn Fowler, Landon Curt Noll, and Phong Vo
unsigned int fnv1a_hash(string str, unsigned int num_buckets) {
    // FNV prime and offset basis for 32-bit
    const unsigned int FNV_PRIME = 16777619;
    const unsigned int OFFSET_BASIS = 2166136261;
    
    unsigned int hash = OFFSET_BASIS;
    unsigned int len = str.length();
    
    for (unsigned int i = 0; i < len; i++) {
        hash = hash ^ str[i];       // XOR the byte with the hash
        hash = hash * FNV_PRIME;    // Multiply by the prime
    }
    
    return hash % num_buckets;
}