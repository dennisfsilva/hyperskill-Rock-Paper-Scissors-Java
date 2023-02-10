package rockpaperscissors;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    private static Map<String, String> options;
    private static String userName;
    private static int userScore;
    private static Random random;
    private static Scanner scanner;

    public static void main(String[] args) {
        options = new HashMap<>();
        random = new Random();
        scanner = new Scanner(System.in);

        userName = promptUserName();
        userScore = promptUserScore();

        promptOptionsList();

        System.out.println("Okay, let's start");
        runGameLoop();
    }

    private static String promptUserName() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name);
        return name;
    }

    private static int promptUserScore() {
        int score = 0;

        try {
            Scanner ratingFile = new Scanner(new File("rating.txt"));
            while (ratingFile.hasNext()) {
                String entry = ratingFile.nextLine();
                if (entry.contains(userName)) {
                    score = Integer.parseInt(entry.split(" ")[1]);
                    break;
                }
            }
            ratingFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return score;
    }

    private static void promptOptionsList() {
        String optionsList = scanner.nextLine();
        if (!optionsList.isEmpty()) {
            options.clear();
            String[] optionsArray = optionsList.split(",");
            int length = optionsArray.length;
            int half = length / 2;
            for (int i = 0; i < length; i++) {
                String[] losingTo = new String[half];
                for (int j = i + 1; j < i + half + 1; j++) {
                    losingTo[j - (i + 1)] = optionsArray[j % length];
                }
                options.put(optionsArray[i], Arrays.toString(losingTo));
            }
        } else {
            options.put("rock", "[paper]");
            options.put("paper", "[scissors]");
            options.put("scissors", "[rock]");
        }
    }

    private static void runGameLoop() {
        while (true) {
            String userChoice = scanner.nextLine().toLowerCase();
            if (userChoice.equals("!exit")) {
                System.out.println("Bye!");
                break;
            } else if (userChoice.equals("!rating")) {
                System.out.println("Your rating: " + userScore);
            } else if (!options.containsKey(userChoice)) {
                System.out.println("Invalid input");
            } else {
                String compChoice = getRandomOption();
                int result = compare(compChoice, userChoice);
                switch (result) {
                    case 1 -> System.out.println("Sorry, but the computer chose " + compChoice);
                    case 0 -> {
                        System.out.println("There is a draw (" + compChoice + ")");
                        userScore += 50;
                    }
                    case -1 -> {
                        System.out.println("Well done. The computer chose " + compChoice + " and failed");
                        userScore += 100;
                    }
                    default -> System.out.println("Error!");
                }
            }
        }
    }

    private static int compare(String p1, String p2) {
        if (p1.equals(p2)) {
            return 0;
        }
        return options.get(p1).contains(p2) ? -1 : 1;
    }

    private static String getRandomOption() {
        Object[] optionArray = options.keySet().toArray();
        return (String) optionArray[random.nextInt(optionArray.length)];
    }
}