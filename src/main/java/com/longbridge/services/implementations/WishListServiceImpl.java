package com.longbridge.services.implementations;

import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.WishListRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

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



    @Value("${artwork.picture.folder}")
    private String artworkPictureImagePath;

    @Value("${material.picture.folder}")
    private String materialPicturesImagePath;

    @Value("${product.picture.folder}")
    private String productPicturesImagePath;


    @Override
    public String addToWishList(WishListDTO wishListDTO, User user) {
        try {
            WishList wishList = new WishList();
            wishList.setProducts(productRepository.findOne(wishListDTO.getProductId()));
            wishList.setUser(user);
            wishListRepository.save(wishList);
            return wishListRepository.countByUser(user).toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

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
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.picture=picture.pictureName;
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
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.artWorkPicture=picture.pictureName;
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
        pictureDTO.id=picture.id;
        pictureDTO.productId=picture.products.id;
        pictureDTO.materialPicture=picture.pictureName;
        return pictureDTO;

    }



}
