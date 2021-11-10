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
	
	@Select("SELECT * FROM UserProfile")
	List<UserProfile> getUserProfileList();
	
	@Insert("INSERT UserProfile VALUES(#{id},#{idname},#{phone},#{address})")
	int insertUserProfile(@Param("id")String id, @Param("idname") String idname,@Param("phone")String phone ,@Param("address") String address);
	
	@Update("UPDATE UserProfile SET name=#{idname},phone=#{phone},address=#{address} WHERE id=#{id}")
	int updateUserProfile(@Param("id")String id, @Param("idname") String idname,@Param("phone")String phone ,@Param("address") String address);
	
	@Delete("DELETE FROM UserProfile WHERE id=#{id}")
	int deleteUserProfile(@Param("id") String id);
	
	
	
	
}