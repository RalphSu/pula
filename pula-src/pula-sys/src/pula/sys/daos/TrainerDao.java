/**
 * Created on 2009-7-23
 * WXL 2009
 * $Id$
 */
package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.TrainerCondition;
import pula.sys.domains.Trainer;

/**
 * 
 * @author tiyi
 * 
 */
public interface TrainerDao extends BaseDao<Trainer, Long> {

	/**
	 * @param Trainer
	 * @return
	 */
	Trainer save(Trainer Trainer);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<Trainer> search(TrainerCondition condition, int pageNo);

	/**
	 * @param Trainer
	 * @return
	 */
	Trainer update(Trainer Trainer);

	/**
	 * @param no
	 * @return
	 */
	List<Trainer> loadByKeywords(String no);

	List<Trainer> loadByNo();

}
