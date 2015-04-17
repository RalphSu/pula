package puerta.support.dao;

import java.io.Serializable;

public interface TreePathPo<T, ID extends Serializable> {

	public TreePathPo<T, ID> getParent();

	public void setParent(T p);

	public int getLevel();

	public void setLevel(int n);

	public int getIndexNo();

	public String getTreePath();

	public ID getIdentify();

	public void setTreePath(String string);

	public void setIndexNo(int indexNo);

}
