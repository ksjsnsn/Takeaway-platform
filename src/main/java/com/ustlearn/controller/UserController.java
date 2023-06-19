package com.ustlearn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ustlearn.common.Result;
import com.ustlearn.pojo.User;
import com.ustlearn.service.UserService;
import com.ustlearn.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码短信
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用阿里云提供的短信api完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //需要将生成的验证码保存到session
            session.setAttribute(phone, code);
            return Result.success("手机验证码短信发送成功");
        }

        return Result.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<User> longin(@RequestBody Map map, HttpSession session) {

        log.info("map:{}", map);
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码比对(页面中提交的验证码和session中的验证码比对)
        if (codeInSession != null && codeInSession.equals(code)) {
            //比对成功,说明登录成功
            //查看数据库中的用户并返回给前端
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前手机号是否为新用户,新用户则自动完成注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //登录成功将用户id放到session中,通过过滤器
            session.setAttribute("user", user.getId());
            return Result.success(user);
        }
        return Result.error("登录失败");
    }

}
