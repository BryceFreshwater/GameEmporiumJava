package ff;

class Game {
    int copies;
    private char format;
    private String title;

    public Game(int copies, char format, String title) {
        this.copies = copies;
        this.format = format;
        this.title = title;
    }

    public int getCopies() {
        return copies;
    }

    public char getFormat() {
        return format;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + " (" + format + ")";
    }
}









