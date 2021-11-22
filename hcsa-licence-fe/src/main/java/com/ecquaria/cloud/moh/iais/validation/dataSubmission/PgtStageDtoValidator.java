package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * PgtDtoValidator
 *
 * @author junyu
 * @date 2021/10/28
 */
@Component
@Slf4j
public class PgtStageDtoValidator implements CustomizeValidator {


    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(request);
        PgtStageDto pgtStageDto=arSuperDataSubmissionDto.getPgtStageDto();
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");

        if(pgtStageDto.getIsPgtA()+pgtStageDto.getIsOtherPgt()+pgtStageDto.getIsPgtM()+pgtStageDto.getIsPtt()+pgtStageDto.getIsPgtSr()==0){
            errorMap.put("pgt_type", errMsgErr006);
        }

        if( pgtStageDto.getIsPgtM()==1){
            if(pgtStageDto.getIsPgtMWithHla()+pgtStageDto.getIsPgtMDsld()+pgtStageDto.getIsPgtMNon()==0){
                errorMap.put("pgt_m_performed", errMsgErr006);
            }

            if(pgtStageDto.getIsPgtMDsld()!=null&&pgtStageDto.getIsPgtMDsld()==1){
                if(StringUtil.isEmpty(pgtStageDto.getPgtMRefNo())){
                    errorMap.put("pgt_m_ref_no", errMsgErr006);
                }else  if(pgtStageDto.getPgtMRefNo().length()==19) {
                    String PGT_M_REF_NO="MHXX:0X/0X-XX(XXXX)";
                    for (int i=0;i<19;i++
                    ) {
                        char vad=PGT_M_REF_NO.charAt(i);
                        char refNo=pgtStageDto.getPgtMRefNo().charAt(i);
                        if(vad!='X'&&vad!=refNo){
                            errorMap.put("pgt_m_ref_no", MessageUtil.getMessageDesc("SYSPAM_ERROR0008"));
                        }
                    }
                }else {
                    errorMap.put("pgt_m_ref_no", MessageUtil.getMessageDesc("SYSPAM_ERROR0008"));
                }
            }

            if(StringUtil.isEmpty(pgtStageDto.getPgtMCondition())){
                errorMap.put("PgtMCondition", errMsgErr006);
            }else  if(pgtStageDto.getPgtMCondition().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("PgtMCondition", errMsg);
            }
        }
        if(pgtStageDto.getIsPgtSr()==1){
            if(StringUtil.isEmpty(pgtStageDto.getPgtSrCondition())){
                errorMap.put("PgtSrCondition", errMsgErr006);
            }else  if(pgtStageDto.getPgtSrCondition().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("PgtSrCondition", errMsg);
            }
        }

        if(pgtStageDto.getIsOtherPgt()==1){
            if(StringUtil.isEmpty(pgtStageDto.getOtherPgt())){
                errorMap.put("otherPgt", errMsgErr006);
            }else  if(pgtStageDto.getOtherPgt().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherPgt", errMsg);
            }
        }

        if(pgtStageDto.getIsPgtA()==1){
            if(pgtStageDto.getIsPgtAAma()+pgtStageDto.getIsPgtATomrif()+pgtStageDto.getIsPgtATomrpl()==0){
                errorMap.put("PerformedBecause", errMsgErr006);
            }
            if(StringUtil.isEmpty(pgtStageDto.getPgtAResult())){
                errorMap.put("PGTAResult", errMsgErr006);
            }else if( "Abnormal".equals(pgtStageDto.getPgtAResult())){
                if(StringUtil.isEmpty(pgtStageDto.getPgtACondition())){
                    errorMap.put("PgtACondition", errMsgErr006);
                }else  if(pgtStageDto.getPgtACondition().length()>20){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","20");
                    repMap.put("fieldNo","Field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("PgtACondition", errMsg);
                }
            }

        }

        if(pgtStageDto.getIsPtt()==1){
            if(StringUtil.isEmpty(pgtStageDto.getPttCondition())){
                errorMap.put("pttCondition", errMsgErr006);
            }else  if(pgtStageDto.getPttCondition().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("pttCondition", errMsg);
            }

        }


        if(StringUtil.isEmpty(pgtStageDto.getIsEmbryosBiopsiedLocal())){
            errorMap.put("isEmbryosBiopsiedLocal", errMsgErr006);
        }else if( "Others".equals(pgtStageDto.getIsEmbryosBiopsiedLocal())){
            if(StringUtil.isEmpty(pgtStageDto.getOtherEmbryosBiopsiedAddr())){
                errorMap.put("otherEmbryosBiopsiedAddr", errMsgErr006);
            }else  if(pgtStageDto.getOtherEmbryosBiopsiedAddr().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherEmbryosBiopsiedAddr", errMsg);
            }
        }
        if(StringUtil.isEmpty(pgtStageDto.getIsBiopsyLocal())){
            errorMap.put("isBiopsyLocal", errMsgErr006);
        }else if( "Others".equals(pgtStageDto.getIsBiopsyLocal())){
            if(StringUtil.isEmpty(pgtStageDto.getOtherBiopsyAddr())){
                errorMap.put("otherBiopsyAddr", errMsgErr006);
            }else  if(pgtStageDto.getOtherBiopsyAddr().length()>20){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherBiopsyAddr", errMsg);
            }
        }

        return errorMap;
    }
}
