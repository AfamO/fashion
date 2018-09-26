package com.longbridge.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.services.elasticSearch.RemoteWebServiceLogger;
import com.longbridge.dto.*;
import com.longbridge.dto.elasticSearch.MaterialPictureSearchDTO;
import com.longbridge.dto.elasticSearch.ProductAttributeSearchDTO;
import com.longbridge.dto.elasticSearch.ProductPictureSearchDTO;
import com.longbridge.dto.elasticSearch.ProductSearchDTO;
import com.longbridge.exception.InvalidAmountException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.models.elasticSearch.ApiResponse;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.elasticSearch.ElasticSearchService;
import com.longbridge.services.ProductService;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    ProductRatingRepository productRatingRepository;

    @Autowired
    ArtWorkPictureRepository artWorkPictureRepository;
    
    @Autowired
    ElasticSearchService searchService;

    @Autowired
    MaterialPictureRepository materialPictureRepository;


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ProductSizesRepository productSizesRepository;

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

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    PriceSlashRepository priceSlashRepository;

    @Autowired
    ProductAttributeRepository productAttributeRepository;



    @Override
    public ProductRespDTO getDesignerProductById(Long id) {
        try {
            ItemStatus itemStatus1 = itemStatusRepository.findByStatus("OP");
            ItemStatus itemStatus2 = itemStatusRepository.findByStatus("CO");
            ItemStatus itemStatus3 = itemStatusRepository.findByStatus("RI");
            List<ItemStatus> itemStatuses = new ArrayList();
            itemStatuses.add(itemStatus1);
            itemStatuses.add(itemStatus2);
            itemStatuses.add(itemStatus3);
            Products products = productRepository.findOne(id);
            ProductRespDTO productDTO = generalUtil.convertEntityToDTO(products);
            Designer designer = designerRepository.findByUser(getCurrentUser());
            int salesInQueue = itemRepository.findActiveOrdersOnProduct(designer.id,products.id,itemStatuses);
            int totalSales = itemRepository.countByDesignerIdAndProductIdAndItemStatus_Status(designer.id,products.id,"D");
            productDTO.salesInQueue=salesInQueue;
            productDTO.totalSales=totalSales;
            return productDTO;
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new WawoohException();
    }


    @Override
    public ProductRespDTO getProductById(Long id, boolean reviewsPresent) {
        try {
            User user = getCurrentUser();
            Products products = productRepository.findOne(id);
            ProductRespDTO productDTO;
            if(reviewsPresent)
                productDTO = generalUtil.convertEntityToDTOWithReviews(products);
            else
                productDTO = generalUtil.convertEntityToDTO(products);
            if(user != null){
                if(wishListRepository.findByUserAndProducts(user,products) != null){
                    productDTO.wishListFlag="Y";
                }
                else {
                    productDTO.wishListFlag="N";
                }
            }
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
            List<String> subCategoryList = subCategoryDTO.subCategoryName;
            System.out.println(subCategoryList);
            for(String s: subCategoryList){
                SubCategory subCategory = new SubCategory();
                subCategory.setSubCategory(s);
                subCategory.setProductType(subCategoryDTO.productType);
                subCategory.setCategory(categoryRepository.findOne(subCategoryDTO.categoryId));
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
    public void deleteSubCategory(Long id) {

        try {
            Date date = new Date();

            SubCategory subCategory = subCategoryRepository.findOne(id);
            subCategory.setDelFlag("Y");
            subCategory.setUpdatedOn(date);
            subCategoryRepository.save(subCategory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }



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
                    pictureTag.setProducts(productRepository.findOne(productId));
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
                style.setStyle(s);
                style.setSubCategory(subCategoryRepository.findOne(subCategoryId));
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
    @Transactional
    public String addProduct(ProductDTO productDTO,String elastic_search_host_api_url) {
        try {

            if(productDTO.amount < 0 || productDTO.slashedPrice < 0 || productDTO.percentageDiscount <0){
                throw new InvalidAmountException();
            }
            User user = getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            Date date = new Date();
            int totalStock = 0;
            Products products = new Products();
            ProductSearchDTO productSearchDTO= new ProductSearchDTO();
            Long subCategoryId = Long.parseLong(productDTO.subCategoryId);

            ArrayList<String> artWorkPics = productDTO.artWorkPicture;
            ArrayList<MaterialPictureDTO> materialPics = productDTO.materialPicture;
            productSearchDTO.setSubCategoryId(productDTO.subCategoryId);
            products.setSubCategory( subCategoryRepository.findOne(subCategoryId));
            products.setName(productDTO.name);
            productSearchDTO.setName(productDTO.name);
            products.setAmount(productDTO.amount);
            productSearchDTO.setAmount(productDTO.amount);
            productSearchDTO.setPicture(productDTO.picture);
            productSearchDTO.setAvailability(productDTO.inStock);
            products.setAvailability(productDTO.inStock);
            products.setAcceptCustomSizes(productDTO.acceptCustomSizes);
            productSearchDTO.setNumOfDaysToComplete(productDTO.numOfDaysToComplete);
            products.setNumOfDaysToComplete(productDTO.numOfDaysToComplete);
            productSearchDTO.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.setMaterialPrice(productDTO.materialPrice);
            productSearchDTO.setMaterialPrice(productDTO.materialPrice);
            productSearchDTO.setMaterialName(productDTO.materialName);
            productSearchDTO.setCategoryName(products.getSubCategory().getCategory().categoryName);
            productSearchDTO.setSubCategoryName(products.getSubCategory().getSubCategory());
            productSearchDTO.setProdSummary(productDTO.prodSummary);
            products.setProdSummary(productDTO.prodSummary);
            productSearchDTO.setDescription(productDTO.description);
            products.setProdDesc(productDTO.description);
            productSearchDTO.setDesignerId(Long.toString(designer.id));
            productSearchDTO.setDesignerStatus(designer.getStatus());
            productSearchDTO.setStatus(productDTO.status);
            productSearchDTO.setDesignerName(designer.getUser().getFirstName()+" "+designer.getUser().getLastName());
            products.setDesigner(designer);
            productSearchDTO.setProductType(productDTO.productType);
            products.setProductType(productDTO.productType);

            if(productDTO.styleId != null && !productDTO.styleId.equalsIgnoreCase("null")) {
                if(!productDTO.styleId.isEmpty()) {
                    Long styleId = Long.parseLong(productDTO.styleId);
                    products.setStyle(styleRepository.findOne(styleId));
                }
            }

            products.setStockNo(productDTO.stockNo);
            productSearchDTO.setStockNo(productDTO.stockNo);
            products.setInStock(productDTO.inStock);
            productSearchDTO.setInStock(productDTO.inStock);
            productSearchDTO.setId(products.id);
            products.setCreatedOn(date);
            products.setUpdatedOn(date);

            productRepository.save(products);
            List<ProductAttributeSearchDTO> productAttributesListSearchDTO =  new ArrayList<>();
            List<ProductPictureSearchDTO> productPicturseSearchDTOList =  new ArrayList<>();
            List<MaterialPictureSearchDTO> materialPictureSearchDTOList=new ArrayList<>();
            List<ProductSizes> productSizesSearchDTOList =  new ArrayList<ProductSizes>();
            for (ProductAttributeDTO pa: productDTO.productAttributes) {
                ProductAttribute productAttribute=new ProductAttribute();
                ProductAttributeSearchDTO productAttributeSearch=new ProductAttributeSearchDTO();
                productAttribute.setProducts(products);
                productAttributeSearch.setProductId(productDTO.id);
                String colourName= generalUtil.getPicsName("prodcolour",pa.getColourName());
                CloudinaryResponse c = cloudinaryService.uploadToCloud(pa.getColourPicture(),colourName,"materialpictures");
                productAttribute.setColourName(pa.getColourName());
                productAttributeSearch.setColourName(colourName);
                productAttribute.setColourPicture(c.getUrl());
                productAttributeSearch.setColourPicture(c.getUrl());
                productAttributeRepository.save(productAttribute);

                for (ProductSizes p: pa.getProductSizes()) {
                    ProductSizes productSizes = new ProductSizes();
                    productSizes.setName(p.getName());
                    productSizes.setNumberInStock(p.getNumberInStock());
                    totalStock += p.getNumberInStock();
                    productSizes.setProductAttribute(productAttribute);
                    System.out.println(productSizes);
                    productSizesRepository.save(productSizes);
                    productSizesSearchDTOList.add(productSizes);
                }
                productAttributeSearch.setProductSizes(productSizesSearchDTOList);
                for(String p:pa.getPicture()){
                    ProductPicture productPicture = new ProductPicture();
                    String  productPictureName= generalUtil.getPicsName("prodpic",products.getName());
                    ProductPictureSearchDTO productPictureSearchDTO = new ProductPictureSearchDTO();
                    c = cloudinaryService.uploadToCloud(p,productPictureName,"productpictures");
                    productPicture.setPictureName(c.getUrl());
                    productPicture.setPicture(c.getPublicId());
                    productPictureSearchDTO.picture=c.getUrl();
                    productPicture.setProducts(products);
                    productPictureSearchDTO.createdOn = date;
                    productPictureSearchDTO.updatedOn=date;
                    productPicture.createdOn = date;
                    productPicture.setUpdatedOn(date);
                    productPicture.setProductAttribute(productAttribute);
                    productPictureSearchDTO.setId(productPicture.getId());
                    productPicturseSearchDTOList.add(productPictureSearchDTO);
                    productPictureRepository.save(productPicture);
                }
                productAttributeSearch.setProductPictureSearchDTOS(productPicturseSearchDTOList);
                productAttributesListSearchDTO.add(productAttributeSearch);
                productSearchDTO.setProductAttributeDTOS(productAttributesListSearchDTO) ;
            }

            if(productDTO.slashedPrice > 0){
                PriceSlash priceSlash = new PriceSlash();
                products.setPriceSlashEnabled(true);
                priceSlash.setProducts(products);
                priceSlash.setSlashedPrice(productDTO.slashedPrice);
                priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                productSearchDTO.setSlashedPrice(productDTO.slashedPrice);
                productSearchDTO.setPercentageDiscount((productDTO.slashedPrice/productDTO.amount)*100);
                priceSlashRepository.save(priceSlash);
            } else if(productDTO.percentageDiscount > 0){

                PriceSlash priceSlash=new PriceSlash();
                products.setPriceSlashEnabled(true);
                priceSlash.setProducts(products);
                priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getAmount()));
                priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                productSearchDTO.setSlashedPrice((productDTO.percentageDiscount/100)*products.getAmount());
                productSearchDTO.setPercentageDiscount(productDTO.percentageDiscount);
                priceSlashRepository.save(priceSlash);
            }



            if( productDTO.productType == 1) {
                for (MaterialPictureDTO mp : materialPics) {
                    MaterialPicture materialPicture = new MaterialPicture();
                    MaterialPictureSearchDTO  materialPictureSearchDTO  =new MaterialPictureSearchDTO ();
                    String matName = generalUtil.getPicsName("materialpic", products.getName());
                    //materialPicture.pictureName = matName;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(mp.getMaterialPicture(), matName, "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPictureSearchDTO.setPicture(c.getUrl());
                    materialPictureSearchDTO.setPictureName(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPictureSearchDTO.setMaterialName(mp.getMaterialName());
                    materialPicture.setMaterialName(mp.getMaterialName());
                    materialPicture.setProducts(products);
                    materialPicture.createdOn = date;
                    materialPicture.setUpdatedOn(date);
                    materialPictureRepository.save(materialPicture);
                    materialPictureSearchDTO.setId(materialPicture.getId());
                    materialPictureSearchDTOList.add(materialPictureSearchDTO);
                }
                productSearchDTO.setMaterialPicture(materialPictureSearchDTOList);
                for (String ap : artWorkPics) {
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    String artName = generalUtil.getPicsName("artworkpic", products.getName());
                    //artWorkPicture.pictureName = artName;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(ap, artName, "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture(c.getPublicId());
                    artWorkPicture.setProducts(products);
                    artWorkPicture.createdOn = date;
                    artWorkPicture.setUpdatedOn(date);
                    artWorkPictureRepository.save(artWorkPicture);
                }
            }

            products.setStockNo(totalStock);
            productSearchDTO.setStockNo(totalStock);
            productRepository.save(products);
            Gson gson= new Gson();
            ObjectMapper objectMapper = new ObjectMapper();
            String productSearchDTOWriteValueAsString = objectMapper.writeValueAsString(productSearchDTO);

            RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(this.getClass()); 
            ApiResponse makeRemoteRequest = searchService.makeRemoteRequest( elastic_search_host_api_url,"/products/_doc/"+products.id+"/","put","create_index","products",productSearchDTOWriteValueAsString);
            apiLogger.log("The Result Of Indexing A  New Product For Elastic Search Is:"+gson.toJson(makeRemoteRequest));
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        try {
            User user = getCurrentUser();
            Date date = new Date();
            Designer designer = designerRepository.findByUser(user);
            Long subCategoryId = Long.parseLong(productDTO.subCategoryId);
            Products products = productRepository.findOne(productDTO.id);
            products.setSubCategory(subCategoryRepository.findOne(subCategoryId));
            products.setName(productDTO.name);
            products.setAmount(productDTO.amount);
            products.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.setProdDesc(productDTO.description);
            products.setProdSummary(productDTO.prodSummary);
            products.setDesigner(designer);
            if(!"null".equalsIgnoreCase(productDTO.styleId)) {
                if(!productDTO.styleId.isEmpty()) {
                    Long styleId = Long.parseLong(productDTO.styleId);
                    products.setStyle(styleRepository.findOne(styleId));
                }
            }
            products.setStockNo(productDTO.stockNo);
            products.setUpdatedOn(date);

            if(productDTO.slashedPrice > 0){
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlash.setSlashedPrice(productDTO.slashedPrice);
                    priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                }else {
                    priceSlash=new PriceSlash();
                    products.setPriceSlashEnabled(true);
                    priceSlash.setProducts(products);
                    priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                    priceSlash.setSlashedPrice(productDTO.slashedPrice);
                }

                priceSlashRepository.save(priceSlash);
            }
            else if(productDTO.percentageDiscount > 0){
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getAmount()));
                    priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                }else {
                    priceSlash=new PriceSlash();
                    products.setPriceSlashEnabled(true);
                    priceSlash.setProducts(products);
                    priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getAmount()));
                    priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                }

                priceSlashRepository.save(priceSlash);
            }else{
                products.setPriceSlashEnabled(false);
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlashRepository.delete(priceSlash);
                }
            }

            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    //todo come back later
    @Override
    public void updateProductStock(ProductDTO productDTO) {
        try {
            Products products = productRepository.findOne(productDTO.id);


            if(productDTO.productAttributes != null){
                List<ProductAttribute> productAttributes=productAttributeRepository.findByProducts(products);
                productAttributeRepository.delete(productAttributes);
                for (ProductAttributeDTO p: productDTO.productAttributes) {
//                    p.setProducts(products);
//                    productAttributeRepository.save(p);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }



    @Override
    public void updateProductImages(ProductDTO p) {
        Date date = new Date();
        try {
            Products products = productRepository.findOne(p.id);
            int totalStock = 0;
            List<ProductAttribute> productAttributes=productAttributeRepository.findByProducts(products);
            List<String> reOccuringPictures = new ArrayList<String>();
            products.setAcceptCustomSizes( p.acceptCustomSizes);
            products.setInStock(p.inStock);
            products.setNumOfDaysToComplete( p.numOfDaysToComplete);

            if(productAttributes.size()>0){

                for (ProductAttribute prA: productAttributes) {
                    List<ProductSizes> productSizes = productSizesRepository.findByProductAttribute(prA);
                    productSizesRepository.delete(productSizes);

                    for(ProductPicture pp:prA.getProductPictures()) {
                        Long id = pp.getId();
                        ProductPicture productPicture = productPictureRepository.findOne(id);
                        cloudinaryService.deleteFromCloud(productPicture.getPicture(), productPicture.getPictureName());
                    }
                }
                productAttributeRepository.delete(productAttributes);
            }

            for (ProductAttributeDTO pa: p.productAttributes) {
                ProductAttribute productAttribute = new ProductAttribute();
                productAttribute.setProducts(products);
                String  colourName= generalUtil.getPicsName("prodcolour",pa.getColourName());
                System.out.println(pa.getColourPicture());
                CloudinaryResponse c = cloudinaryService.uploadToCloud(pa.getColourPicture(),colourName,"materialpictures");
                productAttribute.setColourName(pa.getColourName());
                productAttribute.setColourPicture(c.getUrl());
                productAttributeRepository.save(productAttribute);



                for (ProductSizes prodSizes: pa.getProductSizes()) {
                    ProductSizes productSizes = new ProductSizes();
                    productSizes.setName(prodSizes.getName());
                    productSizes.setNumberInStock(prodSizes.getNumberInStock());
                    totalStock += prodSizes.getNumberInStock();
                    productSizes.setProductAttribute(productAttribute);
                    productSizesRepository.save(productSizes);
                }

                for(String pp : pa.getPicture()){

                        ProductPicture productPicture = new ProductPicture();
                        c = cloudinaryService.uploadToCloud(pp, generalUtil.getPicsName("prodpic", products.getName()), "productpictures");
                        System.out.println("i got here no id");
                        productPicture.setPictureName(c.getUrl());
                        productPicture.setPicture(c.getPublicId());
                        productPicture.setProducts(products);
                        productPicture.createdOn = date;
                        productPicture.setUpdatedOn(date);
                        productPicture.setProductAttribute(productAttribute);
                        productPictureRepository.save(productPicture);
                    }
            }

            products.setStockNo(totalStock);
            productRepository.save(products);

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

                    cloudinaryService.deleteFromCloud(artWorkPicture.getPicture(), artWorkPicture.getPictureName());

                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", products.getName()), "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture( c.getPublicId());

                    artWorkPictureRepository.save(artWorkPicture);
                }else {
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.artWorkPicture, generalUtil.getPicsName("artworkpic", products.getName()), "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture( c.getPublicId());
                    artWorkPicture.setProducts(products);
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
                if(pp.getId() != null) {
                    Long id = pp.getId();
                    MaterialPicture materialPicture = materialPictureRepository.findOne(id);
                    cloudinaryService.deleteFromCloud(materialPicture.getPicture(), materialPicture.getPictureName());
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.getMaterialPicture(), generalUtil.getPicsName("materialpic", products.getName()), "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPicture.setMaterialName(pp.getMaterialName());


                    materialPictureRepository.save(materialPicture);
                }else {
                    MaterialPicture materialPicture = new MaterialPicture();

                    CloudinaryResponse c = cloudinaryService.uploadToCloud(pp.getMaterialPicture(), generalUtil.getPicsName("materialpic", products.getName()), "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPicture.setProducts(products);
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
    public void updateProductVisibility(Long id, String status) {
        try {
            Products products = productRepository.findOne(id);
            products.setStatus(status);
            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateProductStatus(Long id, String status) {

        try {
            Date date = new Date();
            Products products = productRepository.findOne(id);
            products.setVerifiedFlag(status);
            products.setVerfiedOn(date);
            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void sponsorProduct(Long id, String status) {
        try {
            Products products = productRepository.findOne(id);
            products.setSponsoredFlag(status);
            productRepository.save(products);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            Products p = productRepository.findOne(id);
            productPictureRepository.findByProducts(p).forEach(pictures -> {
                cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
             //deletePics(pictures.pictureName,productPicturesFolder);
            });

            artWorkPictureRepository.findByProducts(p).forEach(pictures -> {
                cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
                //deletePics(pictures.pictureName,artworkPictureFolder);
            });

            materialPictureRepository.findByProducts(p).forEach(pictures -> {
                cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
                //deletePics(pictures.pictureName,artworkPictureFolder);
            });

            productRepository.delete(id);

        }catch (Exception e) {
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
    public List<ProductRespDTO> getProductsByDesigner() {
        try {
            Designer designer = designerRepository.findByUser(getCurrentUser());
            List<Products> products = productRepository.findByDesigner(designerRepository.findOne(designer.id));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products);
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
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
    public List<ProductRespDTO> getVerifiedProducts(PageableDetailsDTO pageableDetailsDTO) {

        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        try {
            Page<Products> products = productRepository.findByVerifiedFlag("Y", new PageRequest(page,size));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());

            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getUnVerifiedProducts(PageableDetailsDTO pageableDetailsDTO) {

        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        try {
            Page<Products> products = productRepository.findByVerifiedFlag("N",new PageRequest(page,size));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());

            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getNewProducts(PageableDetailsDTO pageableDetailsDTO) {
        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize();
        try {
            //Page<Products> products = productRepository.findByVerfiedOnIsNull(new PageRequest(page,size));

            Page<Products> products = productRepository.findByVerifiedFlagOrderByCreatedOnDesc(new PageRequest(page,size));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> filterProductsByPrice(FilterProductDTO filterProductDTO) {
        int page = filterProductDTO.getPage();
        int size = filterProductDTO.getSize();
        Double fromAmount = Double.parseDouble(filterProductDTO.getFromPrice());
        Double toAmount = Double.parseDouble(filterProductDTO.getToPrice());
        try {
            Page<Products> products = productRepository.findByVerifiedFlagAndDesignerStatusAndAmountBetween("Y","A",fromAmount,toAmount,new PageRequest(page,size));
            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }



    @Override
    public List<ProductRespDTO> filterProducts(FilterProductDTO filterProductDTO) {

        int page = filterProductDTO.getPage();
        int size = filterProductDTO.getSize();
        SubCategory subCategory = subCategoryRepository.findOne(filterProductDTO.getSubCategoryId());
        String name = filterProductDTO.getProductName();
        List<ProductRespDTO> productDTOS = null;
        List<Products> products;
        List<Long> ids = null;

        if(!name .equalsIgnoreCase("") ){
            ids = productRepository.findByVerifiedFlagAndDesignerStatusAndNameIsLike(name, subCategory);
        }

        if(filterProductDTO.getFromPrice() != null && !filterProductDTO.getFromPrice().equalsIgnoreCase("")){
            double fromAmount = Double.parseDouble(filterProductDTO.getFromPrice());
            double toAmount = Double.parseDouble(filterProductDTO.getToPrice());

            if(ids == null){
                ids = productRepository.filterProductByPrice(fromAmount, toAmount, subCategory);
            }else{
                List<Long> tempIds = new ArrayList<Long>();
                products = productRepository.findByIdIn(ids);
                for (Products p : products) {
                    if(p.getAmount() >= fromAmount && p.getAmount() <= toAmount){
                        tempIds.add(p.id);
                    }
                }
                ids = tempIds;
                productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
            }
        }

        if(filterProductDTO.getProductQualityRating() > 0){
            int prodQualityFilter = filterProductDTO.getProductQualityRating();

            if(ids == null){
                products = productRepository.findByVerifiedFlagAndDesignerStatusAndSubCategory("Y", "A", subCategory);
                productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
                List<ProductRespDTO> tempProRes = new ArrayList<ProductRespDTO>();
                for (ProductRespDTO p : productDTOS) {
                    if(p.productQualityRating >= prodQualityFilter){
                        tempProRes.add(p);
                    }
                }

                productDTOS = tempProRes;
            }else{
                products = productRepository.findByIdIn(ids);
                productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
                List<ProductRespDTO> tempProRes = new ArrayList<ProductRespDTO>();

                for (ProductRespDTO p : productDTOS) {
                    if(p.productQualityRating >= prodQualityFilter){
                        tempProRes.add(p);
                    }
                }

                productDTOS = tempProRes;
            }

        }

        if(productDTOS == null){
            productDTOS = generalUtil.convertProdEntToProdRespDTOs(productRepository.findByIdIn(ids));
        }

        List<ProductRespDTO> tempProd = getPage(productDTOS, page, size);
        if(tempProd == null){ tempProd = new ArrayList<ProductRespDTO>(); }

        return tempProd;
    }



    @Override
    public List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p) {

        int page = Integer.parseInt(p.page);
        int size = Integer.parseInt(p.size);
        Page<Products> products;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.subcategoryId);
                products = productRepository.findBySubCategoryAndVerifiedFlagAndDesigner_Status(new PageRequest(page, size), subCategory, "Y","A");

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }

//    @Override
//    public List<ProductRespDTO> searchProductsBySubCat(String search, ProdSubCategoryDTO p) {
//
//        int page = Integer.parseInt(p.page);
//        int size = Integer.parseInt(p.size);
//        Page<Products> products= null;
//        try {
//          SubCategory subCategory = subCategoryRepository.findBySubCategory(search);
//
//
//            products = productRepository.findBySubCategoryAndVerifiedFlag(new PageRequest(page, size), subCategory, "Y");
//
//            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
//            return productDTOS;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new WawoohException();
//        }
//    }

    @Override
    public List<ProductRespDTO> getTagProducts(PicTagDTO p) {

        List<Products> products= new ArrayList<>();

        List<Products> searchProducts= new ArrayList<>();
        try {
            PictureTag pictureTag = pictureTagRepository.findOne(p.id);

            if(pictureTag.getProducts() != null) {

                List<Products> prod = productRepository.findFirst9BySubCategoryAndSponsoredFlagAndVerifiedFlag(pictureTag.getSubCategory(),"Y", "Y");
                System.out.println(prod);

                System.out.println(searchProducts);
                if(prod.size() <9){
                    List<Products> prod2=productRepository.findFirst9BySubCategoryAndSponsoredFlagAndVerifiedFlag(pictureTag.getSubCategory(),"N","Y");
                    System.out.println(prod);
                    searchProducts.addAll(generalUtil.getRandomProducts(prod2,9-prod.size()));
                    System.out.println(searchProducts);
                }
                if(prod.size() >0){
                    searchProducts.addAll(generalUtil.getRandomProducts(prod,9));
                }
//                if(prod.size() < 1){
//                    prod=productRepository.findFirst9BySubCategoryAndVerifiedFlag(pictureTag.subCategory, "Y");
//                }

                if(searchProducts.size() > 0) {
                    //List<Products> randomProducts = generalUtil.getRandomProducts(prod,9);

                    for (Products pp : searchProducts) {
                        if(pp != pictureTag.getProducts())
                        products.add(pp);
                    }
                    System.out.println(products);
                }
                System.out.println(pictureTag.getProducts());
                if(pictureTag.getProducts().getVerifiedFlag().equalsIgnoreCase("Y")) {
                    products.add(pictureTag.getProducts());
                }

                    Collections.reverse(products);
            }
            else {
                List<Products> prod = productRepository.findFirst10BySubCategoryAndSponsoredFlagAndVerifiedFlag(pictureTag.getSubCategory(),"Y", "Y");
                if(prod.size() >0){
                    searchProducts.addAll(generalUtil.getRandomProducts(prod,10));

                }
                if(prod.size() <10){
                    List<Products> prod2=productRepository.findFirst10BySubCategoryAndSponsoredFlagAndVerifiedFlag(pictureTag.getSubCategory(),"N","Y");
                    System.out.println(prod2);
                    searchProducts.addAll(generalUtil.getRandomProducts(prod2,10-prod.size()));
                    System.out.println(searchProducts);
                }

                if(prod.size() == 10){
                    List<Products> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
                    products.addAll(randomProducts);
                    return generalUtil.convertProdEntToProdRespDTOs(products);
                }


                if(searchProducts.size() > 0) {
                   // prod=productRepository.findFirst10BySubCategoryAndVerifiedFlag(pictureTag.subCategory,"Y");
                    //searchProducts.addAll(prod);
                    //List<Products> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
                    products.addAll(searchProducts);
                }


//                if(searchProducts.size() > 0) {
//                    List<Products> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
//                    products.addAll(randomProducts);
//                }


            }
            return generalUtil.convertProdEntToProdRespDTOs(products);


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getTopProducts() {

        try {
            List<Object[]> products= productRatingRepository.findTop10Products();
            List<Products> products1 = new ArrayList<>();
            products.forEach(productsWithRating ->{
                Long longProducts= ((BigInteger) productsWithRating[0]).longValue();
                products1.add(productRepository.findOne(longProducts));
            });
            return generalUtil.convertProdEntToProdRespDTOs(generalUtil.getRandomProducts(products1,10));


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getFreqBoughtProducts() {
        try {
            List<Products> products= productRepository.findTop10ByDesignerStatusAndNumOfTimesOrderedNotOrderByNumOfTimesOrderedDesc("A",0);


            return generalUtil.convertProdEntToProdRespDTOs(products);


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getFeaturedProducts() {
        try {

            List<Products> products= new ArrayList<>();
            List<Products> prods = productRepository.findFirst5BySponsoredFlag("Y");
            products.addAll(prods);

            List<Products> prods1=productRepository.findFirst5ByPriceSlashEnabledTrue();

            products.addAll(prods1);

            products=generalUtil.getRandomProducts(products,10);
            return generalUtil.convertProdEntToProdRespDTOs(products);


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p) {
        User user = getCurrentUser();
        int page = Integer.parseInt(p.page);
        int size = Integer.parseInt(p.size);
        Page<Products> products;
        Designer designer = null;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.subcategoryId);
            if(p.designerId != null) {
                designer = designerRepository.findOne(p.designerId);
            }

            if(user != null && user.getRole().equalsIgnoreCase("designer")){
                designer=designerRepository.findByUser(user);
            }

            products = productRepository.findByDesignerAndSubCategoryAndVerifiedFlag(new PageRequest(page,size),designer,subCategory,"Y");

            return generalUtil.convertProdEntToProdRespDTOs(products.getContent());

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


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() != "anonymousUser") {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return jwtUser.getUser();
        }else {
            return null;
        }

    }

    @Override
    public int getTotalProducts() {
        int count = 0;
        try {
            User user = getCurrentUser();
                count= productRepository.countByDesigner(designerRepository.findByUser(user));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        return count;
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

        if(pictureTag.getProducts() != null){
            pictureTagDTO.productId = pictureTag.getProducts().id.toString();
            pictureTagDTO.productName = pictureTag.getProducts().getName();
        }

        return pictureTagDTO;

    }



    public List<ProductRespDTO> getPage(List<ProductRespDTO> productRespDTOS, int page, int size){

        int totalElements = productRespDTOS.size();
        int totalPages = (int) Math.ceil(productRespDTOS.size()/(double) size);

        if(page >= totalPages){
            return null;
        }else{

            int start = (page == 0) ? 0 : (page * size);
            int end = start + size;
            List<ProductRespDTO> newRespDto = productRespDTOS.subList(start, (end > totalElements) ? totalElements : end);
            return newRespDto;
        }
    }

    @Override
    public ProductAttributeDTO getProductAttributesById(Long id) {
        try {

           ProductAttribute productAttribute = productAttributeRepository.findOne(id);
            return generalUtil.convertProductAttributeEntityToDTO(productAttribute);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WawoohException();
        }
    }



    public boolean isUrl(String url){
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
