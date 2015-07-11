package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.TrainLog;

public class TrainLogForm extends TrainLog {
	private String trainDateText;

	public String getTrainDateText() {
		return trainDateText;
	}

	public void setTrainDateText(String trainDateText) {
		this.trainDateText = trainDateText;
	}

	public TrainLog toTrainLog() {
		TrainLog obj = new TrainLog();
		obj.setBranch(this.getBranch());
		obj.setComments(this.getComments());
		obj.setContent(this.getContent());
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setItems(this.getItems());
		obj.setLocation(this.getLocation());
		obj.setTrainDate(DateExTool.getDate(this.trainDateText));
		obj.setTrainer(this.getTrainer());
		obj.setUpdater(this.getUpdater());
		return obj;
	}

}
