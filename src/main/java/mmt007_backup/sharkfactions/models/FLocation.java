package mmt007_backup.sharkfactions.models;

public class FLocation {
    private double x;
    private double y;
    private double z;
    private String world;

    public FLocation(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public static FLocation getEmpty(){
        return new FLocation(0d,0d,0d,"");
    }
}
