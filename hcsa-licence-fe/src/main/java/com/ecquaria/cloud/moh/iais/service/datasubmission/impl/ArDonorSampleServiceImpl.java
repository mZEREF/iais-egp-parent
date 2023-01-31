package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDonorSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ArDonorSampleServiceImpl implements ArDonorSampleService {
    @Autowired
    private GenerateIdClient generateIdClient;

    @Override
    public DonorSampleDto genDonorSampleDtoByPage(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String id = null;
        String sampleFromHciCode = null;
        String submissionId = null;
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(arSuperDataSubmissionDto.getAppType())) {
            DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
            id = donorSampleDto.getId();
            sampleFromHciCode = donorSampleDto.getSampleFromHciCode();
            submissionId = donorSampleDto.getSubmissionId();
        }
        DonorSampleDto donorSampleDto = ControllerHelper.get(request, DonorSampleDto.class);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(arSuperDataSubmissionDto.getAppType())) {
            donorSampleDto.setId(id);
            donorSampleDto.setSampleFromHciCode(sampleFromHciCode);
            donorSampleDto.setSubmissionId(submissionId);
        }
        String sampleType = donorSampleDto.getSampleType();

        if (Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO
        ).contains(sampleType)) {
            donorSampleDto.setSampleKey(generateIdClient.getSeqId().getEntity());
        }
        if (Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO
        ).contains(sampleType)) {
            donorSampleDto.setSampleKeyMale(generateIdClient.getSeqId().getEntity());
        }

        String hasIdNumberF = ParamUtil.getString(request, "hasIdNumberF");
        String idNo = donorSampleDto.getIdNumber();
        donorSampleDto.setIdType(judgeIdType(hasIdNumberF, idNo));

        String hasIdNumberM = ParamUtil.getString(request, "hasIdNumberM");
        String idNoM = donorSampleDto.getIdNumberMale();
        donorSampleDto.setIdTypeMale(judgeIdType(hasIdNumberM, idNoM));

        return donorSampleDto;
    }

    @Override
    public List<SelectOption> getSampleFromSelOpts(HttpServletRequest request){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().forEach(v->stringStringMap.put(v.getHciCode(),v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,"Others"));
        return selectOptions;
    }

    private String judgeIdType(String isNricFin, String idNo) {
        String idType = DataSubmissionConsts.DTV_ID_TYPE_FIN;
        if (AppConsts.NO.equals(isNricFin)) {
            idType = DataSubmissionConsts.DTV_ID_TYPE_PASSPORT;
        } else if(StringUtil.isEmpty(isNricFin)){
            return null;
        } else if (StringUtil.isNotEmpty(idNo)) {
            boolean isNric = SgNoValidator.validateNric(idNo);
            if (isNric) {
                idType = DataSubmissionConsts.DTV_ID_TYPE_NRIC;
            }
        }
        return idType;
    }
}
