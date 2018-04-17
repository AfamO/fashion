package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.exception.WriteFileException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.ProductService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.util.*;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    ArtWorkPictureRepository artWorkPictureRepository;

    @Autowired
    MaterialPictureRepository materialPictureRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    public ProductServiceImpl(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }


    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    EventPictureRepository eventPictureRepository;

    @Autowired
    StyleRepository styleRepository;

    @Autowired
    PictureTagRepository pictureTagRepository;

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


    @Override
    public ProductRespDTO getProductById(Long id) {

        try {
            Products products = productRepository.findOne(id);
            ProductRespDTO productDTO = generalUtil.convertEntityToDTO(products);
            //ProductDTO productDTO = convertEntityToDTO(products);
            return productDTO;
        }catch (Exception e){
            e.printStackTrace();
        }
       throw new WawoohException();
    }


    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        try {
            Date date = new Date();
            String categoryName = categoryDTO.getCategoryName();
            Category category = new Category();
            category.setCreatedOn(date);
            category.setUpdatedOn(date);
            category.categoryName= categoryName;
            categoryRepository.save(category);


        } catch (Exception e) {
            e.printStackTrace();
          throw new WawoohException();
        }

    }

    @Override
    public void addSubCategory(SubCategoryDTO subCategoryDTO) {

        try {
            Date date = new Date();

            Long categoryId = Long.parseLong(subCategoryDTO.categoryId);

            List<String> subCategoryList = subCategoryDTO.subCategoryName;
            System.out.println(subCategoryList);
            for(String s: subCategoryList){
                SubCategory subCategory = new SubCategory();
                subCategory.subCategory= s;
                subCategory.category = categoryRepository.findOne(categoryId);
                subCategory.setCreatedOn(date);
                subCategory.setUpdatedOn(date);
                subCategoryRepository.save(subCategory);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public Category getCategoryById(Long id) {
        try {
            return categoryRepository.findOne(id);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void addPictureTag(PictureTagDTO pictureTagDTO) {

        try {
            Long designerId=0L;
            Long productId=0L;
            Long subCategoryId=0L;
            Date date = new Date();

            List<TagDTO> tag = pictureTagDTO.tags;
            for(TagDTO tagDTO: tag){
                PictureTag pictureTag = new PictureTag();
                if(!tagDTO.designerId.equalsIgnoreCase("")) {
                    designerId = Long.parseLong(tagDTO.designerId);
                    pictureTag.designer=designerRepository.findOne(designerId);
                }
                Long eventPictureId = Long.parseLong(pictureTagDTO.eventPicturesId);
                pictureTag.leftCoordinate=tagDTO.leftCoordinate;
                pictureTag.topCoordinate = tagDTO.topCoordinate;
                pictureTag.imageSize=tagDTO.imageSize;
                pictureTag.eventPictures = eventPictureRepository.findOne(eventPictureId);
                if(!tagDTO.subCategoryId.equalsIgnoreCase("")){
                    subCategoryId = Long.parseLong(tagDTO.subCategoryId);
                    pictureTag.subCategory=subCategoryRepository.findOne(subCategoryId);
                }

                if(!tagDTO.productId.equalsIgnoreCase("")) {
                    productId = Long.parseLong(tagDTO.productId);
                    pictureTag.products=productRepository.findOne(productId);
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
    public List<PicTagDTO> getPictureTags(Long eventPictureId) {
        Map<String,Object> responseMap = new HashMap();
        try {
            List<PictureTag> pictureTags = pictureTagRepository.findPictureTagsByEventPictures(eventPictureRepository.findOne(eventPictureId));

            List<PicTagDTO> pictureTagDTOS = convertPictureTagEntityToDTO(pictureTags);
            return pictureTagDTOS;

        }catch (Exception e){
            e.printStackTrace();
        }
        throw new WawoohException();
    }

    @Override
    public void addStyle(StyleDTO styleDTO) {
        Map<String,Object> responseMap = new HashMap();

        try {
            Date date = new Date();

            //Long categoryId = Long.parseLong(styleDTO.categoryId);
            Long subCategoryId = Long.parseLong(styleDTO.subCategoryId);
            List<String> styleList = styleDTO.style;
            for(String s: styleList){
                Style style = new Style();
                style.style= s;
                style.subCategory = subCategoryRepository.findOne(subCategoryId);
                style.setCreatedOn(date);
                style.setUpdatedOn(date);
                styleRepository.save(style);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<Category> getAllCategories() {

        try {
           List<Category> categories = categoryRepository.findAll();
            return categories;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<SubCategory> getSubCategories(Long categoryId) {

        try {
            List<SubCategory> subCategories = subCategoryRepository.findByCategory(categoryRepository.findOne(categoryId));
            return subCategories;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<Style> getStyles(Long subCategoryId) {
        try {
            List<Style> styles = styleRepository.findBySubCategory(subCategoryRepository.findOne(subCategoryId));
            return styles;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void addProduct(ProductDTO productDTO, Designer designer) {

        try {
            Date date = new Date();
            Products products = new Products();
            Long styleId = Long.parseLong(productDTO.styleId);
            Long subCategoryId = Long.parseLong(productDTO.subCategoryId);
            ArrayList<String> pics = productDTO.picture;
            ArrayList<String> artWorkPics = productDTO.artWorkPicture;
            ArrayList<String> materialPics = productDTO.materialPicture;

            products.subCategory = subCategoryRepository.findOne(subCategoryId);
            products.name=productDTO.name;
            products.amount = productDTO.amount;
            products.availability = productDTO.inStock;
            products.color = productDTO.color;
            products.sizes = productDTO.sizes;
            products.prodDesc=productDTO.description;
            products.designer=designer;
            products.style=styleRepository.findOne(styleId);
            products.stockNo=productDTO.stockNo;
            products.inStock=productDTO.inStock;
            products.setCreatedOn(date);
            products.setUpdatedOn(date);
            productRepository.save(products);

            for(String p:pics){
                ProductPicture productPicture = new ProductPicture();
                String  productPictureName= generalUtil.getPicsName("prodpic",products.name);


//                String base64Image = p.split(",")[1];
//
//                    byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
//                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
////
//                    File imgfile = File.createTempFile(productPictureName,"tmp");
//                    FileUtils.copyInputStreamToFile(bis,imgfile);
//                    bis.close();

                    CloudinaryResponse c = generalUtil.uploadToCloud(p,productPictureName,"productpictures");

                    //productPicture.pictureName = productPictureName;
                    productPicture.pictureName=c.getUrl();
                    productPicture.picture = c.getPublicId();
                    productPicture.products = products;
                    productPicture.createdOn = date;
                    productPicture.setUpdatedOn(date);
                    productPictureRepository.save(productPicture);
            }

            for(String mp:materialPics){
                MaterialPicture materialPicture = new MaterialPicture();
                String matName= generalUtil.getPicsName("materialpic",products.name);
                    //materialPicture.pictureName = matName;
                CloudinaryResponse c= generalUtil.uploadToCloud(mp,matName,"materialpictures");
                    materialPicture.pictureName = c.getUrl();
                    materialPicture.picture = c.getPublicId();
                    materialPicture.products = products;
                    materialPicture.createdOn = date;
                    materialPicture.setUpdatedOn(date);
                    materialPictureRepository.save(materialPicture);
            }



            for(String ap:artWorkPics){
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    String artName= generalUtil.getPicsName("artworkpic",products.name);
                    //artWorkPicture.pictureName = artName;
                CloudinaryResponse c = generalUtil.uploadToCloud(ap,artName,"artworkpictures");
                artWorkPicture.pictureName = c.getUrl();
                artWorkPicture.picture = c.getPublicId();
                    artWorkPicture.products = products;
                    artWorkPicture.createdOn = date;
                    artWorkPicture.setUpdatedOn(date);
                    artWorkPictureRepository.save(artWorkPicture);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateProduct(ProductDTO productDTO, Designer designer) {

        try {
            Date date = new Date();

            Long subCategoryId = Long.parseLong(productDTO.subCategoryId);
            Products products = productRepository.findOne(productDTO.id);
            products.subCategory = subCategoryRepository.findOne(subCategoryId);
            products.name=productDTO.name;
            products.amount = productDTO.amount;
            products.availability = productDTO.inStock;
            products.color = productDTO.color;
            products.sizes = productDTO.sizes;
            products.prodDesc=productDTO.description;
            products.designer=designer;
            if(productDTO.styleId != null) {
                Long styleId = Long.parseLong(productDTO.styleId);
                products.style=styleRepository.findOne(styleId);
            }
            products.stockNo=productDTO.stockNo;
            products.inStock=productDTO.inStock;
            products.setUpdatedOn(date);
            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void updateProductImages(ProdPicReqDTO p) {
Date date = new Date();
        try {
            Products products = productRepository.findOne(p.productId);
            for(ProductPictureDTO pp : p.picture){

                if(pp.id != null) {
                    Long id = pp.id;
                    ProductPicture productPicture = productPictureRepository.findOne(id);

                    generalUtil.deleteFromCloud(productPicture.picture, productPicture.pictureName);

                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.picture, generalUtil.getPicsName("prodpic", products.name), "productpictures");
                    productPicture.pictureName = c.getUrl();
                    productPicture.picture = c.getPublicId();
                    productPicture.setUpdatedOn(date);
                    productPictureRepository.save(productPicture);
                }
                else {
                    ProductPicture productPicture = new ProductPicture();
                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.picture, generalUtil.getPicsName("prodpic", products.name), "productpictures");
                    productPicture.pictureName = c.getUrl();
                    productPicture.picture = c.getPublicId();
                    productPicture.products = products;
                    productPicture.createdOn = date;
                    productPicture.setUpdatedOn(date);
                    productPictureRepository.save(productPicture);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void updateArtWorkImages(ArtPicReqDTO artPicReqDTO) {

        try {
            Date date = new Date();
            Products products = productRepository.findOne(artPicReqDTO.productId);
            for(ArtPictureDTO pp : artPicReqDTO.artWorkPicture){

                if(pp.id != null) {
                    Long id = pp.id;
                    ArtWorkPicture artWorkPicture = artWorkPictureRepository.findOne(id);

                    generalUtil.deleteFromCloud(artWorkPicture.picture, artWorkPicture.pictureName);

                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", products.name), "artworkpictures");
                    artWorkPicture.pictureName = c.getUrl();
                    artWorkPicture.picture = c.getPublicId();

                    artWorkPictureRepository.save(artWorkPicture);
                }else {
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", products.name), "artworkpictures");
                    artWorkPicture.pictureName = c.getUrl();
                    artWorkPicture.picture = c.getPublicId();
                    artWorkPicture.products = products;
                    artWorkPicture.createdOn = date;
                    artWorkPicture.setUpdatedOn(date);
                    artWorkPictureRepository.save(artWorkPicture);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public void updateMaterialImages(MatPicReqDTO matPicReqDTO) {
        Date date = new Date();
        try {

            Products products = productRepository.findOne(matPicReqDTO.productId);
            for (MaterialPictureDTO pp : matPicReqDTO.materialPicture) {
                if(pp.id != null) {
                    Long id = pp.id;
                    MaterialPicture materialPicture = materialPictureRepository.findOne(id);
                    generalUtil.deleteFromCloud(materialPicture.picture, materialPicture.pictureName);
                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.materialPicture, generalUtil.getPicsName("materialpic", products.name), "materialpictures");
                    materialPicture.pictureName = c.getUrl();
                    materialPicture.picture = c.getPublicId();


                    materialPictureRepository.save(materialPicture);
                }else {
                    MaterialPicture materialPicture = new MaterialPicture();

                    CloudinaryResponse c = generalUtil.uploadToCloud(pp.materialPicture, generalUtil.getPicsName("materialpic", products.name), "materialpictures");
                    materialPicture.pictureName = c.getUrl();
                    materialPicture.picture = c.getPublicId();
                    materialPicture.products = products;
                    materialPicture.createdOn = date;
                    materialPicture.setUpdatedOn(date);
                    materialPictureRepository.save(materialPicture);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void updateProductStatus(Long id, String status) {
        Map<String,Object> responseMap = new HashMap();
        try {
            Products products = productRepository.findOne(id);
            products.status=status;
            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Map<String,Object> responseMap = new HashMap();
        try {
            Products p = productRepository.findOne(id);
            productPictureRepository.findByProducts(p).forEach(pictures -> {
                generalUtil.deleteFromCloud(pictures.picture,pictures.pictureName);
             //deletePics(pictures.pictureName,productPicturesFolder);
            });

            artWorkPictureRepository.findByProducts(p).forEach(pictures -> {
                generalUtil.deleteFromCloud(pictures.picture,pictures.pictureName);
                //deletePics(pictures.pictureName,artworkPictureFolder);
            });

            materialPictureRepository.findByProducts(p).forEach(pictures -> {
                generalUtil.deleteFromCloud(pictures.picture,pictures.pictureName);
                //deletePics(pictures.pictureName,artworkPictureFolder);
            });

            productRepository.delete(id);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteProductImages(Long id) {
        ProductPicture p = productPictureRepository.findOne(id);
            generalUtil.deleteFromCloud(p.picture,p.pictureName);
            productPictureRepository.delete(id);
    }

    @Override
    public void deleteArtWorkImages(Long id) {
        ArtWorkPicture p = artWorkPictureRepository.findOne(id);
        generalUtil.deleteFromCloud(p.picture,p.pictureName);
        artWorkPictureRepository.delete(id);
    }

    @Override
    public void deleteMaterialImages(Long id) {
        MaterialPicture p = materialPictureRepository.findOne(id);
        generalUtil.deleteFromCloud(p.picture,p.pictureName);
        materialPictureRepository.delete(id);
    }

    @Override
    public List<ProductRespDTO> getProductsByDesigner(Long designerId) {


        try {
            List<Products> products = productRepository.findByDesigner(designerRepository.findOne(designerId));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products);
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getAllProducts(PageableDetailsDTO pageableDetailsDTO) {

        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        try {
            Page<Products> products = productRepository.findAll(new PageRequest(page,size));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
           return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p) {

        int page = Integer.parseInt(p.page);
        int size = Integer.parseInt(p.size);
        Page<Products> products= null;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.subcategoryId);


                products = productRepository.findBySubCategory(new PageRequest(page, size), subCategory);

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }


    @Override
    public List<ProductRespDTO> getTopProducts() {

        try {
            List<Products> products= productRepository.findTop10ByOrderByNumOfTimesOrderedDesc();

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products);
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getFeaturedProducts() {
        try {
            List<Products> products= productRepository.findTop10ByOrderByNumOfTimesOrderedDesc();

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products);
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p, User user) {

        int page = Integer.parseInt(p.page);
        int size = Integer.parseInt(p.size);
        Page<Products> products= null;
        Designer designer = null;
        System.out.println(p.subcategoryId);
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.subcategoryId);
            if(p.designerId != null) {
                designer = designerRepository.findOne(p.designerId);
            }

            if(user != null && user.designer != null){
                designer=user.designer;
            }

            products = productRepository.findByDesignerAndSubCategory(new PageRequest(page,size),designer,subCategory);

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO) {
        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        List<EventPictures> ev = new ArrayList<>();
        try {
            //List<EventPictures> e = eventPictureRepository.findAll();
            Page<EventPictures> e = eventPictureRepository.findAll(new PageRequest(page, size));

            for(EventPictures pictures: e) {
                if (pictureTagRepository.findByEventPictures(pictures).size() < 1) {
                    ev.add(pictures);
                }
            }

            List<EventPicturesDTO> picturesDTOS = generalUtil.convertEntsToDTOs(ev);
            return picturesDTOS;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }





    private List<PicTagDTO> convertPictureTagEntityToDTO(List<PictureTag> pictureTags){

        List<PicTagDTO> pictureTagDTOS = new ArrayList<PicTagDTO>();

        for(PictureTag p: pictureTags){
            PicTagDTO picTagDTO = convertPicTagEntityToDTO(p);
            pictureTagDTOS.add(picTagDTO);
        }
        return pictureTagDTOS;
    }


    private PicTagDTO convertPicTagEntityToDTO(PictureTag pictureTag){
        PicTagDTO pictureTagDTO = new PicTagDTO();
        pictureTagDTO.id=pictureTag.id;
        pictureTagDTO.topCoordinate=pictureTag.topCoordinate;
        pictureTagDTO.leftCoordinate=pictureTag.leftCoordinate;
        pictureTagDTO.imageSize=pictureTag.imageSize;
        pictureTagDTO.subcategoryId = pictureTag.subCategory.id;
        if(pictureTag.designer != null){
            pictureTagDTO.designerId = pictureTag.designer.id;
            pictureTagDTO.designerName = pictureTag.designer.storeName;
        }
        //pictureTagDTO.designerId = pictureTag.designer.id;


        return pictureTagDTO;

    }


    private void deletePics(String pics, String folder){

            try {
                File imgFile =new File(folder + pics);
                imgFile.delete();

            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Delete operation is failed.");
                throw new WriteFileException("Delete operation is failed");
            }
    }

}
