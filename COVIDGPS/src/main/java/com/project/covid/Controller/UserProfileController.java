package com.project.covid.Controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.UserProfileMapper;
import com.project.covid.model.UserProfile;
import com.project.covid.service.UserProfileService;


@RestController
@RequestMapping(value="/kakao")
public class UserProfileController {

	@Autowired
    private UserProfileService kakao;
	
	@Autowired
	private UserProfileMapper mapper;
    
    @GetMapping("/connect")
    public String connect() {	
    	return "https://kauth.kakao.com/oauth/authorize?client_id=c8f72fe8e065caa66728ff40de53d9fd"
    			+ "&redirect_uri=http://115.21.52.248:8080/kakao/login&response_type=code";
    }
    
    //로그인 or 회원가입
    @GetMapping("/login")
    public void login(@RequestParam("code") String code) {
        UserProfile userProfile = new UserProfile();
        userProfile.setcode(code);
    	System.out.println("controller code : "+ code);
        HashMap<String, String> userInfo = kakao.getAccessToken(code);
        userProfile.setAccess_token(userInfo.get("access_token"));
        userProfile.setRefresh_token(userInfo.get("refresh_token"));
        System.out.println("controller access_token : " + userInfo.get("access_token"));
        userProfile.setId(kakao.getUserInfo(userInfo.get("access_token")));
        System.out.println("login Controller : " + userProfile);
        UserProfile temp=mapper.getUserProfile(userProfile.getId());
        if(temp==null)
        	mapper.insertUserProfile(userProfile.getId(), userProfile.getAccess_token(),
        			userProfile.getRefresh_token(), userProfile.getCode());
        else
        	mapper.updateUserProfile(userProfile.getId(), userProfile.getAccess_token(),
        			userProfile.getRefresh_token(), userProfile.getCode());   

    }
    
    @GetMapping("/token")
    public String token(@RequestParam("code") String code) {
    	UserProfile userProfile=mapper.getUserToken(code);
    	if(userProfile==null) {
    		return "failed";
    	}else {
    		return userProfile.getId()+"/"+userProfile.getAccess_token()+"/"+userProfile.getRefresh_token();
    	}
    }
    
    @GetMapping("/access")
    public String access(@RequestParam("access_token") String access_token) {
    	return kakao.access(access_token);
    }
    
    @GetMapping("/refresh")
    public String refresh(@RequestParam("refresh_token") String refresh_token) {
    	String id=mapper.getUserId(refresh_token);
    	HashMap<String,String> userInfo=kakao.updateToken(refresh_token);
    	mapper.updateToken(id, userInfo.get("access_token"), userInfo.get(refresh_token));
        return userInfo.get("access_code")+"/"+userInfo.get("refresh_token");
    }
    
    @GetMapping("/code")
    public String getUserInfo(@RequestParam("refresh_token") String refresh_token) {
    	String id=mapper.getUserId(refresh_token);
    	String code=mapper.getUserCode(refresh_token);
    	System.out.println("controller code : "+ code);
        HashMap<String, String> userInfo = kakao.getAccessToken(code);
        mapper.updateUserProfile(id, userInfo.get("access_token"), userInfo.get("refresh_token"), code);
        return userInfo.get("access_code")+"/"+userInfo.get("refresh_token");
    }
    
    @GetMapping("/logout/check")
    public void logoutCheck() {
    	System.out.println("Logout Success");
    }
    @GetMapping("/logout")
    public String logout(@RequestParam("id") String id, @RequestParam("access_token") String access_token) {
    	String empty="";
    	kakao.logout(access_token);
    	mapper.logout(id, empty);
    	return "https://kauth.kakao.com/oauth/logout?client_id=c8f72fe8e065caa66728ff40de53d9fd"
    			+ "&logout_redirect_uri=http://115.21.52.248:8080/kakao/logout/check";
    }
    
    @GetMapping("/test")
    public void test(@RequestParam("id") String id) {
    	kakao.sendMessage(id);
    }
}

//@RestController
//public class UserProfileController {
//	private UserProfileMapper mapper;
//	public UserProfileController(UserProfileMapper mapper) {
//		this.mapper = mapper;
//	}
//		
//	
//	
//	@GetMapping("/user/{id}")
//	public UserProfile getUserProfile(@PathVariable("id") String id) {
//		return mapper.getUserProfile(id);
//	}
//	
//	@GetMapping("/user/all")
//	public List<UserProfile> getUserProfileList(){
//		return mapper.getUserProfileList();
//	}
//	
//	@PostMapping("/user/{id}")
//	public void putUserProfile(@PathVariable("id")String id, @RequestParam("idname")String idname,@RequestParam("phone")String phone,@RequestParam("address")String address) {
//		mapper.insertUserProfile(id, idname, phone, address);
//	}
//	
//	@PutMapping("/user/{id}")
//	public void postUserProfile(@PathVariable("id")String id, @RequestParam("idname")String idname,@RequestParam("phone")String phone,@RequestParam("address")String address) {
//		mapper.updateUserProfile(id, idname, phone, address);
//	}
//	
//	@DeleteMapping("/user/{id}")
//	public void deleteUserProfile(@PathVariable("id") String id) {
//		mapper.deleteUserProfile(id);
//	}
//	
//}
