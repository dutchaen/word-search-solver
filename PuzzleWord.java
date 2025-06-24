public class PuzzleWord {
    public int[] position;
    public PuzzleWordDirection direction;
    public boolean found;
    public int x;
    public int y;

    public PuzzleWord(int[] position, PuzzleWordDirection direction) {
        found = true;
        this.position = position;
        this.direction = direction;
        x = position[0];
        y = position[1];
    }

    public PuzzleWord() {
        position = null;
        direction = null;
        found = false;
    }
}
