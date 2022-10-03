package com.cdut.controller;

import com.cdut.epidemicsyscontrolcommon.utils.AjaxResult;
import com.cdut.epidemicsyscontrolcommon.utils.JWTUtil;
import com.cdut.epidemicsyscontrolcommon.utils.RedisCache;
import com.cdut.epidemicsyscontrolframework.security.SecurityUtil;
import com.cdut.epidemicsyscontrolsystem.pojo.LoginUser;
import com.cdut.epidemicsyscontrolsystem.pojo.SysUser;
import com.cdut.epidemicsyscontrolsystem.service.RoleService;
import com.cdut.epidemicsyscontrolsystem.service.SysUserRoleService;
import com.cdut.epidemicsyscontrolsystem.service.SysUserService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

// TODO: 2022/9/1 系统用户信息相关的接口
@RestController
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    RedisCache redisCache;

    /**
     * 管理员信息
     * @return
     */
    @GetMapping("/sysuser/info")
    public AjaxResult info(){
        LoginUser loginUser = SecurityUtil.getLoginUser();
        SysUser user = loginUser.getUser();
        user.setPassword(null);
        user.setSalt(null);
        user.setAvatarUrl(null);
        loginUser.setUser(user);
        return AjaxResult.success("管理员信息查询成功",loginUser);
    }


    // TODO: 2022/9/1 更新管理员信息
    @RequestMapping(value = "/sysuser/update",method = POST)
    public AjaxResult updateInfo(@RequestBody SysUser sysUser) {
        SysUser sysUser1 = sysUserService.getSysUserByUsername(sysUser.getUsername());
        if (sysUser1.getId() <= 0) return AjaxResult.error("不存在此用户",-1);
        sysUser.setId(sysUser1.getId());
        return AjaxResult.success("更新成功", sysUserService.update(sysUser));
    }

    // TODO: 2022/9/1 更改管理员权限
    @RequestMapping(value = "/sysuser/updaterole",method = POST)
    public AjaxResult updateRole(@Param("username") String username, @Param("roleId")Integer roleId){
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        if (sysUser.getId() <= 0) return AjaxResult.error("不存在此用户",-1);
        return AjaxResult.success("更新成功", sysUserRoleService.update(sysUser, roleId));
    }

    // TODO: 2022/9/1 更改管理员密码
    public AjaxResult changePassword(){
        return null;
    }

}
