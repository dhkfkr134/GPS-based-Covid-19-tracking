package com.project.covid.Controller;

//import java.util.HashMap;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.project.covid.mapper.UserProfileMapper;
//import com.project.covid.model.UserProfile;
//import com.project.covid.service.KakaoAPI;
//
//@Controller
//@RequestMapping(value="/kakao")
//public class LoginController {
//	
//	@Autowired
//    private KakaoAPI kakao;
//	
//	@Autowired
//	private UserProfileMapper mapper;
//    
//    @GetMapping("/connect")
//    public String connect() {
//    	
//    	return "connect";
//    }
//    
//    @GetMapping("/login")
//    public String login(@RequestParam("code") String code) {
//        UserProfile userProfile = new UserProfile();
//    	System.out.println("controller code : "+ code);
//        HashMap<String, String> userInfo = kakao.getAccessToken(code);
//        userProfile.setAccess_token(userInfo.get("access_token"));
//        userProfile.setRefresh_token(userInfo.get("refresh_token"));
//        System.out.println("controller access_token : " + userInfo.get("access_token"));
//        userProfile.setUid(kakao.getUserInfo(userInfo.get("access_token")));
//        System.out.println("login Controller : " + userProfile);
//        return "index";
//    }
//}
