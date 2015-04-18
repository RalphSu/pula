package pula.sys.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import puerta.support.Pe;
import puerta.system.vo.MapBean;
import pula.sys.daos.OrderFormDao;

@Service
public class OrderFormService {

	@Resource
	OrderFormDao orderFormDao;

	public void consume(long studentId) {

		// 找到订单
		MapBean meta = orderFormDao.meta4consume(studentId);

		if (meta == null) {
			Pe.raise("该学员无有效订单 (状态为确认)");
		}

		// 看看数量咋样,用的差不多就关掉
		int qty = meta.asInteger("courseCount");
		int cqty = meta.asInteger("consumeCourseCount");

		if (qty == cqty + 1) {
			// 该关了
			orderFormDao.complete(meta.asLong("id"));
		} else {
			orderFormDao.consume(meta.asLong("id"));
		}
	}

	public void restore(Long studentId) {
		// 找到订单
		MapBean meta = orderFormDao.meta4consumeRestore(studentId);

		if (meta == null) {
			Pe.raise("该学员无有效订单 (状态为已关闭或确认)");
		}

		orderFormDao.consumeRestore(meta.asLong("id"));

	}

}
