package Games.Minesweeper;

public class GameObject {
    private int x, y, countMineNeighbors;
    private boolean isMine, isOpen, isFlag;

    public GameObject(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setCountMineNeighbors(int countMineNeighbors) {
        this.countMineNeighbors += countMineNeighbors;
    }

    public int getCountMineNeighbors() {
        return countMineNeighbors;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public boolean isFlag() {
        return isFlag;
    }
}