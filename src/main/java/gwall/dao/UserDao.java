package gwall.dao;

import java.util.List;

import gwall.entity.PageModel;
import gwall.entity.UserInfo;

public interface UserDao {

	List<UserInfo> selectUserInfo(PageModel<UserInfo> pageModel);
	
	Integer selectUserCountWithPage(PageModel<UserInfo> pageModel);
	
	Integer saveuserInfo(UserInfo user);
	
	Integer deleteuserInfo(List<UserInfo> lists);
}
