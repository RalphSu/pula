package pula.sys.domains;

import java.util.Calendar;

import pula.sys.helpers.StudentPointsHelper;

public class StudentPoints {

	public static final int FROM_ORDER_FORM = 1;
	public static final int FORM_TAKE_CLASS = 2;
	public static final int FROM_COUSE_GAME = 3;
	public static final int FROM_MANUAL = 6;
	public static final int FROM_EXCHANGE = 7;

	public static final int MT_NONE = 0;

	private long id;
	private Student owner;
	private int fromType;
	private int minorType;

	private int points;

	private Calendar createdTime;
	private boolean removed;
	private SysUser admin;
	private String comments;
	private Gift gift;
	private String refId;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
	}

	public SysUser getAdmin() {
		return admin;
	}

	public void setAdmin(SysUser admin) {
		this.admin = admin;
	}

	public int getMinorType() {
		return minorType;
	}

	public void setMinorType(int minorType) {
		this.minorType = minorType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getFromName() {
		return StudentPointsHelper.getTypeName(this.fromType);
	}

	public static StudentPoints create(Student student, int fromOrderForm,
			int points2, String string, String refId) {
		StudentPoints sp = new StudentPoints();
		sp.owner = student;
		sp.fromType = fromOrderForm;
		sp.points = points2;
		sp.comments = string;
		sp.refId = refId;

		return sp;
	}
}
