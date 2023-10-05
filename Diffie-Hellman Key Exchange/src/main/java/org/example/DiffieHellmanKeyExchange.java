package org.example;


import java.util.Random;

public class DiffieHellmanKeyExchange {
    public static void main(String[] args) {
        int P = 7237;
        int g = findPrimitiveElement(P);

        int a = generateSecretKey(P);
        int b = generateSecretKey(P);

        System.out.println("P: " + P);
        System.out.println("Primitive Element (g): " + g);
        System.out.println("Alice's Secret Key (a): " + a);
        System.out.println("Bob's Secret Key (b): " + b);


        int A = modPow(g, a, P); // Alice вычисляет A
        System.out.println("Alice computes A: " + A);


        int B = modPow(g, b, P); // Bob вычисляет B
        System.out.println("Bob computes B: " + B);


        System.out.println("Alice and Bob exchange values...");


        int sharedSecretAlice = modPow(B, a, P);
        System.out.println("Alice computes shared secret: " + sharedSecretAlice);


        int sharedSecretBob = modPow(A, b, P);
        System.out.println("Bob computes shared secret: " + sharedSecretBob);


        if (sharedSecretAlice == sharedSecretBob) {
            System.out.println("Alice and Bob have the same shared secret!");
        } else {
            System.out.println("Error: Alice and Bob have different shared secrets.");
        }
    }


    private static int findPrimitiveElement(int P) {
        for (int g = 2; g < P; g++) {
            boolean isPrimitive = true;
            for (int i = 1; i <= P - 2; i++) {
                int power = modPow(g, i, P);
                if (power == 1) {
                    isPrimitive = false;
                    break;
                }
            }
            if (isPrimitive) {
                return g;
            }
        }
        return -1;
    }


    private static int generateSecretKey(int P) {
        Random rand = new Random();
        return rand.nextInt(P - 2) + 1;
    }

    private static int modPow(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            base = (base * base) % modulus;
            exponent = exponent / 2;
        }
        return result;
    }
}