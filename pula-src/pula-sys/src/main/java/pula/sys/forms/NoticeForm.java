package pula.sys.forms;

import pula.sys.domains.Notice;

public class NoticeForm extends Notice {

    // the given picture
    private FileAttachmentForm attachmentForms;

    public FileAttachmentForm getAttachmentForms() {
        return attachmentForms;
    }

    public void setAttachmentForms(FileAttachmentForm attachmentForms) {
        this.attachmentForms = attachmentForms;
    }

    public Notice toNotice() {
        Notice work = new Notice(this);
        return work;
    }

}
