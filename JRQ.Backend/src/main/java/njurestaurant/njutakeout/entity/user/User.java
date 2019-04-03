package njurestaurant.njutakeout.entity.user;

import njurestaurant.njutakeout.entity.job.JobCard;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {
	@Id
	@Column
	private String openid;//用户微信编号

	@Column
	private String username; //用户名

	@Column
	private String face; //用户头像

	@Column
	@ElementCollection(targetClass = String.class)
	private List<String> medals; //用户类别提示（可多个）

	@Column
	private String phone; //电话号码

	@Column
	private String email; //电子邮件

	@Column
	private String company; //公司名称

	@Column
	private String department; //部门

	@Column
	private String position; //职位

	@Column
	private String intro; //个人简介

	@Column
	private String city; //所在城市

	@Column
	private int credit; //账户余额

	@Column
	@ElementCollection(targetClass = String.class)
	private List<String> label; //用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照

	@Column
	@ElementCollection(targetClass = String.class)
	private List<String> label2; //用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照

	@Column
	private int cardLimit; //今天剩余查看别人名片的次数

	@Column
	private String levelName; //用户所属会员等级

	@Column
	private boolean valid;//是否冻结/启用，true代表启用

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<JobCard> jobCards;

	public User() {
	}

	public User(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label, List<String> label2,int cardLimit, String levelName, boolean valid) {
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
		this.label2=label2;
		this.cardLimit = cardLimit;
		this.levelName = levelName;
		this.valid = valid;
		this.jobCards=null;
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

	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> label) {
		this.label = label;
	}

	public List<String> getLabel2() {
		return label2;
	}

	public void setLabel2(List<String> label2) {
		this.label2 = label2;
	}

	public int getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(int cardLimit) {
		this.cardLimit = cardLimit;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public List<JobCard> getJobCards() {
		return jobCards;
	}

	public void setJobCards(List<JobCard> jobCards) {
		this.jobCards = jobCards;
	}
}
