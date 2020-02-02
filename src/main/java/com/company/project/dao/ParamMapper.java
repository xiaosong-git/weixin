package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ParamMapper extends Mapper<Param> {

    @Select("select * from "+ TableList.PARAM +" where paramName = #{paramName}")
    List<Param> findValueByNameFromDB(String paramName);
}