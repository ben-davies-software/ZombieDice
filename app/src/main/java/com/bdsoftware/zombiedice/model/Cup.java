package com.bdsoftware.zombiedice.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cup {

    private List<Die> dice;

    public Cup() {
        reset();
    }

    // Build the full set of 13 dice: 6 green, 4 yellow, 3 red
    public void reset() {
        dice = new ArrayList<>();

        for (int i = 0; i < 6; i++) dice.add(new Die(Die.Colour.GREEN));
        for (int i = 0; i < 4; i++) dice.add(new Die(Die.Colour.YELLOW));
        for (int i = 0; i < 3; i++) dice.add(new Die(Die.Colour.RED));

        Collections.shuffle(dice);
    }

    // Draw up to 3 dice from the cup
    public List<Die> draw(int count) {
        List<Die> drawn = new ArrayList<>();
        for (int i = 0; i < count && !dice.isEmpty(); i++) {
            drawn.add(dice.remove(0));
        }
        return drawn;
    }

    // Return footprint dice to the cup and reshuffle
    public void returnDice(List<Die> returned) {
        dice.addAll(returned);
        Collections.shuffle(dice);
    }

    public int remaining() {
        return dice.size();
    }

    public boolean isEmpty() {
        return dice.isEmpty();
    }
}