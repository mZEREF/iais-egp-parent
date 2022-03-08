package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TransferInOutStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        String oocyteNum  = ParamUtil.getRequestString(request,"oocyteNum");
        String embryoNum  = ParamUtil.getRequestString(request,"embryoNum");
        String spermVialsNum  = ParamUtil.getRequestString(request,"spermVialsNum");
        String transferType = transferInOutStageDto.getTransferType();
        if(IaisCommonUtils.isEmpty(transferredList)){
            errorMap.put("transferredList","GENERAL_ERR0006");
        }
        if (StringUtil.isEmpty(transferType)){
            errorMap.put("transferType","GENERAL_ERR0006");
        }
        if(!StringUtil.isEmpty(transferType) && transferType.equals("in")){
            String transInFromHciCode = transferInOutStageDto.getTransInFromHciCode();
            if (StringUtil.isEmpty(transInFromHciCode)) {
                errorMap.put("transInFromHciCode", "GENERAL_ERR0006");
            }
            if (!StringUtil.isEmpty(transInFromHciCode) && transInFromHciCode.equals("Others")) {
                if (StringUtil.isEmpty(transferInOutStageDto.getTransInFromOthers())) {
                    errorMap.put("transInFromOthers", "GENERAL_ERR0006");
                }
            }
        }
        if(!StringUtil.isEmpty(transferType) && transferType.equals("out")){
            String transOutToHciCode = transferInOutStageDto.getTransOutToHciCode();
            if (StringUtil.isEmpty(transOutToHciCode)) {
                errorMap.put("transOutToHciCode", "GENERAL_ERR0006");
            }
            if (!StringUtil.isEmpty(transOutToHciCode) && transOutToHciCode.equals("Others")) {
                if (StringUtil.isEmpty(transferInOutStageDto.getTransOutToOthers())) {
                    errorMap.put("transOutToOthers", "GENERAL_ERR0006");
                } else if (transferInOutStageDto.getTransOutToOthers().length() > 20) {
                    String general_err0041 = NewApplicationHelper.repLength("Transfer Out To (Others)", "20");
                    errorMap.put("transOutToOthers", general_err0041);
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(transferredList)){
           for (String transferred :transferredList){
                if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)){
                  if(StringUtil.isEmpty(transferInOutStageDto.getOocyteNum())){
                      errorMap.put("oocyteNum", "GENERAL_ERR0006");
                    }
                }
                if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)){
                    if(StringUtil.isEmpty(transferInOutStageDto.getEmbryoNum())){
                        errorMap.put("embryoNum", "GENERAL_ERR0006");
                    }
                }
               if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)){
                   if(StringUtil.isEmpty(transferInOutStageDto.getSpermVialsNum())){
                       errorMap.put("spermVialsNum", "GENERAL_ERR0006");
                   }
               }
           }
        }
        if(!StringUtil.isEmpty(oocyteNum)&&!StringUtil.isNumber(oocyteNum)){
            errorMap.put("oocyteNum", "GENERAL_ERR0002");
        }
        if(!StringUtil.isEmpty(embryoNum)&&!StringUtil.isNumber(embryoNum)){
            errorMap.put("embryoNum", "GENERAL_ERR0002");
        }
        if(!StringUtil.isEmpty(spermVialsNum)&&!StringUtil.isNumber(spermVialsNum)){
            errorMap.put("spermVialsNum", "GENERAL_ERR0002");
        }
        return errorMap;
    }
}
