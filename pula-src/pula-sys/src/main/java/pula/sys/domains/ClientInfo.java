package pula.sys.domains;

/**
 * 客户信息
 * 
 * @author tiyi
 * 
 */
public class ClientInfo {

	private long id;
	private String name;
	private String mobile;
	private String phone;
	private int gender;
	private String address;

	private boolean createdTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(boolean createdTime) {
		this.createdTime = createdTime;
	}

}
