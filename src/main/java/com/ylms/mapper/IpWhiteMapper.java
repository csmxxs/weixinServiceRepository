package com.ylms.mapper;

import com.ylms.entity.IpWhite;

public interface IpWhiteMapper {
	IpWhite findAll();

	int add(IpWhite ipWhite);

	int update(IpWhite ipWhite);
}
