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
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
            pregnancyOutcomeStageDto.setBabyDetailsUnknown(Boolean.FALSE);
            pregnancyOutcomeStageDto.setBirthPlace("POSBP001");
            pregnancyOutcomeStageDto.setWasSelFoeReduCarryOut(1);
            arSuperDataSubmissionDto.setPregnancyOutcomeStageDto(pregnancyOutcomeStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }
        int totalLiveBirthNum = getInt(pregnancyOutcomeStageDto.getMaleLiveBirthNum()) + getInt(pregnancyOutcomeStageDto.getFemaleLiveBirthNum());
        ParamUtil.setRequestAttr(bpc.request, "totalLiveBirthNum", totalLiveBirthNum);

        List<SelectOption> transferNumSelectOption = DataSubmissionHelper.getNumsSelections(0, 6);
        ParamUtil.setRequestAttr(bpc.request, "transferNumSelectOption", transferNumSelectOption);

        List<SelectOption> defectTypeOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_POS_BABY_DEFECT);
        ParamUtil.setRequestAttr(bpc.request, "defectTypeOptions", defectTypeOptions);

        List<SelectOption> wasSelFoeReduCarryOutOptions = IaisCommonUtils.genNewArrayList();
        wasSelFoeReduCarryOutOptions.add(new SelectOption("0","Yes"));
        wasSelFoeReduCarryOutOptions.add(new SelectOption("1","No"));
        wasSelFoeReduCarryOutOptions.add(new SelectOption("2","Unknown"));
        ParamUtil.setRequestAttr(bpc.request, "wasSelFoeReduCarryOutOptions", wasSelFoeReduCarryOutOptions);

        initBabyDefect(arSuperDataSubmissionDto.getPregnancyOutcomeStageDto(), bpc.request);
    }

    private int getInt(String s) {
        if (StringUtil.isNumber(s)) {
            return Integer.parseInt(s);
        }
        return 0;
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
            valRFC(request, pregnancyOutcomeStageDto);
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
                    if ("POSBDT008".equals(pregnancyOutcomeBabyDefectDto.getDefectType())) {
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

        String maleLiveBirthNum = ParamUtil.getString(request, "maleLiveBirthNum");
        String femaleLiveBirthNum = ParamUtil.getString(request, "femaleLiveBirthNum");

        String stillBirthNum = ParamUtil.getString(request, "stillBirthNum");
        String spontAbortNum = ParamUtil.getString(request, "spontAbortNum");
        String intraUterDeathNum = ParamUtil.getString(request, "intraUterDeathNum");

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

        String l2CareBabyDays = ParamUtil.getString(request, "l2CareBabyDays");
        String l3CareBabyDays = ParamUtil.getString(request, "l3CareBabyDays");

        int totalLiveBirthNum = 0;
        if (StringUtil.isNotEmpty(maleLiveBirthNum) && CommonValidator.isPositiveInteger(maleLiveBirthNum)) {
            totalLiveBirthNum += Integer.parseInt(maleLiveBirthNum);
        }

        if (StringUtil.isNotEmpty(femaleLiveBirthNum) && CommonValidator.isPositiveInteger(femaleLiveBirthNum)) {
            totalLiveBirthNum += Integer.parseInt(femaleLiveBirthNum);
        }

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
                    if ("POSBDT008".equals(defectType)) {
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

    protected void valRFC(HttpServletRequest request, PregnancyOutcomeStageDto pregnancyOutcomeStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getArCycleStageDto() != null && pregnancyOutcomeStageDto.equals(arOldSuperDataSubmissionDto.getPregnancyOutcomeStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
