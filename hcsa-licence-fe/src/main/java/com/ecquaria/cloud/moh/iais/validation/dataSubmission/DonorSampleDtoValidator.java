package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class DonorSampleDtoValidator implements CustomizeValidator {


    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArDataSubmissionService arDataSubmissionService = SpringContextHelper.getContext().getBean(ArDataSubmissionService.class);
        DonorSampleDto donorSampleDto = (DonorSampleDto) obj;

        if (validateLocally(donorSampleDto, errorMap)) {
            validateFrom(donorSampleDto, errorMap);
        }
        if (validateSampleType(donorSampleDto, errorMap)) {
            if (showFemale(donorSampleDto)) {
                boolean idValidated = true;
                if (validateFemaleIdenrityKnown(donorSampleDto, errorMap)) {
                    if (DataSubmissionConsts.DONOR_IDENTITY_KNOWN.equals(donorSampleDto.getDonorIdentityKnown())) {
                        if (validateFemaleHasNric(donorSampleDto, errorMap)) {
                            idValidated = validateFemaleIdNumber(donorSampleDto, errorMap);
                        }
                        validateFemaleSimpleCode(donorSampleDto, errorMap);
                    } else {
                        idValidated = validateFemaleSimpleCode(donorSampleDto, errorMap);
                    }
                }
                validateFemaleAge(donorSampleDto, errorMap, arDataSubmissionService, idValidated);
            }
            if (showMale(donorSampleDto)) {
                boolean idValidated = true;
                if (validateMaleIdenrityKnown(donorSampleDto, errorMap)) {
                    if (donorSampleDto.getMaleDonorIdentityKnow()) {
                        if (validateMaleHasNric(donorSampleDto, errorMap)) {
                            idValidated = validateMaleIdNumber(donorSampleDto, errorMap);
                        }
                        validateMaleSimpleCode(donorSampleDto, errorMap);
                    } else {
                        idValidated = validateMaleSimpleCode(donorSampleDto, errorMap);
                    }
                }
                validateMaleAge(donorSampleDto, errorMap, arDataSubmissionService, idValidated);
            }
        }
        validateReason(donorSampleDto, errorMap);
        if (validatePurpose(donorSampleDto, errorMap)) {
            if (donorSampleDto.isDonatedForResearch()) {
                validateDonResForTreatNum(donorSampleDto, errorMap);
                validateDonResForCurCenNotTreatNum(donorSampleDto, errorMap);
                validateGameteResType(donorSampleDto, errorMap);
                validateDonatedForResearchOtherType(donorSampleDto, errorMap);
            }
            if (donorSampleDto.isDonatedForTraining()) {
                validateTrainingNum(donorSampleDto, errorMap);
            }
            if (donorSampleDto.isDonatedForTreatment()) {
                validateDonatedForTreatment(donorSampleDto, errorMap);
            }
        }

        return errorMap;
    }

    private boolean validateLocally(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (Objects.isNull(donorSampleDto.getLocalOrOversea())) {
            errorMap.put("localOrOversea", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateSampleType(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getSampleType())) {
            errorMap.put("sampleType", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateFemaleIdenrityKnown(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getDonorIdentityKnown())) {
            errorMap.put("donorIdentityKnown", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateFemaleHasNric(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getIdType())) {
            errorMap.put("hasIdNumberF", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateFemaleIdNumber(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        int maxLength = 9;
        if (DataSubmissionConsts.DTV_ID_TYPE_PASSPORT.equals(donorSampleDto.getIdType())) {
            maxLength = 20;
        }
        if (StringUtil.isEmpty(donorSampleDto.getIdNumber())) {
            errorMap.put("idNumber", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        } else if (donorSampleDto.getIdNumber().length() > maxLength) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field", "The field");
            params.put("maxlength", String.valueOf(maxLength));
            errorMap.put("idNumber", MessageUtil.getMessageDesc("GENERAL_ERR0041", params));
            return false;
        }
        return true;
    }

    private boolean validateFemaleSimpleCode(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getDonorSampleCode())) {
            errorMap.put("donorSampleCode", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateFemaleAge(DonorSampleDto donorSampleDto, Map<String, String> errorMap, ArDataSubmissionService arDataSubmissionService, boolean idValidated) {
        if (StringUtil.isEmpty(donorSampleDto.getDonorSampleAge())) {
            errorMap.put("donorSampleAge", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        if (idValidated) {
            List<DonorSampleAgeDto> donorSampleDtoAgeList;
            if (DataSubmissionConsts.DONOR_IDENTITY_KNOWN.equals(donorSampleDto.getDonorIdentityKnown())) {
                String sampleKey = arDataSubmissionService.getDonorSampleKey(donorSampleDto.getIdType(), donorSampleDto.getIdNumber());
                if (StringUtil.isNotEmpty(sampleKey)) {
                    donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtoBySampleKey(sampleKey);
                } else {
                    donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtos(DataSubmissionConsts.DTV_ID_TYPE_CODE, donorSampleDto.getDonorSampleCode());
                }
            } else {
                donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtos(DataSubmissionConsts.DTV_ID_TYPE_CODE, donorSampleDto.getDonorSampleCode());
            }
            if (IaisCommonUtils.isNotEmpty(donorSampleDtoAgeList)) {
                donorSampleDto.setSampleKey(donorSampleDtoAgeList.get(0).getSampleKey());
            }
            if (!StringUtil.isNumber(donorSampleDto.getDonorSampleAge())) {
                errorMap.put("donorSampleAge", "GENERAL_ERR0002");
            }

        }
        return true;
    }

    private boolean validateMaleIdenrityKnown(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getMaleDonorIdentityKnow())) {
            errorMap.put("maleDonorIdentityKnow", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateMaleHasNric(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getIdTypeMale())) {
            errorMap.put("hasIdNumberM", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateMaleIdNumber(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        int maxLength = 9;
        if (DataSubmissionConsts.DTV_ID_TYPE_PASSPORT.equals(donorSampleDto.getIdTypeMale())) {
            maxLength = 20;
        }
        if (StringUtil.isEmpty(donorSampleDto.getIdNumberMale())) {
            errorMap.put("idNumberMale", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        } else if (donorSampleDto.getIdNumberMale().length() > maxLength) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field", "The field");
            params.put("maxlength", String.valueOf(maxLength));
            errorMap.put("idNumberMale", MessageUtil.getMessageDesc("GENERAL_ERR0041", params));
            return false;
        }
        return true;
    }

    private boolean validateMaleSimpleCode(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (StringUtil.isEmpty(donorSampleDto.getMaleDonorSampleCode())) {
            errorMap.put("maleDonorSampleCode", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateMaleAge(DonorSampleDto donorSampleDto, Map<String, String> errorMap, ArDataSubmissionService arDataSubmissionService, boolean idValidated) {
        if (StringUtil.isEmpty(donorSampleDto.getMaleDonorSampleAge())) {
            errorMap.put("maleDonorSampleAge", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        if (idValidated) {
            List<DonorSampleAgeDto> donorSampleDtoAgeList;
            if (donorSampleDto.getMaleDonorIdentityKnow()) {
                String sampleKey = arDataSubmissionService.getDonorSampleKey(donorSampleDto.getIdTypeMale(), donorSampleDto.getIdNumberMale());
                if (StringUtil.isNotEmpty(sampleKey)) {
                    donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtoBySampleKey(sampleKey);
                } else {
                    donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtos(DataSubmissionConsts.DTV_ID_TYPE_CODE, donorSampleDto.getMaleDonorSampleCode());
                }
            } else {
                donorSampleDtoAgeList = arDataSubmissionService.getDonorSampleAgeDtos(DataSubmissionConsts.DTV_ID_TYPE_CODE, donorSampleDto.getMaleDonorSampleCode());
            }
            if (IaisCommonUtils.isNotEmpty(donorSampleDtoAgeList)) {
                donorSampleDto.setSampleKeyMale(donorSampleDtoAgeList.get(0).getSampleKey());
            }
            if (!StringUtil.isNumber(donorSampleDto.getMaleDonorSampleAge())) {
                errorMap.put("maleDonorSampleAge", "GENERAL_ERR0002");
            }
        }
        return true;
    }

    private boolean validateFrom(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        Boolean localOrOversea = donorSampleDto.getLocalOrOversea();
        String sampleFromOversea = donorSampleDto.getSampleFromOthers();
        if (Boolean.FALSE.equals(localOrOversea) && StringUtil.isEmpty(sampleFromOversea)) {
            errorMap.put("sampleFromOversea", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateReason(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        String reason = donorSampleDto.getDonationReason();
        final String GENERAL_ERR0006 = "GENERAL_ERR0006";
        if (DataSubmissionConsts.DONATION_REASON_OTHERS.equals(reason)) {
            if (StringUtil.isEmpty(donorSampleDto.getOtherDonationReason())) {
                errorMap.put("otherDonationReason", GENERAL_ERR0006);
                return false;
            }
        } else {
            if (StringUtil.isEmpty(reason)) {
                errorMap.put("donationReason", GENERAL_ERR0006);
                return false;
            }
        }
        return true;
    }

    private boolean validatePurpose(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (!donorSampleDto.isDonatedForResearch()
                && !donorSampleDto.isDonatedForTraining()
                && !donorSampleDto.isDonatedForTreatment()) {
            errorMap.put("donationPurpose", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        }
        return true;
    }

    private boolean validateDonResForTreatNum(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForResearch() && StringUtil.isEmpty(donorSampleDto.getDonResForTreatNum())) {
            errorMap.put("donResForTreatNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        } else if (donorSampleDto.isDonatedForResearch() && !StringUtil.isNumber(donorSampleDto.getDonResForTreatNum())) {
            errorMap.put("donResForTreatNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            return false;
        }
        return true;
    }

    private boolean validateDonResForCurCenNotTreatNum(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForResearch() && StringUtil.isEmpty(donorSampleDto.getDonResForCurCenNotTreatNum())) {
            errorMap.put("donResForCurCenNotTreatNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            return false;
        } else if (donorSampleDto.isDonatedForResearch() && !StringUtil.isNumber(donorSampleDto.getDonResForCurCenNotTreatNum())) {
            errorMap.put("donResForCurCenNotTreatNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            return false;
        }
        return true;
    }

    private boolean validateGameteResType(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForResearch()) {
            if (!donorSampleDto.isDonatedForResearchHescr()
                    && !donorSampleDto.isDonatedForResearchRrar()
                    && !donorSampleDto.isDonatedForResearchOther()) {
                errorMap.put("gameteResType", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                return false;
            }
        }
        return true;
    }

    private boolean validateDonatedForResearchOtherType(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForResearchOther()) {
            if (StringUtil.isEmpty(donorSampleDto.getDonatedForResearchOtherType())) {
                errorMap.put("donatedForResearchOtherType", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                return false;
            }
        }
        return true;
    }

    private boolean validateTrainingNum(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForTraining()) {
            if (StringUtil.isEmpty(donorSampleDto.getTrainingNum())) {
                errorMap.put("trainingNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                return false;
            } else if (!StringUtil.isNumber(donorSampleDto.getTrainingNum())) {
                errorMap.put("trainingNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
                return false;
            }
        }
        return true;
    }

    private boolean validateDonatedForTreatment(DonorSampleDto donorSampleDto, Map<String, String> errorMap) {
        if (donorSampleDto.isDonatedForTreatment()) {
            if (StringUtil.isEmpty(donorSampleDto.getDirectedDonation())) {
                errorMap.put("directedDonation", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
            if (StringUtil.isEmpty(donorSampleDto.getTreatNum())) {
                errorMap.put("treatNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                return false;
            } else if (!StringUtil.isNumber(donorSampleDto.getTreatNum())) {
                errorMap.put("treatNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
                return false;
            }
        }
        return true;
    }

    private boolean showFemale(DonorSampleDto donorSampleDto) {
        return Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO
        ).contains(donorSampleDto.getSampleType());
    }

    private boolean showMale(DonorSampleDto donorSampleDto) {
        return Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM
        ).contains(donorSampleDto.getSampleType());
    }
}
