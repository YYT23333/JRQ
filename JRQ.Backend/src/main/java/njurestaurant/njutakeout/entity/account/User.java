package njurestaurant.njutakeout.entity.account;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "openid")
	private String openid;//用户微信编号

	@Column(name = "username")
	private String username; //用户名

	@Column(name = "face")
	private String face; //用户头像

	@Column(name = "medals")
	@ElementCollection(targetClass = String.class)
	private List<String> medals; //用户类别提示（可多个）

	@Column(name = "phone")
	private String phone; //电话号码

	@Column(name = "email")
	private String email; //电子邮件

	@Column(name = "company")
	private String company; //公司名称

	@Column(name = "department")
	private String department; //部门

	@Column(name = "position")
	private String position; //职位

	@Column(name = "intro")
	private String intro; //个人简介

	@Column(name = "city")
	private String city; //所在城市

	@Column(name = "credit")
	private int credit; //账户余额

	@Column(name = "label")
	private String label; //用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照

	@Column(name = "valid")
	private boolean valid;//是否冻结/启用，true代表启用

	public User() {
	}

	public User(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, String label, boolean valid) {
		this.openid = openid;
		this.username = username;
		this.face = face;
		this.medals = medals;
		this.phone = phone;
		this.email = email;
		this.company = company;
		this.department = department;
		this.position = position;
		this.intro = intro;
		this.city = city;
		this.credit = credit;
		this.label = label;
		this.valid = valid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public List<String> getMedals() {
		return medals;
	}

	public void setMedals(List<String> medals) {
		this.medals = medals;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
