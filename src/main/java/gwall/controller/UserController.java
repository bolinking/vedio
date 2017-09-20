package gwall.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import gwall.entity.PageModel;
import gwall.entity.UserInfo;
import gwall.service.UserService;

@Controller
@RequestMapping("/userInfo")
public class UserController {
	@Autowired  
    private UserService userService;  
      
    @RequestMapping(value ="/",method = {RequestMethod.GET})  
    public String showInfo(final Model model) { 
        return "/user/userInfo";  
    }  
    
	@ResponseBody
    @RequestMapping("showInfos")  
    public Object selectUserInfo(PageModel<UserInfo> pageModel,UserInfo user) {//蓝色的就是你在ajax提交的时候传递的数据
    	 Object jsonObject = null;
         try {
             pageModel.setQueryObj(user);
             userService.selectUserInfo(pageModel);
             jsonObject = JSONObject.toJSON(pageModel);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return jsonObject;
    }
	
	@ResponseBody
    @RequestMapping("saveuserInfo") 
	public boolean saveuserInfo(UserInfo user){
		Integer result = userService.saveuserInfo(user);
		if(result<1){
			return false;
		}
		return true;
	}
	
	@ResponseBody
	@RequestMapping("deleteuserInfo")
	public boolean deleteuserInfo(String list){
		if(list == null){
			return false;
		}
		List<UserInfo> lists = new ArrayList<UserInfo>();  
        lists = JSONObject.parseArray(list, UserInfo.class);
		Integer result = userService.deleteuserInfo(lists);
		if(result<1){
			return false;
		}
		return true;
	}
	

}
