package com.qzero.bt.common.authorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityTestController {

    @RequestMapping("/test")
    public String test(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails.getUsername();
    }

}
