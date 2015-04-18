package pula.sys.domains;

public class Stock {

	private long id;
	private Material material;
	private int quantity;
	private Branch branch;

	private boolean removed; // 删除 ，留个标记

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static Stock create(Long s) {
		if (s == null || s == 0)
			return null;
		Stock st = new Stock();
		st.id = s;
		return st;
	}

}
