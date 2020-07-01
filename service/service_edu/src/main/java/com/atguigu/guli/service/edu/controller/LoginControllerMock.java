package com.atguigu.guli.service.edu.controller;

import com.atguigu.guli.common.base.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * 临时的登录接口
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class LoginControllerMock {

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public R info() {
        return R.ok().data("name", "admin")
                .data("roles", "[admin]")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public R logout() {
        return R.ok();
    }

}
