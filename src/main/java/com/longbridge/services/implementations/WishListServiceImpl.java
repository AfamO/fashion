package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.ProductNotificationRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.WishListRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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



    @Override
    public String addToWishList(WishListDTO wishListDTO) {
        try {
            User user = getCurrentUser();
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
    public String notifyMe(WishListDTO wishListDTO) {
        try {
            User user = getCurrentUser();
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
    public List<WishListDTO> getWishLists(PageableDetailsDTO pageable) {
        try{
            User user = getCurrentUser();
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
        productDTO.amount=products.getPrice().getAmount();
       // productDTO.color=products.color;
        productDTO.description=products.getProdDesc();
        productDTO.name=products.getName();
      //  productDTO.productSizes=products.productSizes;

        productDTO.productAttributeDTOS=generalUtil.convertProductAttributeEntitiesToDTOs(products.getProductStyle().getProductColorStyles());
        if(products.getProductStyle().getStyle() != null) {
            productDTO.styleId = products.getProductStyle().getStyle().id.toString();
        }

        productDTO.designerId=products.getDesigner().id.toString();
        productDTO.stockNo=products.getProductItem().getStockNo();
        productDTO.inStock=products.getProductItem().getInStock();
        productDTO.designerName=products.getDesigner().getStoreName();
        productDTO.status=products.getProductStatuses().getStatus();
        productDTO.verifiedFlag=products.getProductStatuses().getVerifiedFlag();
        productDTO.subCategoryId=products.getSubCategory().id.toString();
        productDTO.categoryId=products.getSubCategory().getCategory().id.toString();

        List<ProductPicture> productPictures = products.getProductStyle().getPicture();
        productDTO.picture=convertProdPictureEntitiesToDTO(productPictures);

        List<ArtWorkPicture> artWorkPictures = products.getProductStyle().getArtWorkPicture();
        productDTO.artWorkPicture=convertArtPictureEntitiesToDTO(artWorkPictures);

        List<MaterialPicture> materialPictures = products.getProductStyle().getMaterialPicture();
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
        pictureDTO.productId=picture.getProductColorStyles().getProducts().id;
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
        pictureDTO.productId=picture.getProductStyle().getProducts().id;
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
        pictureDTO.setId(picture.getId());
        pictureDTO.setProductId(picture.getProductStyle().getProducts().id);
        pictureDTO.setMaterialPicture(picture.getPictureName());
        return pictureDTO;

    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }


}
