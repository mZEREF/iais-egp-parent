package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.EmailService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: guyin
 * @date time:6/24/2020 10:56 AM
 * @description:
 */
@Service
public class RoleHelper {
    @Autowired
    private EmailService emailService;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
	// retrieve All officers by role
	public List<OrgUserDto> getOfficersByRole(List<String> role){
	    List<OrgUserDto> userDtos = IaisCommonUtils.genNewArrayList();
        userDtos = emailService.retrieveOrgUserByroleId(role);
        return userDtos;
	}

	// retrieve All effect officers
    public List<OrgUserDto> getOfficers(){
        List<OrgUserDto> orgUserDtoList = IaisCommonUtils.genNewArrayList();
        orgUserDtoList = emailService.retrieveOrgUser();

        return orgUserDtoList;
    }

	// retrieve Licence Personel info
    public List<PersonnelsDto> getLicencePersonal(String licenceId){
        List<PersonnelsDto> keyPersonnelDto = IaisCommonUtils.genNewArrayList();
        hcsaLicenceClient.getPersonnelDtoByLicId(licenceId);
        return keyPersonnelDto;
    }
}
