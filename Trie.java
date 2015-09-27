import java.util.TreeMap;
import java.util.Comparator;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Davit Javadian
 */
public class Trie {
    private Node root;
    private Comparator<Character> comp;

    /**
     * Initializes a new root Node for a Trie.
     */
    public Trie() {
        root = new Node();
        comp = null;
    }

    /**
     * Initializes a new root Node for a Trie with a comparator.
     * @param comp Comparator for the Trie.
     */
    public Trie(Comparator<Character> comp) {
        root = new Node(comp);
        this.comp = comp;
    }

    /**
     * Returns whether the String exists in the Trie, or whether a partial
     * match exists if the isFullWord parameter is false.
     * @param s String to be searched for in the Trie.
     * @param isFullWord True if isFullWord must be an exact match.
     * @return Boolean which is true if the word exists or if there is a
     * partial match if the isFullWord argument is false. Otherwise, returns false.
     * @throws IllegalArgumentException if the String is null or empty.
     */
    public boolean find(String s, boolean isFullWord) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        return find(root, s, isFullWord, 0);
    }

    /**
     * Helper method to find whether a word or prefix exists in a Trie.
     * @param n Current Node that must be checked for the appropriate character.
     * @param s String that is being searched for in the Trie.
     * @param isFullWord True if isFullWord must be an exact match.
     * @param d Current character of the String.
     * @return Boolean which indicates if the word or prefix (accordingly)
     * exists in a Trie.
     */
    private boolean find(Node n, String s, boolean isFullWord, int d) {
        if (d == s.length()) {
            if (isFullWord) {
                return n.exists;
            }
            return true;
        }
        char c = s.charAt(d);
        if (!n.links.containsKey(c)) {
            return false;
        }
        return find(n.links.get(c), s, isFullWord, d + 1);
    }

    /**
     * Inserts a String into the Trie.
     * @param s String which is to be inserted into the Trie.
     * @throws IllegalArgumentException if the String is null or empty.
     */
    public void insert(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        insert(root, s, 0);
    }

    /**
     * Helper method to insert a String into the Trie.
     * @param n Current Node which respresents the appropriate character
     * of the String to be added.
     * @param s String that is being inserted into the Trie.
     * @param d Current character of the String.
     */
    private void insert(Node n, String s, int d) {
        if (d == s.length()) {
            n.exists = true;
            return;
        }
        char c = s.charAt(d);
        if (!n.links.containsKey(c)) {
            if (comp != null) {
                n.links.put(c, new Node(comp));
            } else {
                n.links.put(c, new Node());
            }
        }
        insert(n.links.get(c), s, d + 1);
    }

    /**
     * Outputs the words in the Trie in order.
     */
    public void outputOrderedWords() {
        outputOrderedWords(root, "");
    }

    /**
     * Helper method to output the words in the Trie in order.
     * @param n Node to start outputting words from.
     * @param s String with characters built up to current Node.
     */
    public void outputOrderedWords(Node n, String s) {
        if (n.exists) {
            System.out.println(s);
        }
        for (Character c : n.links.keySet()) {
            outputOrderedWords(n.links.get(c), s + c);
        }
    }

    /**
     * Represents a Node in the Trie.
     */
    private class Node {
        boolean exists;
        TreeMap<Character, Node> links;

        /**
         * Initializes the links map for the node and sets exists to false.
         */
        public Node() {
            links = new TreeMap<Character, Node>();
            exists = false;
        }

        /**
         * Initializes the links map for the node with a comparator and sets exists to false.
         * @param comp Comparator for the links map.
         */
        public Node(Comparator<Character> comp) {
            links = new TreeMap<Character, Node>(comp);
            exists = false;
        }
    }
}
