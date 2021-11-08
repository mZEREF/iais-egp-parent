package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * DisposalStageDtoValidator
 *
 * @author junyu
 * @date 2021/11/5
 */
public class DisposalStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(request);
        DisposalStageDto disposalStageDto=arSuperDataSubmissionDto.getDisposalStageDto();
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if(disposalStageDto.getDisposedTypeDisplay()!=null&&(disposalStageDto.getTotalNum()==null||disposalStageDto.getTotalNum()==0)){
            errorMap.put("totalNum", "One data item in the list must be entered");

        }
        int maxSamplesNum=100;

        if(disposalStageDto.getImmature()!=null){
            if(disposalStageDto.getImmature()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("immature", errMsg);
            }
            if(disposalStageDto.getImmature()>maxSamplesNum){
                errorMap.put("immature", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getAbnormallyFertilised()!=null){
            if(disposalStageDto.getAbnormallyFertilised()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("abnormallyFertilised", errMsg);
            }
            if(disposalStageDto.getAbnormallyFertilised()>maxSamplesNum){
                errorMap.put("abnormallyFertilised", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getUnfertilised()!=null){
            if(disposalStageDto.getUnfertilised()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("unfertilised", errMsg);
            }
            if(disposalStageDto.getUnfertilised()>maxSamplesNum){
                errorMap.put("unfertilised", "Cannot be greater than number of samples under patient");
            }
        }


        if(disposalStageDto.getAtretic()!=null){
            if(disposalStageDto.getAtretic()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("atretic", errMsg);
            }
            if(disposalStageDto.getAtretic()>maxSamplesNum){
                errorMap.put("atretic", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getDamaged()!=null){
            if(disposalStageDto.getDamaged()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("damaged", errMsg);
            }
            if(disposalStageDto.getDamaged()>maxSamplesNum){
                errorMap.put("damaged", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getLysedOrDegenerated()!=null){
            if(disposalStageDto.getLysedOrDegenerated()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("lysedOrDegenerated", errMsg);
            }
            if(disposalStageDto.getLysedOrDegenerated()>maxSamplesNum){
                errorMap.put("lysedOrDegenerated", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getUnhealthyNum()!=null){
            if(disposalStageDto.getUnhealthyNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("unhealthyNum", errMsg);
            }
            if(disposalStageDto.getUnhealthyNum()>maxSamplesNum){
                errorMap.put("unhealthyNum", "Cannot be greater than number of samples under patient");
            }
        }

        if(disposalStageDto.getOtherDiscardedNum()!=null){
            if(disposalStageDto.getOtherDiscardedNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherDiscardedNum", errMsg);
            }
            if(disposalStageDto.getOtherDiscardedNum()>maxSamplesNum){
                errorMap.put("otherDiscardedNum", "Cannot be greater than number of samples under patient");
            }
            if(disposalStageDto.getOtherDiscardedNum()>0){

                if(StringUtil.isEmpty(disposalStageDto.getOtherDiscardedReason())){
                    errorMap.put("otherDiscardedReason", errMsgErr006);
                }else if(disposalStageDto.getOtherDiscardedReason().length()>20){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","20");
                    repMap.put("fieldNo","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("otherDiscardedReason", errMsg);
                }

            }
        }

        return errorMap;
    }
}
