package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
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

    @Autowired
    RequestForChangeService requestForChangeService;

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

    @Override
    public List<PremisesDto> getArCenterPremises() {
        if (tempCenterEnable) {
            return getCenterPremisesByCentreType(DataSubmissionConsts.DS_AR);
        } else {
            //TODO
            return IaisCommonUtils.genNewArrayList();
        }
    }

    public Map<String, PremisesDto> getDataSubmissionPremises(String licenseeId, String dsType) {
        log.info(StringUtil.changeForLog("Licensee Id: " + licenseeId + " - DS Type: " + dsType));
        if (tempCenterEnable) {
            return getDsCenterPremises(getOrgId(licenseeId), dsType);
        } else {
            List<String> svcNames = new ArrayList<>();
            //TODO
            //svcNames.add(DataSubmissionConsts.SVC_NAME_AR_CENTER);
            return getLicencePremises(licenseeId, svcNames);
        }
    }

    @Override
    public PremisesDto getArPremisesDto(String orgId, String hciCode) {
        if (tempCenterEnable) {
            return getArCenterPremises(orgId, hciCode);
        } else {
            List<String> svcNames = new ArrayList<>();
            //TODO
            //svcNames.add(DataSubmissionConsts.SVC_NAME_AR_CENTER);
            List<PremisesDto> premisesDtos = licenceClient.getLatestPremisesByConds(getLicenseeId(orgId), svcNames, false).getEntity();
            for (PremisesDto premisesDto : premisesDtos) {
                if (hciCode.equals(premisesDto.getHciCode())) {
                    return premisesDto;
                }
            }
            return null;
        }
    }

    private PremisesDto getArCenterPremises(String orgId, String hciCode) {
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        DsCenterDto dsCenterDto = licenceClient.getArCenter(orgId, hciCode).getEntity();
        if (dsCenterDto != null) {
            return transfer(dsCenterDto, orgId);
        }
        return null;
    }

    private Map<String, PremisesDto> getDsCenterPremises(String orgId, String dsType) {
        log.info(StringUtil.changeForLog("Org Id: " + orgId + " - Center Type: " + dsType));
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

    private List<PremisesDto> getCenterPremisesByCentreType(String dsType) {
        List<PremisesDto> premisesDtos = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(dsType)) {
            return premisesDtos;
        }
        List<DsCenterDto> centerDtos = licenceClient.getCenterDtosByCentreType(dsType).getEntity();
        if (centerDtos == null || centerDtos.isEmpty()) {
            return premisesDtos;
        }
        for (DsCenterDto dsCenterDto : centerDtos) {
            premisesDtos.add(transfer(dsCenterDto, dsCenterDto.getOrganizationId()));
        }
        return premisesDtos;
    }

    private Map<String, PremisesDto> getLicencePremises(String licenseeId, List<String> svcNames) {
        log.info(StringUtil.changeForLog("Licensee Id: " + licenseeId + " - Svc Names: " + svcNames));
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

    private String getLicenseeId(String orgId) {
        String result = null;
        LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(orgId);
        if (licenseeDto != null) {
            result = licenseeDto.getId();
        }
        return result;
    }
}
