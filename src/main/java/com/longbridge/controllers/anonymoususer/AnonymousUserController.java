package com.longbridge.controllers.anonymoususer;

import com.longbridge.models.AnonymousUser;
import com.longbridge.models.Response;
import com.longbridge.services.AnonymousUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/anonymoususer")
public class AnonymousUserController {

    @Autowired
    AnonymousUserService anonymousUserService;

    @PostMapping(value = "/create")
    public Response createAnonymousUser(@RequestBody AnonymousUser anonymousUser){

        return anonymousUserService.createAnonymousUser(anonymousUser);
    }

    @PostMapping(value = "/update")
    public Response updateAnonymousUser(@RequestBody AnonymousUser anonymousUser){

        return anonymousUserService.updateAnonymousUser(anonymousUser);
    }

    @GetMapping(value = "/{id}/getdetails")
    public Response getAnonymousUserDetails(@PathVariable Long id){

        return anonymousUserService.getAnonymousUserDetails(id);
    }
}
