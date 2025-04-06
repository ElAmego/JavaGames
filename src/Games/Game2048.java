package Games;
import com.javarush.engine.cell.*;


public class Game2048 extends Game {
    private static final int SIDE = 4;
    private boolean isGameStopped = false;
    private int score = 0;
    private int[][] gameField = new int[SIDE][SIDE];

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
        setTurnTimer(10);
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }

    private void createNewNumber() {
        boolean isContinue = true;

        if (getMaxTileValue() == 2048) {
            win();
        }

        do {
            int x = getRandomNumber(SIDE);
            int y = getRandomNumber(SIDE);

            if (gameField[x][y] == 0) {
                gameField[x][y] = getRandomNumber(10) == 9 ? 4 : 2;
                isContinue = false;
            }

        } while (isContinue);
    }

    private Color getColorByValue(int value) {
        Color color = null;
        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.LIGHTSALMON;
                break;
            case 4:
                color = Color.INDIGO;
                break;
            case 8:
                color = Color.BLUE;
                break;
            case 16:
                color = Color.SKYBLUE;
                break;
            case 32:
                color = Color.SEAGREEN;
                break;
            case 64:
                color = Color.GREEN;
                break;
            case 128:
                color = Color.ORANGE;
                break;
            case 256:
                color = Color.DARKORANGE;
                break;
            case 512:
                color = Color.RED;
                break;
            case 1024:
                color = Color.PINK;
                break;
            case 2048:
                color = Color.VIOLET;
                break;
        }

        return color;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), String.valueOf(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    private boolean compressRow(int[] row) {
        boolean isEdit = false;
        int counter = 0;
        int tmp = 0;

        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0) {
                counter++;
            } else if(counter != 0) {
                tmp = row[i];
                row[i] = row[i-counter];
                row[i-counter] = tmp;
                isEdit = true;
            }
        }

        return isEdit;
    }

    private boolean mergeRow(int[] row) {
        boolean isEdit = false;
        for (int i = 1; i < row.length; i++) {
            if (row[i] != 0 && row[i-1] != 0 && row[i] == row[i-1]) {
                row[i-1] += row[i];
                row[i] = 0;
                isEdit = true;
                score += row[i-1];
                onTurn(10);
            }
        }

        return isEdit;
    }

    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove() && !isGameStopped) {
            gameOver();
        }

        if (key == Key.UP && !isGameStopped) {
            moveUp();
            drawScene();
        } else if (key == Key.RIGHT && !isGameStopped) {
            moveRight();
            drawScene();
        } else if (key == Key.DOWN && !isGameStopped) {
            moveDown();
            drawScene();
        } else if (key == Key.LEFT && !isGameStopped) {
            moveLeft();
            drawScene();
        } else if (key == Key.SPACE && isGameStopped) {
            isGameStopped = false;
            score = 0;
            onTurn(10);
            createGame();
            drawScene();
        }
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveLeft() {
        boolean needNewPlate = false;
        for (int[] row: gameField) {
            boolean isCompressed = compressRow(row);
            boolean isMerged = mergeRow(row);

            if (isMerged) {
                compressRow(row);
            }

            if (isCompressed || isMerged) {
                needNewPlate = true;
            }
        }

        if (needNewPlate) {
            createNewNumber();
        }
    }

    private void rotateClockwise() {
        int[][] result = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                result[j][SIDE - 1 - i] = gameField[i][j];
            }
        }
        gameField = result;
    }

    private int getMaxTileValue() {
        int maxValue = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > maxValue) maxValue = gameField[i][j];
            }
        }

        return maxValue;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Поздравляем! Вы победили!", Color.BLACK, 50);
    }

    private boolean canUserMove() {
        boolean isCanUserMove = false;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) { isCanUserMove = true; }
                else if (i < SIDE-1 && gameField[i][j] == gameField[i+1][j] && !isCanUserMove) { isCanUserMove = true; }
                else if (j < SIDE-1 && gameField[i][j] == gameField[i][j+1] && !isCanUserMove) { isCanUserMove = true; }
            }
        }

        return isCanUserMove;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Вы проиграли!", Color.BLACK, 50);
    }

    @Override
    public void onTurn(int step) {
        setScore(score);
    }
}
