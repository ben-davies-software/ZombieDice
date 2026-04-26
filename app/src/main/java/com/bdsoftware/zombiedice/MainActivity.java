package com.bdsoftware.zombiedice;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.bdsoftware.zombiedice.ui.GameActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playButton);
        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        Button quitButton = findViewById(R.id.quitButton);

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        howToPlayButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("How To Play")
                    .setMessage(
                            "You are a zombie hungry for brains!\n\n" +
                                    "Each turn, draw 3 dice from the cup and roll them.\n\n" +
                                    "🟢 Green dice: more likely to show brains\n" +
                                    "🟡 Yellow dice: balanced odds\n" +
                                    "🔴 Red dice: more likely to show shotguns\n\n" +
                                    "BRAIN = collect it!\n" +
                                    "SHOTGUN = danger!\n" +
                                    "FOOTPRINT = reroll this die next turn\n\n" +
                                    "Get 3 shotguns and you lose all brains this turn.\n\n" +
                                    "Bank your brains to keep them safe.\n\n" +
                                    "First to 13 brains wins!"
                    )
                    .setPositiveButton("Got it!", null)
                    .show();
        });

        quitButton.setOnClickListener(v -> finishAffinity());
    }
}