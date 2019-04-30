package dev.raghav.sael.ModelClass;

public class LevelListModel {

    private String masterdata_id;
    private String m_name;
    private String type;


    public LevelListModel(String masterdata_id, String m_name, String type) {

        this.m_name=m_name;
        this.masterdata_id=masterdata_id;
        this.type=type;

    }

    public String getMasterdata_id() {
        return masterdata_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setMasterdata_id(String masterdata_id) {
        this.masterdata_id = masterdata_id;
    }
}
