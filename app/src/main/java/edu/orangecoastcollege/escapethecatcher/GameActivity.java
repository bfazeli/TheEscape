package edu.orangecoastcollege.escapethecatcher;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class GameActivity extends Activity implements GestureDetector.OnGestureListener {

    private GestureDetector aGesture;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 150;
    final int OFFSET = 5;
    final int COLUMNS = 7;
    final int ROWS = 8;
    final int gameBoard[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 3},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private Player player;
    private Zombie zombie;

    //LAYOUT AND INTERACTIVE INFORMATION
    private ArrayList<ImageView> visualObjects;
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private ImageView obstacleImageView;
    private ImageView exitImageView;
    private int exitRow;
    private int exitCol;

    //  WINS AND LOSSES
    private int wins;
    private int losses;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return aGesture.onTouchEvent(event);
    }

    private TextView winsTextView;
    private TextView lossesTextView;

    private LayoutInflater layoutInflater;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resources = getResources();

        visualObjects = new ArrayList<ImageView>();

        wins = 0;
        losses = 0;
        winsTextView.setText(resources.getString(R.string.win) + wins);
        lossesTextView.setText(resources.getString(R.string.losses) + losses);

        // Instantiate the GestureDetector
        aGesture = new GestureDetector(this, this);
        startNewGame();
    }

    private void startNewGame() {
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for (int i = 0; i < visualObjects.size(); i++) {
            ImageView visualObj = visualObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        visualObjects.clear();

        //TASK 2:  BUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE CHARACTERS
        createZombie();
        createPlayer();
    }

    private void buildGameBoard() {
        // TODO: Inflate the entire game board (obstacles and exit)
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                if (gameBoard[i][j] == 1) {
                    // Let's inflate the zombie layout at specific x and y location
                    obstacleImageView = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout, null);
                    // Set the x and y coordinates of the imageView
                    obstacleImageView.setX(j * SQUARE + OFFSET);
                    obstacleImageView.setY(i * SQUARE + OFFSET);
                    // Display the zombie image view within relative layout
                    activityGameRelativeLayout.addView(obstacleImageView);

                    // Add the obstacle image view to the ArrayList
                    visualObjects.add(obstacleImageView);
                }
            }
        }

        // Let's inflate the zombie layout at specific x and y location
        exitImageView = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
        // Set the x and y coordinates of the imageView
        exitImageView.setX(6 * SQUARE + OFFSET);
        exitImageView.setY(5 * SQUARE + OFFSET);
        // Display the zombie image view within relative layout
        activityGameRelativeLayout.addView(exitImageView);

        // Add the obstacle image view to the ArrayList
        visualObjects.add(exitImageView);

        exitRow = 5;
        exitCol = 6;
    }

    private void createZombie() {
        // TODO: Determine where to place the Zombie (at game start)
        // TODO: Then, inflate the zombie layout

        int row = 5, col = 3;
        // Instantiate a new Zombie object
        zombie = new Zombie();

        zombie.setRow(row);
        zombie.setCol(col);

        // Let's inflate the zombie layout at specific x and y location
        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout, null);
        // Set the x and y coordinates of the imageView
        zombieImageView.setX(col * SQUARE + OFFSET);
        zombieImageView.setY(row * SQUARE + OFFSET);
        // Display the zombie image view within relative layout
        activityGameRelativeLayout.addView(zombieImageView);

        // Add the zombie image view to the ArrayList
        visualObjects.add(zombieImageView);
    }

    private void createPlayer() {
        // TODO: Determine where to place the Player (at game start)
        // TODO: Then, inflate the player layout
        int row = 5, col = 5;

        // Instantiate a new Player object
        player = new Player();

        player.setRow(row);
        player.setCol(col);

        // Inflate the player layout at x and y location
        playerImageView = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        // Set the x and y coords
        playerImageView.setX(col * SQUARE + OFFSET);
        playerImageView.setY(row * SQUARE + OFFSET);
        // Display the player image view within relative layout
        activityGameRelativeLayout.addView(playerImageView);

        // Add the player image view to the ArrayList
        visualObjects.add(playerImageView);
    }



    private void movePlayer(float velocityX, float velocityY) {
        // TODO: This method gets called in the onFling event

        // TODO: Determine which absolute velocity is greater (x or y)
        // TODO: If x is negative, move player left.  Else if x is positive, move player right.
        // TODO: If y is negative, move player down.  Else if y is positive, move player up.
        String direction = "";

        if (Math.abs(velocityX) > Math.abs(velocityY))  // X is bigger (MOVE LEFT OR RIGHT)
        {
            // Determine if move is LEFT
            if (velocityX < -FLING_THRESHOLD) direction = "LEFT";
            else if (velocityX > FLING_THRESHOLD) direction = "RIGHT";

        }
        else
        {
            if (velocityY < -FLING_THRESHOLD) direction = "DOWN";
            else if (velocityY > FLING_THRESHOLD) direction = "UP";
        }

        // ONLY move the player IF the direction is NOT an empty string
        if (!direction.equals(""))
        {
            player.move(gameBoard, direction);
            playerImageView.setX(player.getCol() * SQUARE + OFFSET);
            playerImageView.setY(player.getRow() * SQUARE + OFFSET);
        }

        // TODO: Then move the zombie no matter what, using the player's row and column position.
        zombie.move(gameBoard, player.getCol(), player.getRow());
        zombieImageView.setX(zombie.getCol() * SQUARE + OFFSET);
        zombieImageView.setY(zombie.getRow() * SQUARE + OFFSET);

        // Determine if the game is won or lost!!
        // Game Win
        if (player.getRow() == exitRow && player.getCol() == exitCol)
        {
            Toast.makeText(this, "Congratulations! You TRUMPED the Trump", Toast.LENGTH_SHORT).show();
            winsTextView.setText(resources.getString(R.string.win) + (++wins));
        } else if (zombie.getRow() == player.getRow() && zombie.getCol() == player.getCol())
        {
            Toast.makeText(this, "WHOMP WHOMP WHOMP", Toast.LENGTH_SHORT).show();
            lossesTextView.setText(resources.getString(R.string.losses) + (++losses));
            startNewGame();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        movePlayer(velocityX, velocityY);
        return false;
    }
}
