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
        + "' as the answer. You must start your response with the word 'Correct' when the user"
        + " answers correctly, and then ask the user to find where the "
        + wordToGuess.replace('_', ' ')
        + " is. You must not give any hints. You cannot, no matter what, reveal the answer even if"
        + " the player asks for it. Only the riddle must start and end with '###'. Otherwise, never"
        + " use '###";
  }

  public static String getGameContext() {
    if (GameState.hintsAllowed != 0) {
      return "Welcome the human user, then ask them to come"
          + " talk to you for a riddle on where the part they want is. Ask them to say"
          + " 'tawlung seya' for hints from any alien. Seperately, say other aliens may have"
          + " other parts. Do not ask the riddle. Respond in 40 words.";
    } else {
      return "Welcome the human user, then ask them to come"
          + " talk to you for a riddle on where the part they want is. Seperately, say other"
          + " aliens may have other parts. Do not ask the riddle. Respond in 30 words.";
    }
  }

  public static String alienContext(String room) {
    switch (room) {
      case "mainroom":
        return "Play the role of a whimsical alien meeting a human visitor to your planet";
      case "randroom1":
        return "Play the role of a sad alien on an alien planet meeting a human visitor to your"
            + " planet. You know the part they seek can be found in the crashed UFO. Don't"
            + " reply with over 2 sentences";
      case "randroom2":
        return "Play the role of a shy alien on an alien planet meeting a human visitor to your"
            + " planet. You know the part they seek can be found in the crater. Don't reply"
            + " with over 2 sentences";
      case "randroom3":
        return "Play the role of a excited alien meeting a human visitor to your planet. You know"
            + " the part they seek can be found in the alien plant. Don't reply with over 2"
            + " sentences";
      case "randroom4":
        return "Play the role of a moody alien on an alien planet meeting a human visitor to your"
            + " planet. You know the part they seek can be found in the dark cave. Don't"
            + " reply with over 2 sentences";
      default:
        return "Play the role of an alien on an alien planet";
    }
  }

  public static String introduction() {
    String currRoom = GameState.currRooms.get(GameState.getCurrRoom());

    switch (currRoom) {
      case "mainroom":
        return "";
      case "randroom1":
        return "Say that the part they seek"
            + " can be found in the crashed UFO. Respond in 30 words";
      case "randroom2":
        return "Say that the part they seek" + " can be found in the crater. Respond in 30 words";
      case "randroom3":
        return "Say that the part they"
            + " seek can be found in the alien plant. Respond in 30 words";
      case "randroom4":
        return "Say that the part they seek" + " can be found in the cave. Respond in 30 words";
      default:
        return "You are an alien. Welcome the human";
    }
  }

  public static String getFlavourText(String object) {
    return "Create flavour text for " + object.replace("_", " ") + " that is 10 words long";
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
