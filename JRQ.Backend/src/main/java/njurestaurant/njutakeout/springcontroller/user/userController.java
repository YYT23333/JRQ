package njurestaurant.njutakeout.springcontroller.user;

import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import njurestaurant.njutakeout.blservice.admin.AdminBlService;
import njurestaurant.njutakeout.blservice.user.UserBlService;
import njurestaurant.njutakeout.exception.CardLimitUseUpException;
import njurestaurant.njutakeout.exception.NotExistException;
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

@RestController
public class userController {
    private final UserBlService userBlService;

    @Autowired
    public userController(UserBlService userBlService) {
        this.userBlService = userBlService;
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
            @ApiImplicitParam(name = "face", value = "用户头像", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "credit", value = "余额", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cardLimit", value = "用户名片查看限制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "levelName", value = "用户等级", required = true, dataType = "String"),
            @ApiImplicitParam(name = "valid", value = "是否启用", required = true, dataType = "boolean")
    })
    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse updateUser(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="face")String face,@RequestParam(name="phone")String phone,@RequestParam(name="email")String email,@RequestParam(name="company")String company,@RequestParam(name="department")String department,@RequestParam(name="position")String position,@RequestParam(name="intro")String intro,@RequestParam(name="city")String city,@RequestParam(name="credit")String credit,@RequestParam(name="label")String label,@RequestParam(name="cardLimit")String cardLimit,@RequestParam(name="levelName")String levelName,@RequestParam(name="valid")boolean valid) throws NotExistException {
        InfoResponse r=userBlService.updateUser(openid,username,face,null,phone,email,company,department,position,intro,city,Integer.parseInt(credit),label,Integer.parseInt(cardLimit),levelName,valid);
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
    public InfoResponse deleteClassification(@RequestParam(name="userLabel")String userLabel) throws NotExistException {
        return userBlService.deleteClassification(userLabel);
    }

    @ApiOperation(value = "添加会员等级信息", notes = "添加会员等级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cardLimit", value = "名片限制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addLevel", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse addLevel(@RequestParam(name="name")String name,@RequestParam(name="cardLimit")String cardLimit,@RequestParam(name="price")String price) throws NotExistException {
        return userBlService.addLevel(name,Integer.parseInt(cardLimit), Integer.parseInt(price));
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
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateLevel", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public InfoResponse updateLevel(@RequestParam(name="name")String name,@RequestParam(name="cardLimit")String cardLimit,@RequestParam(name="price")String price) throws NotExistException {
        return userBlService.updateLevel(name,Integer.parseInt(cardLimit), Integer.parseInt(price));
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






    @ApiOperation(value = "用户登录小程序", notes = "用户登录小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "username", value = "名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginMyUser", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> loginMyUser(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username) throws NotExistException {
        return new ResponseEntity<>(userBlService.loginMyUser(openid,username), HttpStatus.OK);
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
            @ApiImplicitParam(name = "face", value = "用户头像", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "company", value = "用户公司", required = true, dataType = "String"),
            @ApiImplicitParam(name = "department", value = "用户部门", required = true, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "用户职位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "intro", value = "用户简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "用户城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "label", value = "用户标签", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/updateMyProfile", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateMyProfile(@RequestParam(name="openid")String openid,@RequestParam(name="username")String username,@RequestParam(name="face")String face,@RequestParam(name="phone")String phone,@RequestParam(name="email")String email,@RequestParam(name="company")String company,@RequestParam(name="department")String department,@RequestParam(name="position")String position,@RequestParam(name="intro")String intro,@RequestParam(name="city")String city,@RequestParam(name="label")String label) throws NotExistException {
        return new ResponseEntity<>(userBlService.updateMyProfile(openid,username,face,phone,email,company,department,position,intro,city,label), HttpStatus.OK);
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
            @ApiImplicitParam(name = "receiverOpenid", value = "接收者微信openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendMyCard", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> sendMyCard(@RequestParam(name="senderOpenid")String senderOpenid,@RequestParam(name="receiverOpenid")String receiverOpenid) {
        return new ResponseEntity<>(userBlService.sendMyCard(senderOpenid,receiverOpenid), HttpStatus.OK);
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
    @RequestMapping(value = "/getOtherCard", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getOtherCard(@RequestParam(name="userOpenid")String userOpenid,@RequestParam(name="otherOpenid")String otherOpenid) throws NotExistException, CardLimitUseUpException {
        return new ResponseEntity<>(userBlService.getOtherCard(userOpenid,otherOpenid), HttpStatus.OK);
    }



















}
