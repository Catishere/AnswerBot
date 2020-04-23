package com.cat.bots.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
public class NumberParser {

    private long lastExponent;
    private long finalNumber;
    private long tempNumber;

    public NumberParser() {
        this.lastExponent = 0;
        this.finalNumber = 0;
        this.tempNumber = 0;
    }


    private List<String> numbers20 = Arrays.asList(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten",
            "eleven",
            "twelve",
            "thirteen",
            "fourteen",
            "fifteen",
            "sixteen",
            "seventeen",
            "eighteen",
            "nineteen"
    );

    private List<String> numbers100 = Arrays.asList(
            "twenty",
            "thirty",
            "forty",
            "fifty",
            "sixty",
            "seventy",
            "eighty",
            "ninety"
    );

    private List<String> exponent = Arrays.asList(
            "hundred",
            "thousand",
            "million",
            "billion",
            "trillion",
            "quadrillion",
            "quintillion",
            "sextillion",
            "septillion",
            "octillion",
            "nonillion",
            "decillion"
    );

    private long getExponent(String word) {
        int index = exponent.indexOf(word);
        if (index == 0)
            return 2;
        else
            return index * 3;
    }

    private long getTenPower(long power) {
        return (long) Math.pow(10, power);
    }

    private void resolveExponents(long currentExponent) {
        if (lastExponent - currentExponent > 1) {
            finalNumber += tempNumber;
            tempNumber = 0;
        }
        lastExponent = currentExponent;
    }

    private long getNumberFromWords(List<String> wordList) {
        Iterator<String> iter = wordList.iterator();

        while (iter.hasNext()) {
            String word = iter.next();
            if (numbers20.contains(word)) {
                resolveExponents(1);
                tempNumber += numbers20.indexOf(word) + 1;
            } else if (numbers100.contains(word)) {
                resolveExponents(1);
                tempNumber += (numbers100.indexOf(word) + 2) * 10;
            } else if (exponent.contains(word)) {
                resolveExponents(getExponent(word));
                tempNumber *= getTenPower(lastExponent);
            } else {
                break;
            }
            iter.remove();
        }
        return finalNumber + tempNumber;
    }

    public String getNumberFromWordsAsString(String words) {
        List<String> wordList = new ArrayList<>(Arrays.asList(words.replace(" and "," ").split("[ \\-]")));
        long number = getNumberFromWords(wordList);
        if (number == 0)
            return String.join(" ", wordList);
        return number + " " + String.join(" ", wordList);
    }
}
