package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDefectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OutcomePregnancyDelegator
 *
 * @author jiawei
 * @date 2021/11/2
 */

@Delegator("outcomePregnancyDelegator")
public class OutcomePregnancyDelegator extends CommonDelegator {
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Outcome of Pregnancy Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = arSuperDataSubmissionDto.getPregnancyOutcomeStageDto();
        if (pregnancyOutcomeStageDto == null) {
            pregnancyOutcomeStageDto = new PregnancyOutcomeStageDto();
            pregnancyOutcomeStageDto.setBabyDetailsUnknown(false);
            pregnancyOutcomeStageDto.setBirthPlace("Local Birth");
            pregnancyOutcomeStageDto.setWasSelFoeReduCarryOut(1);
            pregnancyOutcomeStageDto.setL2CareBabyNum(0);
            pregnancyOutcomeStageDto.setL3CareBabyNum(0);
            arSuperDataSubmissionDto.setPregnancyOutcomeStageDto(pregnancyOutcomeStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }

        List<SelectOption> firstUltrasoundOrderShowSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ORDER_IN_ULTRASOUND);
        ParamUtil.setRequestAttr(bpc.request, "firstUltrasoundOrderShowSelectOption", firstUltrasoundOrderShowSelectOption);

        List<SelectOption> pregnancyOutcomeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_OUTCOME_OFPREGNANCY);
        ParamUtil.setRequestAttr(bpc.request, "pregnancyOutcomeSelectOption", pregnancyOutcomeSelectOption);

        List<SelectOption> deliveryModeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MODE_OF_DELIVERY);
        ParamUtil.setRequestAttr(bpc.request, "deliveryModeSelectOption", deliveryModeSelectOption);

        List<SelectOption> birthWeightSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BABY_BIRTH_WEIGHT);
        ParamUtil.setRequestAttr(bpc.request, "birthWeightSelectOption", birthWeightSelectOption);

        List<SelectOption> transferNumSelectOption = IaisCommonUtils.genNewArrayList();
        String[] options = new String[]{"0", "1", "2", "3", "4", "5", "6"};
        for (String item : options) {
            transferNumSelectOption.add(new SelectOption(item, item));
        }
        ParamUtil.setRequestAttr(bpc.request, "NICUCareBabyNumSelectOption", transferNumSelectOption);
        ParamUtil.setRequestAttr(bpc.request, "l2CareBabyNumSelectOption", transferNumSelectOption);
        ParamUtil.setRequestAttr(bpc.request, "l3CareBabyNumSelectOption", transferNumSelectOption);

        initBabyDefect(arSuperDataSubmissionDto.getPregnancyOutcomeStageDto(), bpc.request);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        initBabyDefect(arSuperDataSubmissionDto.getPregnancyOutcomeStageDto(), bpc.request);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = arSuperDataSubmissionDto.getPregnancyOutcomeStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(pregnancyOutcomeStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(pregnancyOutcomeStageDto, "save");
            errorMap = validationResult.retrieveAll();
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void initBabyDefect(PregnancyOutcomeStageDto pregnancyOutcomeStageDto, HttpServletRequest request) {
        List<List<String>> defectTypesArray = IaisCommonUtils.genNewArrayList();
        List<String> otherDefectTypes = IaisCommonUtils.genNewArrayList();
        List<PregnancyOutcomeBabyDto> pregnancyOutcomeBabyDtos = pregnancyOutcomeStageDto.getPregnancyOutcomeBabyDtos();
        if (IaisCommonUtils.isNotEmpty(pregnancyOutcomeBabyDtos)) {
            for (int i = 0; i < pregnancyOutcomeBabyDtos.size(); i++) {
                PregnancyOutcomeBabyDto pregnancyOutcomeBabyDto = pregnancyOutcomeBabyDtos.get(i);
                List<String> defectTypes = IaisCommonUtils.genNewArrayList();
                otherDefectTypes.add("");
                for (PregnancyOutcomeBabyDefectDto pregnancyOutcomeBabyDefectDto : pregnancyOutcomeBabyDto.getPregnancyOutcomeBabyDefectDtos()) {
                    defectTypes.add(pregnancyOutcomeBabyDefectDto.getDefectType());
                    if ("other".equals(pregnancyOutcomeBabyDefectDto.getDefectType())) {
                        otherDefectTypes.set(i, pregnancyOutcomeBabyDefectDto.getOtherDefectType());
                    }
                }
                defectTypesArray.add(defectTypes);
            }
        }
        ParamUtil.setRequestAttr(request, "defectTypesArray", defectTypesArray);
        ParamUtil.setRequestAttr(request, "otherDefectTypes", otherDefectTypes);
    }

    private void fromPageData(PregnancyOutcomeStageDto pregnancyOutcomeStageDto, HttpServletRequest request) {
        String firstUltrasoundOrderShow = ParamUtil.getString(request, "firstUltrasoundOrderShow");
        String pregnancyOutcome = ParamUtil.getString(request, "pregnancyOutcome");
        String otherPregnancyOutcome = ParamUtil.getString(request, "otherPregnancyOutcome");

        Integer maleLiveBirthNum = ParamUtil.getInt(request, "maleLiveBirthNum", 0);
        Integer femaleLiveBirthNum = ParamUtil.getInt(request, "femaleLiveBirthNum", 0);

        Integer stillBirthNum = ParamUtil.getInt(request, "stillBirthNum", 0);
        Integer spontAbortNum = ParamUtil.getInt(request, "spontAbortNum", 0);
        Integer intraUterDeathNum = ParamUtil.getInt(request, "intraUterDeathNum", 0);

        Integer wasSelFoeReduCarryOut = ParamUtil.getInt(request, "wasSelFoeReduCarryOut", 1);

        String deliveryMode = ParamUtil.getString(request, "deliveryMode");

        String deliveryDateType = ParamUtil.getString(request, "deliveryDateCheckbox");
        Date deliveryDate = DateUtil.parseDate(ParamUtil.getString(request, "deliveryDate"), AppConsts.DEFAULT_DATE_FORMAT);

        String birthPlace = ParamUtil.getString(request, "birthPlace");
        String localBirthPlace = ParamUtil.getString(request, "localBirthPlace");

        Boolean babyDetailsUnknown = "true".equals(ParamUtil.getString(request, "babyDetailsUnknown"));

        Integer l2CareBabyNum = ParamUtil.getInt(request, "l2CareBabyNum", 0);
        Integer nicuCareBabyNum = ParamUtil.getInt(request, "NICUCareBabyNum", 0);
        Integer l3CareBabyNum = ParamUtil.getInt(request, "l3CareBabyNum", 0);

        Integer l2CareBabyDays = ParamUtil.getInt(request, "l2CareBabyDays", 0);
        Integer l3CareBabyDays = ParamUtil.getInt(request, "l3CareBabyDays", 0);

        int totalLiveBirthNum = maleLiveBirthNum + femaleLiveBirthNum;

        List<PregnancyOutcomeBabyDto> pregnancyOutcomeBabyDtos = genPrenancyOUtcomeBabyDtos(request, totalLiveBirthNum);

        pregnancyOutcomeStageDto.setFirstUltrasoundOrderShow(firstUltrasoundOrderShow);
        pregnancyOutcomeStageDto.setPregnancyOutcome(pregnancyOutcome);
        pregnancyOutcomeStageDto.setOtherPregnancyOutcome(otherPregnancyOutcome);
        pregnancyOutcomeStageDto.setMaleLiveBirthNum(maleLiveBirthNum);
        pregnancyOutcomeStageDto.setFemaleLiveBirthNum(femaleLiveBirthNum);
        pregnancyOutcomeStageDto.setStillBirthNum(stillBirthNum);
        pregnancyOutcomeStageDto.setSpontAbortNum(spontAbortNum);
        pregnancyOutcomeStageDto.setIntraUterDeathNum(intraUterDeathNum);
        pregnancyOutcomeStageDto.setWasSelFoeReduCarryOut(wasSelFoeReduCarryOut);
        pregnancyOutcomeStageDto.setDeliveryMode(deliveryMode);
        pregnancyOutcomeStageDto.setDeliveryDateType(deliveryDateType);
        pregnancyOutcomeStageDto.setDeliveryDate(deliveryDate);
        pregnancyOutcomeStageDto.setBirthPlace(birthPlace);
        pregnancyOutcomeStageDto.setLocalBirthPlace(localBirthPlace);
        pregnancyOutcomeStageDto.setBabyDetailsUnknown(babyDetailsUnknown);
        pregnancyOutcomeStageDto.setNicuCareBabyNum(nicuCareBabyNum);
        pregnancyOutcomeStageDto.setL2CareBabyNum(l2CareBabyNum);
        pregnancyOutcomeStageDto.setL3CareBabyNum(l3CareBabyNum);
        pregnancyOutcomeStageDto.setL2CareBabyDays(l2CareBabyDays);
        pregnancyOutcomeStageDto.setL3CareBabyDays(l3CareBabyDays);
        pregnancyOutcomeStageDto.setPregnancyOutcomeBabyDtos(pregnancyOutcomeBabyDtos);
    }

    private List<PregnancyOutcomeBabyDto> genPrenancyOUtcomeBabyDtos(HttpServletRequest request, int totalLiveBirthNum) {
        List<PregnancyOutcomeBabyDto> pregnancyOutcomeBabyDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < totalLiveBirthNum; i++) {
            PregnancyOutcomeBabyDto pregnancyOutcomeBabyDto = new PregnancyOutcomeBabyDto();
            String birthWeight = ParamUtil.getString(request, "birthWeight" + i);
            String birthDefect = ParamUtil.getString(request, "birthDefect" + i);
            String[] defectTypes = ParamUtil.getStrings(request, "defectType" + i);
            String otherDefectType = ParamUtil.getString(request, "otherDefectType" + i);

            pregnancyOutcomeBabyDto.setSeqNum(i);
            pregnancyOutcomeBabyDto.setBirthWeight(birthWeight);
            pregnancyOutcomeBabyDto.setBirthDefect(birthDefect);
            List<PregnancyOutcomeBabyDefectDto> pregnancyOutcomeBabyDefectDtos = IaisCommonUtils.genNewArrayList();
            if (defectTypes != null && defectTypes.length > 0) {
                for (String defectType : defectTypes) {
                    PregnancyOutcomeBabyDefectDto pregnancyOutcomeBabyDefectDto = new PregnancyOutcomeBabyDefectDto();
                    pregnancyOutcomeBabyDefectDto.setDefectType(defectType);
                    if ("other".equals(defectType)) {
                        pregnancyOutcomeBabyDefectDto.setOtherDefectType(otherDefectType);
                    }
                    pregnancyOutcomeBabyDefectDtos.add(pregnancyOutcomeBabyDefectDto);
                }
            }
            pregnancyOutcomeBabyDto.setPregnancyOutcomeBabyDefectDtos(pregnancyOutcomeBabyDefectDtos);
            pregnancyOutcomeBabyDtos.add(pregnancyOutcomeBabyDto);
        }
        return pregnancyOutcomeBabyDtos;
    }
}
