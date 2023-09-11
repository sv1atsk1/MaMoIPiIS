package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Choose the desired function: \nTable Route Permutation Cipher (without a key) - 1\n" +
                "Vertical Permutation Cipher (with a key) - 2\n" +
                "Brute-force Key Attack - 3\n");
        String key_or_not = reader.readLine();
        String encryptedText2 = "";
        switch (key_or_not) {
            case "1":
                System.out.println("Enter the text to encrypt:");
                String inputText = reader.readLine().replaceAll("\\s", "");
                String encryptedText = encoderWithoutKey(inputText);
                System.out.println("Encrypted text: " + encryptedText);
                break;

            case "2":
                System.out.println("Enter the text to encrypt:");
                String inputText2 = reader.readLine().replaceAll("\\s", "");
                System.out.println("Enter the encryption key:");
                String[] keyInput = reader.readLine().split(" ");
                int[] key = new int[keyInput.length];
                for (int i = 0; i < keyInput.length; i++) {
                    key[i] = Integer.parseInt(keyInput[i]);
                }
                encryptedText2 = encoderWithKey(inputText2, key);
                System.out.println("Encrypted text: " + encryptedText2);
                break;

            case "3":
                System.out.println("Enter the encrypted text:");
                String encryptedTextToDecrypt = reader.readLine();
                bruteForceAttack(encryptedTextToDecrypt);
                break;
        }
    }

    public static String encoderWithoutKey(String string_for_encoder) {
        int rows = (int) Math.ceil((double) string_for_encoder.length() / 5);

        char[][] table = new char[rows][5];

        int currentIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 5; j++) {
                if (currentIndex < string_for_encoder.length()) {
                    table[i][j] = string_for_encoder.charAt(currentIndex);
                    currentIndex++;
                } else {
                    table[i][j] = ' ';
                }
            }
        }

        System.out.println("\nTable:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }

        StringBuilder encodedString = new StringBuilder();
        for (int j = 4; j >= 0; j--) {
            for (int i = 0; i < rows; i++) {
                char currentChar = table[i][j];
                if(currentChar != ' ')
                    encodedString.append(table[i][j]);
            }
        }

        return encodedString.toString();
    }

    public static String encoderWithKey(String string_for_encoder, int[] key) {
        int columns = key.length;
        int rows = (int) Math.ceil((double) string_for_encoder.length() / columns);

        char[][] table = new char[rows][columns];

        int currentIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (currentIndex < string_for_encoder.length()) {
                    table[i][j] = string_for_encoder.charAt(currentIndex);
                    currentIndex++;
                } else {
                    table[i][j] = ' ';
                }
            }
        }

        System.out.println("\nTable:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }

        StringBuilder encodedString = new StringBuilder();
        for (int i = 1; i <= key.length; i++) {
            int columnIndex = findIndex(key, i);
            for (int row = 0; row < table.length; row++) {
                String symbol = String.valueOf(table[row][columnIndex]);
                encodedString.append(symbol);
            }
        }

        return encodedString.toString().replaceAll("\\s", "");
    }

    public static int findIndex(int[] key, int value) {
        for (int i = 0; i < key.length; i++) {
            if (key[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static void bruteForceAttack(String encryptedText) {
        char[] encryptedChars = encryptedText.toCharArray();
        int messageLength = encryptedChars.length;

        for (int keyLength = 1; keyLength <= messageLength; keyLength++) {
            int[] key = generateKey(keyLength);

            do {
                String decryptedText = decoderWithKey(encryptedChars, key);
                System.out.println("Attempt with key " + Arrays.toString(key) + ": " + decryptedText);
            } while (nextPermutation(key));
        }
        System.out.println("Attack completed.");
    }

    public static int[] generateKey(int length) {
        int[] key = new int[length];
        for (int i = 0; i < length; i++) {
            key[i] = i + 1;
        }
        return key;
    }

    public static String decoderWithKey(char[] encryptedChars, int[] key) {
        int columns = key.length;
        int rows = (int) Math.ceil((double) encryptedChars.length / columns);

        char[][] table = new char[rows][columns];

        int currentIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (currentIndex < encryptedChars.length) {
                    table[i][j] = encryptedChars[currentIndex];
                    currentIndex++;
                } else {
                    table[i][j] = ' ';
                }
            }
        }

        StringBuilder decodedString = new StringBuilder();
        for (int i = 0; i < columns; i++) {
            int columnIndex = findIndex(key, i + 1);
            for (int row = 0; row < rows; row++) {
                char symbol = table[row][columnIndex];
                decodedString.append(symbol);
            }
        }

        return decodedString.toString().replaceAll("\\s", "");
    }

    public static boolean nextPermutation(int[] array) {
        int i = array.length - 1;
        while (i > 0 && array[i - 1] >= array[i]) {
            i--;
        }

        if (i <= 0) {
            return false;
        }

        int j = array.length - 1;
        while (array[j] <= array[i - 1]) {
            j--;
        }

        int temp = array[i - 1];
        array[i - 1] = array[j];
        array[j] = temp;

        j = array.length - 1;
        while (i < j) {
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
        return true;
    }
}