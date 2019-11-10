package abs.dto;

public class Ecu {
    private int ecuId;
    private int type;
    private String description;
    private String absId;

    public Ecu(int ecuId, int type, String description, String absId) {
        this.ecuId = ecuId;
        this.type = type;
        this.description = description;
        this.absId = absId;
    }

    public int getEcuId() {
        return ecuId;
    }

    public void setEcuId(int ecuId) {
        this.ecuId = ecuId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbsId() {
        return absId;
    }

    public void setAbsId(String absId) {
        this.absId = absId;
    }

    @Override
    public String toString() {
        return absId;
    }
}
