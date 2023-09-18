package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.GameController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

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

  /** The hashmap to array of riddle answers */
  private static HashMap<String, ArrayList<String>> riddleAnswers =
      new HashMap<String, ArrayList<String>>();

  /** The current answer to the riddle */
  private static String currRiddleAnswer;

  /** The current riddle */
  public static String currRiddle;

  /** The randomised rooms from text file */
  public static ArrayList<String> randomRooms = new ArrayList<String>();

  /** Rooms in current game */
  public static ArrayList<String> currRooms = new ArrayList<String>();

  /** Whether or not minigame was solved in each room */
  public static ArrayList<Boolean> minigameSolved = new ArrayList<Boolean>();

  /** The current loaded room as index in currRooms array */
  public static int currRoom;

  /** The chat completion requests for each room */
  public static ArrayList<ChatCompletionRequest> roomChatCompletionRequests =
      new ArrayList<ChatCompletionRequest>();

  /** The number of hints given */
  public static int hintsCounter;

  /** The number of hints allowed */
  public static int hintsAllowed;

  /** The time limit for the game */
  public static int timeLimit;

  /** The number of parts found */
  public static int partsFound;

  static {
    // Read random rooms and their riddle answers from file
    try {
      InputStream isRoom = App.class.getResourceAsStream("/randomRooms.txt");
      BufferedReader readerRoom = new BufferedReader(new InputStreamReader(isRoom));

      while (true) {
        String line = readerRoom.readLine();
        if (line == null) {
          break;
        }
        randomRooms.add(line);

        ArrayList<String> temp = new ArrayList<String>();

        // Read riddle answers from the room
        try {
          InputStream isRiddle = App.class.getResourceAsStream("/answers/" + line + ".txt");
          BufferedReader readerRiddle = new BufferedReader(new InputStreamReader(isRiddle));

          while (true) {
            String line2 = readerRiddle.readLine();
            if (line2 == null) {
              break;
            }

            temp.add(line2);
          }
        } catch (Exception e) {
          System.out.println("Could not read from riddleAnswers.txt");
        }

        riddleAnswers.put(line, temp);
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

  /**
   * Switches the room.
   *
   * @param room the room to switch to
   */
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

  /** Returns a hint prompt depending on game state */
  public static String getHint() {
    String roomName = currRooms.get(currRoom);

    // Tell user to talk to alien by rocket to get a riddle
    if (currRiddle == null) {
      if (roomName.equals("mainroom")) {
        return "Ask the user to come to you for a riddle. Do not say a riddle";
      } else {
        return "Ask the user to talk to the alien by the rocket for a riddle. Do not say a riddle";
      }
    }

    // As more puzzles are added, add more cases to provide context for hints
    switch (roomName) {
      case "mainroom":
        return "Give a short hint for a riddle with the answer "
            + currRiddleAnswer
            + ". Do not say the word "
            + currRiddleAnswer;
      default:
        return "Give a short hint for a riddle with the answer "
            + currRiddleAnswer
            + ". Do not say the word "
            + currRiddleAnswer;
    }
  }

  /** Gets AI to say some flavour text */
  public static void sayFlavourText(String object) {
    chatController.sayFlavourText(object);
  }

  /** Puts message into chatbox */
  public static void showChatMessage(ChatMessage chat) {
    chatController.appendChatMessage(chat.getContent());
  }

  /** Sets the current riddle answer to a random answer from the list of riddle answers */
  public static void setRandomCurrRiddleAnswer() {
    ArrayList<String> temp = new ArrayList<String>();
    temp.addAll(riddleAnswers.get(currRooms.get(1)));
    temp.addAll(riddleAnswers.get(currRooms.get(2)));

    int index = (int) (Math.random() * temp.size());
    currRiddleAnswer = temp.get(index);
    System.out.println(currRiddleAnswer);
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
   * Returns the current riddle answer.
   *
   * @return the current riddle answer
   */
  public static String getCurrRiddleAnswer() {
    return currRiddleAnswer;
  }

  /** Chooses 2 random rooms from the pool of 4 */
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

    // Set chat completion requests for each room
    for (int i = 0; i < 3; i++) {
      ChatCompletionRequest chatCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

      // Provide context to ai
      String context = GptPromptEngineering.alienContext(currRooms.get(i));

      ChatMessage msg = new ChatMessage("system", context);
      chatCompletionRequest.addMessage(msg);

      roomChatCompletionRequests.add(chatCompletionRequest);
    }
  }

  /** Returns the chat completion request for the current room */
  public static ChatCompletionRequest getChatCompletionRequest() {
    return roomChatCompletionRequests.get(currRoom);
  }

  /** Returns the current room index */
  public static int getCurrRoom() {
    return currRoom;
  }

  /** Switches to previous room */
  public static void prevRoom() {
    currRoom--;

    if (currRoom < 0) {
      currRoom += currRooms.size();
    }

    switchRoom(currRooms.get(currRoom));
  }

  /** Switches to next room */
  public static void nextRoom() {
    currRoom++;

    if (currRoom == currRooms.size()) {
      currRoom -= currRooms.size();
    }

    switchRoom(currRooms.get(currRoom));
  }

  /** Get name of previous room */
  public static String getPrevRoom() {
    int temp = currRoom - 1;

    if (temp < 0) {
      temp += currRooms.size();
    }

    return currRooms.get(temp);
  }

  /** Get name of next room */
  public static String getNextRoom() {
    int temp = currRoom + 1;

    if (temp == currRooms.size()) {
      temp -= currRooms.size();
    }

    return currRooms.get(temp);
  }

  public static void incrementPartsFound() {
    partsFound++;
    chatController.setPartsCounter(partsFound);
  }

  /** Sets the number of hints allowed for the game */
  public static void setHintsAllowed(String difficulty) {
    if (difficulty.equals("easy")) {
      hintsAllowed = Integer.MAX_VALUE;
    } else if (difficulty.equals("medium")) {
      hintsAllowed = 5;
    } else {
      hintsAllowed = 0;
    }
  }

  /**
   * Check whether minigame in current room has been solved
   *
   * @return true if minigame has been solved, false otherwise
   */
  public static boolean getMinigameSolved() {
    return minigameSolved.get(currRoom);
  }

  /** Sets the minigame in the current room as solved */
  public static void setMinigameSolved() {
    minigameSolved.set(currRoom, true);
  }

  /** Sets the time limit for the game */
  public static void setTimeLimit(int minutes) {
    timeLimit = minutes * 60;
  }

  /**
   * Checks if a hint is available.
   *
   * @return true if a hint is available, false otherwise
   */
  public static boolean isHintAvailable() {
    return hintsCounter < hintsAllowed;
  }

  /** Resets all flags in the game, making it ready for the next round */
  public static void resetGameState() {
    isRiddleResolved = false;
    isObjectFound = false;
    currRiddle = null;
    gameWon = true;
    hintsCounter = 0;
    partsFound = 0;
    currRoom = 0;

    // Reset minigameSolved
    minigameSolved.clear();
    for (int i = 0; i < 3; i++) {
      minigameSolved.add(false);
    }

    currRooms.clear();
    currRooms.add("mainroom");
    roomChatCompletionRequests.clear();
  }
}
