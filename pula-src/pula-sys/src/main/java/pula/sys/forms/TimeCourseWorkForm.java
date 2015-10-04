package pula.sys.forms;

import pula.sys.domains.TimeCourseWork;

public class TimeCourseWorkForm extends TimeCourseWork {

    private String workEffectDateText;

    // the given picture
    private FileAttachmentForm attachmentForms;

    public FileAttachmentForm getAttachmentForms() {
        return attachmentForms;
    }

    public void setAttachmentForms(FileAttachmentForm attachmentForms) {
        this.attachmentForms = attachmentForms;
    }

    public TimeCourseWork toWork() {
        TimeCourseWork work = new TimeCourseWork(this);
        work.setWorkEffectDate(TimeCourseForm.getDate(workEffectDateText));
        return work;
    }

    public String getWorkEffectDateText() {
        return workEffectDateText;
    }

    public void setWorkEffectDateText(String workEffectDateText) {
        this.workEffectDateText = workEffectDateText;
    }
}
