package com.caitlynwiley.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView blue;
    ImageView green;
    //1 means blue, 2 means green, 0 is empty
    int[][] board;
    int[][] ids =  {{R.id.square1, R.id.square2, R.id.square3},
                    {R.id.square4, R.id.square5, R.id.square6},
                    {R.id.square7, R.id.square8, R.id.square9}};
    int turnsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blue = findViewById(R.id.blue);
        green = findViewById(R.id.green);
        board = new int[3][3];
        turnsLeft = 9;
    }

    public void switchTurn(View view) {
        int j = getColumnIndex(view);
        int i = getRowIndex(j);

        if (i == -1) {
            Toast.makeText(MainActivity.this, "Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        turnsLeft--;

        ImageView piece = findViewById(ids[i][j]);
        if (blue.getAlpha() == 1f) {
            piece.setImageDrawable(getResources().getDrawable(R.drawable.blue_piece));
        } else {
            piece.setImageDrawable(getResources().getDrawable(R.drawable.green_piece));
        }
        piece.setTranslationY(-2000f);
        piece.setAlpha(1f);
        piece.animate().translationYBy(2000f).setDuration(500);

        if (blue.getAlpha() == 1f) {
            blue.setAlpha(0f);
            green.setAlpha(1f);
            board[i][j] = 1;
        } else {
            blue.setAlpha(1f);
            green.setAlpha(0f);
            board[i][j] = 2;
        }

        boolean playerWon = checkForWin(board[i][j]);

        if (playerWon) {
            //Toast.makeText(MainActivity.this, "Player " + board[i][j] + " won!", Toast.LENGTH_SHORT).show();
            //Pop up a button letting user to choose to play again (or quit maybe)
            //TODO: animation doesn't finish before dialog displays
            TextView winner = findViewById(R.id.gameOverText);
            winner.setText("Player " + board[i][j] + " Won!");
            RelativeLayout layout = findViewById(R.id.gameOverDialog);
            layout.setVisibility(View.VISIBLE);
        }

        if (turnsLeft == 0) {
            TextView gameOverText = findViewById(R.id.gameOverText);
            gameOverText.setText("It's a Draw!");
            RelativeLayout layout = findViewById(R.id.gameOverDialog);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private String printBoard() {
        String b = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b += board[i][j];
            }
            b += "/n";
        }
        return b;
    }

    private int getColumnIndex(View view) {
        switch (view.getId()) {
            case R.id.square1:
            case R.id.square4:
            case R.id.square7:
                return 0;
            case R.id.square2:
            case R.id.square5:
            case R.id.square8:
                return 1;
            case R.id.square3:
            case R.id.square6:
            case R.id.square9:
                return 2;
            default:
                return -1;
        }
    }

    private int getRowIndex(int col) {
        //Get row number based on what's filled in
        for (int i = 2; i >= 0; i--) {
            if (board[i][col] == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkForWin(int player) {
        //check across rows
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            } else {
                count = 0;
            }
        }
        //check down columns
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            } else {
                count = 0;
            }
        }
        //check diagonals
        for (int i = 0, j = 0; i < 3 && j < 3; i++, j++) {
            if (board[i][j] == player) {
                count ++;
            }
        }
        if (count == 3) {
            return true;
        } else {
            count = 0;
        }

        for (int i = 2, j = 0; i >= 0 && j < 3; i--, j++) {
            if (board[i][j] == player) {
                count ++;
            }
        }
        return count == 3;
    }

    public void resetGame(View view) {
        //Clear board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }

        //Reset images
        for (int[] row : ids) {
            for (int id : row) {
                ImageView img = findViewById(id);
                img.setAlpha(0f);
            }
        }

        RelativeLayout layout = findViewById(R.id.gameOverDialog);
        layout.setVisibility(View.INVISIBLE);

        turnsLeft = 9;
    }
}
