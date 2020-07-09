package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Company;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CompanyMapper extends Mapper<Company> {
    /**
     *  查找用户的所有公司
     * @param userId
     * @return java.util.List<com.company.project.model.CompanyUser>
     * @throws Exception
     * @author cwf
     * @date 2019/10/10 10:24
     */
    List<Company> findByUserId(long userId);
    /**
     *  查找用户当前登入的公司信息
     * @param userId
     * @return java.util.List<com.company.project.model.CompanyUser>
     * @throws Exception
     * @author cwf
     * @date 2019/10/10 10:24
     */
    List<Company> findNotCompanyUser(long userId);
    
    @Select("select c.id,companyName from tbl_company c,tbl_company_user cu,tbl_user u where c.id=cu.companyId and cu.userId=u.id and u.phone=#{phone}")
    List<Company> findByPhone (String phone);
    @Select("select c.id,companyName,IF(c.addr is null or c.addr='' ,IF(o.addr is null,'',o.addr),c.addr) addr,c.level from tbl_company c left join t_org o on o.id=c.orgId" +
            " where companyName like \"%\"#{companyName}\"%\" limit 10")
    List<Company> findCompany(String companyName);

    @Select("select id,companyName,addr from tbl_company where addr like \"%\"#{addr}\"%\" limit 10")
    List<Company> findCompanyAddr(String addr);
}