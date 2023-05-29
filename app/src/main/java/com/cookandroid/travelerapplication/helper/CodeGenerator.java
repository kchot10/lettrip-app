package com.cookandroid.travelerapplication.helper;


import java.util.Random;

public class CodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    public String generateCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            codeBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}