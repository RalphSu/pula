package pula.sys.services;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pula.sys.BhzqConstants;

@Service
public class UtilsService {
	//
	// private static final String[] ALLOW_SUPPLY = new String[] {
	// BhzqConstants.ROLE_ADMIN, BhzqConstants.ROLE_PROJECT_DIRECTOR,
	// BhzqConstants.ROLE_PROJECT_MANAGER, BhzqConstants.ROLE_CEO,
	// BhzqConstants.ROLE_SUPPLY, BhzqConstants.ROLE_DESIGNER_MANAGER,
	// BhzqConstants.ROLE_STOCK, BhzqConstants.ROLE_MANUFACTURE_MANAGER,
	// BhzqConstants.ROLE_TECHNOLOGIST
	//
	// };
	// private static final String[] ALLOW_STOCK = new String[] {
	// BhzqConstants.ROLE_ADMIN, BhzqConstants.ROLE_PROJECT_DIRECTOR,
	// BhzqConstants.ROLE_PROJECT_MANAGER, BhzqConstants.ROLE_CEO,
	// BhzqConstants.ROLE_SUPPLY, BhzqConstants.ROLE_STOCK,
	// BhzqConstants.ROLE_DESIGNER_MANAGER };
	//
	// private static final String[] ALLOW_REPORT = new String[] {
	// BhzqConstants.ROLE_ADMIN, BhzqConstants.ROLE_PROJECT_DIRECTOR,
	// BhzqConstants.ROLE_PROJECT_MANAGER, BhzqConstants.ROLE_CEO };

	private static final String[] SUPER = new String[] {
			BhzqConstants.ROLE_ADMIN, BhzqConstants.ROLE_SALES_DIRECTOR,
			BhzqConstants.ROLE_TEACH_DIRECTOR, BhzqConstants.ROLE_CEO };

	@Resource
	SessionUserService sessionUserService;

	//
	// public TokenMenu getTokenMenu() {
	// if (sessionUserService.getRole() == null) {
	// return null;
	// }
	// String roleNo = sessionUserService.getRole().getNo();
	// TokenMenu menu = new TokenMenu();
	// // if (ArrayUtils.contains(ALLOW_SUPPLY, roleNo)) {
	// // menu.setShowSupply(true);
	// // }
	// // 大家都有这个菜单了。
	// menu.setShowSupply(true);
	//
	// if (ArrayUtils.contains(ALLOW_STOCK, roleNo)) {
	// menu.setShowStock(true);
	// }
	//
	// if (ArrayUtils.contains(ALLOW_REPORT, roleNo)) {
	// menu.setShowReport(true);
	// }
	//
	// return menu;
	// }

	public boolean role(String r, boolean only) {
		if (sessionUserService.getRole() == null) {
			return false;
		}
		String roleNo = sessionUserService.getRole().getNo();

		boolean b = StringUtils.equals(r, roleNo);

		if (only || b) {
			return b;
		}

		return ArrayUtils.contains(SUPER, roleNo);

	}

	public boolean isHeadquarter() {
		return sessionUserService.isHeadquarter();
	}

}
