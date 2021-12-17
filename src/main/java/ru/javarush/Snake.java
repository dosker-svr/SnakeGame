package ru.javarush;

import com.javarush.engine.cell.*;

import java.util.*;

public class Snake {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    public boolean isAlive = true;

    private Direction direction = Direction.LEFT;

    private List<GameObject> snakeParts = new ArrayList<>(); // список в котором хранятся части змеи

    public Snake(int x, int y) {
        GameObject go1 = new GameObject(x, y);
        GameObject go2 = new GameObject(x + 1, y);
        GameObject go3 = new GameObject(x + 2, y);
        snakeParts.add(go1);
        snakeParts.add(go2);
        snakeParts.add(go3);
    }

    public void draw(Game game) { // закрашивает поле у игровой таблицы ТАМ, где змея
        Color color = isAlive ? Color.BLACK : Color.RED;

        for (int i = 0; i < snakeParts.size(); i++) {
            GameObject part = snakeParts.get(i);
            String typeSing = i != 0 ? BODY_SIGN : HEAD_SIGN;
            game.setCellValueEx(part.x, part.y, Color.NONE, typeSing, color, 75);
        }

    }

    public Direction getCurrentDirection() {
        return direction;
    }

    public void setDirection(Direction direction) { // метод устанавливает направление змеи
        switch (direction) { /* switch для того чтобы проверить:
                                                                А: не противоположное ли направление (2ой if)
                                                                Б: так и не понял зачем 1ый if*/
            case LEFT:
                if (snakeParts.get(0).y == snakeParts.get(1).y) {
                    break;
                }
                if (this.direction.equals(Direction.RIGHT)) {
                    break;
                } else {
                    this.direction = direction;
                }
                break;
            case RIGHT:
                if (snakeParts.get(0).y == snakeParts.get(1).y) {
                    break;
                }
                if (this.direction.equals(Direction.LEFT)) {
                    break;
                } else {
                    this.direction = direction;
                }
                break;
            case UP:
                if (snakeParts.get(0).x == snakeParts.get(1).x) {
                    break;
                }
                if (this.direction.equals(Direction.DOWN)) {
                    break;
                } else {
                    this.direction = direction;
                }
                break;
            case DOWN:
                if (snakeParts.get(0).x == snakeParts.get(1).x) {
                    break;
                }
                if (this.direction.equals(Direction.UP)) {
                    break;
                } else {
                    this.direction = direction;
                }
                break;
        }

    }

    /* метод move - это всего одни шаг змеи по полю.
    создаём новую голову змеи. если за пределами игрового поля - то змея мертва.
     * голову добавляем в начало списка. хвост удаляем.*/
    public void move(Apple apple) {
        GameObject newHead = createNewHead();

        if (newHead.x >= SnakeGame.WIDTH
                || newHead.x < 0
                || newHead.y >= SnakeGame.HEIGHT
                || newHead.y < 0) {
            isAlive = false;
            return;
        }

        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }

        snakeParts.add(0, newHead);

        if (apple.x == newHead.x && apple.y == newHead.y) {
            apple.isAlive = false;
            return;
        } else {
            removeTail();
        }
    }

    public GameObject createNewHead() {
        /* метод возвращает GameObject с координатами головы в следующем ходе, в зависимости от напраления движения */
        int xHead = snakeParts.get(0).x;
        int yHead = snakeParts.get(0).y;

        GameObject newHead;

        switch (direction) {
            case LEFT:
                newHead = new GameObject(xHead - 1, yHead);
                return newHead;
            case RIGHT:
                newHead = new GameObject(xHead + 1, yHead);
                return newHead;
            case UP:
                newHead = new GameObject(xHead, yHead - 1);
                return newHead;
            case DOWN:
                newHead = new GameObject(xHead, yHead + 1);
                return newHead;
            default:
                return snakeParts.get(0);
        }
    }

    public void removeTail() { // метод удаляет хвост змеи
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject head) {
        /*нужно проверить не пересекает ли новая голова тело змеи. но и не только голова.
         * ещё и не создается ли яблоко в теле змеи*/
        for (int i = 0; i < snakeParts.size(); i++) {
            if (head.x == snakeParts.get(i).x && head.y == snakeParts.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
