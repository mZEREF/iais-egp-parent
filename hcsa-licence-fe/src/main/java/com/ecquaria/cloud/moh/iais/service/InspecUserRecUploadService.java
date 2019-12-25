package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/23 15:25
 **/
public interface InspecUserRecUploadService {

    /**
      * @author: shicheng
      * @Date 2019/12/23
      * @Param: appNo
      * @return: ChecklistItemDto
      * @Descripation: get Checklist Question and Clause
      */
    List<ChecklistItemDto> getQuesAndClause(String appNo);

    /**
      * @author: shicheng
      * @Date 2019/12/24
      * @Param: loginContext, auditTrailStr, inspecUserRecUploadDtos
      * @return: void
      * @Descripation: submit Rectification By User
      */
    void submitRecByUser(LoginContext loginContext, String auditTrailStr, List<InspecUserRecUploadDto> inspecUserRecUploadDtos);
}
