package com.cdut.controller;

import com.cdut.epidemicsyscontrolcommon.utils.AjaxResult;
import com.cdut.epidemicsyscontrolcommon.utils.MD5Util;
import com.cdut.epidemicsyscontrolcommon.utils.SaltUtils;
import com.cdut.epidemicsyscontrolframework.annotations.Anonymous;
import com.cdut.epidemicsyscontrolsystem.pojo.SysUser;
import com.cdut.epidemicsyscontrolsystem.pojo.SysUserRole;
import com.cdut.epidemicsyscontrolsystem.service.SysUserRoleService;
import com.cdut.epidemicsyscontrolsystem.service.SysUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class SysRegistryController {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    // TODO: 2022/9/1 注册接口
    @Anonymous
    @RequestMapping(value = "/sysuser/rigister",method = POST)
    public AjaxResult registr(@Param("username")String username,@Param("password")String password){
        if (sysUserService.getSysUserByUsername(username) != null) {
            return AjaxResult.error("用户名已存在");
        }
        System.out.println(username + "    " + password);
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        System.out.println("一次加密: " + password);
        System.out.println(username);

        sysUser.setPassword(bCryptPasswordEncoder.encode(password));
        System.out.println(sysUser.toString());

        return AjaxResult.success("注册成功",sysUserService.rigister(sysUser));

    }
}
