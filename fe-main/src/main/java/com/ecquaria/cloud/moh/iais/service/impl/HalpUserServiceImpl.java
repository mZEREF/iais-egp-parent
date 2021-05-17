package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HalpUserService;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloud.pwd.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @Description manage licnesee and user
 * @Auther chenlei on 5/17/2021.
 */
@Slf4j
@Service
public class HalpUserServiceImpl implements HalpUserService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private FeUserClient feUserClient;

    @Override
    public FeUserDto syncFeUserFromBe(FeUserDto feUserDto) {
        log.info("Synchronize FE user from BE");
        if (!isValid(feUserDto)) {
            return feUserDto;
        }
        log.info(StringUtil.changeForLog("User Id: " + feUserDto.getUserId()));
        // syncronize halp user
        log.info("Synchronize iais user");
        FeUserDto feUserDtoRes = saveUser(feUserDto);
        // syncronize egp user
        log.info("Synchronize egp user");
        String userId = feUserDto.getUserId();
        ClientUser clientUser = getClientUserById(userId);
        if (Objects.isNull(clientUser)) {
            clientUser = transferEntity(clientUser, feUserDto);
            userClient.createClientUser(clientUser);
        } else {
            userClient.updateClientUser(clientUser);
        }
        log.info("Synchronize FE user from BE end.");
        return feUserDtoRes;
    }

    private boolean isValid(FeUserDto feUserDto) {
        if (Objects.isNull(feUserDto) || StringUtil.isEmpty(feUserDto.getUserId())) {
            log.warn(StringUtil.changeForLog("The user Id is null!"));
            return false;
        }
        ValidationResult result = WebValidationHelper.validateProperty(feUserDto, "edit");
        if (result.isHasErrors()) {
            log.warn(StringUtil.changeForLog("" + WebValidationHelper.generateJsonStr(result.retrieveAll())));
            return false;
        }
        return true;
    }

    private ClientUser transferEntity(ClientUser clientUser, FeUserDto feUserDto) {
        if (Objects.isNull(clientUser)) {
            clientUser = new ClientUser();
            String randomStr = IaisEGPHelper.generateRandomString(6);
            String pwd = PasswordUtil.encryptPassword(clientUser.getUserDomain(), randomStr, null);
            clientUser.setPassword(pwd);
        }
//        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
//        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
//        String email = orgUserDto.getEmail();
//        String displayName = orgUserDto.getDisplayName();
//        String firstName = orgUserDto.getFirstName();
//        String lastName = orgUserDto.getLastName();
//        String salutation = orgUserDto.getSalutation();
//        String mobileNo = orgUserDto.getMobileNo();
//
//        clientUser.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
//        clientUser.setId(userId);
//        clientUser.setDisplayName(displayName);
//        clientUser.setAccountStatus(ClientUser.STATUS_TERMINATED);
//        clientUser.setPasswordChallengeQuestion("A");
//        clientUser.setPasswordChallengeAnswer("A");
//        clientUser.setAccountActivateDatetime(accountActivateDatetime);
//        clientUser.setAccountDeactivateDatetime(accountDeactivateDatetime);
//        clientUser.setFirstName(firstName);
//        clientUser.setLastName(lastName);
//        clientUser.setEmail(email);
//        clientUser.setMobileNo(mobileNo);
//        clientUser.setSalutation(salutation);
//        intranetUserService.updateEgpUser(clientUser);
        clientUser = MiscUtil.transferEntityDto(feUserDto, ClientUser.class, null, clientUser);
        clientUser.setId(feUserDto.getUserId());
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");
        return clientUser;
    }

    private ClientUser getClientUserById(String userId) {
        return userClient.getUserByIdentifier(userId, AppConsts.HALP_EGP_DOMAIN).getEntity();
    }

    private FeUserDto saveUser(FeUserDto feUserDto) {
        return feUserClient.editUserAccount(feUserDto).getEntity();
    }

}
