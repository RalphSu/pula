package pula.sys.services;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import puerta.PuertaWeb;
import puerta.support.utils.FileHelper;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.keeper.LoggerClassKeeper;
import puerta.system.keeper.OrderNoMutexKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.ProjectPath;
import pula.sys.BhzqConstants;
import pula.sys.daos.SysUserDao;
import pula.sys.domains.SysUserLog;

@Service("PulaSysStartService")
public class StartupService implements SmartLifecycle {
	private static final Logger logger = Logger.getLogger(StartupService.class);

	public StartupService() {
	}

	@Resource
	AppFieldKeeper appFieldKeeper;
	@Resource
	LoggerClassKeeper loggerClassKeeper;
	@Resource
	SysUserDao sysUseDao;
	// @Resource
	// AdminDao adminDao;
	@Resource
	ProjectPath projectPath;
	@Resource
	OrderNoMutexKeeper orderNoMutexKeeper;
	@Resource
	ParameterKeeper parameterKeeper;
	// @Resource
	// SysCategoryDao sysCategoryDao;
	// @Resource
	// SmsService smsService;

	private boolean inited = false;
	private boolean isRunning = false;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (inited)
			return;
		inited = true;
		logger.debug("Hg Sys StartupService >>>>>");

		loggerClassKeeper.addClass(PuertaWeb.AS_WEBUSER, SysUserLog.class);
		appFieldKeeper.register(PuertaWeb.AS_WEBUSER, "/app");
		sysUseDao.registerSetting();

		orderNoMutexKeeper.register(BhzqConstants.TICKET);
		orderNoMutexKeeper.register(BhzqConstants.TICKET_ERROR);

		// if (!parameterKeeper.getBoolean(ParameterHelper.DEV_MODE, false)) {
		// // projectPath..
		// PingHost3322 ph = new PingHost3322();
		// ph.setUserName("hg2012");
		// ph.setPassword("admin@nas");
		// ph.setDomain("hg2012.3322.org");
		// ph.start();
		// }

		// sysCategoryDao.registerSetting();
		try {
			String webRoot = projectPath.getWebRoot();
			FileHelper.generateEmptyFile("./", "server.startup");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// // 注册短信
		// EmppSmsGateway g = EmppSmsGateway.getInstance();
		// g.register(smsService);

	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	@Transactional
	public void start() {
		onApplicationEvent(null);

	}

	@Override
	public void stop() {
		isRunning = false;
	}

	@Override
	public int getPhase() {
		return 10;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable arg0) {
		arg0.run();
		isRunning = false;
	}
}
