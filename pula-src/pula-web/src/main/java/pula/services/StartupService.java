package pula.services;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import puerta.support.utils.FileHelper;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.keeper.LoggerClassKeeper;
import puerta.system.keeper.OrderNoMutexKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.ProjectPath;

@Service("PulaSysStartService")
public class StartupService implements SmartLifecycle {
	private static final Logger logger = Logger.getLogger(StartupService.class);

	public StartupService() {
	}

	@Resource
	AppFieldKeeper appFieldKeeper;
	@Resource
	LoggerClassKeeper loggerClassKeeper;

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

	public static final String AS_TEACHER = "teacher";
	public static final String AS_STUDENT = "studnet";

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (inited)
			return;
		inited = true;
		logger.debug("PULA WEB StartupService >>>>>");

		// loggerClassKeeper.addClass(PuertaWeb.AS_WEBUSER, SysUserLog.class);
		appFieldKeeper.register(AS_TEACHER, "/teacher");
		appFieldKeeper.register(AS_STUDENT, "/student");

		// sysUseDao.registerSetting();

		// sysCategoryDao.registerSetting();
		try {
			String webRoot = projectPath.getWebRoot();
			FileHelper.generateEmptyFile(webRoot, "server.startup");
		} catch (Exception e) {
			e.printStackTrace();
		}

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
