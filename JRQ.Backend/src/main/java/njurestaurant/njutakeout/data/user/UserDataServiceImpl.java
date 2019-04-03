package njurestaurant.njutakeout.data.user;

import njurestaurant.njutakeout.data.dao.user.FormIdDao;
import njurestaurant.njutakeout.data.dao.user.SendCardDao;
import njurestaurant.njutakeout.data.dao.user.UserDao;
import njurestaurant.njutakeout.dataservice.article.FeedDataService;
import njurestaurant.njutakeout.dataservice.user.EnterpriseDataService;
import njurestaurant.njutakeout.dataservice.user.UserDataService;
import njurestaurant.njutakeout.entity.user.*;
import njurestaurant.njutakeout.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class UserDataServiceImpl implements UserDataService {
	private final UserDao userDao;
	private final SendCardDao sendCardDao;
	private final FormIdDao formIdDao;
	private final FeedDataService feedDataService;
	private final EnterpriseDataService enterpriseDataService; //DataService中封装了相关数据连锁删除

	@Autowired
	public UserDataServiceImpl(UserDao userDao, SendCardDao sendCardDao, FormIdDao formIdDao, FeedDataService feedDataService, EnterpriseDataService enterpriseDataService) {
		this.userDao = userDao;
		this.sendCardDao = sendCardDao;
		this.formIdDao = formIdDao;
		this.feedDataService = feedDataService;
		this.enterpriseDataService = enterpriseDataService;
	}

	@Override
	public void saveUser(User user) {
		userDao.save(user);
	}

	@Override
	public void addUser(User user) {
		userDao.save(user);
	}

	@Override
	public User getUserByOpenid(String openid) throws NotExistException {
		Optional<User> optionalUser = userDao.findById(openid);
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			throw new NotExistException("用户openid", openid);
		}
	}

	@Override
	public List<User> getAllUsers() {
		return userDao.findAll();
	}

	@Override
	public void updateUserByOpenid(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label, List<String> label2,int cardLimit, String levelName, boolean valid) throws NotExistException {
		Optional<User> optionalUser = userDao.findById(openid);
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setUsername(username);
			user.setFace(face);
			user.setMedals(medals);
			user.setPhone(phone);
			user.setEmail(email);
			user.setCompany(company);
			user.setDepartment(department);
			user.setPosition(position);
			user.setIntro(intro);
			user.setCity(city);
			user.setCredit(credit);
			user.setLabel(label);
			user.setLabel2(label2);
			user.setCardLimit(cardLimit);
			user.setLevelName(levelName);
			user.setValid(valid);
			userDao.save(user);
		} else {
			throw new NotExistException("User openid", openid);
		}
	}

	@Override
	public void deleteUserByOpenid(String openid) throws NotExistException {
		Optional<User> optionalUser = userDao.findById(openid);
		if (optionalUser.isPresent()) {
			sendCardDao.deleteSendCardsByReceiverOpenid(openid);  //删除此人接收名片的记录
			sendCardDao.deleteSendCardsBySenderOpenid(openid);  //删除此人送给别人名片的记录
			feedDataService.deleteFeedsByWriterOpenid(openid);  //删除此人发过的圈子内容
			formIdDao.deleteAllByOpenid(openid); //删除此人的所有formId

			//若此人在Enterprise表中，需要删除该项
			try {
				Enterprise enterprise = enterpriseDataService.getEnterpriseByOpenid(openid);
				enterpriseDataService.deleteEnterpriseByIdCascade(enterprise.getId()); //连环删除Admin的相关数据
			} catch (NotExistException e) {
				//若此人不在Enterprise表中，什么也不需要做
			}

			//删除此人头像
			User user = optionalUser.get();
			if (!new File(user.getFace()).delete()) {
				System.err.println("头像文件"+user.getFace()+"删除失败！");
			}
			userDao.deleteById(openid);
		} else {
			throw new NotExistException("User openid", openid);
		}
	}

	@Override
	public boolean addSendCard(SendCard sendCard) {
		// 若已经送过名片，则无变化
		if (!sendCardDao.existsById(new SendCardKey(sendCard.getSenderOpenid(), sendCard.getReceiverOpenid()))) {
			sendCardDao.save(sendCard);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<SendCard> getSendsByOpenid(String openid) {
		return sendCardDao.findAllByReceiverOpenid(openid);//收到的名片
	}

	@Override
	public List<SendCard> getReceivesByOpenid(String openid) {
		return sendCardDao.findAllBySenderOpenid(openid);//发送的名片
	}

	@Override
	public void checkSendCard(SendCardKey sendCardKey) throws NotExistException {
		Optional<SendCard> optionalSendCard = sendCardDao.findById(sendCardKey);
		if(optionalSendCard.isPresent()) {
			SendCard sendCard = optionalSendCard.get();
			sendCard.setChecked(true);
			sendCardDao.save(sendCard);
		} else {
			throw new NotExistException("SendCard key", "(senderOpenid="+sendCardKey.getSenderOpenid()+",receiverOpenid="+sendCardKey.getReceiverOpenid()+")");
		}
	}

	@Override
	public boolean existsByLabel(String userLabel) {
		return userDao.existsByLabel(userLabel);
	}

	@Override
	public boolean addFormId(FormId formId) {
		if (formIdDao.existsById(new FormIdKey(formId.getOpenid(), formId.getFormId()))) {
			return false;
		} else {
			formIdDao.save(formId);
			return true;
		}
	}

	@Override
	public String getFormIdByOpenid(String openid) {
		formIdDao.deleteAllByTimestampBefore(System.currentTimeMillis()); //首先删除所有过期数据
		List<FormId> formIds = formIdDao.findAllByOpenidOrderByTimestamp(openid);
		if (formIds.isEmpty()) {
			return "";
		} else {
			String formId = formIds.get(0).getFormId();
			formIdDao.deleteById(new FormIdKey(openid, formId)); //已使用过的formId要被删除
			return formId;
		}
	}

}
