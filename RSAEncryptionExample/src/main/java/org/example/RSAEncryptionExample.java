package org.example;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAEncryptionExample {
    private static final int KEY_SIZE = 1024;

    public static void main(String[] args) {

        KeyPair keyPair = generateKeys();


        saveKeyToFile("public_key.txt", keyPair.getPublicKey());
        saveKeyToFile("modulus.txt", keyPair.getModulus());


        String message = "Hello, RSA Encryption!";
        BigInteger encryptedMessage = encrypt(message, keyPair.getPublicKey(), keyPair.getModulus());
        saveEncryptedMessageToFile("encrypted_message.txt", encryptedMessage);

        BigInteger receivedMessage = readEncryptedMessageFromFile("encrypted_message.txt");
        String decryptedMessage = decrypt(receivedMessage, keyPair.getPrivateKey(), keyPair.getModulus());
        System.out.println("Decrypted Message: " + decryptedMessage);
    }

    private static KeyPair generateKeys() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(KEY_SIZE, random);
        BigInteger q = BigInteger.probablePrime(KEY_SIZE, random);
        BigInteger modulus = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger publicKey, privateKey;
        do {
            publicKey = new BigInteger(KEY_SIZE, random);
        } while (publicKey.compareTo(BigInteger.ONE) <= 0 || publicKey.compareTo(phi) >= 0 || !publicKey.gcd(phi).equals(BigInteger.ONE));

        privateKey = publicKey.modInverse(phi);

        return new KeyPair(publicKey, privateKey, modulus);
    }

    private static void saveKeyToFile(String fileName, BigInteger key) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(key.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveEncryptedMessageToFile(String fileName, BigInteger message) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BigInteger readEncryptedMessageFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (BigInteger) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BigInteger encrypt(String message, BigInteger publicKey, BigInteger modulus) {
        BigInteger plaintext = new BigInteger(message.getBytes());
        return plaintext.modPow(publicKey, modulus);
    }

    private static String decrypt(BigInteger ciphertext, BigInteger privateKey, BigInteger modulus) {
        BigInteger decryptedMessage = ciphertext.modPow(privateKey, modulus);
        return new String(decryptedMessage.toByteArray());
    }

    private static class KeyPair {
        private final BigInteger publicKey;
        private final BigInteger privateKey;
        private final BigInteger modulus;

        public KeyPair(BigInteger publicKey, BigInteger privateKey, BigInteger modulus) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
            this.modulus = modulus;
        }

        public BigInteger getPublicKey() {
            return publicKey;
        }

        public BigInteger getPrivateKey() {
            return privateKey;
        }

        public BigInteger getModulus() {
            return modulus;
        }
    }
}