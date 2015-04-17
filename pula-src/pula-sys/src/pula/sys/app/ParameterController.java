package pula.sys.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.system.dao.ParameterDao;
import puerta.system.dao.ParameterFolderDao;
import puerta.system.dao.ParameterPageDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.ProjectPath;
import puerta.system.po.Parameter;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;
import pula.sys.PurviewConstants;

@Controller("sysUserParameter")
public class ParameterController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(ParameterController.class);

	@Resource
	private ParameterKeeper parameterKeeper;

	@Resource
	ParameterDao parameterDao;
	@Resource
	ParameterPageDao parameterPageDao;
	@Resource
	ParameterFolderDao parameterFolderDao;
	@Resource
	ProjectPath projectPath;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.PARAMETER)
	public ModelAndView setup(HttpServletRequest request) {
		ModelAndView m = new ModelAndView();
		String applicableScopeNo = PuertaWeb.AS_WEBUSER;
		List<ParameterPage> pages = parameterPageDao
				.loadPages(applicableScopeNo);
		m.addObject("pages", pages);
		Map<String, List<ParameterFolder>> folders = parameterFolderDao
				.loadFoldersByAppField(applicableScopeNo);
		m.addObject("folders", folders);

		Map<String, List<Parameter>> parameters = parameterDao
				.loadParameterByAppField(applicableScopeNo);
		m.addObject("parameters", parameters);

		String webRoot = projectPath.getWebRoot();
		String contextPath = request.getContextPath();

		// logger.debug(")

		return m.addObject("webRoot", webRoot).addObject("contextPath",
				contextPath);

	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.PARAMETER)
	public String _setup(@RequestParam("params") String[] params,
			@RequestParam("values") String[] values) {
		parameterKeeper.updateParams(params, values);
		return ViewResult.JSON_SUCCESS;
	}

}
