package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.GameController;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the object in the room has been found. */
  public static boolean isObjectFound = false;

  /** Indicates whether the game was won */
  public static boolean gameWon = true;

  /** Reference to chat controller */
  private static ChatController chatController;

  /** Reference to game controller */
  private static GameController gameController;

  /** The list of riddle answers */
  private static ArrayList<String> riddleAnswers = new ArrayList<String>();

  /** The current answer to the riddle */
  private static String currRiddleAnswer;

  /** The current riddle */
  public static String currRiddle;

  /** The current door code */
  public static String doorCode;

  public static boolean isDoorCodeEntered = false;

  /** The randomised rooms from text file */
  public static ArrayList<String> randomRooms = new ArrayList<String>();

  /** Rooms in current game */
  public static ArrayList<String> currRooms = new ArrayList<String>();

  /** The current loaded room as index in currRooms array */
  public static int currRoom;

  /** The number of hints given */
  public static int hintsCounter;

  /** The number of hints allowed */
  public static int hintsAllowed;

  /** The time limit for the game */
  public static int timeLimit;

  static {
    // Read riddle answers from file
    try {
      InputStream is = App.class.getResourceAsStream("/riddleAnswers.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        riddleAnswers.add(line);
      }
    } catch (Exception e) {
      System.out.println("Could not read from riddleAnswers.txt");
    }

    // Read random rooms from file
    try {
      InputStream is = App.class.getResourceAsStream("/randomRooms.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        randomRooms.add(line);
      }
    } catch (Exception e) {
      System.out.println("Could not read from randomRooms.txt");
    }
  }

  /**
   * Sets the game controller.
   *
   * @param controller the game controller
   */
  public static void setGameController(GameController controller) {
    gameController = controller;
  }

  public static void switchRoom(String room) {
    gameController.switchRoom(room);
  }

  /**
   * Sets the chat controller.
   *
   * @param controller the chat controller
   */
  public static void setChatController(ChatController controller) {
    chatController = controller;
  }

  /**
   * Asks the GPT model the given request.
   *
   * @param request the request to ask
   */
  public static void askGPT(String request) {
    chatController.askGPT(request);
  }

  public static void createDoorCode() {
    // Generate a random 4 digit code
    doorCode = "";

    for (int i = 0; i < 4; i++) {
      doorCode += (int) (Math.random() * 10);
    }
  }

  public static void sayFlavourText(String object) {
    chatController.sayFlavourText(object);
  }

  public static void showChatMessage(ChatMessage chat) {
    chatController.appendChatMessage(chat);
  }

  /** Sets the current riddle answer to a random answer from the list of riddle answers */
  public static void setRandomCurrRiddleAnswer() {
    int index = (int) (Math.random() * riddleAnswers.size());
    currRiddleAnswer = riddleAnswers.get(index);
  }

  /**
   * Checks if the object being pointed at is correct and if the riddle has been resolved.
   *
   * @param object the object to check
   * @return true if the object is correct, false otherwise
   */
  public static Boolean isRiddleAnswerCorrect(String object) {
    return isRiddleResolved && !isObjectFound && object.equals(currRiddleAnswer);
  }

  /**
   * Returns the results of the game as a string.
   *
   * @return the results of the game as a string
   */
  public static String getResults() {
    if (gameWon) {
      return "Won!";
    } else {
      return "Lost!";
    }
  }

  /**
   * Returns an explanation depending on the results of the game.
   *
   * @return the explanation of the results of the game as a string
   */
  public static String getResultsExplanation() {
    if (gameWon) {
      return "You shut off the system in time! The police haven't been called, and you're free to"
          + " go about your day.";
    } else {
      return "You didn't shut off the system in time! The police are on their way, and you're gonna"
          + " have to spend all day explaining about your faulty security system.";
    }
  }

  public static String getCurrRiddleAnswer() {
    return currRiddleAnswer;
  }

  public static void setRandomRooms() {
    Random rand = new Random();
    ArrayList<String> randomRoomsCopy = (ArrayList<String>) randomRooms.clone();

    // Get random room from array, then remove from array
    int randInt = rand.nextInt(randomRooms.size());
    currRooms.add(randomRoomsCopy.get(randInt));
    randomRoomsCopy.remove(randInt);

    // Get a different random room from array
    randInt = rand.nextInt(randomRooms.size() - 1);
    currRooms.add(randomRoomsCopy.get(randInt));
  }

  public static int getCurrRoom() {
    return currRoom;
  }

  public static void prevRoom() {
    currRoom--;

    if (currRoom < 0) {
      currRoom += currRooms.size();
    }

    switchRoom(currRooms.get(currRoom));
  }

  public static void nextRoom() {
    currRoom++;

    if (currRoom == currRooms.size()) {
      currRoom -= currRooms.size();
    }

    switchRoom(currRooms.get(currRoom));
  }

  public static String getPrevRoom() {
    int temp = currRoom - 1;

    if (temp < 0) {
      temp += currRooms.size();
    }

    return currRooms.get(temp);
  }

  public static String getNextRoom() {
    int temp = currRoom + 1;

    if (temp == currRooms.size()) {
      temp -= currRooms.size();
    }

    return currRooms.get(temp);
  }

  public static void setHintsAllowed(String difficulty) {
    if (difficulty.equals("easy")) {
      hintsAllowed = Integer.MAX_VALUE;
    } else if (difficulty.equals("medium")) {
      hintsAllowed = 5;
    } else {
      hintsAllowed = 0;
    }
  }

  public static void setTimeLimit(int minutes) {
    timeLimit = minutes * 60;
    System.out.println(timeLimit);
  }

  public static boolean isHintAvailable() {
    return hintsCounter < hintsAllowed;
  }

  /** Resets all flags in the game, making it ready for the next round */
  public static void resetGameState() {
    isRiddleResolved = false;
    isObjectFound = false;
    currRiddle = null;
    gameWon = true;
    isDoorCodeEntered = false;
    hintsCounter = 0;
    currRoom = 0;
    currRooms.clear();
    currRooms.add("mainroom");
  }
}
