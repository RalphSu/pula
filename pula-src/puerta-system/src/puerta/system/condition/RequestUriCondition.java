package puerta.system.condition;

public class RequestUriCondition {

	private String uri;
	private int assignedCount = -1;
	private boolean forceNoPurview, forceNoOnline;

	public boolean isForceNoPurview() {
		return forceNoPurview;
	}

	public void setForceNoPurview(boolean forceNoPurview) {
		this.forceNoPurview = forceNoPurview;
	}

	public boolean isForceNoOnline() {
		return forceNoOnline;
	}

	public void setForceNoOnline(boolean forceNoOnline) {
		this.forceNoOnline = forceNoOnline;
	}

	public int getAssignedCount() {
		return assignedCount;
	}

	public void setAssignedCount(int assignCount) {
		this.assignedCount = assignCount;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
