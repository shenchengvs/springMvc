package com.holley.mvc.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.holley.platform.common.util.StringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云工具类
 * 
 * @author sc
 */
public class QinNiuUtil {

    private static String        accessKey     = "access key";
    private static String        secretKey     = "secret key";
    private static String        bucket        = "bucket name";
    private static String        key           = "qiniu.jpg";
    private static Auth          auth          = null;
    private static Configuration cfg           = null;
    private static UploadManager uploadManager = null;

    public static Auth getNewInstance() {
        if (auth == null) {
            cfg = new Configuration(Zone.zone0());
            uploadManager = new UploadManager(cfg);
            auth = Auth.create(accessKey, secretKey);
        }
        return auth;
    }

    public static void uploadFile(String data, String key) {
        try {
            String upToken = getNewInstance().uploadToken(bucket);
            getResult(uploadManager.put(data, key, upToken));
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(byte[] data, String key) {
        try {
            String upToken = getNewInstance().uploadToken(bucket);
            getResult(uploadManager.put(data, key, upToken));
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }

    public static void uploadFile(InputStream data, String key) {
        try {
            String upToken = getNewInstance().uploadToken(bucket);
            getResult(uploadManager.put(data, key, upToken, null, null));
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }

    private static void getResult(Response response) throws JsonSyntaxException, QiniuException {
        // 解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
    }

    /**
     * 执行文件上传的
     * 
     * @param max_size
     * @param request request请求
     * @return 文件上传之后存储的路径的url List集合
     * @throws Exception
     */
    public static List uploadFile(HttpServletRequest request) throws Exception {
        List fileList = new ArrayList();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                List<MultipartFile> files = multiRequest.getFiles(iter.next());
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    String myFileName = file.getOriginalFilename();
                    if (StringUtil.isNotEmpty(myFileName)) {
                        Map map = new HashMap();
                        String type = getFileType(myFileName);
                        String fileName = get32UUID() + type;
                        // 使用七牛API进行文件保存
                        uploadFile(file.getInputStream(), fileName);
                        map.put("file_url", fileName);
                        fileList.add(map);
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * 获取文件类型
     * 
     * @param file_name
     * @return
     */
    private static String getFileType(String file_name) {
        int length = file_name.length();
        int potin = file_name.lastIndexOf(".");
        String file_type = "";
        if (potin == -1) {
            if (length > 6) {
                potin = length - 5;
            } else {
                potin = length - 3;
            }
        }
        file_type = file_name.substring(potin, length).toLowerCase();
        return file_type;
    }

    /**
     * 获取UUID 32位
     * 
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
