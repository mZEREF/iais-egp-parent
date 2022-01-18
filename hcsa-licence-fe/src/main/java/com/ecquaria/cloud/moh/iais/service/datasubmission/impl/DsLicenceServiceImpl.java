package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code DsLicenceServiceImpl}
 *
 * @Auther chenlei on 1/13/2022.
 */
@Slf4j
@Service
public class DsLicenceServiceImpl implements DsLicenceService {

    @Autowired
    private LicenceClient licenceClient;

    @Value("${halp.ds.tempCenter.enable:false}")
    private boolean tempCenterEnable;

    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    static {
        System.out.println("-------------------tempCenterEnable---------------------");
    }

    @Override
    public Map<String, PremisesDto> getArCenterPremises(String licenseeId) {
        return getDataSubmissionPremises(licenseeId, DataSubmissionConsts.DS_AR);
    }

    public Map<String, PremisesDto> getDpCenterPremises(String licenseeId) {
        return getDataSubmissionPremises(licenseeId, DataSubmissionConsts.DS_DRP);
    }

    public Map<String, PremisesDto> getDataSubmissionPremises(String licenseeId, String dsType) {
        if (tempCenterEnable) {
            return getDsCenterPremises(getOrgId(licenseeId), dsType);
        } else {
            List<String> svcNames = new ArrayList<>();
            //TODO
            //svcNames.add(DataSubmissionConsts.SVC_NAME_AR_CENTER);
            return getLicencePremises(licenseeId, svcNames);
        }
    }

    private Map<String, PremisesDto> getDsCenterPremises(String orgId, String dsType) {
        if (StringUtil.isEmpty(orgId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        List<DsCenterDto> dsCenterDtos = licenceClient.getDsCenterDtosByOrgIdAndCentreType(orgId, dsType).getEntity();
        Map<String, PremisesDto> premisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (dsCenterDtos == null || dsCenterDtos.isEmpty()) {
            return premisesDtoMap;
        }
        for (DsCenterDto dsCenterDto : dsCenterDtos) {
            premisesDtoMap.put(dsCenterDto.getId(), DsHelper.transfer(dsCenterDto));
        }
        return premisesDtoMap;
    }

    private Map<String, PremisesDto> getLicencePremises(String licenseeId, List<String> svcNames) {
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        svcNames = IaisCommonUtils.getList(svcNames);
        List<PremisesDto> premisesDtos = licenceClient.getLatestPremisesByConds(licenseeId, svcNames, false).getEntity();
        Map<String, PremisesDto> premisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (premisesDtos == null || premisesDtos.isEmpty()) {
            return premisesDtoMap;
        }
        for (PremisesDto premisesDto : premisesDtos) {
            premisesDtoMap.put(DataSubmissionHelper.getPremisesMapKey(premisesDto, DataSubmissionConsts.DS_AR),
                    premisesDto);
        }
        return premisesDtoMap;
    }

    private PremisesDto transfer(DsCenterDto dsCenterDto, String orgId) {
        PremisesDto premisesDto = new PremisesDto();
        premisesDto.setId(dsCenterDto.getId());
        premisesDto.setSvcName(dsCenterDto.getCenterType());
        premisesDto.setBusinessName(dsCenterDto.getCenterName());
        premisesDto.setHciCode(dsCenterDto.getHciCode());
        premisesDto.setOrganizationId(orgId);
        return premisesDto;
    }

    private String getOrgId(String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)) {
            return null;
        }
        OrganizationDto organizationDto = organizationLienceseeClient.getOrganizationDtoByLicenseeId(licenseeId).getEntity();
        if (organizationDto != null) {
            return organizationDto.getId();
        }
        return null;
    }

}
