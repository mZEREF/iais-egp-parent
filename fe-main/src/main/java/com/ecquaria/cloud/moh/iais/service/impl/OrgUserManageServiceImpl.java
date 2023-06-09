package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.FeMainEmailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.FEMainRbacClient;
import com.ecquaria.cloud.moh.iais.service.client.FeAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import com.ecquaria.cloud.rbac.role.Role;
import com.ecquaria.cloud.rbac.role.RoleServiceClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrgUserManageServiceImpl implements OrgUserManageService {

    private static final String ORD_VALUE = "value";

    @Autowired
    private FeAdminClient feAdminClient;

    @Autowired
    private FeUserClient feUserClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private FEMainRbacClient feMainRbacClient;

    @Autowired
    private LicenseeClient licenseeClient;

    @Autowired
    private LicenceInboxClient licenceClient;

    @Autowired
    private EicGatewayFeMainClient eicGatewayFeMainClient;

    @Autowired
    private FeMainEmailHelper feMainEmailHelper;

    @Autowired
    private RoleServiceClient roleServiceClient;

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
        if (feUserDto.getAuditTrailDto() == null) {
            feUserDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        return feUserClient.editUserAccount(feUserDto).getEntity();
    }

    @Override
    public String ChangeActiveStatus(String userId, String targetStatus){
        return feAdminClient.ChangeActiveStatus(userId,targetStatus).getEntity();
    }

    @Override
    public void updateCompLicensee(String orgId, String telNo, String emailAddr) {
        List<LicenseeDto> list = feAdminClient.getLicenseeByOrgId(orgId).getEntity();
        if (!IaisCommonUtils.isEmpty(list)) {
            LicenseeDto ld = list.get(0);
            ld.setEmilAddr(emailAddr);
            ld.setOfficeTelNo(telNo);
            feAdminClient.updateLicence(ld);
        }
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
            return postCreate.getFeUserDto();
        }else {
            return null;
        }
    }

    @Override
    public void refreshLicensee(String uen) {
        // eicGatewayFeMainClient.getUen(uen);
        licenseeClient.getEntityByUEN(uen);
    }

    @Override
    public void refreshLicensee(LicenseeDto licenseeDto){
        feAdminClient.updateLicence(licenseeDto);
    }

    private String refreshSubLicenseeInfo(LicenseeDto licenseeDto) {
        return licenceClient.refreshSubLicenseeInfo(licenseeDto).getEntity();
    }

    private void refreshBeSubLicenseeInfo(LicenseeDto licenseeDto) {
        eicGatewayFeMainClient.callEicWithTrackForApp(licenseeDto, eicGatewayFeMainClient::refreshSubLicenseeInfo,
                "refreshSubLicenseeInfo");
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
            eicGatewayFeMainClient.callEicWithTrackForOrg(organizationDto, eicGatewayFeMainClient::syncAccountDataFormFe,
                    "syncAccountDataFormFe");
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
    public FeUserDto getFeUserAccountByNricAndType(String nric, String idType, String uen) {
        if(StringUtil.isEmpty(uen)) {
            uen = "null";
        }
        return feUserClient.getInternetUserByNricAndIdType(nric, idType, uen).getEntity();
    }

    @Override
    public OrganizationDto findOrganizationByUen(String uen) {
        return feUserClient.findOrganizationByUen(uen).getEntity();
    }

    @Override
    public void saveEgpUser(FeUserDto feUserDto) {
        if (feUserDto == null) {
            return;
        }
        String userId = feUserDto.getUserId();
        String status = feUserDto.getStatus();
        Character accountStatus = null;
        if (AppConsts.COMMON_STATUS_DELETED.equals(status)) {
            accountStatus = ClientUser.STATUS_TERMINATED;
        } else if (AppConsts.COMMON_STATUS_IACTIVE.equals(status)) {
            accountStatus = ClientUser.STATUS_INACTIVE;
        } else if (AppConsts.COMMON_STATUS_ACTIVE.equals(status)) {
            accountStatus = ClientUser.STATUS_ACTIVE;
        }

        ClientUser clientUser = userClient.findUser(AppConsts.HALP_EGP_DOMAIN, userId).getEntity();
        String pwd = PasswordUtil.encryptPassword(AppConsts.HALP_EGP_DOMAIN, IaisEGPHelper.generateRandomString(6), null);
        if (clientUser != null) {
            clientUser.setSalutation(feUserDto.getSalutation());
            clientUser.setEmail(feUserDto.getEmail());
            clientUser.setDisplayName(feUserDto.getDisplayName());
            clientUser.setIdentityNo(feUserDto.getIdentityNo());
            clientUser.setMobileNo(feUserDto.getMobileNo());
            clientUser.setContactNo(feUserDto.getOfficeTelNo());
            clientUser.setAccountStatus(accountStatus);
            //prevent history simple pwd throw 500
            clientUser.setPassword(pwd);
            userClient.updateClientUser(clientUser);
            //delete egp role
            if (AppConsts.COMMON_STATUS_DELETED.equals(status)) {
                List<String> roles = IaisEGPHelper.getFeRoles();
                feMainRbacClient.deleteUerRoleIds(AppConsts.HALP_EGP_DOMAIN, feUserDto.getUserId(),
                        roles.toArray(new String[roles.size()]));
            }
        } else {
            clientUser = MiscUtil.transferEntityDto(feUserDto, ClientUser.class);
            clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
            clientUser.setId(userId);
            clientUser.setAccountStatus(accountStatus);
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
            calendar.add(Calendar.YEAR, 99);
            clientUser.setAccountActivateDatetime(activeDate);
            clientUser.setAccountDeactivateDatetime(calendar.getTime());

            userClient.createClientUser(clientUser);
        }
        // roles
        List<OrgUserRoleDto> orgUserRoleDtos = feUserDto.getOrgUserRoleDtos();
        if (!AppConsts.COMMON_STATUS_DELETED.equals(status) && IaisCommonUtils.isNotEmpty(orgUserRoleDtos)) {
            orgUserRoleDtos.stream()
                    .map(roleDto -> {
                        EgpUserRoleDto egpUserRole = new EgpUserRoleDto();
                        egpUserRole.setUserId(userId);
                        egpUserRole.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                        egpUserRole.setRoleId(roleDto.getRoleName());
                        egpUserRole.setPermission("A");
                        return egpUserRole;
                    })
                    .forEach(feMainRbacClient::createUerRoleIds);
        }
    }

    @Override
    public LicenseeDto getLicenseeById(String id){
        return feAdminClient.getLicenseeById(id).getEntity();
    }

    @Override
    public LicenseeDto saveMyinfoDataByFeUserDtoAndLicenseeDto(LicenseeDto licenseeDto, FeUserDto feUserDto, MyInfoDto myInfoDto,
            boolean amendLicensee) {
        LicenseeEntityDto licenseeEntityDto;
        LicenseeIndividualDto licenseeIndividualDto;
        if (licenseeDto == null) {
            licenseeDto = new LicenseeDto();
            licenseeEntityDto = new LicenseeEntityDto();
            licenseeIndividualDto = new LicenseeIndividualDto();
        } else {
            licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
            licenseeIndividualDto = licenseeDto.getLicenseeIndividualDto();
        }
        if (myInfoDto != null) {
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
        feUserDto = editUserAccount(feUserDto);
        //egpcloud
        saveEgpUser(feUserDto);

        //update be user
        OrganizationDto organizationById = getOrganizationById(feUserDto.getOrgId());
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
        organizationDto.setFeUserDto(feUserDto);
        organizationDto.setOrgType(organizationById.getOrgType());
        organizationDto.setStatus(organizationById.getStatus());
        organizationDto.setUenNo(organizationById.getUenNo());
        if (amendLicensee) {
            organizationDto.setLicenseeDto(licenseeDto);
        }
        organizationDto.setId(organizationById.getId());
        if (amendLicensee) {
            //update licensee
            licenseeDto.setOrganizationId(organizationDto.getId());
            licenseeDto.setName(feUserDto.getDisplayName());
            licenseeDto.setEmilAddr(feUserDto.getEmail());
            licenseeDto.setUenNo(organizationDto.getUenNo());
            licenseeEntityDto.setOfficeTelNo(StringUtil.isNotEmpty(feUserDto.getOfficeTelNo()) ? feUserDto.getOfficeTelNo() : feUserDto.getMobileNo());
            licenseeEntityDto.setOfficeEmailAddr(feUserDto.getEmail());
            if (Objects.nonNull(licenseeIndividualDto)) {
                licenseeIndividualDto.setSalutation(StringUtil.isEmpty(feUserDto.getSalutation()) ? "-" : feUserDto.getSalutation());
                licenseeIndividualDto.setIdType(feUserDto.getIdType());
                licenseeIndividualDto.setIdNo(feUserDto.getIdentityNo());
                licenseeIndividualDto.setMobileNo(feUserDto.getMobileNo());
                licenseeIndividualDto.setEmailAddr(feUserDto.getEmail());
                licenseeDto.setLicenseeIndividualDto(licenseeIndividualDto);
            }
            licenseeDto.setLicenseeEntityDto(licenseeEntityDto);
            refreshLicensee(licenseeDto);
        }
        updateUserBe(organizationDto);

        //note update sublic must in the end
        if(amendLicensee){
            log.info(StringUtil.changeForLog("Refresh SubLicenseeInfo Start"));
            licenseeDto.setId(null);
            String sublicId = refreshSubLicenseeInfo(licenseeDto);
            log.info(StringUtil.changeForLog("id: " + sublicId));
            licenseeDto.setId(sublicId);
            refreshBeSubLicenseeInfo(licenseeDto);
            log.info(StringUtil.changeForLog("Refresh SubLicenseeInfo End"));
        }
        return licenseeDto;
    }

    @Override
    public Boolean isNotExistUserAccount(String orgId) {
        return feUserClient.isNotExistUserAccount(orgId).getEntity();
    }

//    public static void main(String[] args) {
//        JwtEncoder encoder = new JwtEncoder();
//        Claims claims = Jwts.claims();
//        claims.put("uen", "201800001A");
//        String jwtt = encoder.encode(claims, "");
//    }

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
    public void setSingPassAutoCeased(String uen, String nricNumber) {
        boolean autoCeased = feUserClient.setPermitLoginStatusInUenTrack(uen, nricNumber, Boolean.FALSE).getEntity();
        if (autoCeased) {
            //send email
            boolean hasBeenReminder = feMainEmailHelper.hasBeenReminder(uen + "_" + nricNumber,
                    FeMainEmailHelper.SINGPASS_EXPIRE_REMINDER_JOB);

            if (!hasBeenReminder){
                feMainEmailHelper.sendSingPassAutoCeasedMsg(uen, nricNumber);
            }
        }
    }

    @Override
    public void receiveEntityFormEDH(FeUserDto user) {
        log.info("receiveEntityFormEDH START");
        try {
            String entityJson = licenseeClient.getEntityInfoByUEN(user.getUenNo()).getEntity();
            if (StringUtil.isNotEmpty(entityJson)){
                log.info("receiveEntityFormEDH entityJson {}", entityJson);
                user.setAcraGetEntityJsonStr(entityJson);
                JSONObject object = new JSONObject(entityJson);
                if (Optional.ofNullable(object).isPresent()){
                    if (object.has("licences")) {
                        JSONArray licences = object.getJSONArray("licences");
                        if (Optional.ofNullable(licences).isPresent()){
                            for (int i = 0; i < licences.length(); i++) {
                                JSONObject licence = licences.optJSONObject(i);
                                if (Optional.ofNullable(licence).isPresent()){
                                    JSONObject acraLicensee = licence.optJSONObject("licensee");
                                    String nric = acraLicensee.optJSONObject("id-no").getString(ORD_VALUE);
                                    if (user.getIdentityNo().equals(nric)){
                                        log.info("writeInfoFromEDH START................. {}", nric);
                                        String licenseeName = acraLicensee.optJSONObject("name").getString(ORD_VALUE);
                                        user.setDisplayName(licenseeName);
                                        break;
                                    }
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
                                        String ido = idNo.getString(ORD_VALUE);
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

    @Override
    public void sendReminderForExpiredSingPass() {
        String objJson = feUserClient.getExpireSingPassList().getEntity();
        if (objJson != null) {
            JSONArray uenList = new JSONArray(objJson);
            for (int i = 0; i < uenList.length(); i++){
                JSONObject object = uenList.getJSONObject(i);
                if (Optional.ofNullable(object).isPresent()){
                    String uen = object.optString("uen");
                    String nric = object.optString("nricNumber");
                    boolean hasBeenReminder = feMainEmailHelper.hasBeenReminder(uen + "_" + nric,
                            FeMainEmailHelper.SINGPASS_EXPIRE_REMINDER_JOB);
                    if (!hasBeenReminder){
                        feMainEmailHelper.sendSingPassAutoCeasedMsg(uen, nric);
                    }
                }
            }
        }
    }

    @Override
    public FeUserDto syncFeUserFromBe(FeUserDto feUserDto) {
        log.info("Synchronize FE user from BE");
        log.info(StringUtil.changeForLog("Data: " + JsonUtil.parseToJson(feUserDto)));
        if (!isValid(feUserDto)) {
            return feUserDto;
        }
        // syncronize halp user
        log.info("Synchronize iais user");
        FeUserDto feUserDtoRes = editUserAccount(feUserDto);
        // syncronize egp user
        log.info("Synchronize egp user");
        saveEgpUser(feUserDto);
        log.info("Synchronize FE user from BE end.");
        return feUserDtoRes;
    }

    @Override
    public String getActiveUserAndRoleFlag(FeUserDto userSession) {
        //if userSession == null, crop pass first login
        if(userSession != null) {
            //if id == null, sing pass login
            String id = userSession.getId();
            String identityNo = userSession.getIdentityNo();
            String idType = userSession.getIdType();
            if(!StringUtil.isEmpty(identityNo) && !StringUtil.isEmpty(idType)) {
                List<OrgUserDto> orgUserDtoList = feUserClient.getUserListByNricAndIdType(identityNo, idType).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtoList)) {
                    for(OrgUserDto orgUserDto : orgUserDtoList) {
                        //find sing pass account or crop pass account
                        if ((StringUtil.isEmpty(id) && orgUserDto.getUserId().equals(identityNo))
                            || (!StringUtil.isEmpty(id) && id.equals(orgUserDto.getId()))) {
                            String userStatus = orgUserDto.getStatus();
                            //if find, check status
                            if (AppConsts.COMMON_STATUS_ACTIVE.equals(userStatus)) {
                                List<OrgUserRoleDto> orgUserRoleDtos = feUserClient.retrieveRolesByUserAccId(orgUserDto.getId()).getEntity();
                                if (!IaisCommonUtils.isEmpty(orgUserRoleDtos)) {
                                    for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                                        if (orgUserRoleDto != null) {
                                            if (AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserRoleDto.getStatus())
                                                    && !RoleConsts.USER_ROLE_ORG_ADMIN.equalsIgnoreCase(orgUserRoleDto.getRoleName())
                                                    && IaisEGPHelper.getFeRoles().contains(orgUserRoleDto.getRoleName())) {
                                                //account active
                                                return AppConsts.TRUE;
                                            }
                                        }
                                    }
                                }
                            } else {
                                return AppConsts.FALSE;
                            }
                        }
                    }
                    //first login
                    return AppConsts.EMPTY_STR_NA;
                } else {
                    return AppConsts.EMPTY_STR_NA;
                }
            }
        }
        return AppConsts.FALSE;
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

    @Override
    public List<SelectOption> getRoleSelection(String licenseeId) {
        // ConfigHelper.getBoolean("halp.ds.tempCenter.enable",false)
        List<String> data = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isNotEmpty(licenseeId)) {
            log.info(StringUtil.changeForLog("The Licensee Id: " + licenseeId));
            List<DsCenterDto> dsCenterDtos = licenceClient.getDsCenterDtosByLicenseeId(licenseeId).getEntity();
            if (IaisCommonUtils.isNotEmpty(dsCenterDtos)) {
                for (DsCenterDto dsCenterDto:dsCenterDtos
                     ) {
                    data.add(dsCenterDto.getCenterType());
                }
            }
            List<LicenceDto> licenceDtos = licenceClient.getActiveLicencesByLicenseeId(licenseeId).getEntity();
            if (IaisCommonUtils.isNotEmpty(licenceDtos)) {
                for (LicenceDto licenceDto:licenceDtos
                ) {
                    data.add(licenceDto.getSvcName());
                }
            }
        }
        List<String> roles = IaisEGPHelper.getFeRoles(data);
        Map<String, String> roleMap = getFeRoleMap();
        return roles.stream()
                .map(role -> new SelectOption(role, roleMap.get(role)))
                .filter(opt -> Objects.nonNull(opt.getText()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getFeRoles() {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomains", AppConsts.HALP_EGP_DOMAIN);
        return roleServiceClient.search(map).getEntity();
    }

    @Override
    public Map<String, String> getFeRoleMap() {
        List<Role> roleList = getFeRoles();
        if (roleList == null || roleList.isEmpty()) {
            return IaisCommonUtils.genNewHashMap();
        }
        return roleList.stream().collect(Collectors.toMap(Role::getId, Role::getName, (a, b) -> b));
    }
}
