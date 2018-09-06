package com.ylms.service;

import com.ylms.entity.User;
import com.ylms.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: 49524
 * @Date: 2018/8/20 17:27
 * @Version 1.0
 */
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	public int add(User user) {
		if (user == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return userMapper.add(user);
	}

	public User findByOpenId(String openId) {
		if (openId.equals("") || openId == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return userMapper.findByOpenId(openId);
	}

	public int update(User user) {
		if (user == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return userMapper.update(user);
	}

	public int deleteByOpenId(String openId) {
		if (openId.equals("") || openId == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return userMapper.deleteByOpenId(openId);
	}
}
