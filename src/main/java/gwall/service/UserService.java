package gwall.service;

import java.util.List;

import gwall.entity.PageModel;
import gwall.entity.UserInfo;

public interface UserService {

	void selectUserInfo(PageModel<UserInfo> pageModel);
	
	Integer saveuserInfo(UserInfo user);
	
	Integer deleteuserInfo(List<UserInfo> lists);
}
