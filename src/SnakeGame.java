import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 3;
    int foodX;
    int foodY;
    int score = 0;

    char direction = 'R';

    boolean running = false;

    Timer timer;
    Random random;

    SnakeGame() {

        random = new Random();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new MyKeyAdapter());

        startGame();
    }

    public void startGame() {

        bodyParts = 3;
        score = 0;
        direction = 'R';

        x[0] = 0;
        y[0] = 0;

        newFood();

        running = true;

        timer = new Timer(DELAY, this);
        timer.start();

        requestFocusInWindow();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            // Draw food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }

                g.fillRect(
                    x[i],
                    y[i],
                    UNIT_SIZE,
                    UNIT_SIZE
                );
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));

            g.drawString(
                "Score: " + score,
                20,
                30
            );

        } else {

            gameOver(g);
        }
    }

    public void newFood() {

        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;

        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {

        // Move the body
        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Move the head
        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {

        if (x[0] == foodX && y[0] == foodY) {

            bodyParts++;
            score++;

            newFood();
        }
    }

    public void checkCollisions() {

        // Snake hits itself
        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;
            }
        }

        // Snake hits left wall
        if (x[0] < 0) {

            running = false;
        }

        // Snake hits right wall
        if (x[0] >= WIDTH) {

            running = false;
        }

        // Snake hits top wall
        if (y[0] < 0) {

            running = false;
        }

        // Snake hits bottom wall
        if (y[0] >= HEIGHT) {

            running = false;
        }

        if (!running) {

            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        // Game Over
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));

        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString(
            "Game Over",
            (WIDTH - metrics.stringWidth("Game Over")) / 2,
            HEIGHT / 2
        );

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));

        String scoreText = "Score: " + score;

        g.drawString(
            scoreText,
            (WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2,
            HEIGHT / 2 + 50
        );

        // Restart message
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String restartText = "Press ENTER to restart";
        String exitText = "Press ESC to exit";

        g.drawString(
            restartText,
            (WIDTH - g.getFontMetrics().stringWidth(restartText)) / 2,
            HEIGHT / 2 + 100
        );

        g.drawString(
            exitText,
            (WIDTH - g.getFontMetrics().stringWidth(exitText)) / 2,
            HEIGHT / 2 + 130
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();

            checkFood();

            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            // ESC → Exit game
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                System.exit(0);
            }

            // ENTER → Restart game
            if (e.getKeyCode() == KeyEvent.VK_ENTER && !running) {

                startGame();

                return;
            }

            // Arrow keys
            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:

                    if (direction != 'R') {
                        direction = 'L';
                    }

                    break;

                case KeyEvent.VK_RIGHT:

                    if (direction != 'L') {
                        direction = 'R';
                    }

                    break;

                case KeyEvent.VK_UP:

                    if (direction != 'D') {
                        direction = 'U';
                    }

                    break;

                case KeyEvent.VK_DOWN:

                    if (direction != 'U') {
                        direction = 'D';
                    }

                    break;
            }
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");

        SnakeGame game = new SnakeGame();

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        // Give keyboard focus to the game
        game.requestFocusInWindow();
    }
}