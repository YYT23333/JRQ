package njurestaurant.njutakeout.springcontroller.article.document;

import io.swagger.annotations.*;
import njurestaurant.njutakeout.blservice.article.RecordBlService;
import njurestaurant.njutakeout.blservice.article.document.DocumentBlService;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.Response;
import njurestaurant.njutakeout.response.WrongResponse;
import njurestaurant.njutakeout.response.event.EventLoadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static njurestaurant.njutakeout.util.FormatDateTime.toLongDateString;

@RestController
public class DocumentController {
    private final DocumentBlService documentBlService;

    private final RecordBlService recordBlService;

    @Autowired
    public DocumentController(DocumentBlService documentBlService, RecordBlService recordBlService) {
        this.documentBlService = documentBlService;
        this.recordBlService = recordBlService;
    }

    @ApiOperation(value = "获取文档图片", notes = "获取文档图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "文档图片", required = true, dataType = "MultipartFile")
    })
    @RequestMapping(value = "/documentImage", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public String documentImage(@RequestParam("image")MultipartFile image){
        Map<String,Object> map= new HashMap<String,Object>();
        if(image.isEmpty()){
            map.put( "result", "error");
            map.put( "msg", "上传文件不能为空" );
            return null;
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
            String thePath="record/document/image/"+uuid+"."+temp[1];
            String path="record/document/image/"+uuid+"."+temp[1];
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


            if (file.exists() && file.isFile()) {
                file.delete();
            }
            return thePath;
        }
    }


    @ApiOperation(value = "获取附件", notes = "获取附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attachment", value = "附件路径", required = true, dataType = "MultipartFile")
    })
    @RequestMapping(value = "/uploadDocument", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public String uploadDocument(@RequestParam("attachment")MultipartFile attachment){
        Map<String,Object> map= new HashMap<String,Object>();
        if(attachment.isEmpty()){
            map.put( "result", "error");
            map.put( "msg", "上传文件不能为空" );
            return null;
        } else {

            // 获取文件名
            String fileName = attachment.getOriginalFilename();
            // 获取文件后缀

            // 用uuid作为文件名，防止生成的临时文件重复
            // MultipartFile to File
            //你的业务逻辑
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = null;    //读入原文件
            try {
                inStream = attachment.getInputStream();
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
            File file = new File(fileName);
            String[] temp=fileName.split("\\.");
            String thePath="record/document/"+uuid+"."+temp[1];
            String path="record/document/"+uuid+"."+temp[1];
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
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            return thePath;
        }

    }


    @ApiOperation(value = "添加文档", notes = "添加文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "文档标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "文档内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "image", value = "图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "writerName", value = "作者名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addDocument", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> addDocument(@RequestParam(name="title")String title, @RequestParam(name="content")String content, @RequestParam(name="image")String image, @RequestParam(name="attachment")String attachment,@RequestParam(name="writerName")String writerName, @RequestParam(name="price")String price) {
        ResponseEntity<Response> r=new ResponseEntity<>(documentBlService.addDocument(title,content,image,attachment,writerName,Integer.parseInt(price),0,false), HttpStatus.OK);
        return r;
    }

    @ApiOperation(value = "添加合同", notes = "添加合同")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "文档标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "文档内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "image", value = "图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "writerName", value = "作者名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addContract", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> addContract(@RequestParam(name="title")String title, @RequestParam(name="content")String content, @RequestParam(name="image")String image, @RequestParam(name="attachment")String attachment,@RequestParam(name="writerName")String writerName, @RequestParam(name="price")String price) {
        ResponseEntity<Response> r=new ResponseEntity<>(documentBlService.addDocument(title,content,image,attachment,writerName,Integer.parseInt(price),0,true), HttpStatus.OK);
        return r;
    }

    @ApiOperation(value = "根据文档ID获取文档", notes = "根据文档ID获取文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getDocument", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getDocument(@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(documentBlService.getDocument(id), HttpStatus.OK);
    }

    @ApiOperation(value = "获取文档列表", notes = "获取文档列表")
    @RequestMapping(value = "/getDocumentList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getDocumentList(){
        return new ResponseEntity<>(documentBlService.getDocumentList(), HttpStatus.OK);
    }

    @ApiOperation(value = "获取合同列表", notes = "获取合同列表")
    @RequestMapping(value = "/getContractList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getContractList(){
        return new ResponseEntity<>(documentBlService.getContractList(), HttpStatus.OK);
    }

    @ApiOperation(value = "根据文档ID修改文档", notes = "根据文档ID修改文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "title", value = "文档标题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "文档内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "image", value = "图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "writerName", value = "作者名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "String"),
            @ApiImplicitParam(name = "likeNum", value = "点赞数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateDocument", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> updateDocument(@RequestParam(name="id")String id,
                                                   @RequestParam(name="title")String title,
                                                   @RequestParam(name="content")String content,
                                                   @RequestParam(name="image")String image,
                                                   @RequestParam(name="attachment")String attachment,
                                                   @RequestParam(name="writerName")String writerName,
                                                   @RequestParam(name="price")String price,
                                                   @RequestParam(name="likeNum")String likeNum,
                                                   HttpServletRequest request) throws NotExistException {

        recordBlService.addRecord("time:"+toLongDateString(new Date())+" ip:"+request.getRemoteAddr()+" update document"+
        " id:"+id+" title:"+title+" content:"+content+" attachment:"+attachment+" writerName:"+writerName+" price:"+price+" likeNum:"+likeNum);
        ResponseEntity<Response> r= new ResponseEntity<>(documentBlService.updateDocument(id,title,content,image,attachment,writerName,Integer.parseInt(price),Long.parseLong(likeNum)), HttpStatus.OK);
        return r;
    }

    @ApiOperation(value = "根据文档ID删除文档", notes = "根据文档ID删除文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteDocument", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> deleteDocument(@RequestParam(name="id")String id, HttpServletRequest request) throws NotExistException {
        recordBlService.addRecord("time:"+toLongDateString(new Date())+" ip:"+request.getRemoteAddr()+" delete document id:"+id);
        return new ResponseEntity<>(documentBlService.deleteDocument(id), HttpStatus.OK);
    }

    @ApiOperation(value = "根据openid和文档ID获得文档", notes = "根据openid和文档ID获得文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "documentId", value = "文档ID", required = true, dataType = "String")

    })
    @RequestMapping(value = "/getMyDocument", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyDocument(@RequestParam(name="openid")String openid,@RequestParam(name="documentId")String documentId) throws NotExistException {
        return new ResponseEntity<>(documentBlService.getMyDocument(openid,documentId), HttpStatus.OK);
    }

    @ApiOperation(value = "获得我的文档列表", notes = "获得我的文档列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyDocumentList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyDocumentList(@RequestParam(name="openid")String openid) {
        return new ResponseEntity<>(documentBlService.getMyDocumentList(openid,false), HttpStatus.OK);
    }

    @ApiOperation(value = "获得我的合同列表", notes = "获得我的合同列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyContractList", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyContractList(@RequestParam(name="openid")String openid) {
        return new ResponseEntity<>(documentBlService.getMyDocumentList(openid,true), HttpStatus.OK);
    }

    @ApiOperation(value = "获取某一篇文档文章时间戳前的10篇文章", notes = "获取某一篇文档文章时间戳前的10篇文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyDocumentListBefore", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyDocumentListBefore(@RequestParam(name="openid")String openid,@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(documentBlService.getMyDocumentListBefore(openid,id,false), HttpStatus.OK);
    }

    @ApiOperation(value = "获取某一篇文档文章时间戳前的10篇文章", notes = "获取某一篇文档文章时间戳前的10篇文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "用户的openid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "用户的openid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMyContractListBefore", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EventLoadResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = WrongResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = WrongResponse.class)})
    @ResponseBody
    public ResponseEntity<Response> getMyContractListBefore(@RequestParam(name="openid")String openid,@RequestParam(name="id")String id) throws NotExistException {
        return new ResponseEntity<>(documentBlService.getMyDocumentListBefore(openid,id,true), HttpStatus.OK);
    }
}
