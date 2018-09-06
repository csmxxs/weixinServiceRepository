package com.ylms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylms.entity.IpWhite;
import com.ylms.mapper.IpWhiteMapper;

@Service
public class IpWhiteService {
	@Autowired
	private IpWhiteMapper ip;

	public IpWhite findAll() {
		return ip.findAll();
	}
    @Transactional
	public int update(IpWhite ipWhite) {
		if (ipWhite == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return ip.update(ipWhite);
	}
    @Transactional
	public int add(IpWhite ipWhite) {
		if (ipWhite == null) {
			throw new IllegalArgumentException("参数错误!");
		}
		return ip.add(ipWhite);
	}
}
