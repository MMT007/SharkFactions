package mmt007_backup.sharkfactions.models;

public class Players {
    private String uuid;
    private String fuuid;
    private Invite invite;


    public Players(String uuid, String fuuid, Invite invite) {
        this.uuid = uuid;
        this.fuuid = fuuid;
        this.invite = invite;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFuuid() {
        return this.fuuid;
    }

    public void setFuuid(String fuuid) {
        this.fuuid = fuuid;
    }

    public Invite getInvite() {
        return this.invite;
    }

    public void setInvite(Invite inviteUUID) {
        this.invite = inviteUUID;
    }

}
