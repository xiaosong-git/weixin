package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.CompanyUser;

import java.util.List;

public interface CompanyUserMapper extends Mapper<CompanyUser> {

    /**
     *  查找用户的所有公司
     * @param userId 用户id
     * @return java.util.List<com.company.project.model.CompanyUser>
     * @author cwf
     * @date 2019/10/10 10:24
     */
    List<CompanyUser> findByUserId (long userId);

    /**
     * 查找用户
     * @param phone 手机号
     * @return 公司员工
     */
    List<CompanyUser> findByPhone (String phone);


}