package pula.sys.domains;

import java.util.Calendar;

//库存日志
public class StockLog {

	public static final int IN = 1;

	public static final int OUT = -1;

	private long id;
	private Calendar eventTime;

	private Material material;
	private int quantity;
	private Branch branch;

	private int type; // inbound outbound
	private SysUser creator;
	private Calendar createdTime;
	private String comments;

	private String outNo;// 内外单号
	private MaterialRequire materialRequire;
	private StockEvent stockEvent;

	public StockEvent getStockEvent() {
		return stockEvent;
	}

	public void setStockEvent(StockEvent stockEvent) {
		this.stockEvent = stockEvent;
	}

	public MaterialRequire getMaterialRequire() {
		return materialRequire;
	}

	public void setMaterialRequire(MaterialRequire materialRequire) {
		this.materialRequire = materialRequire;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser sysUser) {
		this.creator = sysUser;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar logTime) {
		this.eventTime = logTime;
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

	public StockLog copy() {
		StockLog sl = new StockLog();
		sl.setCreatedTime(this.createdTime);
		sl.setCreator(this.creator);
		sl.setEventTime(this.eventTime);
		sl.setOutNo(this.outNo);
		sl.setType(this.type);

		sl.setMaterial(this.material);
		sl.setQuantity(this.quantity);

		return sl;
	}

}
