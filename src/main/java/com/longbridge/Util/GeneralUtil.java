package com.longbridge.Util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.exception.WriteFileException;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by Longbridge on 24/01/2018.
 */
@Service
public class GeneralUtil {

//    @Value("${event.picture.folder}")
//    private String eventPicturesImagePath;
//
//
//    @Value("${artwork.picture.folder}")
//    private String artworkPictureImagePath;
//
//    @Value("${material.picture.folder}")
//    private String materialPicturesImagePath;
//
//    @Value("${product.picture.folder}")
//    private String productPicturesImagePath;
//
//    @Value("${s.artwork.picture.folder}")
//    private String artworkPictureFolder;
//
//    @Value("${s.material.picture.folder}")
//    private String materialPicturesFolder;
//
//    @Value("${s.product.picture.folder}")
//    private String productPicturesFolder;

    public List<ProductPictureDTO> convertProdPictureEntitiesToDTO(List<ProductPicture> productPictures){
        List<ProductPictureDTO> productPictureDTOS = new ArrayList<ProductPictureDTO>();
        for(ProductPicture p: productPictures){
            ProductPictureDTO pictureDTO = convertProdPictureEntityToDTO(p);
            productPictureDTOS.add(pictureDTO);
        }
        return productPictureDTOS;
    }


    public ProductPictureDTO convertProdPictureEntityToDTO(ProductPicture picture){
        ProductPictureDTO pictureDTO = new ProductPictureDTO();
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.picture=picture.pictureName;
        return pictureDTO;

    }

    public List<ArtPictureDTO> convertArtPictureEntitiesToDTO(List<ArtWorkPicture> artWorkPictures){
        List<ArtPictureDTO> artPictureDTOS = new ArrayList<ArtPictureDTO>();
        for(ArtWorkPicture p: artWorkPictures){
            ArtPictureDTO artPictureDTO = convertArtPictureEntityToDTO(p);
            artPictureDTOS.add(artPictureDTO);
        }
        return artPictureDTOS;
    }


    public ArtPictureDTO convertArtPictureEntityToDTO(ArtWorkPicture picture){
        ArtPictureDTO pictureDTO = new ArtPictureDTO();
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.artWorkPicture=picture.pictureName;
        return pictureDTO;

    }

    public List<MaterialPictureDTO> convertMatPictureEntitiesToDTO(List<MaterialPicture> materialPictures){
        List<MaterialPictureDTO> materialPictureDTOS = new ArrayList<MaterialPictureDTO>();
        for(MaterialPicture p: materialPictures){
            MaterialPictureDTO materialPictureDTO = convertMatPictureEntityToDTO(p);
            materialPictureDTOS.add(materialPictureDTO);
        }
        return materialPictureDTOS;
    }


    public MaterialPictureDTO convertMatPictureEntityToDTO(MaterialPicture picture){
        MaterialPictureDTO pictureDTO = new MaterialPictureDTO();
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.materialPicture=picture.pictureName;
        return pictureDTO;

    }



    public List<ProductRespDTO> convertProdEntToProdRespDTOs(List<Products> products){

        List<ProductRespDTO> productDTOS = new ArrayList<ProductRespDTO>();

        for(Products p: products){
            ProductRespDTO productDTO = convertEntityToDTO(p);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    public ProductRespDTO convertEntityToDTO(Products products){
        ProductRespDTO productDTO = new ProductRespDTO();
        productDTO.id=products.id;
        productDTO.amount=products.amount;
        productDTO.color=products.color;
        productDTO.description=products.prodDesc;
        productDTO.name=products.name;
        productDTO.sizes=products.sizes;
        productDTO.styleId=products.style.id.toString();
        productDTO.designerId=products.designer.id.toString();
        productDTO.stockNo=products.stockNo;
        productDTO.inStock=products.inStock;
        productDTO.designerName=products.designer.storeName;
        productDTO.status=products.status;
        productDTO.verifiedFlag=products.verifiedFlag;
        productDTO.subCategoryId=products.subCategory.id.toString();
        productDTO.categoryId=products.subCategory.category.id.toString();

        List<ProductPicture> productPictures = products.picture;
        productDTO.picture=convertProdPictureEntitiesToDTO(productPictures);

        List<ArtWorkPicture> artWorkPictures = products.artWorkPicture;
        productDTO.artWorkPicture=convertArtPictureEntitiesToDTO(artWorkPictures);

        List<MaterialPicture> materialPictures = products.materialPicture;
        productDTO.materialPicture=convertMatPictureEntitiesToDTO(materialPictures);

        return productDTO;

    }


    public void deletePics(String pics, String folder){

        try {
            File imgFile =new File(folder + pics);
            System.out.println(imgFile);
            imgFile.delete();

        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Delete operation is failed.");
            throw new WriteFileException("Delete operation is failed");
        }
    }

    public String getPicsName(String picsArrayType, String productName){

        String timeStamp = picsArrayType + getCurrentTime();
        System.out.println(productName);
        String fName = productName.replaceAll("\\s","") + timeStamp;
        return  fName;
    }



    public String getCurrentTime(){
        int length = 10;
        SecureRandom random = new SecureRandom();
        BigInteger bigInteger = new BigInteger(130, random);

        String sessionId = String.valueOf(bigInteger.toString(length));
        return sessionId.toUpperCase();
    }


    public List<EventPicturesDTO> convertEntitiesToDTOs(List<EventPictures> events){

        List<EventPicturesDTO> picturesDTOS = new ArrayList<EventPicturesDTO>();

        for(EventPictures eventsp: events){
            EventPicturesDTO picturesDTO = convertEntityToDTO(eventsp);
            picturesDTOS.add(picturesDTO);
        }
        return picturesDTOS;
    }

    public EventPicturesDTO convertEntityToDTO(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();

        eventPicturesDTO.setId(eventPictures.id);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        return eventPicturesDTO;

    }


    public CloudinaryResponse uploadToCloud(String base64Image, String fileName, String folder){
        CloudinaryResponse cloudinaryResponse = new CloudinaryResponse();
        try {


            String image = base64Image.split(",")[1];

            byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//
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
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }


}
