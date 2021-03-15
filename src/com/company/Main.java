package com.company;

public class Main {

    public static void main(String[] args) {
        // create an instance of BookReader to fetch the texts to be compressed
        BookReader bookReader = new BookReader();

        //decompress compressed books and put them in resources/books directory

        Compressor cEng1 = new Compressor("pride_and_prejudice_eng");
        Compressor cEng2 = new Compressor("the_picture_of_dorian_gray_eng");
        Compressor cPrt1 = new Compressor("historia_da_grecia_prt");
        Compressor cPrt2 = new Compressor("lupe_prt");
        Compressor cFr1 = new Compressor("Leclaireur_fr");
        Compressor cFr2 = new Compressor("tolla_fr");
        Compressor cArtificial = new Compressor("artificial");
        Compressor cPseudoReal = new Compressor("pseudo_real");
        Compressor cReal = new Compressor("real");

        String eng1 = cEng1.decompressAndSaveAs("pride_and_prejudice", "pride_and_prejudice_eng", "books/pride_and_prejudice_eng_decompressed");
        String eng2 = cEng2.decompressAndSaveAs("dorian_gray", "the_picture_of_dorian_gray_eng", "books/the_picture_of_dorian_gray_eng_decompressed");
        String prt1 = cPrt1.decompressAndSaveAs("grecia", "historia_da_grecia_prt", "books/historia_da_grecia_prt_decompressed");
        String prt2 = cPrt2.decompressAndSaveAs("lupe", "lupe_prt", "books/lupe_prt_decompressed");
        String fr1 = cFr1.decompressAndSaveAs("leclaireur", "Leclaireur_fr", "books/leclaireur_fr_decompressed");
        String fr2 = cFr2.decompressAndSaveAs("tolla", "tolla_fr", "books/tolla_fr_decompressed");
        String artificial = cArtificial.decompressAndSaveAs("artificial", "artificial", "pseudo/pseudo_decompressed");
        String pseudoReal = cPseudoReal.decompressAndSaveAs("pseudo_real", "pseudo_real", "pseudo/pseudo_real_decompressed");
        String real = cReal.decompressAndSaveAs("real", "real", "pseudo/real_decompressed");

        String[] texts = {eng1, eng2, prt1, prt2, fr1, fr2, artificial, pseudoReal, real};
        String[] textsTitles = {"pride_and_prejudice_eng",
                "the_picture_of_dorian_gray_eng",
                "historia_da_grecia_prt",
                "lupe_prt", "Leclaireur_fr", "tolla_fr",
                "artificial", "real", "pseudo_real"};
        // now the texts can be used to create new trees and save the codes to use them to (de)compress txt files
        // examples:

        // create a map of char occurrences for all texts
//        Map<Character, Integer> mapEng1 = bookReader.countCharOccurrences(eng1);
//        Map<Character, Integer> mapReal = bookReader.countCharOccurrences(real);
        // create a huffman trees based on char occurrences
//        HuffmanTree htEng1 = new HuffmanTree(mapEng1);
//        HuffmanTree htReal = new HuffmanTree(mapReal);
        // save codes for chars from a given map
//        htEng1.saveTreeCodes("pride_and_prejudice_eng");
//        htReal.saveTreeCodes("real");


        // compress every book using different compressors (for statistics) (examples)
//        compressEveryBookUsingCompressor(cEng2, texts, textsTitles, "dorian_gray");
//        compressEveryBookUsingCompressor(cPseudoReal, texts, textsTitles, "pseudo_real");

        // compress a single book using any compressor (example) (saves in recources/compressed_texts)
        cEng1.compressText(eng2, "eng2");
    }

    /**
     * Compresses every String from given String array and saves all bin files in a given subdir in resources/compressed_texts
     *
     * @param c            compressor
     * @param books        strings to be compressed
     * @param bookTitles   titles of compressed .bin files
     * @param nameOfSubdir name of subdirectory in resources/compressed_texts where all the .bin files will be saved
     */
    private static void compressEveryBookUsingCompressor(Compressor c, String[] books, String[] bookTitles, String nameOfSubdir) {
        for (int i = 0; i < books.length; i++) {
            c.compressText(books[i], "/" + nameOfSubdir + "/" + bookTitles[i]);
        }
    }
}
