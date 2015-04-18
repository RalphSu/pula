package pula.sys.domains;

//活动所属区域
public class ActivityBranch {

	private long id;
	private Activity activity;
	private Branch branch;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity actitiy) {
		this.activity = actitiy;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

}
