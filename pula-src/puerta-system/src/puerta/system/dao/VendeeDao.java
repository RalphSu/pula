package puerta.system.dao;

import puerta.support.dao.BaseDao;
import puerta.system.po.Vendee;

public interface VendeeDao extends BaseDao<Vendee, String> {

	Vendee get();


	void update(Vendee vendee);

}
