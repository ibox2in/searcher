package com.eastbanc;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for searching numbers which occurs in file more than particular amount of times
 *
 * @version 1.0
 */
public final class Searcher {
    /**
     * Searches first number which occurs in file more than occurrenceCount times
     *
     * @param file path to file
     * @param occurrenceCount max occurrence count of number in file
     * @return integer which occurs at least occurrenceCount, null otherwise
     * @throws java.io.FileNotFoundException if file not exists
     * @throws java.io.IOException otherwise
     */
    public Integer find(String file, int occurrenceCount) throws IOException {
        Map<Integer, Integer> numbers = new HashMap<Integer, Integer>();
        Reader reader = new FileReader(file);
        try {
            char[] chars = new char[8192];
            StringBuilder builder = new StringBuilder();
            for(int length = 0; ; length = reader.read(chars)) {
                for(int i = 0; i < length || length == -1; i++) {
                    if(length != -1 && (Character.isDigit(chars[i]) || chars[i] == '-')) {
                        builder.append(chars[i]);
                    } else if(length != -1 && !Character.isWhitespace(chars[i])) {
                        throw new IllegalArgumentException();
                    } else if(builder.length() > 0) {
                        Integer number = Integer.parseInt(builder.toString());
                        Integer count = numbers.get(number);
                        count = count == null ? 1 : (count + 1);
                        if(count >= occurrenceCount) {
                            return number;
                        }
                        numbers.put(number, count);
                        builder.setLength(0);
                    }
                    if(length == -1) {
                        return null;
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Generates file of integers
     *
     * @param file path to file
     * @param count amount of numbers to generate
     * @throws java.io.FileNotFoundException if cannot create file
     */
    public void generate(String file, BigInteger count) throws FileNotFoundException {
        SecureRandom secureRandom = new SecureRandom();
        PrintWriter writer = new PrintWriter(file);
        for(BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
            writer.print(secureRandom.nextInt());
            writer.print(" ");
        }
        writer.close();
    }
}
