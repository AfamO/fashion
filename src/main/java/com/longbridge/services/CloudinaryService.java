package com.longbridge.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.exception.WawoohException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by Longbridge on 27/08/2018.
 */
@Service
public class CloudinaryService {

    public CloudinaryResponse uploadToCloud(String base64Image, String fileName, String folder){
        CloudinaryResponse cloudinaryResponse = new CloudinaryResponse();
        try {

            String image = base64Image.split(",")[1];

            System.out.println(image);

            byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            File imgfile = File.createTempFile(fileName, "tmp");
            FileUtils.copyInputStreamToFile(bis, imgfile);
            bis.close();
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "har9qnw3d",
                    "api_key", "629146977531321",
                    "api_secret", "wW5HlSfyi-2oTlj6NX60lIGWyG0"));
            Map uploadResult = cloudinary.uploader().upload(base64Image, ObjectUtils.asMap("public_id", fileName, "folder", folder));

            cloudinaryResponse.setPublicId(uploadResult.get("public_id").toString());
            cloudinaryResponse.setUrl(uploadResult.get("url").toString());
        }catch (UnknownHostException ex){
            ex.printStackTrace();
            throw new WawoohException();

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

        return cloudinaryResponse;
    }

    public CloudinaryResponse uploadFileToCloud(File base64Image, String fileName, String folder){
        CloudinaryResponse cloudinaryResponse = new CloudinaryResponse();
        try {

            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "har9qnw3d",
                    "api_key", "629146977531321",
                    "api_secret", "wW5HlSfyi-2oTlj6NX60lIGWyG0"));
            Map uploadResult = cloudinary.uploader().upload(base64Image,  ObjectUtils.asMap("public_id",fileName,"folder",folder));

            cloudinaryResponse.setPublicId(uploadResult.get("public_id").toString());
            cloudinaryResponse.setUrl(uploadResult.get("url").toString());
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

        return cloudinaryResponse;
    }


    public Map deleteFromCloud(String publicId, String fileName){

        try {

            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "har9qnw3d",
                    "api_key", "629146977531321",
                    "api_secret", "wW5HlSfyi-2oTlj6NX60lIGWyG0"));
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

}
