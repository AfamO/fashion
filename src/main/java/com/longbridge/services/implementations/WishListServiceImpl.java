package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.ProductNotificationRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.WishListRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 19/01/2018.
 */
@Service
public class WishListServiceImpl implements WishListService{

    @Autowired
    ProductRepository productRepository;


    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    ProductNotificationRepository productNotificationRepository;

    @Autowired
    GeneralUtil generalUtil;



    @Value("${artwork.picture.folder}")
    private String artworkPictureImagePath;

    @Value("${material.picture.folder}")
    private String materialPicturesImagePath;

    @Value("${product.picture.folder}")
    private String productPicturesImagePath;


    @Override
    public String addToWishList(WishListDTO wishListDTO, User user) {
        try {

            WishList wishList1 = wishListRepository.findByUserAndProducts(user,productRepository.findOne(wishListDTO.getProductId()));
            if(wishList1 != null) {
                wishListRepository.delete(wishList1);
            }
            else {
                WishList wishList = new WishList();
                wishList.setProducts(productRepository.findOne(wishListDTO.getProductId()));
                wishList.setUser(user);
                wishListRepository.save(wishList);
            }
            return wishListRepository.countByUser(user).toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }


    @Override
    public String notifyMe(WishListDTO wishListDTO, User user) {
        try {

            ProductNotification productNotification = productNotificationRepository.findByEmailAndProductId(user.getEmail(), wishListDTO.getProductId());
            if (productNotification == null) {
                productNotification = new ProductNotification();
                productNotification.setEmail(user.getEmail());
                productNotification.setProductId(wishListDTO.getProductId());
                productNotificationRepository.save(productNotification);
            }
        }
            catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        return "success";
    }



    @Override
    public List<WishListDTO> getWishLists(User user, PageableDetailsDTO pageable) {
        try{

            return convertWishEntToDTOs(wishListRepository.findByUser(user,new PageRequest(pageable.getPage(),pageable.getSize())).getContent());

        }
       catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
       }
    }

    @Override
    public void deleteWishList(Long id) {
        try{

            wishListRepository.delete(id);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private List<WishListDTO> convertWishEntToDTOs(List<WishList> wishLists){

        List<WishListDTO> wishListDTOS = new ArrayList<WishListDTO>();

        for(WishList w: wishLists){
            WishListDTO wishListDTO = convertWishEntityToDTO(w);
            wishListDTOS.add(wishListDTO);
        }
        return wishListDTOS;
    }



    private WishListDTO convertWishEntityToDTO(WishList wishList){
        WishListDTO wishListDTO = new WishListDTO();
        wishListDTO.setId(wishList.id);
        wishListDTO.setProducts(convertEntityToDTO(wishList.getProducts()));
        return wishListDTO;

    }

    private ProductRespDTO convertEntityToDTO(Products products){
        ProductRespDTO productDTO = new ProductRespDTO();
        productDTO.id=products.id;
        productDTO.amount=products.getAmount();
       // productDTO.color=products.color;
        productDTO.description=products.getProdDesc();
        productDTO.name=products.getName();
      //  productDTO.productSizes=products.productSizes;

        productDTO.productAttributeDTOS=generalUtil.convertProductAttributeEntitiesToDTOs(products.getProductAttributes());
        if(products.getStyle() != null) {
            productDTO.styleId = products.getStyle().id.toString();
        }

        productDTO.designerId=products.getDesigner().id.toString();
        productDTO.stockNo=products.getStockNo();
        productDTO.inStock=products.getInStock();
        productDTO.designerName=products.getDesigner().getStoreName();
        productDTO.status=products.getStatus();
        productDTO.verifiedFlag=products.getVerifiedFlag();
        productDTO.subCategoryId=products.getSubCategory().id.toString();
        productDTO.categoryId=products.getSubCategory().getCategory().id.toString();

        List<ProductPicture> productPictures = products.getPicture();
        productDTO.picture=convertProdPictureEntitiesToDTO(productPictures);

        List<ArtWorkPicture> artWorkPictures = products.getArtWorkPicture();
        productDTO.artWorkPicture=convertArtPictureEntitiesToDTO(artWorkPictures);

        List<MaterialPicture> materialPictures = products.getMaterialPicture();
        productDTO.materialPicture=convertMatPictureEntitiesToDTO(materialPictures);

        return productDTO;

    }


    private List<ProductPictureDTO> convertProdPictureEntitiesToDTO(List<ProductPicture> productPictures){
        List<ProductPictureDTO> productPictureDTOS = new ArrayList<ProductPictureDTO>();
        for(ProductPicture p: productPictures){
            ProductPictureDTO pictureDTO = convertProdPictureEntityToDTO(p);
            productPictureDTOS.add(pictureDTO);
        }
        return productPictureDTOS;
    }


    private ProductPictureDTO convertProdPictureEntityToDTO(ProductPicture picture){
        ProductPictureDTO pictureDTO = new ProductPictureDTO();
        pictureDTO.id=picture.getId();
        pictureDTO.productId=picture.getProducts().id;
        pictureDTO.picture=picture.getPictureName();
        return pictureDTO;

    }

    private List<ArtPictureDTO> convertArtPictureEntitiesToDTO(List<ArtWorkPicture> artWorkPictures){
        List<ArtPictureDTO> artPictureDTOS = new ArrayList<ArtPictureDTO>();
        for(ArtWorkPicture p: artWorkPictures){
            ArtPictureDTO artPictureDTO = convertArtPictureEntityToDTO(p);
            artPictureDTOS.add(artPictureDTO);
        }
        return artPictureDTOS;
    }


    private ArtPictureDTO convertArtPictureEntityToDTO(ArtWorkPicture picture){
        ArtPictureDTO pictureDTO = new ArtPictureDTO();
        pictureDTO.id=picture.getId();
        pictureDTO.productId=picture.getProducts().id;
        pictureDTO.artWorkPicture=picture.getPictureName();
        return pictureDTO;

    }

    private List<MaterialPictureDTO> convertMatPictureEntitiesToDTO(List<MaterialPicture> materialPictures){
        List<MaterialPictureDTO> materialPictureDTOS = new ArrayList<MaterialPictureDTO>();
        for(MaterialPicture p: materialPictures){
            MaterialPictureDTO materialPictureDTO = convertMatPictureEntityToDTO(p);
            materialPictureDTOS.add(materialPictureDTO);
        }
        return materialPictureDTOS;
    }


    private MaterialPictureDTO convertMatPictureEntityToDTO(MaterialPicture picture){
        MaterialPictureDTO pictureDTO = new MaterialPictureDTO();
        pictureDTO.id=picture.getId();
        pictureDTO.productId=picture.getProducts().id;
        pictureDTO.materialPicture=picture.getPictureName();
        return pictureDTO;

    }



}
