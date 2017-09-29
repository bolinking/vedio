package gwall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

import gwall.entity.Raw;
import gwall.service.RawService;
/**
 * 用户登录
 * @author Lbl
 *
 */
@Controller
@RequestMapping("/")
public class LoginController {
	
	@Autowired
	private RawService rawService;
	@RequestMapping("login") // @RequestMapping 注解可以用指定的URL路径访问本控制层
    public String login(@RequestParam("Username") String Username, @RequestParam("Password") String Password,Model model) {

        if (Username.equals("admin") && Password.equals("admin")) {
            model.addAttribute("username", Username);
            String a="64";
            Raw raw =rawService.queryRawById(a);
            model.addAttribute("raw", raw);
            return "/display/display";
        } else {
            model.addAttribute("username", Username);
            return "no.jsp";
        }
    }
}
