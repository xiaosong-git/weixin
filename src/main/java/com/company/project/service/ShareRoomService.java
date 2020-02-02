package com.company.project.service;
import com.company.project.model.ShareRoom;
import com.company.project.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2019/09/22.
 */
public interface ShareRoomService extends Service<ShareRoom> {

    List<ShareRoom> findByOrg(String orgCode);
}
