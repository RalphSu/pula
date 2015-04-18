package pula.sys.forms;

import pula.sys.domains.Teacher;
import pula.sys.domains.TrainLogItem;

public class TrainLogItemForm extends TrainLogItem {

	private long teacherId;

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public TrainLogItem toTrainLogItem() {
		TrainLogItem obj = new TrainLogItem();
		obj.setId(this.getId());
		obj.setIndexNo(this.getIndexNo());
		obj.setPoints(this.getPoints());
		obj.setTeacher(Teacher.create(teacherId));
		obj.setTrainLog(this.getTrainLog());
		return obj;
	}

}
