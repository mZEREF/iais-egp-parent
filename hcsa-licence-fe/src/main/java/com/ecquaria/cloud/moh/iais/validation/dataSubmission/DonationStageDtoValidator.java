package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonationStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
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
        String errMsgErr008 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        Map<String, String> eMsg = IaisCommonUtils.genNewHashMap();
        eMsg.put("field","donation");
        String errMsg023 = MessageUtil.getMessageDesc("DS_ERR002",eMsg);
        if (donationStageDto.getLocalOrOversea() == 0 && StringUtil.isEmpty(donationStageDto.getOverseaDonatedCentre())){
            errorMap.put("overseaDonatedCentre", errMsgErr006);
        }


        int maxSamplesNum=100;
        if(arSuperDataSubmissionDto.getArCurrentInventoryDto()!=null){
            if(donationStageDto.getDonatedType()!=null){
                switch (donationStageDto.getDonatedType()){
                    case DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFreshOocyteNum();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenOocyteNum();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenEmbryoNum();
                        break;
                    case DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenSpermNum();
                        errMsg023 = MessageUtil.getMessageDesc("DS_ERR023");
                        break;
                    default:
                }
            }
        }
        if(donationStageDto.getDonatedForResearch()+donationStageDto.getDonatedForTraining()+donationStageDto.getDonatedForTreatment()==0){
            errorMap.put("donatedFor", errMsgErr006);
        }
        if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(donationStageDto.getDonatedType())
                || DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(donationStageDto.getDonatedType())) {
            validateFemaleDonor(request, donationStageDto, errorMap, errMsgErr006,errMsgErr008);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donationStageDto.getDonatedType())){
            validateMaleDonor(request, donationStageDto, errorMap, errMsgErr006,errMsgErr008);
        } else if(DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donationStageDto.getDonatedType())){
            validateFemaleDonor(request, donationStageDto, errorMap, errMsgErr006,errMsgErr008);
            validateMaleDonor(request, donationStageDto, errorMap, errMsgErr006,errMsgErr008);
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
                    errorMap.put("donResForTreatNum", errMsg023);
                }
                if(donationStageDto.getDonResForTreatNumStr().length()>2){
                    String general_err0041= AppValidatorHelper.repLength("This field","2");
                    errorMap.put("donResForTreatNum", general_err0041);
                }
            }else {
                if(StringUtil.isEmpty(donationStageDto.getDonResForTreatNumStr())){
                    errorMap.put("donResForTreatNum", errMsgErr006);
                }else {
                    errorMap.put("donResForTreatNum", errMsgErr008);
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
                    errorMap.put("donResForCurCenNotTreatNum", errMsg023);
                }
                if(donationStageDto.getDonResForCurCenNotTreatNumStr().length()>2){
                    String general_err0041= AppValidatorHelper.repLength("This field","2");
                    errorMap.put("donResForCurCenNotTreatNum", general_err0041);
                }
            }else {
                if(StringUtil.isEmpty(donationStageDto.getDonResForCurCenNotTreatNumStr())){
                    errorMap.put("donResForCurCenNotTreatNum", errMsgErr006);
                }else {
                    errorMap.put("donResForCurCenNotTreatNum", errMsgErr008);
                }
            }

            if(donationStageDto.getDonatedForResearchHescr()+donationStageDto.getDonatedForResearchRrar()+donationStageDto.getDonatedForResearchOther()==0){
                errorMap.put("donatedForResearchBox", errMsgErr006);
            }

        }

        if(donationStageDto.getDonatedForTraining()==1){
            String trainingNumStr = ParamUtil.getString(request, "trainingNum");
            if (StringUtil.isEmpty(trainingNumStr)) {
                errorMap.put("trainingNum", "GENERAL_ERR0006");
            } else if (!StringUtil.isNumber(trainingNumStr)) {
                errorMap.put("trainingNum", "GENERAL_ERR0002");
            } else if(donationStageDto.getTrainingNum()!=null){
                if(donationStageDto.getTrainingNum()>99||donationStageDto.getTrainingNum()<0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","99");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMap.put("trainingNum", errMsg);
                }
                if(donationStageDto.getTrainingNum()>maxSamplesNum){
                    errorMap.put("trainingNum", errMsg023);
                }
            }
        }
        if(donationStageDto.getDonatedForTreatment()==1){
            if (donationStageDto.getIsDirectedDonation() == null) {
                errorMap.put("directedDonation",errMsgErr006);
            } else if (donationStageDto.getIsDirectedDonation() == 1 && donationStageDto.getRecipientNo() == null){
                errorMap.put("recipientNo",errMsgErr006);
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
                    errorMap.put("treatNum", errMsg023);
                }
                if(donationStageDto.getTreatNumStr().length()>2){
                    String general_err0041= AppValidatorHelper.repLength("This field","2");
                    errorMap.put("treatNum", general_err0041);
                }
            }else {
                if(StringUtil.isEmpty(donationStageDto.getTreatNumStr())){
                    errorMap.put("treatNum", errMsgErr006);
                }else {
                    errorMap.put("treatNum", errMsgErr008);
                }
            }
        }


        if(StringUtil.isEmpty(donationStageDto.getDonationReason())){
            errorMap.put("donationReason", errMsgErr006);
        }else if(DataSubmissionConsts.DONATION_REASON_OTHERS.equals(donationStageDto.getDonationReason())){
            if(StringUtil.isEmpty(donationStageDto.getOtherDonationReason())){
                errorMap.put("otherDonationReason", errMsgErr006);
            }else if(donationStageDto.getOtherDonationReason().length()>100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("otherDonationReason", errMsg);
            }
        }
        if(donationStageDto.getDonatedForResearchOther()!=null&&donationStageDto.getDonatedForResearchOther()==1){
            if(StringUtil.isNotEmpty(donationStageDto.getDonatedForResearchOtherType())){
                if(donationStageDto.getDonatedForResearchOtherType().length()>100){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","100");
                    repMap.put("fieldNo","Field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("donatedForResearchOtherType", errMsg);
                }
            }else {
                errorMap.put("donatedForResearchOtherType", errMsgErr006);
            }
        }


        if(donationStageDto.getTotalNum()>maxSamplesNum){
            errorMap.put("totalNum", errMsg023);
        }
        return errorMap;
    }

    private void validateFemaleDonor(HttpServletRequest request, DonationStageDto donationStageDto, Map<String, String> errorMap, String errMsgErr006, String errMsgErr008){
        if (donationStageDto.getIsOocyteDonorPatient() == null){
            errorMap.put("oocyteDonorPatient",errMsgErr006);
        } else if (donationStageDto.getIsOocyteDonorPatient() == 0){
            if (donationStageDto.getIsFemaleIdentityKnown() == null) {
                errorMap.put("isFemaleIdentityKnown",errMsgErr006);
            } else if(donationStageDto.getIsFemaleIdentityKnown() == 1){
                if (donationStageDto.getFemaleIdType() == null){
                    errorMap.put("femaleIdType",errMsgErr006);
                }
            }

        }
        if(donationStageDto.getFemaleDonorSampleCode() == null) {
            errorMap.put("femaleDonorSampleCode",errMsgErr006);
        }
        if(donationStageDto.getFemaleDonorAgeStr() != null) {
            errorMap.put("femaleDonorAge",errMsgErr008);
        } else if(donationStageDto.getFemaleDonorAge() == null) {
            errorMap.put("femaleDonorAge",errMsgErr006);
        }
        if(donationStageDto.getFemaleIdNumber() == null && donationStageDto.getFemaleIdType() != null) {
            if(donationStageDto.getFemaleIdType() == 1) {
                errorMap.put("femaleIdNumber",errMsgErr006);
            } else if(donationStageDto.getFemaleIdType() == 0){
                errorMap.put("femaleIdNumberPassport",errMsgErr006);
            }
        }
    }

    private void validateMaleDonor(HttpServletRequest request, DonationStageDto donationStageDto, Map<String, String> errorMap, String errMsgErr006, String errMsgErr008){
        if (donationStageDto.getIsSpermDonorPatient() == null){
            errorMap.put("spermDonorPatient",errMsgErr006);
        } else if (donationStageDto.getIsSpermDonorPatient() == 0){
            if (donationStageDto.getIsMaleIdentityKnown() == null) {
                errorMap.put("isMaleIdentityKnown",errMsgErr006);
            } else if(donationStageDto.getIsMaleIdentityKnown() == 1){
                if (donationStageDto.getMaleIdType() == null){
                    errorMap.put("maleIdType",errMsgErr006);
                }
            }

        }
        if(donationStageDto.getMaleDonorSampleCode() == null) {
            errorMap.put("maleDonorSampleCode",errMsgErr006);
        }
        if(donationStageDto.getMaleDonorAgeStr() != null) {
            errorMap.put("maleDonorAge",errMsgErr008);
        } else if(donationStageDto.getMaleDonorAge() == null) {
            errorMap.put("maleDonorAge",errMsgErr006);
        }
        if(donationStageDto.getMaleIdNumber() == null && donationStageDto.getMaleIdType() != null) {
            if(donationStageDto.getMaleIdType() == 1) {
                errorMap.put("maleIdNumber",errMsgErr006);
            } else if(donationStageDto.getMaleIdType() == 0){
                errorMap.put("maleIdNumberPassport",errMsgErr006);
            }
        }
    }
}
