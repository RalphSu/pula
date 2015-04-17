package puerta.system.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import puerta.system.dao.PurviewToRequestUriDao;
import puerta.system.po.RequestUri;

@Service
public class PurviewService {

	//
	// @Resource
	// private ParameterKeeper parameterKeeper;
	@Resource
	private PurviewToRequestUriDao purviewToRequestUriDao;

	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public boolean check(RequestUri ma, String id) {
		// final boolean const_pass_not_register = parameterKeeper.getBoolean(
		// ParameterHelper.PURVIEW_CHECK_PASS_NOTREGED, true);
		// final boolean force_check_session = parameterKeeper.getBoolean(
		// ParameterHelper.PURVIEW_CHECK_FORCE_SESSION, true);

		boolean checked = purviewToRequestUriDao.canAccess(ma, id);
		// if (!checked) {
		// Pe.raise("无访问权限，请联系管理员");
		// }
		return checked;
	}
}
