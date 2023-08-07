package mmt007_backup.sharkfactions.models;

import java.util.ArrayList;

public class Factions {
    private String uuid;
    private String owner;
    private String name;
    private String tag;
    private int members;
    private ArrayList<FChunk> chunks;
    private ArrayList<String> allys;
    private ArrayList<String> enemies;
    private Invite invite;
    private FLocation baseLocation;

    public Factions(String uuid, String owner, String name, String tag, int members, ArrayList<FChunk> chunks, ArrayList<String> allys, ArrayList<String> enemies, Invite invite, FLocation baseLocation) {
        this.uuid = uuid;
        this.owner = owner;
        this.name = name;
        this.tag = tag;
        this.members = members;
        this.chunks = chunks;
        this.allys = allys;
        this.enemies = enemies;
        this.invite = invite;
        this.baseLocation = baseLocation;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getMembers() {
        return this.members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public ArrayList<FChunk> getChunks() {
        return this.chunks;
    }

    public void setChunks(ArrayList<FChunk> chunks) {
        this.chunks = chunks;
    }

    public ArrayList<String> getAllys() {
        return this.allys;
    }

    public void setAllys(ArrayList<String> allys) {
        this.allys = allys;
    }

    public ArrayList<String> getEnemies() {
        return this.enemies;
    }

    public void setEnemies(ArrayList<String> enemies) {
        this.enemies = enemies;
    }

    public Invite getInvite() {
        return this.invite;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public FLocation getBaseLocation() {
        return this.baseLocation;
    }

    public void setBaseLocation(FLocation baseLocation) {
        this.baseLocation = baseLocation;
    }
}
