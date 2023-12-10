import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

//  -главным игровым объектом будет яркий квадрат, двигающийся по горизонтали в нижней части окна;
//  -в верхней части появляются объекты другой формы и цвета, опускающиеся вниз с разной скоростью;
//  -падающие объекты нельзя задевать;
//  -в левой верхней части расположен счетчик объектов, от которых удалось увернуться;
//  -если коснуться падающего объекта, то игра завершается.

public class Game extends JPanel implements ActionListener,
        KeyListener {

    private int playerX = 175; //Начальное положение игрока по горизонтали
    private int playerY = 480; //Рачальное положение игрока по вертикали
    private int playerSpeed = 15; //Скорость движения игрока
    private ArrayList<Integer>  enemyX = new ArrayList<>(); // X - координаты врагов
    private ArrayList<Integer> enemyY = new ArrayList<>(); // Y - координаты врагов
    private int enemySpeed = 20; // Скорость движения врагов
    private Timer timer; // Таймер обновления экрана
    private boolean gameOver = false; // Флаг окончания игры
    private int score = 0; //Счёт игрока

    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(100, this); // Создаём таймер
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Game game = new Game();
        frame.add(game);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.BLACK); // Заливаем фон чёрным цветом
        graphics.fillRect(0, 0, 400, 600);
        graphics.setColor(Color.WHITE); // Белый цвет для фигуры игрока
        graphics.fillRect(playerX, playerY, 30, 30); // Рисуем объект игрока

        // Врагов отрисуем в верхней части окна и отобразим с помощью красных кругов.
        // Вражеских объектов на игровом поле может быть несколько одновременно,
        // поэтому выводить их будем с помощью цикла for:

        for (int i = 0; i < enemyX.size(); i++) {
            graphics.setColor(Color.RED); // Красный цвет для врагов
            graphics.fillOval(enemyX.get(i), enemyY.get(i), 20, 20);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString("Score: " + score, 10, 20); // Выводим счёт игрока на экран

        if (gameOver) {
            graphics.setFont(new Font("Arial", Font.PLAIN, 40));
            graphics.drawString("GAME OVER", 120, 300); // Выводим надпись "GAME OVER" при окончании игры
            timer.stop(); // Останавливаем таймер
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            for (int i = 0; i < enemyX.size(); i++) {
                enemyY.set(i, enemyY.get(i) + enemySpeed); // Двигаем врагов вниз по экрану
                if (enemyY.get(i) >= 600) {
                    enemyX.remove(i);
                    enemyY.remove(i);
                    score++; // Увеличиваем счет при уничтожении врага
                }
            }
            repaint(); // Обновляем экран

            if (enemyX.isEmpty()) {
                spawnEnemy(); //  // Создаем нового врага, если текущих нет на экране
            }
            checkCollision();
        }
    }

    public void spawnEnemy() {
        Random random = new Random();
        int numEnemies = random.nextInt(5) + 1; // Генерируем от 1 до 5 врагов за раз

        for (int i = 0; i < numEnemies; i++) {
            int x = random.nextInt(350); // Генерируем случайную Х-координату для врага
            int y = 0;
            enemyX.add(x);
            enemyY.add(y); // Добавляем врага в списки координат
        }
    }

    public void checkCollision() {
        Rectangle playerBounds = new Rectangle(playerX, playerY, 30, 30); // Границы игрока
        for (int i = 0; i < enemyX.size(); i++) {
            Rectangle enemyBounds = new Rectangle(enemyX.get(i),
                    enemyY.get(i), 20, 20); // Границы врага
            if (playerBounds.intersects(enemyBounds)) {
                gameOver = true; // Если произошло столкновение, игра заканчивается
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!gameOver) {
            if (key == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= playerSpeed;  // Перемещаем игрока влево
            }
            if (key == KeyEvent.VK_RIGHT && playerX < 350) {
                playerX += playerSpeed;  // Перемещаем игрока вправо
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
