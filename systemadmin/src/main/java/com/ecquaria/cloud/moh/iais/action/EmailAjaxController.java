package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/emailAjax")
public class EmailAjaxController {

    @Autowired
    private DistributionListService distributionListService;

    @Autowired
    private BlastManagementListService blastManagementListService;


    @RequestMapping(value = "mulrecipientsRoles.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> mulrecipientsRoles(HttpServletRequest request, HttpServletResponse response) {
        String serviceCode = request.getParameter("serviceCode");
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(!serviceCode.isEmpty()){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceCode);
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(hcsaServiceDto.getId(),AppConsts.COMMON_STATUS_ACTIVE);
            for (HcsaSvcPersonnelDto item:hcsaSvcPersonnelDtoList
            ) {
                selectOptions.add(new SelectOption(item.getPsnType(),roleName(item.getPsnType())));
            }
        }
        Map<String, String> result = new HashMap<>();
        Map<String,String> roleAttr = IaisCommonUtils.genNewHashMap();
        roleAttr.put("class", "role");
        roleAttr.put("id", "role");
        roleAttr.put("name", "role");
        roleAttr.put("style", "display: none;");
        String roleSelectStr = getMultiSelect(selectOptions, "role");
        result.put("roleSelect",roleSelectStr);
        return result;
    }
    @RequestMapping(value = "recipientsRoles.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> recipientsRoles(HttpServletRequest request, HttpServletResponse response) {
        String serviceCode = request.getParameter("serviceCode");
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(!serviceCode.isEmpty()){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceCode);
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(hcsaServiceDto.getId(),AppConsts.COMMON_STATUS_ACTIVE);
            for (HcsaSvcPersonnelDto item:hcsaSvcPersonnelDtoList
            ) {
                selectOptions.add(new SelectOption(item.getPsnType(),roleName(item.getPsnType())));
            }
        }
        Map<String, String> result = new HashMap<>();
        Map<String,String> roleAttr = IaisCommonUtils.genNewHashMap();
        roleAttr.put("class", "role");
        roleAttr.put("id", "role");
        roleAttr.put("name", "role");
        roleAttr.put("style", "display: none;");
        String roleSelectStr = generateDropDownHtml(roleAttr, selectOptions, "Please Select", null);
        result.put("roleSelect",roleSelectStr);
        return result;
    }

    @RequestMapping(value = "distributionList.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> distributionList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = new HashMap<>();
        Map<String,String> distributionAttr = IaisCommonUtils.genNewHashMap();
        distributionAttr.put("class", "distributionList");
        distributionAttr.put("id", "distributionList");
        distributionAttr.put("name", "distributionList");
        distributionAttr.put("style", "display: none;");
        List<SelectOption> selectOptions = getDistribution(request);
        String distributionSelect = generateDropDownHtml(distributionAttr, selectOptions, "Please Select", null);

        result.put("distributionSelect",distributionSelect);
        return result;
    }

    @RequestMapping(value = "checkUse.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> checkUse(HttpServletRequest request, HttpServletResponse response) {
        String[] disString = ParamUtil.getMaskedStrings(request, "checkboxlist");

        Map<String, String> result = new HashMap<>();
        List<String> disList = IaisCommonUtils.genNewArrayList();
        for (String item: disString
             ) {
            disList.add(item);
        }

        if(blastManagementListService.checkUse(disList)){
            result.put("res",AppConsts.TRUE);
        }else{
            result.put("res",AppConsts.FALSE);
        }
        return result;
    }

    private String roleName(String roleAbbreviation){
        String roleName = "";
        switch (roleAbbreviation){
            case ApplicationConsts.PERSONNEL_PSN_TYPE_CGO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_PO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_DPO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_DEPUTY_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_MAP:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_SVC;
                break;
            default:
                roleName = roleAbbreviation;
                break;
        }
        return roleName;
    }

    private List<SelectOption> getDistribution(HttpServletRequest request){
        String mode = request.getParameter("modeDelivery");
        List<DistributionListWebDto> distributionListDtos = distributionListService.getDistributionList(mode);
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        for (DistributionListWebDto item :distributionListDtos
        ) {
            selectOptions.add(new SelectOption(item.getId(),item.getDisname()));
        }
        return selectOptions;
    }

    private String getMultiSelect(List<SelectOption> selectOptionList,String name){
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("<div style=\"height: 200px; border: 1px solid darkgrey;overflow: scroll\" id=\"")
                .append(name).append("Clear\">");
        int i = 0;
        for (SelectOption item:selectOptionList
              ) {
            sBuffer.append("<label class=\"checkbox-custom check-primary\" style=\"margin-left: 2px\">")
                    .append("<input value=\"").append(item.getValue()).append("\" id=\"").append(name).append(i).append("\" name=\"").append(name).append("\" type=\"checkbox\">")
                    .append("<label for=\"").append(name).append(i).append("\">")
                    .append("<span>").append(item.getText()).append("</span>")
                    .append("</label></label><br>");
            i ++;
        }
        sBuffer.append("</div><span id=\"error_").append(name).append("\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        return sBuffer.toString();
    }

    private String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption, String checkedVal){
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        sBuffer.append(" >");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<option value=\"\">").append(firestOption).append("</option>");
        }
        for(SelectOption sp:selectOptionList){
            if(!StringUtil.isEmpty(checkedVal)){
                if(checkedVal.equals(sp.getValue())){
                    sBuffer.append("<option selected=\"selected\" value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }else{
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            }else{
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(checkedVal)){
            sBuffer.append("<span selected=\"selected\" class=\"current\">").append(checkedVal).append("</span>");
        }else{
            if(!StringUtil.isEmpty(firestOption)){
                sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
            }else{
                sBuffer.append("<span class=\"current\">").append(selectOptionList.get(0).getText()).append("</span>");
            }
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");

        if(!StringUtil.isEmpty(checkedVal)){
            for(SelectOption kv:selectOptionList){
                if(checkedVal.equals(kv.getValue())){
                    sBuffer.append("<li selected=\"selected\" data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }else if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"\" class=\"option selected\">").append(firestOption).append("</li>");
            for(SelectOption kv:selectOptionList){
                sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
            }
        }else{
            for(int i = 0;i<selectOptionList.size();i++){
                SelectOption kv = selectOptionList.get(i);
                if(i == 0){
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }

        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }
}