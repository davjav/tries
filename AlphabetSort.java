import java.util.Scanner;
import java.util.HashMap;
import java.util.Comparator;

/**
 * Implements AlphabetSort, which sorts words using the given alphabet
 * in M*N time, where M is the length of the longest word and N is the
 * number of words.
 * @author Davit Javadian
 */
public class AlphabetSort {
    Trie t;

    /**
     * Constructs a new AlphabetSort object to do the necessary procesing.
     * @param in Scanner object recieved in main method.
     */
    private AlphabetSort(Scanner in) {
        if (!in.hasNextLine()) {
            throw new IllegalArgumentException("No alphabet is given.");
        }
        AlphabetComparator ac = new AlphabetComparator(in.nextLine());
        if (!in.hasNextLine()) {
            throw new IllegalArgumentException("No words are given.");
        }
        t = new Trie(ac);
        while (in.hasNextLine()) {
            try {
                t.insert(in.nextLine());
            } catch (IllegalArgumentException e) {
                String error = "invalid word";
            }
        }
    }

    /**
     * Outputs the words in the dictionary in order.
     */
    private void outputOrderedWords() {
        t.outputOrderedWords();
    }

    /**
     * Takes in input from stdin. The first line should be
     * an alphabet with ascending order. The remaining lines
     * are the words to be sorted accordingly. If a word contains
     * a character not in the alphabet, the word is ignored.
     * Finally, prints words in sorted order.
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        AlphabetSort as = new AlphabetSort(in);
        as.outputOrderedWords();
    }

    /**
     * Helper class used to sort based on a given alphabet.
     */
    private class AlphabetComparator implements Comparator<Character> {
        HashMap<Character, Integer> ranks;

        /**
         * Constructs the necessary data structures for the comparator.
         * @param alphabet The String containing the alphabet in order.
         */
        public AlphabetComparator(String alphabet) {
            ranks = new HashMap<Character, Integer>();
            char[] alphCA = alphabet.toCharArray();
            for (int i = 0; i < alphCA.length; i++) {
                if (ranks.containsKey(alphCA[i])) {
                    throw new IllegalArgumentException("A letter appears multiple times "
                        + "in the alphabet.");
                }
                ranks.put(alphCA[i], i);
            }
        }

        /**
         * Compares two characters in the given alphabet.
         * @param c1 First character to compare.
         * @param c2 Second character to compare.
         * @return An integer > 0 if c1 > c2, 0 if c1 = c2,
         * or < 0 if c1 < c2 in the given alphabet.
         * @throws IllegalArgumentException if either key is not in the alphabet.
         */
        public int compare(Character c1, Character c2) {
            if (!ranks.containsKey(c1) || !ranks.containsKey(c2)) {
                throw new IllegalArgumentException();
            }
            return ranks.get(c1) - ranks.get(c2);
        }
    }
}
