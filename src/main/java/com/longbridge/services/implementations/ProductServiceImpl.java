package com.longbridge.services.implementations;

import com.google.gson.Gson;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SearchUtilities;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(this.getClass()); 
    
    @Value("${search.url}")
    private String elastic_host_api_url; //host_api_url for elastic search

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
    public void editSubCategory(SubCategoryDTO subCategoryDTO) {
        try {
            Date date = new Date();
                SubCategory subCategory = subCategoryRepository.findOne(subCategoryDTO.id);
                subCategory.setSubCategory(subCategoryDTO.name);
                subCategory.setProductType(subCategoryDTO.productType);
                subCategory.setCreatedOn(date);
                subCategory.setUpdatedOn(date);
                subCategoryRepository.save(subCategory);

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
    public String addProduct(ProductDTO productDTO) {
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
            products.getPrice().setAmount(productDTO.amount);
            productSearchDTO.setAmount(productDTO.amount);
            //productSearchDTO.setPicture(productDTO.picture);
            productSearchDTO.setAvailability(productDTO.inStock);
            products.getProductStatuses().setAvailability(productDTO.inStock);
            products.getProductStatuses().setAcceptCustomSizes(productDTO.acceptCustomSizes);
            productSearchDTO.setNumOfDaysToComplete(productDTO.numOfDaysToComplete);
            products.setNumOfDaysToComplete(productDTO.numOfDaysToComplete);
            productSearchDTO.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.getPrice().setMaterialPrice(productDTO.materialPrice);
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
            productSearchDTO.setDesignerName(designer.getStoreName());
            System.out.println("The Designer Name Is::"+designer.getStoreName());
            products.setDesigner(designer);
            productSearchDTO.setProductType(productDTO.productType);
            products.setProductType(productDTO.productType);

            if(productDTO.styleId != null && !productDTO.styleId.equalsIgnoreCase("null")) {
                if(!productDTO.styleId.isEmpty()) {
                    Long styleId = Long.parseLong(productDTO.styleId);
                    products.getProductStyle().setStyle(styleRepository.findOne(styleId));
                }
            }

            products.getProductItem().setStockNo(productDTO.stockNo);
            productSearchDTO.setStockNo(productDTO.stockNo);
            products.getProductItem().setInStock(productDTO.inStock);
            productSearchDTO.setInStock(productDTO.inStock);
            products.setCreatedOn(date);
            products.setUpdatedOn(date);
            productRepository.save(products);
            productSearchDTO.setId(products.id);
            List<ProductAttributeSearchDTO> productAttributesListSearchDTO =  new ArrayList<>();
            List<ProductPictureSearchDTO> productPicturseSearchDTOList =  new ArrayList<>();
            List<MaterialPictureSearchDTO> materialPictureSearchDTOList=new ArrayList<>();
            List<ProductSizes> productSizesSearchDTOList =  new ArrayList<>();
            for (ProductAttributeDTO pa: productDTO.productAttributes) {
                ProductColorStyles productAttribute=new ProductColorStyles();
                ProductAttributeSearchDTO productAttributeSearch=new ProductAttributeSearchDTO();
                productAttribute.setProducts(products);
                productAttributeSearch.setProductId(productDTO.id);
                String name = pa.getColourName().replace("&","");
                String colourName= generalUtil.getPicsName("prodcolour",name);
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
                    String  productPictureName= generalUtil.getPicsName("prodpic",products.getSubCategory().getSubCategory());
                    ProductPictureSearchDTO productPictureSearchDTO = new ProductPictureSearchDTO();
                    c = cloudinaryService.uploadToCloud(p,productPictureName,"productpictures");
                    productPicture.setPictureName(c.getUrl());
                    productPicture.setPicture(c.getPublicId());
                    productPictureSearchDTO.picture=c.getUrl();
                    productPicture.getProductColorStyles().setProducts(products);
                    productPictureSearchDTO.createdOn = date;
                    productPictureSearchDTO.updatedOn=date;
                    productPicture.createdOn = date;
                    productPicture.setUpdatedOn(date);
                    productPicture.setProductColorStyles(productAttribute);
                    productPicturseSearchDTOList.add(productPictureSearchDTO);
                    productPictureRepository.save(productPicture);
                    productPictureSearchDTO.setId(productPicture.getId());
                }
                productAttributeSearch.setProductPictureSearchDTOS(productPicturseSearchDTOList);
                productAttributesListSearchDTO.add(productAttributeSearch);
                productSearchDTO.setProductAttributeDTOS(productAttributesListSearchDTO) ;
            }

            if(productDTO.slashedPrice > 0){
                PriceSlash priceSlash = new PriceSlash();
                products.getPrice().setPriceSlashEnabled(true);
                priceSlash.getPrice().setProducts(products);
                priceSlash.setSlashedPrice(productDTO.slashedPrice);
                priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                productSearchDTO.setSlashedPrice(productDTO.slashedPrice);
                productSearchDTO.setPercentageDiscount((productDTO.slashedPrice/productDTO.amount)*100);
                priceSlashRepository.save(priceSlash);
            } else if(productDTO.percentageDiscount > 0){

                PriceSlash priceSlash=new PriceSlash();
                products.getPrice().setPriceSlashEnabled(true);
                priceSlash.getPrice().setProducts(products);
                priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getPrice().getAmount()));
                priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                productSearchDTO.setSlashedPrice((productDTO.percentageDiscount/100)*products.getPrice().getAmount());
                productSearchDTO.setPercentageDiscount(productDTO.percentageDiscount);
                priceSlashRepository.save(priceSlash);
            }



            if( productDTO.productType == 1) {
                for (MaterialPictureDTO mp : materialPics) {
                    MaterialPicture materialPicture = new MaterialPicture();
                    MaterialPictureSearchDTO  materialPictureSearchDTO  =new MaterialPictureSearchDTO ();
                    String matName = generalUtil.getPicsName("materialpic", products.getSubCategory().getSubCategory());
                    //materialPicture.pictureName = matName;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(mp.getMaterialPicture(), matName, "materialpictures");
                    materialPicture.setPictureName(c.getUrl());
                    materialPictureSearchDTO.setMaterialPicture(c.getUrl());
                    materialPicture.setPicture(c.getPublicId());
                    materialPictureSearchDTO.setMaterialName(mp.getMaterialName());
                    materialPicture.setMaterialName(mp.getMaterialName());
                    materialPicture.getProductStyle().setProducts(products);
                    materialPicture.createdOn = date;
                    materialPicture.setUpdatedOn(date);
                    materialPictureRepository.save(materialPicture);
                    materialPictureSearchDTO.setId(materialPicture.getId());
                    materialPictureSearchDTOList.add(materialPictureSearchDTO);
                }
                productSearchDTO.setMaterialPicture(materialPictureSearchDTOList);
                for (String ap : artWorkPics) {
                    ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                    String artName = generalUtil.getPicsName("artworkpic", products.getSubCategory().getSubCategory());
                    //artWorkPicture.pictureName = artName;
                    CloudinaryResponse c = cloudinaryService.uploadToCloud(ap, artName, "artworkpictures");
                    artWorkPicture.setPictureName(c.getUrl());
                    artWorkPicture.setPicture(c.getPublicId());
                    artWorkPicture.getProductStyle().setProducts(products);
                    artWorkPicture.createdOn = date;
                    artWorkPicture.setUpdatedOn(date);
                    artWorkPictureRepository.save(artWorkPicture);
                }
            }

            products.getProductItem().setStockNo(totalStock);
            productSearchDTO.setStockNo(totalStock);
            productSearchDTO.setVerifiedFlag("N");
            productRepository.save(products);
            Gson gson= new Gson();
            ApiResponse makeRemoteRequest = searchService.AddSearchProductIndex(elastic_host_api_url, productSearchDTO);      
            apiLogger.log("The Result Of Indexing A New Product For Elastic Search Is:"+gson.toJson(makeRemoteRequest));
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
            //Get the product from elastic search 'products' index
            ProductSearchDTO productSearchDTO=null;
            if(products!=null){
                productSearchDTO=searchService.convertIndexApiReponseToProductDTO(searchService.getProduct(elastic_host_api_url,productDTO.id));
            }
            products.setSubCategory(subCategoryRepository.findOne(subCategoryId));
            products.setName(productDTO.name);
            products.getPrice().setAmount(productDTO.amount);
            products.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            products.setProdDesc(productDTO.description);
            products.setProdSummary(productDTO.prodSummary);
            products.setDesigner(designer);
            productSearchDTO.setSubCategoryId(productDTO.subCategoryId);
            productSearchDTO.setName(productDTO.name);
            productSearchDTO.setAmount(productDTO.amount);
            productSearchDTO.setAvailability(productDTO.inStock);
            productSearchDTO.setNumOfDaysToComplete(productDTO.numOfDaysToComplete);
            productSearchDTO.setMandatoryMeasurements(productDTO.mandatoryMeasurements);
            productSearchDTO.setMaterialPrice(productDTO.materialPrice);
            productSearchDTO.setMaterialName(productDTO.materialName);
            productSearchDTO.setCategoryName(products.getSubCategory().getCategory().categoryName);
            productSearchDTO.setSubCategoryName(products.getSubCategory().getSubCategory());
            productSearchDTO.setProdSummary(productDTO.prodSummary);
            productSearchDTO.setDescription(productDTO.description);
            productSearchDTO.setDesignerId(Long.toString(designer.id));
            productSearchDTO.setDesignerStatus(designer.getStatus());
            productSearchDTO.setStatus(productDTO.status);
            productSearchDTO.setDesignerName(designer.getStoreName());

            productSearchDTO.setProductType(productDTO.productType);
            //
            if(!"null".equalsIgnoreCase(productDTO.styleId)) {
                if(productDTO.styleId!=null)
                {
                   if(!productDTO.styleId.isEmpty()) {
                    Long styleId = Long.parseLong(productDTO.styleId);
                    products.getProductStyle().setStyle(styleRepository.findOne(styleId));
                    productSearchDTO.setStyleId(productDTO.styleId);
                } 
                }
                
            }
            products.getProductItem().setStockNo(productDTO.stockNo);
            products.setUpdatedOn(date);
            productSearchDTO.setStockNo(productDTO.stockNo);

            if(productDTO.slashedPrice > 0){
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlash.setSlashedPrice(productDTO.slashedPrice);
                    priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                    productSearchDTO.setSlashedPrice(productDTO.slashedPrice);
                    productSearchDTO.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                }else {
                    priceSlash=new PriceSlash();
                    products.getPrice().setPriceSlashEnabled(true);
                    priceSlash.getPrice().setProducts(products);
                    priceSlash.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                    priceSlash.setSlashedPrice(productDTO.slashedPrice);
                    productSearchDTO.setSlashedPrice(productDTO.slashedPrice);
                    productSearchDTO.setPercentageDiscount(((productDTO.amount - productDTO.slashedPrice)/productDTO.amount)*100);
                }

                priceSlashRepository.save(priceSlash);
            }
            else if(productDTO.percentageDiscount > 0){
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getPrice().getAmount()));
                    priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                    productSearchDTO.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getPrice().getAmount()));
                    productSearchDTO.setPercentageDiscount(productDTO.percentageDiscount);
                }else {
                    priceSlash=new PriceSlash();
                    products.getPrice().setPriceSlashEnabled(true);
                    priceSlash.getPrice().setProducts(products);
                    priceSlash.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getPrice().getAmount()));
                    priceSlash.setPercentageDiscount(productDTO.percentageDiscount);
                    productSearchDTO.setSlashedPrice(productDTO.amount - ((productDTO.percentageDiscount/100)*products.getPrice().getAmount()));
                    productSearchDTO.setPercentageDiscount(productDTO.percentageDiscount);
                }

                priceSlashRepository.save(priceSlash);
            }else{
                products.getPrice().setPriceSlashEnabled(false);
                PriceSlash priceSlash =priceSlashRepository.findByProducts(products);
                if(priceSlash != null){
                    priceSlashRepository.delete(priceSlash);
                }
            }
            products.getProductStatuses().setVerifiedFlag("N");

            productRepository.save(products);
            //Then save the Updated product status
            //Update the search index to display verified products only
            Object saveEditedProduct=searchService.UpdateProductIndex(elastic_host_api_url, productSearchDTO);
            apiLogger.log("The Result Of ReIndexing A Verified/UnVerified Product For Elastic Search Is:"+SearchUtilities.convertObjectToJson(saveEditedProduct));


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
                List<ProductColorStyles> productAttributes=productAttributeRepository.findByProducts(products);
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
    public void updateProductVisibility(Long id, String status) {
        try {
            Products products = productRepository.findOne(id);
            products.getProductStatuses().setStatus(status);
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
            //get the product to update
            ProductSearchDTO productSearchDTO=null;
            Products products = productRepository.findOne(id);
            if(products!=null){
                productSearchDTO=searchService.convertIndexApiReponseToProductDTO(searchService.getProduct(elastic_host_api_url, id));
                productSearchDTO.setVerifiedFlag(status);
            }
            products.getProductStatuses().setVerifiedFlag(status);
            products.setVerfiedOn(date);
            productRepository.save(products);
            //Then save the Updated product status
            //Update the search index to display verified products only
            Object saveEditedProduct=searchService.UpdateProductIndex(elastic_host_api_url, productSearchDTO);
            apiLogger.log("The Result Of ReIndexing A Verified/UnVerified Product For Elastic Search Is:"+SearchUtilities.convertObjectToJson(saveEditedProduct));

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void unVerifyProduct(VerifyDTO verifyDTO) {

        try {
            Date date = new Date();
            Products products = productRepository.findOne(verifyDTO.getId());
            products.getProductStatuses().setVerifiedFlag(verifyDTO.getFlag());
            products.getProductStatuses().setUnVerifiedReason(verifyDTO.getUnverifyReason());
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
            products.getProductStatuses().setSponsoredFlag(status);
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
                    if(p.getPrice().getAmount() >= fromAmount && p.getPrice().getAmount() <= toAmount){
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

        int page = p.getPage();
        int size = p.getSize();
        Page<Products> products;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.getSubcategoryId());
                products = productRepository.findBySubCategoryAndVerifiedFlagAndDesigner_Status(new PageRequest(page, size), subCategory, "Y","A");

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getProductsByCatId(ProdSubCategoryDTO p) {
        Page<Products> products;
        try {
            Category category = categoryRepository.findOne(p.getCategoryId());
            products = productRepository.findBySubCategory_CategoryAndVerifiedFlagAndDesigner_Status(new PageRequest(p.getPage(), p.getSize()), category, "Y","A");

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
                if(pictureTag.getProducts().getProductStatuses().getVerifiedFlag().equalsIgnoreCase("Y")) {
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
    public List<ProductRespDTO> getFeaturedProducts(PageableDetailsDTO pageableDetailsDTO) {
        try {

            List<Products> products= new ArrayList<>();
            Page<Products> prods = productRepository.findBySponsoredFlag(new PageRequest(pageableDetailsDTO.getPage(),pageableDetailsDTO.getSize()),"Y");
            products.addAll(prods.getContent());

           // List<Products> prods1=productRepository.findFirst5ByPriceSlashEnabledTrue();

            //products.addAll(prods1);

            products=generalUtil.getRandomProducts(products,products.size());
            return generalUtil.convertProdEntToProdRespDTOs(products);


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<ProductRespDTO> getAllFeaturedProducts() {
        try {
            List<Products> products = new ArrayList<>();
            List<Products> prods = productRepository.findBySponsoredFlag("Y");

            products=generalUtil.getRandomProducts(prods,prods.size());
            return generalUtil.convertProdEntToProdRespDTOs(products);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p) {
        User user = getCurrentUser();
        int page = p.getPage();
        int size = p.getSize();
        Page<Products> products;
        Designer designer = null;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.getSubcategoryId());
            if(p.getDesignerId() != null) {
                designer = designerRepository.findOne(p.getDesignerId());
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

           ProductColorStyles productAttribute = productAttributeRepository.findOne(id);
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
