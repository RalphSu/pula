package pula.sys.domains;

import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonProperty;

public class StudentCard {

    @JsonProperty
	private long id;
    @JsonProperty
	private Card card;
	private Student student;
	@JsonProperty
	private boolean current;
	@JsonProperty
	private Calendar createdTime;

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
