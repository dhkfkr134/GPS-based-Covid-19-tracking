package com.project.covid.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.project.covid.model.OwnerProfile;
import com.project.covid.model.UserProfile;

public interface OwnerProfileMapper {
	
	@Select("SELECT * FROM OwnerProfile WHERE hostID=#{hostID}")
	OwnerProfile getOwnerProfile(@Param("hostID")String hostID);
	
	@Select("SELECT * FROM OwnerProfile WHERE code=#{code}")
	OwnerProfile getOwnerToken(@Param("code")String code);
	
	@Select("SELECT hostID FROM UserProfile WHERE refresh_token=#{refresh_token}")
	String getOwnerId(@Param("refresh_token") String refresh_token);
	
	@Insert("INSERT OwnerProfile VALUES(#{hostID},#{access_token},#{refresh_token},#{code}, #{loc})")
	int insertOwnerProfile(@Param("hostID")String hostID, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token, @Param("code") String code,
			@Param("loc") String loc);
	
	@Update("UPDATE OwnerProfile SET access_token=#{access_token}, refresh_token=#{refresh_token}, code=#{code}, loc=#{loc} WHERE hostID=#{hostID}")
	int updateOwnerProfile(@Param("hostID")String hostID, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token,@Param("code") String code,
			@Param("loc") String loc);
	
	@Update("UPDATE OwnerProfile SET loc=#{loc} WHERE hostID=#{hostID}")
	int updateLocation(@Param("hostID")String hostID, @Param("loc") String loc);

	@Update("UPDATE OwnerProfile SET access_token=#{access_token}, refresh_token=#{refresh_token} WHERE hostID=#{hostID}")
	int updateToken(@Param("hostID")String hostID, @Param("access_token") String access_token,
			@Param("refresh_token") String refresh_token);
	
	@Update("UPDATE OwnerProfile SET code=#{empty} WHERE hostID=#{hostID}")
	int logout(@Param("hostID")String hostID, @Param("empty") String empty);
}
