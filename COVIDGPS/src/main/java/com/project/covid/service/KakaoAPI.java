package com.project.covid.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.project.covid.model.KakaoOAuthToken;
import com.project.covid.model.KakaoProfile;

@Service
public class KakaoAPI {

	public String getAccessToken(String authorize_code) {

		//http요청을 편하게 함
		RestTemplate rt=new RestTemplate();
		
		//httphead 오브젝트 생성
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=uft-8");
		
		//httpbody
		MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
		params.add("grant_type","authorization_code");
		params.add("client_id","c8f72fe8e065caa66728ff40de53d9fd");
		params.add("redirect_ur","http://localhost:8080/kakao/login");
		params.add("code", authorize_code);
		
		HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
				new HttpEntity<>(params,headers);
		//http 요청하기
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class);
		
		ObjectMapper objectmapper = new ObjectMapper();
		KakaoOAuthToken token = null;
		try {
			token = objectmapper.readValue(response.getBody(), KakaoOAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return token.getAccess_token();
	}
	
	public String getKakaoProfile(String access_Token) {
		RestTemplate rt=new RestTemplate();
		
		//httphead 오브젝트 생성
		HttpHeaders headers= new HttpHeaders();
		headers.add("Authorization", "Bearer" + access_Token);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=uft-8");
		
		
		HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
				new HttpEntity<>(headers);
		//http 요청하기
		ResponseEntity<String> response = rt.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class);
		
		ObjectMapper objectmapper = new ObjectMapper();
		KakaoProfile profile = null;
		try {
			profile = objectmapper.readValue(response.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return profile.toString();
	}
	
	public void logout(String access_Token) {
		RestTemplate rt=new RestTemplate();
		
		//httphead 오브젝트 생성
		HttpHeaders headers= new HttpHeaders();
		headers.add("Authorization", "Bearer" + access_Token);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=uft-8");
		
		
		HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
				new HttpEntity<>(headers);
		//http 요청하기
		ResponseEntity<String> response = rt.exchange(
				"https://kapi.kakao.com/v1/user/logout",
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class);
		
		ObjectMapper objectmapper = new ObjectMapper();
		KakaoProfile profile = null;
		try {
			profile = objectmapper.readValue(response.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}
}
