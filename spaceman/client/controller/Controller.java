package spaceman.client.controller;

/**
 * Controller class for the Spaceman game, following the MVC pattern.
 */
public interface Controller {

    /**
     * Initializes and starts the user interface.
     */
    void start();

    /**
     * Performs the actions that follows a forfeit.
     */
    void forfeit();

    /**
     * Performs the actions that follow requesting a new game .
     */
    void newGame();

    /**
     * Performs the actions that follow requesting a new game with a set word.
     *
     * @param wordToGuess the word that must be guessed by the player in the new game
     */
    void newGame(String wordToGuess);

    /**
     * Performs the actions that follow a player's character guess.
     *
     * @param guessedCharacter the character input made by the player
     */
    void madeGuess(String guessedCharacter);

    /**
     * Performs the actions that follow a player requesting to go to the dialogue for creating a new
     * singleplayer game.
     */
    void requestedSinglePlayerMode();

    /**
     * Performs the actions that follow a player requesting to go to the dialogue for creating a new
     * multiplayer game.
     */
    void requestedMultiPlayerMode();

    /**
     * Perfroms the actions that follow a player requesting to start a new multiplayer game at the
     * given server addess.
     *
     * @param userName      The desired user name
     * @param serverAddress The address of the game server
     */
    void startNewMultiplayerGame(String userName, String serverAddress);

    /**
     * Perfroms the actions that follow a player requesting to join a new multiplayer game at the
     * given server addess with the given id.
     *
     * @param userName      The desired user name
     * @param serverAddress The address of the game server
     * @param gameId        The id of the game to join
     */
    void joinMultiplayerGame(String userName, String serverAddress, String gameId);

    /**
     * Performs the actions that follow a player requesting to start a new game.
     */
    void requestNewGame();

    /**
     * Performs the actions that follow a player requesting to exit the game.
     */
    void closeWindow();
}
