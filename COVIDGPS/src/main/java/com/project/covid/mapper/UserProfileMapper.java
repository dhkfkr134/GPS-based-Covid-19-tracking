package com.project.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.project.covid.model.UserProfile;



@Mapper
public interface UserProfileMapper {
	
	@Select("SELECT * FROM UserProfile WHERE id=#{id}")
	UserProfile getUserProfile(@Param("id")String id);
	
	@Select("SELECT * FROM UserProfile WHERE code=#{code}")
	UserProfile getUserToken(@Param("code")String code);
	
	@Select("SELECT code FROM UserProfile WHERE refresh_token=#{refresh_token}")
	String getUserCode(@Param("refresh_token") String refresh_token);
	
	@Select("SELECT id FROM UserProfile WHERE refresh_token=#{refresh_token}")
	String getUserId(@Param("refresh_token") String refresh_token);
	
	@Select("SELECT * FROM UserProfile")
	List<UserProfile> getUserProfileList();
	
	@Insert("INSERT UserProfile VALUES(#{id},#{access_token},#{refresh_token},#{code})")
	int insertUserProfile(@Param("id")String id, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token, @Param("code") String code);
	
	@Update("UPDATE UserProfile SET access_token=#{access_token}, refresh_token=#{refresh_token}, code=#{code} WHERE id=#{id}")
	int updateUserProfile(@Param("id")String id, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token,@Param("code") String code);
	
	@Update("UPDATE UserProfile SET code=#{empty} WHERE id=#{id}")
	int logout(@Param("id")String id, @Param("empty") String empty);
	
	@Update("UPDATE UserProfile SET access_token=#{access_token}, refresh_token=#{refresh_token} WHERE id=#{id}")
	int updateToken(@Param("id")String id, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token);
	
	@Delete("DELETE FROM UserProfile WHERE id=#{id}")
	int deleteUserProfile(@Param("id") String id);
	
	
	
	
}