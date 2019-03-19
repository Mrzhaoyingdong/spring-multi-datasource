package yuwei35kd.springmultidatasource.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yuwei35kd.springmultidatasource.mapper.UserMapper;
import yuwei35kd.springmultidatasource.mapper.UserMapper2;

@RestController
public class UserController {
	@Resource
	private UserMapper2 userMapper2;

	@Resource
	private UserMapper userMapper;
	
	@RequestMapping("findUser")
	public List<Map<String,Object>> findUser(){
		return userMapper.findUsers();
	}

	@RequestMapping("findUser2")
	public List<Map<String,Object>> findUsers(){
		return  userMapper2.findUsers();
	}



}
