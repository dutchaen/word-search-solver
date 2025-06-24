import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String PuzzleCharset = "abcdefghijklmnopqrstuvwxyz\r\n";

    public static void main(String[] args) {

        var puzzle = getWordSearchPuzzle();
        if (puzzle == null) {
            System.out.println("unable to parse word search from 'puzzle.txt'");
            return;
        }

        System.out.println("successfully parsed word search!");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("[?] Word >> ");
            String word = scanner.nextLine();

            PuzzleWord puzzleWord  = findWordInPuzzle(puzzle, word);
            if (!puzzleWord.found) {
                System.out.println("did not find the word in the puzzle");
                continue;
            }

            printWordLocationInPuzzle(puzzle, puzzleWord, word);
        }
    }


    public static void printWordLocationInPuzzle(char[][] puzzle, PuzzleWord word, String text) {

        if (!word.found) {
            return;
        }

        if (word.direction == PuzzleWordDirection.Right) {
            System.out.println("[→] Direction: Right");
        }
        else {
            System.out.println("[↓] Direction: Down");
        }
        System.out.printf("[!] Position of \"%s\": { %d steps down, %d steps right }\r\n", text, word.y + 1, word.x + 1);

        System.out.println();

        char[][] out = new char[3][3];

        int startX = Math.max(word.x - 1, 0);
        int startY = Math.max(word.y - 1, 0);

        if (word.y == puzzle.length - 1) {
            startY = word.y - 2;
        }

        if (word.x == puzzle[word.y].length - 1) {
            startX = word.x - 2;
        }


        out[0] = new char[] { puzzle[startY][startX],  puzzle[startY][startX+1], puzzle[startY][startX+2] };
        out[1] = new char[] { puzzle[startY+1][startX], puzzle[startY+1][startX+1], puzzle[startY+1][startX+2] };
        out[2] = new char[] { puzzle[startY+2][startX],  puzzle[startY+2][startX+1], puzzle[startY+2][startX+2] };

        for (char[] chars : out) {
            for (char c : chars) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static PuzzleWord findWordInPuzzle(char[][] puzzle, String word) {

        char firstChar = word.charAt(0);
        List<int[]> firstCharPositions = new ArrayList<>();

        for (int y = 0; y < puzzle.length; y++) {
            for (int x = 0; x < puzzle[y].length; x++) {
                if (puzzle[y][x] == firstChar) {
                    firstCharPositions.add(new int[] { x, y });
                }
            }
        }

        for (var position : firstCharPositions) {
            int x = position[0];
            int y = position[1];

            boolean canReadDown = y + word.length() <= puzzle.length;
            boolean canReadRight = x + word.length() <= puzzle[y].length;

            boolean foundDown = true;
            boolean foundRight = true;

            if (!canReadDown && !canReadRight) {
                continue;
            }

            if (canReadDown) {
                for (int i = y; i < y + word.length(); i++) {
                    char c = word.charAt(i - y);
                    if (c != puzzle[i][x]) {
                        foundDown = false;
                        break;
                    }
                }

                if (foundDown) {
                    return new PuzzleWord(position, PuzzleWordDirection.Down);
                }
            }

            if (canReadRight) {
                for (int i = x; i < x + word.length(); i++) {
                    char c = word.charAt(i - x);
                    if (c != puzzle[y][i]) {
                        foundRight = false;
                        break;
                    }
                }

                if (foundRight) {
                    return new PuzzleWord(position, PuzzleWordDirection.Right);
                }
            }
        }

        return new PuzzleWord();
    }

    public static char[][] getWordSearchPuzzle() {

        File f  = new File("puzzle.txt");
        if (!f.exists()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        try (FileReader reader = new FileReader(f)) {

            while (reader.ready()) {
                int c = reader.read();
                char c0 = Character.toLowerCase((char)c);

                boolean isValid = PuzzleCharset.indexOf(c0) != -1;
                if (!isValid) {
                    return null;
                }

                builder.append(c0);
            }

            var text = builder.toString().toLowerCase();
            var chunks = text.split("\r\n");

            int expectedLength = chunks[0].length();
            for (String chunk : chunks) {
                if (chunk.length() != expectedLength)
                    return null;
            }

            char[][] wordSearch = new char[chunks.length][expectedLength];
            for (int i = 0; i < chunks.length; i++) {
                wordSearch[i] = chunks[i].toCharArray();
            }

            return wordSearch;

        }
        catch (Exception ex) {
            return null;
        }
    }
}
