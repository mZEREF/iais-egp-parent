package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.*;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.rbac.user.UserIdentifier;

import java.util.*;

@Slf4j
@Service
public class OrgUserManageService {

    @Autowired
    private FeUserClient feUserClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private FEMainRbacClient feMainRbacClient;

    @Autowired
    private LicenseeClient licenseeClient;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    
    @SearchTrack(catalog = "interInboxQuery", key = "feUserList")
    public SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam){
        return feUserClient.getFeUserList(searchParam).getEntity();
    }
    
    public FeUserDto getUserAccount(String userId){
        return feUserClient.getUserAccount(userId).getEntity();
    }
    
    public FeUserDto editUserAccount(FeUserDto feUserDto){
        feUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return feUserClient.editUserAccount(feUserDto).getEntity();
    }
    
    public OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto){
        return feUserClient.addUserRole(orgUserRoleDto).getEntity();
    }

    public FeUserDto createSingpassAccount(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createHalpAccount(organizationDto);
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK){
            OrganizationDto postCreate = result.getEntity();
            return postCreate.getFeUserDto();
        }else {
            return null;
        }
    }
    
    public void refreshLicensee(String uen) {
        // eicGatewayFeMainClient.getUen(uen);
        licenseeClient.getEntityByUEN(uen);
    }

    public FeUserDto createCorpPassUser(OrganizationDto organizationDto) {
        FeignResponseEntity<OrganizationDto> result = feUserClient.createHalpAccount(organizationDto);
        OrganizationDto postCreate = result.getEntity();
        if (postCreate == null){
            // sonar check
            return null;
        }

        return postCreate.getFeUserDto();
    }

    public FeUserDto getUserByNricAndUen(String uen, String nric) {
        return feUserClient.getUserByNricAndUen(uen, nric).getEntity();
    }

    public List<FeUserDto> getAccountByOrgId(String orgId){
        return feUserClient.getAccountByOrgId(orgId).getEntity();
    }
    
    public FeUserDto getFeUserAccountByNricAndType(String nric, String idType) {
        return feUserClient.getInternetUserByNricAndIdType(nric, idType).getEntity();
    }


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
        String chanQue = PasswordUtil.encryptPassword(userIdentifier, randomStr, null);
        String chanAn = PasswordUtil.encryptPassword(userIdentifier, randomStr, null);

        clientUser.setPassword(pwd);
        clientUser.setPasswordChallengeQuestion(chanQue);
        clientUser.setPasswordChallengeAnswer(chanAn);

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
    
    public OrganizationDto findOrganizationByUen(String uen) {
        return feUserClient.findOrganizationByUen(uen).getEntity();
    }

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
                String chanQue = PasswordUtil.encryptPassword(AppConsts.HALP_EGP_DOMAIN, IaisEGPHelper.generateRandomString(6), null);
                String chanAn = PasswordUtil.encryptPassword(AppConsts.HALP_EGP_DOMAIN, IaisEGPHelper.generateRandomString(6), null);
                clientUser.setPasswordChallengeQuestion(chanQue);
                clientUser.setPasswordChallengeAnswer(chanAn);

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
    
    public Boolean isNotExistUserAccount(String orgId) {
        return feUserClient.isNotExistUserAccount(orgId).getEntity();
    }

//    public static void main(String[] args) {
//        JwtEncoder encoder = new JwtEncoder();
//        Claims claims = Jwts.claims();
//        claims.put("uen", "201800001A");
//        String jwtt = encoder.encode(claims, "");
//    }
    
    public Boolean validateSingpassAccount(String idNo, String idType) {
        log.info(StringUtil.changeForLog("SingPass Login service [checkIssueUen] ...ID " + idNo + " ID Type..." + idType));
        return feUserClient.validateSingpassAccount(idNo, idType).getEntity().isHasError();
    }

    public List<LicenseeDto> getLicenseeNoUen(){
        return feUserClient.getLicenseeNoUen().getEntity();
    }

    public Boolean validatePwd(FeUserDto feUserDto) {
        return feUserClient.validatePwd(feUserDto).getEntity();
    }

    public void receiveEntityFormEDH(FeUserDto user) {
        log.info("receiveEntityFormEDH START");
        try {
            String entityJson = licenseeClient.getEntityInfoByUEN(user.getUenNo()).getEntity();
            if (StringUtil.isNotEmpty(entityJson)){
                log.info("receiveEntityFormEDH entityJson {}", entityJson);
                user.setAcraGetEntityJsonStr(entityJson);
                JSONObject object = new JSONObject(entityJson);
                if (Optional.ofNullable(object).isPresent()){
                    JSONArray licences = object.getJSONArray("licences");
                    if (Optional.ofNullable(licences).isPresent()){
                        for (int i = 0; i < licences.length(); i++) {
                            JSONObject licence = licences.optJSONObject(i);
                            if (Optional.ofNullable(licence).isPresent()){
                                JSONObject acraLicensee = licence.optJSONObject("licensee");
                                String nric = acraLicensee.optJSONObject("id-no").getString("value");
                                if (user.getIdentityNo().equals(nric)){
                                    log.info("writeInfoFromEDH START................. {}", nric);
                                    String licenseeName = acraLicensee.optJSONObject("name").getString("value");
                                    user.setDisplayName(licenseeName);
                                    break;
                                }
                            }

                        }
                    }

                    JSONArray appointments = object.getJSONArray("appointments");
                    if (Optional.ofNullable(appointments).isPresent()){
                        for (int i = 0; i < appointments.length(); i++) {
                            JSONObject appointment = appointments.optJSONObject(i);
                            if (Optional.ofNullable(appointment).isPresent()){
                                JSONObject aptPerson = appointment.optJSONObject("appointed-person");
                                if (Optional.ofNullable(aptPerson).isPresent()){
                                    JSONObject idNo = aptPerson.optJSONObject("id-no");
                                    if (Optional.ofNullable(idNo).isPresent()){
                                        String ido = idNo.getString("value");
                                        if (user.getIdentityNo().equals(ido)){
                                            log.info("========>>>>>>>>> key appointment true");
                                            user.setKeyAppointment(true);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public FeUserDto syncFeUserFromBe(FeUserDto feUserDto) {
        log.info("Synchronize FE user from BE");
        if (!isValid(feUserDto)) {
            return feUserDto;
        }
        log.info(StringUtil.changeForLog("User Id: " + feUserDto.getUserId()));
        // syncronize halp user
        log.info("Synchronize iais user");
        FeUserDto feUserDtoRes = editUserAccount(feUserDto);
        // syncronize egp user
        log.info("Synchronize egp user");
        updateEgpUser(feUserDto);
        log.info("Synchronize FE user from BE end.");
        return feUserDtoRes;
    }

    private boolean isValid(FeUserDto feUserDto) {
        if (Objects.isNull(feUserDto) || StringUtil.isEmpty(feUserDto.getUserId())) {
            log.warn(StringUtil.changeForLog("The user Id is null!"));
            return false;
        }
        ValidationResult result = WebValidationHelper.validatePropertyWithoutCustom(feUserDto, "edit");
        if (result.isHasErrors()) {
            log.warn(StringUtil.changeForLog("" + WebValidationHelper.generateJsonStr(result.retrieveAll())));
            return false;
        }
        return true;
    }

}
