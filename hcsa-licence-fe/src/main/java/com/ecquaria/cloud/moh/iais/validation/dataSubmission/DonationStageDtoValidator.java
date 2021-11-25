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
        if(arSuperDataSubmissionDto.getPatientInventoryDto()!=null){
            if(donationStageDto.getDonatedType()!=null){
                switch (donationStageDto.getDonatedType()){
                    case DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFreshOocytes();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFrozenOocytes();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO:
                        maxSamplesNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFrozenEmbryos();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM:
                        maxSamplesNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFrozenSperms();
                        break;
                    default:
                }
            }
        }
        if(donationStageDto.getDonatedForResearch()+donationStageDto.getDonatedForTraining()+donationStageDto.getDonatedForTreatment()==0){
            errorMap.put("donatedFor", errMsgErr006);
        }
        if(donationStageDto.getDonatedForResearch()==1){
            if(donationStageDto.getDonResForTreatNum()!=null){
                if(donationStageDto.getDonResForTreatNum()>99||donationStageDto.getDonResForTreatNum()<0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","99");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMap.put("donResForTreatNum", errMsg);
                }
                if(donationStageDto.getDonResForTreatNum()>maxSamplesNum){
                    errorMap.put("donResForTreatNum", "Cannot be greater than number of samples under patient");
                }
            }
            if(donationStageDto.getDonResForCurCenNotTreatNum()!=null){
                if(donationStageDto.getDonResForCurCenNotTreatNum()>99||donationStageDto.getDonResForCurCenNotTreatNum()<0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","99");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMap.put("donResForCurCenNotTreatNum", errMsg);
                }
                if(donationStageDto.getDonResForCurCenNotTreatNum()>maxSamplesNum){
                    errorMap.put("donResForCurCenNotTreatNum", "Cannot be greater than number of samples under patient");
                }
            }

            if(donationStageDto.getDonatedForResearchHescr()+donationStageDto.getDonatedForResearchRrar()+donationStageDto.getDonatedForResearchOther()==0){
                errorMap.put("curCenResType", errMsgErr006);
            }

        }

        if(donationStageDto.getDonatedForTraining()==1){
            if(donationStageDto.getTrainingNum()!=null){
                if(donationStageDto.getTrainingNum()>99||donationStageDto.getTrainingNum()<0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","99");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMap.put("trainingNum", errMsg);
                }
                if(donationStageDto.getTrainingNum()>maxSamplesNum){
                    errorMap.put("trainingNum", "Cannot be greater than number of samples under patient");
                }
            }
            if(donationStageDto.getTreatNum()!=null){
                if(donationStageDto.getTreatNum()>99||donationStageDto.getTreatNum()<0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","99");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMap.put("treatNum", errMsg);
                }
                if(donationStageDto.getTreatNum()>maxSamplesNum){
                    errorMap.put("treatNum", "Cannot be greater than number of samples under patient");
                }
            }else {
                errorMap.put("treatNum", errMsgErr006);
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
