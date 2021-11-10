package com.project.covid.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.UserProfileMapper;
import com.project.covid.model.UserProfile;



@RestController
public class UserProfileController {
	private UserProfileMapper mapper;
	public UserProfileController(UserProfileMapper mapper) {
		this.mapper = mapper;
	}

	@GetMapping("/user/{id}")
	public UserProfile getUserProfile(@PathVariable("id") String id) {
		return mapper.getUserProfile(id);
	}
	
	@GetMapping("/user/all")
	public List<UserProfile> getUserProfileList(){
		return mapper.getUserProfileList();
	}
	
	@PostMapping("/user/{id}")
	public void putUserProfile(@PathVariable("id")String id, @RequestParam("idname")String idname,@RequestParam("phone")String phone,@RequestParam("address")String address) {
		mapper.insertUserProfile(id, idname, phone, address);
	}
	
	@PutMapping("/user/{id}")
	public void postUserProfile(@PathVariable("id")String id, @RequestParam("idname")String idname,@RequestParam("phone")String phone,@RequestParam("address")String address) {
		mapper.updateUserProfile(id, idname, phone, address);
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteUserProfile(@PathVariable("id") String id) {
		mapper.deleteUserProfile(id);
	}
	
}
