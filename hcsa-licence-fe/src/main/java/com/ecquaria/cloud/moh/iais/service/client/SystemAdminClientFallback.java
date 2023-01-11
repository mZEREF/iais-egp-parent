package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.inputFiles.InputFilesDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * SystemAdminClientFallback
 *
 * @author suocheng
 * @date 12/5/2019
 */
public class SystemAdminClientFallback implements SystemAdminClient{


    @Override
    public FeignResponseEntity<String> draftNumber(String applicationType) {
        return IaisEGPHelper.getFeignResponseEntity("draftNumber",applicationType);
    }

    @Override
    public FeignResponseEntity<String> submissionID(String submissionType) {
        return IaisEGPHelper.getFeignResponseEntity("submissionID",submissionType);
    }

    @Override
    public FeignResponseEntity<String> applicationNumber(String applicationType) {
        return IaisEGPHelper.getFeignResponseEntity("applicationNumber",applicationType);
    }

    @Override
    public FeignResponseEntity<MsgTemplateDto> getMsgTemplate(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getMsgTemplate",id);
    }

    @Override
    public FeignResponseEntity<MasterCodeDto> getMasterCodeById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getMasterCodeById",id);
    }

    @Override
    public FeignResponseEntity<Map<String, String>> saveEicTrack(List<EicRequestTrackingDto> dtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveEicTrack",dtoList);
    }

    @Override
    public FeignResponseEntity<String> createMessageId() {
        return IaisEGPHelper.getFeignResponseEntity("createMessageId");
    }

    @Override
    public FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(String moduleName) {
        return IaisEGPHelper.getFeignResponseEntity("getPendingRecords",moduleName);
    }

    @Override
    public FeignResponseEntity<EicRequestTrackingDto> getByRefNum(String refNum) {
        return IaisEGPHelper.getFeignResponseEntity("getByRefNum",refNum);
    }

    @Override
    public FeignResponseEntity<List<String>> getMsgTemplateReceiptToCc(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getMsgTemplateReceiptToCc",id);
    }

    @Override
    public FeignResponseEntity<Void> saveInputFiles(InputFilesDto inputFilesDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
