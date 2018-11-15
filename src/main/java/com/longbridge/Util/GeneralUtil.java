package com.longbridge.Util;

import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Longbridge on 24/01/2018.
 */
@Service
public class GeneralUtil {

    @Autowired
    EventPictureRepository eventPictureRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    PriceSlashRepository priceSlashRepository;


    @Autowired
    OrderItemProcessingPictureRepository orderItemProcessingPictureRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ArtWorkPictureRepository artWorkPictureRepository;

    @Autowired
    MaterialPictureRepository materialPictureRepository;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    DesignerRepository designerRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    BespokeProductRepository bespokeProductRepository;

    @Autowired
    ProductColorStyleRepository productColorStyleRepository;

    @Autowired
    ProductSizesRepository productSizesRepository;

    @Autowired
    AnonymousUserRepository anonymousUserRepository;

    @Autowired
    PromoCodeRepository promoCodeRepository;

    @Autowired
    PromoItemsRepository promoItemsRepository;

    public DesignerDTO convertDesigner2EntToDTO(Designer d){
        DesignerDTO dto = new DesignerDTO();
        dto.id=d.id;
        dto.userId = d.getUser().id;
        dto.logo=d.getLogo();
        dto.storeName=d.getStoreName();
        dto.setStoreId(d.getStoreId());
        User u = d.getUser();
        dto.firstName=u.getFirstName();
        dto.lastName=u.getLastName();
        dto.phoneNo=u.getPhoneNo();
        dto.email=u.getEmail();
        dto.emailVerificationFlag = u.getEmailVerificationFlag();
        dto.gender=u.getGender();
        dto.registrationProgress = d.getRegistrationProgress();

        dto.accountNumber = d.getAccountNumber();
        Designer designer = designerRepository.findByUser(u);
        dto.accountName = designer.getAccountName();
        dto.bankName = designer.getBankName();
        dto.swiftCode = designer.getSwiftCode();
        dto.countryCode = designer.getCountryCode();
        dto.currency = designer.getCurrency();

        dto.address = d.getAddress();
        dto.localGovt = d.getLocalGovt();
        dto.city = d.getCity();
        dto.state = d.getState();
        dto.country = d.getCountry();

        dto.registeredFlag = d.getRegisteredFlag();
        dto.registrationNumber = d.getRegistrationNumber();
        dto.registrationDocument = d.getRegistrationDocument();

        dto.sizeGuideFlag = d.getSizeGuideFlag();
        if(d.getSizeGuide() != null){
            dto.maleSizeGuide = d.getSizeGuide().getMaleSizeGuide();
            dto.femaleSizeGuide = d.getSizeGuide().getFemaleSizeGuide();
        }

        dto.threshold=designer.getThreshold();
        dto.setStatus(d.getStatus());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.createdDate = formatter.format(d.createdOn);
        List<ProductRespDTO> products= convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndProductStatuses_VerifiedFlag(d,"Y"));
        dto.setProducts(products);

        List<String> stats = new ArrayList<>();
        //stats.add("OP");
        stats.add("P");

        List<ItemStatus> statuses = itemStatusRepository.findByStatusIn(stats);
        dto.noOfPendingOders= itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"P");
        dto.noOfDeliveredOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"D");
        dto.noOfCancelledOrders=itemRepository.countByDesignerIdAndItemStatus_StatusIn(d.id, Arrays.asList("C","OR"));
        dto.noOfConfirmedOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"A");
        dto.noOfReadyToShipOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"RS");
        dto.noOfShippedOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"OS");
        Double amountOfPendingOrders = itemRepository.findSumOfPendingOrders(d.id,statuses);
        Double amountOfTotalOrders = itemRepository.findSumOfOrders(d.id,Arrays.asList(itemStatusRepository.findByStatus("NV")));
        dto.setTotalOrders(itemRepository.countByDesignerId(d.id));

        if(amountOfTotalOrders != null){
            dto.amountOfOrders = amountOfTotalOrders;
        }else{
            dto.amountOfOrders = 0;
        }

        if(amountOfPendingOrders != null){
            dto.amountOfPendingOrders = amountOfPendingOrders;
        }else{
            dto.amountOfPendingOrders = 0;
        }
        //dto.amountOfPendingOrders=itemRepository.findSumOfPendingOrders(d.id,"OP");
        // dto.setSalesChart(getSalesChart(d.id));
        return dto;

    }

    public DesignerDTO convertDesignerEntToDTO(Designer d){
        DesignerDTO dto = new DesignerDTO();
        dto.id=d.id;
        dto.userId = d.getUser().id;
        dto.logo=d.getLogo();
        dto.storeName=d.getStoreName();
        dto.setStoreId(d.getStoreId());
        dto.address=d.getAddress();
        User u = d.getUser();
        dto.firstName=u.getFirstName();
        dto.lastName=u.getLastName();
        dto.phoneNo=u.getPhoneNo();
        dto.email=u.getEmail();
        dto.gender=u.getGender();
        dto.accountName=d.getAccountName();
        dto.accountNumber=d.getAccountNumber();
        dto.swiftCode=d.getSwiftCode();
        dto.bankName=d.getBankName();
        dto.setStatus(d.getStatus());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.createdDate = formatter.format(d.createdOn);
        if(d.getStatus().equalsIgnoreCase("A")) {
            List<ProductRespDTO> products = convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndProductStatuses_VerifiedFlag(d, "Y"));
            dto.setProducts(products);
        }
        return dto;

    }

    public List<DesignerDTO> convDesignerEntToDTOs(List<Designer> designers){
        List<DesignerDTO> designerDTOS = new ArrayList<DesignerDTO>();

        for(Designer designer: designers){
            DesignerDTO designerDTO = convertDesignerEntToDTO(designer);
            designerDTOS.add(designerDTO);
        }
        return designerDTOS;
    }

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
        pictureDTO.id=picture.getId();
        pictureDTO.productId=picture.getProductColorStyle().getProductStyle().getProduct().id;
        pictureDTO.picture=picture.getPictureName();
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
        pictureDTO.id=picture.getId();
        pictureDTO.productId=picture.getBespokeProduct().getProduct().id;
        pictureDTO.artWorkPicture=picture.getPictureName();

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
        pictureDTO.setId(picture.getId());
        pictureDTO.setProductId(picture.getBespokeProduct().getProduct().id);
        pictureDTO.setMaterialPicture(picture.getPictureName());
        return pictureDTO;

    }



    public List<ProductRespDTO> convertProdEntToProdRespDTOs(List<Product> products, User user){

        List<ProductRespDTO> productDTOS = new ArrayList<ProductRespDTO>();

        for(Product p: products){
            ProductRespDTO productDTO = convertEntityToDTO(p);
            if(user != null){
                if(wishListRepository.findByUserAndProduct(user,p) != null){
                 productDTO.wishListFlag = "Y";
                }
                else {
                    productDTO.wishListFlag = "N";
                }
            }

            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    public List<ProductRespDTO> convertProdEntToProdRespDTOs(List<Product> products){


        List<ProductRespDTO> totalProductDTOS = new ArrayList<ProductRespDTO>();
        List<ProductRespDTO> verifiedProductDTOS = new ArrayList<ProductRespDTO>();
        List<ProductRespDTO> unVerfiedproductDTOS = new ArrayList<ProductRespDTO>();
        for(Product p: products){
            if(p.getProductStatuses().getVerifiedFlag().equalsIgnoreCase("Y")){
                ProductRespDTO productDTO = convertEntityToDTO(p);
                verifiedProductDTOS.add(productDTO);
            }
            else {
                ProductRespDTO productDTO = convertEntityToDTO(p);
                unVerfiedproductDTOS.add(productDTO);
            }
        }
        totalProductDTOS.addAll(verifiedProductDTOS);
        totalProductDTOS.addAll(unVerfiedproductDTOS);
        return totalProductDTOS;
    }

    public ProductRespDTO convertEntityToDTO(Product product){
        ProductRespDTO productDTO = new ProductRespDTO();
        DecimalFormat df = new DecimalFormat("#.00");
        productDTO.id= product.id;
        productDTO.description= product.getProdDesc();
        productDTO.prodSummary= product.getProdSummary();
        productDTO.name= product.getName();
        productDTO.sku = product.getSku();

        productDTO.productColorStyleDTOS =convertProductAttributeEntitiesToDTOs(product.getProductStyle().getProductColorStyles());

      //  productDTO.productSizes=product.productSizes;
        if(product.getProductStyle().getStyle() != null) {
            productDTO.styleId = product.getProductStyle().getStyle().id.toString();
        }
        productDTO.designerId= product.getDesigner().id.toString();
        productDTO.designerStatus= product.getDesigner().getStatus();
//        productDTO.stockNo= product.getProductItem().getStockNo();
//        productDTO.inStock= product.getProductItem().getInStock();
        productDTO.availability= product.getProductStatuses().getAvailability();
        productDTO.acceptCustomSizes= product.getProductStatuses().getAcceptCustomSizes();
        productDTO.designerName= product.getDesigner().getStoreName();
        productDTO.status= product.getProductStatuses().getStatus();
        productDTO.sponsoredFlag= product.getProductStatuses().getSponsoredFlag();
        productDTO.unVerifiedReason= product.getProductStatuses().getUnVerifiedReason();
        productDTO.verifiedFlag= product.getProductStatuses().getVerifiedFlag();
        productDTO.subCategoryId= product.getSubCategory().id.toString();
        productDTO.subCategoryName= product.getSubCategory().getSubCategory();
        productDTO.categoryId= product.getSubCategory().getCategory().id.toString();
        productDTO.categoryName= product.getSubCategory().getCategory().categoryName;
        productDTO.numOfTimesOrdered = product.getNumOfTimesOrdered();

        productDTO.amount=product.getProductPrice().getAmount();
        if(product.getProductPrice().getPriceSlash() != null){
            productDTO.slashedPrice = product.getProductPrice().getPriceSlash().getSlashedPrice();
            productDTO.percentageDiscount = Double.parseDouble(df.format(product.getProductPrice().getPriceSlash().getPercentageDiscount()));
        }

        if(product.getProductStyle().getBespokeProduct() != null) {
            BespokeProductDTO bespokeProductDTO = new BespokeProductDTO();
            if(product.getProductType() == 1) {
                bespokeProductDTO.setArtPictureDTOS(convertArtPictureEntitiesToDTO(product.getProductStyle().getBespokeProduct().getArtWorkPicture()));
                bespokeProductDTO.setMaterialPicture(convertMatPictureEntitiesToDTO(product.getProductStyle().getBespokeProduct().getMaterialPicture()));
            }
            else{
                bespokeProductDTO.setArtPictureDTOS(null);
                bespokeProductDTO.setMaterialPicture(null);
            }
                 bespokeProductDTO.setId(product.getProductStyle().getBespokeProduct().id);
                bespokeProductDTO.setNumOfDaysToComplete(product.getProductStyle().getBespokeProduct().getNumOfDaysToComplete());
                bespokeProductDTO.setMandatoryMeasurements(product.getProductStyle().getBespokeProduct().getMandatoryMeasurements());

            productDTO.bespokeProductDTO=bespokeProductDTO;
        }

        int sum = 0;
        int deliverySum = 0;
        int serviceSum = 0;

        int noOfUsers = product.getReviews().size();

        for (ProductRating productrating: product.getReviews()) {
            sum = sum+productrating.getProductQualityRating();
            deliverySum += productrating.getDeliveryTimeRating();
            serviceSum += productrating.getServiceRating();
        }
        if(sum != 0){
            Double pQualityRating= (double) sum/noOfUsers;
            productDTO.productQualityRating = pQualityRating.intValue();
        }else {
            productDTO.productQualityRating=0;
        }

        if(deliverySum != 0){
            Double deliveryRating = (double) deliverySum/noOfUsers;
            productDTO.productDeliveryRating = deliveryRating.intValue();
        }else{
            productDTO.productDeliveryRating = 0;
        }

        if(serviceSum != 0){
            Double serviceRating = (double) serviceSum/noOfUsers;
            productDTO.productServiceRating = serviceRating.intValue();
        }else{
            productDTO.productServiceRating = 0;
        }

        productDTO.productType = product.getProductType();

        SizeGuide sizeGuide = product.getDesigner().getSizeGuide();
        if(sizeGuide != null){
            productDTO.sizeGuide = new SizeGuideDTO();
            productDTO.sizeGuide.femaleSizeGuide = sizeGuide.getFemaleSizeGuide();
            productDTO.sizeGuide.maleSizeGuide = sizeGuide.getMaleSizeGuide();
        }

        return productDTO;
    }

    public ProductRespDTO convertEntityToDTOWithReviews(Product product){
        ProductRespDTO productDTO = new ProductRespDTO();
        DecimalFormat df = new DecimalFormat("#.00");
        productDTO.id= product.id;
        productDTO.productColorStyleDTOS =convertProductAttributeEntitiesToDTOs(product.getProductStyle().getProductColorStyles());
        productDTO.description= product.getProdDesc();
        productDTO.prodSummary= product.getProdSummary();
        productDTO.name= product.getName();
        productDTO.sku = product.getSku();
        if(product.getProductStyle().getStyle() != null) {
            productDTO.styleId = product.getProductStyle().getStyle().id.toString();
        }
        productDTO.designerId= product.getDesigner().id.toString();

        productDTO.acceptCustomSizes= product.getProductStatuses().getAcceptCustomSizes();
        productDTO.availability= product.getProductStatuses().getAvailability();
        productDTO.designerName= product.getDesigner().getStoreName();
        productDTO.status= product.getProductStatuses().getStatus();
        productDTO.sponsoredFlag= product.getProductStatuses().getSponsoredFlag();
        productDTO.verifiedFlag= product.getProductStatuses().getVerifiedFlag();
        productDTO.subCategoryId= product.getSubCategory().id.toString();
        productDTO.subCategoryName= product.getSubCategory().getSubCategory();
        productDTO.categoryId= product.getSubCategory().getCategory().id.toString();
        productDTO.categoryName= product.getSubCategory().getCategory().categoryName;
        productDTO.numOfTimesOrdered = product.getNumOfTimesOrdered();

        productDTO.amount=product.getProductPrice().getAmount();
        if(product.getProductPrice().getPriceSlash() != null){
            productDTO.slashedPrice = product.getProductPrice().getPriceSlash().getSlashedPrice();
            productDTO.percentageDiscount = Double.parseDouble(df.format(product.getProductPrice().getPriceSlash().getPercentageDiscount()));
        }

        if(product.getProductStyle().getBespokeProduct() != null) {
            BespokeProductDTO bespokeProductDTO = new BespokeProductDTO();
            if(product.getProductType() == 1) {

                bespokeProductDTO.setArtPictureDTOS(convertArtPictureEntitiesToDTO(product.getProductStyle().getBespokeProduct().getArtWorkPicture()));
                bespokeProductDTO.setMaterialPicture(convertMatPictureEntitiesToDTO(product.getProductStyle().getBespokeProduct().getMaterialPicture()));
            }
            else{
                bespokeProductDTO.setArtPictureDTOS(null);
                bespokeProductDTO.setMaterialPicture(null);
            }
            bespokeProductDTO.setId(product.getProductStyle().getBespokeProduct().id);
            bespokeProductDTO.setNumOfDaysToComplete(product.getProductStyle().getBespokeProduct().getNumOfDaysToComplete());
            bespokeProductDTO.setMandatoryMeasurements(product.getProductStyle().getBespokeProduct().getMandatoryMeasurements());

            productDTO.bespokeProductDTO=bespokeProductDTO;
        }


        productDTO.reviews= product.getReviews();


        productDTO.productType = product.getProductType();

        SizeGuide sizeGuide = product.getDesigner().getSizeGuide();
        if(sizeGuide != null){
            productDTO.sizeGuide = new SizeGuideDTO();
            productDTO.sizeGuide.femaleSizeGuide = sizeGuide.getFemaleSizeGuide();
            productDTO.sizeGuide.maleSizeGuide = sizeGuide.getMaleSizeGuide();
        }

        return productDTO;

    }


    public String getPicsName(String picsArrayType, String productName){

        String timeStamp = picsArrayType + getCurrentTime();

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



    public EventPicturesDTO convertEntityToDTO(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();

        eventPicturesDTO.setId(eventPictures.id);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        return eventPicturesDTO;

    }


    public List<ProductColorStyleDTO> convertProductAttributeEntitiesToDTOs(List<ProductColorStyle> productColorStyles){

        List<ProductColorStyleDTO> productColorStyleDTOS = new ArrayList<ProductColorStyleDTO>();
        for(ProductColorStyle p: productColorStyles){
            ProductColorStyleDTO productColorStyleDTO = convertProductAttributeEntityToDTO(p);
            productColorStyleDTOS.add(productColorStyleDTO);
        }
        return productColorStyleDTOS;
    }

    public ProductColorStyleDTO convertProductAttributeEntityToDTO(ProductColorStyle productAttribute){
        ProductColorStyleDTO productColorStyleDTO = new ProductColorStyleDTO();

        productColorStyleDTO.setId(productAttribute.id);
        productColorStyleDTO.setColourPicture(productAttribute.getColourPicture());
        productColorStyleDTO.setColourName(productAttribute.getColourName());

        productColorStyleDTO.setProductPictureDTOS(convertProdPictureEntitiesToDTO(productAttribute.getProductPictures()));
        productColorStyleDTO.setProductSizes(productAttribute.getProductSizes());
        return productColorStyleDTO;

    }


    public List<EventsDTO> convertEntitiesToDTOs(List<Events> events){

        List<EventsDTO> eventsDTOS = new ArrayList<EventsDTO>();

        for(Events events1: events){
            EventsDTO eventsDTO = convertEntityToDTO(events1);
            eventsDTOS.add(eventsDTO);
        }
        return eventsDTOS;
    }


    public List<EventPicturesDTO> convertEntsToDTOs(List<EventPictures> events){

        List<EventPicturesDTO> picturesDTOS = new ArrayList<EventPicturesDTO>();

        for(EventPictures eventsp: events){
            EventPicturesDTO picturesDTO = convertEntityToDTO(eventsp);
            picturesDTOS.add(picturesDTO);
        }
        return picturesDTOS;
    }

    public EventsDTO convertEntityToDTO(Events events){
        EventsDTO eventsDTO = new EventsDTO();
        eventsDTO.setId(events.id);
        eventsDTO.setDescription(events.getDescription());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringDate = formatter.format(events.getEventDate());
        eventsDTO.setEventDate(stringDate);
        eventsDTO.setEventName(events.getEventName());
        eventsDTO.setLocation(events.getLocation());

        eventsDTO.setMainPicture(events.getMainPicture());
        eventsDTO.setEventPictures(convertEvtPicEntToDTOsMin(eventPictureRepository.findFirst6ByEvents(events)));

        return eventsDTO;

    }

    public List<EventPicturesDTO> convertEvtPicEntToDTOsMin(List<EventPictures> eventPictures){

        List<EventPicturesDTO> eventPicturesDTOS = new ArrayList<EventPicturesDTO>();

        for(EventPictures eventPictures1: eventPictures){
            EventPicturesDTO eventPicturesDTO = convertEntityToDTOMin(eventPictures1);
            eventPicturesDTOS.add(eventPicturesDTO);
        }
        return eventPicturesDTOS;
    }


    public EventPicturesDTO convertEntityToDTOMin(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();
        eventPicturesDTO.setEventName(eventPictures.events.getEventName());
        eventPicturesDTO.setId(eventPictures.id);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        eventPicturesDTO.setPictureDesc(eventPictures.getPictureDesc());
        return eventPicturesDTO;

    }

    public List<Product> getRandomProducts(List<Product> products, int numberOfProducts) {
        List<Product> randomProducts = new ArrayList<>();
        List<Product> copy = new ArrayList<>(products);

        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < Math.min(numberOfProducts, products.size()); i++) {
            randomProducts.add( copy.remove( rand.nextInt( copy.size() ) ));
        }
        return randomProducts;
    }




    public List<ItemsRespDTO> convertItemsEntToDTOs(List<Items> items){

        List<ItemsRespDTO> itemsDTOS = new ArrayList<ItemsRespDTO>();

        for(Items items1: items){
            ItemsRespDTO itemsDTO = convertEntityToDTO(items1);
            itemsDTOS.add(itemsDTO);
        }
        return itemsDTOS;
    }


    public List<CartDTO> convertCartEntsToDTOs(List<Cart> carts){
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart:carts){
            CartDTO cartDTO = convertCartEntToDTO(cart);
            cartDTOS.add(cartDTO);
        }
        return cartDTOS;
    }

    public CartDTO convertCartEntToDTO(Cart cart){

        //System.out.println("The ProductId=="+cart.getProductId());
        CartDTO cartDTO = new CartDTO();

        cartDTO.setId(cart.id);

        cartDTO.setProductId(cart.getProductId());

        Product product = productRepository.findOne(cart.getProductId());
        //System.out.println("The Saved Total Amount For this cart "+cart.id+"==="+cart.getAmount());
        // Hence search for itemtype of either 'product' or 'category'
        List<PromoItem> promoItemList=promoItemsRepository.findAllPromoItemsBelongToCategoryAndProduct(cart.getProductId(),"p","c");
        //System.out.println("Retrieved promoItemsList Size=="+promoItemList.size());
        if(promoItemList!=null && promoItemList.size()>0){

            PromoCode promoCode=promoItemList.get(0).getPromoCode(); // Ensure that alteast one of the items has a promoCode attached to it.
            //System.out.println("Retrieved PromoCode=="+promoCode.getCode());
            //Does this product have a promocode and it has not been used by the same user?
            if(promoCode!=null && !cart.getPromoCode().equalsIgnoreCase("USER_USED")){
                //Then set 'Y' for yes so that the user is allowed to enter the promocode and use it.
                cartDTO.setHasPromoCode("Y");
            }

        }
        cartDTO.setProductName(product.getName());
        cartDTO.setProductColorStyleId(cart.getProductColorStyleId());

        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setPrice(product.getProductPrice().getAmount());
        cartDTO.setSlashedPrice(0.0);

        if(product.getProductPrice().getPriceSlash() != null) {
            if(product.getProductPrice().getPriceSlash().getSlashedPrice() > 0){
                cartDTO.setSlashedPrice(product.getProductPrice().getPriceSlash().getSlashedPrice());
            }
        }

        if(cartDTO.getSlashedPrice() > 0){
            cartDTO.setTotalPrice(cartDTO.getSlashedPrice()*cartDTO.getQuantity());
        }else{
            cartDTO.setTotalPrice(cartDTO.getPrice()*cartDTO.getQuantity());
        }

        ProductPicture p = productPictureRepository.findFirst1ByProductColorStyle_ProductStyle_Product(product);
        cartDTO.setProductPicture(p.getPictureName());

        cartDTO.setStockNo(productSizesRepository.findOne(cart.getProductSizeId()).getNumberInStock());

        if(cart.getArtWorkPictureId() != null) {
            ArtWorkPicture a = artWorkPictureRepository.findOne(cart.getArtWorkPictureId());
            cartDTO.setArtWorkPicture(a.getPictureName());
            cartDTO.setArtWorkPictureId(cart.getArtWorkPictureId());
        }

        if(cart.getMaterialPictureId() != null) {
            System.out.println(cart.getMaterialPictureId());
            MaterialPicture m = materialPictureRepository.findOne(cart.getMaterialPictureId());
            cartDTO.setMaterialPicture(m.getPictureName());
            cartDTO.setMaterialPictureId(cart.getMaterialPictureId());
        }

        cartDTO.setColor(cart.getColor());
        cartDTO.setSize(cart.getSize());
        String acceptCustomSizes = productRepository.findOne(cart.getProductId()).getProductStatuses().getAcceptCustomSizes();

        if(cart.getProductColorStyleId() == null){
            cartDTO.setSizeStockNo(1);
        }else{
           ProductColorStyle productAttribute =  productColorStyleRepository.findOne(cart.getProductColorStyleId());
           if(productAttribute != null){
               ProductSizes productSizes = productSizesRepository.findByProductColorStyleAndName(productAttribute, cart.getSize());
               cartDTO.setSize(productSizes.getName());
               cartDTO.setSizeStockNo(productSizes.getNumberInStock());
           }
        }

        cartDTO.setMaterialLocation(cart.getMaterialLocation());
        cartDTO.setMaterialPickupDate(cart.getMaterialPickupDate());
        cartDTO.setMaterialStatus(cart.getMaterialStatus());
        cartDTO.setDesignerId(cart.getDesignerId());
        Designer designer = designerRepository.findOne(cart.getDesignerId());
        cartDTO.setDesignerName(designer.getStoreName());

        if(cart.getMeasurementId() != null) {
            Measurement m = measurementRepository.findOne(cart.getMeasurementId());
            cartDTO.setMeasurementName(m.getName());
            cartDTO.setMeasurementId(cart.getMeasurementId());
        }
        return cartDTO;

    }


    public ItemsRespDTO convertEntityToDTO(Items items){
        ItemsRespDTO itemsDTO = new ItemsRespDTO();
        if(items != null) {

            List<OrderItemProcessingPicture> orderItemProcessingPictures = orderItemProcessingPictureRepository.findByItems(items);
            if(orderItemProcessingPictures.size() > 0){
                itemsDTO.setPictures(orderItemProcessingPictures);
            }
            itemsDTO.setId(items.id);
            itemsDTO.setProductId(items.getProductId());
            Product p = productRepository.findOne(items.getProductId());
            itemsDTO.setProductName(p.getName());
            itemsDTO.setProductAvailability(p.getProductStatuses().getAvailability());

            itemsDTO.setAmount(items.getAmount().toString());
            itemsDTO.setQuantity(items.getQuantity());

            User user;
            if(items.getOrders().isAnonymousBuyer()){
                user = new User();
                AnonymousUser anonymousUser = anonymousUserRepository.findOne(items.getOrders().getAnonymousUserId());
                user.setEmail(anonymousUser.getEmail());
                user.setFirstName("Anonymous");
                user.setLastName("Anonymous");
            }else{
                user = userRepository.findById(items.getOrders().getUserId());
            }


            itemsDTO.setCustomerName(user.getLastName()+" "+user.getFirstName());
            itemsDTO.setCustomerId(user.id);
            itemsDTO.setProductPicture(items.getProductPicture());


            itemsDTO.setArtWorkPicture(items.getArtWorkPicture());

            itemsDTO.setMaterialPicture(items.getMaterialPicture());

            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Orders orders = items.getOrders();
            itemsDTO.setArtWorkPictureId(items.getArtWorkPictureId());
            itemsDTO.setSize(items.getSize());
            itemsDTO.setUserComplain(items.getComplain());

            itemsDTO.setOrderDate(formatter.format(orders.getOrderDate()));

            itemsDTO.setStatus(items.getItemStatus().getStatus());
            itemsDTO.setStatusId(items.getItemStatus().id);
            itemsDTO.setFailedInspectionReason(items.getFailedInspectionReason());
            if(items.getMaterialLocation() != null){
                itemsDTO.setMaterialLocation(items.getMaterialLocation().toString());
            }
            itemsDTO.setMaterialPickupDate(items.getMaterialPickupDate());
            itemsDTO.setMaterialStatus(items.getMaterialStatus());
            itemsDTO.setMaterialPictureId(items.getMaterialPictureId());
            itemsDTO.setDesignerId(items.getDesignerId());
            itemsDTO.setOrderNumber(orders.getOrderNum());
            itemsDTO.setOrderId(orders.id);
            if (items.getMeasurementId() != null) {
                itemsDTO.setMeasurement(items.getMeasurement());
            }
            itemsDTO.setTrackingNumber(items.getTrackingNumber());


        }
        return itemsDTO;

    }





    public List<OrderDTO> convertOrderEntsToDTOs(List<Orders> orders){
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for(Orders orders1:orders){
            OrderDTO orderDTO = convertOrderEntToDTOs(orders1);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    public OrderDTO convertOrderEntToDTOs(Orders orders){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orders.id);
        orderDTO.setDeliveryAddress(orders.getDeliveryAddress().getAddress());
        orderDTO.setDeliveryStatus(orders.getDeliveryStatus());
        orderDTO.setOrderNumber(orders.getOrderNum());
        orderDTO.setPaymentType(orders.getPaymentType());
        orderDTO.setTotalAmount(orders.getTotalAmount().toString());
        orderDTO.setPaidAmount(orders.getPaidAmount());
        User user=userRepository.findById(orders.getUserId());
        orderDTO.setCustomerName(user.getLastName()+user.getFirstName());
        orderDTO.setDeliveryPhoneNumber(orders.getDeliveryAddress().getPhoneNo());
        orderDTO.setCustomerPhoneNumber(user.getPhoneNo());
        orderDTO.setUserId(orders.getUserId());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(orders.getDeliveryDate() != null) {
            orderDTO.setDeliveryDate(formatter.format(orders.getDeliveryDate()));
        }

        orderDTO.setOrderDate(formatter.format(orders.getOrderDate()));
        orderDTO.setItemsList(convertItemsEntToDTOs(orders.getItems()));

        return orderDTO;

    }

    public User convertAnonymousUsertoTempUser(AnonymousUser anonymousUser){
        User customer = new User();
        customer.setEmail(anonymousUser.getEmail());
        customer.setPhoneNo(anonymousUser.getPhoneNo());
        customer.setFirstName("User");
        customer.setLastName("Anonymous");

        return customer;
    }

    public List<PromoCodeDTO> convertPromoCodeListsToDTO(List<PromoCode> promoCodeList){

        List<PromoCodeDTO> promoCodeDTOLists= new ArrayList<>();
        for(PromoCode promoCode :promoCodeList){
            PromoCodeDTO promoCodeDTO=new PromoCodeDTO();
            promoCodeDTO.setCode(promoCode.getCode());
            List<PromoItemDTO> promoItemDTOLists = new ArrayList<>();
            for(PromoItem promoItem: promoCode.getPromoItems())
            {
                PromoItemDTO promoItemDTO =new PromoItemDTO();
                promoItemDTO.setItemId(promoItem.getItemId());
                promoItemDTO.setItemType(promoItem.getItemType());
                promoItemDTOLists.add(promoItemDTO);
            }
            promoCodeDTO.setPromoItemsDTO(promoItemDTOLists);
            promoCodeDTO.setValue(promoCode.getValue());
            promoCodeDTO.setValueType(promoCode.getValueType());
            promoCodeDTO.setExpiryDate(promoCode.getExpiryDate().toString());
            promoCodeDTO.setIsUsedStatus(promoCode.getIsUsedStatus());
            promoCodeDTO.setNumberOfUsage(promoCode.getNumberOfUsage());
            promoCodeDTOLists.add(promoCodeDTO);
        }
        return promoCodeDTOLists;
    }

    public PromoCodeDTO convertSinglePromoCodeToDTO(PromoCode promoCode){

        PromoCodeDTO promoCodeDTO=new PromoCodeDTO();
        promoCodeDTO.setCode(promoCode.getCode());
        List<PromoItemDTO> promoItemDTOLists = new ArrayList<>();
        for(PromoItem promoItem: promoCode.getPromoItems())
        {
            PromoItemDTO promoItemDTO =new PromoItemDTO();
            promoItemDTO.setItemId(promoItem.getItemId());
            promoItemDTO.setItemType(promoItem.getItemType());
            promoItemDTOLists.add(promoItemDTO);
        }
        promoCodeDTO.setPromoItemsDTO(promoItemDTOLists);
        promoCodeDTO.setValue(promoCode.getValue());
        promoCodeDTO.setValueType(promoCode.getValueType());
        promoCodeDTO.setExpiryDate(promoCode.getExpiryDate().toString());
        promoCodeDTO.setIsUsedStatus(promoCode.getIsUsedStatus());
        promoCodeDTO.setNumberOfUsage(promoCode.getNumberOfUsage());


        return promoCodeDTO;
    }



}
