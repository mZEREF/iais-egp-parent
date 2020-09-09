package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.FEMainRbacClient;
import com.ecquaria.cloud.moh.iais.service.client.FeAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.rbac.user.UserIdentifier;

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
    private FEMainRbacClient feMainRbacClient;

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

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

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
    public List<String> getUenListByIdAndType(String nric, String idType) {
        return feUserClient.getUenListByIdAndType(nric, idType).getEntity();
    }

    @Override
    public OrganizationDto createSingpassAccount(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createSingpassAccount(organizationDto);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            OrganizationDto postCreate = result.getEntity();
            saveAccountInfotmation(postCreate);
            return postCreate;
        }else {
            return null;
        }
    }

    public void callFeEicCreateAccount(OrganizationDto organizationDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        eicGatewayFeMainClient.syncAccountDataFormFe(organizationDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());

    }

    private FeUserDto saveAccountInfotmation(OrganizationDto postCreate){
        EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.ORGANIZATION_CLIENT, OrgUserManageServiceImpl.class.getName(),
                "callFeEicCreateAccount", currentApp + "-" + currentDomain,
                OrganizationDto.class.getName(), JsonUtil.parseToJson(postCreate));

        try {
            FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getOrgTrackingClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
            if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                EicRequestTrackingDto entity = fetchResult.getEntity();
                if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                    postCreate.setLicenseeDto(new LicenseeDto());
                    callFeEicCreateAccount(postCreate);
                    entity.setProcessNum(1);
                    Date now = new Date();
                    entity.setFirstActionAt(now);
                    entity.setLastActionAt(now);
                    entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                    eicRequestTrackingHelper.getOrgTrackingClient().saveEicTrack(entity);
                }
            }

        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when sync user account to be" + e.getMessage()));
        }

        //create egp user
        FeUserDto postCreateUser = postCreate.getFeUserDto();

        createClientUser(postCreateUser);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        eicGatewayFeMainClient.getUen(postCreate.getUenNo(), signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());

        return postCreateUser;
    }


    @Override
    public FeUserDto createCropUser(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createCropUser(organizationDto);
        OrganizationDto postCreate = result.getEntity();


        return saveAccountInfotmation(postCreate);
    }

    @Override
    public void updateUserBe(OrganizationDto organizationDto){
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            eicGatewayFeMainClient.syncAccountDataFormFe(organizationDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when sync account data to be"));
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public FeUserDto getUserByNricAndUen(String uen, String nric) {
        return feUserClient.getUserByNricAndUen(uen, nric).getEntity();
    }

    @Override
    public FeUserDto getFeUserAccountByNricAndType(String nric, String idType) {
        return feUserClient.getInternetUserByNricAndIdType(nric, idType).getEntity();
    }


    @Override
    public void createClientUser(FeUserDto userDto) {
        if (userDto == null) {
            return;
        }
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

        String userId = userDto.getUserId();
        String email = userDto.getEmail();
        String salutation = userDto.getSalutation();

        clientUser = MiscUtil.transferEntityDto(userDto, ClientUser.class);
        clientUser.setId(userId);
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        clientUser.setSalutation(salutation);
        clientUser.setEmail(email);

        UserIdentifier userIdentifier = new UserIdentifier();
        userIdentifier.setId(userId);
        userIdentifier.setUserDomain(AppConsts.HALP_EGP_DOMAIN);

        String randomStr = IaisEGPHelper.generateRandomString(6);
        String pwd = PasswordUtil.encryptPassword(userIdentifier, randomStr, null);

        clientUser.setPassword(pwd);
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");

        Date activeDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(activeDate);
        calendar.add(Calendar.DAY_OF_MONTH, 12);
        clientUser.setAccountActivateDatetime(activeDate);
        clientUser.setAccountDeactivateDatetime(calendar.getTime());

        OrgUserRoleDto userRoleDto = userDto.getUserRoleDto();
        if (userDto != null){
            EgpUserRoleDto egpUserRole = new EgpUserRoleDto();
            egpUserRole.setUserId(userId);
            egpUserRole.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            egpUserRole.setRoleId(userRoleDto.getRoleName());
            egpUserRole.setPermission("A");

            //assign role
            feMainRbacClient.createUerRoleIds(egpUserRole).getEntity();
        }
        userClient.createClientUser(clientUser);
    }

    @Override
    public OrganizationDto findOrganizationByUen(String uen) {
        return feUserClient.findOrganizationByUen(uen).getEntity();
    }

    @Override
    public void updateEgpUser(FeUserDto feUserDto) {
        ClientUser clientUser = userClient.findUser(AppConsts.HALP_EGP_DOMAIN, feUserDto.getUserId()).getEntity();
        String pwd = PasswordUtil.encryptPassword(AppConsts.HALP_EGP_DOMAIN, IaisEGPHelper.generateRandomString(6), null);
        if (clientUser != null){
            clientUser.setSalutation(feUserDto.getSalutation());
            clientUser.setEmail(feUserDto.getEmail());
            clientUser.setDisplayName(feUserDto.getDisplayName());
            clientUser.setIdentityNo(feUserDto.getIdentityNo());
            clientUser.setMobileNo(feUserDto.getMobileNo());
            clientUser.setContactNo(feUserDto.getOfficeTelNo());

            //prevent history simple pwd throw 500
            clientUser.setPassword(pwd);

            userClient.updateClientUser(clientUser);
            //delete egp role
            String epgUserId ;
            if(feUserDto.getUserRole().equals(RoleConsts.USER_ROLE_ORG_ADMIN)){
                epgUserId = RoleConsts.USER_ROLE_ORG_USER;
            }else{
                epgUserId = RoleConsts.USER_ROLE_ORG_ADMIN;
            }
            feMainRbacClient.deleteUerRoleIds(AppConsts.HALP_EGP_DOMAIN,feUserDto.getUserId(),epgUserId);
        }else{
            clientUser = MiscUtil.transferEntityDto(feUserDto, ClientUser.class);
            clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            clientUser.setId(feUserDto.getUserId());
            clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
            String email = feUserDto.getEmail();
            String salutation = feUserDto.getSalutation();
            clientUser.setSalutation(salutation);
            clientUser.setEmail(email);
            clientUser.setDisplayName(feUserDto.getFirstName()+feUserDto.getLastName());


            clientUser.setPassword(pwd);
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

        //assign role
        EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
        egpUserRoleDto.setUserId(feUserDto.getUserId());
        egpUserRoleDto.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        egpUserRoleDto.setRoleId(feUserDto.getUserRole());
        egpUserRoleDto.setPermission("A");
        feMainRbacClient.createUerRoleIds(egpUserRoleDto);
    }

    @Override
    public LicenseeDto getLicenseeById(String id){
        return feAdminClient.getLicenseeById(id).getEntity();
    }

    @Override
    public List<LicenseeDto> getLicenseeByOrgId(String orgId){
        return feAdminClient.getLicenseeByOrgId(orgId).getEntity();
    }

    @Override
    public List<LicenseeKeyApptPersonDto> getPersonById(String id){
        return feAdminClient.getPersonByid(id).getEntity();
    }

    @Override
    public IaisApiResult<Void> checkIssueUen(String idNo, String idType) {
        return feUserClient.checkIssueUen(idNo, idType).getEntity();
    }

    @Override
    public List<LicenseeDto> getLicenseeNoUen(){
        return feUserClient.getLicenseeNoUen().getEntity();
    }
}
