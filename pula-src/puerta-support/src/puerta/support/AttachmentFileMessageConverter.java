package puerta.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import puerta.support.utils.FileHelper;

public class AttachmentFileMessageConverter extends
		AbstractHttpMessageConverter<AttachmentFile> {

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (AttachmentFile.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	protected AttachmentFile readInternal(
			Class<? extends AttachmentFile> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	private static final String CONTENT_DISPOSITION = "Content-Disposition";

	@Override
	protected void writeInternal(AttachmentFile t,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		try {
			HttpHeaders headers = outputMessage.getHeaders();
			if (t.getMediaType() != null) {
				headers.setContentType(t.getMediaType());
			}

			StringBuilder sb = new StringBuilder(100);
			if (t.isAttachment())
				sb.append("attachment;");
			sb.append("filename=").append(
					URLEncoder.encode(t.getFileName(), "utf-8"));

			headers.add(CONTENT_DISPOSITION, sb.toString());

			File file = new File(t.getFilePath());

			headers.add("Content-Length", String.valueOf(file.length()));

			OutputStream sos = outputMessage.getBody();

			InputStream pi = new FileInputStream(file);
			int blockLength = 2048;
			byte[] blobbytes = new byte[blockLength];
			int bytesRead = 0;

			while (-1 != (bytesRead = pi.read(blobbytes, 0, blockLength))) {
				sos.write(blobbytes, 0, bytesRead);
			}
			pi.close();
			sos.flush();
		} finally {
			if (t != null) {
				if (t.isBurnAfterReading()) {
					FileHelper.delete(t.getFilePath());
				}
			}

		}

	}
}
