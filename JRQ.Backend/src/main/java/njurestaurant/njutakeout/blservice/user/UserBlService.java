package njurestaurant.njutakeout.blservice.user;

import njurestaurant.njutakeout.entity.user.Classification;
import njurestaurant.njutakeout.entity.user.ClassificationDescription;
import njurestaurant.njutakeout.exception.CannotGetOpenIdAndSessionKeyException;
import njurestaurant.njutakeout.exception.CardLimitUseUpException;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.BoolResponse;
import njurestaurant.njutakeout.response.InfoResponse;
import njurestaurant.njutakeout.response.account.OpenIdAndSessionKeyResponse;
import njurestaurant.njutakeout.response.user.*;

import java.util.List;

public interface UserBlService {


	/*------------ 管理员使用的API ------------*/


	/**
	 * 添加用户(Admin)
	 * @param openid 微信openid
	 * @param username 用户名
	 * @param face 用户头像
	 * @param medals 用户类别提示（可多个）
	 * @param phone 电话号码
	 * @param email 电子邮件
	 * @param company 公司名称
	 * @param department 部门
	 * @param position 职位
	 * @param intro 个人简介
	 * @param city 所在城市
	 * @param credit 账户余额
	 * @param label 用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照
	 * @param levelName 用户会员等级名称
	 * @param valid 是否冻结/启用，true代表启用
	 * @return 是否成功
	 */
	InfoResponse addUser(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label, List<String> label2,String levelName, boolean valid) throws NotExistException;

	/**
	 * 根据微信openid获取用户信息(Admin)
	 * @param openid 微信的openid
	 * @return 用户信息
	 */
	UserResponse getUser(String openid) throws NotExistException;

	/**
	 * 获取用户列表(Admin)
	 * @return 用户列表
	 */
	UserListResponse getUserList();

	/**
	 * 根据微信openid更新用户信息(Admin)
	 * @param openid 微信openid
	 * @param username 用户名
	 * @param face 用户头像
	 * @param medals 用户类别提示（可多个）
	 * @param phone 电话号码
	 * @param email 电子邮件
	 * @param company 公司名称
	 * @param department 部门
	 * @param position 职位
	 * @param intro 个人简介
	 * @param city 所在城市
	 * @param credit 账户余额
	 * @param label 用户类别信息，可取值：融资租赁，商业保理，地产交易，金融牌照
	 * @param levelName 用户的会员等级名称，可取值：common，298，998
	 * @param valid 是否冻结/启用，true代表启用
	 * @return 是否成功
	 */
	InfoResponse updateUser(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label,List<String> label2, String levelName, boolean valid) throws NotExistException;

	/**
	 * 根据微信openid删除用户(Admin)
	 * @param openid 微信openid
	 * @return 是否成功
	 */
	InfoResponse deleteUser(String openid) throws NotExistException;

	/**
	 * 添加用户标签相应的业务分类项(Admin)
	 * @param userLabel 用户标签：融资租赁，商业保理，地产交易，金融牌照
	 * @param workClass 业务分类：capital, stock, merge分别代表金融类，股票类，并购类
	 * @return 是否成功
	 */
	InfoResponse addClassification(String userLabel, String workClass);

	/**
	 * 根据用户标签获取其对应的业务分类(User&Admin)
	 * @param userLabel 用户标签
	 * @return 分类项
	 */
	ClassificationResponse getClassification(String userLabel) throws NotExistException;

	/**
	 * 获取所有业务分类项(User&Admin)
	 * @return 业务分类项列表
	 */
	ClassificationListResponse getClassificationList();

	/**
	 * 更新特定用户标签所属的业务分类(Admin)
	 * @param userLabel 用户标签
	 * @param workClass 业务分类
	 * @return 是否成功
	 */
	InfoResponse updateClassification(String userLabel, String workClass) throws NotExistException;

	/**
	 * 删除特定用户标签的分类项(Admin)
	 * @param userLabel 用户标签
	 * @return 是否成功
	 */
	BoolResponse deleteClassification(String userLabel) throws NotExistException;

	/**
	 * 获取类别对应的中文描述
	 * @return 类别-中文描述表
	 */
	ClassificationDescriptionListResponse getClassificationDescriptionList();

	/**
	 * 修改特定类别的中文描述
	 * @param workClass 要修改的类别
	 * @param description 新的中文描述
	 * @return 是否操作成功
	 */
	BoolResponse updateClassificationDescription(String workClass, String description);

	/**
	 * 添加会员等级信息(Admin)
	 * @param name 会员等级名称
	 * @param cardLimit 每天能看的名片数
	 * @param price 价格
	 * @param courseDiscountedRatio 该会员级别购买课程时的折扣率，价格是原价的courseDiscountedRatio倍
	 * @param checkCardPrice 查看名片联系方式次数用完以后再查看联系方式需要花多少钱
	 * @return 是否成功
	 */
	InfoResponse addLevel(String name, int cardLimit, int price, double courseDiscountedRatio, int checkCardPrice);

	/**
	 * 获取所有会员等级信息(User&Admin)
	 * @return 会员等级信息
	 */
	LevelListResponse getLevelList();

	/**
	 * 修改某会员等级的信息(Admin)
	 * @param name 会员等级名称
	 * @param cardLimit 新的每天能看的名片数
	 * @param price 新的价格
	 * @param courseDiscountedRatio 该会员级别购买课程时的折扣率，价格是原价的courseDiscountedRatio倍
	 * @param checkCardPrice 查看名片联系方式次数用完以后再查看联系方式需要花多少钱
	 * @return 是否成功
	 */
	InfoResponse updateLevel(String name, int cardLimit, int price, double courseDiscountedRatio, int checkCardPrice) throws NotExistException;

	/**
	 * 删除某会员等级(Admin)
	 * @param name 要被删除的会员等级名称
	 * @return 是否成功
	 */
	InfoResponse deleteLevel(String name) throws NotExistException;


	/*------------ 用户使用的API ------------*/


	/**
	 * 用户登录小程序，若账号不存在则新建一个（初始用户名与头像都与微信一致），否则不处理(User)
	 * @param openid 用户的微信openid
	 * @param username 初始用户名
	 * @param faceWxUrl 用户微信头像的URL
	 * @return 用户个人信息与token
	 */
	UserLoginResponse loginMyUser(String openid, String username, String faceWxUrl) throws NotExistException;

	/**
	 * 小程序前端获取openid和session
	 * @param jsCode 微信小程序的jsCode
	 * @return openid和session信息
	 */
	OpenIdAndSessionKeyResponse getOpenIdAndSessionKey(String jsCode) throws CannotGetOpenIdAndSessionKeyException;

	/**
	 * 小程序前端获取微信小程序的二维码
	 * @param scene 参数，对应微信API的scene
	 * @param page 跳转的页面，对应微信API的page
	 * @param width 二维码的宽度，对应微信API的width
	 * @param autoColor 是否自动配置线条颜色，对应微信API的auto_color
	 * @param lineColorR 当autoColor为false时有效，对应微信API的line_color中的r
	 * @param lineColorG 当autoColor为false时有效，对应微信API的line_color中的g
	 * @param lineColorB 当autoColor为false时有效，对应微信API的line_color中的b
	 * @param isHyaline 是否需要透明底色，对应微信API的is_hyaline
	 * @return Base64编码的图片
	 */
	QrCodeResponse getWxQrCode(String scene, String page, int width, boolean autoColor, String lineColorR, String lineColorG, String lineColorB, boolean isHyaline);

	/**
	 * 用户获取自己的个人信息(User)
	 * @param openid 用户的微信openid
	 * @return 用户个人信息
	 */
	UserResponse getMyUser(String openid) throws NotExistException;

	/**
	 * 用户修改自己的个人信息，只能改部分属性(User)
	 * @param openid 用户微信openid
	 * @param username 用户名
	 * @param face 用户头像
	 * @param phone 用户手机号
	 * @param email 用户邮箱
	 * @param company 所在公司
	 * @param department 所在部门
	 * @param position 职位
	 * @param intro 个人简介
	 * @param city 城市
	 * @param label 用户标签
	 * @return 是否成功
	 */
	InfoResponse updateMyProfile(String openid, String username, String face, String phone, String email, String company, String department, String position, String intro, String city, List<String> label,List<String> label2) throws NotExistException;

	/**
	 * 根据用户微信openid获取其业务名片(User)
	 * @param openid 用户微信openid
	 * @return 业务名片
	 */
	PersonResponse getPerson(String openid) throws NotExistException;

	/**
	 * 获取业务名片列表(User)
	 * @param workClass 业务类别：capital, stock, merge分别代表金融类，股票类，并购类
	 * @return 业务名片列表
	 */
	PersonListResponse getPersonList(String workClass) throws NotExistException;

	/**
	 * 获取符合特定条件的业务名片列表(User)
	 * @param condition 条件
	 * @return 业务名片列表
	 */
	PersonListResponse getPersonListByCondition(String condition);

	/**
	 * 用户向别人发送名片(User)
	 * @param senderOpenid 发送者微信openid
	 * @param receiverOpenid 接收者微信openid
	 * @param page 微信模板消息接口中要跳转的页面
	 * @param formId 微信模板消息接口参数
	 * @param data 微信模板消息接口内容
	 * @param emphasisKeyword 微信模板消息接口放大的关键词
	 * @return 是否成功
	 */
	BoolResponse sendMyCard(String senderOpenid, String receiverOpenid, String page, String formId, String data, String emphasisKeyword);

	/**
	 * 用户获取自己的名片列表(User)
	 * @param openid 用户的微信openid
	 * @param kind 名片类型 "new","current","whoHasMyCard"
	 * @return 特定类型的名片列表
	 */
	CardListResponse getMyCardList(String openid, String kind) throws NotExistException;

	/**
	 * 将用户收到的名片设置为已读(User)
	 * @param senderOpenid 发送者微信openid
	 * @param receiverOpenid 接收者微信openid
	 * @return 是否成功
	 */
	InfoResponse checkMyReceivedCard(String senderOpenid, String receiverOpenid) throws NotExistException;

	/**
	 * 用户是否可以直接查看别人的名片，而不需要消耗查看次数
	 * other为用户自己或在用户收到的名片里时返回true
	 * 注意：这里的实现应当与getOtherCard里面的实现保持一致
	 * @param userOpenid 用户的微信openid
	 * @param otherOpenid 要查看的别人的微信openid
	 * @return 是否可以
	 */
	BoolResponse isOtherCardAccessible(String userOpenid, String otherOpenid);

	/**
	 * 用户查看别人的名片，每天次数有限制(User)
	 * @param userOpenid 用户自己的微信openid
	 * @param otherOpenid 要查看的用户的openid
	 * @return 别人的名片
	 */
	CardResponse getOtherCard(String userOpenid, String otherOpenid) throws NotExistException;

	/**
	 * 获取用户收到的名片数量
	 * @param openid 用户的微信openid
	 * @return 用户收到名片的数量
	 */
	String getMyReceivedCardNum(String openid);

	/**
	 * 获取用户互持名片的数量
	 * @param openid 用户的微信openid
	 * @return 用户互持名片的数量
	 */
	String getMyMutualCardNum(String openid);

}
