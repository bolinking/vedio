package gwall.service;

import java.util.List;

import gwall.entity.Raw;

public interface RawService {
	
	Raw queryRawById(String id);
	
	List<Raw> queryAllRaw();
	
	int sumTotal();
}
