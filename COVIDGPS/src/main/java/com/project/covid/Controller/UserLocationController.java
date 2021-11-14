package com.project.covid.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.UserLocationMapper;
import com.project.covid.model.BluetoothVO;
import com.project.covid.model.InfectionLoc;
import com.project.covid.model.LocationVO;
import com.project.covid.model.UserLocation;

@RestController
@RequestMapping("/location")
public class UserLocationController {
	private UserLocationMapper mapper;

	public UserLocationController(UserLocationMapper mapper) {
		super();
		this.mapper = mapper;
	}
	
	@GetMapping("/infection/{uid}")
	public List<Integer> getUserIdLocation(@PathVariable("uid") Integer uid) {
		List<Integer> uid_List = new ArrayList<>();
		List<InfectionLoc> mcode_list;
		mcode_list = mapper.getUidLocation(uid);
		for (InfectionLoc loc : mcode_list) {
			String[] m_list = loc.getMcode().split("/");
			if(loc.getMcode().length()==0) {
				uid_List.addAll(mapper.getUidContactorLoc(loc.getLoc(), loc.getInTime(), loc.getOutTime()));
			}else {
				for (String str : m_list) {
					uid_List.addAll(mapper.getUidContactor(str, loc.getInTime(), loc.getOutTime()));
				}				
			}
		}
		HashSet<Integer> dupData = new HashSet<Integer>(uid_List);
		uid_List = new ArrayList<Integer>(dupData);
		uid_List.remove(Integer.valueOf(uid));
		return uid_List;
	}
	
	@PostMapping("/GPS")
	public void putUserIdLocation(@RequestBody LocationVO lvo) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date it = fm.parse(lvo.getInTime());
			Date ot = fm.parse(lvo.getOutTime());
			mapper.putUserIdLocation(lvo.getUid(), lvo.getMcode(), lvo.getLoc(), it,ot);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	@PostMapping("/bluetooth")
	public void putBluetoothUserIdLocation(@RequestBody BluetoothVO bvo) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		try {
			Date it=fm.parse(bvo.getInTime());
			cal.setTime(it);
			cal.add(Calendar.HOUR,+2);
			Date ot=cal.getTime();
			mapper.putUserIdLocation(bvo.getUid(), "", mapper.gethostIdLocation(bvo.getHostID()), it, ot);
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}
