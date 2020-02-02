package com.company.project.service.impl;

import com.company.project.dao.ShareRoomMapper;
import com.company.project.model.ShareRoom;
import com.company.project.service.ShareRoomService;
import com.company.project.core.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;



/**
 * Created by CodeGenerator on 2019/09/22.
 */
@Service
@Transactional
public class ShareRoomServiceImpl extends AbstractService<ShareRoom> implements ShareRoomService {
    @Resource
    private ShareRoomMapper tblShareRoomMapper;
    private final Logger logger = LoggerFactory.getLogger(ShareRoomServiceImpl.class);

    @Override
    public List<ShareRoom> findByOrg(String orgCode) {

        List<ShareRoom> byCondition = tblShareRoomMapper.findByOrg(orgCode);
       logger.info("list {}",byCondition);
        return byCondition;
    }
}
