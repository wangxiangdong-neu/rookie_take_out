package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rookie.takeout.common.R;
import com.rookie.takeout.entity.User;
import com.rookie.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @title: UserController
 * @Author: Mrdong
 * @Date: 2022/8/31 12:23
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {

            //生成随机的4位验证码
            //String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //由于无法使用阿里云短信服务，这里验证码暂时固定为1234
            String code = "1234";

            log.info("验证码为：{}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("菜鸟外卖","",phone,code);

            //需要将生成的验证码保存到Session
            session.setAttribute(phone, code);

            return R.success("验证码发送成功！");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        String codeInSession = (String) session.getAttribute(phone);
        if (StringUtils.isNotEmpty(codeInSession) && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);

            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            //查看用户状态，如果为已禁用状态，则返回员工已禁用结果. 0表示禁用，1表示可用
            if (user.getStatus() == 0) {
                return R.error("该账号已封禁！无法登录");
            }

            //把用户id存入session中，表示已登录
            session.setAttribute("user", user.getId());
            return R.success(user);

        }

        return R.error("验证码错误，登录失败");
    }

    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("退出成功");
    }


}
