package mmt007_backup.sharkfactions.models;

import org.bukkit.Chunk;

import java.util.Arrays;
import java.util.Objects;

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

    public String getName(){return this.x + "_" + this.y + "_" + this.world;}

    public static FChunk fromBukkit(Chunk chunk){
        return new FChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        FChunk c = (FChunk) o;

        if(x != c.getX()) return false;
        if(y != c.getY()) return false;

        return Objects.equals(world, c.getWorld());
    }

    @Override
    public int hashCode(){
        int result = x;
        result = 31 * result + y;
        result = 31 * result + Arrays.hashCode(world.getBytes());

        return result;
    }
}
