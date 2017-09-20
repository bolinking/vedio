package gwall.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gwall.dao.UserDao;
import gwall.entity.PageModel;
import gwall.entity.UserInfo;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public void selectUserInfo(PageModel<UserInfo> pageModel) {
		pageModel.setRows(userDao.selectUserInfo(pageModel));
        pageModel.setTotal(userDao.selectUserCountWithPage(pageModel));
	}
    @Transactional
	public Integer saveuserInfo(UserInfo user) {
		return userDao.saveuserInfo(user);
	}
    @Transactional
	public Integer deleteuserInfo(List<UserInfo> lists) {
		return userDao.deleteuserInfo(lists);
	}

}
