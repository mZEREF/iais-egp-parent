package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.apache.commons.lang.StringUtils;
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
        if(blastManagementDto.getEmailAddress() != null){
            for (String item :blastManagementDto.getEmailAddress()
            ) {
                if(!item.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    errMap.put("addr","Please key in a valid email address");
                }
            }
        }
        String step = (String)ParamUtil.getSessionAttr(request,"BlastManagementStep");
        if("fillMessage".equals(step)){
            if(blastManagementDto.getSchedule() == null){
                errMap.put("date","The field is mandatory.");
            }
            String HH = blastManagementDto.getHH();
            String MM = blastManagementDto.getMM();
            if(HH == null){
                errMap.put("HH","The field is mandatory.");
            }else if(!(StringUtils.isNumeric(HH) &&  Integer.parseInt(HH) < 24)){
                errMap.put("HH","Field format is wrong");
            }
            if(MM == null){
                errMap.put("HH","The field is mandatory.");
            }else if(!(StringUtils.isNumeric(MM) &&  Integer.parseInt(MM) < 60)){
                errMap.put("HH","Field format is wrong");
            }
            if(blastManagementDto.getSchedule() != null && HH != null && MM != null) {
                SimpleDateFormat newformat = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
                Date schedule = new Date();
                schedule = blastManagementDto.getSchedule();
                Date now = new Date();
                if (schedule.compareTo(now) < 0) {
                    errMap.put("date", "Send date and time cannot be earlier than now");
                }
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
