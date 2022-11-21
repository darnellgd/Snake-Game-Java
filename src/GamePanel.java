import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int xBody[] = new int[GAME_UNITS];
    final int yBody[] = new int[GAME_UNITS];
    int bodySize = 4;
    int applesEaten = 0;
    int appleXPosition;
    int appleYPosition;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        createApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        if (running) {
            // // Draws lines in x and y directions to show the screen in a grid-like format
            // for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); // y direction
            //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);  // x direction
            // }

            g.setColor(Color.GREEN);
            g.fillOval(appleXPosition, appleYPosition, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodySize; i++) {
                // Color head of snake
                if (i == 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(xBody[0], yBody[0], UNIT_SIZE, UNIT_SIZE);
                } else {
                    // Color body of snake
                    g.setColor(Color.CYAN);
                    g.fillRect(xBody[i], yBody[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, UNIT_SIZE * 3));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void move() {
        
        for (int i = bodySize; i>0; i--) {
            xBody[i] = xBody[i-1];
            yBody[i] = yBody[i-1];
        }

        switch (direction) {
            case 'U':
                yBody[0] = yBody[0] - UNIT_SIZE;
                break;
            case 'D':
                yBody[0] = yBody[0] + UNIT_SIZE;
                break;
            case 'L':
                xBody[0] = xBody[0] - UNIT_SIZE;
                break;
            case 'R':
                xBody[0] = xBody[0] + UNIT_SIZE;
                break;
        }

    }

    public void createApple() {
        appleXPosition = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleYPosition = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void checkApple() {
        
        if ((xBody[0] == appleXPosition) && (yBody[0] == appleYPosition)) {
            bodySize++;
            applesEaten++;
            createApple();
        }

    }

    public void checkCollisions() {

        // Checks if head collides with body
        for (int i = bodySize; i > 0; i--) {
            if ((xBody[0] == xBody[i]) && (yBody[0] == yBody[i])) running = false;
        }

        // Checks if head exceeds left side of window
        if (xBody[0] < 0) running = false;

        // Checks if head exceeds right side if window
        if (xBody[0] > SCREEN_WIDTH) running = false;

        //Checks if head exceeds top of window
        if (yBody[0] < 0) running = false;

        // Checks if head exceeds bottom of window
        if (yBody[0] > SCREEN_HEIGHT) running = false;

        if (!running) timer.stop();

    }

    public void gameOver(Graphics g) {

        // Score text
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, UNIT_SIZE * 3));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, UNIT_SIZE * 3));
        FontMetrics metricsOver = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metricsOver.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                // Arrows keys or WASD
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
