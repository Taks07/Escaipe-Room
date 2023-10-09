package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.se206.GameMediaPlayer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController {
  // Various fields for managing chat and interactions
  private ChatCompletionRequest flavourTxtChatCompletionRequest;
  private ChatCompletionRequest hintTxtCompletionRequest;
  private Thread chatThread;
  private Thread animationThread;
  private Pattern riddlePattern;
  private String randomSigns;
  private TimerTask alienTextTask;
  private boolean isTranslating;
  private int currRoom;
  private static String[] randomAlienTexts = {
    "คгђקкภשєгץฬคเкฬђєש๔ยгคє",
    "เ๒เקгคкฬђเгђคєเђคгкשקгคгђקภђђשкђђђгยคгคгкฬคкเгкคשђคк๔ק๔คשђเ๒๔г๔",
    "เ๔קгשкคгเקค๒๒๒кєคקค๔เгкђ๔คгкเภןคгђเคкค๒ђคץչг๔гкเ๒๒๒ןєเ๒ןยคкเ๒єเคггץչ"
        + "ןђคгคคє๔гשђן๔гคг๔гђค๔คгןคยค๒ยкєקคкгк๔ןคк๔๔คєгђ",
    "кђคгןןкคк๔гкคค๔ђןคкץչשкเยเгןђคгยкเยкгןг๔ןђเยг๔гยשкค๔гкคยк๔๔เเгคгђเเ๔ค"
        + "гкเรкค๔ย๒к๒кђย๒ยยשץչןย๒เยןкคкгเק๔เкค๔เгยק๔กץฬคเкฬђєש๔ยשкค๔гкฬђєคยк๔๔เเгคгђ",
    "๔๔๔๒кคשк๔๒гкคשยคยยгยกยгคгเђยยгคเยן๔гเกเรкєк๔เย๒קคเค๒кเ๔гคยเкггย๔คยเгггคк๔เ๔гเยยก๔๔เггץ"
        + "չкггคยย๔เยкยкเคгคкเยฬђєยг๔гยשкค๔гкคยк๔๔เเгคгђเเ๔คгкเรкค๔ย๒к๒кђย๒ยยשןย๒เยןкคкгเק๔เ",
    "ггคгןןשгย๒гкยยгกןเгเยเкคשยгкยןยยггยк๔ยןยคк๔гยคยןгยкгเг๔кгкггยกยгยยยкกгยץչคгђקк"
        + "ภשєгץฬคเкฬђยยฬђєยยยкยккคยเкгยкгยยкคгђקкภשєгץฬคเкฬђгยกยггยгкг๔",
    "๒קгคгђקภђђשєгץฬคเкฬђєש๔ยгקкภשєгץฬคเкฬђยยฬђєยยยкยккคยเкгยкгยยкคгђקкภשєгץฬคเк"
        + "ฬђгยกยггยгкг๔к๔เ๔ץչгเยยก๔๔เггкгשєгץฬคเкฬђгยกยггยгкг",
    "๔ןђเยг๔гยשкค๔гкคยк๔๔เเгคץչгђเเ๔คгкเรкคยยгกןเгเยเкץչคשยгкยןยยץչггยк๔ยןยคк๔гยค"
        + "ย๔คгןคยค๒ยкєקคкгк๔ןคк๔๔คєгђยгยกยгץչคгเђยยгคเยן๔гเกเรкєк๔เย๒קคเค๒кเ๔гคยเкггย๔คย"
        + "เгггค๔คгןคยค๒ยкєקคкгк๔ןคк๔๔คєгђк๔เ๔гเยยก๔๔เггкггคยย๔เ",
    "ןђคгยкเยкгןг๔ןђเยгเยเкคשยгкยןยยггยк๔ยןยคк๔гยคยןгยкקгคгђקภђђשкђђђгยคгคгкฬץչคкเг"
        + "гเг๔кгкггยกยгยץչยยкกгยคгђ๔гยשкค๔гкคยк๔๔เเгคгђเเ๔คгкเรкค๔ย๒к๒кђย๒ยยשןย๒เยןкคкгเкггคย"
        + "ย๔เยкยкเคгคкเยฬђєยг๔ยשкค๔гкคยк๔๔เเгคгђเเ๔คгкเรкค๔ยкггคยย๔เยкยкเคгคкเยฬђєยг๔๒к๒кђย๒кเคг"
        + "คкเยฬђєยг๔гยשкค๔гкคยк๔๔เเгคгђเเ๔คгкเรкค๔ย๒к๒кђย๒ยยשןย๒เยןкเยเкคשยгкยןยยггยк๔ยןยค"
        + " к๔гยคยןгยкгเг๔кгкггยกยгยยยкกгยคгђคкгเק๔ยยשןยק๔เ",
    "๒ןยคкเ๒єเคггђןђคгคคє๔гשђן๔гคггкยןยยггยץչк๔ยןยคк๔гยคยןгย๔гђค๔คгןคยค๒ยкєקคкгк๔ןยץչย"
        + "ггยк๔ยןยคк๔гยคยןгยкקгץչคгђקภђђשкђђן๔гเกเรкєк๔เย๒קคเค๒кเ๔гคยเкггย๔คยเгггคк๔เ๔гเยยก๔๔เггкг"
        + "гคยย๔เยкยкเคгคкเยฬђєยг๔гยשкค๔гкคยкђгยคгคгкฬคкเггเг๔кгкггยกยгยקкภשєгץฬคเкฬђєש๔"
        + "ยгคє๒кเคгคкเยฬђєยг๔гยשкค๔гкคยк๔๔เเгคгђเเ๔คгкเยยкกгยคгђ๔гยשкค๔г"
  };

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Label translatingLabel;
  @FXML private Label hintLabel;
  @FXML private Label partsLabel;
  @FXML private ImageView alienHeadImage;

  /**
   * Initializes the chat view and sets up the GPT model. This method performs the following tasks:
   * - Configures the ChatCompletionRequest parameters. - Initializes the chatThread. - Compiles the
   * riddlePattern for pattern matching. - Sets randomSigns for use in the chat. - Sets hint and
   * parts counters. - Loads the alien head image. - Initializes the translation flag.
   */
  @FXML
  public void initialize() {
    // Initialize GPT request configuration
    flavourTxtChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

    chatThread = new Thread();

    // Regular expression pattern for detecting riddles
    riddlePattern = Pattern.compile("###((.|\n)+)###", Pattern.CASE_INSENSITIVE);

    // Random signs for creating alien-like text
    this.randomSigns = "ค๒ς๔єŦﻮђเןкɭ๓ภ๏קợгรՇยשฬאץչ";

    // Initialize hint and parts counters
    setHintCounter();
    setPartsCounter(0);

    // Load the alien head image
    setAlienHeadImage(new Image("/images/chatroom/alien.png"));

    // Initialize the translation flag
    isTranslating = false;
  }

  /**
   * Send a message to the GPT model when the user presses the enter key.
   *
   * @param event The key event triggered by the enter key.
   * @throws IOException if there is an I/O error.
   */
  @FXML
  public void onKeyPressed(KeyEvent event) throws IOException {
    switch (event.getCode()) {
      case ENTER:
        // Send message when enter key is pressed - makes it easier to send messages
        try {
          onSendMessage(new ActionEvent());
        } catch (ApiProxyException e) {
          System.out.println("API NOT WORKING");
        }
        break;
        // Otherwise, nothing happens
      default:
        break;
    }
  }

  /**
   * Set the alien head image.
   *
   * @param image The alien head image to set.
   */
  public void setAlienHeadImage(Image image) {
    alienHeadImage.setImage(image);
  }

  /**
   * Set the parts counter label.
   *
   * @param parts The number of parts found.
   */
  public void setPartsCounter(int parts) {
    partsLabel.setText("Parts Found: " + parts + "/3");
  }

  /** Set the hint counter label based on the game state. */
  public void setHintCounter() {
    if (GameState.hintsAllowed > 5) {
      hintLabel.setText("Unlimited");
    } else if (GameState.hintsAllowed == 0) {
      hintLabel.setText("None");
    } else {
      hintLabel.setText("5");
    }
  }

  /**
   * Send a user's request to the GPT model and handle the response.
   *
   * @param request The user's request.
   */
  public void askGpt(String request) {
    try {
      runGpt(new ChatMessage("user", request), GameState.getChatCompletionRequest());
    } catch (ApiProxyException e) {
      System.out.println("API NOT WORKING");
    }
  }

  /**
   * Get the translation flag.
   *
   * @return True if translating, false otherwise.
   */
  public boolean getIsTranslating() {
    return isTranslating;
  }

  /**
   * Append a chat message to the chat text area.
   *
   * @param msg The chat message to append.
   */
  public void appendChatMessage(String msg) {
    chatTextArea.setText(getAlienText(msg.length()));
    originalTransform(msg);
  }

  /**
   * Run the GPT model with a given chat message.
   *
   * @param msg The chat message to process.
   * @param chatCompletionRequest The chat completion request configuration.
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   */
  private void runGpt(ChatMessage msg, ChatCompletionRequest chatCompletionRequest)
      throws ApiProxyException {
    // If there is a chat thread already running, do nothing
    if (isTranslating) {
      return;
    }

    currRoom = GameState.getCurrRoom();
    GameState.toggleAlienTalking(currRoom);
    isTranslating = true;

    // Show thinking label and disable send button
    translatingLabel.setOpacity(100);
    animateWhileTranslating();
    inputText.setVisible(false);
    sendButton.setDisable(true);

    Timer myTimer = new Timer();

    // Set up task and thread to create changing alien text while GPT is thinking
    alienTextTask =
        new TimerTask() {
          @Override
          public void run() {
            String randomAlienText = getAlienText(-1);
            Platform.runLater(
                () -> {
                  chatTextArea.setText(randomAlienText);
                });
          }
        };

    myTimer.scheduleAtFixedRate(alienTextTask, 0, 200);

    // Set up task to run GPT model in a new thread
    Task<ChatMessage> chatTask =
        new Task<ChatMessage>() {
          @Override
          protected ChatMessage call() throws Exception {
            chatCompletionRequest.addMessage(msg);
            try {

              // Get the response from the GPT model and return it
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              // If the response is a riddle, set the riddle flag to true and extract the riddle
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              System.out.println("Error communicating with API proxy");
              return null;
            }
          }
        };

    // When the task is complete, check if the assistant has sent a message
    // starting with "Correct". Also, remove the thinking label and enable the send button.
    chatTask.setOnSucceeded(
        (event) -> {
          // Stop displaying fluctuating alien text
          myTimer.cancel();

          // Set random alien text to length of response, then start translating
          String gptResponse = chatTask.getValue().getContent();

          // Play notification sound, remove thinking label and enable send button
          GameMediaPlayer.playNotificationSound();
          sendButton.setDisable(false);

          // Use regex to see if response is a riddle
          Matcher matcher = riddlePattern.matcher(gptResponse);

          // If response is a riddle, extract the riddle and append to chat box
          if (matcher.find() && GameState.currRiddle == null) {
            String riddle = matcher.group(1).replace("###", "");
            GameState.currRiddle = riddle;
            appendChatMessage(
                riddle + "\nMake sure to tell the answer to me and not any other alien!");
          } else {
            if (checkCorrectAnswer(gptResponse)) {
              gptResponse =
                  gptResponse
                      + "\nA missing ship part is located where the answer to the riddle is!";
            }
            appendChatMessage(gptResponse);
          }
        });

    chatThread = new Thread(chatTask);
    chatThread.start();
  }

  /**
   * Send a message to the GPT model to say the flavor text of an object.
   *
   * @param object The object to say the flavor text of.
   */
  public void sayFlavourText(String object) {
    if (!GameState.isFlavourTextEnabled) {
      return;
    }

    try {
      runGpt(
          new ChatMessage("user", GptPromptEngineering.getFlavourText(object)),
          flavourTxtChatCompletionRequest);
    } catch (ApiProxyException e) {
      System.out.println("API NOT WORKING");
    }
  }

  /**
   * Handle sending a message to the GPT model when the user interacts with the chat interface.
   *
   * @param event The action event triggered by the send button.
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   * @throws IOException if there is an I/O error.
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();

    // Set alien head to the one that is speaking
    GameState.setAlienHead();

    ChatMessage msg;

    if (message.contains("tawlung seya")) {
      // User asks for hint
      hintTxtCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

      if (GameState.isHintAvailable()) {
        // Hints available
        GameState.hintsCounter++;
        message = GameState.getHint();

        if (GameState.hintsAllowed == 5) {
          hintLabel.setText("" + (5 - GameState.hintsCounter));
        }
      } else {
        // No hints available
        message = "Tell the user they are out of hints";
      }
      msg = new ChatMessage("user", message);
      runGpt(msg, hintTxtCompletionRequest);

    } else {
      // User talks to AI normally
      msg = new ChatMessage("user", message);
      runGpt(msg, GameState.getChatCompletionRequest());
    }
  }

  /**
   * Check if the assistant has sent a message starting with "Correct". If so, set the
   * isRiddleResolved flag to true.
   *
   * @param msg The chat message to check.
   */
  private boolean checkCorrectAnswer(String msg) {
    if (msg.toLowerCase().startsWith("correct")) {
      GameState.isRiddleResolved = true;
      return true;
    }

    return false;
  }

  /**
   * Create random alien text of a given length and return it. If given length is -1, alien text is
   * randomly chosen from pregenerated texts.
   *
   * @param length The length of the alien text to create.
   */
  private String getAlienText(int length) {
    Random random = new Random();

    // If length is -1, choose random alien text from array
    if (length == -1) {
      return randomAlienTexts[random.nextInt(randomAlienTexts.length)];
    }

    // Create random alien text of given length
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < length; i++) {

      // Randomly choose a character from randomSigns and append to string
      sb.append(randomSigns.charAt(random.nextInt(randomSigns.length())));
    }

    return sb.toString();
  }

  /**
   * Transform the chat text area to display the original response.
   *
   * @param gptResponse The response from the GPT model.
   */
  public void originalTransform(String gptResponse) {
    StringBuilder currentMessage = new StringBuilder(chatTextArea.getText());

    new Thread(
            () -> {
              for (int i = 0; i < gptResponse.length(); i++) {
                char originalChar = gptResponse.charAt(i);

                // Update the corresponding character in the current message
                currentMessage.setCharAt(i, originalChar);

                // Update the TextArea with the current message
                Platform.runLater(() -> chatTextArea.setText(currentMessage.toString()));

                try {
                  Thread.sleep(10); // Sleep for 0.04 seconds
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  return;
                }
              }

              translatingLabel.setOpacity(0);
              inputText.setVisible(true);
              isTranslating = false;
              GameState.toggleAlienTalking(currRoom);
              Thread.currentThread().interrupt();
              Platform.runLater(() -> inputText.requestFocus());
              return;
            })
        .start();
  }

  /** Animate the translating label while GPT is thinking. */
  private void animateWhileTranslating() {
    Task<Void> translating =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              while (isTranslating) {
                Platform.runLater(
                    () -> {
                      // Loading
                      translatingLabel.setText("Translating");
                    });
                Thread.sleep(300);
                Platform.runLater(
                    () -> {
                      // Loading.
                      translatingLabel.setText("Translating .");
                    });

                Thread.sleep(300);
                Platform.runLater(
                    () -> {
                      // Loading..
                      translatingLabel.setText("Translating . .");
                    });
                Thread.sleep(300);
                Platform.runLater(
                    () -> {
                      // Loading...
                      translatingLabel.setText("Translating . . .");
                    });
                Thread.sleep(300);
              }

            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return null;
            }
            return null;
          }
        };

    animationThread = new Thread(translating);
    animationThread.setDaemon(true);
    animationThread.start();
  }
}
