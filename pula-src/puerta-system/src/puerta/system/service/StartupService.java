package puerta.system.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import puerta.PuertaWeb;
import puerta.support.mls.Mls;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.keeper.DictLimbKeeper;
import puerta.system.keeper.LoggerClassKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.RequestUriKeeper;
import puerta.system.keeper.VendeeKeeper;
import puerta.system.po.InsiderLog;

@Service
public class StartupService implements SmartLifecycle {
	private static final Logger logger = Logger.getLogger(StartupService.class);

	public StartupService() {
	}

	@Resource
	AppFieldKeeper appFieldKeeper;
	@Resource
	LoggerClassKeeper loggerClassKeeper;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	RequestUriKeeper requestUriKeeper;
	@Resource
	DictLimbKeeper dictLimbKeeper;
	@Resource
	SettingService settingService;
	@Resource
	VendeeKeeper vendeeKeeper;
	@Resource
	Mls mls;

	private boolean inited = false;
	private boolean isRunning = false;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.debug("Puerta System StartupService >>>>>" + event);
		if (inited) {
			logger.debug("Inited");
			return;
		}
		inited = true;
		// mls.init();
		// appFieldKeeper.reload();

		settingService.registerDefault();

		loggerClassKeeper.addClass(PuertaWeb.AS_INSIDER, InsiderLog.class);
		appFieldKeeper.register(PuertaWeb.AS_INSIDER, "/laputa");

		parameterKeeper.reload();
		requestUriKeeper.reload();
		dictLimbKeeper.reload();
		vendeeKeeper.reload();

	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
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
