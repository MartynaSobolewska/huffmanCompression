package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A class containing compressing and decompressing methods for a given encoding based on a huffman tree.
 */
public class Compressor {
    private Map<Character, String> codes = new HashMap<>();

    /**
     * Constructor for compressor, automatically fetches the default encoding for a given
     * @param nameOfTxtFileWithCodes name of the txt file in trees directory containing the encoding
     */
    public Compressor(String nameOfTxtFileWithCodes) {
        getCodesFromFile(nameOfTxtFileWithCodes);
    }

    /**
     * Fetches a map containing a character and its corresponding encoding from a txt file in trees directory
     * @param nameOfTxtFileWithCodes name of the txt file in trees directory (without .txt)
     * @return a map containing a characters as a keys and its corresponding encoding as values
     */
    public Map<Character, String> getCodesFromFile(String nameOfTxtFileWithCodes){
        try {
            File myObj = new File("./resources/trees/" + nameOfTxtFileWithCodes + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Map.Entry<Character, String> entry = getEntryFromALine(data);
                codes.put(entry.getKey(), entry.getValue());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File with codes not found.");
        }
        return null;
    }

    /**
     * Gets the information from a line of file containing encoding generated by the [HuffmanTree.java] class
     * puts the information in a form of a hash map where key is the encoded char and value is the code
     * @param line A line from file with huffman encoding
     * @return A hash map where key is the encoded char and value is the code
     */
    private Map.Entry<Character, String> getEntryFromALine(String line){
        try {
            String[] parts = line.split(" : ");
            String left = parts[0].trim().replaceAll("\"", "");
            Character c;
            if (left.trim().equals("")){
                c = '\"';
            } else if (left.trim().equals("new line")){
                c = '\n';
            } else if (left.trim().equals("space")){
                c = ' ';
            } else if (left.trim().equals("tab")){
                c = '\t';
            } else if (left.trim().equals("carriage return")){
                c = '\r';
            } else if (left.trim().equals("formfeed")){
                c = '\f';
            } else if (left.length() == 1){
                c = left.charAt(0);
            }else {
                System.out.printf("Element %s is not a recognisable char!\n", left);
                return null;
            }
            String code = parts[1].trim().replaceAll("\"", "");
            return new java.util.AbstractMap.SimpleEntry<Character,String>(c, code);


        }catch (Exception e){
            System.out.println("File not well formatted, " +
                    "compression/decompression impossible since codes could not be fetched.");
        }
        return null;
    }

    /**
     * Saves a compressed version of a given text as a .bin file after checking if it already exists.
     * @param text text to compress using the specified map
     * @param nameOfFile name of the file to be saved in ./resources/compressed_texts/ without the extension
     */
    public void compressText(String text, String nameOfFile){
        StringBuilder stringBuilder = new StringBuilder("");

        // convert text to 0s and 1s
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            if (codes.containsKey(c)){
                stringBuilder.append(codes.get(c));
            }
        }
        String textInCode = stringBuilder.toString();
        // get byte array
        byte[] bytes = getBinary(textInCode);
        boolean write = true;
        // checks if a file like that already exists and ask if it should be overwritten
        if (new File("./resources/compressed_texts/" + nameOfFile + ".bin").exists()){
            boolean rightResponse = false;
            Scanner sc = new Scanner(System.in);
            while (!rightResponse){
                System.out.println("A file " + nameOfFile + ".bin already exists.\n" +
                        "Do you wish to overwrite it? (y/n)");
                String yn = sc.next();
                if (yn.trim().toLowerCase().equals("y")){
                    rightResponse = true;
                    //write stays true
                }else if (yn.trim().toLowerCase().equals("n")){
                    rightResponse = true;
                    write = false;
                    System.out.println("Not overwriting.");
                }
            }
        }
        if (write){
            // save byte array to file
            try {
                OutputStream outputStream = new FileOutputStream("./resources/compressed_texts/" + nameOfFile + ".bin");
                outputStream.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets a byte representation of an encoded string of 0s and 1s.
     * @param s encoded string (only containing 0s and 1
     * @return a byte array containing converted bytes
     */
    private byte[] getBinary(String s) {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() % 8 != 0) {
            sBuilder.append('0');
        }
        s = sBuilder.toString();

        byte[] data = new byte[s.length() / 8];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            }
        }
        return data;
    }

    /**
     * decompresses a file from ./resources/compressed_texts + subdir and saves it in ./resources/
     * @param subdir subdirectory of resources file where the compressed file is
     * @param compressedFileName name of the compressed file (without .bin)
     * @param decompressedFileName name that will be given to the decompressed file (without .txt)
     * @return a text of the decompressed file
     */
    public String decompressAndSaveAs(String subdir, String compressedFileName, String decompressedFileName) {
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get("./resources/compressed_texts/"+
                    subdir + "/" + compressedFileName + ".bin"));
            String encoded = GetString(allBytes);

            boolean charFound = false;
            StringBuilder code = new StringBuilder();

            //reverse codes map so that codes are keys and chars are values
            Map<String, Character> reversedCodes = new HashMap<>();
            for(Map.Entry<Character, String> entry : codes.entrySet()){
                reversedCodes.put(entry.getValue(), entry.getKey());
            }

            StringBuilder decodedText = new StringBuilder("");
            String[] keyset = reversedCodes.keySet().toArray(new String[reversedCodes.size()]);
            int maxKeyLength = 0;
            for (String key :
                    keyset) {
                if (key.length() > maxKeyLength){
                    maxKeyLength = key.length();
                }
            }


            for (int i = 0; i < encoded.length(); i++) {
                char bin = encoded.charAt(i);
                code.append(bin);
                if (reversedCodes.containsKey(code.toString())){
                    // match for char found
                    decodedText.append(reversedCodes.get(code.toString()));
                    // wipe the code, start looking for next char value
                    code = new StringBuilder();
                }
                // if no decoding in given tree
                else if (code.toString().length() > maxKeyLength){
                    decodedText.append("?");
                }
            }
            decodedText.deleteCharAt(decodedText.length()-1);
            boolean write = true;
            // check if a file like that exists and ask for permission for overriding
            if (new File("./resources/" + subdir + "/" + decompressedFileName + ".txt").exists()){
                boolean rightResponse = false;
                Scanner sc = new Scanner(System.in);
                while (!rightResponse){
                    System.out.println("A file " + decompressedFileName + ".txt already exists.\n" +
                            "Do you wish to overwrite it? (y/n)");
                    String yn = sc.next();
                    if (yn.trim().toLowerCase().equals("y")){
                        rightResponse = true;
                        //write stays true
                    }else if (yn.trim().toLowerCase().equals("n")){
                        rightResponse = true;
                        write = false;
                        System.out.println("Not overwriting.");
                    }
                }
            }
            // if can write, save file as txt
            try {
                File decompressed = new File("./resources/" + decompressedFileName + ".txt");
                decompressed.createNewFile();
                FileWriter myWriter = new FileWriter(decompressed);
                myWriter.write(decodedText.toString());
                myWriter.close();
                return decodedText.toString();
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Converts an array of bytes back to String
     * @param bytes an array of bytes (0s and 1s)
     * @return String containing 0s and 1s that can be converted using the huffman tree codes
     */
    static String GetString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }
}