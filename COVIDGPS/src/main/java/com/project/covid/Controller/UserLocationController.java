package com.project.covid.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.UserLocationMapper;
import com.project.covid.mapper.UserProfileMapper;
import com.project.covid.model.BluetoothVO;
import com.project.covid.model.InfectionLoc;
import com.project.covid.model.LocationVO;
import com.project.covid.model.UserLocation;
import com.project.covid.model.UserProfile;
import com.project.covid.service.UserLocationService;
import com.project.covid.service.UserProfileService;

@RestController
@RequestMapping("/location")
public class UserLocationController {
	
	@Autowired
	private UserLocationMapper mapper;

	@Autowired
	private UserProfileMapper userMapper;
	@Autowired
	private UserLocationService service;
	
	@GetMapping("/infection/{id}")
	public void getUserIdLocation(@PathVariable("id") String id) {
		List<InfectionLoc> mcode_list;
		mcode_list = mapper.getUidLocation(id);
		List<String> uid_list=service.trackingId(mcode_list, id);
		UserProfileService userService=new UserProfileService();
		for(String uid : uid_list) {
			UserProfile userProfile=userMapper.getUserProfile(uid);
			HashMap<String,String> token=userService.updateToken(userProfile.getRefresh_token());
			userService.sendMessage(token.get("access_token"));
		}
	}
	
	@PostMapping("/GPS")
	public void putUserIdLocation(LocationVO lvo) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(lvo.toString());
		try {
			
			Date it = fm.parse(lvo.getInTime());
			Date ot = fm.parse(lvo.getOutTime());
			mapper.putUserIdLocation(lvo.getId(), lvo.getMcode(), lvo.getLoc(), it,ot,lvo.getAddress());
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	@PostMapping("/bluetooth")
	public void putBluetoothUserIdLocation(BluetoothVO bvo) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		try {
			Date it=fm.parse(bvo.getInTime());
			cal.setTime(it);
			cal.add(Calendar.HOUR,+2);
			Date ot=cal.getTime();
			mapper.putUserIdLocation(bvo.getId(), "","", it, ot,mapper.gethostIdLocation(bvo.getHostID()));
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}
