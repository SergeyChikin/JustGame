import java.util.ArrayList;

public class Enemy {

    private ArrayList<Integer> x = new ArrayList<>(); // X - координаты врагов
    private ArrayList<Integer> y = new ArrayList<>(); // Y - координаты врагов
    private int speed; // Скорость движения врагов


    public Enemy() {
        setSpeed(20);
    }

    public ArrayList<Integer> getX() {
        return x;
    }

    public void setX(ArrayList<Integer> x) {
        this.x = x;
    }

    public ArrayList<Integer> getY() {
        return y;
    }

    public void setY(ArrayList<Integer> y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
