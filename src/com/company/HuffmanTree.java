package com.company;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class HuffmanTree {
    private Node root;
    private List<Node> nodes = new ArrayList<>();
    private Map<Character, String> codes = new HashMap<>();

    /**
     * Creates a huffman tree based on a map of characters and their frequencies.
     *
     * @param frequencyMap map of characters and their frequencies sorted from the least to the most frequent
     */
    public HuffmanTree(Map<Character, Integer> frequencyMap) {
        if (frequencyMap.keySet().size() < 3) {
            return;
        }
        // available to connect, start off with leaf nodes
        List<Node> topsOfSubtrees = new ArrayList<>();
        // create all leaf nodes
        for (Character c : frequencyMap.keySet()) {
            LeafNode temp = new LeafNode(c, frequencyMap.get(c));
            nodes.add(temp);
            topsOfSubtrees.add(temp);
        }
        // so we now have a sorted list of tops of subtrees
        Node leastFrequentLeft = topsOfSubtrees.get(0);
        Node leastFrequentRight = topsOfSubtrees.get(1);

        while (topsOfSubtrees.size() > 2) {
            Node newTop = new Node(leastFrequentLeft, leastFrequentRight);
            topsOfSubtrees.remove(leastFrequentRight);
            topsOfSubtrees.remove(leastFrequentLeft);
            insertIntoSortedList(topsOfSubtrees, newTop);
            leastFrequentLeft = topsOfSubtrees.get(0);
            leastFrequentRight = topsOfSubtrees.get(1);
        }
        this.root = new Node(leastFrequentLeft, leastFrequentRight);
        setCodes(root, "");

    }

    /**
     * Inserts a node into a sorted list of nodes so that the list remains sorted.
     *
     * @param list list of nodes
     * @param node node to be inserted
     */
    private void insertIntoSortedList(List<Node> list, Node node) {
        if (list.size() == 0) {
            list.add(node);
            return;
        }
        // returns position of an object in a sorted list
        int index = Collections.binarySearch(list, node, Comparator.comparing(Node::getCount));
        // If key is not present, the value of index will be "(-(insertion point) - 1)".
        // The insertion point is defined as the point at which the key
        // would be inserted into the list.
        if (index < 0) {
            index = -index - 1;
        }
        list.add(index, node);
    }

    /**
     * A method recursively traverses through the instance of huffman tree and sets the codes to its leaf nodes.
     * <p>
     * The code is a string containing ones and zeroes depending on node's position in the tree.
     * If the node is a left child of its parent node, the method adds 0, right - 1.
     * Chars that occur more often, will have shorter codes since they are not "deep"
     * in the structure of the huffman tree.
     *
     * @param currentRoot root of the tree
     * @param code        start with "", the method adds ones and zeroes when calling itself
     */
    private void setCodes(Node currentRoot, String code) {
        if (currentRoot == null)
            return;
        if (currentRoot instanceof LeafNode) {
            ((LeafNode) currentRoot).setCode(code);
            this.codes.put(((LeafNode) currentRoot).getCharacter(), code);
            return;
        }
        setCodes(currentRoot.getLeftChild(), code + 0);
        setCodes(currentRoot.getRightChild(), code + 1);
    }

    public void saveTreeCodes(String fileName) {
        // convert a map containing characters and corresponding codes to JSON
        String json = "";
        for (Character key :
                codes.keySet()) {
            if (!Character.isWhitespace(key)) {
                json += String.format("\"%s\" : \"%s\"\n", key, codes.get(key));
            }
            // handling whitespace chars
            else if(key == '\n'){
                // \n prints as "new line" instead of enter
                json += String.format("\"%s\" : \"%s\"\n", "new line", codes.get(key));
            } else if (key == ' '){
                json += String.format("\"%s\" : \"%s\"\n", "space", codes.get(key));
            }else if (key == '\t'){
                json += String.format("\"%s\" : \"%s\"\n", "tab", codes.get(key));
            }else if (key == '\r'){
                json += String.format("\"%s\" : \"%s\"\n", "carriage return", codes.get(key));
            }else if (key == '\f'){
                json += String.format("\"%s\" : \"%s\"\n", "formfeed", codes.get(key));
            }
        }
        try {
            File tree = new File(".resources/trees/" + fileName + ".txt");
            if (tree.createNewFile()) {
                System.out.println("tree saved: " + tree.getName());
            } else {
                System.out.println("Tree already exists.");
                System.out.println("Tree updated.");
            }
            FileWriter myWriter = new FileWriter(tree);
            myWriter.write(json);
            myWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.out.println(json);
        }
    }
}
