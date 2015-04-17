package puerta.support;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;

public class AttachmentFile {

	public final static MediaType APPLICATION_XLS;
	public final static MediaType APPLICATION_CSV;
	public final static MediaType APPLICATION_UNKNOWN;

	static {
		APPLICATION_XLS = new MediaType("application", "xls");
		APPLICATION_CSV = new MediaType("application", "csv");
		APPLICATION_UNKNOWN = new MediaType("application", "unknown");
	}

	private MediaType mediaType;
	private boolean attachment;
	private String fileName;
	private String filePath;
	private boolean burnAfterReading;

	public boolean isBurnAfterReading() {
		return burnAfterReading;
	}

	public void setBurnAfterReading(boolean burnAfterRead) {
		this.burnAfterReading = burnAfterRead;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public boolean isAttachment() {
		return attachment;
	}

	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public static AttachmentFile create(File f, MediaType mt) {
		AttachmentFile af = new AttachmentFile();
		af.mediaType = mt;
		af.burnAfterReading = true;
		af.fileName = f.getName();
		af.filePath = f.getAbsolutePath();
		af.attachment = false;
		return af;

	}

	public static AttachmentFile attach(File file, MediaType mediaType2) {
		AttachmentFile af = create(file, mediaType2);
		af.attachment = true;
		return af;
	}

	public AttachmentFile rename(String string) {
		this.fileName = string;
		return this;
	}

	public static AttachmentFile forShow(File f) {
		AttachmentFile af = new AttachmentFile();
		af.mediaType = getMediaType(f);
		af.burnAfterReading = false;
		af.fileName = f.getName();
		af.filePath = f.getAbsolutePath();
		af.attachment = false;

		return af;
	}

	private static MediaType getMediaType(File f) {
		String extName = FilenameUtils.getExtension(f.getName());
		if (StringUtils.equalsIgnoreCase(extName, "JPG")
				|| StringUtils.equalsIgnoreCase(extName, "JPEG")) {
			return MediaType.IMAGE_JPEG;
		}
		if (StringUtils.equalsIgnoreCase(extName, "PNG")) {
			return MediaType.IMAGE_PNG;
		}

		if (StringUtils.equalsIgnoreCase(extName, "GIF")) {
			return MediaType.IMAGE_GIF;
		}
		return APPLICATION_UNKNOWN;
	}

}
