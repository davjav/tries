import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Collections;

/**
 * Ternary Search Trie implementation.
 * @author Davit Javadian
 */
public class TST {
    private Node root;

    /**
     * Returns the top k matching terms (in descending order of weight) as an ArrayList.
     * If there are less than k matches, return all the matching terms.
     * @param prefix The prefix String of the matching terms to return.
     * @param k Number of top matching terms to return as an int.
     * @return Top k matching terms as a String (in descending order of weight) in an ArrayList.
     * @throws IllegalArgumentException if k is non-positive.
     */
    public ArrayList<String> topMatches(String prefix, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException();
        }
        PriorityQueue<Node> wordNodes = new PriorityQueue<Node>(k, new Node());
        PriorityQueue<Node> nodes = new PriorityQueue<Node>();
        HashMap<Node, String> strings = new HashMap<Node, String>();
        Node start = findNode(root, prefix, 0);
        if (start == null) {
            return new ArrayList<String>();
        }
        strings.put(start, prefix);
        if (start.val != null) {
            wordNodes.add(start);
        }
        if (prefix.equals("")) {
            strings.put(start, "" + start.c);
            nodes.add(start);
        } else {
            if (start.mid != null) {
                nodes.add(start.mid);
                strings.put(start.mid, prefix + start.mid.c);
                if (start.mid.val != null) {
                    if (wordNodes.size() == k && start.mid.val > wordNodes.peek().val) {
                        wordNodes.poll();
                        wordNodes.add(start.mid);
                    } else if (wordNodes.size() < k) {
                        wordNodes.add(start.mid);
                    }
                }
            }
        }

        while (nodes.size() != 0) {
            Node current = nodes.poll();
            String currStr = strings.get(current);

            if (current.mid != null) {
                strings.put(current.mid, currStr + current.mid.c);
                if (current.mid.val != null) {
                    if (wordNodes.size() == k && current.mid.val > wordNodes.peek().val) {
                        wordNodes.poll();
                        wordNodes.add(current.mid);
                    } else if (wordNodes.size() < k) {
                        wordNodes.add(current.mid);
                    }
                }
                if (wordNodes.size() < k || current.mid.max > wordNodes.peek().val) {
                    nodes.add(current.mid);
                }
            }
            if (current.left != null) {
                StringBuilder left = new StringBuilder(currStr);
                left.setCharAt(left.length() - 1, current.left.c);
                strings.put(current.left, left.toString());
                if (current.left.val != null) {
                    if (wordNodes.size() == k && current.left.val > wordNodes.peek().val) {
                        wordNodes.poll();
                        wordNodes.add(current.left);
                    } else if (wordNodes.size() < k) {
                        wordNodes.add(current.left);
                    }
                }
                if (wordNodes.size() < k || current.left.max > wordNodes.peek().val) {
                    nodes.add(current.left);
                }
            }
            if (current.right != null) {
                StringBuilder right = new StringBuilder(currStr);
                right.setCharAt(right.length() - 1, current.right.c);
                strings.put(current.right, right.toString());
                if (current.right.val != null) {
                    if (wordNodes.size() == k && current.right.val > wordNodes.peek().val) {
                        wordNodes.poll();
                        wordNodes.add(current.right);
                    } else if (wordNodes.size() < k) {
                        wordNodes.add(current.right);
                    }
                }
                if (wordNodes.size() < k || current.right.max > wordNodes.peek().val) {
                    nodes.add(current.right);
                }
            }
        }

        ArrayList<String> result = new ArrayList<String>();
        while (wordNodes.size() != 0) {
            Node removed = wordNodes.poll();
            String nodeString = strings.get(removed);
            result.add(nodeString);
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Finds the Node corresponding to the given String in the TST.
     * @param n Current Node in the finding process.
     * @param s The String to find the Node of in the TST.
     * @param d Current character index of the String.
     * @return Node The Node corresponding to the given String
     * if it exists, otherwise null.
     */
    private Node findNode(Node n, String s, int d) {
        if (n == null || s == null || s.equals("")) {
            return n;
        }
        char c = s.charAt(d);
        if (c < n.c) {
            return findNode(n.left, s, d);
        } else if (c > n.c) {
            return findNode(n.right, s, d);
        } else if (d < s.length() - 1) {
            return findNode(n.mid, s, d + 1);
        }
        return n;
    }

    /**
     * Returns the value of the corresponding String in the TST.
     * @param s The String to find the value of in the TST.
     * @return The weight of String, or 0.0 if it does not exist
     * in the TST.
     */
    public double get(String s) {
        if (s == null || s.equals("")) {
            return 0.0;
        }
        return get(root, s, 0);
    }

    /**
     * Helper method to find the value of a String in the TST.
     * @param n Current Node in the finding process.
     * @param s The String to find the value of in the TST.
     * @param d Current character index of the String.
     * @return The weight of String, or 0.0 if it does not exist
     * in the TST.
     */
    private double get(Node n, String s, int d) {
        if (n == null) {
            return 0.0;
        }
        char c = s.charAt(d);
        if (c < n.c) {
            return get(n.left, s, d);
        } else if (c > n.c) {
            return get(n.right, s, d);
        } else if (d < s.length() - 1) {
            return get(n.mid, s, d + 1);
        }
        if (n.val == null) {
            return 0.0;
        }
        return n.val;
    }

    /**
     * Inserts a String and its corresponding weight into the TST.
     * @param s String which is to be inserted into the TST.
     * @param w The weight of the String to be inserted, as a double.
     * @throws IllegalArgumentException if the String is null or empty,
     * the weight is less than or equal to 0, or the TST already contains
     * the given String.
     */
    public void insert(String s, double w) {
        if (s == null || s.equals("") || w <= 0.0 || contains(s)) {
            throw new IllegalArgumentException();
        }
        root = insert(root, s, w, 0);
    }

    /**
     * Helper method to insert a String into the TST.
     * @param n Current Node in the adding process.
     * @param s String that is being inserted into the TST.
     * @param w The weight of the String to be inserted, as a double.
     * @param d Current character index of the String.
     * @return Node corresponding to updated TST.
     */
    private Node insert(Node n, String s, double w, int d) {
        char c = s.charAt(d);
        if (n == null) {
            n = new Node();
            n.c = c;
            n.max = w;
        }
        if (w > n.max) {
            n.max = w;
        }

        if (c < n.c) {
            n.left = insert(n.left, s, w, d);
        } else if (c > n.c) {
            n.right = insert(n.right, s, w, d);
        } else if (d < s.length() - 1) {
            n.mid = insert(n.mid, s, w, d + 1);
        } else {
            n.val = w;
        }
        return n;
    }

    /**
     * Returns a boolean whether the TST contians the given String.
     * @param s String to be checked if contained in TST.
     * @return True if TST contains the String, false otherwise.
     */
    public boolean contains(String s) {
        if (s == null || s.equals("")) {
            return false;
        }
        return contains(root, s, 0);
    }

    /**
     * Helper method to check if the TST contains a String.
     * @param n Current Node in the checking process.
     * @param s String to be checked if contained in TST.
     * @param d Current character index of the String.
     * @return True if TST contains the String, false otherwise.
     */
    private boolean contains(Node n, String s, int d) {
        if (n == null) {
            return false;
        }
        char c = s.charAt(d);
        if (c < n.c) {
            return contains(n.left, s, d);
        } else if (c > n.c) {
            return contains(n.right, s, d);
        } else if (d < s.length() - 1) {
            return contains(n.mid, s, d + 1);
        }
        return n.val != null;
    }

    /**
     * Represents a Node in the TST.
     */
    private class Node implements Comparator<Node>, Comparable<Node> {
        private char c;
        private Node left, mid, right;
        private Double val, max;

        /**
         * Implements Comparable functionality for the Node.
         * Compares the max of the Nodes in descending order.
         * @param other Node to compare the current Node to.
         * @return A negative int if other Node is null or
         * its max is smaller than the current Node, zero
         * if the two are equal, or a positive int if the
         * max of the other Node is larger than the current Node.
         */
        public int compareTo(Node other) {
            if (other == null) {
                return -1;
            }
            double difference = other.max - max;
            if (difference > 0) {
                return 1;
            } else if (difference == 0) {
                return 0;
            }
            return -1;
        }

        /**
         * Implements Comparator functionality for the Node.
         * Compares the value of the Nodes in ascending order.
         * @param n1 First Node to compare.
         * @param n2 Second Node to compare.
         * @return A negative int if the first Node is null or
         * its value is null or smaller than the second Node, zero
         * if the two are equal or are both null, or a positive int
         * if the value of the first Node is larger than the second Node
         * or if the second Node or its value is null.
         */
        public int compare(Node n1, Node n2) {
            if ((n1 == null || n1.val == null) && (n2 == null || n2.val == null)) {
                return 0;
            }
            if (n1 == null || n1.val == null) {
                return -1;
            }
            if (n2 == null || n2.val == null) {
                return 1;
            }
            double difference = n1.val - n2.val;
            if (difference > 0) {
                return 1;
            } else if (difference == 0) {
                return 0;
            }
            return -1;
        }
    }
}
