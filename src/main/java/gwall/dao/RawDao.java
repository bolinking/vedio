package gwall.dao;

import java.util.List;

import gwall.entity.Raw;

public interface RawDao {

	Raw queryRawById(String id);
	
	List<Raw> queryAllRaw();
	
	int sumTotal();
}
