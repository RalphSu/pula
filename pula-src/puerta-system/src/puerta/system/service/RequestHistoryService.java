package puerta.system.service;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import puerta.PuertaWeb;
import puerta.support.service.SessionBox;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.RequestHistoryDao;
import puerta.system.po.Purview;
import puerta.system.po.RequestHistory;
import puerta.system.vo.AppFieldData;

@Service
public class RequestHistoryService {

	@Resource
	private RequestHistoryDao requestHistoryDao;
	@Resource
	private PurviewDao purviewDao;

	private String buildExtras(HttpServletRequest request) {
		Map<String, String[]> pm = (Map<String, String[]>) request.getParameterMap();
		StringBuilder sb = new StringBuilder();
		for (String k : pm.keySet()) {
			if (StringUtils.equals(k, "_date")) {
				continue;
			}
			if (StringUtils.equals(k, "_json")) {
				continue;
			}			
			String v = request.getParameter(k);
			if (StringUtils.isEmpty(v)) {
				continue;
			}
			sb.append(k).append("=").append(v).append(";");
		}
		return sb.toString();
	}

	@Transactional
	public void record(SessionBox sessionBox, String processUri,
			AppFieldData afData, HttpServletRequest request) {

		String zipURL = StringUtils.right(processUri, processUri.length()
				- afData.getPath().length() - 1);

		Purview p = purviewDao.getByDefaultURL(zipURL, afData.getNo());
		if (p == null) {
			// pick from session
			p = Purview.create((String) request.getSession().getAttribute(
					PuertaWeb.SESSION_PURVIEW));
		} else {
			// save to session
			request.getSession().setAttribute(PuertaWeb.SESSION_PURVIEW,
					p.getId());
		}

		RequestHistory rh = new RequestHistory();
		rh.setPurview(p);
		if (sessionBox != null) {
			rh.setActorId(sessionBox.getId());
		}
		rh.setIp(request.getRemoteAddr());
		rh.setUrl(processUri);
		rh.setExtras(StringUtils.left(buildExtras(request), 500));

		requestHistoryDao.save(rh);

	}

}
