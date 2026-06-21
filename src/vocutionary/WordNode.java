package vocutionary;

public class WordNode {
    public Word data;
    public WordNode left;
    public WordNode right;

    // Constructor to initialize a new tree node
    public WordNode(Word data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}