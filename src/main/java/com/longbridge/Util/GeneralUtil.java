package com.longbridge.Util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    ProductAttributeRepository productAttributeRepository;

    @Autowired
    ProductSizesRepository productSizesRepository;

    @Autowired
    AnonymousUserRepository anonymousUserRepository;

    public DesignerDTO convertDesigner2EntToDTO(Designer d){
        DesignerDTO dto = new DesignerDTO();
        dto.id=d.id;
        dto.userId = d.getUser().id;
        dto.logo=d.getLogo();
        dto.storeName=d.getStoreName();
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
        List<ProductRespDTO> products= convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndVerifiedFlag(d,"Y"));
        dto.setProducts(products);

        List<String> stats = new ArrayList<>();
        //stats.add("OP");
        stats.add("P");

        List<ItemStatus> statuses = itemStatusRepository.findByStatusIn(stats);
        dto.noOfPendingOders= itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"P");
        dto.noOfDeliveredOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"D");
        dto.noOfCancelledOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id, "OR");
        dto.noOfConfirmedOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"A");
        dto.noOfReadyToShipOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"RS");
        dto.noOfShippedOrders=itemRepository.countByDesignerIdAndItemStatus_Status(d.id,"OS");
        Double amountOfPendingOrders = itemRepository.findSumOfPendingOrders(d.id,statuses);
        Double amountOfTotalOrders = itemRepository.findSumOfOrders(d.id);

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
        dto.address=d.getAddress();
        User u = d.getUser();
        dto.firstName=u.getFirstName();
        dto.lastName=u.getLastName();
        dto.phoneNo=u.getPhoneNo();
        dto.email=u.getEmail();
        dto.gender=u.getGender();
        dto.setStatus(d.getStatus());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.createdDate = formatter.format(d.createdOn);
        if(d.getStatus().equalsIgnoreCase("A")) {
            List<ProductRespDTO> products = convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndVerifiedFlag(d, "Y"));
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
        pictureDTO.productId=picture.getProducts().id;
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
        pictureDTO.productId=picture.getProducts().id;
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
        pictureDTO.setProductId(picture.getProducts().id);
        pictureDTO.setMaterialPicture(picture.getPictureName());
        return pictureDTO;

    }



    public List<ProductRespDTO> convertProdEntToProdRespDTOs(List<Products> products, User user){

        List<ProductRespDTO> productDTOS = new ArrayList<ProductRespDTO>();

        for(Products p: products){
            ProductRespDTO productDTO = convertEntityToDTO(p);
            if(user != null){
                if(wishListRepository.findByUserAndProducts(user,p) != null){
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
        DecimalFormat df = new DecimalFormat("#.00");
        productDTO.id=products.id;
        productDTO.amount=products.getAmount();
      //  productDTO.color=products.color;
        productDTO.description=products.getProdDesc();
        productDTO.prodSummary=products.getProdSummary();
        productDTO.name=products.getName();

        productDTO.productAttributeDTOS=convertProductAttributeEntitiesToDTOs(products.getProductAttributes());

      //  productDTO.productSizes=products.productSizes;
        if(products.getStyle() != null) {
            productDTO.styleId = products.getStyle().id.toString();
        }
        productDTO.designerId=products.getDesigner().id.toString();
        productDTO.designerStatus=products.getDesigner().getStatus();
        productDTO.stockNo=products.getStockNo();
        productDTO.inStock=products.getInStock();
        productDTO.availability=products.getAvailability();
        productDTO.acceptCustomSizes=products.getAcceptCustomSizes();
        productDTO.designerName=products.getDesigner().getStoreName();
        productDTO.status=products.getStatus();
        productDTO.sponsoredFlag=products.getSponsoredFlag();
        productDTO.verifiedFlag=products.getVerifiedFlag();
        productDTO.subCategoryId=products.getSubCategory().id.toString();
        productDTO.subCategoryName=products.getSubCategory().getSubCategory();
        productDTO.categoryId=products.getSubCategory().getCategory().id.toString();
        productDTO.categoryName=products.getSubCategory().getCategory().categoryName;
        productDTO.numOfTimesOrdered = products.getNumOfTimesOrdered();
        productDTO.numOfDaysToComplete=products.getNumOfDaysToComplete();
        productDTO.mandatoryMeasurements=products.getMandatoryMeasurements();

        PriceSlash priceSlash = priceSlashRepository.findByProducts(products);
        if(priceSlash != null){
            productDTO.slashedPrice = priceSlash.getSlashedPrice();
            productDTO.percentageDiscount = Double.parseDouble(df.format(priceSlash.getPercentageDiscount()));
        }

        List<ProductPicture> productPictures = products.getPicture();
        productDTO.picture=convertProdPictureEntitiesToDTO(productPictures);

        if(products.getProductType() == 1){
            List<ArtWorkPicture> artWorkPictures = products.getArtWorkPicture();
            productDTO.artWorkPicture=convertArtPictureEntitiesToDTO(artWorkPictures);

            List<MaterialPicture> materialPictures = products.getMaterialPicture();
            productDTO.materialPicture=convertMatPictureEntitiesToDTO(materialPictures);
        }else{
            productDTO.artWorkPicture = null;
            productDTO.materialPicture = null;
        }

        int sum = 0;
        int deliverySum = 0;
        int serviceSum = 0;

        int noOfUsers = products.getReviews().size();

        for (ProductRating productrating: products.getReviews()) {
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

        productDTO.productType = products.getProductType();

        SizeGuide sizeGuide = products.getDesigner().getSizeGuide();
        if(sizeGuide != null){
            productDTO.sizeGuide = new SizeGuideDTO();
            productDTO.sizeGuide.femaleSizeGuide = sizeGuide.getFemaleSizeGuide();
            productDTO.sizeGuide.maleSizeGuide = sizeGuide.getMaleSizeGuide();
        }

        return productDTO;
    }

    public ProductRespDTO convertEntityToDTOWithReviews(Products products){
        ProductRespDTO productDTO = new ProductRespDTO();
        DecimalFormat df = new DecimalFormat("#.00");
        productDTO.id=products.id;
        productDTO.amount=products.getAmount();
        //productDTO.productAttributes=products.productAttributes;
        productDTO.productAttributeDTOS=convertProductAttributeEntitiesToDTOs(products.getProductAttributes());
        //productDTO.color=products.color;
        productDTO.description=products.getProdDesc();
        productDTO.prodSummary=products.getProdSummary();
        productDTO.name=products.getName();
       // productDTO.productSizes=products.productSizes;
        if(products.getStyle() != null) {
            productDTO.styleId = products.getStyle().id.toString();
        }
        productDTO.designerId=products.getDesigner().id.toString();
        productDTO.stockNo=products.getStockNo();
        productDTO.inStock=products.getInStock();
        productDTO.acceptCustomSizes=products.getAcceptCustomSizes();
        productDTO.availability=products.getAvailability();
        productDTO.designerName=products.getDesigner().getStoreName();
        productDTO.status=products.getStatus();
        productDTO.sponsoredFlag=products.getSponsoredFlag();
        productDTO.verifiedFlag=products.getVerifiedFlag();
        productDTO.subCategoryId=products.getSubCategory().id.toString();
        productDTO.subCategoryName=products.getSubCategory().getSubCategory();
        productDTO.categoryId=products.getSubCategory().getCategory().id.toString();
        productDTO.categoryName=products.getSubCategory().getCategory().categoryName;
        productDTO.numOfTimesOrdered = products.getNumOfTimesOrdered();
        productDTO.numOfDaysToComplete=products.getNumOfDaysToComplete();
        productDTO.mandatoryMeasurements=products.getMandatoryMeasurements();
        List<ProductPicture> productPictures = products.getPicture();
        productDTO.picture=convertProdPictureEntitiesToDTO(productPictures);

        if(products.getProductType() == 1){
            List<ArtWorkPicture> artWorkPictures = products.getArtWorkPicture();
            productDTO.artWorkPicture=convertArtPictureEntitiesToDTO(artWorkPictures);

            List<MaterialPicture> materialPictures = products.getMaterialPicture();
            productDTO.materialPicture=convertMatPictureEntitiesToDTO(materialPictures);
        }else{
            productDTO.artWorkPicture = null;
            productDTO.materialPrice = null;
        }

        productDTO.reviews=products.getReviews();
        PriceSlash priceSlash = priceSlashRepository.findByProducts(products);
        if(priceSlash != null){
            productDTO.slashedPrice = priceSlash.getSlashedPrice();
            productDTO.percentageDiscount = Double.parseDouble(df.format(priceSlash.getPercentageDiscount()));
        }

        productDTO.productType = products.getProductType();

        SizeGuide sizeGuide = products.getDesigner().getSizeGuide();
        if(sizeGuide != null){
            productDTO.sizeGuide = new SizeGuideDTO();
            productDTO.sizeGuide.femaleSizeGuide = sizeGuide.getFemaleSizeGuide();
            productDTO.sizeGuide.maleSizeGuide = sizeGuide.getMaleSizeGuide();
        }

        return productDTO;

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



    public EventPicturesDTO convertEntityToDTO(EventPictures eventPictures){
        EventPicturesDTO eventPicturesDTO = new EventPicturesDTO();

        eventPicturesDTO.setId(eventPictures.id);
        eventPicturesDTO.setPicture(eventPictures.pictureName);
        return eventPicturesDTO;

    }


    public List<ProductAttributeDTO> convertProductAttributeEntitiesToDTOs(List<ProductAttribute> productAttributes){

        List<ProductAttributeDTO> productAttributeDTOS = new ArrayList<ProductAttributeDTO>();
        for(ProductAttribute p: productAttributes){
            ProductAttributeDTO productAttributeDTO = convertProductAttributeEntityToDTO(p);
            productAttributeDTOS.add(productAttributeDTO);
        }
        return productAttributeDTOS;
    }

    public ProductAttributeDTO convertProductAttributeEntityToDTO(ProductAttribute productAttribute){
        ProductAttributeDTO productAttributeDTO = new ProductAttributeDTO();

        productAttributeDTO.setId(productAttribute.id);
        productAttributeDTO.setColourPicture(productAttribute.getColourPicture());
        productAttributeDTO.setColourName(productAttribute.getColourName());
        productAttributeDTO.setProductPictureDTOS(convertProdPictureEntitiesToDTO(productAttribute.getProductPictures()));
        productAttributeDTO.setProductSizes(productAttribute.getProductSizes());
        return productAttributeDTO;

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

    public List<Products> getRandomProducts(List<Products> products, int numberOfProducts) {
        List<Products> randomProducts = new ArrayList<>();
        List<Products> copy = new ArrayList<>(products);

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


    public List<CartDTO>    convertCartEntsToDTOs(List<Cart> carts){
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart:carts){
            CartDTO cartDTO = convertCartEntToDTO(cart);
            cartDTOS.add(cartDTO);
        }
        return cartDTOS;
    }

    public CartDTO convertCartEntToDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();

        cartDTO.setId(cart.id);

        cartDTO.setProductId(cart.getProductId());

        Products products = productRepository.findOne(cart.getProductId());

        cartDTO.setProductName(products.getName());

        cartDTO.setProductAttributeId(cart.getProductAttributeId());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setPrice(products.getAmount());
        cartDTO.setSlashedPrice(0.0);

        if(products.getPriceSlash() != null) {
            if(products.getPriceSlash().getSlashedPrice() > 0){
                cartDTO.setSlashedPrice(products.getPriceSlash().getSlashedPrice());
            }
        }

        if(cartDTO.getSlashedPrice() > 0){
            cartDTO.setTotalPrice(cartDTO.getSlashedPrice()*cartDTO.getQuantity());
        }else{
            cartDTO.setTotalPrice(cartDTO.getPrice()*cartDTO.getQuantity());
        }

        ProductPicture p = productPictureRepository.findFirst1ByProducts(products);
        cartDTO.setProductPicture(p.getPictureName());
        cartDTO.setStockNo(products.getStockNo());

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
        String acceptCustomSizes = productRepository.findOne(cart.getProductId()).getAcceptCustomSizes();

        if(cart.getProductAttributeId() == null){
            cartDTO.setSizeStockNo(1);
        }else{
           ProductAttribute productAttribute =  productAttributeRepository.findOne(cart.getProductAttributeId());
           if(productAttribute != null){
               ProductSizes productSizes = productSizesRepository.findByProductAttributeAndName(productAttribute, cart.getSize());
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
            Products p = productRepository.findOne(items.getProductId());
            itemsDTO.setProductName(p.getName());
            itemsDTO.setProductAvailability(p.getAvailability());

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



}
