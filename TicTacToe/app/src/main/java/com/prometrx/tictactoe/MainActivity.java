package com.prometrx.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView playerOneScoreTextView, playerTwoScoreTextView, playerStatus;
    private Button [] buttons = new Button[9];
    private Button resetGameButton;

    private int playerOneScoreCount, playerTwoScoreCount, rountCount;
    private boolean activePlayer;

    //p1 => 0
    //p2 => 1
    //empty => 2

    private int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    private int[][] winningPosition = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //columns
            {0, 4, 8}, {2, 4, 6} //cross
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);                          //
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground(); //
        animationDrawable.setEnterFadeDuration(2500);                                               // These lines are for live background.
        animationDrawable.setExitFadeDuration(2500);                                                //
        animationDrawable.start();                                                                  //

    }

    private void init() {

        playerOneScoreTextView = findViewById(R.id.playerOneScoreTextView);
        playerTwoScoreTextView = findViewById(R.id.playerTwoScoreTextView);
        playerStatus = findViewById(R.id.playerStatusTextView);
        resetGameButton = findViewById(R.id.resetButton);

        for (int i = 0; i<buttons.length; i++) {
            int resourceId = getResources().getIdentifier("tictactoeButton"+i, "id", getPackageName()); //This line get button id.
            buttons[i] = findViewById(resourceId); // initialize
            buttons[i].setOnClickListener(this);
        }

        rountCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

    }

    @Override
    public void onClick(View view) {

        Button button = ((Button) view);//Casting
        if(!button.getText().toString().equals("")) {
            return;
        }

        String buttonId= button.getResources().getResourceEntryName(button.getId());
        int gameStatePointer = Integer.parseInt(buttonId.substring(buttonId.length()-1, buttonId.length()));

        if(activePlayer) {
            button.setText("X");
            button.setTextColor(Color.parseColor("#D81B60"));
            gameState[gameStatePointer] = 0;
        }
        else{
            button.setText("O");
            button.setTextColor(Color.parseColor("#43A047"));
            gameState[gameStatePointer] = 1;
        }

        rountCount++;

        if(winning()) {

            if(activePlayer) {

                playerOneScoreCount++;
                updateScoreCount();
                Toast.makeText(MainActivity.this, "Player One Won", Toast.LENGTH_LONG).show();
                playAgain();

            }
            else{

                playerTwoScoreCount++;
                updateScoreCount();
                Toast.makeText(MainActivity.this, "Player Two Won", Toast.LENGTH_LONG).show();
                playAgain();

            }
        }
        else if(rountCount == 9) {

            Toast.makeText(MainActivity.this, "No Winner", Toast.LENGTH_LONG).show();
            playAgain();

        }
        else{
            activePlayer = !activePlayer;
        }

        if(playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("PLAYER ONE IS WINNING");
        }
        else if(playerOneScoreCount < playerTwoScoreCount) {
            playerStatus.setText("PLAYER TWO IS WINNING");
        }else{
            playerStatus.setText("EQUALTY");
        }

        resetGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updateScoreCount();
            }
        });

    }

    private boolean winning() {

        for(int [] winArray : winningPosition) {
            if (gameState[winArray[0]] == gameState[winArray[1]] && gameState[winArray[1]] == gameState[winArray[2]] && gameState[winArray[0]] != 2) return true;
        }

        return false;
    }

    private void updateScoreCount() {
        playerOneScoreTextView.setText(playerOneScoreCount + "");
        playerTwoScoreTextView.setText(playerTwoScoreCount + "");
    }
    private void playAgain() {
        rountCount = 0;
        activePlayer = true;

        for(int i = 0; i<gameState.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }

    }

}