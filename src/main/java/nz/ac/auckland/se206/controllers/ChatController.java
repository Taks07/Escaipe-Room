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
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Label translatingLabel;
  @FXML private Label hintLabel;

  private ChatCompletionRequest flavourTxtChatCompletionRequest;
  private ChatCompletionRequest hintTxtCompletionRequest;
  private Thread chatThread;
  private Pattern riddlePattern;
  private String randomSigns;
  private TimerTask alienTextTask;
  private boolean isTranslating;

  /** Initializes the chat view and sets up the GPT model. */
  @FXML
  public void initialize() {
    flavourTxtChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

    chatThread = new Thread();

    riddlePattern = Pattern.compile("###((.|\n)+)###", Pattern.CASE_INSENSITIVE);

    this.randomSigns =
        "\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u028B\u0E2C\u05D0\u05E5\u0579\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u05E9\u0E2C\u05D0\u05E5\u0579";

    setHintCounter();

    isTranslating = false;
  }

  public void setHintCounter() {
    // Set hint counter
    if (GameState.hintsAllowed > 5) {
      hintLabel.setText("Unlimited");
    } else if (GameState.hintsAllowed == 0) {
      hintLabel.setText("None");
    } else {
      hintLabel.setText("5");
    }
  }

  /** Asks the GPT model to a request, then appends it to the chatbox */
  public void askGPT(String request) {
    try {
      runGpt(new ChatMessage("user", request), GameState.getChatCompletionRequest());
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(String msg) {
    chatTextArea.setText(getAlienText(msg.length()));
    originalTransform(msg);
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg, ChatCompletionRequest chatCompletionRequest)
      throws ApiProxyException {
    // If there is a chat thread already running, do nothing
    if (isTranslating) {
      return;
    }

    // Check whether AI has been set up yet by user clicking on alien in room

    isTranslating = true;

    // Show thinking label and disable send button
    translatingLabel.setOpacity(100);
    translatingLabel.setText("Translating...");
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
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
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

          // Set random alien text to length of response, then start trnaslating
          String gptResponse = chatTask.getValue().getContent();

          // Play notification sound, remove thinking label and enable send button
          GameMediaPlayer.playNotificationSound();
          sendButton.setDisable(false);

          // Use regex to see if response is a riddle
          Matcher matcher = riddlePattern.matcher(gptResponse);

          System.out.println(gptResponse);

          // If response is a riddle, extract the riddle and append to chat box
          if (matcher.find() && GameState.currRiddle == null) {
            String riddle = matcher.group(1);
            GameState.currRiddle = riddle;
            appendChatMessage(riddle);
          } else {
            appendChatMessage(gptResponse);
            checkCorrectAnswer(gptResponse);
          }

          isTranslating = false;
        });

    chatThread = new Thread(chatTask);
    chatThread.start();
  }

  public void sayFlavourText(String object) {
    try {
      runGpt(
          new ChatMessage("user", GptPromptEngineering.getFlavourText(object)),
          flavourTxtChatCompletionRequest);
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();

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
        // No hints avaialble
        message = "Tell the user they are out of hints";
      }
      msg = new ChatMessage("user", message);
      runGpt(msg, hintTxtCompletionRequest);

    } else {
      // User talks to AI normally
      msg = new ChatMessage("user", message);
      // appendChatMessage(msg);
      runGpt(msg, GameState.getChatCompletionRequest());
    }
  }

  /**
   * Checks if the assistant has sent a message starting with "Correct". If so, sets the
   * isRiddleResolved flag to true.
   *
   * @param msg the chat message to check
   */
  private void checkCorrectAnswer(String msg) {
    if (msg.startsWith("Correct")) {
      GameState.isRiddleResolved = true;
    }
  }

  /**
   * Creates random alien text of a given length and returns it. If given length is -1, alien text
   * is 10 to 25 chars long
   */
  private String getAlienText(int length) {
    Random random = new Random();

    if (length == -1) {
      length = random.nextInt(300) + 20;
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < length; i++) {
      sb.append(randomSigns.charAt(random.nextInt(randomSigns.length())));
    }

    return sb.toString();
  }

  public void originalTransform(String gptResponse) {
    StringBuilder currentMessage = new StringBuilder(chatTextArea.getText());
    translatingLabel.setText("Translating...");

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
                  e.printStackTrace();
                }
              }
              translatingLabel.setOpacity(0);
              inputText.setVisible(true);
            })
        .start();
  }
}
