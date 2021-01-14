package com.smlyk.eshopinventory.controller;


import com.smlyk.eshopinventory.model.User;
import com.smlyk.eshopinventory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author always
 * @since 2020-12-31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("get")
    public User getUser(){
        return userService.getUser();
    }

}
