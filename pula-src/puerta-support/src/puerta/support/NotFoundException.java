package puerta.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends BasementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4227667638984698730L;

}
