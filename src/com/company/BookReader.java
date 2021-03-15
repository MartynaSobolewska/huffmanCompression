package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * class containing methods allowing to read chosen books from the resources directory
 */
public class BookReader {
    /**
     * Fetches a string containing the contents of the txt file in resources.
     * @param fileName name of the txt file in the resources folder (containing .txt)
     *                 if it is in a subfolder - "subfolder/name.txt"
     * @return a cleaned String with book's content
     */
    public String getTextFrom(String fileName){
        try {
            File book = new File("./resources/"+fileName);
            Scanner bookScanner = new Scanner(book);
            StringBuilder text = new StringBuilder();
            while (bookScanner.hasNextLine()) {
                text.append(bookScanner.nextLine().strip()).append("\n");
            }
            bookScanner.close();
            return text.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An file not found!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Counts number of occurrences for each char in the text
     * @param text text that we want to analyze
     * @return a tree map where each character has a corresponding number - times it occurred in the text.
     * the map is sorted so that the most occurring char will be last
     */
    public Map<Character, Integer> countCharOccurrences(String text){
        Map<Character, Integer> chars = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            if (chars.containsKey(c)){
                Integer count = chars.get(c);
                count += 1;
                chars.put(c, count);
            }else
                chars.put(c, 1);
        }
        // using stream on chars map entry set to sort it by values
        Map<Character, Integer> sorted = chars.entrySet().stream()
                // sort the map entries according to the frequencies
                .sorted(Map.Entry.comparingByValue())
                // turn the sorted set of entries back into a map
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (prevVal, sortedVal) -> prevVal, LinkedHashMap::new));
        return sorted;
    }
}
