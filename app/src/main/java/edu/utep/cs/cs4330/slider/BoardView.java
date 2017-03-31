package edu.utep.cs.cs4330.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rebecca on 3/29/2017.
 */

public class BoardView extends View {

    Board board;

    /** Callback interface to listen for board touches. */
    public interface BoardTouchListener {

        /**
         * Called when a place of the board is touched.
         * The coordinate of the touched place is provided.
         *
         * @param x 0-based column index of the touched place
         * @param y 0-based row index of the touched place
         */
        void onTouch(int x, int y);
    }

    /**
     * Overridden here to detect a board touch. When the board is
     * touched, the corresponding place is identified,
     * and registered listeners are notified.
     *
     * @see BoardTouchListener
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int xy = locatePlace(event.getX(), event.getY());
                if (xy >= 0) {
                    notifyBoardTouch(xy / 100, xy % 100);
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /** Listeners to be notified upon board touches. */
    private final List<BoardTouchListener> listeners = new ArrayList<>();

    /** Size of the board. */
    private int boardSize;

    /** Board grid line color. */
    private final int boardLineColor = ContextCompat.getColor(getContext(),R.color.boardLines);

    private final int boardColor = ContextCompat.getColor(getContext(),R.color.boardBackground);;

    /** Board grid line paint. */
    private final Paint boardLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardLinePaint.setColor(boardLineColor);
        boardLinePaint.setStrokeWidth(2);
    }

    /** Board background paint. */
    private final Paint boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardPaint.setColor(boardColor);
    }

    /* Set numbers */
    private final Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        numPaint.setColor(Color.WHITE);
        numPaint.setTextSize(100);
    }

    /* Make board */
    public void setBoard(Board b){
        this.board = b;
        this.boardSize = b.getSize();
    }



    /** Create a new board view to be run in the given context. */
    public BoardView(Context context) {
        super(context);
    }

    /** Create a new board view with the given attribute set. */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Create a new board view with the given attribute set and style. */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /** Overridden here to draw a 2-D representation of the board. */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawGameBoard(canvas);
    }

    /** Draw horizontal and vertical lines. */
    private void drawGrid(Canvas canvas) {
        final float maxCoord = maxCoord();
        final float placeSize = lineGap();
        canvas.drawRect(0, 0, maxCoord, maxCoord, boardPaint);
        for (int i = 0; i < numOfLines(); i++) {
            float xy = i * placeSize;
            canvas.drawLine(0, xy, maxCoord, xy, boardLinePaint); // horizontal line
            canvas.drawLine(xy, 0, xy, maxCoord, boardLinePaint); // vertical line
        }
    }

    private void drawGameBoard(Canvas canvas){
        final float maxCoord = maxCoord();
        final float placeSize = lineGap();
        for (int i=0; i<boardSize;i++){
            for(int j=0; j<boardSize;j++) {
                if (board.getBoard()[i][j] != -1) {
                    canvas.drawRect((i*placeSize)+10,
                                    (j*placeSize)+10,
                                    (i*placeSize+placeSize)-10,
                                    (j*placeSize+placeSize) -10, boardLinePaint);
                    canvas.drawText(""+board.getBoard()[i][j], (i * placeSize)+placeSize/2, (j * placeSize)+placeSize/2,  numPaint);
                }
            }
        }
    }

    /** Calculate the gap between two horizontal/vertical lines. */
    protected float lineGap() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight()) / (float) boardSize;
    }

    /** Calculate the number of horizontal/vertical lines. */
    private int numOfLines() {
        return boardSize + 1;
    }

    /** Calculate the maximum screen coordinate. */
    protected float maxCoord() {
        return lineGap() * (numOfLines() - 1);
    }

    /**
     * Given screen coordinates, locate the corresponding place in the board
     * and return its coordinates; return -1 if the screen coordinates
     * don't correspond to any place in the board.
     * The returned coordinates are encoded as <code>x*100 + y</code>.
     */
    private int locatePlace(float x, float y) {
        if (x <= maxCoord() && y <= maxCoord()) {
            final float placeSize = lineGap();
            int ix = (int) (x / placeSize);
            int iy = (int) (y / placeSize);
            return ix * 100 + iy;
        }
        return -1;
    }

    /** Register the given listener. */
    public void addBoardTouchListener(BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }
}
