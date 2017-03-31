package edu.utep.cs.cs4330.slider;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Handler gameStats = new Handler();
    private BoardView boardView;
    private Board board;
    private Button newButton;
    private boolean close;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        boardView = (BoardView) findViewById(R.id.playerView);
        board = new Board(4);
        boardView.setBoard(board);


        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                board.makeMove(x,y);
                boardView.invalidate();
                if (board.checkWin()) {
                    toast("Hey");
                    alertDialogBuilder.setTitle("Winner!");
                    alertDialogBuilder.setMessage("Total number of moves: "+board.getMoves() +
                            "\nPlay again?");

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });



        newButton = (Button) findViewById(R.id.newButton);

        //Initialize Alert box
        alertDialogBuilder  = new AlertDialog.Builder(MainActivity.this);
        // set dialog message
        alertDialogBuilder
                .setMessage("Want to start a new game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //if clicked, new game
                        restartGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //if true then the application will close on "No" click
                        if(close) {
                            MainActivity.this.finish();
                        }
                        //otherwise the dialog box will simply close
                        else {
                            close = true;
                            dialog.cancel();
                        }
                    }
                });
    }

    //Click of New button
    public void newButtonClicked(View view){
        //If there is a game active there will be a dialog box

        if(board.currGame()){
            close = false;
            alertDialogBuilder.setTitle("Game still ongoing!");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        //Otherwise a new game is created
        else{
            restartGame();
        }
    }

    //reinitializes to new game
    public void restartGame(){
        board.makeNewBoard();
        boardView.invalidate();
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
