package njurestaurant.njutakeout.bl.user;

import njurestaurant.njutakeout.blservice.user.UserBlService;
import njurestaurant.njutakeout.data.dao.user.SendCardDao;
import njurestaurant.njutakeout.dataservice.count.CountDataService;
import njurestaurant.njutakeout.dataservice.user.ClassificationDataService;
import njurestaurant.njutakeout.dataservice.user.EnterpriseDataService;
import njurestaurant.njutakeout.dataservice.user.LevelDataService;
import njurestaurant.njutakeout.dataservice.user.UserDataService;
import njurestaurant.njutakeout.entity.count.Count;
import njurestaurant.njutakeout.entity.user.*;
import njurestaurant.njutakeout.exception.CannotGetOpenIdAndSessionKeyException;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.BoolResponse;
import njurestaurant.njutakeout.response.InfoResponse;
import njurestaurant.njutakeout.response.account.OpenIdAndSessionKeyResponse;
import njurestaurant.njutakeout.response.user.*;
import njurestaurant.njutakeout.security.jwt.JwtService;
import njurestaurant.njutakeout.security.jwt.JwtUser;
import njurestaurant.njutakeout.security.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class UserBlServiceImpl implements UserBlService {
    private final UserDataService userDataService;
    private final ClassificationDataService classificationDataService;
    private final LevelDataService levelDataService;
    private final EnterpriseDataService enterpriseDataService;
    private final SendCardDao sendCardDao;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtService jwtService;
    private final CountDataService countDataService;

    public UserBlServiceImpl(UserDataService userDataService, ClassificationDataService classificationDataService, LevelDataService levelDataService, EnterpriseDataService enterpriseDataService, SendCardDao sendCardDao, JwtUserDetailsService jwtUserDetailsService, JwtService jwtService, CountDataService countDataService) {
        this.userDataService = userDataService;
        this.classificationDataService = classificationDataService;
        this.levelDataService = levelDataService;
        this.enterpriseDataService = enterpriseDataService;
        this.sendCardDao = sendCardDao;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtService = jwtService;
        this.countDataService = countDataService;
    }

    @Override
    public InfoResponse addUser(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label,List<String> label2, String levelName, boolean valid) throws NotExistException {
        userDataService.addUser(new User(openid, username, face, medals, phone, email, company, department, position, intro, city, credit, label,label2,
                levelDataService.getLevelByName(levelName).getCardLimit(), levelName, valid));
        return new InfoResponse();
    }

    @Override
    public UserResponse getUser(String openid) throws NotExistException {
        return new UserResponse(new UserItem(userDataService.getUserByOpenid(openid), enterpriseDataService));
    }

    @Override
    public UserListResponse getUserList() {
        List<User> userList = userDataService.getAllUsers();
        List<UserItem> userItemList = new ArrayList<>();
        for (User user : userList) {
            userItemList.add(new UserItem(user, enterpriseDataService));
        }
        return new UserListResponse(userItemList);
    }

    @Override
    public InfoResponse updateUser(String openid, String username, String face, List<String> medals, String phone, String email, String company, String department, String position, String intro, String city, int credit, List<String> label,List<String> label2, String levelName, boolean valid) throws NotExistException {
        User user = userDataService.getUserByOpenid(openid);
        int used = levelDataService.getLevelByName(user.getLevelName()).getCardLimit() - user.getCardLimit(); //已经用掉的次数
        userDataService.updateUserByOpenid(openid, username, face, medals, phone, email, company, department, position, intro, city, credit, label,label2,
                levelDataService.getLevelByName(levelName).getCardLimit() - used, levelName, valid);
        return new InfoResponse();
    }

    @Override
    public InfoResponse deleteUser(String openid) throws NotExistException {
        userDataService.deleteUserByOpenid(openid);
        return new InfoResponse();
    }

    private static final String[] colorPool = {"rgba(255, 161, 177, 0.699)",
            "rgba(138, 138, 252, 0.767)",
            "rgba(109, 156, 90, 0.726)",
            "rgba(255, 58, 58, 0.678)"}; //业务标签的颜色值

    @Override
    public InfoResponse addClassification(String userLabel, String workClass) {
        classificationDataService.addClassification(
                new Classification(userLabel, workClass, colorPool[new Random().nextInt(colorPool.length)]));
        return new InfoResponse();
    }

    @Override
    public ClassificationResponse getClassification(String userLabel) throws NotExistException {
        return new ClassificationResponse(new ClassificationItem(
                classificationDataService.getClassificationByUserLabel(userLabel)));
    }

    @Override
    public ClassificationListResponse getClassificationList() {
        List<Classification> classificationList = classificationDataService.getAllClassifications();
        List<ClassificationItem> classificationItemList = new ArrayList<>();
        for (Classification classification : classificationList) {
            classificationItemList.add(new ClassificationItem(classification));
        }
        return new ClassificationListResponse(classificationItemList);
    }

    @Override
    public InfoResponse updateClassification(String userLabel, String workClass) throws NotExistException {
        classificationDataService.updateClassificationByUserLabel(userLabel, workClass);
        return new InfoResponse();
    }

    @Override
    public BoolResponse deleteClassification(String userLabel) throws NotExistException {
        if (userDataService.existsByLabel(userLabel)) {
            return new BoolResponse(false, "存在用户的标签为" + userLabel + "，不能删除");
        } else {
            classificationDataService.deleteClassificationByUserLabel(userLabel);
            return new BoolResponse(true, "用户标签" + userLabel + "删除成功");
        }
    }

    @Override
    public ClassificationDescriptionListResponse getClassificationDescriptionList() {
        List<ClassificationDescription> classificationDescriptions = classificationDataService.getAllClassificationDescriptions();
        List<ClassificationDescriptionItem> classificationDescriptionItems = new ArrayList<>();
        for (ClassificationDescription classificationDescription : classificationDescriptions) {
            classificationDescriptionItems.add(new ClassificationDescriptionItem(classificationDescription));
        }

        //浏览业务次数+1
        Count count = countDataService.getCountById(1);
        count.setViewBusiness(count.getViewBusiness() + 1);
        countDataService.saveCount(count);

        return new ClassificationDescriptionListResponse(classificationDescriptionItems);
    }

    @Override
    public BoolResponse updateClassificationDescription(String workClass, String description) {
        ClassificationDescription classificationDescription = null;
        try {
            classificationDescription = classificationDataService.getClassificationDescriptionByWorkClass(workClass);
            classificationDescription.setDescription(description);
            classificationDataService.saveClassificationDescription(classificationDescription);
            return new BoolResponse(true, "修改成功");
        } catch (NotExistException e) {
            return new BoolResponse(false, e.getMessage());
        }
    }

    @Override
    public InfoResponse addLevel(String name, int cardLimit, int price, double courseDiscountedRatio, int checkCardPrice) {
        levelDataService.addLevel(new Level(name, cardLimit, price, courseDiscountedRatio, checkCardPrice));
        return new InfoResponse();
    }

    @Override
    public LevelListResponse getLevelList() {
        List<Level> levels = levelDataService.getAllLevels();
        List<LevelItem> levelItems = new ArrayList<>();
        for (Level level : levels) {
            levelItems.add(new LevelItem(level));
        }
        return new LevelListResponse(levelItems);
    }

    @Override
    public InfoResponse updateLevel(String name, int cardLimit, int price, double courseDiscountedRatio, int checkCardPrice) throws NotExistException {
        levelDataService.updateLevelByName(name, cardLimit, price, courseDiscountedRatio, checkCardPrice);
        return new InfoResponse();
    }

    @Override
    public InfoResponse deleteLevel(String name) throws NotExistException {
        levelDataService.deleteLevelByName(name);
        return new InfoResponse();
    }

    private final static long EXPIRATION = 604800;

    @Override
    public UserLoginResponse loginMyUser(String openid, String username, String faceWxUrl) throws NotExistException {
        User user = null;
        try {
            user = userDataService.getUserByOpenid(openid);
        } catch (NotExistException exception) {
            int initCardLimit = levelDataService.getLevelByName("common").getCardLimit();
            List<String> defaultLabel = new ArrayList<>();
            List<Classification> classifications = classificationDataService.getAllClassifications();
            if (!classifications.isEmpty()) {
                defaultLabel.add(classifications.get(0).getUserLabel());
            }

            //设置初始头像为微信头像
            String faceLocalUrl = "record/user/head/" + UUID.randomUUID();
            try {
                URL url = new URL(faceWxUrl);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());

                FileOutputStream fileOutputStream = new FileOutputStream(new File(faceLocalUrl));
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
                dataInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            user = new User(openid, username, faceLocalUrl, new ArrayList<>(), "", "", "", "", "", "", "", 0, defaultLabel, defaultLabel,initCardLimit, "common", true);
            userDataService.addUser(user);
        }


        return new UserLoginResponse(new UserItem(user, enterpriseDataService));
    }

    @Value(value = "${wechat.url}")
    private String wechatUrl;

    @Value(value = "${wechat.id}")
    private String appId;

    @Value(value = "${wechat.secret}")
    private String appSecret;

    @Override
    public OpenIdAndSessionKeyResponse getOpenIdAndSessionKey(String jsCode) throws CannotGetOpenIdAndSessionKeyException {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = client.exchange(wechatUrl + appId + "&secret=" + appSecret + "&js_code=" + jsCode + "&grant_type=authorization_code", HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("jsCode = [" + jsCode + "]");
            System.out.println("hhhhhhh" + (String) JSONObject.fromObject(response.getBody()).get("openid"));
            System.out.println(response);
            String openid = (String) JSONObject.fromObject(response.getBody()).get("openid");
//            User user=null;
//			try {
//				user = userDataService.getUserByOpenid(openid);
//			} catch (NotExistException e) {
//				e.printStackTrace();
//			}

            //JwtUser jwtUser = (JwtUser) jwtUserDetailsService.loadUserByUsername(openid);
            String token = "";
            //token = jwtService.generateToken(jwtUser, EXPIRATION);

            return new OpenIdAndSessionKeyResponse(openid, (String) JSONObject.fromObject(response.getBody()).get("session_key"), token);
        } else {
            throw new CannotGetOpenIdAndSessionKeyException();
        }
    }

    @Override
    public QrCodeResponse getWxQrCode(String scene, String page, int width, boolean autoColor, String lineColorR, String lineColorG, String lineColorB, boolean isHyaline) {
        RestTemplate client = new RestTemplate();

        //获取accessToken
        String accessToken = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = client.exchange(
                "https://api.weixin.qq.com/cgi-bin/token?" + "&grant_type=client_credential&appid=" + appId + "&secret=" + appSecret, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            accessToken = (String) JSONObject.fromObject(response.getBody()).get("access_token");
        } else {
            System.err.println(response);
            return new QrCodeResponse(false, "access_token获取失败(" + response + ")", "");
        }

        //根据accessToken获取二维码图片
        String wxQrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> wxQrCodeParams = new HashMap<>();
        wxQrCodeParams.put("scene", scene);
        wxQrCodeParams.put("page", page);
        wxQrCodeParams.put("width", width);
        wxQrCodeParams.put("auto_color", autoColor);
        Map<String, Object> lineColor = new HashMap<>();
        lineColor.put("r", lineColorR);
        lineColor.put("g", lineColorG);
        lineColor.put("b", lineColorB);
        wxQrCodeParams.put("line_color", lineColor);
        wxQrCodeParams.put("is_hyaline", isHyaline);
        MultiValueMap<String, String> wxQrCodeHeaders = new LinkedMultiValueMap<>();
        HttpEntity wxQrCodeRequest = new HttpEntity(wxQrCodeParams, wxQrCodeHeaders);
        ResponseEntity<byte[]> wxQrCodeResponse = client.exchange(wxQrCodeUrl, HttpMethod.POST, wxQrCodeRequest, byte[].class);
        if (wxQrCodeResponse.getStatusCode() == HttpStatus.OK) {
            byte[] image = wxQrCodeResponse.getBody();
            final String dirPath = "record/user/qrcode/";
            File dirFile = new File(dirPath);
            if (!dirFile.exists() && !dirFile.mkdirs()) {
                return new QrCodeResponse(false, "二维码存储目录创建失败", "");
            }
            String imagePath = null;
            try {
                imagePath = dirPath + UUID.randomUUID();
                File imageFile = new File(imagePath);
                while (!imageFile.createNewFile()) { //若文件已存在，则换个名字
                    imagePath = dirPath + UUID.randomUUID();
                    imageFile = new File(imagePath);
                }
                InputStream inputStream = new ByteArrayInputStream(image);
                OutputStream outputStream = new FileOutputStream(imageFile);
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.flush();

                //1分钟后删除此图片
                final File finalImageFile = new File(imagePath);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!finalImageFile.delete()) {
                            System.err.println(finalImageFile.getName() + "文件删除失败");
                        }
                    }
                }, 60 * 1000);

                return new QrCodeResponse(true, "ok", imagePath);
            } catch (IOException e) {
                System.err.println("二维码图片保存时出现错误！");
                e.printStackTrace();
                return new QrCodeResponse(false, "二维码保存失败", "");
            }
        } else {
            System.err.println(wxQrCodeResponse);
            return new QrCodeResponse(false, "二维码获取失败", "");
        }
    }

    @Override
    public UserResponse getMyUser(String openid) throws NotExistException {
        return new UserResponse(new UserItem(userDataService.getUserByOpenid(openid), enterpriseDataService));
    }

    @Override
    public InfoResponse updateMyProfile(String openid, String username, String face, String phone, String email, String company, String department, String position, String intro, String city, List<String> label,List<String> label2) throws NotExistException {
        User user = userDataService.getUserByOpenid(openid);
        userDataService.updateUserByOpenid(openid, username, face, user.getMedals(), phone, email, company, department, position, intro, city, user.getCredit(), label,label2, user.getCardLimit(), user.getLevelName(), user.isValid());
        return new InfoResponse();
    }

    @Override
    public PersonResponse getPerson(String openid) throws NotExistException {
        return new PersonResponse(new PersonItem(userDataService.getUserByOpenid(openid)));
    }

    @Override
    public PersonListResponse getPersonList(String workClass) throws NotExistException {
        List<User> userList = userDataService.getAllUsers();
        List<PersonItem> personItemList = new ArrayList<>();
        for (User user : userList) {
            personItemList.add(new PersonItem(user));
        }
        return new PersonListResponse(personItemList);
    }

    @Override
    public PersonListResponse getPersonListByCondition(String condition) {
        List<User> userList = userDataService.getAllUsers();
        List<PersonItem> personItemList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUsername().contains(condition) || user.getCompany().contains(condition)) {
                personItemList.add(new PersonItem(user));
            }
        }
        return new PersonListResponse(personItemList);
    }

    @Override
    public BoolResponse sendMyCard(String senderOpenid, String receiverOpenid, String page, String formId, String data, String emphasisKeyword) {
        boolean flag = userDataService.addSendCard(new SendCard(senderOpenid, receiverOpenid, false));
        if (flag) { //若原先SendCard不存在，则发送模板消息
//			RestTemplate client = new RestTemplate();
//
//			//获取accessToken
//			String accessToken = null;
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//			HttpEntity<String> entity = new HttpEntity<>("", headers);
//			ResponseEntity<String> response = client.exchange(
//					"https://api.weixin.qq.com/cgi-bin/token?" + "&grant_type=client_credential&appid="+ appId + "&secret=" + appSecret, HttpMethod.GET, entity, String.class);
//			if (response.getStatusCode() == HttpStatus.OK) {
//				accessToken = (String)JSONObject.fromObject(response.getBody()).get("access_token");
//			} else {
//				System.err.println(response);
//				return new BoolResponse(false, "access_token获取失败("+response+")");
//			}
//
//			//使用accessToken发送模板消息
//			String wxQrCodeUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+accessToken;
//			Map<String,Object> wxMsgParams = new HashMap<>();
//			wxMsgParams.put("touser", receiverOpenid);
//			wxMsgParams.put("template_id", "NJoOn_GhBn_u_CvYSzfx1lxOO06iSrVPdFAdGqPWc4c");
//			wxMsgParams.put("page", page);
//			wxMsgParams.put("form_id", formId);
//			wxMsgParams.put("data", data);
//			wxMsgParams.put("emphasis_keyword", emphasisKeyword);
//			MultiValueMap<String, String> wxQrCodeHeaders = new LinkedMultiValueMap<>();
//			HttpEntity<Object> wxMsgRequest = new HttpEntity<>(wxMsgParams, wxQrCodeHeaders);
//			ResponseEntity<String> wxMsgResponse = client.exchange(wxQrCodeUrl, HttpMethod.POST, wxMsgRequest, String.class);
//
//			//检查请求结果
//			if (wxMsgResponse.getStatusCode()==HttpStatus.OK) {
//				JSONObject result = JSONObject.fromObject(wxMsgResponse.getBody());
//				if (String.valueOf(result.get("errcode")).equals("0")) {
//					return new BoolResponse(true, wxMsgResponse.getBody());
//				} else {
//					return new BoolResponse(false, wxMsgResponse.getBody());
//				}
//			} else {
//				return new BoolResponse(false, "后台请求失败");
//			}
            return new BoolResponse(true, "发送成功");
        } else {
            return new BoolResponse(false, "已经发送过名片，无需重复发送");
        }
    }

    @Override
    public CardListResponse getMyCardList(String openid, String kind) throws NotExistException {
        List<String> personOpenidList = new ArrayList<>();
        if (kind.equals("new") || kind.equals("current")) {
            List<SendCard> sendCards = userDataService.getSendsByOpenid(openid); //用户收到的
            if (kind.equals("new")) { //用户新收到，尚未查看的
                for (SendCard sendCard : sendCards) {
                    if (!sendCard.isChecked()) {
                        personOpenidList.add(sendCard.getSenderOpenid());
                    }
                }
            } else if (kind.equals("current")) { //用户总共收到的
                for (SendCard sendCard : sendCards) {
                    personOpenidList.add(sendCard.getSenderOpenid());
                }
            }
        } else if (kind.equals("whoHasMyCard")) {
            List<SendCard> sendCards = userDataService.getReceivesByOpenid(openid); //用户发给别人的
            for (SendCard sendCard : sendCards) {
                personOpenidList.add(sendCard.getReceiverOpenid());
            }
        }

        List<CardItem> cardItemList = new ArrayList<>();
        for (String personId : personOpenidList) {
            cardItemList.add(new CardItem(userDataService.getUserByOpenid(personId)));
        }
        return new CardListResponse(cardItemList);
    }

    @Override
    public InfoResponse checkMyReceivedCard(String senderOpenid, String receiverOpenid) throws NotExistException {
        userDataService.checkSendCard(new SendCardKey(senderOpenid, receiverOpenid));
        return new InfoResponse();
    }

    @Override
    public BoolResponse isOtherCardAccessible(String userOpenid, String otherOpenid) {
        return new BoolResponse(userOpenid.equals(otherOpenid) || sendCardDao.existsById(new SendCardKey(otherOpenid, userOpenid)), "ok");
    }

    @Override
    public CardResponse getOtherCard(String userOpenid, String otherOpenid) throws NotExistException {
        User other = userDataService.getUserByOpenid(otherOpenid);
        if (userOpenid.equals(otherOpenid) || sendCardDao.existsById(new SendCardKey(otherOpenid, userOpenid))) {
            //查看自己的和名片夹里的联系方式不消耗次数
            return new CardResponse(new CardItem(other));
        }
        User user = userDataService.getUserByOpenid(userOpenid);
        if (user.getCardLimit() > 0) {
            user.setCardLimit(user.getCardLimit() - 1);
            userDataService.saveUser(user);
            return new CardResponse(new CardItem(other));
        } else if (user.getCredit() >= levelDataService.getLevelByName(user.getLevelName()).getCheckCardPrice()) {
            //次数已用尽，若余额足够，使用余额购买
            user.setCredit(user.getCredit() - levelDataService.getLevelByName(user.getLevelName()).getCheckCardPrice());
            userDataService.saveUser(user);
            return new CardResponse((new CardItem(other)));
        } else {
            return new CardResponse(); //剩余次数用尽且余额不足，返回空对象
        }
    }

    @Override
    public String getMyReceivedCardNum(String openid) {
        List<SendCard> sendCards = userDataService.getSendsByOpenid(openid);
        return String.valueOf(sendCards.size());
    }

    @Override
    public String getMyMutualCardNum(String openid) {
        List<SendCard> receive = userDataService.getSendsByOpenid(openid);//收到的名片
        List<SendCard> send = userDataService.getReceivesByOpenid(openid);//发送过的名片
        int count = 0;
        for (int i = 0; i < receive.size(); i++) {
            for (int j = 0; j < send.size(); j++) {
                if (receive.get(i).equals(send.get(j))) {
                    count++;
                }
            }
        }
        return String.valueOf(count);
    }

    /**
     * 定时任务：每天0点自动重置所有用户查看别人名片的次数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void resetUserCardLimit() throws NotExistException {
        List<User> users = userDataService.getAllUsers();
        for (User user : users) {
            user.setCardLimit(levelDataService.getLevelByName(user.getLevelName()).getCardLimit());
            userDataService.saveUser(user);
        }
    }

}
