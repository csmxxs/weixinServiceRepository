package com.ylms.mapper;

import com.ylms.entity.User;

public interface UserMapper {
	int add(User user);

	int update(User user);

	User findByOpenId(String openId);

	int deleteByOpenId(String openId);
}
