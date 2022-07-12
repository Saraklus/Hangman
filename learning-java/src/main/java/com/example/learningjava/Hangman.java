package com.example.learningjava;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//  SOLVE SPACES LEARN GIT
public class Hangman {
    private final String correctWord;
    private final String correctWordWithSpaces;
    private final Set<Character> guessedLettersSet;
    private final Set<Character> correctLettersSet;
    private int numberOfLives;
    private final char[] correctWordAsCharArray;
    char[] wordAsCharArray;
    private boolean gameOn = true;
    private final long timerStart = System.currentTimeMillis();
    public Hangman() {
        String countryAndCapital = randomWord();
        String[] countryAndCapitalList = countryAndCapital.split("[|]+");
        correctWordWithSpaces = countryAndCapitalList[1].trim().toLowerCase();
        correctWord = countryAndCapitalList[1].replaceAll("\\s+","").toLowerCase();
        guessedLettersSet = new HashSet<>();
        numberOfLives = 8;
        wordAsCharArray = correctWordWithSpaces.toLowerCase().toCharArray();
        correctWordAsCharArray = correctWord.toLowerCase().toCharArray();
        correctLettersSet = createCorrectLettersSet();
    }

    private Set<Character> createCorrectLettersSet() {
        final Set<Character> correctLettersSet = new HashSet<>();
        for(char c : correctWordAsCharArray) {
            correctLettersSet.add(c);
        }
        return correctLettersSet;
    }

    public void play() {
        while (numberOfLives > 0 && gameOn) {
            System.out.println(correctWord);
            displayGameStatus();
            printDashedWord();
            guessLetter();
            checkGameStatus();
            clearScreen();
        }
    }

    private void checkGameStatus(){
        checkWinCondition();
        checkLoseCondition();
    }
    private void checkLoseCondition() {
        if (numberOfLives == 0) {
            System.out.println("  ________ ");
            System.out.println("  |      | ");
            System.out.println("  0      | ");
            System.out.println(" /|\\     | ");
            System.out.println(" / \\     | ");
            System.out.println("       __|__");
            System.out.println("You lose!");
            System.out.println("The word was " + correctWord);
            askToPlayAgain();
        }
    }
    private void writeToHighScores(long time){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type you name");
        String username = scanner.nextLine();
        try(FileWriter highScore = new FileWriter("highScores.txt",true)) {
            highScore.write(username+"|"+correctWordWithSpaces+"-"+time+"\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void printHighScores(){
        ArrayList<String> highScoresList = new ArrayList<>();
        File file = new File("highScores.txt");
        try{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String userRecord = scanner.nextLine();
                highScoresList.add(userRecord);
            }
            System.out.println("High scores");
            for(int i=0;i<Math.min(highScoresList.size(), 10);i++)
                System.out.println(highScoresList.get(i));
        }
         catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void askToPlayAgain(){
        System.out.println("Do you want to play again?");
        System.out.println("(Type y/yes to play again) ");
        Scanner scanner = new Scanner(System.in);
        String playerInput = scanner.next().toLowerCase();
        if(playerInput.equals("y")||
           playerInput.equals("yes")) {
            Hangman hangman = new Hangman();
            hangman.play();
        }
        else {
            gameOn = false;
        }
    }
    private void checkWinCondition() {
        if(correctLettersSet.equals(guessedLettersSet)){
            winGame();
        }
    }

    private void guessLetter(){
        Scanner scanner = new Scanner(System.in);
        String playerInput = scanner.next().toLowerCase();
        boolean isLetterGuessed = false;

        if (playerInput.length() == 1) {
            for (char correctLetter : correctWordAsCharArray) {
                char playerInputChar = playerInput.charAt(0);
                if (playerInputChar == correctLetter) {
                    System.out.println("Correct");
                    guessedLettersSet.add(playerInputChar);
                    isLetterGuessed = true;
                    break;
                }

            }
        }
        else{
            if(playerInput.equals(correctWordWithSpaces)){
                winGame();
            }
        }
        if(!isLetterGuessed) {
            System.out.println("Wrong");
            numberOfLives = numberOfLives - 1;
        }
    }

    public static String randomWord() {
        ArrayList<String> randomWord = new ArrayList<>();
        try {
            File file = new File("countries-capitals.txt");
            Scanner textReader = new Scanner(file);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                randomWord.add(data);
            }
            textReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not load the file.");
            e.printStackTrace();
        }
        Random randomLine = new Random();
        return randomWord.get(randomLine.nextInt(randomWord.size()));
    }

    private void winGame(){
        long timerEnd = System.currentTimeMillis();
        long time = (timerEnd - timerStart)/1000;
        System.out.println(correctWordAsCharArray);
        System.out.println("You win!");
        System.out.println("Your time was "+time+" seconds");
        writeToHighScores(time);
        printHighScores();
        askToPlayAgain();
    }
    private void printDashedWord() {
        for (char c : wordAsCharArray) {
            if(guessedLettersSet.contains(c)){
                System.out.print(c);
            }
            else if (c == ' ') {
                System.out.print(' ');
            }
            else {
                System.out.print("_");
            }
        }
        System.out.println();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void displayGameStatus() {
        printHangmanStage();
        System.out.println("Number of lives: " + numberOfLives);
        System.out.println("Choose a letter");
    }

    private void printHangmanStage() {
        switch(numberOfLives){
            case 1:
                System.out.println("  ________ ");
                System.out.println("  |      | ");
                System.out.println("  0      | ");
                System.out.println("  |      | ");
                System.out.println(" / \\     | ");
                System.out.println("       __|__");
                break;
            case 2:
                System.out.println("  ________ ");
                System.out.println("  |      | ");
                System.out.println("  0      | ");
                System.out.println("  |      | ");
                System.out.println("         | ");
                System.out.println("       __|__");
                break;
            case 3:
                System.out.println("  ________ ");
                System.out.println("  |      | ");
                System.out.println("  0      | ");
                System.out.println("         | ");
                System.out.println("         | ");
                System.out.println("       __|__");
                break;
            case 4:
                System.out.println(" ________ ");
                System.out.println(" |      | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("      __|__");
                break;
            case 5:
                System.out.println(" ________ ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("      __|__");
                break;
            case 6:
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("        | ");
                System.out.println("      __|__");
                break;
            case 7:
                System.out.println("      _____");
                break;
        }
    }
}
