package ru.javarush;

import com.javarush.engine.cell.*;

import java.util.Random;

public class SnakeGame extends Game {

    //public static final String apple = "X";

    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;

    private static final int GOAL = 28;

    private int score;

    private boolean isGameStopped;

    private Snake snake;

    private Apple apple;

    private int turnDelay; // задаржка в движении змеи

    @Override
    public void initialize() { // создание игрового поля, создание игровых объектов, и т.д вроде как типа метод main
        setScreenSize(WIDTH, HEIGHT);

        createGame();

    }

    private void createGame() { // создание игры
        score = 0;
        setScore(score);

        snake = new Snake(WIDTH / 2, HEIGHT / 2); // задаем змейке стартовые значения
        createNewApple();
        isGameStopped = false;
        drawScene();

        turnDelay = 300;
        setTurnTimer(turnDelay); // в библиотеке этот метод вызывает метод onTurn, который вызывает метод у змеи move


    }

    private void drawScene() { // метод закрышивает игровое поле + закрашиват змею с яблоком
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.LIGHTSKYBLUE, "");
            }
        }

        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onTurn(int step) { // что это за метод ? ?? ??? видимо запускает цикл движения змеи
        snake.move(apple);

        if (!apple.isAlive) {
            score += 5;
            setScore(score);
            turnDelay = turnDelay - 10;
            setTurnTimer(turnDelay);
            createNewApple();
        }

        if (!snake.isAlive) {
            gameOver();
        }

        if (snake.getLength() > GOAL) {
            win();
        }

        drawScene();
    }

    @Override
    public void onKeyPress(Key key) { // метод нажатия кнопок управления
        switch (key) {
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
        }

        if (key == Key.SPACE && isGameStopped) {
            createGame();
        }
    }

    private void createNewApple() {
        while(true) {
            int newX = getRandomNumber(WIDTH);
            int newY = getRandomNumber(HEIGHT);

            apple = new Apple(newX, newY);
            if (!snake.checkCollision(apple)) break;
        }
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.AQUA, "GAME OVER", Color.BLACK, 40);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.YELLOWGREEN, "YOU WIN", Color.BLACK, 40);
    }

//    @Override
//    public void setScore(int score) {
//        this.score = this.score + score;
//    }

}
