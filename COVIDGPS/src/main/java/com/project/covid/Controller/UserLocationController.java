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
	
	@GetMapping("/infection/{id}")
	public List<String> getUserIdLocation(@PathVariable("id") String id) {
		List<String> uid_List = new ArrayList<>();
		List<InfectionLoc> mcode_list;
		mcode_list = mapper.getUidLocation(id);
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date it;
		Date ot;
		for (InfectionLoc loc : mcode_list) {
			String[] m_list = loc.getMcode().split("/");
			try {
				it=fm.parse(loc.getInTime());
				ot=fm.parse(loc.getOutTime());
				if(loc.getMcode().length()==0) {	
					uid_List.addAll(mapper.getUidContactorLoc(loc.getLoc(), it, ot));
				}else {
					for (String str : m_list) {
						uid_List.addAll(mapper.getUidContactor(str, it, ot));
					}				
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		HashSet<String> dupData = new HashSet<String>(uid_List);
		uid_List = new ArrayList<String>(dupData);
		uid_List.remove(Integer.valueOf(id));
		return uid_List;
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
