package spaceman;

import spaceman.sharedmodel.GuessChar;
import spaceman.sharedmodel.Phase;
import spaceman.sharedmodel.SingleplayerSpaceman;
import spaceman.sharedmodel.Spaceman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A Shell interface for a Spaceman game. This class handles user input and displays the game state,
 * as provided by a Spaceman instance to the user.
 */
public class Shell {

  private static final String[] SAUCER_STEPS = {
    // 1.:
    "        _.---._\n" + "      .'       '.",
    // 2.:
    "  _.-~===========~-._",
    // 3.:
    " (___________________)",
    // 4.:
    "       \\_______/",
    // 5.:
    "        |     |",
    // 6.:
    "        |_0/  |",
    // 7.:
    "        |  \\  |\n" + "        |  /\\ |"
  };

  private static final String PROMPT = "SP> ";
  private static final String WIN_MESSAGE = "Congratulations, you win!";
  private Spaceman game;

  /**
   * Read and process input until the quit command has been entered.
   *
   * @param args Command line arguments.
   * @throws IOException Error reading from stdin.
   */
  public static void main(String[] args) throws IOException {
    final Shell shell = new Shell();
    shell.run();
  }

  /**
   * Run the spaceman shell. Shows prompt 'SP> ', takes commands from the user and executes them.
   */
  public void run() throws IOException {
    BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    boolean quit = false;

    while (!quit) {
      System.out.print(PROMPT);

      String input = in.readLine();
      if (input == null) {
        break;
      }

      String[] tokens = input.trim().split("\\s+");

      if (tokens.length == 0) {
        displayError("No Command given");
        continue;
      }

      String command = tokens[0];
      String[] arguments = Arrays.copyOfRange(tokens, 1, tokens.length);

      switch (command.toUpperCase()) {
        case "NEWGAME":
          handleNewgameCommand(arguments);
          break;
        case "GUESS":
          handleGuessCommand(arguments);
          break;
        case "DISPLAY":
          handleDisplayCommand(arguments);
          break;
        case "FORFEIT":
          handleForfeitCommand(arguments);
          break;
        case "QUIT":
          if (arguments.length != 0) {
            displayError("Invalid arguments");
            continue;
          }
          quit = true;
          break;
        default:
          displayError("Unknown command");
      }
    }
  }

  private void handleNewgameCommand(String[] arguments) {
    if (arguments.length == 0) {
      game = SingleplayerSpaceman.create();
    } else {
      String word = String.join(" ", arguments);
      game = SingleplayerSpaceman.create(word);
    }

    displayCurrentWord();
  }

  private void handleGuessCommand(String[] arguments) {
    if (arguments.length != 1) {
      displayError("Invalid arguments");
      return;
    }

    String guess = arguments[0];
    if (guess.length() != 1) {
      displayError("Invalid arguments");
      return;
    }

    if (!isGameRunning()) {
      displayError("No game running.");
      return;
    }

    int wrongTriesBefore = game.getState().getCountdownValue();
    game.guess(guess.charAt(0));
    boolean isGuessCorrect = wrongTriesBefore == game.getState().getCountdownValue();

    displayCurrentSaucer();
    displayCurrentWord();

    if (isGuessCorrect && !isGameRunning()) {
      // last guess won the game
      System.out.println(WIN_MESSAGE);
    }
  }

  private void handleDisplayCommand(String[] arguments) {
    if (arguments.length != 0) {
      displayError("Invalid arguments");
      return;
    }

    if (!isGameRunning()) {
      displayError("No game running.");
      return;
    }

    displayCurrentSaucer();
    displayCurrentWord();
  }

  private void handleForfeitCommand(String[] arguments) {
    if (arguments.length != 0) {
      displayError("Invalid arguments");
      return;
    }

    if (!isGameRunning()) {
      displayError("No game running.");
      return;
    }

    game.forfeit();

    displayCurrentSaucer();
    displayCurrentWord();
  }

  private boolean isGameRunning() {
    return game != null && game.getState().getCurrentPhase() == Phase.RUNNING;
  }

  private void displayError(String message) {
    System.out.println("Error! " + message);
  }

  private void displayCurrentWord() {
    List<GuessChar> characters = game.getState().getWord().getCharacters();

    System.out.print(": ");
    for (GuessChar guessChar : characters) {
      Optional<Character> maybeCharacter = guessChar.maybeGetCharacter();
      if (maybeCharacter.isPresent()) {
        System.out.print(maybeCharacter.get() + " ");
      } else {
        System.out.print("_ ");
      }
    }
    System.out.println(":");
  }

  private void displayCurrentSaucer() {
    int stepsToShow =
        game.getState().getMaximumCountdownValue() - game.getState().getCountdownValue();
    for (int i = 0; i < stepsToShow; i++) {
      System.out.println(SAUCER_STEPS[i]);
    }
  }
}
