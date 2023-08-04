import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 30;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 30;
    private static final int GAME_SPEED = 300; // Milliseconds

    private ArrayList<Point> snake;
    private Point food;
    private int direction; // 0: Up, 1: Right, 2: Down, 3: Left
    private boolean gameOver;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(TILE_SIZE * GRID_WIDTH, TILE_SIZE * GRID_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake = new ArrayList<>();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        generateFood();
        direction = 1; // Start moving to the right
        gameOver = false;

        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    private void generateFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_WIDTH);
            y = random.nextInt(GRID_HEIGHT);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case 0: // Up
                newHead.y--;
                break;
            case 1: // Right
                newHead.x++;
                break;
            case 2: // Down
                newHead.y++;
                break;
            case 3: // Left
                newHead.x--;
                break;
        }

        // Check for collisions with food and boundaries
        if (newHead.equals(food)) {
            snake.add(0, newHead);
            generateFood();
        } else if (newHead.x < 0 || newHead.y < 0 || newHead.x >= GRID_WIDTH || newHead.y >= GRID_HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            timer.stop();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Game Over!", 150, 250);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && direction != 2) {
            direction = 0;
        } else if (key == KeyEvent.VK_RIGHT && direction != 3) {
            direction = 1;
        } else if (key == KeyEvent.VK_DOWN && direction != 0) {
            direction = 2;
        } else if (key == KeyEvent.VK_LEFT && direction != 1) {
            direction = 3;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
