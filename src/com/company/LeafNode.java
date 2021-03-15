package com.company;

/**
 * A class representing a leaf node in a frequency tree.
 *
 * It extends the [Node] class but it also contains a character and its code.
 */
public class LeafNode extends Node {
    private char character;
    private String code;

    /**
     * Creates a node object.
     * @param character a character to be decoded
     * @param count a number of appearances of the char in the text
     */
    public LeafNode(char character, int count) {
        super(count);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("<%s, %o>", character, super.getCount());
    }
}
