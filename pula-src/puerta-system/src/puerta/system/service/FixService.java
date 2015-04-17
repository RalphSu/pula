package puerta.system.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import puerta.system.dao.DictLimbDao;

@Service("fixServiceLaputa")
public class FixService {
	@Resource
	DictLimbDao dictLimbDao;

	@Transactional
	public void fixTreePath() {
		dictLimbDao.fixTreePath();

	}

}
