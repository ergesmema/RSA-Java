package rsa;

import java.math.BigInteger;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class RSA {

    private BigInteger p;

    private BigInteger q;

    private BigInteger N;

    private BigInteger phi;

    private BigInteger e;

    private BigInteger d;

    private int bitlength = 32;

    private int certainty = 10;

    private Random r;

    public RSA() {

        r = new Random();

        p = probablePrime(bitlength, r);

        q = probablePrime(bitlength, r);

        N = p.multiply(q);

        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.valueOf(65537);
        BigInteger gcd = gcd(e, phi);
        while (!gcd.equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
            gcd = gcd(e, phi);
        }

        d = ExtendedEuclidean.modInverse(e, phi);
    }

    public BigInteger gcd(BigInteger a, BigInteger b) {
        if (BigInteger.ZERO.equals(b)) {
            return a;
        }
        BigInteger gcd = gcd(b, a.mod(b));
        return gcd;
    }

    public BigInteger probablePrime(int bitlength, Random r) {
        BigInteger number = new BigInteger(bitlength, r);

        while (isProbablePrime(number, certainty) == false) {

            number = new BigInteger(bitlength, r);
        }
        return number;
    }

    public boolean isProbablePrime(BigInteger n, int certainty) {

        for (int i = 0; i < certainty; i++) {
            Random rand = new Random();
            BigInteger a = new BigInteger(bitlength / 2, rand);
            BigInteger powNum = n.subtract(BigInteger.ONE);
            if (!(modPow(a, powNum, n).equals(BigInteger.ONE))) {
                return false;
            }
        }
        return true;
    }


    public BigInteger modPow(BigInteger base, BigInteger exponent, BigInteger mod) {
        if (exponent.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }
        BigInteger z = modPow(base, exponent.divide(BigInteger.TWO), mod);
        if (exponent.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return z.multiply(z).mod(mod);
        } else
            return base.multiply(z.multiply(z)).mod(mod);
    }


    public static void main(String[] args) throws IOException {
        RSA Bob = new RSA();
        RSA Alice = new RSA();

        RSA Charlie = new RSA();
        DataInputStream in = new DataInputStream(System.in);
        int blockSize = 7;

        System.out.println("----Public Keys are made available----");
        System.out.println("Bob says: Hello world, my public key is N=" + Bob.N + " and e=" + Bob.e);
        System.out.println("Alice says: Hello world, my public key is N=" + Alice.N + " and e=" + Alice.e);
        System.out.println();

        System.out.println("----Message from Alice to Bob----");
        System.out.println("What should Alice say: ");
        String teststring = in.readLine();

        System.out.println("Alice says: " + teststring);

        int stringLength = teststring.toCharArray().length;
        int blockCount = getBlockCount(stringLength, blockSize);
        int numberLastChars = (int) stringLength % blockSize;

        ArrayList<String> stringer = divideIntoChunks(blockCount, blockSize, stringLength, numberLastChars, teststring);
        ArrayList<byte[]> messageToBobEncrypted = getEncryptedMessage(Alice, blockCount, Bob.e, Bob.N, stringer);
        String messageDecrypted = getDecryptedMessage(Bob, blockCount, Bob.d, Bob.N, messageToBobEncrypted);

        StringBuilder listString = new StringBuilder();
        messageToBobEncrypted.forEach(s -> listString.append(new String(s)));

        System.out.println("Charlie reads (encoded text): ");
        System.out.println(listString);
//        System.out.print(listString);

        System.out.println("Bob reads (decoded text): " + messageDecrypted);
        System.out.println();

        System.out.println("----Message from Bob to Alice----");
        System.out.println("What should Bob say: ");
        teststring = in.readLine();
        System.out.println("Bob says: " + teststring);


        stringLength = teststring.toCharArray().length;
        blockCount = getBlockCount(stringLength, blockSize);
        numberLastChars = (int) stringLength % blockSize;


        stringer = divideIntoChunks(blockCount, blockSize, stringLength, numberLastChars, teststring);
        ArrayList<byte[]> messageToAliceEncrypted = getEncryptedMessage(Bob, blockCount, Alice.e, Alice.N, stringer);
        messageDecrypted = getDecryptedMessage(Alice, blockCount, Alice.d, Alice.N, messageToAliceEncrypted);


        messageToAliceEncrypted.forEach(s -> listString.append(new String(s)).append(""));

        System.out.println("Charlie reads (encoded text): ");
        System.out.println(listString);
        System.out.println("Alice reads (decoded text): " + messageDecrypted);

        System.out.println("----Charlie started breaking RSA----");
        long startTime = System.currentTimeMillis();
        BigInteger BobDecKey = Charlie.breakRSA(Bob.e, Bob.N);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time to find Bob's Decryption key: " + totalTime / 1000 + " sec");
        System.out.println("Charlie found decryption key of Bob: " + BobDecKey);
        blockCount = messageToBobEncrypted.size();
        messageDecrypted = getDecryptedMessage(Charlie, blockCount, BobDecKey, Bob.N, messageToBobEncrypted);
        System.out.println("Charlie decrypted message send to Bob: " + messageDecrypted);
        startTime = System.currentTimeMillis();
        BigInteger AliceDecKey = Charlie.breakRSA(Alice.e, Alice.N);
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("Time to find Alice's Decryption key: " + totalTime / 1000 + " sec");
        System.out.println("Charlie found decryption key of Alice: " + AliceDecKey);
        blockCount = messageToAliceEncrypted.size();

        messageDecrypted = getDecryptedMessage(Charlie, blockCount, AliceDecKey, Alice.N, messageToAliceEncrypted);
        System.out.println("Charlie decrypted message send to Alice: " + messageDecrypted);


    }


    private static ArrayList<String> divideIntoChunks(int blockCount, int blockSize, int stringLength, int numberChars, String teststring) {
        ArrayList<String> stringer = new ArrayList<String>();
        for (int i = 1; i <= blockCount; i++) {
            if (numberChars > 0 && i == blockCount) {
                stringer.add(new String(Arrays.copyOfRange(teststring.toCharArray(), stringLength - numberChars, stringLength)));
            } else {
                int idx = (i - 1) * blockSize;
                stringer.add(new String(Arrays.copyOfRange(teststring.toCharArray(), idx, idx + blockSize)));
            }
        }
        return stringer;
    }

    private static ArrayList<byte[]> getEncryptedMessage(RSA user, int blockCount, BigInteger e, BigInteger N, ArrayList<String> stringer) {
        String messageToAliceEncrypted = "";
        ArrayList<byte[]> stringEncrypted = new ArrayList<byte[]>();
        for (int i = 0; i < blockCount; i++) {
            stringEncrypted.add(user.encrypt(stringer.get(i).getBytes(StandardCharsets.US_ASCII), e, N));
        }
        return stringEncrypted;
    }

    private static String getDecryptedMessage(RSA user, int blockCount, BigInteger d, BigInteger N, ArrayList<byte[]> messageEncrypted) {
        ArrayList<String> stringDecrypted = new ArrayList<String>();

        for (int i = 0; i < blockCount; i++) {

            stringDecrypted.add(new String(user.decrypt(messageEncrypted.get(i), d, N)));

        }
        String messageDecrypted = "";
        for (String s : stringDecrypted) {
            messageDecrypted += s + "";
        }
        return messageDecrypted;
    }

    private static int getBlockCount(int stringLength, int blockSize) {
        int blockCount = 0;
        if (stringLength % blockSize == 0)
            blockCount = (int) stringLength / blockSize;
        else
            blockCount = (int) stringLength / blockSize + 1;
        //System.out.println(blockCount);
        return blockCount;

    }

    //Encrypt message
    public byte[] encrypt(byte[] message, BigInteger e, BigInteger N) {

        return modPow(new BigInteger(message), e, N).toByteArray();

    }

    // Decrypt message
    public byte[] decrypt(byte[] message, BigInteger d, BigInteger N) {

        return modPow(new BigInteger(message), d, N).toByteArray();

    }

    public BigInteger factorize(BigInteger N) {
        BigInteger number = N.sqrt();

        if (number.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            number = number.subtract(BigInteger.ONE);
        }
        while (!N.mod(number).equals(BigInteger.ZERO)) {

            number = number.subtract(BigInteger.TWO);
        }
        return number;
    }

    public BigInteger breakRSA(BigInteger e, BigInteger N) {

        BigInteger p = factorize(N);
        BigInteger q = N.divide(p);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = ExtendedEuclidean.modInverse(e, phi);
        return d;
    }


}