package com.project.covid.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.project.covid.model.InfectionLoc;

@Mapper
public interface UserLocationMapper {
	@Select("SELECT mcode,inTime,outTime from UserLocation WHERE uid=#{uid}")
	List<InfectionLoc> getUidLocation(@Param("uid")Integer uid);
	
	@Select("SELECT uid from UserLocation WHERE mcode LIKE CONCAT('%',#{mcode},'%') and (( inTime <= #{inTime} and #{inTime} <= outTime) or (inTime<=#{outTime} and outTime<={outTime}))")
	List<Integer> getUidContractor(@Param("mcode") String mcode,@Param("inTime")Date inTime,@Param("outTime")Date outTime);
}

