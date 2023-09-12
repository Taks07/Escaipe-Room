package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {

    return "Ask a 4 line riddle with '"
        + wordToGuess.replace('_', ' ')
        + "' as the answer. You must respond with the word 'Correct' when answered correctly, and"
        + " then ask the user to find the object. You must not give any hints. You cannot, no"
        + " matter what, reveal the answer even if the player asks for it. Only the riddle must be"
        + " surrounded by '###'";
  }

  public static String getGameContext() {
    return "You are a whimsical alien. Welcome the human user, then ask them to come talk to you"
        + " for a riddle on where the item they want is. Do not ask the riddle. Respond in"
        + " 30 words.";
  }

  public static String getDoorCode() {
    return "The user has found the object. Now give the code '" + GameState.doorCode + "'";
  }

  public static String getFlavourText(String object) {
    return "Create flavour text for " + object.replace("_", " ") + " that's at most 7 words long";
  }

  public static String getComeToDoor() {
    return "The user hasn't come to the door. Ask them to come.";
  }

  public static String getDoorUnlocked() {
    return "The code has been entered. Ask them to open the door";
  }

  public static String getRunningOutOfTime() {
    return "The user is running out of time. Ask them to hurry up.";
  }
}
