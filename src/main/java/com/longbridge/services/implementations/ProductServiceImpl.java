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
    BespokeProductRepository bespokeProductRepository;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ProductSizesRepository productSizesRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ProductStyleRepository productStyleRepository;

    @Autowired
    ProductStatusesRepository productStatusesRepository;

    @Autowired
    public ProductServiceImpl(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }


    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    DesignerRepository designerRepository;


    @Autowired
    StyleRepository styleRepository;

    @Autowired
    PictureTagRepository pictureTagRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    PriceSlashRepository priceSlashRepository;

    @Autowired
    ProductColorStyleRepository productColorStyleRepository;

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
            Product product = productRepository.findOne(id);
            ProductRespDTO productDTO = generalUtil.convertEntityToDTO(product);
            Designer designer = designerRepository.findByUser(getCurrentUser());
            int salesInQueue = itemRepository.findActiveOrdersOnProduct(designer.id, product.id,itemStatuses);
            int totalSales = itemRepository.countByDesignerIdAndProductIdAndItemStatus_Status(designer.id, product.id,"D");
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
            Product product = productRepository.findOne(id);
            ProductRespDTO productDTO;
            if(reviewsPresent)
                productDTO = generalUtil.convertEntityToDTOWithReviews(product);
            else
                productDTO = generalUtil.convertEntityToDTO(product);
            if(user != null){
                if(wishListRepository.findByUserAndProduct(user, product) != null){
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
            if(productDTO.productPriceDTO.getAmount() < 0 || productDTO.productPriceDTO.getSlashedPrice() < 0 || productDTO.productPriceDTO.getPercentageDiscount() <0){
                throw new InvalidAmountException();
            }
            User user = getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            Date date = new Date();
            Product product = saveProduct(productDTO, designer, date);
            ProductStyle productStyle = saveProductStyle(productDTO.styleId, product,productDTO);
            saveProductColorStyle(productDTO.productColorStyleDTOS, productDTO,product.getSubCategory().getSubCategory(), productStyle);
            savePriceDetails(productDTO.productPriceDTO,product);
            ProductStatuses productStatuses = new ProductStatuses();
            if(productDTO.bespokeProductDTO != null){
                productStatuses.setAcceptCustomSizes("Y");
            }else {
                productStatuses.setAcceptCustomSizes("N");
                productStatuses.setAvailability("Y");
            }
            productRepository.save(product);
            productStatuses.setProduct(product);
            productStatusesRepository.save(productStatuses);

            return "true";


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    private Product saveProduct(ProductDTO productDTO, Designer designer, Date date) {
        Product product = new Product();
        Long subCategoryId = Long.parseLong(productDTO.subCategoryId);
        product.setSubCategory( subCategoryRepository.findOne(subCategoryId));
        product.setName(productDTO.name);
        product.setProdSummary(productDTO.prodSummary);
        product.setProdDesc(productDTO.description);
        product.setDesigner(designer);
        product.setProductType(productDTO.productType);
        product.setCreatedOn(date);
        product.setUpdatedOn(date);
        productRepository.save(product);
        return product;
    }


    private ProductStyle saveProductStyle(Long styleId, Product product, ProductDTO productDTO) {
        ProductStyle productStyle = new ProductStyle();
        if(styleId != null ) {
                productStyle.setStyle(styleRepository.findOne(styleId));
        }
        productStyle.setProduct(product);
        productStyleRepository.save(productStyle);
        if(productDTO.bespokeProductDTO != null) {
            saveBespokeProduct(productDTO.productType, productDTO.bespokeProductDTO, new Date(),productStyle,product.getSubCategory().getSubCategory());

        }
        return productStyle;
    }

    private void saveBespokeProduct(int productType, BespokeProductDTO bespokeProductDTO, Date date,ProductStyle productStyle, String subCategory) {
        if (productType == 1) {
            BespokeProduct bespokeProduct=new BespokeProduct();
            bespokeProduct.setProduct(productStyle.getProduct());
            bespokeProduct.setNumOfDaysToComplete(bespokeProductDTO.getNumOfDaysToComplete());
            bespokeProduct.setMandatoryMeasurements(bespokeProductDTO.getMandatoryMeasurements());
            bespokeProduct.setProductStyle(productStyle);
            bespokeProductRepository.save(bespokeProduct);
            for (MaterialPictureDTO mp : bespokeProductDTO.getMaterialPicture()) {
                MaterialPicture materialPicture = new MaterialPicture();
                String matName = generalUtil.getPicsName("materialpic", subCategory);
                CloudinaryResponse c = cloudinaryService.uploadToCloud(mp.getMaterialPicture(), matName, "materialpictures");
                materialPicture.setPictureName(c.getUrl());
                materialPicture.setPicture(c.getPublicId());
                materialPicture.setMaterialName(mp.getMaterialName());
                materialPicture.setPrice(mp.getMaterialPrice());
                materialPicture.setBespokeProduct(bespokeProduct);
                materialPicture.createdOn = date;
                materialPicture.setUpdatedOn(date);
                materialPictureRepository.save(materialPicture);
            }
            for (ArtWorkPicture ap : bespokeProductDTO.getArtWorkPicture()) {
                ArtWorkPicture artWorkPicture = new ArtWorkPicture();
                String artName = generalUtil.getPicsName("artworkpic", subCategory);
                CloudinaryResponse c = cloudinaryService.uploadToCloud(ap.getPicture(), artName, "artworkpictures");
                artWorkPicture.setPictureName(c.getUrl());
                artWorkPicture.setPicture(c.getPublicId());
                artWorkPicture.setBespokeProduct(bespokeProduct);
                artWorkPicture.createdOn = date;
                artWorkPicture.setUpdatedOn(date);
                artWorkPictureRepository.save(artWorkPicture);
            }
        }
    }

    private void savePriceDetails(ProductPriceDTO productPriceDTO, Product product) {
        ProductPrice productPrice = new ProductPrice();
        productPrice.setAmount(productPriceDTO.getAmount());
        productPrice.setProduct(product);
        productPrice.setSewingAmount(productPriceDTO.getSewingAmount());
        PriceSlash priceSlash = null;
        if(productPriceDTO.getSlashedPrice() > 0){
            priceSlash = new PriceSlash();
            productPrice.setPriceSlashEnabled(true);
            priceSlash.setSlashedPrice(productPriceDTO.getSlashedPrice());
            priceSlash.setPercentageDiscount(((productPriceDTO.getAmount() - productPriceDTO.getSlashedPrice())/productPriceDTO.getAmount())*100);
            priceSlashRepository.save(priceSlash);
        } else if(productPriceDTO.getPercentageDiscount() > 0){
            priceSlash=new PriceSlash();
            productPrice.setPriceSlashEnabled(true);
            productPrice.setProduct(product);
            priceSlash.setSlashedPrice(productPriceDTO.getAmount() - ((productPriceDTO.getPercentageDiscount()/100)* productPrice.getAmount()));
            priceSlash.setPercentageDiscount(productPriceDTO.getPercentageDiscount());
            priceSlashRepository.save(priceSlash);
        }
        productPrice.setPriceSlash(priceSlash);
        productPriceRepository.save(productPrice);
    }

    private void saveProductColorStyle(List<ProductColorStyleDTO> productColorStyleDTOS,ProductDTO productDTO, String subCategory, ProductStyle productStyle) {
        Date date = new Date();
        for (ProductColorStyleDTO pa: productColorStyleDTOS) {
            ProductColorStyle productColorStyle=new ProductColorStyle();
           // productColorStyle.setProduct(product);
            String name = pa.getColourName().replace("&","");
            String colourName= generalUtil.getPicsName("prodcolour",name);
            CloudinaryResponse c = cloudinaryService.uploadToCloud(pa.getColourPicture(),colourName,"materialpictures");
            productColorStyle.setColourName(pa.getColourName());
            productColorStyle.setColourPicture(c.getUrl());
            productColorStyle.setProductStyle(productStyle);
            productColorStyleRepository.save(productColorStyle);

            for (ProductSizes p: pa.getProductSizes()){
                ProductSizes productSizes = new ProductSizes();
                productSizes.setName(p.getName());
                productSizes.setNumberInStock(p.getNumberInStock());
                productSizes.setProductColorStyle(productColorStyle);
                productSizes.setProductColorStyle(productColorStyle);
                productSizesRepository.save(productSizes);
            }
                saveProductPicture(pa.getPicture(),date,subCategory,productColorStyle);


        }
    }

    private void saveProductPicture(List<String> pictures, Date date, String subCategory, ProductColorStyle productColorStyle) {
        for(String p:pictures){
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
    public void updateProduct(ProductDTO productDTO) {
        try {
            User user = getCurrentUser();
            Date date = new Date();
            Designer designer = designerRepository.findByUser(user);
            Long subCategoryId = Long.parseLong(productDTO.subCategoryId);
            Product product = productRepository.findOne(productDTO.id);
            product.setSubCategory(subCategoryRepository.findOne(subCategoryId));
            product.setName(productDTO.name);
            product.getProductPrice().setAmount(productDTO.productPriceDTO.getAmount());
            product.setProdDesc(productDTO.description);
            product.setProdSummary(productDTO.prodSummary);
            product.setDesigner(designer);
                if(productDTO.styleId!=null)
                {
                    product.getProductStyle().setStyle(styleRepository.findOne(productDTO.styleId));
                }

            product.setUpdatedOn(date);
            product.getProductStatuses().setVerifiedFlag("N");
            productRepository.save(product);
        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    //todo come back later
    @Override
    public void updateProductStock(ProductDTO productDTO) {
        try {
            Product product = productRepository.findOne(productDTO.id);


            if(productDTO.productColorStyleDTOS != null){
                List<ProductSizes> productSizes=productSizesRepository.findByProductColorStyle_ProductStyle_Product(product);
                productSizesRepository.delete(productSizes);
                for (ProductColorStyleDTO p: productDTO.productColorStyleDTOS) {
//                    p.setProduct(product);
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
            Product product = productRepository.findOne(id);
            product.getProductStatuses().setStatus(status);
            productRepository.save(product);
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
            Product product = productRepository.findOne(id);
            if(product !=null){
                productSearchDTO=searchService.convertIndexApiReponseToProductDTO(searchService.getProduct(elastic_host_api_url, id));
                productSearchDTO.setVerifiedFlag(status);
            }
            product.getProductStatuses().setVerifiedFlag(status);
            product.setVerfiedOn(date);
            productRepository.save(product);
            //Then save the Updated product status
            //Update the search index to display verified product only
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
            Product product = productRepository.findOne(verifyDTO.getId());
            product.getProductStatuses().setVerifiedFlag(verifyDTO.getFlag());
            product.getProductStatuses().setUnVerifiedReason(verifyDTO.getUnverifyReason());
            product.setVerfiedOn(date);
            productRepository.save(product);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void sponsorProduct(Long id, String status) {
        try {
            Product product = productRepository.findOne(id);
            product.getProductStatuses().setSponsoredFlag(status);
            productRepository.save(product);

        }catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            Product p = productRepository.findOne(id);
            //BespokeProduct bespokeProduct = p.getProductStyle().getBespokeProduct();
            productPictureRepository.findByProductColorStyle_Product(p).forEach(pictures -> {
                cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
             //deletePics(pictures.pictureName,productPicturesFolder);
            });

//            if(bespokeProduct != null){
//                artWorkPictureRepository.findByBespokeProduct_Product(p).forEach(pictures -> {
//                    cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
//                    //deletePics(pictures.pictureName,artworkPictureFolder);
//                });
//
//
//            materialPictureRepository.findByBespokeProduct_Product(p).forEach(pictures -> {
//                cloudinaryService.deleteFromCloud(pictures.getPicture(),pictures.getPictureName());
//                //deletePics(pictures.pictureName,artworkPictureFolder);
//            });
//            }

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
            List<Product> products = productRepository.findByDesigner(designerRepository.findOne(designer.id));
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

            List<Product> products = productRepository.findByDesigner(designerRepository.findOne(designerId));
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
            Page<Product> products = productRepository.findAll(new PageRequest(page,size));
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
            Page<Product> products = productRepository.findByProductStatuses_VerifiedFlag("Y", new PageRequest(page,size));
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
            Page<Product> products = productRepository.findByProductStatuses_VerifiedFlag("N",new PageRequest(page,size));
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
            //Page<Product> products = productRepository.findByVerfiedOnIsNull(new PageRequest(page,size));

            Page<Product> products = productRepository.findByProductStatuses_VerifiedFlagOrderByCreatedOnDesc(new PageRequest(page,size));
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
            Page<Product> products = productRepository.findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndProductPrice_AmountBetween("Y","A",fromAmount,toAmount,new PageRequest(page,size));
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
        List<Product> products=null;


        //List<Long> ids = null;

        if(!name .equalsIgnoreCase("") ){
            products = productRepository.findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndNameLike("Y","Y",name);
        }

        if(filterProductDTO.getFromPrice() != null && !filterProductDTO.getFromPrice().equalsIgnoreCase("")){
            double fromAmount = Double.parseDouble(filterProductDTO.getFromPrice());
            double toAmount = Double.parseDouble(filterProductDTO.getToPrice());

            if(products == null){
                products = productRepository.findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndProductPrice_AmountBetweenAndSubCategory("Y","Y",fromAmount, toAmount, subCategory);
            }else{
                List<Product> tempProducts=null;
//                products = productRepository.findByIdIn(ids);
                for (Product p : products) {
                    if(p.getProductPrice().getAmount() >= fromAmount && p.getProductPrice().getAmount() <= toAmount){
                        tempProducts.add(p);
                    }
                }
                products = tempProducts;
                productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
            }
        }

        if(filterProductDTO.getProductQualityRating() > 0){
            int prodQualityFilter = filterProductDTO.getProductQualityRating();

            if(products == null){
                products = productRepository.findByProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatusAndSubCategory("Y", "A", subCategory);
                productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
                List<ProductRespDTO> tempProRes = new ArrayList<ProductRespDTO>();
                for (ProductRespDTO p : productDTOS) {
                    if(p.productQualityRating >= prodQualityFilter){
                        tempProRes.add(p);
                    }
                }

                productDTOS = tempProRes;
            }else{

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
            productDTOS = generalUtil.convertProdEntToProdRespDTOs(products);
        }

        List<ProductRespDTO> tempProd = getPage(productDTOS, page, size);
        if(tempProd == null){ tempProd = new ArrayList<ProductRespDTO>(); }

        return tempProd;
    }



    @Override
    public List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p) {

        int page = p.getPage();
        int size = p.getSize();
        Page<Product> products;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.getSubcategoryId());
                products = productRepository.findBySubCategoryAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(new PageRequest(page, size), subCategory, "Y","A");

            List<ProductRespDTO> productDTOS=generalUtil.convertProdEntToProdRespDTOs(products.getContent());
            return productDTOS;

        } catch (Exception e) {
            e.printStackTrace();
           throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getProductsByCatId(ProdSubCategoryDTO p) {
        Page<Product> products;
        try {
            Category category = categoryRepository.findOne(p.getCategoryId());
            products = productRepository.findBySubCategory_CategoryAndProductStatuses_VerifiedFlagAndProductStatuses_DesignerStatus(new PageRequest(p.getPage(), p.getSize()), category, "Y","A");

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
//        Page<Product> products= null;
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

        List<Product> products= new ArrayList<>();

        List<Product> searchProducts= new ArrayList<>();
        try {
            PictureTag pictureTag = pictureTagRepository.findOne(p.id);

            if(pictureTag.getProduct() != null) {

                List<Product> prod = productRepository.findFirst9BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(pictureTag.getSubCategory(),"Y", "Y");
                System.out.println(prod);

                System.out.println(searchProducts);
                if(prod.size() <9){
                    List<Product> prod2=productRepository.findFirst9BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(pictureTag.getSubCategory(),"N","Y");
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
                    //List<Product> randomProducts = generalUtil.getRandomProducts(prod,9);

                    for (Product pp : searchProducts) {
                        if(pp != pictureTag.getProduct())
                        products.add(pp);
                    }
                    System.out.println(products);
                }
                System.out.println(pictureTag.getProduct());
                if(pictureTag.getProduct().getProductStatuses().getVerifiedFlag().equalsIgnoreCase("Y")) {
                    products.add(pictureTag.getProduct());
                }

                    Collections.reverse(products);
            }
            else {
                List<Product> prod = productRepository.findFirst10BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(pictureTag.getSubCategory(),"Y", "Y");
                if(prod.size() >0){
                    searchProducts.addAll(generalUtil.getRandomProducts(prod,10));

                }
                if(prod.size() <10){
                    List<Product> prod2=productRepository.findFirst10BySubCategoryAndProductStatuses_SponsoredFlagAndProductStatuses_VerifiedFlag(pictureTag.getSubCategory(),"N","Y");
                    System.out.println(prod2);
                    searchProducts.addAll(generalUtil.getRandomProducts(prod2,10-prod.size()));
                    System.out.println(searchProducts);
                }

                if(prod.size() == 10){
                    List<Product> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
                    products.addAll(randomProducts);
                    return generalUtil.convertProdEntToProdRespDTOs(products);
                }


                if(searchProducts.size() > 0) {
                   // prod=productRepository.findFirst10BySubCategoryAndVerifiedFlag(pictureTag.subCategory,"Y");
                    //searchProducts.addAll(prod);
                    //List<Product> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
                    products.addAll(searchProducts);
                }


//                if(searchProducts.size() > 0) {
//                    List<Product> randomProducts = generalUtil.getRandomProducts(searchProducts,10);
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
            List<Product> product1 = new ArrayList<>();
            products.forEach(productsWithRating ->{
                Long longProducts= ((BigInteger) productsWithRating[0]).longValue();
                product1.add(productRepository.findOne(longProducts));
            });
            return generalUtil.convertProdEntToProdRespDTOs(generalUtil.getRandomProducts(product1,10));


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getFreqBoughtProducts() {
        try {
            List<Product> products= productRepository.findTop10ByProductStatuses_DesignerStatusAndNumOfTimesOrderedNotOrderByNumOfTimesOrderedDesc("A",0);


            return generalUtil.convertProdEntToProdRespDTOs(products);


        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ProductRespDTO> getFeaturedProducts(PageableDetailsDTO pageableDetailsDTO) {
        try {

            List<Product> products= new ArrayList<>();
            Page<Product> prods = productRepository.findByProductStatuses_SponsoredFlag(new PageRequest(pageableDetailsDTO.getPage(),pageableDetailsDTO.getSize()),"Y");
            products.addAll(prods.getContent());

           // List<Product> prods1=productRepository.findFirst5ByPriceSlashEnabledTrue();

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
            List<Product> products = new ArrayList<>();
            List<Product> prods = productRepository.findByProductStatuses_SponsoredFlag("Y");

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
        Page<Product> products;
        Designer designer = null;
        try {
            SubCategory subCategory = subCategoryRepository.findOne(p.getSubcategoryId());
            if(p.getDesignerId() != null) {
                designer = designerRepository.findOne(p.getDesignerId());
            }

            if(user != null && user.getRole().equalsIgnoreCase("designer")){
                designer=designerRepository.findByUser(user);
            }

            products = productRepository.findByDesignerAndSubCategoryAndProductStatuses_VerifiedFlag(new PageRequest(page,size),designer,subCategory,"Y");

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

        if(pictureTag.getProduct() != null){
            pictureTagDTO.productId = pictureTag.getProduct().id.toString();
            pictureTagDTO.productName = pictureTag.getProduct().getName();
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
    public ProductColorStyleDTO getProductAttributesById(Long id) {
        try {

           ProductColorStyle productAttribute = productColorStyleRepository.findOne(id);
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
