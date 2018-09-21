package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.EventPicturesDTO;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PicTagDTO;
import com.longbridge.dto.PictureTagDTO;
import com.longbridge.models.Response;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Longbridge on 27/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/product")
public class TagController {
    @Autowired
    ProductService productService;


    @PostMapping(value = "/gettagproducts")
    public Object getProductsBySub(@RequestBody PicTagDTO picTagDTO){
        List<ProductRespDTO> products= productService.getTagProducts(picTagDTO);
        return new Response("00","Operation Successful",products);
    }


    @PostMapping(value = "/addTag")
    public Response addTag(@RequestBody PictureTagDTO pictureTagDTO){
        productService.addPictureTag(pictureTagDTO);
        return new Response("00","Operation Successful","success");


    }

    @GetMapping(value = "/{id}/deletetag")
    public Response deleteTag(@PathVariable Long id){
        productService.deletePictureTag(id);
        return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/{id}/gettag")
    public Response getTag(@PathVariable Long id){
        return new Response("00","Operation Successful",productService.getPictureTagById(id));

    }


    @GetMapping(value = "/{eventPictureId}/gettags")
    public Object getTags(@PathVariable Long eventPictureId){
        PictureTagDTO pictureTags=productService.getPictureTags(eventPictureId);
        return new Response("00","Operation Successful",pictureTags);
    }


    @PostMapping(value = "/getuntagged")
    public Response getUntaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPictures(pageableDetailsDTO);
        return new Response("00", "Operation Successful", eventpictures);

    }

    @PostMapping(value = "/gettagged")
    public Response getTaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPictures(pageableDetailsDTO);
        return new Response("00", "Operation Successful", eventpictures);

    }

    @GetMapping(value = "/{eventid}/getuntagged")
    public Response getUntaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPicturesByEvents(eventid);
        return new Response("00", "Operation Successful", eventpictures);
    }


    @GetMapping(value = "/{eventid}/gettagged")
    public Response getTaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPicturesByEvents(eventid);
        return new Response("00", "Operation Successful", eventpictures);
    }

}
