package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
  @FXML private Label thinkingLabel;

  private ChatCompletionRequest mainChatCompletionRequest;
  private ChatCompletionRequest flavourTxtChatCompletionRequest;
  private Thread chatThread;
  private Pattern riddlePattern;

  /** Initializes the chat view and sets up the GPT model. */
  @FXML
  public void initialize() {
    mainChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);
    flavourTxtChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.5).setMaxTokens(100);

    chatThread = new Thread();

    riddlePattern = Pattern.compile("###((.|\n)+)###", Pattern.CASE_INSENSITIVE);
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
      chatTextArea.appendText("AI System: " + msg.getContent() + "\n\n");
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
    thinkingLabel.setOpacity(100);
    sendButton.setDisable(true);

    // Set up task to run GPT model in new thread
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
              // TODO handle exception appropriately
              System.out.println("Error communicating with API proxy");
              return null;
            }
          }
        };

    // When task is complete, check if the assistant has sent a message
    // starting with "Correct". Also, remove thinking label and enable send button.
    chatTask.setOnSucceeded(
        (event) -> {
          // Play notification sound, remove thinking label and enable send button
          GameMediaPlayer.playNotificationSound();
          thinkingLabel.setOpacity(0);
          sendButton.setDisable(false);

          // Use regex to see if response is a riddle
          String gptResponse = chatTask.getValue().getContent();
          Matcher matcher = riddlePattern.matcher(gptResponse);

          System.out.println(gptResponse);

          // If response is a riddle, extract the riddle and append to chat box
          if (matcher.find()) {
            String riddle = matcher.group(1);
            ChatMessage riddleMsg = new ChatMessage("assistant", riddle);
            GameState.currRiddle = riddle;
            appendChatMessage(riddleMsg);
          } else {
            appendChatMessage(chatTask.getValue());
            checkCorrectAnswer(chatTask.getValue());
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
}
