package puerta.system.keeper;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import puerta.PuertaWeb;
import puerta.support.utils.RegisterTool;
import puerta.system.dao.VendeeDao;
import puerta.system.po.Vendee;

@Service
public class VendeeKeeper {
	private static final Logger logger = Logger.getLogger(VendeeKeeper.class);

	@Resource
	private VendeeDao vendeeDao;
	private Vendee vendee;
	private boolean expired = false;
	private boolean invalid = false;
	private TimeChecker timeCheck;

	public synchronized void reload() {
		logger.trace("get single ...");
		vendee = vendeeDao.get();

		logger.trace("calculate ...");
		calculate();
		logger.trace("calculate done ...");
		// 定时检查
		if (timeCheck == null) {
			timeCheck = new TimeChecker(this);

			logger.trace("start thread check...");
			new Thread(timeCheck).start();
		}
		logger.trace("load done ...");
	}

	public synchronized void calculate() {
		// System.out.println("check");
		logger.trace("calculate begin ...");
		boolean e = false;
		boolean i = false;
		if (!RegisterTool
				.isRegisterNo(vendee.getRegisterNo(), vendee.getName())) {
			i = true;
		} else {
			Calendar cal = null;
			try {
				cal = RegisterTool.getExpiredTime(vendee.getRegisterNo());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (cal == null || cal.before(Calendar.getInstance())) {
				e = true;
			}
		}

		if (e != this.expired) {
			this.expired = e;
		}
		if (i != this.invalid) {
			this.invalid = i;
		}
	}

	public boolean isExpired() {
		return expired;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public String getVendeeName() {
		return this.vendee.getName();
	}

}

class TimeChecker implements Runnable {

	private VendeeKeeper vk;

	public TimeChecker(VendeeKeeper vk) {
		this.vk = vk;
	}

	private static final int INTERVAL = 10000;// 30 * 1000 * 60;

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(INTERVAL);
				vk.calculate();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
