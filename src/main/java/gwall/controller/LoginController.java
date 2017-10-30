package gwall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            List<Raw> raws =rawService.queryAllRaw();
            List<Raw> raws1 = new ArrayList<Raw>();
            List<Raw> raws2 = new ArrayList<Raw>();
            List<Raw> raws3 = new ArrayList<Raw>();
            for(int i = 1;i<=raws.size();i++){
            	if(i%3==0){
            		raws1.add(raws.get(i-1));
                }
    			if(i%3==1){
    				raws2.add(raws.get(i-1));
                }
    			if(i%3==2){
    				raws3.add(raws.get(i-1));
    			}
            }
            
            model.addAttribute("raws1", raws1);
            model.addAttribute("raws2", raws2);
            model.addAttribute("raws3", raws3);
            int total = rawService.sumTotal();
            model.addAttribute("total", total);
            if(total%51 == 0){
                model.addAttribute("page", total/51);
            }else{
                model.addAttribute("page", total/51+1);

            }
            return "/display/display";
        } else {
            model.addAttribute("username", Username);
            return "no.jsp";
        }
    }
}
