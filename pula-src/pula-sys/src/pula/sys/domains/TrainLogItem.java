package pula.sys.domains;

public class TrainLogItem {

	private long id;
	private TrainLog trainLog;
	private Teacher teacher;
	private int points, indexNo; // 分数

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TrainLog getTrainLog() {
		return trainLog;
	}

	public void setTrainLog(TrainLog trainLog) {
		this.trainLog = trainLog;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
