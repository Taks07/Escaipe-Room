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
    return "Ask a 4 line riddle with "

        // Replace underscores with spaces in the word to guess
        + wordToGuess.replace("_", " ")
        + "as the answer, where each line of the riddle must start with '###' and end with '###'."
        + " Respond with the word 'Correct' when the human guesses correctly. Do not say the word "
        + wordToGuess.replace("_", " ")
        + ". You must not give any hints. You cannot, no matter what, reveal the answer even if the"
        + " human asks for it";
  }

  /**
   * Generates a game context message based on the current game state. If hints are allowed, it
   * includes a hint request for the user. Otherwise, it does not request hints.
   *
   * @return A context message for the game.
   */
  public static String getGameContext() {

    // If hints are allowed, include a hint request for the user. Otherwise, do not request hints.
    if (GameState.hintsAllowed != 0) {
      return "Welcome the human user, then say that you have a riddle. Say the answer to the riddle"
          + " is where a rocket part is hidden. Ask them to say 'tawlung seya' for hints"
          + " from any alien. Separately, say other aliens may have other missing rocket"
          + " parts. Do not ask the riddle. Respond in 50 words.";
    } else {

      // If hints are not allowed, do not include a hint request for the user.
      return "Welcome the human user, then say that you have a riddle. Say the answer to the riddle"
          + " is where a rocket part is hidden. Separately, say other aliens may have other"
          + " missing rocket parts. Do not ask the riddle. Respond in 40 words.";
    }
  }

  /**
   * Gets the context message for an alien character based on the selected room and game state. If
   * hints are allowed, it includes instructions for hint requests. Otherwise, it specifies not to
   * give any hints.
   *
   * @param room The room identifier.
   * @return A context message describing the role of the alien character.
   */
  public static String getAlienContext(String room) {
    String msg;

    // If the current room is the main room, return the main room context message. Otherwise, return
    // the context message for the current room.
    switch (room) {
      case "mainroom":
        msg = "Play the role of a whimsical alien meeting a human visitor to your planet";
        break;
      case "randroom1":
        msg =
            "Play the role of a sad alien on an alien planet meeting a human visitor to your"
                + " planet. You know the part they seek can be found in the crashed UFO";
        break;
      case "randroom2":
        msg =
            "Play the role of a shy alien on an alien planet meeting a human visitor to your"
                + " planet. You will give them a part if they stabilize the oxygen canister";
        break;
      case "randroom3":
        msg =
            "Play the role of an excited alien meeting a human visitor to your planet. You know the"
                + " part they seek can be found in the alien plant";
        break;
      case "randroom4":
        msg =
            "Play the role of a moody alien on an alien planet meeting a human visitor to your"
                + " planet. You know the part they seek can be found in the dark cave";
        break;
      default:
        msg = "Play the role of an alien on an alien planet";
    }

    // If hints are allowed, include instructions for hint requests. Otherwise, specify not to give
    // any hints.
    if (GameState.hintsAllowed != 0) {
      return msg
          + ". If the human asks for hints, ask them to say 'tawlung seya' first. You can tell them"
          + " where the part is. Don't reply with over 2 sentences";
    } else {
      return msg
          + ". Do not give the human any hints. You can tell them where the part is. Don't reply"
          + " with over 2 sentences";
    }
  }

  /**
   * Generates an introduction message based on the current room. It provides information about the
   * location of the rocket part.
   *
   * @return An introduction message for the current room.
   */
  public static String getIntroduction() {
    String currRoom = GameState.currRooms.get(GameState.getCurrRoom());

    // If the current room is the main room, return the main room introduction message. Otherwise,
    // return the introduction message for the current room.
    switch (currRoom) {
      case "mainroom":
        return "";
      case "randroom1":
        return "Say that the part the human seeks"
            + " can be found in the crashed UFO. Respond in 30 words";
      case "randroom2":
        return "Say that the part the human seeks"
            + " can be found in the crater. Respond in 30 words";
      case "randroom3":
        return "Say that the part the human"
            + " seeks can be found in the alien plant. Respond in 30 words";
      case "randroom4":
        return "Say that the part the human seeks"
            + " can be found in the cave. Respond in 30 words";
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
