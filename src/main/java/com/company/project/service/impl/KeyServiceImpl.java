package com.company.project.service.impl;

import com.company.project.compose.TableList;
import com.company.project.dao.KeyMapper;
import com.company.project.model.Key;
import com.company.project.service.KeyService;
import com.company.project.core.AbstractService;
import com.company.project.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2019/10/14.
 */
@Service
@Transactional
public class KeyServiceImpl extends AbstractService<Key> implements KeyService {
    @Resource
    private KeyMapper  tblKeyMapper;

    @Override
    public String findKeyByStatus(String cstatus) throws  Exception{
        String key = null;
        //redis修改，原dbNum=8 现在dbNum=32
        key = RedisUtil.getStrVal("key_workKey",32);
        if(key == null){
            key =  findKeyFromDB(cstatus);
            if(key != null){
                //redis修改，原dbNum=8 现在dbNum=32
                RedisUtil.setStr("key_workKey",key, 32, null);
            }
        }
        return key;
    }

    /**
     * 从数据库中获取密钥
     * @param cstatus
     * @return
     */
    private String findKeyFromDB(String cstatus){
        if(!StringUtils.isEmpty(cstatus)){
            Condition condition=new Condition(Key.class);
            condition.createCriteria().andEqualTo("cstatus",cstatus);
            condition.selectProperties("workKey");
            List<Key> key = findByCondition(condition);
            if(key != null){
                return key.get(0).getWorkkey();
            }
        }
        return null;
    }

}
