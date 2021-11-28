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
	
	@Select("SELECT mcode,address,inTime,outTime from UserLocation WHERE id=#{id}")
	List<InfectionLoc> getUidLocation(@Param("id")String id);
	
	@Select("SELECT id from UserLocation WHERE (mcode LIKE CONCAT('%/',#{mcode},'/%')or mcode LIKE CONCAT(#{mcode},'/','%') or mcode LIKE CONCAT('%/',#{mcode},''))and (( inTime <= #{inTime} and #{inTime} <= outTime) or (inTime<=#{outTime} and outTime<=#{outTime}))")
	ArrayList<Integer> getUidContactor(@Param("mcode") String mcode,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
	
	@Insert("Insert into UserLocation values(#{id},#{mcode},#{loc},#{inTime},#{outTime},#{address})")
	void putUserIdLocation(@Param("id")String id,@Param("mcode")String mcode,@Param("loc")String loc,@Param("inTime")Date inTime,@Param("outTime")Date outTime,@Param("address")String address);
	
	@Select("SELECT loc from OwnerProfile where hostID=#{hostID}")
	String gethostIdLocation(@Param("hostID")String hostID);
	
	@Select("SELECT id from UserLocation WHERE address=#{loc} and (( inTime <= #{inTime} and #{inTime} <= outTime) or (inTime<=#{outTime} and outTime<=#{outTime}))")
	ArrayList<Integer> getUidContactorLoc(@Param("loc")String loc,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
}

