package njurestaurant.njutakeout.springcontroller.user;

import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import njurestaurant.njutakeout.blservice.admin.AdminBlService;
import njurestaurant.njutakeout.blservice.user.UserBlService;
import njurestaurant.njutakeout.exception.CannotGetOpenIdAndSessionKeyException;
import njurestaurant.njutakeout.exception.CardLimitUseUpException;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.BoolResponse;
import njurestaurant.njutakeout.response.InfoResponse;
import njurestaurant.njutakeout.response.Response;
import njurestaurant.njutakeout.response.WrongResponse;
import njurestaurant.njutakeout.response.event.EventLoadResponse;
import njurestaurant.njutakeout.response.user.ClassificationListResponse;
import njurestaurant.njutakeout.response.user.ClassificationResponse;
import njurestaurant.njutakeout.response.user.UserListResponse;
import njurestaurant.njutakeout.response.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class userController {
    private final UserBlService userBlService;

    @Autowired
    public userController(UserBlService userBlService) {
        this.userBlService = userBlService;
    }

    private static String headPath="";
    @ApiOperation(value = "获取用户头像", notes = "获取用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "face", value = "用户头像", required = true, dataType = "MultipartFile")
    })
    @RequestMapping(value = "/uploadHead", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public void uploadHead(@RequestParam("face")MultipartFile face){
        Map<String,Object> map= new HashMap<String,Object>();
        if(face.isEmpty()){
            map.put( "result", "error");
            map.put( "msg", "上传文件不能为空" );
        } else {

            // 获取文件名
            String fileName = face.getOriginalFilename();
            // 获取文件后缀

            // 用uuid作为文件名，防止生成的临时文件重复
            // MultipartFile to File
            //你的业务逻辑
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = null;    //读入原文件
            try {
                inStream = face.getInputStream();
                FileOutputStream fs = new FileOutputStream(fileName);
                headPath=fileName;
                byte[] buffer = new byte[20000000];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;            //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }



    @ApiOperation(value = "增加用户", notes = "增加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "credit", value = "余额", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "List"),
            @ApiImplicitParam(name = "levelName", value = "用户等级", required = true, dataType = "String"),
            @ApiImplicitParam(name = "valid", value = "是否启用", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse addUser(@RequestParam(name="openid")String openid, @RequestParam(name="username")String username, @RequestParam(name="phone")String phone, @RequestParam(name="email")String email, @RequestParam(name="company")String company, @RequestParam(name="department")String department, @RequestParam(name="position")String position, @RequestParam(name="intro")String intro, @RequestParam(name="city")String city, @RequestParam(name="credit")String credit, @RequestParam(name="label")List<String> label,@RequestParam(name="label2")List<String> label2, @RequestParam(name="levelName")String levelName, @RequestParam(name="valid")String valid) throws NotExistException {
        boolean is=true;
        if(valid=="冻结"){
            is=false;
        }
        File file = new File(headPath);
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String[] temp=headPath.split("\\.");
        String thePath="record/user/head/"+uuid+"."+temp[1];
        String path="record/user/head/"+uuid+"."+temp[1];
        File tempfile=new File(path);
        if (tempfile.exists() && tempfile.isFile()) {
             tempfile.delete();
        }
        int bytesum = 0;
        int byteread = 0;

        try {
            InputStream inStream =new FileInputStream(headPath);
            FileOutputStream fs = new FileOutputStream(path);
            byte[] buffer = new byte[20000000];
            while ( (byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;            //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        InfoResponse r=userBlService.addUser(openid,username,thePath,null,phone,email,company,department,position,intro,city,Integer.parseInt(credit),label,label2,levelName,is);
        headPath="";
        return r;
    }

    @ApiOperation(value = "增加用户", notes = "增加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "face", value = "头像", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "credit", value = "余额", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "List"),
            @ApiImplicitParam(name = "levelName", value = "用户等级", required = true, dataType = "String"),
            @ApiImplicitParam(name = "valid", value = "是否启用", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addUserWithoutFace", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse addUserWithoutFace(@RequestParam(name="openid")String openid, @RequestParam(name="username")String username, @RequestParam(name="face")String face,@RequestParam(name="phone")String phone, @RequestParam(name="email")String email, @RequestParam(name="company")String company, @RequestParam(name="department")String department, @RequestParam(name="position")String position, @RequestParam(name="intro")String intro, @RequestParam(name="city")String city, @RequestParam(name="credit")String credit, @RequestParam(name="label")List<String> label,@RequestParam(name="label2")List<String> label2, @RequestParam(name="levelName")String levelName, @RequestParam(name="valid")String valid) throws NotExistException {
        boolean is=true;
        if(valid=="冻结"){
            is=false;
        }
        InfoResponse r=userBlService.updateUser(openid,username,face,null,phone,email,company,department,position,intro,city,Integer.parseInt(credit),label,label2,levelName,is);
        return r;
    }

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public UserListResponse getUserList(){
        UserListResponse r=userBlService.getUserList();
        return r;
    }

    @ApiOperation(value = "获取用户", notes = "获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public UserResponse getUser(@RequestParam(name="openid")String openid) throws NotExistException {
        UserResponse r=userBlService.getUser(openid);
        return r;
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "credit", value = "余额", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "List"),
            @ApiImplicitParam(name = "levelName", value = "用户等级", required = true, dataType = "String"),
            @ApiImplicitParam(name = "valid", value = "是否启用", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse updateUser(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="face")String face,@RequestParam(name="phone")String phone,@RequestParam(name="email")String email,@RequestParam(name="company")String company,@RequestParam(name="department")String department,@RequestParam(name="position")String position,@RequestParam(name="intro")String intro,@RequestParam(name="city")String city,@RequestParam(name="credit")String credit,@RequestParam(name="label")List<String> label,@RequestParam(name="label2")List<String> label2,@RequestParam(name="levelName")String levelName,@RequestParam(name="valid")String valid) throws NotExistException {
        boolean is=true;
        if(valid=="冻结"){
            is=false;
        }
        File file = new File(headPath);
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String[] temp=headPath.split("\\.");
        String thePath="record/user/head/"+uuid+"."+temp[1];
        String path="record/user/head/"+uuid+"."+temp[1];
        File tempfile=new File(path);
        if (tempfile.exists() && tempfile.isFile()) {
            tempfile.delete();
        }
        int bytesum = 0;
        int byteread = 0;

        try {
            InputStream inStream =new FileInputStream(headPath);
            FileOutputStream fs = new FileOutputStream(path);
            byte[] buffer = new byte[20000000];
            while ( (byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;            //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        InfoResponse r=userBlService.updateUser(openid,username,thePath,null,phone,email,company,department,position,intro,city,Integer.parseInt(credit),label,label2,levelName,is);
        headPath="";
        return r;
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse deleteUser(@RequestParam(name="openid")String openid) throws NotExistException {
        InfoResponse r=userBlService.deleteUser(openid);
        return r;
    }

    @ApiOperation(value = "添加标签", notes = "添加标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLabel", value = "用户标签", required = true, dataType = "String"),
            @ApiImplicitParam(name = "workClass", value = "业务分类", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addClassification", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse addClassification(@RequestParam(name="userLabel")String userLabel,@RequestParam(name="workClass")String workClass) {
        InfoResponse r=userBlService.addClassification(userLabel,workClass);
        return r;
    }

    @ApiOperation(value = "获取分类", notes = "根据标签获取分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLabel", value = "标签名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getClassification", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ClassificationResponse getClassification(@RequestParam(name="userLabel")String userLabel) throws NotExistException {
        ClassificationResponse r=userBlService.getClassification(userLabel);
        return r;
    }

    @ApiOperation(value = "获取列表", notes = "获取分类列表")
    @RequestMapping(value = "/getClassificationList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getClassificationList() {
        return new ResponseEntity<>(userBlService.getClassificationList(), HttpStatus.OK);
    }

    @ApiOperation(value = "更新标签分类", notes = "更新标签分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLabel", value = "标签名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "workClass", value = "业务分类", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateClassification", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse updateClassification(@RequestParam(name="userLabel")String userLabel,@RequestParam(name="workClass")String workClass) throws NotExistException {
        return userBlService.updateClassification(userLabel,workClass);
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLabel", value = "标签名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteClassification", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public BoolResponse deleteClassification(@RequestParam(name="userLabel")String userLabel) throws NotExistException {
        return userBlService.deleteClassification(userLabel);
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @RequestMapping(value = "/getClassificationDescriptionList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getClassificationDescriptionList()  {
        return new ResponseEntity<>(userBlService.getClassificationDescriptionList(),HttpStatus.OK);
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workClass", value = "标签名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "description", value = "标签名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateClassificationDescription", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateClassificationDescription(@RequestParam(name="workClass")String workClass,@RequestParam(name="description")String description) throws NotExistException {
        return new ResponseEntity<>(userBlService.updateClassificationDescription(workClass,description),HttpStatus.OK);
    }

    @ApiOperation(value = "添加会员等级信息", notes = "添加会员等级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cardLimit", value = "名片限制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String"),
            @ApiImplicitParam(name = "courseDiscountedRatio", value = "该会员级别购买课程时的折扣率，价格是原价的courseDiscountedRatio倍", required = true, dataType = "String"),
            @ApiImplicitParam(name = "checkCardPrice", value = "查看名片联系方式次数用完以后再查看联系方式需要花多少钱", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addLevel", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse addLevel(@RequestParam(name="name")String name,@RequestParam(name="cardLimit")String cardLimit,@RequestParam(name="price")String price,@RequestParam(name="courseDiscountedRatio")String courseDiscountedRatio,@RequestParam(name="checkCardPrice")String checkCardPrice) throws NotExistException {
        return userBlService.addLevel(name,Integer.parseInt(cardLimit), Integer.parseInt(price),Double.parseDouble(courseDiscountedRatio),Integer.parseInt(checkCardPrice));
    }

    @ApiOperation(value = "获取所有会员等级信息", notes = "获取所有会员等级信息")
    @RequestMapping(value = "/getLevelList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getLevelList() {
        return new ResponseEntity<>(userBlService.getLevelList(), HttpStatus.OK);
    }

    @ApiOperation(value = "更新会员等级信息", notes = "更新会员等级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cardLimit", value = "名片限制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String"),
            @ApiImplicitParam(name = "courseDiscountedRatio", value = "该会员级别购买课程时的折扣率，价格是原价的courseDiscountedRatio倍", required = true, dataType = "String"),
            @ApiImplicitParam(name = "checkCardPrice", value = "查看名片联系方式次数用完以后再查看联系方式需要花多少钱", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateLevel", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse updateLevel(@RequestParam(name="name")String name,@RequestParam(name="cardLimit")String cardLimit,@RequestParam(name="price")String price,@RequestParam(name="courseDiscountedRatio")String courseDiscountedRatio,@RequestParam(name="checkCardPrice")String checkCardPrice) throws NotExistException {
        return userBlService.updateLevel(name,Integer.parseInt(cardLimit), Integer.parseInt(price),Double.parseDouble(courseDiscountedRatio),Integer.parseInt(checkCardPrice));
    }

    @ApiOperation(value = "更新会员等级信息", notes = "更新会员等级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteLevel", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse deleteLevel(@RequestParam(name="name")String name) throws NotExistException {
        return userBlService.deleteLevel(name);
    }


    @ApiOperation(value = "小程序前端获取openid和session", notes = "小程序前端获取openid和session")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsCode", value = "微信小程序的jsCode", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getOpenIdAndSessionKey", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getOpenIdAndSessionKey(@RequestParam(name="jsCode")String jsCode) throws CannotGetOpenIdAndSessionKeyException {
        return new ResponseEntity<>(userBlService.getOpenIdAndSessionKey(jsCode), HttpStatus.OK);
    }


    @ApiOperation(value = "用户登录小程序", notes = "用户登录小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "faceWxUrl", value = "用户微信头像的URL", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginMyUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> loginMyUser(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="faceWxUrl")String faceWxUrl) throws NotExistException {
        return new ResponseEntity<>(userBlService.loginMyUser(openid,username,faceWxUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "小程序前端获取微信小程序的二维码", notes = "小程序前端获取微信小程序的二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scene", value = "参数，对应微信API的scene", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "跳转的页面，对应微信API的page", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "维码的宽度，对应微信API的width", required = true, dataType = "int"),
            @ApiImplicitParam(name = "autoColor", value = "是否自动配置线条颜色，对应微信API的auto_color", required = true, dataType = "boolean"),
            @ApiImplicitParam(name = "lineColorR", value = "当autoColor为false时有效，对应微信API的line_color中的r", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lineColorG", value = "当autoColor为false时有效，对应微信API的line_color中的g", required = true, dataType = "String"),
            @ApiImplicitParam(name = "lineColorB", value = "当autoColor为false时有效，对应微信API的line_color中的b", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isHyaline", value = "是否需要透明底色，对应微信API的is_hyaline", required = true, dataType = "boolean")
    })
    @RequestMapping(value = "/getWxQrCode", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getWxQrCode(@RequestParam(name="scene")String scene,@RequestParam(name="page")String page,@RequestParam(name="width")int width,@RequestParam(name="autoColor")boolean autoColor,@RequestParam(name="lineColorR")String lineColorR,@RequestParam(name="lineColorG")String lineColorG,@RequestParam(name="lineColorB")String lineColorB,@RequestParam(name="isHyaline")boolean isHyaline) throws NotExistException {
        return new ResponseEntity<>(userBlService.getWxQrCode(scene,page,width,autoColor,lineColorR,lineColorG,lineColorB,isHyaline), HttpStatus.OK);
    }

    @ApiOperation(value = "用户获取自己的个人信息", notes = "用户获取自己的个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyUser(@RequestParam(name="openid")String openid) throws NotExistException {
        return new ResponseEntity<>(userBlService.getMyUser(openid), HttpStatus.OK);
    }

    @ApiOperation(value = "用户修改自己的个人信息", notes = "用户修改自己的个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "List"),
    })
    @RequestMapping(value = "/updateMyProfile", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateMyProfile(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="phone")String phone,@RequestParam(name="email")String email,@RequestParam(name="company")String company,@RequestParam(name="department")String department,@RequestParam(name="position")String position,@RequestParam(name="intro")String intro,@RequestParam(name="city")String city,@RequestParam(name="label")List<String> label,@RequestParam(name="label2")List<String> label2) throws NotExistException {
        File file = new File(headPath);
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String[] temp=headPath.split("\\.");
        String thePath="";
        String path="";
        if(temp.length>2) {
            thePath = "record/user/head/" + uuid + "." + temp[3];
            path = "record/user/head/" + uuid + "." + temp[3];
        }
        else{
            thePath = "record/user/head/" + uuid + "." + temp[1];
            path = "record/user/head/" + uuid + "." + temp[1];
        }
        File tempfile=new File(path);
        if (tempfile.exists() && tempfile.isFile()) {
            tempfile.delete();
        }
        int bytesum = 0;
        int byteread = 0;
        try {
            InputStream inStream =new FileInputStream(headPath);
            FileOutputStream fs = new FileOutputStream(path);
            byte[] buffer = new byte[20000000];
            while ( (byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;            //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        headPath="";
        return new ResponseEntity<>(userBlService.updateMyProfile(openid,username,thePath,phone,email,company,department,position,intro,city,label,label2), HttpStatus.OK);
    }

    @ApiOperation(value = "用户修改自己的个人信息", notes = "用户修改自己的个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "face", value = "用户头像", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "List"),
    })
    @RequestMapping(value = "/updateMyProfileWithoutFile", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateMyProfileWithoutFile(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="face")String face,@RequestParam(name="phone")String phone,@RequestParam(name="email")String email,@RequestParam(name="company")String company,@RequestParam(name="department")String department,@RequestParam(name="position")String position,@RequestParam(name="intro")String intro,@RequestParam(name="city")String city,@RequestParam(name="label")List<String> label,@RequestParam(name="label2")List<String> label2) throws NotExistException {
        return new ResponseEntity<>(userBlService.updateMyProfile(openid,username,face,phone,email,company,department,position,intro,city,label,label2), HttpStatus.OK);
    }

    @ApiOperation(value = "根据用户微信openid获取其业务名片", notes = "根据用户微信openid获取其业务名片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户微信openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getPerson", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getPerson(@RequestParam(name="openid")String openid) throws NotExistException {
        return new ResponseEntity<>(userBlService.getPerson(openid), HttpStatus.OK);
    }

    @ApiOperation(value = "获取业务名片列表", notes = "获取业务名片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workClass", value = "类别", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getPersonList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getPersonList(@RequestParam(name="workClass")String workClass) throws NotExistException {
        return new ResponseEntity<>(userBlService.getPersonList(workClass), HttpStatus.OK);
    }

    @ApiOperation(value = "获取符合特定条件的业务名片列表", notes = "获取符合特定条件的业务名片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "condition", value = "类别", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getPersonListByCondition", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getPersonListByCondition(@RequestParam(name="condition")String condition) {
        return new ResponseEntity<>(userBlService.getPersonListByCondition(condition), HttpStatus.OK);
    }

    @ApiOperation(value = "用户向别人发送名片", notes = "用户向别人发送名片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "senderOpenid", value = "发送者微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "receiverOpenid", value = "接收者微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "微信模板消息接口中要跳转的页面", required = true, dataType = "String"),
            @ApiImplicitParam(name = "formId", value = "微信模板消息接口参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "data", value = "微信模板消息接口内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "emphasisKeyword", value = "微信模板消息接口放大的关键词", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendMyCard", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> sendMyCard(@RequestParam(name="senderOpenid")String senderOpenid,@RequestParam(name="receiverOpenid")String receiverOpenid,@RequestParam(name="page")String page,@RequestParam(name="formId")String formId,@RequestParam(name="data")String data,@RequestParam(name="emphasisKeyword")String emphasisKeyword) {
        return new ResponseEntity<>(userBlService.sendMyCard(senderOpenid,receiverOpenid,page,formId,data,emphasisKeyword), HttpStatus.OK);
    }

    @ApiOperation(value = "用户获取自己的名片列表", notes = "用户获取自己的名片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "kind", value = "名片类型", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyCardList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyCardList(@RequestParam(name="openid")String openid,@RequestParam(name="kind")String kind) throws NotExistException {
        return new ResponseEntity<>(userBlService.getMyCardList(openid,kind), HttpStatus.OK);
    }

    @ApiOperation(value = "将用户收到的名片设置为已读", notes = "将用户收到的名片设置为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "senderOpenid", value = "发送者微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "receiverOpenid", value = "接收者微信openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/checkMyReceivedCard", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> checkMyReceivedCard(@RequestParam(name="senderOpenid")String senderOpenid,@RequestParam(name="receiverOpenid")String receiverOpenid) throws NotExistException {
        return new ResponseEntity<>(userBlService.checkMyReceivedCard(senderOpenid,receiverOpenid), HttpStatus.OK);
    }

    @ApiOperation(value = "用户查看别人的名片，每天次数有限制", notes = "用户查看别人的名片，每天次数有限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userOpenid", value = "用户自己的微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "otherOpenid", value = "要查看的用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/isOtherCardAccessible", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> isOtherCardAccessible(@RequestParam(name="userOpenid")String userOpenid,@RequestParam(name="otherOpenid")String otherOpenid) throws NotExistException, CardLimitUseUpException {
        return new ResponseEntity<>(userBlService.isOtherCardAccessible(userOpenid,otherOpenid), HttpStatus.OK);
    }

    @ApiOperation(value = "用户查看别人的名片，每天次数有限制", notes = "用户查看别人的名片，每天次数有限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userOpenid", value = "用户自己的微信openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "otherOpenid", value = "要查看的用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getOtherCard", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getOtherCard(@RequestParam(name="userOpenid")String userOpenid,@RequestParam(name="otherOpenid")String otherOpenid) throws NotExistException, CardLimitUseUpException {
        return new ResponseEntity<>(userBlService.getOtherCard(userOpenid,otherOpenid), HttpStatus.OK);
    }

    @ApiOperation(value = "获取用户收到的名片数量", notes = "获取用户收到的名片数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的微信openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyReceivedCardNum", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public String getMyReceivedCardNum(@RequestParam(name="openid")String openid) throws NotExistException {
        return userBlService.getMyReceivedCardNum(openid);
    }

    @ApiOperation(value = "获取用户互持名片的数量", notes = "获取用户互持名片的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的微信openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyMutualCardNum", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public String getMyMutualCardNum(@RequestParam(name="openid")String openid) throws NotExistException {
        return userBlService.getMyMutualCardNum(openid);
    }
}
