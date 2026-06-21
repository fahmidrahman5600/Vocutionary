package vocutionary;

public class Word {
    public String text;
    public String meaning;
    public int mistakeCount;

    // Use this when a user manually adds a brand new word
    public Word(String text, String meaning) {
        this.text = text;
        this.meaning = meaning;
        this.mistakeCount = 0;
    }

    // Use this when loading existing words from your data file/preset list
    public Word(String text, String meaning, int mistakeCount) {
        this.text = text;
        this.meaning = meaning;
        this.mistakeCount = mistakeCount;
    }
}