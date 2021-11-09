package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * DonationStageDtoValidator
 *
 * @author junyu
 * @date 2021/11/1
 */
public class DonationStageDtoValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(request);
        DonationStageDto donationStageDto=arSuperDataSubmissionDto.getDonationStageDto();
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        int maxSamplesNum=100;

        if(donationStageDto.getCurCenDonatedNum()!=null){
            if(donationStageDto.getCurCenDonatedNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("curCenDonatedNum", errMsg);
            }
            if(donationStageDto.getCurCenDonatedNum()>maxSamplesNum){
                errorMap.put("curCenDonatedNum", "Cannot be greater than number of samples under patient");
            }
        }

        if(donationStageDto.getOtherCenDonatedNum()!=null){
            if(donationStageDto.getOtherCenDonatedNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherCenDonatedNum", errMsg);
            }
            if(donationStageDto.getOtherCenDonatedNum()>maxSamplesNum){
                errorMap.put("otherCenDonatedNum", "Cannot be greater than number of samples under patient");
            }
            if(donationStageDto.getOtherCenDonatedNum()>0){
                if(donationStageDto.getIsCurCenDonated()==null){
                    errorMap.put("isCurCenDonated", errMsgErr006);
                }else if("Others".equals(donationStageDto.getIsCurCenDonated())){
                    if(StringUtil.isEmpty(donationStageDto.getOtherCenDonatedNum())){
                        errorMap.put("otherDonatedCen", errMsgErr006);
                    }
                }
            }
        }

        if(donationStageDto.getResDonarNum()!=null){
            if(donationStageDto.getResDonarNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("resDonarNum", errMsg);
            }
            if(donationStageDto.getResDonarNum()>maxSamplesNum){
                errorMap.put("resDonarNum", "Cannot be greater than number of samples under patient");
            }
        }

        if(donationStageDto.getCurCenResDonatedNum()!=null){
            if(donationStageDto.getCurCenResDonatedNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("curCenResDonatedNum", errMsg);
            }
            if(donationStageDto.getCurCenResDonatedNum()>maxSamplesNum){
                errorMap.put("curCenResDonatedNum", "Cannot be greater than number of samples under patient");
            }
        }

        if(donationStageDto.getResDonarNum()!=null&&donationStageDto.getResDonarNum()>0){
            if(donationStageDto.getIsCurCenResTypeHescr()+donationStageDto.getIsCurCenResTypeRrar()+donationStageDto.getIsCurCenResTypeOther()==0){
                errorMap.put("curCenResType", errMsgErr006);
            }
        }
        if(donationStageDto.getCurCenResDonatedNum()!=null&&donationStageDto.getCurCenResDonatedNum()>0){
            if(donationStageDto.getIsCurCenResTypeHescr()+donationStageDto.getIsCurCenResTypeRrar()+donationStageDto.getIsCurCenResTypeOther()==0){
                errorMap.put("curCenResType", errMsgErr006);
            }
        }
        if(donationStageDto.getIsCurCenResTypeOther()==1){
            if(StringUtil.isEmpty(donationStageDto.getCurCenOtherResType())){
                errorMap.put("curCenOtherResType", errMsgErr006);
            }
        }
        if(donationStageDto.getOtherCenResDonarNum()!=null){
            if(donationStageDto.getOtherCenResDonarNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherCenResDonarNum", errMsg);
            }
            if(donationStageDto.getOtherCenResDonarNum()>maxSamplesNum){
                errorMap.put("otherCenResDonarNum", "Cannot be greater than number of samples under patient");
            }
            if(donationStageDto.getOtherCenResDonarNum()>0){
                if(donationStageDto.getIsInsSentToCur()==null){
                    errorMap.put("isInsSentToCur", errMsgErr006);
                }else if("Others".equals(donationStageDto.getIsInsSentToCur())){
                    if(StringUtil.isEmpty(donationStageDto.getInsSentToOtherCen())){
                        errorMap.put("insSentToOtherCen", errMsgErr006);
                    }
                }
            }
        }

        if(donationStageDto.getTrainingNum()!=null){
            if(donationStageDto.getTrainingNum()>99){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("trainingNum", errMsg);
            }
            if(donationStageDto.getTrainingNum()>maxSamplesNum){
                errorMap.put("trainingNum", "Cannot be greater than number of samples under patient");
            }
        }

        if(StringUtil.isEmpty(donationStageDto.getDonationReason())){
            errorMap.put("donationReason", errMsgErr006);
        }else if(DataSubmissionConsts.DONATION_REASON_OTHERS.equals(donationStageDto.getDonationReason())){
            if(StringUtil.isEmpty(donationStageDto.getOtherDonationReason())){
                errorMap.put("otherDonationReason", errMsgErr006);
            }
        }

        return errorMap;
    }

}
