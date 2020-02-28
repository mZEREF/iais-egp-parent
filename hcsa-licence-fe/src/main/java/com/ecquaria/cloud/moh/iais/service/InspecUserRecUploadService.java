package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
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
      * @Param: loginContext, auditTrailStr, inspecUserRecUploadDto
      * @return: void
      * @Descripation: submit Rectification By User
      */
    void submitRecByUser(LoginContext loginContext, InspecUserRecUploadDto inspecUserRecUploadDto);

    /**
      * @author: shicheng
      * @Date 2020/2/24
      * @Param: appPremCorrId
      * @return: ApplicationDto
      * @Descripation: get Application By CorrId
      */
    ApplicationDto getApplicationByCorrId(String appPremCorrId);

    /**
      * @author: shicheng
      * @Date 2020/2/27
      * @Param: inspecUserRecUploadDto auditTrailStr
      * @return: InspecUserRecUploadDto
      * @Descripation: save File Report Get File Id
      */
    InspecUserRecUploadDto saveFileReportGetFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String auditTrailStr);

    /**
      * @author: shicheng
      * @Date 2020/2/27
      * @Param: inspecUserRecUploadDto
      * @return: InspecUserRecUploadDto
      * @Descripation: getNcItemData
      */
    InspecUserRecUploadDto getNcItemData(InspecUserRecUploadDto inspecUserRecUploadDto);

    /**
      * @author: shicheng
      * @Date 2020/2/28
      * @Param: inspecUserRecUploadDto, removeId
      * @return: InspecUserRecUploadDto
      * @Descripation: removeFileByFileId
      */
    InspecUserRecUploadDto removeFileByFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String removeId);
}
