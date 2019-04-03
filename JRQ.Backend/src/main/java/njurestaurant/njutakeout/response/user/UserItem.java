package njurestaurant.njutakeout.response.user;

import njurestaurant.njutakeout.dataservice.user.EnterpriseDataService;
import njurestaurant.njutakeout.entity.user.User;

import java.util.List;

public class UserItem {
	private String openid;//用户微信编号
	private String username; //用户名
	private String face; //用户头像
	private List<String> medals; //用户类别提示（可多个）
	private String phone; //电话号码
	private String email; //电子邮件
	private String company; //公司名称
	private String department; //部门
	private String position; //职位
	private String intro; //个人简介
	private String city; //所在城市
	private int credit; //账户余额
	private List<String> label; //用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照
	private List<String> label2;
	private int cardLimit; //今天剩余查看别人名片的次数
	private String levelName; //用户所属会员等级
	private boolean isEnterprise; //是否为企业用户
	private boolean valid;//是否冻结/启用，true代表启用

	public UserItem(User user, EnterpriseDataService enterpriseDataService){
		this.openid = user.getOpenid();
		this.username = user.getUsername();
		this.face = user.getFace();
		this.medals = user.getMedals();
		this.phone = user.getPhone();
		this.email = user.getEmail();
		this.company = user.getCompany();
		this.department = user.getDepartment();
		this.position = user.getPosition();
		this.intro = user.getIntro();
		this.city = user.getCity();
		this.credit = user.getCredit();
		this.label = user.getLabel();
		this.label2=user.getLabel2();
		this.cardLimit = user.getCardLimit();
		switch(user.getLevelName()){
			case "VIP":
				this.levelName = "record/user/head/VIP-icon.png";
				break;
			case "普通会员":
				this.levelName = "record/user/head/普通会员-icon.png";
				break;
			default: ;
		}
		this.levelName = user.getLevelName();
		this.isEnterprise = enterpriseDataService.isUserEnterprise(user.getOpenid());
		this.valid = user.isValid();
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

	public boolean isEnterprise() {
		return isEnterprise;
	}

	public void setEnterprise(boolean enterprise) {
		isEnterprise = enterprise;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
