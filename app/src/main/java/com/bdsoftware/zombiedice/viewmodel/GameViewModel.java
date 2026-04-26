package com.bdsoftware.zombiedice.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zombiedice.model.Cup;
import com.example.zombiedice.model.Die;
import com.example.zombiedice.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {

    // Win condition
    public static final int WINNING_SCORE = 13;

    // Players
    private Player humanPlayer;
    private Player aiPlayer;

    // Cup of dice
    private Cup cup;

    // The three dice currently in play
    private List<Die> currentDice;

    // Footprint dice held over for the next roll
    private List<Die> heldFootprints;

    // Tracks whose turn it is
    private boolean isHumanTurn;

    // LiveData observed by the UI
    private final MutableLiveData<List<Die>> rolledDice = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> humanTurnActive = new MutableLiveData<>();
    private final MutableLiveData<String> winner = new MutableLiveData<>();
    private final MutableLiveData<int[]> scores = new MutableLiveData<>();

    public GameViewModel() {
        startNewGame();
    }

    // Initialise everything for a fresh game
    public void startNewGame() {
        humanPlayer = new Player("You");
        aiPlayer = new Player("Zombie AI");
        cup = new Cup();
        currentDice = new ArrayList<>();
        heldFootprints = new ArrayList<>();
        isHumanTurn = true;
        winner.setValue(null);
        updateScores();
        statusMessage.setValue("Press Roll to start your turn!");
        humanTurnActive.setValue(true);
    }

    // Called when the Roll button is pressed
    public void rollDice() {
        Player current = isHumanTurn ? humanPlayer : aiPlayer;

        // Work out how many new dice we need to draw
        int drawCount = 3 - heldFootprints.size();
        List<Die> newDice = cup.draw(drawCount);

        // If the cup ran out, return non-footprint held dice and redraw
        if (newDice.size() < drawCount) {
            cup.reset();
            newDice.addAll(cup.draw(drawCount - newDice.size()));
        }

        // Combine held footprints with newly drawn dice
        currentDice = new ArrayList<>(heldFootprints);
        currentDice.addAll(newDice);
        heldFootprints.clear();

        // Roll each die and process the result
        for (Die die : currentDice) {
            die.roll();
            switch (die.getCurrentFace()) {
                case BRAIN:
                    current.addBrain();
                    break;
                case SHOTGUN:
                    current.addShotgun();
                    break;
                case FOOTPRINT:
                    heldFootprints.add(die);
                    break;
            }
        }

        rolledDice.setValue(currentDice);
        updateScores();

        // Check for bust
        if (current.isBust()) {
            handleBust(current);
            return;
        }

        statusMessage.setValue(current.getName() + " rolled: "
                + current.getTurnBrains() + " brain(s), "
                + current.getTurnShotguns() + " shotgun(s) this turn.");
    }

    // Called when the Bank button is pressed
    public void bankBrains() {
        Player current = isHumanTurn ? humanPlayer : aiPlayer;
        current.bankBrains();
        updateScores();

        // Check for win condition
        if (current.hasWon()) {
            winner.setValue(current.getName());
            return;
        }

        endTurn();
    }

    // Handle a bust (3 shotguns)
    private void handleBust(Player current) {
        current.bustTurn();
        updateScores();
        statusMessage.setValue(current.getName() + " got 3 shotguns! No brains scored this turn.");
        endTurn();
    }

    // Switch turns and reset the cup state for the next player
    private void endTurn() {
        cup = new Cup();
        heldFootprints.clear();
        currentDice.clear();
        isHumanTurn = !isHumanTurn;
        humanTurnActive.setValue(isHumanTurn);

        if (isHumanTurn) {
            statusMessage.setValue("Your turn! Press Roll.");
        } else {
            statusMessage.setValue("Zombie AI is thinking...");
        }
    }

    // AI decision logic: roll again if fewer than 2 shotguns and fewer than 3 brains banked
    public boolean aiShouldRollAgain() {
        return aiPlayer.getTurnShotguns() < 2 && aiPlayer.getTurnBrains() < 3;
    }

    // Push updated scores to the UI
    private void updateScores() {
        scores.setValue(new int[]{
                humanPlayer.getTotalScore(),
                humanPlayer.getTurnBrains(),
                humanPlayer.getTurnShotguns(),
                aiPlayer.getTotalScore(),
                aiPlayer.getTurnBrains(),
                aiPlayer.getTurnShotguns()
        });
    }

    // LiveData getters
    public LiveData<List<Die>> getRolledDice() { return rolledDice; }
    public LiveData<String> getStatusMessage() { return statusMessage; }
    public LiveData<Boolean> getHumanTurnActive() { return humanTurnActive; }
    public LiveData<String> getWinner() { return winner; }
    public LiveData<int[]> getScores() { return scores; }
    public boolean isHumanTurn() { return isHumanTurn; }
}
