package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Date;

/**
 * @author weilu
 * @date 2020/2/28 10:27
 */
@Delegator(value = "IntranetUserDownload")
@Slf4j
@RequestMapping("/intranetUserAjax")
public class UserDownloadDelegator {

    @Autowired
    private IntranetUserService intranetUserService;
    public void action(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        String[] ids = (String[]) ParamUtil.getSessionAttr(bpc.request, "userIdsSess");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        if(ids!=null){
            byte[] xml = createXML(ids);
            bpc.request.setAttribute("xml", xml);
            bpc.request.setAttribute("fileName", "intranetUser.xml");
        }
    }
    private byte[] createXML(String [] ids) {
        byte[] bytes = null;
        try {
            //1.create dom
            Document document = DocumentHelper.createDocument();
            //2.create rootElement
            Element userGroups = document.addElement("user-groups");
            for(String id :ids){
                String maskUserId = MaskUtil.unMaskValue("maskUserId", id);
                OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(maskUserId);
                String userIdText = orgUserDto.getUserId();
                String displayNameText = orgUserDto.getDisplayName();
                String userDomainText = orgUserDto.getUserDomain();
                String salutationText = orgUserDto.getSalutation();
                Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
                Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
                String startStr = DateUtil.formatDate(accountActivateDatetime, "dd/MM/yyyy");
                String endStr = DateUtil.formatDate(accountDeactivateDatetime, "dd/MM/yyyy");
                String firstNameText = orgUserDto.getFirstName();
                String lastNameText = orgUserDto.getLastName();
                String organizationText = orgUserDto.getOrganization();
                String divisionText = orgUserDto.getDivision();
                String mobileNoText = orgUserDto.getMobileNo();
                String officeTelNoText = orgUserDto.getOfficeTelNo();
                String branchUnitText = orgUserDto.getBranchUnit();
                String emailText = orgUserDto.getEmail();
                String remarksText = orgUserDto.getRemarks();
                String statusText = null;
                String status1 = orgUserDto.getStatus();
                if(IntranetUserConstant.COMMON_STATUS_ACTIVE.equals(status1)){
                    statusText = "Active";
                }
                if(IntranetUserConstant.COMMON_STATUS_DEACTIVATED.equals(status1)){
                    statusText = "Deactivated";
                }
                if(IntranetUserConstant.COMMON_STATUS_TERMINATED.equals(status1)){
                    statusText = "Terminated";
                }
                if(IntranetUserConstant.COMMON_STATUS_SUSPENDED.equals(status1)){
                    statusText = "Suspended";
                }
                if(IntranetUserConstant.COMMON_STATUS_LOCKED.equals(status1)){
                    statusText = "Locked";
                }
                if(IntranetUserConstant.COMMON_STATUS_EXPIRED.equals(status1)){
                    statusText = "Expired";
                }
                Element userGroup = userGroups.addElement("user-group");
                Element userId = userGroup.addElement("userId");
                if (!StringUtil.isEmpty(userIdText)) {
                    userId.setText(orgUserDto.getUserId());
                }
                Element userDomain = userGroup.addElement("userDomain");
                if (!StringUtil.isEmpty(userDomainText)) {
                    userDomain.setText(userDomainText);
                }
                Element displayName = userGroup.addElement("displayName");
                if (!StringUtil.isEmpty(displayNameText)) {
                    displayName.setText(displayNameText);
                }
                Element startDate = userGroup.addElement("accountActivationStart");
                if (!StringUtil.isEmpty(startStr)) {
                    startDate.setText(startStr);
                }
                Element endDate = userGroup.addElement("accountActivationEnd");
                if (!StringUtil.isEmpty(endStr)) {
                    endDate.setText(endStr);
                }
                Element salutation = userGroup.addElement("salutation");
                if (!StringUtil.isEmpty(salutationText)) {
                    salutation.setText(salutationText);
                }
                Element firstName = userGroup.addElement("name");
                if (!StringUtil.isEmpty(firstNameText)) {
                    firstName.setText(firstNameText);
                }
//                Element lastName = userGroup.addElement("lastName");
//                if (!StringUtil.isEmpty(lastNameText)) {
//                    lastName.setText(lastNameText);
//                }
                Element organization = userGroup.addElement("organization");
                if (!StringUtil.isEmpty(organizationText)) {
                    organization.setText(organizationText);
                }
                Element division = userGroup.addElement("division");
                if (!StringUtil.isEmpty(divisionText)) {
                    division.setText(divisionText);
                }
                Element branchUnit = userGroup.addElement("branchUnit");
                if (!StringUtil.isEmpty(branchUnitText)) {
                    branchUnit.setText(branchUnitText);
                }
                Element email = userGroup.addElement("email");
                if (!StringUtil.isEmpty(emailText)) {
                    email.setText(emailText);
                }
                Element mobileNo = userGroup.addElement("mobileNo");
                if (!StringUtil.isEmpty(mobileNoText)) {
                    mobileNo.setText(mobileNoText);
                }
                Element officeNo = userGroup.addElement("officeNo");
                if (!StringUtil.isEmpty(officeTelNoText)) {
                    officeNo.setText(officeTelNoText);
                }
                Element status = userGroup.addElement("status");
                if (!StringUtil.isEmpty(statusText)) {
                    status.setText(statusText);
                }
                Element remarks = userGroup.addElement("remarks");
                if (!StringUtil.isEmpty(remarksText)) {
                    remarks.setText(remarksText);
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            File tempFile = File.createTempFile("temp", ".xml");
            XMLWriter xmlWriter = new XMLWriter(Files.newOutputStream(tempFile.toPath()), outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
            bytes = FileUtils.readFileToByteArray(tempFile);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return bytes;
    }



    @RequestMapping(value = "/userId", method = RequestMethod.POST)
    public @ResponseBody
    String[] getUserIds(HttpServletRequest request) {
        String[] arrs =  request.getParameterValues("userIds");
        request.getSession().setAttribute("userIdsSess",arrs);
        return arrs;
    }

}