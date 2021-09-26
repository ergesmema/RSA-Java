# RSA in Java 

Implementation of RSA in Java using BigInteger Library. 
RSA is a public-key asymetric encryption method used for secure data transfers. 
This repository provides the implementation in java, as well as an analysis of algorithms' time complexities.

## Description

The program starts by generating two random prime numbers of 32 bits. (Note: The classic variables (p, q, e, etc.) are used in order to provide quick reference and better understanding for the reader.) After p and q are generated, they are multiplied in order to achieve 𝑁𝑁, which is part of both the public and private key pair. After that, we calculate ∅(𝑁) which is used in the calculations of both the variables e and d, that are used in public and private key pairs respectively. e is a number relatively prime to ∅(𝑁), while d is found as the modular multiplicative inverse of the pair (𝑒,∅(N)).

Find time complexity analysis [here](https://drive.google.com/file/d/1TcCV-OBiV1nMpx4QR1PE9Ej4dgt3rZXQ/view?usp=sharing).

## Getting Started

### Dependencies

Java JDK 13 or newer.
Older versions might also work.

### Executing program

An intuitive terminal program is available for testing the implementation by running the main() method of RSA.

### Sample Output
```
----Public Keys are made available----
Bob says: Hello world, my public key is N=506843262446850739 and e=65537 
Alice says: Hello world, my public key is N=12647022965299532473 and e=65537

----Message from Alice to Bob----
What should Alice say:
Hello Bob!
Alice says: Hello Bob!
Charlie reads (encoded text):
u��+>Q�a��;{v
Bob reads (decoded text): Hello Bob!


----Message from Bob to Alice----
What should Bob say:
Hello Alice
Bob says: Hello Alice
Charlie reads (encoded text):
u��+>Q�a��;{vbD�Sd�V� ����5�)
Alice reads (decoded text): Hello Alice 

----Charlie started breaking RSA---- 
Time to find Bob's Decryption key: 29 sec 
Charlie found decryption key of Bob: 112687690016002265 
Charlie decrypted message send to Bob: Hello Bob! 
Time to find Alice's Decryption key: 26 sec 
Charlie found decryption key of Alice: 12063658592921248169 
Charlie decrypted message send to Alice: Hello Alice
```



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

