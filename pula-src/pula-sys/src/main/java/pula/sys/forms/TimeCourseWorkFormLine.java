package pula.sys.forms;

import pula.sys.domains.TimeCourseWork;

public class TimeCourseWorkFormLine extends TimeCourseWork {

    private String fileId;
    private String fileName;
    private int type;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
