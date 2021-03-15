package com.company;

/**
 * A class representing a node in a frequency tree.
 *
 * It contains a count (sum of children's frequencies), left and right child.
 * The right child has to have a higher occurrence than the left one.
 */
public class Node {
    private int count;
    private Node leftChild;
    private Node rightChild;

    /**
     * Creates an instance of a Node class.
     * @param leftChild a node with a lower frequency
     * @param rightChild a node with a higher frequency
     * Sets count equal to a sum of children's frequencies.
     */
    public Node(Node leftChild, Node rightChild) {
        this.count = leftChild.count + rightChild.count;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Creates an instance of a node class for a Leaf node.
     * @param frequency frequency of a character appearing
     */
    public Node(int frequency){
        this.count = frequency;
        this.leftChild = null;
        this.rightChild = null;
    }

    public int getCount() {
        return count;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    @Override
    public String toString() {
        return "Node{" +
                "count=" + count +
                ", leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                '}';
    }
}
