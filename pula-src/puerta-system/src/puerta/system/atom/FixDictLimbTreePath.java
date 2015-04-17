package puerta.system.atom;

import org.springframework.web.multipart.MultipartFile;

import puerta.system.service.FixService;
import puerta.system.vo.ControlAtomBase;

public class FixDictLimbTreePath extends ControlAtomBase {

	private FixService fixServiceLaputa;

	public FixService getFixServiceLaputa() {
		return fixServiceLaputa;
	}

	public void setFixServiceLaputa(FixService fixServiceLaputa) {
		this.fixServiceLaputa = fixServiceLaputa;
	}

	@Override
	public String execute(MultipartFile file, String textValue)
			throws Exception {
		fixServiceLaputa.fixTreePath();
		return "OK";
	}

}
