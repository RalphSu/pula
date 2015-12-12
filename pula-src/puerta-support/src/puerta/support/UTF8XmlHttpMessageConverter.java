package puerta.support;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

public class UTF8XmlHttpMessageConverter extends StringHttpMessageConverter {

	private static final Logger logger = LoggerFactory.getLogger(UTF8XmlHttpMessageConverter.class);
	public static final MediaType utf8 = new MediaType("text", "xml",
			Charset.forName("UTF-8"));

	private boolean writeAcceptCharset = true;

	public UTF8XmlHttpMessageConverter() {
		// super();
		// this.availableCharsets = new
		// ArrayList<Charset>(Charset.availableCharsets().values());

		setSupportedMediaTypes(Arrays.asList(utf8));
	}

	@Override
	protected MediaType getDefaultContentType(String dumy) {

		return utf8;

	}

	protected List<Charset> getAcceptedCharsets() {

		return Arrays.asList(utf8.getCharSet());

	}

	protected void writeInternal(String s, HttpOutputMessage outputMessage)

	throws IOException {

		if (this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}

		Charset charset = utf8.getCharSet();

		StringWriter writer = new StringWriter();
		FileCopyUtils.copy(s, writer);
		logger.info("xml output string: " + writer.getBuffer().toString());

		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(),
				charset));

	}

	public boolean isWriteAcceptCharset() {

		return writeAcceptCharset;

	}

	public void setWriteAcceptCharset(boolean writeAcceptCharset) {

		this.writeAcceptCharset = writeAcceptCharset;

	}

}
