/**
 * 
 */
package pula.sys.app;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.AttachmentFile;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 */
@Controller
public class ImageController {
    @Resource
    ParameterKeeper parameterKeeper;
    @Resource
    SessionService sessionService;
    @Resource
    SessionUserService sessionUserService;
    @Resource
    FileAttachmentDao fileAttachmentDao;

    @RequestMapping
    @ResponseBody
    @Barrier(PurviewConstants.IMAGE)
    public AttachmentFile icon(@RequestParam(value = "fp", required = false) String fp,
            @RequestParam(value = "sub", required = false) String sub, HttpServletResponse res) {
        // simply check the path to avoid client traversing
        if (sub.contains("/") || sub.contains(File.separator) || sub.contains("\\")) {
            Pe.raise("sub folder should be simple controller names, not allow special characters!");
        }

        String srcPath = null;
        if (StringUtils.isEmpty(sub)) {
            srcPath = parameterKeeper.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
        } else {
            String basePath = parameterKeeper.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
            int idx = basePath.lastIndexOf(File.separatorChar);
            if (idx >= 0) {
                srcPath = basePath.substring(0, idx) + File.separator + sub;
            } else {
                srcPath = basePath;
            }
        }
        String fullPath = srcPath + File.separatorChar + fp;

        return AttachmentFile.forShow(new File(fullPath));
    }
}
