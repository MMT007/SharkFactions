package mmt007_backup.sharkfactions.models;

public class FChunk {
    private int x;
    private int y;
    private String world;

    public FChunk(int x, int y, String world) {
        this.x = x;
        this.y = y;
        this.world = world;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
