package spaceman.client.controller;

import spaceman.client.model.MultiplayerSpacemanClient;
import spaceman.client.view.SwingGui;
import spaceman.sharedmodel.Phase;
import spaceman.sharedmodel.SingleplayerSpaceman;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for {@link SwingGui}, according to MVC pattern.
 */
public class SwingController implements Controller {

    private Spaceman model;
    private SwingGui view;

    @Override
    public void start() {
        view = new SwingGui(this);
        view.makeVisible();
    }

    @Override
    public void forfeit() {
        if (!isGameRunning()) {
            throw new IllegalStateException("Calling forfeit while no game is running");
        }
        final SwingWorker<Void, Void> doForfeit =
                new SwingWorker<>() {

                    @Override
                    protected Void doInBackground() {
                        model.forfeit();
                        return null;
                    }
                };
        doForfeit.execute();
    }

    @Override
    public void newGame() {
        model = SingleplayerSpaceman.create();
        registerModelAtView(model);
    }

    @Override
    public void newGame(String userInput) {
        String givenWord = userInput.strip();
        if (givenWord.length() <= 0) {
            view.showError(
                    "Expected a word of at least one non-space character. Please correct your input.");
            return;
        }

        model = SingleplayerSpaceman.create(givenWord);
        registerModelAtView(model);
    }

    private void registerModelAtView(Spaceman model) {
        Objects.requireNonNull(model, "Trying to register with model == null");
        view.setGameScreen(model);
    }

    @Override
    public void madeGuess(String userInput) {
        if (!isGameRunning()) {
            throw new IllegalStateException("Calling forfeit while no game is running");
        }
        if (userInput.length() != 1) {
            view.showError("Expected exactly one character for guess. Please correct your input.");
            return;
        }

        final char guessedCharacter = getOnlyChar(userInput);
        final SwingWorker<Void, Void> doGuess =
                new SwingWorker<>() {

                    @Override
                    protected Void doInBackground() {
                        model.guess(guessedCharacter);
                        return null; // necessary because of Void type
                    }
                };
        doGuess.execute();
    }

    private char getOnlyChar(String userInput) {
        assert userInput.length() == 1 : "User input not exactly one character";
        return userInput.charAt(0);
    }

    private boolean isGameRunning() {
        return model.getState().getCurrentPhase() == Phase.RUNNING;
    }

    @Override
    public void requestedSinglePlayerMode() {
        view.setNewSinglePlayerGameScreen();
    }

    @Override
    public void requestedMultiPlayerMode() {
        view.setNewMultiPlayerGameScreen();
    }

    @Override
    public void startNewMultiplayerGame(String userName, String serverAddress) {
        view.showLoadingScreen();
        final SwingWorker<Void, Void> connect =
                new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            MultiplayerSpacemanClient multiplayerModel = MultiplayerSpacemanClient.create();
                            multiplayerModel.newGame(userName, serverAddress);
                            model = multiplayerModel;
                            registerModelAtView(model);
                        } catch (IOException e) {
                            e.printStackTrace();
                            view.showError("Could not establish a connection.");
                        } catch (MultiplayerSpacemanClient.DuplicatePlayerNameException e) {
                            view.showDuplicatePlayerNameMessage();
                        }

                        return null;
                    }

                    @Override
                    protected void done() {
                        view.hideLoadingScreen();
                    }
                };
        connect.execute();
    }

    @Override
    public void joinMultiplayerGame(String userName, String serverAddress, String gameId) {
        view.showLoadingScreen();
        final SwingWorker<Void, Void> connect =
                new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        int gameIdInt = -1;
                        try {
                            gameIdInt = Integer.parseInt(gameId);
                        } catch (NumberFormatException e) {
                            SwingUtilities.invokeLater(
                                    () -> view.showError("Invalid game id. Number is required!"));
                            return null;
                        }
                        try {
                            MultiplayerSpacemanClient multiplayerModel = MultiplayerSpacemanClient.create();
                            multiplayerModel.joinGame(userName, serverAddress, gameIdInt);
                            model = multiplayerModel;
                            registerModelAtView(model);
                        } catch (IOException e) {
                            e.printStackTrace();
                            view.showError("Could not establish a connection.");
                        } catch (MultiplayerSpacemanClient.UnknownGameIdException e) {
                            view.showUnknownGameIdMessage();
                        } catch (MultiplayerSpacemanClient.DuplicatePlayerNameException e) {
                            view.showDuplicatePlayerNameMessage();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        view.hideLoadingScreen();
                    }
                };
        connect.execute();
    }

    @Override
    public void requestNewGame() {
        if (model instanceof MultiplayerSpacemanClient) {
            final SwingWorker<Void, Void> doClose =
                    new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                ((MultiplayerSpacemanClient) model).close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            model = null;
                            view.setWelcomeScreen();
                        }
                    };
            doClose.execute();
        } else {
            requestedSinglePlayerMode();
        }
    }

    @Override
    public void closeWindow() {
        if (model instanceof MultiplayerSpacemanClient) {
            final SwingWorker<Void, Void> doClose =
                    new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                ((MultiplayerSpacemanClient) model).close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
            doClose.execute();
        }
    }
}
