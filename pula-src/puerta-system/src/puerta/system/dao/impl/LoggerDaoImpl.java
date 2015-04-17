/**
 * Created on 2008-12-18 02:44:31
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import puerta.support.annotation.MlsDomain;
import puerta.support.annotation.WxlDomain;
import puerta.support.dao.DaoException;
import puerta.support.dao.IWxlActor;
import puerta.support.dao.IWxlLogger;
import puerta.support.dao.LoggablePo;
import puerta.support.mls.Mls;
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.LoggerClassKeeper;
import puerta.system.service.SessionService;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class LoggerDaoImpl extends HibernateDaoSupport implements LoggerDao {

	@Resource
	private SessionService sessionService;
	@Resource
	private LoggerClassKeeper loggerClassKeeper;
	@Resource
	private Mls mls;

	@Resource(name = "sessionFactory")
	public void setBaseDaoSessionFactory(SessionFactory sf) {
		super.setSessionFactory(sf);
	}

	public Mls getMls() {
		return mls;
	}

	private IWxlLogger doLog(String event, String extendInfo, String actorId,
			IWxlLogger log) {

		// log.init(sessionService);
		log.setEvent(event);
		log.setEventTime(Calendar.getInstance());
		log.setExtendInfo(extendInfo);
		log.setIp(sessionService.env().getIp());
		log.setActorId(actorId);

		getHibernateTemplate().save(log);
		return log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.LoggerDao#doLog(java.lang.String,
	 * java.lang.String)
	 */
	public void doLog(String event, String extendInfo) {
		String aid = sessionService.getActorId();
		if (StringUtils.isEmpty(aid))
			return;
		String ap = sessionService.env().getAppFieldNo();

		Class<? extends IWxlLogger> clz = loggerClassKeeper.getClass(ap);
		if (clz == null)
			return;
		try {
			IWxlLogger log = clz.newInstance();

			this.doLog(event, extendInfo, aid, log);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.LoggerMgr#doLog(java.lang.String)
	 */
	public void doLog(String string) {
		doLog(string, "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.LoggerMgr#doLog(java.lang.String,
	 * wxlplatform.po.Insider)
	 */
	public void doLog(String event, IWxlActor actor) {
		this.doLog(event, null, actor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.LoggerMgr#doLog(java.lang.String,
	 * java.lang.String, wxlplatform.logger.IWxlActor)
	 */
	public void doLog(String event, String extendInfo, IWxlActor actor) {
		if (actor == null) {
			throw new DaoException("actor is null");
		}
		IWxlLogger log = actor.getNewLogger();
		// log.init(sessionService);
		this.doLog(event, extendInfo, actor.getActorId(), log);
	}

	@Override
	public void doLog(String event, LoggablePo data) {
		if (data == null)
			return;
		if (data.getClass().isAnnotationPresent(WxlDomain.class)) {
			String v = data.getClass().getAnnotation(WxlDomain.class).value();
			if (data.getClass().isAnnotationPresent(MlsDomain.class)) {
				v = mls.t("log." + v);
			}

			if (!StringUtils.isEmpty(v)) {
				event += v;
			}
		}

		doLog(event, StringUtils.left(data.toLogString(), 50));
	}

}
