package com.project.covid.mapper;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.project.covid.model.InfectionLoc;

@Mapper
public interface UserLocationMapper {
	
	@Select("SELECT mcode,loc,inTime,outTime from UserLocation WHERE uid=#{uid}")
	List<InfectionLoc> getUidLocation(@Param("uid")Integer uid);
	
	@Select("SELECT uid from UserLocation WHERE (mcode LIKE CONCAT('%/',#{mcode},'/%')or mcode LIKE CONCAT(#{mcode},'/','%') or mcode LIKE CONCAT('%/',#{mcode},''))and (( inTime <= #{inTime} and #{inTime} <= outTime) or (inTime<=#{outTime} and outTime<=#{outTime}))")
	ArrayList<Integer> getUidContactor(@Param("mcode") String mcode,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
	
	@Insert("Insert into UserLocation values(#{uid},#{mcode},#{loc},#{inTime},#{outTime})")
	void putUserIdLocation(@Param("uid")Integer uid,@Param("mcode")String mcode,@Param("loc")String loc,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
	
	@Select("SELECT loc from OnwerProfile where hostID=#{hostID}")
	String gethostIdLocation(@Param("hostID")Integer hostID);
	
	@Select("SELECT uid from UserLocation WHERE loc=#{loc} and (( inTime <= #{inTime} and #{inTime} <= outTime) or (inTime<=#{outTime} and outTime<=#{outTime}))")
	ArrayList<Integer> getUidContactorLoc(@Param("loc")String loc,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
}

