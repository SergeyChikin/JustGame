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

    Player player = new Player(175, 480, 15);
    Enemy enemy = new Enemy();
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
        graphics.fillRect(player.getX(), player.getY(),30,30); // Рисуем объект игрока

        // Врагов отрисуем в верхней части окна и отобразим с помощью красных кругов.
        // Вражеских объектов на игровом поле может быть несколько одновременно,
        // поэтому выводить их будем с помощью цикла for:

        for (int i = 0; i < enemy.getX().size(); i++) {
            graphics.setColor(Color.RED); // Красный цвет для врагов
            graphics.fillOval(enemy.getX().get(i), enemy.getY().get(i), 20, 20);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString("Score: " + score, 10, 20); // Выводим счёт игрока на экран

        if (gameOver) {
            graphics.setFont(new Font("Arial", Font.PLAIN, 40));
            graphics.drawString("GAME OVER", 75, 300); // Выводим надпись "GAME OVER" при окончании игры
            timer.stop(); // Останавливаем таймер
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            for (int i = 0; i < enemy.getX().size(); i++) {
                enemy.getY().set(i, enemy.getY().get(i) + enemy.getSpeed()); // Двигаем врагов вниз по экрану
                if (enemy.getY().get(i) >= 600) {
                    enemy.getX().remove(i);
                    enemy.getY().remove(i);
                    score++; // Увеличиваем счет при уничтожении врага
                }
            }
            repaint(); // Обновляем экран

            if (enemy.getX().isEmpty()) {
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
            enemy.getX().add(x);
            enemy.getY().add(y); // Добавляем врага в списки координат
        }
    }

    public void checkCollision() {
        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 30, 30); // Границы игрока
        for (int i = 0; i < enemy.getX().size(); i++) {
            Rectangle enemyBounds = new Rectangle(enemy.getX().get(i),
                    enemy.getY().get(i), 20, 20); // Границы врага
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
            if (key == KeyEvent.VK_LEFT && player.getX() > 0) {
                player.setX(player.getX() - player.getSpeed());  // Перемещаем игрока влево
            }
            if (key == KeyEvent.VK_RIGHT && player.getX() < 350) {
                player.setX(player.getX() + player.getSpeed());  // Перемещаем игрока вправо
            }
            if (key == KeyEvent.VK_SPACE) {
                timer.stop(); // Игра на паузе
            }
            if (key == KeyEvent.VK_ENTER) {
                timer.start(); // Продолжение игры
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
