package com.cdut.service;

import com.cdut.epidemic_common.utils.Page;
import com.cdut.mapper.UserMapper;
import com.cdut.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 47345
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-08-26 13:04:12
*/
public interface UserService extends IService<User>{
    List<User> getAll();

    User findByName(String username);

    int register(User user);
    int update(User user);

    User getUserByID(int id);

    Integer deleteByID(Integer id);

    List<User> getUsersByPage(Page page);

}
