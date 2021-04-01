package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtEncoder;
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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Value("${moh.halp.acra.enable}")
    private String enableAcra;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "feUserList")
    public SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam){
        return feUserClient.getFeUserList(searchParam).getEntity();
    }

    @Override
    public OrganizationDto getOrganizationById( String id){
        return feAdminClient.getOrganizationById(id).getEntity();
    }

    @Override
    public FeUserDto getUserAccount(String userId){
        return feUserClient.getUserAccount(userId).getEntity();
    }

    @Override
    public FeUserDto editUserAccount(FeUserDto feUserDto){
        feUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
    public FeUserDto createSingpassAccount(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createHalpAccount(organizationDto);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            OrganizationDto postCreate = result.getEntity();
            syncAccountInformationToBackend(postCreate);
            return postCreate.getFeUserDto();
        }else {
            return null;
        }
    }

    public void callFeEicCreateAccount(OrganizationDto organizationDto){
        eicGatewayFeMainClient.syncAccountDataFormFe(organizationDto);
    }

    private FeUserDto syncAccountInformationToBackend(OrganizationDto postCreate){
        EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.ORGANIZATION_CLIENT, OrgUserManageServiceImpl.class.getName(),
                "callFeEicCreateAccount", currentApp + "-" + currentDomain,
                OrganizationDto.class.getName(), JsonUtil.parseToJson(postCreate));

        try {
            FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getOrgTrackingClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
            if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                EicRequestTrackingDto entity = fetchResult.getEntity();
                if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
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
            log.error(StringUtil.changeForLog("encounter failure when sync user account to be"), e);
        }


        FeUserDto postCreateUser = postCreate.getFeUserDto();
        return postCreateUser;
    }

    @Override
    public void refreshLicensee(String uen){
        eicGatewayFeMainClient.getUen(uen);

    }

    @Override
    public void refreshLicensee(LicenseeDto licenseeDto){
        feAdminClient.updateLicence(licenseeDto);
    }

    @Override
    public FeUserDto createCorpPassUser(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createHalpAccount(organizationDto);
        OrganizationDto postCreate = result.getEntity();
        if (postCreate == null){
            // sonar check
            return null;
        }

        return postCreate.getFeUserDto();
    }

    @Override
    public void updateUserBe(OrganizationDto organizationDto){
        try {
            eicGatewayFeMainClient.syncAccountDataFormFe(organizationDto);
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
    public List<FeUserDto> getAccountByOrgId(String orgId){
        return feUserClient.getAccountByOrgId(orgId).getEntity();
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
        FeignResponseEntity<ClientUser> result = userClient.findUser(AppConsts.HALP_EGP_DOMAIN, userDto.getUserId());
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
        calendar.add(Calendar.DAY_OF_YEAR, 999);
        clientUser.setAccountActivateDatetime(activeDate);
        clientUser.setAccountDeactivateDatetime(calendar.getTime());

        OrgUserRoleDto userRoleDto = userDto.getUserRoleDto();
        EgpUserRoleDto egpUserRole = new EgpUserRoleDto();
        String roleName = userRoleDto.getRoleName();
        egpUserRole.setUserId(userId);
        egpUserRole.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        egpUserRole.setRoleId(roleName);
        egpUserRole.setPermission("A");
        //assign role
        feMainRbacClient.createUerRoleIds(egpUserRole).getEntity();

        //corppass
        if (RoleConsts.USER_ROLE_ORG_ADMIN.equalsIgnoreCase(roleName)){
            EgpUserRoleDto role = new EgpUserRoleDto();
            role.setUserId(userId);
            role.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            role.setPermission("A");
            role.setRoleId(RoleConsts.USER_ROLE_ORG_USER);
            //assign role
            feMainRbacClient.createUerRoleIds(role).getEntity();
        }

        userClient.createClientUser(clientUser);
    }

    @Override
    public OrganizationDto findOrganizationByUen(String uen) {
        return feUserClient.findOrganizationByUen(uen).getEntity();
    }

    @Override
    public void updateEgpUser(FeUserDto feUserDto) {
        if(feUserDto != null){
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
                feMainRbacClient.deleteUerRoleIds(AppConsts.HALP_EGP_DOMAIN,feUserDto.getUserId(),RoleConsts.USER_ROLE_ORG_ADMIN);
                feMainRbacClient.deleteUerRoleIds(AppConsts.HALP_EGP_DOMAIN,feUserDto.getUserId(),RoleConsts.USER_ROLE_ORG_USER);

            }else{
                clientUser = MiscUtil.transferEntityDto(feUserDto, ClientUser.class);
                clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                clientUser.setId(feUserDto.getUserId());
                clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
                String email = feUserDto.getEmail();
                String salutation = feUserDto.getSalutation();
                clientUser.setSalutation(salutation);
                clientUser.setEmail(email);
                clientUser.setDisplayName(feUserDto.getDisplayName());


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
            EgpUserRoleDto egpUserRole = new EgpUserRoleDto();
            String roleName = feUserDto.getUserRole();
            egpUserRole.setUserId(feUserDto.getUserId());
            egpUserRole.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            egpUserRole.setRoleId(roleName);
            egpUserRole.setPermission("A");
            //assign role
            feMainRbacClient.createUerRoleIds(egpUserRole).getEntity();

            //corppass
            if (RoleConsts.USER_ROLE_ORG_ADMIN.equalsIgnoreCase(roleName) &&
                    feUserDto.isCorpPass()){
                EgpUserRoleDto role = new EgpUserRoleDto();
                role.setUserId(feUserDto.getUserId());
                role.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                role.setPermission("A");
                role.setRoleId(RoleConsts.USER_ROLE_ORG_USER);
                //assign role
                feMainRbacClient.createUerRoleIds(role).getEntity();
            }


        }
    }

    @Override
    public LicenseeDto getLicenseeById(String id){
        return feAdminClient.getLicenseeById(id).getEntity();
    }

    @Override
    public LicenseeDto saveMyinfoDataByFeUserDtoAndLicenseeDto(LicenseeDto licenseeDto, FeUserDto feUserDto, MyInfoDto myInfoDto,boolean amendLicensee ) {
        LicenseeEntityDto licenseeEntityDto;
        LicenseeIndividualDto licenseeIndividualDto;
        if(licenseeDto == null) {
            licenseeDto = new LicenseeDto();
            licenseeEntityDto = new LicenseeEntityDto();
            licenseeIndividualDto= new LicenseeIndividualDto();
        }else {
            licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
            licenseeIndividualDto = licenseeDto.getLicenseeIndividualDto();
        }
      if( myInfoDto != null){
          feUserDto.setEmail(myInfoDto.getEmail());
          feUserDto.setMobileNo(myInfoDto.getMobileNo());
          feUserDto.setDisplayName(myInfoDto.getUserName());
          licenseeDto.setName(myInfoDto.getUserName());
          licenseeDto.setFloorNo(myInfoDto.getFloor());
          licenseeDto.setPostalCode(myInfoDto.getPostalCode());
          licenseeDto.setUnitNo(myInfoDto.getUnitNo());
          licenseeDto.setBlkNo(myInfoDto.getBlockNo());
          licenseeDto.setBuildingName(myInfoDto.getBuildingName());
          licenseeDto.setStreetName(myInfoDto.getStreetName());
      }
      //fe user
       FeUserDto feUserDtoCreate = editUserAccount(feUserDto);
       feUserDto.setId(feUserDtoCreate.getId());
      //egpcloud
       updateEgpUser(feUserDto);

        //update be user

        OrganizationDto organizationById = getOrganizationById(feUserDto.getOrgId());
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
        organizationDto.setFeUserDto(feUserDto);
        organizationDto.setOrgType(organizationById.getOrgType());
        organizationDto.setStatus(organizationById.getStatus());
        organizationDto.setUenNo(organizationById.getUenNo());
        organizationDto.setId(organizationById.getId());
        organizationDto.setLicenseeDto(licenseeDto);
        updateUserBe(organizationDto);
        if(amendLicensee){
            //update licensee
            licenseeDto.setOrganizationId(organizationDto.getId());
            licenseeDto.setName(feUserDto.getDisplayName());
            licenseeDto.setEmilAddr(feUserDto.getEmail());
            licenseeEntityDto.setOfficeTelNo(feUserDto.getOfficeTelNo());
            licenseeEntityDto.setOfficeEmailAddr(feUserDto.getEmail());
            licenseeIndividualDto.setSalutation(feUserDto.getSalutation());
            licenseeIndividualDto.setIdType(feUserDto.getIdType());
            licenseeIndividualDto.setIdNo(feUserDto.getIdentityNo());
            licenseeIndividualDto.setMobileNo(feUserDto.getMobileNo());
            licenseeIndividualDto.setEmailAddr(feUserDto.getEmail());
            licenseeDto.setLicenseeEntityDto(licenseeEntityDto);
            licenseeDto.setLicenseeIndividualDto(licenseeIndividualDto);
            refreshLicensee(licenseeDto);
        }
        return licenseeDto;
    }

    @Override
    public Boolean isNotExistUserAccount(String orgId) {
        return feUserClient.isNotExistUserAccount(orgId).getEntity();
    }


    public static void main(String[] args) {
        JwtEncoder encoder = new JwtEncoder();
        Claims claims = Jwts.claims();
        claims.put("uen", "201800001A");
        String jwtt = encoder.encode(claims, "");
    }

    @Override
    public Boolean isKeyAppointment(String uen) {
        JwtEncoder encoder = new JwtEncoder();
        Claims claims = Jwts.claims();
        claims.put("uen", uen);
        //edhClient.receiveEDHEntity(jwtt, uen);
        return Boolean.FALSE;
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
    public Boolean validateSingpassAccount(String idNo, String idType) {
        log.info(StringUtil.changeForLog("SingPass Login service [checkIssueUen] ...ID " + idNo + " ID Type..." + idType));
        return feUserClient.validateSingpassAccount(idNo, idType).getEntity().isHasError();
    }

    @Override
    public List<LicenseeDto> getLicenseeNoUen(){
        return feUserClient.getLicenseeNoUen().getEntity();
    }

    @Override
    public Boolean validatePwd(FeUserDto feUserDto) {
        return feUserClient.validatePwd(feUserDto).getEntity();
    }

    @Override
    public void setPermitLoginStatusInUenTrack(String uen, String nricNumber, boolean isPermit) {
        feUserClient.setPermitLoginStatusInUenTrack(uen, nricNumber, isPermit);
    }

}
