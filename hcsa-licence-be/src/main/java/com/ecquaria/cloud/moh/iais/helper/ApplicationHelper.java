package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @Description ApplicationHelper
 * @Auther chenlei on 12/27/2021.
 */
@Slf4j
public final class ApplicationHelper {

    public static final String PLEASEINDICATE = "Please indicate";
    public static final String SERVICE_SCOPE_LAB_OTHERS = "Others";

    public static <T> List<T> getList(List<T> sourceList) {
        if (sourceList == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        return sourceList;
    }

    public static String getDocDisplayTitle(HcsaSvcDocConfigDto entity, Integer num) {
        if (entity == null) {
            return null;
        }
        String dupForPrem = entity.getDupForPrem();
        String dupForPerson = entity.getDupForPerson();
        String docTitle = entity.getDocTitle();
        return getDocDisplayTitle(dupForPrem, dupForPerson, docTitle, num);
    }

    public static String getDocDisplayTitle(String dupForPrem, String dupForPerson, String docTitle, Integer num) {
        log.info(StringUtil.changeForLog("The dupForPrem -->: " + dupForPrem));
        log.info(StringUtil.changeForLog("The dupForPerson -->: " + dupForPerson));
        log.info(StringUtil.changeForLog("The docTitle -->: " + docTitle));
        String result = null;
        if (dupForPerson == null && "0".equals(dupForPrem)) {
            result = docTitle;
        } else if (dupForPerson == null && "1".equals(dupForPrem)) {
            result = ApplicationConsts.TITLE_MODE_OF_SVCDLVY + "  1: " + docTitle;
        } else if (dupForPerson != null) {
            StringBuilder title = new StringBuilder();
            if ("1".equals(dupForPrem)) {
                title.append(ApplicationConsts.TITLE_MODE_OF_SVCDLVY).append(" 1: ");
            }
            title.append(getDupForPersonName(dupForPerson));
            if (num != null) {
                title.append(" ").append(num);
            }
            title.append(": ").append(docTitle);
            result = title.toString();
        }
        log.info(StringUtil.changeForLog("The Result -->: " + result));
        return result;
    }

    private static String getDupForPersonName(String dupForPerson) {
        String psnName = "";
        switch (dupForPerson) {
            case ApplicationConsts.DUP_FOR_PERSON_CGO:
                psnName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_PO:
                psnName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_DPO:
                psnName = HcsaConsts.NOMINEE;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_MAP:
                psnName = HcsaConsts.MEDALERT_PERSON;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                psnName = HcsaConsts.SERVICE_PERSONNEL;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_CD:
                psnName = HcsaConsts.CLINICAL_DIRECTOR;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SL:
                psnName = HcsaConsts.SECTION_LEADER;
                break;
            default:
                break;
        }
        return psnName;
    }

    public static List<AppSvcChckListDto> handlerPleaseIndicateLab(List<AppSvcChckListDto> appSvcChckListDtos) {
        List<AppSvcChckListDto> newAppSvcChckListDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
            AppSvcChckListDto targetDto = appSvcChckListDtos.stream()
                    .filter(dto -> Objects.equals(PLEASEINDICATE, dto.getChkName()))
                    .findAny()
                    .orElse(null);
            if (targetDto != null) {
                for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                    AppSvcChckListDto newAppSvcChckListDto = (AppSvcChckListDto) CopyUtil.copyMutableObject(appSvcChckListDto);
                    String chkName = newAppSvcChckListDto.getChkName();
                    if (PLEASEINDICATE.equals(chkName)) {
                        continue;
                    }
                    if (SERVICE_SCOPE_LAB_OTHERS.equals(chkName)) {
                        chkName = chkName + " (" + targetDto.getOtherScopeName() + ")";
                        newAppSvcChckListDto.setChkName(chkName);
                    }
                    newAppSvcChckListDtos.add(newAppSvcChckListDto);
                }
            } else {
                newAppSvcChckListDtos = appSvcChckListDtos;
            }
        }
        return newAppSvcChckListDtos;
    }

}
