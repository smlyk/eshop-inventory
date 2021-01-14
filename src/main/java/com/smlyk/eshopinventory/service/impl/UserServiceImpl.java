package com.smlyk.eshopinventory.service.impl;

import com.alibaba.fastjson.JSON;
import com.smlyk.eshopinventory.model.User;
import com.smlyk.eshopinventory.mapper.UserMapper;
import com.smlyk.eshopinventory.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author always
 * @since 2020-12-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public User getUser() {

        //先从redis获取
        String cacheUser = (String) redisTemplate.opsForValue().get("cache_user");
        if (!StringUtils.isEmpty(cacheUser)){
            User user = JSON.parseObject(cacheUser, User.class);
            return user;
        }
        List<User> users = baseMapper.selectList(null);
        if (!CollectionUtils.isEmpty(users)){
            //先放入缓存
            redisTemplate.opsForValue().set("cache_user", JSON.toJSONString(users.get(0)));
            return users.get(0);
        }
        return null;
    }
}
