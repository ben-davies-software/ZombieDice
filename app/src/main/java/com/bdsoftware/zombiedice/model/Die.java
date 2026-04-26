package com.bdsoftware.zombiedice.model;

public class Die {

    public enum Colour {
        GREEN, YELLOW, RED
    }

    public enum Face {
        BRAIN, FOOTPRINT, SHOTGUN
    }

    private final Colour colour;
    private Face currentFace;

    // Each die has a weighted array of faces based on its colour
    private final Face[] faces;

    public Die(Colour colour) {
        this.colour = colour;

        switch (colour) {
            case GREEN:
                // 3 brains, 2 footprints, 1 shotgun
                faces = new Face[]{
                        Face.BRAIN, Face.BRAIN, Face.BRAIN,
                        Face.FOOTPRINT, Face.FOOTPRINT,
                        Face.SHOTGUN
                };
                break;
            case YELLOW:
                // 2 brains, 2 footprints, 2 shotguns
                faces = new Face[]{
                        Face.BRAIN, Face.BRAIN,
                        Face.FOOTPRINT, Face.FOOTPRINT,
                        Face.SHOTGUN, Face.SHOTGUN
                };
                break;
            case RED:
            default:
                // 1 brain, 2 footprints, 3 shotguns
                faces = new Face[]{
                        Face.BRAIN,
                        Face.FOOTPRINT, Face.FOOTPRINT,
                        Face.SHOTGUN, Face.SHOTGUN, Face.SHOTGUN
                };
                break;
        }

        currentFace = Face.FOOTPRINT;
    }

    // Roll the die by picking a random face
    public void roll() {
        int index = (int) (Math.random() * faces.length);
        currentFace = faces[index];
    }

    public Face getCurrentFace() {
        return currentFace;
    }

    public Colour getColour() {
        return colour;
    }
}