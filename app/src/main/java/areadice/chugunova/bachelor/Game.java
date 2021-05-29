package areadice.chugunova.bachelor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;


public class Game extends AppCompatActivity {

    Point pc1 = new Point(0, 0);
    Point pc2 = new Point(0, 0);
    Grid grid;
    final int grid_row = 20;
    final int grid_column = 40;
    int[][] areaGridColor = new int[grid_column][grid_row];
    Bitmap[] areaPiece = new Bitmap[4];
    Canvas canvas = new Canvas();
    TextView timer, score;
    CountDownTimer timeLeft;
    ImageView dice1, dice2;
    private int player;
    int[] dices = new int[2];
    int defaultTime = 30;
    private boolean timerIsRunning = false;
    int[] playerScore = new int[2];

    public boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        grid = new Grid(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.main);
        frame.addView(grid, 0);
        areaPiece[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gray);
        areaPiece[1] = BitmapFactory.decodeResource(getResources(), R.drawable.player1);
        areaPiece[2] = BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        areaPiece[3] = BitmapFactory.decodeResource(getResources(), R.drawable.select);
        dice1 = findViewById(R.id.diceOne);
        dice2 = findViewById(R.id.diceTwo);
        timer = (TextView) findViewById(R.id.timer);
        timer.setText("0:" + defaultTime);
        score = (TextView) findViewById(R.id.player_score);
        playerScore[0] = 0;
        playerScore[1] = 0;
        score.setText("Score " + playerScore[0] + ":" + playerScore[1]);
        timeLeft = new CountDownTimer(defaultTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 > 9)
                    timer.setText("0:" + millisUntilFinished / 1000);
                else
                    timer.setText("0:0" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timer.setText("0:" + defaultTime);
                for (int i = pc1.x; i <= pc2.x; i++) {
                    for (int j = pc1.y; j <= pc2.y; j++) {
                        areaGridColor[i][j] = player;
                    }
                }
                grid.drawScreen(canvas);
                if (player == 1) {
                    player = 2;
                    playerScore[0] = playerScore[0] + (dices[0] * dices[1]);
                } else {
                    player = 1;
                    playerScore[1] = playerScore[1] + (dices[0] * dices[1]);
                }
                score.setText("Score " + playerScore[0] + ":" + playerScore[1]);
                gameOverCheck();

            }
        };

        for (int i = 0; i < grid_column; i++) {
            for (int j = 0; j < grid_row; j++) {
                areaGridColor[i][j] = 0;
            }
        }
    }

    public void onReset(View view) {
        timeLeft.cancel();
        timer.setText("0:" + defaultTime);
        timerIsRunning = false;
        playerScore[0] = 0;
        playerScore[1] = 0;
        score.setText("Score " + playerScore[0] + ":" + playerScore[1]);
        dice1.setImageResource(R.drawable.zero);
        dice2.setImageResource(R.drawable.zero);
        for (int i = 0; i < grid_column; i++) {
            for (int j = 0; j < grid_row; j++) {
                areaGridColor[i][j] = 0;
            }
        }
        grid.drawScreen(canvas);
    }

    public void changeDices(View view) {
        if (!timerIsRunning) {
            Random r = new Random();
            switch (r.nextInt(6)) {
                case 0:
                    dices[0] = 1;
                    dice1.setImageResource(R.drawable.one);
                    break;
                case 1:
                    dices[0] = 2;
                    dice1.setImageResource(R.drawable.two);
                    break;
                case 2:
                    dices[0] = 3;
                    dice1.setImageResource(R.drawable.three);
                    break;
                case 3:
                    dices[0] = 4;
                    dice1.setImageResource(R.drawable.four);
                    break;
                case 4:
                    dices[0] = 5;
                    dice1.setImageResource(R.drawable.five);
                    break;
                case 5:
                    dices[0] = 6;
                    dice1.setImageResource(R.drawable.six);
            }
            switch (r.nextInt(6)) {
                case 0:
                    dices[1] = 1;
                    dice2.setImageResource(R.drawable.one);
                    break;
                case 1:
                    dices[1] = 2;
                    dice2.setImageResource(R.drawable.two);
                    break;
                case 2:
                    dices[1] = 3;
                    dice2.setImageResource(R.drawable.three);
                    break;
                case 3:
                    dices[1] = 4;
                    dice2.setImageResource(R.drawable.four);
                    break;
                case 4:
                    dices[1] = 5;
                    dice2.setImageResource(R.drawable.five);
                    break;
                case 5:
                    dices[1] = 6;
                    dice2.setImageResource(R.drawable.six);
            }
            switch (player) {
                case 1:
                    pc1.x = 0;
                    pc2.y = grid_row - 1;
                    pc1.y = pc2.y + 1 - dices[1];
                    pc2.x = pc1.x + dices[0] - 1;
                    break;
                case 2:
                    pc2.x = grid_column - 1;
                    pc2.y = grid_row - 1;
                    pc1.x = pc2.x + 1 - dices[0];
                    pc1.y = pc2.y + 1 - dices[1];
            }

            timeLeft.start();
            timerIsRunning = true;
            setGhost();
            grid.drawScreen(canvas);
        }
    }

    public void startGame(View view) {
        if (!timerIsRunning && playerScore[0] == 0 && playerScore[1] == 0) {
            player = 1;
            changeDices(view);
            grid.onStart();
        }
    }

    public void gameOverCheck() {
        if ((playerScore[0] + playerScore[1]) >= (grid_column * grid_row)) {
            int winner = playerScore[0] > playerScore[1] ? 1 : 2;
            new AlertDialog.Builder(this).setTitle("Player " + winner + " win").setNeutralButton("Restart", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    timeLeft.cancel();
                    timer.setText("0:" + defaultTime);
                    timerIsRunning = false;
                    playerScore[0] = 0;
                    playerScore[1] = 0;
                    score.setText("Score " + playerScore[0] + ":" + playerScore[1]);
                    dice1.setImageResource(R.drawable.zero);
                    dice2.setImageResource(R.drawable.zero);
                    for (int i = 0; i < grid_column; i++) {
                        for (int j = 0; j < grid_row; j++) {
                            areaGridColor[i][j] = 0;
                        }
                    }
                    grid.drawScreen(canvas);
                }
            }).show();
        }
    }

    public void onUp(View view) {
        if (timerIsRunning) {
            if (pc1.y - 1 >= 0) {
                pc1.y--;
                pc2.y--;
            }
            setGhost();
            grid.drawScreen(canvas);
        }
    }

    public void onDown(View view) {
        if (timerIsRunning) {
            if (pc2.y + 1 < grid_row) {
                pc1.y++;
                pc2.y++;
            }
            setGhost();
            grid.drawScreen(canvas);
        }
    }

    private void setGhost() {
        x = false;
        while (!x) {
            checkIsFree();
        }
        checkToReturn();
        for (int i = 0; i < grid_column; i++) {
            for (int j = 0; j < grid_row; j++) {
                if (areaGridColor[i][j] == 3) {
                    areaGridColor[i][j] = 0;
                }
            }
        }
        for (int i = pc1.x; i <= pc2.x; i++) {
            for (int j = pc1.y; j <= pc2.y; j++) {
                areaGridColor[i][j] = 3;
            }
        }
    }

    private void checkToReturn() {
        int counter = 0;
        switch (player) {
            case 1:
                for (int i = pc1.x - 1; i >= 0; i--) {
                    for (int j = pc1.y; j <= pc2.y; j++) {
                        if ((areaGridColor[pc1.x - 1][j] == 1) || (areaGridColor[pc1.x - 1][j] == 2)) {
                            counter++;
                        }
                    }
                    if (counter == 0) {
                        pc1.x--;
                        pc2.x--;
                    }
                }
                break;
            case 2:
                for (int i = pc2.x + 1; i < grid_column; i++) {
                    for (int j = pc1.y; j <= pc2.y; j++) {
                        if ((areaGridColor[pc2.x + 1][j] == 1) || (areaGridColor[pc2.x + 1][j] == 2)) {
                            counter++;
                        }
                    }
                    if (counter == 0) {
                        pc1.x++;
                        pc2.x++;
                    }
                }
        }
    }

    public void checkIsFree() {
        for (int i = pc1.x; i <= pc2.x; i++) {
            for (int j = pc1.y; j <= pc2.y; j++) {
                if (areaGridColor[i][j] == 1 || areaGridColor[i][j] == 2) {
                    switch (player) {
                        case 1:
                            if (pc2.x < grid_column - 1) {
                                pc1.x++;
                                pc2.x++;
                            }
                            break;
                        case 2:
                            if (pc1.x - 1 >= 0) {
                                pc1.x--;
                                pc2.x--;
                            }
                    }
                    return;
                }
            }
        }
        x = true;
    }

    public void onOk(View view) {
        if (timerIsRunning) {
            timeLeft.cancel();
            timerIsRunning = false;
            timer.setText("0:" + defaultTime);
            for (int i = pc1.x; i <= pc2.x; i++) {
                for (int j = pc1.y; j <= pc2.y; j++) {
                    areaGridColor[i][j] = player;
                }
            }
            grid.drawScreen(canvas);
            if (player == 1) {
                player = 2;
                playerScore[0] = playerScore[0] + (dices[0] * dices[1]);
            } else {
                player = 1;
                playerScore[1] = playerScore[1] + (dices[0] * dices[1]);
            }
            score.setText("Score " + playerScore[0] + ":" + playerScore[1]);
        }
        gameOverCheck();
    }

    public void onRotate(View view) {
        if (timerIsRunning) {
            int x = dices[0];
            dices[0] = dices[1];
            dices[1] = x;
            switch (player) {
                case 1:
                    pc1.y = pc2.y + 1 - dices[1];
                    pc2.x = pc1.x + dices[0] - 1;
                    break;
                case 2:
                    pc1.x = pc2.x + 1 - dices[0];
                    pc1.y = pc2.y + 1 - dices[1];
            }
            setGhost();
            grid.drawScreen(canvas);
        }
    }


    class Grid extends View {

        private Paint paint = new Paint();

        public Grid(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            drawScreen(canvas);
        }

        private void drawScreen(Canvas canvas) {
            Rect screen = getScreenRect();
            int height = screen.height() / (grid_row);
            int width = screen.width() / (grid_column);
            int startX = screen.left;
            int startY = screen.top;
            for (int i = 0; i < grid_column; i++) {
                for (int j = 0; j < grid_row; j++) {
                    int posX = startX + i * width;
                    int posY = startY + j * height;
                    canvas.drawBitmap(areaPiece[areaGridColor[i][j]], null, new Rect(posX, posY, posX + width, posY + height), null);
                }
            }
            grid.invalidate();
        }

        public void onStart() {
            player = 1;
            drawScreen(canvas);
        }

        public Rect getScreenRect() {
            Rect rtScreen = new Rect();
            rtScreen.left = 70;
            rtScreen.top = 225;
            rtScreen.right = this.getWidth() - rtScreen.left - 250;
            rtScreen.bottom = this.getHeight() - 50;
            return rtScreen;
        }
    }
}
