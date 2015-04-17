package puerta.support.dao;

import org.hibernate.criterion.Projection;

public interface HibernateInjectMapper {

	Projection make(String name, String aliasName);
}
