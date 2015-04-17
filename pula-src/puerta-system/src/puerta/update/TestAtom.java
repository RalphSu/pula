package puerta.update;

import org.springframework.web.multipart.MultipartFile;

import puerta.system.vo.ControlAtomBase;

public class TestAtom extends ControlAtomBase {

	@Override
	public String execute(MultipartFile file, String textValue)
			throws Exception {
		return "TEST OK file=" + file + " text=" + textValue;
	}

}
