package pula.sys.domains;

public class GiftStock {

	private long id;
	private Gift gift;
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

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static GiftStock create(Long s) {
		if (s == null || s == 0)
			return null;
		GiftStock st = new GiftStock();
		st.id = s;
		return st;
	}

}
