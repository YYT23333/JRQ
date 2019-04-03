package njurestaurant.njutakeout.response.user;

import njurestaurant.njutakeout.dataservice.user.ClassificationDataService;
import njurestaurant.njutakeout.entity.user.User;
import njurestaurant.njutakeout.exception.NotExistException;

import java.util.List;

public class PersonItem {
	private String openid; //用户微信openid
	private String username; //用户名
	private String face; //用户头像
	private String company; //公司名称
	private String department; //部门
	private String position; //职位
	private String intro; //个人简介
	private String city; //所在城市
	private List<String> label; //用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照
	//private String bgColor; //标签背景颜色，对应Classification表中的color字段
	private List<String> label2;
	public PersonItem() {
	}

	public PersonItem(User user) {
		this.openid = user.getOpenid();
		this.username = user.getUsername();
		this.face = user.getFace();
		this.company = user.getCompany();
		this.department = user.getDepartment();
		this.position = user.getPosition();
		this.intro = user.getIntro();
		this.city = user.getCity();
		this.label = user.getLabel();
		this.label2=user.getLabel2();
//		try {
//			this.bgColor = classificationDataService.getClassificationByUserLabel(user.getLabel()).getColor();
//		} catch (NotExistException e) {
//			this.bgColor = "";
//		}
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

	//	public String getBgColor() {
//		return bgColor;
//	}
//
//	public void setBgColor(String bgColor) {
//		this.bgColor = bgColor;
//	}
}
