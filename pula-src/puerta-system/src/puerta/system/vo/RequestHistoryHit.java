package puerta.system.vo;

public class RequestHistoryHit {

	private String uri;
	private int hits;
	private boolean stored;
	private boolean assigned;

	public boolean isStored() {
		return stored;
	}

	public void setStored(boolean stored) {
		this.stored = stored;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigend) {
		this.assigned = assigend;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public void incHits() {
		this.hits++;

	}

}
