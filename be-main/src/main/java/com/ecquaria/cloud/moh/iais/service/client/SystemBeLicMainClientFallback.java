package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * SystemClientBeLicMainFallback
 *
 * @author junyu
 * @date 2020/4/16
 */
public class SystemBeLicMainClientFallback {
    public FeignResponseEntity<ProcessFileTrackDto> isFileExistence(Map<String,String> map){
        return IaisEGPHelper.getFeignResponseEntity("isFileExistence",map);
    }

    public FeignResponseEntity<List<ProcessFileTrackDto>> getFileTypeAndStatus(String processType, String status){
        return IaisEGPHelper.getFeignResponseEntity("getFileTypeAndStatus",processType,status);
    }

    public FeignResponseEntity<ProcessFileTrackDto> updateProcessFileTrack(ProcessFileTrackDto processFileTrackDto){
        return IaisEGPHelper.getFeignResponseEntity("updateProcessFileTrack",processFileTrackDto);
    }

    FeignResponseEntity<String> hclCodeByCode(String code){
        return IaisEGPHelper.getFeignResponseEntity("hclCodeByCode",code);
    }

    public FeignResponseEntity<String> licence( String hciCode,  String serviceCode,
                                         Integer yearLength,  Integer licenceSeq) {
        return IaisEGPHelper.getFeignResponseEntity("licence",hciCode,serviceCode,yearLength,licenceSeq);
    }

    public FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(String id ){
        return IaisEGPHelper.getFeignResponseEntity("loadingEmailTemplate",id);
    }

    public FeignResponseEntity<String> messageID(){
        return IaisEGPHelper.getFeignResponseEntity("messageID");
    }

    public FeignResponseEntity<List<JobRemindMsgTrackingDto>> createJobRemindMsgTrackingDtos(List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtoList){
        return IaisEGPHelper.getFeignResponseEntity("createJobRemindMsgTrackingDtos",jobRemindMsgTrackingDtoList);
    }

    public FeignResponseEntity<JobRemindMsgTrackingDto> updateJobRemindMsgTrackingDto(JobRemindMsgTrackingDto jobRemindMsgTrackingDto){
        return IaisEGPHelper.getFeignResponseEntity("updateJobRemindMsgTrackingDto",jobRemindMsgTrackingDto);
    }

    public FeignResponseEntity<Void> saveSendMailJob(JobRemindMsgTrackingDto jobRemindMsgTrackingDto){
        return IaisEGPHelper.getFeignResponseEntity("saveSendMailJob",jobRemindMsgTrackingDto);
    }

    public FeignResponseEntity<List<JobRemindMsgTrackingDto>>  listJob(){
        return IaisEGPHelper.getFeignResponseEntity("listJob");
    }

    public FeignResponseEntity<Void> getJobRemindMsgTrackingDto( String refNo, String msgKey){
        return IaisEGPHelper.getFeignResponseEntity("getJobRemindMsgTrackingDto",refNo,msgKey);
    }

    public FeignResponseEntity<Void> initCachePostCodes() {
        return IaisEGPHelper.getFeignResponseEntity("initCachePostCodes");
    }
}
