# RSA in Java 

Implementation of RSA in Java using BigInteger Library. 
RSA is a public-key asymetric encryption method used for secure data transfers. 
This repository provides the implementation in java, as well as an analysis of algorithms' time complexities.

## Description

The program starts by generating two random prime numbers of 32 bits. (Note: The classic variables (p, q, e, etc.) are used in order to provide quick reference and better understanding for the reader.) After p and q are generated, they are multiplied in order to achieve 𝑁𝑁, which is part of both the public and private key pair. After that, we calculate ∅(𝑁) which is used in the calculations of both the variables e and d, that are used in public and private key pairs respectively. e is a number relatively prime to ∅(𝑁), while d is found as the modular multiplicative inverse of the pair (𝑒,∅(N)).

## Getting Started

### Dependencies

Java JDK 13 or newer.
Older versions might also work.

### Executing program

An intuitive terminal program is available for testing the implementation by running the main() method of RSA.

## Authors

Contributors names and contact info:

Erges Mema  
[@MemaErges](https://twitter.com/memaerges)

## Version History

* 0.1
    * Initial Release

## Acknowledgments

Pseudocode references from:

Dasgupta, S., Papadimitriou, C. H., & Vazirani, U. V. (2006). Algorithms. Boston: McGraw-Hill Higher Education.
