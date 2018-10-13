package njurestaurant.njutakeout.springcontroller.article.course;

import io.swagger.annotations.*;
import njurestaurant.njutakeout.blservice.article.course.CourseBlService;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.Response;
import njurestaurant.njutakeout.response.WrongResponse;
import njurestaurant.njutakeout.response.event.EventLoadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
public class CourseController {
    private final CourseBlService courseBlService;

    @Autowired
    public CourseController(CourseBlService courseBlService) {
        this.courseBlService = courseBlService;
    }
    private static String courseImage="";
    private static String courseVideo="";
    @ApiOperation(value = "获取课程图片", notes = "获取课程图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "课程图片", required = true, dataType = "MultipartFile")
    })
    @RequestMapping(value = "/courseImage", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public void courseImage(@RequestParam("image")MultipartFile image){
        Map<String,Object> map= new HashMap<String,Object>();
        if(image.isEmpty()){
            map.put( "result", "error");
            map.put( "msg", "上传文件不能为空" );
        } else {

            // 获取文件名
            String fileName = image.getOriginalFilename();
            // 获取文件后缀

            // 用uuid作为文件名，防止生成的临时文件重复
            // MultipartFile to File
            //你的业务逻辑
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = null;    //读入原文件
            try {
                inStream = image.getInputStream();
                FileOutputStream fs = new FileOutputStream(fileName);
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
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            File file = new File(fileName);
            String[] temp=fileName.split("\\.");
            String thePath="record/course/image/"+uuid+"."+temp[1];
            String path="record/course/image/"+uuid+"."+temp[1];
            File tempfile=new File(path);
            if (tempfile.exists() && tempfile.isFile()) {
                tempfile.delete();
            }
            bytesum = 0;
            byteread = 0;
            try {
                inStream =new FileInputStream(fileName);
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
            courseImage=thePath;

            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    @ApiOperation(value = "获取课程视频", notes = "获取课程视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "video", value = "课程视频", required = true, dataType = "MultipartFile")
    })
    @RequestMapping(value = "/courseVideo", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public void courseVideo(@RequestParam("video")MultipartFile video){
        Map<String,Object> map= new HashMap<String,Object>();
        if(video.isEmpty()){
            map.put( "result", "error");
            map.put( "msg", "上传文件不能为空" );
        } else {

            // 获取文件名
            String fileName = video.getOriginalFilename();
            // 获取文件后缀

            // 用uuid作为文件名，防止生成的临时文件重复
            // MultipartFile to File
            //你的业务逻辑
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = null;    //读入原文件
            try {
                inStream = video.getInputStream();
                FileOutputStream fs = new FileOutputStream(fileName);
                byte[] buffer = new byte[200000000];
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
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String[] temp=fileName.split("\\.");
            String thePath="record/course/video/"+uuid+"."+temp[1];
            String path="record/course/video/"+uuid+"."+temp[1];
            File tempfile=new File(path);
            if (tempfile.exists() && tempfile.isFile()) {
                tempfile.delete();
            }
            bytesum = 0;
            byteread = 0;
            try {
                inStream =new FileInputStream(fileName);
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
            courseVideo=thePath;
            File file=new File(fileName);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }


    @ApiOperation(value = "添加课程", notes = "添加课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "课程标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "writerName", value = "作者名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "发布日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "课程价格", required = true, dataType = "int")
    })
    @RequestMapping(value = "/addCourse", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> addCourse(@RequestParam(name="title")String title, @RequestParam(name="writerName")String writerName, @RequestParam(name="date")String date, @RequestParam(name="price")int price) {
        ResponseEntity<Response> r=new ResponseEntity<>(courseBlService.addCourse(title,courseImage,writerName,0,courseVideo,price), HttpStatus.OK);
        courseImage="";
        courseVideo="";
        return r;
    }

    @ApiOperation(value = "根据课程ID获取课程内容", notes = "根据课程ID获取课程内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getCourse", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getCourse(@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(courseBlService.getCourse(id), HttpStatus.OK);
    }

    @ApiOperation(value = "用户根据课程ID获取课程信息", notes = "用户根据课程ID获取课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "courseId", value = "课程ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyCourse", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyCourse(@RequestParam(name="openid")String openid,@RequestParam(name="courseId")String courseId) throws NotExistException {
        return new ResponseEntity<>(courseBlService.getMyCourse(openid,courseId), HttpStatus.OK);
    }

    @ApiOperation(value = "用户获取所有课程信息列表", notes = "用户获取所有课程信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyCourseList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyCourseList(@RequestParam(name="openid")String openid) throws NotExistException {
        return new ResponseEntity<>(courseBlService.getMyCourseList(openid), HttpStatus.OK);
    }

    @ApiOperation(value = "获取课程列表", notes = "获取课程列表")
    @RequestMapping(value = "/getCourseList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getCourseList(){
        return new ResponseEntity<>(courseBlService.getCourseList(), HttpStatus.OK);
    }

    @ApiOperation(value = "根据课程ID修改课程内容", notes = "根据课程ID修改课程内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "title", value = "课程标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "writerName", value = "作者名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "发布日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "likeNum", value = "点赞数", required = true, dataType = "long"),
            @ApiImplicitParam(name = "price", value = "课程价格", required = true, dataType = "int")
    })
    @RequestMapping(value = "/updateCourse", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateCourse(@RequestParam(name="id")String id, @RequestParam(name="title")String title, @RequestParam(name="writerName")String writerName, @RequestParam(name="date")String date, @RequestParam(name="likeNum")long likeNum, @RequestParam(name="price")int price) throws NotExistException {

        ResponseEntity<Response> r= new ResponseEntity<>(courseBlService.updateCourse(id,title,courseImage,writerName,likeNum,courseVideo,price), HttpStatus.OK);
        courseImage="";
        courseVideo="";
        return r;
    }


    @ApiOperation(value = "根据课程ID删除课程", notes = "根据课程ID删除课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteCourse", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> deleteCourse(@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(courseBlService.deleteCourse(id), HttpStatus.OK);
    }

    @ApiOperation(value = "获取某一篇课程文章时间戳前的10篇文章", notes = "获取某一篇课程文章时间戳前的10篇文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "课程ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "课程ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyCourseListBefore", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyCourseListBefore(@RequestParam(name="openid")String openid,@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(courseBlService.getMyCourseListBefore(openid,id), HttpStatus.OK);
    }




}
