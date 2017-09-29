package gwall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gwall.dao.RawDao;
import gwall.entity.Raw;
@Service
public class RawServiceImpl implements RawService {

	@Autowired
	private RawDao rawDao;
	
	public Raw queryRawById(String id) {
		return rawDao.queryRawById(id);
	}

}
