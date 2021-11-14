package com.project.covid.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.UserLocationMapper;
import com.project.covid.model.InfectionLoc;

@RestController
@RequestMapping("/location")
public class UserLocationController {
	private UserLocationMapper mapper;
	public UserLocationController(UserLocationMapper mapper) {
		super();
		this.mapper = mapper;
	}
	//@PathVariable("uid") Integer uid
	@GetMapping("/infection/{uid}")
	public List<Integer> getUserIdLocation(@PathVariable("uid") Integer id){
		List<Integer> uid_List=new ArrayList<>();
		List<InfectionLoc> mcode_list;
		int uid=1;
		mcode_list=mapper.getUidLocation(uid);
		for(InfectionLoc loc:mcode_list) {
			String[] m_list=loc.getMcode().split("/");
			for(String str: m_list) {
				
			}
		}
		return uid_List;
	}
	
	



}
	
	
	
	

