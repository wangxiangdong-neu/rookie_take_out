package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.entity.User;
import com.rookie.takeout.service.UserService;
import com.rookie.takeout.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-08-31 12:22:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




