import effect.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

//  -главным игровым объектом будет яркий квадрат, двигающийся по горизонтали в нижней части окна;
//  -в верхней части появляются объекты другой формы и цвета, опускающиеся вниз с разной скоростью;
//  -падающие объекты нельзя задевать;
//  -в левой верхней части расположен счетчик объектов, от которых удалось увернуться;
//  -если коснуться падающего объекта, то игра завершается.

public class Game extends JPanel implements ActionListener,
        KeyListener {

    Player player = new Player(175, 500, 15);
    Enemy enemy = new Enemy();
    private Timer timer; // Таймер обновления экрана
//    private Timer timerSound;
    private boolean gameOver = false; // Флаг окончания игры
    private int score = 0; // Счёт игрока
    private int levelCount = 1; // Уровень сложности

    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
//        timerSound = new Timer(3000, this); //todo возможно нужно использовать потоки, для паузы(мониторы)
//        timerSound.start();
//        Sound begin = Sound.playSound("src/repository/sound/begin.wav");
//        begin.join();  //todo Проигрывается до начала игры
//        Sound.playSound("C:\\Users\\79185\\Desktop\\WAV\\begin.wav");
        Sound.playSound("src/repository/sound/begin.wav");
//        timerSound.stop();
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
        graphics.fillRect(player.getX(), player.getY(), 25, 25); // Рисуем объект игрока

        // Врагов отрисуем в верхней части окна и отобразим с помощью красных кругов.
        // Вражеских объектов на игровом поле может быть несколько одновременно,
        // поэтому выводить их будем с помощью цикла for:

        for (int i = 0; i < enemy.getX().size(); i++) {
            graphics.setColor(Color.RED); // Красный цвет для врагов
            graphics.fillOval(enemy.getX().get(i), enemy.getY().get(i), 20, 20);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 15));
        graphics.drawString("Score: " + score, 310, 20); // Выводим счёт игрока на экран

        if (gameOver) {
            graphics.setFont(new Font("Arial", Font.PLAIN, 40));
            graphics.drawString("GAME OVER", 75, 300); // Выводим надпись "GAME OVER" при окончании игры
            graphics.setFont(new Font("Arial", Font.PLAIN, 25));
            graphics.drawString("You scored " + score + " points.", 80, 330);
            Sound.playSound("src/repository/sound/game-over.wav");
            timer.stop(); // Останавливаем таймер
        }


        graphics.setFont(new Font("Arial", Font.PLAIN, 15));
        graphics.drawString("Level " + levelCount, 10, 20);
        if (score >= 25 && score < 50) {  //todo Сделать метод!!!
            if (score == 25) {
                Sound.playSound("src/repository/sound/level-up.wav");
            }
            enemy.setSpeed(25);
            levelCount = 2;
        }
        if (score >= 50 && score < 75) {
            if (score == 50) {
                Sound.playSound("src/repository/sound/level-up.wav");
            }
            enemy.setSpeed(30);
            levelCount = 3;
        }
        if (score >= 75 && score < 100) {
            if (score == 75) {
                Sound.playSound("src/repository/sound/level-up.wav");
            }
            enemy.setSpeed(35);
            levelCount = 4;
        }
        if (score >= 100 && score < 125) {
            if (score == 100) {
                Sound.playSound("src/repository/sound/level-up.wav");
            }
            enemy.setSpeed(40);
            levelCount = 5;
        }
        if (score >= 125 && score < 150) {
            if (score == 125) {
                Sound.playSound("src/repository/sound/level-up.wav");
            }
            enemy.setSpeed(45);
            levelCount = 6;
        }
        if (score >= 150) {
            graphics.setFont(new Font("Arial", Font.PLAIN, 40));
            graphics.drawString("YOU WIN!", 95, 300);
//            Sound.playSound("C:\\Users\\79185\\Desktop\\WAV\\" +
//                    "dominirovanie-igrovogo-personaja-po-vneshnemu-vidu-bgm-42277.wav");
            Sound.playSound("src/repository/sound/you-win.wav");
            timer.stop();
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
                    Sound.playSound("src/repository/sound/score-plus.wav");
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
        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 25, 25); // Границы игрока
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
                Sound.playSound("src/repository/sound/button.wav");
            }
            if (key == KeyEvent.VK_RIGHT && player.getX() < 350) {
                player.setX(player.getX() + player.getSpeed());  // Перемещаем игрока вправо
                Sound.playSound("src/repository/sound/button.wav");
            }
            if (key == KeyEvent.VK_SPACE) {
                timer.stop(); // Игра на паузе
                Sound.playSound("src/repository/sound/pause.wav");
            }
            if (key == KeyEvent.VK_ENTER) { //todo Переделать паузу на одну кнопку
                timer.start(); // Продолжение игры
                Sound.playSound("src/repository/sound/resume-game.wav");
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
