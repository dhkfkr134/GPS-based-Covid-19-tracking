package com.project.covid.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.covid.mapper.OwnerProfileMapper;
import com.project.covid.model.OwnerProfile;
import com.project.covid.model.UserProfile;
import com.project.covid.service.UserProfileService;

@RestController
@RequestMapping("/host")
public class OwnerProfileController {

	@Autowired
	OwnerProfileMapper mapper;

	@Autowired
	UserProfileService kakao;

	@GetMapping("/connect")
	public String connect() {
		return "https://kauth.kakao.com/oauth/authorize?client_id=c8f72fe8e065caa66728ff40de53d9fd"
				+ "&redirect_uri=http://115.21.52.248:8080/host/login&response_type=code";
	}

	@GetMapping("/login")
	public void login(@RequestParam("code") String code) {
		OwnerProfile ownerProfile = new OwnerProfile();
		ownerProfile.setCode(code);
		System.out.println("controller code : " + code);
		HashMap<String, String> ownerInfo = kakao.getHostAccessToken(code);
		ownerProfile.setAccess_token(ownerInfo.get("access_token"));
		ownerProfile.setRefresh_token(ownerInfo.get("refresh_token"));
		System.out.println("controller access_token : " + ownerInfo.get("access_token"));
		ownerProfile.setHostID(kakao.getUserInfo(ownerInfo.get("access_token")));
		System.out.println("login Controller : " + ownerProfile);
		OwnerProfile temp = mapper.getOwnerProfile(ownerProfile.getHostID());
		if (temp == null)
			mapper.insertOwnerProfile(ownerProfile.getHostID(), ownerProfile.getAccess_token(),
					ownerProfile.getRefresh_token(), ownerProfile.getCode(), "");
		else
			mapper.updateOwnerProfile(ownerProfile.getHostID(), ownerProfile.getAccess_token(),
					ownerProfile.getRefresh_token(), ownerProfile.getCode(), "");
	}

	@GetMapping("/update")
	public void updateLocation(@RequestParam String hostID, @RequestParam String loc) {
		mapper.updateLocation(hostID, loc);
	}

	@GetMapping("/token")
	public String token(@RequestParam("code") String code) {
		OwnerProfile ownerProfile = mapper.getOwnerToken(code);
		if (ownerProfile == null) {
			return "failed";
		} else {
			return ownerProfile.getHostID() + " / " + ownerProfile.getAccess_token() + " / " + ownerProfile.getRefresh_token();
		}
	}

	@GetMapping("/access")
	public String access(@RequestParam("access_token") String access_token) {
		return kakao.access(access_token);
	}

	@GetMapping("/refresh")
	public String refresh(@RequestParam("refresh_token") String refresh_token) {
		String id = mapper.getOwnerId(refresh_token);
		HashMap<String, String> ownerInfo = kakao.updateToken(refresh_token);
		mapper.updateToken(id, ownerInfo.get("access_token"), ownerInfo.get(refresh_token));
		return ownerInfo.get("access_code") + " / " + ownerInfo.get("refresh_token");
	}

	@GetMapping("/logout/check")
	public void logoutCheck() {
		System.out.println("Logout Success");
	}

	@GetMapping("/logout")
	public String logout(@RequestParam("id") String id, @RequestParam("access_token") String access_token) {
		String empty = "";
		kakao.logout(access_token);
		mapper.logout(id, empty);
		return "https://kauth.kakao.com/oauth/logout?client_id=c8f72fe8e065caa66728ff40de53d9fd"
				+ "&logout_redirect_uri=http://115.21.52.248:8080/host/logout/check";
	}
}
