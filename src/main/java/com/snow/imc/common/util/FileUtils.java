package com.snow.imc.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {



    /**
     * base64上传文件
     *
     *
     */
    public String base64SaveFile(Path path,String data,String type){
        try {
            String root=PathUtils.pathToPath(String.valueOf(path))+dateToString();
            String key=getUuid()+"."+type;
            if(!Files.exists(FileSystems.getDefault().getPath(root))) Files.createDirectories(FileSystems.getDefault().getPath(root));
            byte[] bytes=Base64.getDecoder().decode(data);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            Files.copy(new ByteArrayInputStream(bytes), Paths.get(root).resolve(key));
            return dateToString()+"/"+key;
        }catch (Exception e){
          e.printStackTrace();
        }
        return null;
    }



    public String saveFile(String path, MultipartFile file) throws IOException {
        if (path==null||file.isEmpty()) return null;
        String root=PathUtils.pathToPath(String.valueOf(path))+dateToString();
        String fileName = file.getOriginalFilename();
        //获取文件类型
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //文件名
        String key=getUuid()+"."+suffix;
        if(!Files.exists(FileSystems.getDefault().getPath(root))) Files.createDirectories(FileSystems.getDefault().getPath(root));
        if (suffix==null)return null;
        FileOutputStream fos=new FileOutputStream(new File( Paths.get(root).resolve(key).toUri()));
        FileChannel out=fos.getChannel();
        InputStream is=file.getInputStream();
        int capacity = 1024;// 字节
        ByteBuffer bf = ByteBuffer.allocate(capacity);
       for (int i=0;i<is.available();i++){






       }

        return null;
    }






    /***
     * 保存文件到磁盘
     * @param path
     * @param file
     * @return
     * @throws IOException
     */


    public  String saveFile(Path path, MultipartFile file) {
        try {
            if (file.isEmpty()) return null;
            String root=PathUtils.pathToPath(String.valueOf(path))+dateToString();
            //获取文件类型
            String fileType=getFileType(file);
            //文件名
            String key=getUuid()+"."+fileType;
            if(!Files.exists(FileSystems.getDefault().getPath(root))) Files.createDirectories(FileSystems.getDefault().getPath(root));
            if (fileType==null)return null;
            Files.copy(file.getInputStream(), Paths.get(root).resolve(key));
            return dateToString()+"/"+key;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取上传文件类型
     */
    public  String getFileType(MultipartFile file){
        String[] s = file.getOriginalFilename().split("\\.");
        List list = new ArrayList();
        for (String s1 : s) {
            list.add(s1);
        }
        if(list.size()>1){
            return list.get(list.size()-1).toString();
        }
        return null;
    }


    /**
     * 获取UUID作为文件名
     */
    public String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


    /**
     * date>>string
     * yyyyMMdd
     */
    public static String dateToString(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

    }





}

