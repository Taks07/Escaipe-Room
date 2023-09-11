package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
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

  private ChatCompletionRequest mainChatCompletionRequest;
  private ChatCompletionRequest flavourTxtChatCompletionRequest;
  private Thread chatThread;
  private Pattern riddlePattern;
  private Timeline randomTransformTimeline;
  private Pattern hintPattern;

  String input = "";
  RandomSignConverter signConverter;

  /** Initializes the chat view and sets up the GPT model. */
  @FXML
  public void initialize() {
    mainChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);
    flavourTxtChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

    chatThread = new Thread();

    riddlePattern = Pattern.compile("###((.|\n)+)###", Pattern.CASE_INSENSITIVE);
    hintPattern = Pattern.compile("&&&((.|\n)+)&&&", Pattern.CASE_INSENSITIVE);
  }

  /** Asks the GPT model to a request, then appends it to the chatbox */
  public void askGPT(String request) {
    try {
      runGpt(new ChatMessage("user", request), mainChatCompletionRequest);
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(ChatMessage msg) {
    String role = msg.getRole();
    if (role.equals("user")) {
      chatTextArea.appendText("You: " + msg.getContent() + "\n\n");
    } else {
      // TODO: CONVERT THE RANDOM STRING OF LENGTH GPTRESPONSE TO THE ORIGINAL STRING HERE

    }
    ;
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
    if (chatThread.isAlive()) {
      return;
    }

    // Show thinking label and disable send button
    translatingLabel.setOpacity(100);
    translatingLabel.setText("Processing...");
    sendButton.setDisable(true);
    signConverter = new RandomSignConverter(input, chatTextArea);

    // Start the random string loading sequence and store the timeline
    randomTransformTimeline = new Timeline();
    signConverter.randomTransform(chatTextArea, randomTransformTimeline);

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
          // Stop the randomTransform animation
          randomTransformTimeline.stop();

          // Play notification sound, remove thinking label and enable send button
          GameMediaPlayer.playNotificationSound();
          sendButton.setDisable(false);

          // Use regex to see if response is a riddle
          String gptResponse = chatTask.getValue().getContent();
          Matcher matcher = riddlePattern.matcher(gptResponse);

          // Call the correctLength method with the textArea and input
          signConverter.correctLength(chatTextArea, gptResponse);

          System.out.println(gptResponse);

          // If response is a riddle, extract the riddle and append to chat box
          if (matcher.find() && GameState.currRiddle == null) {
            String riddle = matcher.group(1);
            ChatMessage riddleMsg = new ChatMessage("assistant", riddle);
            GameState.currRiddle = riddle;
            appendChatMessage(riddleMsg);
          } else {
            matcher = hintPattern.matcher(gptResponse);

            // If response is not a hint, append to chat box
            if (!matcher.find()) {
              appendChatMessage(chatTask.getValue());
              checkCorrectAnswer(chatTask.getValue());
            } else {
              // If response is hint, and there are still hints allowed, append to chat box
              if (GameState.isHintAvailable()) {
                appendChatMessage(chatTask.getValue());
                GameState.hintsCounter++;
              } else {
                // TODO: Might mess with GPT doing this. If there are problems with GPT later,
                // change implementation
                askGPT("Tell the user you can't give any hints");
              }
            }
          }
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
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg, mainChatCompletionRequest);
  }

  /**
   * Checks if the assistant has sent a message starting with "Correct". If so, sets the
   * isRiddleResolved flag to true.
   *
   * @param msg the chat message to check
   */
  private void checkCorrectAnswer(ChatMessage msg) {
    if (msg.getRole().equals("assistant") && msg.getContent().startsWith("Correct")) {
      GameState.isRiddleResolved = true;
    }
  }

  /**
   * This class is used to convert a string into a string of random characters. It also has a method
   * to transform the string into the original string.
   */
  public class RandomSignConverter {
    String converted = "";
    String randomSigns;
    String input = "";
    TextArea textArea;

    public RandomSignConverter(String input, TextArea textArea) {
      this.textArea = textArea;
      this.input = input;

      this.randomSigns =
          "\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u028B\u0E2C\u05D0\u05E5\u0579\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u05E9\u0E2C\u05D0\u05E5\u0579";
      textArea.setText(converted);
    }

    /**
     * Transforms the alien string back into its original form and displays it in the TextField with
     * each letter taking 0.06 seconds to transform.
     *
     * @param word The original string.
     */
    public void randomTransform(TextArea textArea, Timeline timeline) {

      sendButton.setVisible(false);
      StringBuilder currentMessage = new StringBuilder(converted);
      int interval = 10; // 0.04 seconds in milliseconds

      timeline.getKeyFrames().clear(); // Clear existing key frames to stop the animation

      KeyFrame keyFrame =
          new KeyFrame(
              Duration.millis(interval),
              event -> {
                Random random = new Random();

                // Generate a random number between 0 and 2
                int randomNumber = random.nextInt(3);

                if (randomNumber == 0 && currentMessage.length() > 20) {
                  // Delete a character from the current message
                  currentMessage.deleteCharAt(random.nextInt(currentMessage.length()));
                } else {
                  // Add a random character to the current message
                  char randomChar = randomSigns.charAt(random.nextInt(randomSigns.length()));
                  currentMessage.append(randomChar);
                }

                // Update the TextArea with the current message
                Platform.runLater(() -> textArea.setText(currentMessage.toString()));
              });

      timeline.getKeyFrames().add(keyFrame); // Add the new key frame to the timeline
      timeline.setCycleCount(1000);
      timeline.play();
    }

    public void correctLength(TextArea textArea, String input) {
      int length = textArea.getLength();
      int inputLength = input.length();
      Random random = new Random();

      new Thread(
              () -> {
                if (length < inputLength) {
                  // Append a random character from randomSigns to the TextArea
                  char randomChar = randomSigns.charAt(random.nextInt(randomSigns.length()));

                  Platform.runLater(() -> textArea.appendText(String.valueOf(randomChar)));

                  try {
                    Thread.sleep(10); // Sleep for 0.06 seconds
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }

                  correctLength(textArea, input); // Call the method again
                } else if (length > inputLength) {
                  // Delete one character from the end of the TextArea
                  int caretPosition = textArea.getLength();
                  if (caretPosition > 0) {

                    Platform.runLater(
                        () -> {
                          String currentText = textArea.getText();

                          // Check if the TextArea has content
                          if (!currentText.isEmpty()) {
                            // Remove the last character
                            String updatedText = currentText.substring(0, currentText.length() - 1);

                            // Set the updated text back to the TextArea
                            textArea.setText(updatedText);
                          }
                        });

                    try {
                      Thread.sleep(10); // Sleep for 0.06 seconds
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    }

                    correctLength(textArea, input); // Call the method again
                  }

                } else {
                  Platform.runLater(() -> translatingLabel.setText("Translating..."));
                  originalTransform(input);
                  return; // Length matches input length, stop recursion
                }
              })
          .start();
    }

    public void originalTransform(String word) {
      StringBuilder currentMessage = new StringBuilder(textArea.getText());

      new Thread(
              () -> {
                for (int i = 0; i < word.length(); i++) {
                  char originalChar = word.charAt(i);

                  // Update the corresponding character in the current message
                  currentMessage.setCharAt(i, originalChar);

                  // Update the TextArea with the current message
                  Platform.runLater(() -> textArea.setText(currentMessage.toString()));

                  try {
                    Thread.sleep(10); // Sleep for 0.04 seconds
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
                sendButton.setVisible(true);
                translatingLabel.setOpacity(0);
              })
          .start();
    }
  }
}
