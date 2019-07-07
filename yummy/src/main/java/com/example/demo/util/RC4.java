package com.example.demo.util;

class RC4 {
    private final byte[] S = new byte[256];

    RC4(final byte[] key) {
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            int keyLen = key.length;
            byte[] t = new byte[256];
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                t[i] = key[i % keyLen];
            }
            int j = 0;
            byte tmp;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + t[i]) & 0xFF;
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
            }
        }
    }

    byte[] encrypt(final byte[] plaintext) {
        final byte[] cipherText = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            cipherText[counter] = (byte) (plaintext[counter] ^ k);
        }
        return cipherText;
    }
}
