package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.service.client.OrgCommClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther chenlei on 5/4/2022.
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrgCommClient orgCommClient;

    @Override
    public List<FeUserDto> getFeUserDtoByLicenseeId(String licenseeId) {
        log.info(StringUtil.changeForLog("licenseeId is " + licenseeId));
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return orgCommClient.getFeUserDtoByLicenseeId(licenseeId).getEntity();
    }

    @Override
    public SubLicenseeDto getSubLicenseeByLicenseeId(String licenseeId) {
        log.info(StringUtil.changeForLog("licenseeId is " + licenseeId));
        if (StringUtil.isEmpty(licenseeId)) {
            return null;
        }
        LicenseeDto licenseeDto = orgCommClient.getLicenseeById(licenseeId).getEntity();
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        fieldMap.put("name", "licenseeName");
        fieldMap.put("organizationId", "orgId");
        fieldMap.put("officeTelNo", "telephoneNo");
        fieldMap.put("emilAddr", "emailAddr");
        SubLicenseeDto subLicenseeDto = MiscUtil.transferEntityDto(licenseeDto, SubLicenseeDto.class, fieldMap);
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
        }
        if (OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY);
        } else if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setAssignSelect(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW);
            LicenseeIndividualDto licenseeIndividualDto = Optional.ofNullable(licenseeDto)
                    .map(LicenseeDto::getLicenseeIndividualDto)
                    .orElseGet(LicenseeIndividualDto::new);
            subLicenseeDto.setIdType(licenseeIndividualDto.getIdType());
            subLicenseeDto.setIdNumber(licenseeIndividualDto.getIdNo());
            if (!StringUtil.isEmpty(licenseeDto.getMobileNo())) {
                subLicenseeDto.setTelephoneNo(licenseeDto.getMobileNo());
            }
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO);
        }
        return subLicenseeDto;
    }

    @Override
    public boolean isGiroAccount(String licenseeId) {
        boolean result = false;
        List<OrgGiroAccountInfoDto> orgGiroAccountInfoDtos = orgCommClient.getGiroAccByLicenseeId(licenseeId).getEntity();
        if(!IaisCommonUtils.isEmpty(orgGiroAccountInfoDtos)){
            result = true;
        }
        return result;
    }

}
