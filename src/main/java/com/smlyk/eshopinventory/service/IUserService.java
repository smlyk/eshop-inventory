package com.smlyk.eshopinventory.service;

import com.smlyk.eshopinventory.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author always
 * @since 2020-12-31
 */
public interface IUserService extends IService<User> {


    User getUser();

}
