package mmt007_backup.sharkfactions.models;

public class Invite {
    private String UUID;
    private InviteType Type;

    public Invite(String UUID, InviteType type) {
        this.UUID = UUID;
        Type = type;
    }

    public String getUUID() {return UUID;}

    public void setUUID(String UUID) {this.UUID = UUID;}

    public InviteType getType() {return Type;}

    public void setType(InviteType type) {Type = type;}

    public static Invite getEmpty(){return new Invite("",InviteType.NONE);}
}
