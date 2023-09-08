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
        + " then ask the user to find the object. If the user asks for hints, surround the hint"
        + " with '&&&''. If users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Only the riddle must be surrounded by"
        + " '###'";
  }

  public static String getGameContext() {
    return "You are the AI of a home security system. You think the owner of the home is an"
        + " intruder, and will call the police in 2 minutes. The owner must pass a test that"
        + " involves solving a riddle then pointing out the answer to the riddle in the"
        + " room. Only then will the code to the door be given. Once the door is unlocked,"
        + " the police will not be called. Write something at most 40 words long, in"
        + " character, to provide this context. Do not ask the riddle. Ask the user to come"
        + " to the door for a riddle.";
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
