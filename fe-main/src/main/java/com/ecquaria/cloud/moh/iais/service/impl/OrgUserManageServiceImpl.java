package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.FeAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrgUserManageServiceImpl implements OrgUserManageService {

    @Autowired
    private FeAdminClient feAdminClient;

    @Autowired
    private FeUserClient feUserClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private EicGatewayFeMainClient eicGatewayFeMainClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam){
        return feUserClient.getFeUserList(searchParam).getEntity();
    }

    @Override
    public OrganizationDto getOrganizationById( String id){
        return feAdminClient.getOrganizationById(id).getEntity();
    }

    @Override
    public FeUserDto addAdminAccount(FeUserDto feUserDto){
        return feAdminClient.addAdminAccount(feUserDto).getEntity();
    }

    @Override
    public FeUserDto getUserAccount(String userId){
        return feUserClient.getUserAccount(userId).getEntity();
    }

    @Override
    public FeUserDto editUserAccount(FeUserDto feUserDto){
        return feUserClient.editUserAccount(feUserDto).getEntity();
    }

    @Override
    public String ChangeActiveStatus(String userId, String targetStatus){
        return feAdminClient.ChangeActiveStatus(userId,targetStatus).getEntity();
    }

    @Override
    public OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto){
        return feUserClient.addUserRole(orgUserRoleDto).getEntity();
    }

    @Override
    public Boolean validateSingpassId(String nric, String pwd) {

        //TODO : call egp api to do validation

        return Boolean.TRUE;
    }

    @Override
    public Boolean isKeyappointment(String uen, String nricNumber){
        return Boolean.FALSE;
    }

    @Override
    public List<String> getUenListByNric(String nric) {
        return feUserClient.getUenListByNric(nric).getEntity();
    }

    @Override
	public OrganizationDto createSingpassAccount(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createSingpassAccount(organizationDto);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            OrganizationDto postCreate = result.getEntity();

            String json = JsonUtil.parseToJson(postCreate);

            //create egp user
            FeUserDto feUserDto = postCreate.getFeUserDto();
            createClientUser(feUserDto);

            try {
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                eicGatewayFeMainClient.syncAccountDataFormFe(postCreate, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync account data to be"));
                log.error(e.getMessage(), e);
            }
            return postCreate;
        }else {
            return null;
        }
	}

    @Override
    public FeUserDto createCropUser(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createCropUser(organizationDto);
        int status = result.getStatusCode();

        if (status != HttpStatus.SC_OK){
            return null;
        }else {
            OrganizationDto postCreate = result.getEntity();

            //create egp user
            FeUserDto postCreateUser = postCreate.getFeUserDto();
            createClientUser(postCreateUser);
            try {
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                eicGatewayFeMainClient.syncAccountDataFormFe(postCreate, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync account data to be"));
                log.error(e.getMessage(), e);
            }

            return postCreateUser;
        }
    }

    @Override
    public FeUserDto getUserByNricAndUen(String uen, String nric) {
        return feUserClient.getUserByNricAndUen(uen, nric).getEntity();
    }

    @Override
    public FeUserDto getFeUserAccountByNric(String nric) {
        return feUserClient.getInternetUserByNric(nric).getEntity();
    }


    @Override
    public void createClientUser(FeUserDto userDto) {

        //TODO : simple create
        FeignResponseEntity<ClientUser> result = userClient.findUser(AppConsts.USER_DOMAIN_INTERNET, userDto.getUserId());
        int status = result.getStatusCode();

        if (status != HttpStatus.SC_OK){
            return;
        }

        ClientUser clientUser = result.getEntity();
        if (clientUser != null){
            return;
        }

        clientUser = MiscUtil.transferEntityDto(userDto, ClientUser.class);
        clientUser.setUserDomain(userDto.getUserDomain());
        clientUser.setId(userDto.getUserId());
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        String email = userDto.getEmail();
        String salutation = userDto.getSalutation();
        clientUser.setSalutation(salutation);
        clientUser.setEmail(email);
        clientUser.setPassword("password$2");
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");

        Date activeDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(activeDate);
        calendar.add(Calendar.DAY_OF_MONTH, 12);
        clientUser.setAccountActivateDatetime(activeDate);
        clientUser.setAccountDeactivateDatetime(calendar.getTime());

        userClient.createClientUser(clientUser);
    }

    @Override
    public OrganizationDto findOrganizationByUen(String uen) {
        return feUserClient.findOrganizationByUen(uen).getEntity();
    }

    @Override
    public void updateEgpUser(FeUserDto feUserDto) {
        ClientUser clientUser = userClient.findUser(feUserDto.getUserDomain(), feUserDto.getUserId()).getEntity();
        if (clientUser != null){
            clientUser.setSalutation(feUserDto.getSalutation());
            clientUser.setEmail(feUserDto.getEmail());
            clientUser.setDisplayName(feUserDto.getDisplayName());
            clientUser.setIdentityNo(feUserDto.getIdentityNo());
            clientUser.setMobileNo(feUserDto.getMobileNo());
            clientUser.setContactNo(feUserDto.getOfficeTelNo());
            userClient.updateClientUser(clientUser);
        }else{
            clientUser = MiscUtil.transferEntityDto(feUserDto, ClientUser.class);
            clientUser.setUserDomain(feUserDto.getUserDomain());
            clientUser.setId(feUserDto.getUserId());
            clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
            String email = feUserDto.getEmail();
            String salutation = feUserDto.getSalutation();
            clientUser.setSalutation(salutation);
            clientUser.setEmail(email);
            clientUser.setDisplayName(feUserDto.getFirstName()+feUserDto.getLastName());
            clientUser.setPassword("password$2");
            clientUser.setPasswordChallengeQuestion("A");
            clientUser.setPasswordChallengeAnswer("A");

            Date activeDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(activeDate);
            calendar.add(Calendar.DAY_OF_MONTH, 12);
            clientUser.setAccountActivateDatetime(activeDate);
            clientUser.setAccountDeactivateDatetime(calendar.getTime());

            userClient.createClientUser(clientUser);
        }



    }


}
