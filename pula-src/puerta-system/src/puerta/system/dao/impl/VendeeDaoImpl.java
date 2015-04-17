package puerta.system.dao.impl;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import puerta.PuertaWeb;
import puerta.support.Pe;
import puerta.support.utils.RegisterTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.VendeeDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.po.Vendee;

@Repository
public class VendeeDaoImpl extends HibernateGenericDao<Vendee, String>
		implements VendeeDao {

	@Resource
	private ParameterKeeper parameterKeeper;

	@Override
	public Vendee get() {
		String hql = "select u FROM Vendee u ";
		Vendee v = findSingle(hql);
		if (v == null) {
			v = new Vendee();
			String registerNo = parameterKeeper.getString(PuertaWeb.PUERTA_NO);
			v.setRegisterNo(registerNo);
			v.setMachineNo(RegisterTool.getMachineNo());

			getHibernateTemplate().save(v);
		}
		return v;
	}

	@Override
	public void update(Vendee vendee) {
		// check no
		// 检查注册码，机器码是否对应的上。

		Vendee v = get();
		RegisterTool.checkRegisterNo(vendee.getRegisterNo(), v.getMachineNo(),
				vendee.getName());

		Calendar cal = null;
		try {
			cal = RegisterTool.getExpiredTime(vendee.getRegisterNo());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Calendar lastDay = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -5);
		// if (cal == null || cal.before(lastDay)) {
		// Pe.raise("过期的注册码，或即将过期");
		// }

		v.setName(vendee.getName());
		v.setRegisterNo(vendee.getRegisterNo());
	}

}
