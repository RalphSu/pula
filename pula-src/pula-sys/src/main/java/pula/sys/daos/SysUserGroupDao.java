package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.SysUserGroupCondition;
import pula.sys.domains.SysUserGroup;

public interface SysUserGroupDao extends BaseDao<SysUserGroup, String> {

	PaginationSupport<SysUserGroup> search(SysUserGroupCondition condition,
			int pageIndex);

	void save(SysUserGroup sysUserGroup);

	void update(SysUserGroup sysUserGroup);

	List<SysUserGroup> loadByKeywords(String no);

	String getIdByNo(String sysUserGroupSalesmen);

}
