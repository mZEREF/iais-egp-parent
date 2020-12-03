package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
@Component
public class BlastValidate implements CustomizeValidator {
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request, "blastManagementDto");
        String step = (String)ParamUtil.getSessionAttr(request,"BlastManagementStep");
        if("fillMessage".equals(step)){
            if(blastManagementDto.getSchedule() == null){
                errMap.put("date",MessageUtil.replaceMessage("GENERAL_ERR0006","Send date and time","field"));
            }
            String HH = blastManagementDto.getHH();
            String MM = blastManagementDto.getMM();
            if(HH == null){
                errMap.put("HH",MessageUtil.replaceMessage("GENERAL_ERR0006","HH","field"));
            }
            if(MM == null){
                errMap.put("HH",MessageUtil.replaceMessage("GENERAL_ERR0006","MM","field"));
            }
            if(blastManagementDto.getSchedule() != null && HH != null && MM != null) {
                SimpleDateFormat newformat = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
                Date schedule = new Date();
                schedule = blastManagementDto.getSchedule();
                Date now = new Date();
                if (schedule.compareTo(now) < 0) {
                    errMap.put("date", MessageUtil.getMessageDesc("EMM_ERR007"));
                }
            }
        }
        if("saveSms".equals(step)){
            if(blastManagementDto.getSubject() == null){
                errMap.put("subject",MessageUtil.replaceMessage("GENERAL_ERR0006","Header","field"));
            }
            if(blastManagementDto.getDistributionId() == null){
                errMap.put("distribution",MessageUtil.replaceMessage("GENERAL_ERR0006","Recipients","field"));
            }
            if(blastManagementDto.getMsgContent() == null){
                errMap.put("msgContent",MessageUtil.replaceMessage("GENERAL_ERR0006","Text","field"));
            }
        }
        if(blastManagementDto.getAttachmentDtos() != null && blastManagementDto.getAttachmentDtos().size() > 0){
            double filesSize = 0;
            for (AttachmentDto item:blastManagementDto.getAttachmentDtos()
            ) {
                if (Double.parseDouble(item.getDocSize()) <= 0){
                    errMap.put(FILE_UPLOAD_ERROR, "GENERAL_ERR0004");
                }
                filesSize += Double.parseDouble(item.getDocSize());
            }

            double size = (double)(filesSize / 0x400) / (double) 0x400;
            if (Math.ceil(size) > 0x05){
                errMap.put(FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(5),"sizeMax"));
            }
        }

        return errMap;
    }
}
