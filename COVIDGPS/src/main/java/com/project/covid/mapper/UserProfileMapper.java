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
	
	@Select("SELECT code FROM UserProfile WHERE refresh_token=#{refresh_token}")
	String getUserCode(@Param("refresh_token") String refresh_token);
	
	@Select("SELECT * FROM UserProfile")
	List<UserProfile> getUserProfileList();
	
	@Insert("INSERT UserProfile VALUES(#{id},#{access_token},#{refresh_token},#{code})")
	int insertUserProfile(@Param("id")String id, @Param("access_token") String access_token,
			@Param("refersh_token") String refresh_token, @Param("code") String code);
	
	@Update("UPDATE UserProfile SET access_token=#{access_token}, refresh_token=#{refresh_token}, code=#{code} WHERE id=#{id}")
	int updateUserProfile(@Param("id")String id, @Param("access_token") String access_token,
			@Param("refrsh_token") String refresh_token,@Param("code") String code);
	
	@Update("UPDATE UserProfile SET access_token=#{access_token} WHERE id=#{id}")
	int updateAccess_token(@Param("id")String id, @Param("access_token") String access_token);
	
	@Update("UPDATE UserProfile SET refresh_token=#{refresh_token} WHERE id=#{id}")
	int updateRefresh_token(@Param("id")String id, @Param("refresh_token") String refresh_token);
	
	@Delete("DELETE FROM UserProfile WHERE id=#{id}")
	int deleteUserProfile(@Param("id") String id);
	
	
	
	
}