package com.bdsoftware.zombiedice.model;

public class Player {

    private final String name;
    private int totalScore;
    private int turnBrains;
    private int turnShotguns;

    public Player(String name) {
        this.name = name;
        this.totalScore = 0;
        this.turnBrains = 0;
        this.turnShotguns = 0;
    }

    // Called when a brain is rolled this turn
    public void addBrain() {
        turnBrains++;
    }

    // Called when a shotgun is rolled this turn
    public void addShotgun() {
        turnShotguns++;
    }

    // Bank the turn brains into the total score
    public void bankBrains() {
        totalScore += turnBrains;
        resetTurn();
    }

    // Lose all turn brains if 3 shotguns are accumulated
    public void bustTurn() {
        resetTurn();
    }

    // Reset turn counters ready for the next turn
    public void resetTurn() {
        turnBrains = 0;
        turnShotguns = 0;
    }

    public boolean isBust() {
        return turnShotguns >= 3;
    }

    public boolean hasWon() {
        return totalScore >= 13;
    }

    public String getName() {
        return name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTurnBrains() {
        return turnBrains;
    }

    public int getTurnShotguns() {
        return turnShotguns;
    }
}
