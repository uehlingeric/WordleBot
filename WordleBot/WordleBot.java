package WordleBot;
import java.io.*;
import java.util.*;

/* Eric Uehling 10.27.2021
 *
 * Usage (in terminal, in immediate directory): java WordleBot.java
 * 
 * Description: 
 * 
 * Bot will output 5 letter string to input into online wordle game. 
 * Bot will then ask for input of the information provided.
 * 
 *  1 - If said letter is absent (grey)
 *  2 - If said letter is present but in wrong place (yellow)
 *  3 - If said letter is present and in correct place (green)
 * 
 * Enter each value individually, in order.
 * Repeats until game has ended.
 * Algorithm: https://docs.google.com/spreadsheets/d/1P0uHB4j99cqztub6xJRM-bYGSF_P_hGBMrZJpuo-LLY/edit#gid=0
 */
public class WordleBot {
    private static final String NAME = "C:\\Users\\Eric\\Desktop\\CS\\Personal Projects\\WordleBot\\algorithm.txt";
    private static final int maxGuess = 6;
    private static final int numLettters = 5;
    private ArrayList<String> guessList;
    private int[][] info;
    public static Scanner input = new Scanner(System.in);
    private ArrayList<ArrayList<String>> wordList;
    
    /* Creates a WordleBot object and updates the word list. */
    public WordleBot() {
        guessList = new ArrayList<>();
        info = new int[maxGuess][numLettters];

        wordList = new ArrayList<ArrayList<String>>();
        try {
            File file = new File(NAME);
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String temp = in.nextLine();
                wordList.add(parseLine(temp));
            }
            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* Parses a string and adds the information to an array list and returns it. */
    private ArrayList<String> parseLine(String line) {
        ArrayList<String> list = new ArrayList<>();
        String[] temp = line.split("\t");
        for (int i = 0; i < temp.length; i++) {
            list.add(temp[i]);
        }
        return list;
    }

    /* Checks if there is only one word left. */
    private boolean isSolved() {
        return (wordList.size() == 1);
    }

    /* Checks if the last guess was correct. */
    private boolean checkGuess() {
        int[] lastInfo = info[guessList.size() - 1];
        boolean solved = true;
        for (int i = 0; i < numLettters; i++) {
            if (lastInfo[i] != 3) solved = false;
        }
        return solved;
    }

    /* Displays the information the user is working with. */
    private void displayInfo() {
        for (int r = 0; r < maxGuess; r++) {
            if (r < guessList.size()) System.out.println(guessList.get(r));
            for (int c = 0; c < numLettters; c++) {
                if (info[r][c] == 0) {
                    return;
                }
                System.out.print(info[r][c] + " ");
            }
            System.out.println();
        }
    }

    /* Recieves the data given from the user about most recent guess provided. */
    private void nextGuess() {
        String guess = determineGuess();
        System.out.println(guess);
        int[] values = new int[numLettters];

        for (int i = 0; i < numLettters; i++) {
            values[i] = input.nextInt();
            while (values[i] < 1 || values[i] > 3) {
                System.out.println("The value is not within 1-3.");
                values[i] = input.nextInt();
            }
        }
        guessList.add(guess);
        info[guessList.size() - 1] = values;
    }

    /* Returns the next guess given to the user. */
    private String determineGuess() {
        if (guessList.size() == 0) {
            return "salet";
        }
        else {
            return wordList.get(0).get(0);
        }
        
    }

    /* Removes the incorrect words from the list based on the information provided. */
    private void updateList() {
        int[] lastInfo = info[guessList.size() - 1];
        String s = "_____";
        for (int i = 0; i < lastInfo.length; i++) {
            if (lastInfo[i] == 3) s = s.substring(0, i) + "G" + s.substring(i + 1);
            else if (lastInfo[i] == 2) s = s.substring(0, i) + "Y" + s.substring(i + 1);
        }
        
        for (int i = wordList.size() - 1; i >= 0; i--) {
            if (guessList.size() > 1) wordList.get(i).remove(0);
            if (!wordList.get(i).get(0).equals(s)) wordList.remove(i);
            else {
                wordList.get(i).remove(0);
            }
        }
    }

    /* Runs solver. */
    public void bot() {
        int count = 0;
        for (int i = 0; i < maxGuess; i++) {
            nextGuess();
            if (checkGuess()) break;
            displayInfo();
            count++;
            updateList();
            if (isSolved()) break;
        }
        count++;
        System.out.println(wordList.get(0).get(0));
        System.out.println("3 3 3 3 3");
        System.out.println("The Algorithm solved the wordle in " + count
            + " turns.");
    }

    public static void main(String[] args) {
        WordleBot bot = new WordleBot();
        bot.bot();
    }
}