package pula.sys.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import puerta.support.utils.FileHelper;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import pula.sys.BhzqConstants;
import pula.sys.domains.FileAttachment;
import pula.sys.forms.FileAttachmentForm;

/**
 * Uploaded file processor
 * 
 * @author Liangfei
 *
 */
public class FileUtil {

    /**
     * Convert the given attachment form to FileAttachment. This would move the files specify in the forms to the
     * given filePath, and create FileAttachment to record this targeted file.
     * 
     * @param filePath
     * @param parameterKeeper
     * @param attachmentForms
     * @return
     */
    public static List<FileAttachment> processFile(
            String filePath, 
            ParameterKeeper parameterKeeper,
            List<FileAttachmentForm> attachmentForms) {
        List<FileAttachment> attachments = WxlSugar.newArrayList();

        FileHelper.mkdir(filePath);

        String srcPath = parameterKeeper.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);

        if (attachmentForms != null) {

            // 文件要复制！
            for (FileAttachmentForm a : attachmentForms) {

                if (StringUtils.isEmpty(a.getFileId())) {
                    continue;
                }
                FileAttachment fa = new FileAttachment();
                fa.setExtName(FilenameUtils.getExtension(a.getFileName()));
                fa.setName(a.getFileName());
                fa.setType(a.getType());
                fa.setFileId(a.getFileId());

                if (a.getId() == 0) {
                    // build
                    String dest = filePath + File.separatorChar + a.getFileId();
                    File f = new File(srcPath + File.separatorChar + a.getFileId());
                    f.renameTo(new File(dest));

                } else {
                    fa.setId(a.getId());
                }
                attachments.add(fa);
            }
        }
        return attachments;
    }

}
