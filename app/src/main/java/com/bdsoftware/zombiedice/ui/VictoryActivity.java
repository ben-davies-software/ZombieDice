package com.bdsoftware.zombiedice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bdsoftware.zombiedice.MainActivity;
import com.bdsoftware.zombiedice.R;

public class VictoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        // Retrieve data passed from GameActivity
        String winner = getIntent().getStringExtra("winner");
        int humanScore = getIntent().getIntExtra("humanScore", 0);
        int aiScore = getIntent().getIntExtra("aiScore", 0);

        TextView victoryEmoji = findViewById(R.id.victoryEmoji);
        TextView victoryTitle = findViewById(R.id.victoryTitle);
        TextView victoryMessage = findViewById(R.id.victoryMessage);
        TextView finalHumanScore = findViewById(R.id.finalHumanScore);
        TextView finalAiScore = findViewById(R.id.finalAiScore);
        Button playAgainButton = findViewById(R.id.playAgainButton);
        Button menuButton = findViewById(R.id.menuButton);

        // Set content based on who won
        if ("You".equals(winner)) {
            victoryEmoji.setText("🏆");
            victoryTitle.setText("VICTORY!");
            victoryTitle.setTextColor(0xFF7CFC00);
            victoryMessage.setText("You collected 13 brains!\nThe zombies bow to you.");
        } else {
            victoryEmoji.setText("💀");
            victoryTitle.setText("DEFEATED!");
            victoryTitle.setTextColor(0xFFcc0000);
            victoryMessage.setText("Zombie AI collected 13 brains!\nBetter luck next time.");
        }

        finalHumanScore.setText(String.valueOf(humanScore));
        finalAiScore.setText(String.valueOf(aiScore));

        // Play Again starts a fresh GameActivity
        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(VictoryActivity.this, GameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Main Menu returns to the title screen
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(VictoryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}