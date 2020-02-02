package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.CompanyUser;
import com.company.project.model.ShareRoom;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CompanyUserMapper extends Mapper<CompanyUser> {

    /**
     *  查找用户的所有公司
     * @param userId
     * @return java.util.List<com.company.project.model.CompanyUser>
     * @throws Exception
     * @author cwf
     * @date 2019/10/10 10:24
     */
    List<CompanyUser> findByUserId (long userId);
}