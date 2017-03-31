package edu.utep.cs.cs4330.slider;

import java.util.Random;

/**
 * Created by Rebecca on 3/30/2017.
 */

class Board {
    private int moves;
    private boolean gameOn=false;
    private int missingNo;
    private int size;
    private int[][] board;

    public Board(int newSize){
        size = newSize;
        makeNewBoard();
        missingNo = size*size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }

    public void makeNewBoard() {
        moves = 0;
        int num = 1;
        gameOn = true;
        board = new int[size][size];
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                board[i][j] = num;
                num++;
            }
        }
        shuffle();
    }

    private void shuffle(){
        Random rand = new Random();

        int x = size-1;
        int y = size-1;

        board[x][y] = -1;
        int a = x, b = y;
        int shuff = 0;
        int temp;
        int side =0;
        boolean rotate = false;
        while(shuff<200){
            if(rotate)
                side= rand.nextInt(4);
            switch (side){
                case 0: if(x-1 > 0){
                    x--;
                }
                break;
                case 1: if(y-1 > 0){
                    y--;
                }
                break;
                case 2: if(x+1 < size){
                    x++;
                }
                break;
                case 3: if(y+1 < size){
                    y++;
                }
                break;
            }

            temp = board[x][y];
            board[x][y] = -1;
            board[a][b] = temp;
            a = x;
            b = y;
            shuff++;
            rotate=!rotate;
        }
    }

    public boolean checkWin() {
        int num = 1;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
               if(board[i][j] == num){
                   num++;
               }

            }
        }
        if(num == missingNo)
            return true;
        return false;
    }

    public int getMoves() {
        return moves;
    }

    public boolean currGame() {
        return gameOn;
    }

    public void makeMove(int x, int y) {
        int temp;
        int a = x;
        int b = y;
        moves++;

        if(x+1<size && board[x+1][y] == -1){
            x++;
        }else if(y+1<size && board[x][y+1] == -1){
            y++;
        }else if(x-1>-1 && board[x-1][y] == -1){
            x--;
        }else if(y-1>-1 && board[x][y-1] == -1){
            y--;
        }else{
            return;
        }
        board[x][y] = board[a][b];
        board[a][b] = -1;
    }
}
