package org.sunyh.bean;

public class TimeStampField {
    private final String DATA_TYPE="TIMESTAMP(0)";
    private final String COLUMN_NAME;
    private final String DATA_DEFAULT;
    private final String NULLABLE;
    private final String COMMENTS;

    public TimeStampField(String COLUMN_NAME, String DATA_DEFAULT, String NULLABLE, String COMMENTS) {
        this.COLUMN_NAME = COLUMN_NAME;
        this.DATA_DEFAULT = DATA_DEFAULT;
        this.NULLABLE = NULLABLE;
        this.COMMENTS = COMMENTS;
    }

    public String getDATA_TYPE() {
        return DATA_TYPE;
    }

    public String getCOLUMN_NAME() {
        return COLUMN_NAME;
    }

    public String getDATA_DEFAULT() {
        return DATA_DEFAULT;
    }

    public String getNULLABLE() {
        return NULLABLE;
    }

    public String getCOMMENTS() {
        return COMMENTS;
    }
}
