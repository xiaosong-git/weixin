package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.Org;
import org.apache.ibatis.annotations.Select;

public interface OrgMapper extends Mapper<Org> {
    @Select(  "select org_code from " + TableList.ORG + " o left join "+TableList.COMPANY+" c " +
            "on c.orgId=o.id " +
            "left join " +TableList.USER+" u on u.companyId=c.id"+
            " where u.id=#{userId} limit 1")
    String findOrgCodeByUserId(Long userId);
}