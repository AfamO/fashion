/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SearchUtilities;
import com.longbridge.dto.ArtPicReqDTO;
import com.longbridge.dto.ArtPictureDTO;
import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.dto.EventPicturesDTO;
import com.longbridge.dto.MatPicReqDTO;
import com.longbridge.dto.MaterialPictureDTO;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PictureTagDTO;
import com.longbridge.dto.ProductColorStyleDTO;
import com.longbridge.dto.ProductDTO;
import com.longbridge.dto.ProductPictureIdListDTO;
import com.longbridge.dto.TagDTO;
import com.longbridge.dto.elasticSearch.MaterialPictureSearchDTO;
import com.longbridge.dto.elasticSearch.ProductAttributeSearchDTO;
import com.longbridge.dto.elasticSearch.ProductPictureSearchDTO;
import com.longbridge.dto.elasticSearch.ProductSearchDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.models.Product;
import com.longbridge.repository.*;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.ProductPictureService;
import com.longbridge.services.elasticSearch.ElasticSearchService;
import com.longbridge.services.elasticSearch.RemoteWebServiceLogger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author longbridge
 */
@Service
public class ProductPictureServiceImpl implements ProductPictureService{
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    ArtWorkPictureRepository artWorkPictureRepository;
    
    @Autowired
    ElasticSearchService searchService;

    @Autowired
    MaterialPictureRepository materialPictureRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ProductSizesRepository productSizesRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    EventPictureRepository eventPictureRepository;

    @Autowired
    PictureTagRepository pictureTagRepository;


    @Autowired
    ProductColorStyleRepository productColorStyleRepository;

    @Autowired
    BespokeProductRepository bespokeProductRepository;
    
    @Value("${search.url}")
    private String elastic_host_api_url; //host_api_url for elastic search
    
    RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(this.getClass());
    
    @Override
    public void addPictureTag(PictureTagDTO pictureTagDTO) {

        try {
            Long designerId;
            Long productId;
            Long subCategoryId=0L;
            Date date = new Date();

            List<TagDTO> tag = pictureTagDTO.tags;
            for(TagDTO tagDTO: tag){
                PictureTag pictureTag = new PictureTag();
                if(!tagDTO.designerId.equalsIgnoreCase("")) {
                    designerId = Long.parseLong(tagDTO.designerId);
                    pictureTag.setDesigner(designerRepository.findOne(designerId));
                }
                Long eventPictureId = Long.parseLong(pictureTagDTO.eventPicturesId);
                pictureTag.setLeftCoordinate(tagDTO.leftCoordinate);
                pictureTag.setTopCoordinate(tagDTO.topCoordinate);
                pictureTag.setImageSize(tagDTO.imageSize);
                pictureTag.setEventPictures( eventPictureRepository.findOne(eventPictureId));
                if(!tagDTO.subCategoryId.equalsIgnoreCase("")){
                    subCategoryId = Long.parseLong(tagDTO.subCategoryId);
                    pictureTag.setSubCategory(subCategoryRepository.findOne(subCategoryId));
                }

                if(!tagDTO.productId.equalsIgnoreCase("")) {
                    productId = Long.parseLong(tagDTO.productId);
                    pictureTag.setProduct(productRepository.findOne(productId));
                }
                pictureTag.setCreatedOn(date);
                pictureTag.setUpdatedOn(date);
                pictureTagRepository.save(pictureTag);

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deletePictureTag(Long id) {
        try {
            pictureTagRepository.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public TagDTO getPictureTagById(Long id) {
        try {
            PictureTag pictureTag = pictureTagRepository.findOne(id);
            return convertPicTagEntityToDTO(pictureTag);
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public PictureTagDTO getPictureTags(Long eventPictureId) {

        try {
            List<PictureTag> pictureTags = pictureTagRepository.findPictureTagsByEventPictures(eventPictureRepository.findOne(eventPictureId));
            PictureTagDTO pictureTagDTO = new PictureTagDTO();
            pictureTagDTO.tags=convertPictureTagEntityToDTO(pictureTags);
           pictureTagDTO.picture=eventPictureRepository.findOne(eventPictureId).getPictureName();
            return pictureTagDTO;

        }catch (Exception e){
            e.printStackTrace();
        }
        throw new WawoohException();
    }
    
//    @Override
//    public void updateProductImages(ProductDTO p) {
//        Date date = new Date();
//        try {
//            Product product = productRepository.findOne(p.id);
//            //Get the product from elastic search 'product' index
//            ProductSearchDTO productSearchDTO=searchService.convertIndexApiReponseToProductDTO(searchService.getProduct(elastic_host_api_url,p.id));
//            List<ProductAttributeSearchDTO> productColorStylesListSearchDTO =  new ArrayList<>();
//            List<ProductPictureSearchDTO> productPicturseSearchDTOList =  new ArrayList<>();
//            List<MaterialPictureSearchDTO> materialPictureSearchDTOList=new ArrayList<>();
//            List<ProductSizes> productSizesSearchDTOList =  new ArrayList<ProductSizes>();
//
//            List<ProductColorStyle> productColorStyles=productColorStyleRepository.findByProduct(product);
//
//            product.getProductStatuses().setAcceptCustomSizes( p.acceptCustomSizes);
//
////            productSearchDTO.setAcceptCustomSizes(p.acceptCustomSizes);
////            productSearchDTO.setNumOfDaysToComplete(p.numOfDaysToComplete);
//            //productSearchDTO.setInStock(p.inStock);
//
//            if(productColorStyles.size()>0){
//
//                for (ProductColorStyle productColorStyle: productColorStyles) {
//                    List<ProductSizes> productSizes = productSizesRepository.findByProductColorStyle(productColorStyle);
//                    productSizesRepository.delete(productSizes);
//
//                    for(ProductPicture pp:productColorStyle.getProductStyle().getProductPictures()) {
//                        Long id = pp.getId();
//                        ProductPicture productPicture = productPictureRepository.findOne(id);
//                        cloudinaryService.deleteFromCloud(productPicture.getPicture(), productPicture.getPictureName());
//                    }
//                }
//                productColorStyleRepository.delete(productColorStyles);
//            }
//
//            for (ProductColorStyleDTO pa: p.productColorStyleDTOS) {
//                ProductColorStyle productColorStyle = new ProductColorStyle();
//                productColorStyle.setProduct(product);
//                String  colourName= generalUtil.getPicsName("prodcolour",pa.getColourName());
//                System.out.println(pa.getColourPicture());
//                CloudinaryResponse c = cloudinaryService.uploadToCloud(pa.getColourPicture(),colourName,"materialpictures");
//                productColorStyle.setColourName(pa.getColourName());
//                productColorStyle.setColourPicture(c.getUrl());
//              //  productColorStyle.setStockNo(pa.getStockNo());
//                productColorStyleRepository.save(productColorStyle);
//                ProductAttributeSearchDTO productAttributeSearch=new ProductAttributeSearchDTO();
//                productAttributeSearch.setProductId(p.id);
//                productAttributeSearch.setColourName(colourName);
//                productAttributeSearch.setColourPicture(c.getUrl());
//                productColorStyleRepository.save(productColorStyle);
//
//
//                for (ProductSizes prodSizes: pa.getProductSizes()) {
//                    ProductSizes productSizes = new ProductSizes();
//                    productSizes.setName(prodSizes.getName());
//                    productSizes.setNumberInStock(prodSizes.getNumberInStock());
//                    //totalStock += prodSizes.getNumberInStock();
//                    productSizes.setProductColorStyle(productColorStyle);
//                    productSizesRepository.save(productSizes);
//                    productSizesSearchDTOList.add(productSizes);
//                }
//                productAttributeSearch.setProductSizes(productSizesSearchDTOList);
//                for(String pp : pa.getPicture()){
//
//                        ProductPicture productPicture = new ProductPicture();
//                        c = cloudinaryService.uploadToCloud(pp, generalUtil.getPicsName("prodpic", product.getSubCategory().getSubCategory()), "productpictures");
//                        System.out.println("i got here no id");
//                        productPicture.setPictureName(c.getUrl());
//                        productPicture.setPicture(c.getPublicId());
//                        productPicture.setProductStyle(productstyle);
//                        productPicture.createdOn = date;
//                        productPicture.setUpdatedOn(date);
//                        productPictureRepository.save(productPicture);
//                        ProductPictureSearchDTO productPictureSearchDTO = new ProductPictureSearchDTO();
//                        productPictureSearchDTO.picture=c.getUrl();
//                        productPictureSearchDTO.createdOn = date;
//                        productPictureSearchDTO.updatedOn=date;
//                        productPictureSearchDTO.setId(productPicture.getId());
//                        productPicturseSearchDTOList.add(productPictureSearchDTO);
//                    }
//                        productAttributeSearch.setProductPictureSearchDTOS(productPicturseSearchDTOList);
//                        productColorStylesListSearchDTO.add(productAttributeSearch);
//                        productSearchDTO.setProductAttributeDTOS(productColorStylesListSearchDTO);
//            }
//
//           // product.getProductItem().setStockNo(totalStock);
//            productSearchDTO.setStockNo(totalStock);
//            product.getProductStatuses().setVerifiedFlag("N");
//            productSearchDTO.setVerifiedFlag("N");
//            productRepository.save(product);
//            //Then save the Updated product status
//            //Update the search index to display verified product only
//            Object saveEditedProduct=searchService.UpdateProductIndex(elastic_host_api_url, productSearchDTO);
//            apiLogger.log("The Result Of ReIndexing An Updated Product Images/Properties For Elastic Search Is:"+SearchUtilities.convertObjectToJson(saveEditedProduct));
//
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new WawoohException();
//        }
//
//    }
//


    @Override
    public void updateProductImages(ProductDTO p) {
        Date date = new Date();
        try {

            Product product = productRepository.findOne(p.id);

            List<ProductColorStyle> productColorStyles=productColorStyleRepository.findByProductStyle(product.getProductStyle());

            if(productColorStyles.size()>0){

                for (ProductColorStyle productColorStyle: productColorStyles) {
                    List<ProductSizes> productSizes = productSizesRepository.findByProductColorStyle(productColorStyle);
                    productSizesRepository.delete(productSizes);

                    for(ProductPicture pp:productColorStyle.getProductPictures()) {
                        Long id = pp.getId();
                        ProductPicture productPicture = productPictureRepository.findOne(id);
                        cloudinaryService.deleteFromCloud(productPicture.getPicture(), productPicture.getPictureName());
                        productPictureRepository.delete(productPicture);
                    }
                }
                productColorStyleRepository.delete(productColorStyles);
            }

            for (ProductColorStyleDTO pa: p.productColorStyleDTOS) {
                ProductColorStyle productColorStyle=new ProductColorStyle();
                // productColorStyle.setProduct(product);
                String name = pa.getColourName().replace("&","");
                String colourName= generalUtil.getPicsName("prodcolour",name);
                CloudinaryResponse c = cloudinaryService.uploadToCloud(pa.getColourPicture(),colourName,"materialpictures");
                productColorStyle.setColourName(pa.getColourName());
                productColorStyle.setColourPicture(c.getUrl());
                productColorStyle.setProductStyle(product.getProductStyle());
                productColorStyleRepository.save(productColorStyle);

                for (ProductSizes pSizes: pa.getProductSizes()){
                    ProductSizes productSizes = new ProductSizes();
                    productSizes.setName(pSizes.getName());
                    productSizes.setNumberInStock(pSizes.getNumberInStock());
                    productSizes.setProductColorStyle(productColorStyle);
                    productSizesRepository.save(productSizes);
                }
                saveProductPicture(pa.getPicture(),date,product.getSubCategory().getSubCategory(),productColorStyle);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void saveProductPicture(List<String> pictures, Date date, String subCategory, ProductColorStyle productColorStyle) {
        for(String p:pictures){
            System.out.println(pictures);
            ProductPicture productPicture = new ProductPicture();
            String  productPictureName= generalUtil.getPicsName("prodpic", subCategory);
            CloudinaryResponse c = cloudinaryService.uploadToCloud(p,productPictureName,"productpictures");
            productPicture.setPictureName(c.getUrl());
            productPicture.setPicture(c.getPublicId());
            productPicture.setProductColorStyle(productColorStyle);
            productPicture.createdOn = date;
            productPicture.setUpdatedOn(date);
            productPictureRepository.save(productPicture);
        }
    }


    @Override
    public void updateArtWorkImages(ArtPicReqDTO artPicReqDTO) {

        try {
            Date date = new Date();
            Product product = productRepository.findOne(artPicReqDTO.productId);
            for(ArtPictureDTO pp : artPicReqDTO.artWorkPicture){
                if(pp.id != null) {
                    Long id = pp.id;
                    ArtWorkPicture artWorkPicture = artWorkPictureRepository.findOne(id);

                    cloudinaryService.deleteFromCloud(artWorkPicture.getPicture(), artWorkPicture.getPictureName());

                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", product.getSubCategory().getSubCategory()), "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture( c.getPublicId());

                    artWorkPictureRepository.save(artWorkPicture);
                }else {
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", product.getSubCategory().getSubCategory()), "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture( c.getPublicId());
                    artWorkPicture.setBespokeProduct(bespokeProductRepository.findOne(artPicReqDTO.bespokeProductId));
                    artWorkPicture.createdOn = date;
                    artWorkPicture.setUpdatedOn(date);
                    artWorkPictureRepository.save(artWorkPicture);
                }

            }
            product.getProductStatuses().setVerifiedFlag("N");
            productRepository.save(product);

        }catch (Exception e){
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public void updateMaterialImages(MatPicReqDTO matPicReqDTO) {
        Date date = new Date();
        try {

            Product product = productRepository.findOne(matPicReqDTO.productId);
            for (MaterialPictureDTO pp : matPicReqDTO.materialPicture) {
                if(pp.getId() != null) {
                    Long id = pp.getId();
                    MaterialPicture materialPicture = materialPictureRepository.findOne(id);
                    cloudinaryService.deleteFromCloud(materialPicture.getPicture(), materialPicture.getPictureName());
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.getMaterialPicture(), generalUtil.getPicsName("materialpic", product.getSubCategory().getSubCategory()), "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPicture.setMaterialName(pp.getMaterialName());


                    materialPictureRepository.save(materialPicture);
                }else {
                    MaterialPicture materialPicture = new MaterialPicture();

                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.getMaterialPicture(), generalUtil.getPicsName("materialpic", product.getSubCategory().getSubCategory()), "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPicture.setBespokeProduct(bespokeProductRepository.findOne(matPicReqDTO.bespokeProductId));
                    materialPicture.createdOn = date;
                    materialPicture.setUpdatedOn(date);
                    materialPictureRepository.save(materialPicture);
                }
            }
            product.getProductStatuses().setVerifiedFlag("N");
            productRepository.save(product);

        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void deleteProductImages(ProductPictureIdListDTO ids) {
        try {
        for (Long id:ids.getIds()) {
            ProductPicture p = productPictureRepository.findOne(id);
            cloudinaryService.deleteFromCloud(p.getPicture(), p.getPictureName());
            productPictureRepository.delete(p);
        }
        }
         catch (Exception ex){
                ex.printStackTrace();
                throw new WawoohException();
            }
    }



    @Override
    public void deleteProductImage(Long id) {
        try{
            productPictureRepository.delete(id);
        }catch (Exception ex){
            ex.printStackTrace();;
            throw new WawoohException();
        }
    }

    @Override
    public void deleteArtWorkImages(ProductPictureIdListDTO ids) {
        try {
            for (Long id:ids.getIds()) {
                ArtWorkPicture p = artWorkPictureRepository.findOne(id);
                cloudinaryService.deleteFromCloud(p.getPicture(), p.getPictureName());
                artWorkPictureRepository.delete(id);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteMaterialImages(ProductPictureIdListDTO ids) {
        try {
            for (Long id:ids.getIds()) {
                MaterialPicture p = materialPictureRepository.findOne(id);
                cloudinaryService.deleteFromCloud(p.getPicture(), p.getPictureName());
                materialPictureRepository.delete(id);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }
    
    @Override
    public List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO) {
        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        List<EventPictures> ev = new ArrayList<>();
        try {

            Page<EventPictures> e = eventPictureRepository.findAll(new PageRequest(page, size));

            for(EventPictures pictures: e) {
                if (pictureTagRepository.findByEventPictures(pictures).size() < 1) {
                    ev.add(pictures);
                }
            }

            return generalUtil.convertEntsToDTOs(ev);


        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<EventPicturesDTO> getTaggedPictures(PageableDetailsDTO pageableDetailsDTO) {
        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();

        try {
            List<EventPicturesDTO> eventPicturesDTOS= new ArrayList<>();

           Page<Long> eventPictures=pictureTagRepository.getTagged(new PageRequest(page, size));

            for(Long eventPictures1: eventPictures){
                eventPicturesDTOS.add(generalUtil.convertEntityToDTO(eventPictureRepository.findOne(eventPictures1)));
            }

        return eventPicturesDTOS;


        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<EventPicturesDTO> getUntaggedPicturesByEvents(Long id) {

        List<EventPicturesDTO> ev = new ArrayList<>();
        List<EventPictures> e;
        try {
             e = eventPictureRepository.findByEvents(eventRepository.findOne(id));
                if(e!=null) {
                    for (EventPictures pictures : e) {
                        if (pictureTagRepository.findByEventPictures(pictures).size() < 1) {
                            EventPicturesDTO picturesDTO = generalUtil.convertEntityToDTO(pictures);
                            ev.add(picturesDTO);
                        }
                    }
                }
                return ev;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<EventPicturesDTO> getTaggedPicturesByEvents(Long id) {

        List<EventPicturesDTO> ev = new ArrayList<>();
        List<EventPictures> e;
        try {
                    e = eventPictureRepository.findByEvents(eventRepository.findOne(id));
                if(e!=null) {
                    for (EventPictures pictures : e) {
                        if (pictureTagRepository.findByEventPictures(pictures).size() > 0) {
                            EventPicturesDTO picturesDTO = generalUtil.convertEntityToDTO(pictures);
                            ev.add(picturesDTO);
                        }

                    }
                }
                return ev;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }
    
   private List<TagDTO> convertPictureTagEntityToDTO(List<PictureTag> pictureTags){

        List<TagDTO> pictureTagDTOS = new ArrayList<TagDTO>();

        for(PictureTag p: pictureTags){
            TagDTO picTagDTO = convertPicTagEntityToDTO(p);
            pictureTagDTOS.add(picTagDTO);
        }
        return pictureTagDTOS;
    }


    private TagDTO convertPicTagEntityToDTO(PictureTag pictureTag){
        TagDTO pictureTagDTO = new TagDTO();
        pictureTagDTO.id=pictureTag.id;

        pictureTagDTO.topCoordinate=pictureTag.getTopCoordinate();
        pictureTagDTO.leftCoordinate=pictureTag.getLeftCoordinate();
        pictureTagDTO.imageSize=pictureTag.getImageSize();
        pictureTagDTO.subCategoryId = pictureTag.getSubCategory().id.toString();
        pictureTagDTO.subCategoryName=pictureTag.getSubCategory().getSubCategory();
        if(pictureTag.getDesigner() != null){
            pictureTagDTO.designerId = pictureTag.getDesigner().id.toString();
            pictureTagDTO.designerName = pictureTag.getDesigner().getStoreName();
        }

        if(pictureTag.getProduct() != null){
            pictureTagDTO.productId = pictureTag.getProduct().id.toString();
            pictureTagDTO.productName = pictureTag.getProduct().getName();
        }

        return pictureTagDTO;

    }
}
