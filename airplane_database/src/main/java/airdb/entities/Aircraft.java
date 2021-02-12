package airdb.entities;

public class Aircraft {
    private final String code;
    private final String desc;
    private final int range;

    public Aircraft(String code, String desc, int range) {
        this.code = code;
        this.desc = desc;
        this.range = range;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public int getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", range=" + range +
                '}';
    }
}
