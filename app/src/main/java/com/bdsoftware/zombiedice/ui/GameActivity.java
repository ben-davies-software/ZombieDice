package com.bdsoftware.zombiedice.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.zombiedice.R;
import com.example.zombiedice.model.Die;
import com.example.zombiedice.viewmodel.GameViewModel;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private GameViewModel viewModel;

    private TextView statusMessage;
    private TextView humanTotalScore;
    private TextView humanTurnInfo;
    private TextView aiTotalScore;
    private TextView aiTurnInfo;

    private ImageView die1;
    private ImageView die2;
    private ImageView die3;

    private Button rollButton;
    private Button bankButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialise ViewModel
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Bind views
        statusMessage = findViewById(R.id.statusMessage);
        humanTotalScore = findViewById(R.id.humanTotalScore);
        humanTurnInfo = findViewById(R.id.humanTurnInfo);
        aiTotalScore = findViewById(R.id.aiTotalScore);
        aiTurnInfo = findViewById(R.id.aiTurnInfo);
        die1 = findViewById(R.id.die1);
        die2 = findViewById(R.id.die2);
        die3 = findViewById(R.id.die3);
        rollButton = findViewById(R.id.rollButton);
        bankButton = findViewById(R.id.bankButton);

        // Observe LiveData from ViewModel
        viewModel.getStatusMessage().observe(this, msg -> statusMessage.setText(msg));

        viewModel.getScores().observe(this, s -> {
            humanTotalScore.setText(String.valueOf(s[0]));
            humanTurnInfo.setText("Brains: " + s[1] + "  Shotguns: " + s[2]);
            aiTotalScore.setText(String.valueOf(s[3]));
            aiTurnInfo.setText("Brains: " + s[4] + "  Shotguns: " + s[5]);
        });

        viewModel.getRolledDice().observe(this, dice -> {
            updateDiceImages(dice);
        });

        viewModel.getHumanTurnActive().observe(this, isHumanTurn -> {
            rollButton.setEnabled(isHumanTurn);
            bankButton.setEnabled(isHumanTurn);
            if (!isHumanTurn) {
                runAiTurn();
            }
        });

        viewModel.getWinner().observe(this, winner -> {
            if (winner != null) {
                showWinnerDialog(winner);
            }
        });

        // Button listeners
        rollButton.setOnClickListener(v -> viewModel.rollDice());
        bankButton.setOnClickListener(v -> viewModel.bankBrains());
    }

    // Update the three dice ImageViews based on roll results
    private void updateDiceImages(List<Die> dice) {
        ImageView[] dieViews = {die1, die2, die3};
        for (int i = 0; i < dice.size(); i++) {
            dieViews[i].setImageResource(getDieDrawable(dice.get(i)));
        }
    }

    // Return the correct drawable for a die based on its colour and face
    private int getDieDrawable(Die die) {
        Die.Colour colour = die.getColour();
        Die.Face face = die.getCurrentFace();

        if (colour == Die.Colour.GREEN) {
            switch (face) {
                case BRAIN:     return R.drawable.die_green_brain;
                case SHOTGUN:   return R.drawable.die_green_shotgun;
                default:        return R.drawable.die_green_footprint;
            }
        } else if (colour == Die.Colour.YELLOW) {
            switch (face) {
                case BRAIN:     return R.drawable.die_yellow_brain;
                case SHOTGUN:   return R.drawable.die_yellow_shotgun;
                default:        return R.drawable.die_yellow_footprint;
            }
        } else {
            switch (face) {
                case BRAIN:     return R.drawable.die_red_brain;
                case SHOTGUN:   return R.drawable.die_red_shotgun;
                default:        return R.drawable.die_red_footprint;
            }
        }
    }

    // Run the AI turn automatically with a short delay so the player can follow it
    private void runAiTurn() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!viewModel.isHumanTurn()) {
                    viewModel.rollDice();

                    // Check if AI should roll again or bank
                    handler.postDelayed(() -> {
                        if (!viewModel.isHumanTurn()) {
                            if (viewModel.aiShouldRollAgain()) {
                                runAiTurn();
                            } else {
                                viewModel.bankBrains();
                            }
                        }
                    }, 1200);
                }
            }
        }, 1000);
    }

    // Show a dialog when the game is won
    private void showWinnerDialog(String winner) {
        String message = winner.equals("You") ?
                "You collected 13 brains! You win!" :
                "Zombie AI collected 13 brains! You lose!";

        new AlertDialog.Builder(this)
                .setTitle(winner.equals("You") ? "Victory!" : "Defeated!")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> {
                    viewModel.startNewGame();
                })
                .setNegativeButton("Quit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
