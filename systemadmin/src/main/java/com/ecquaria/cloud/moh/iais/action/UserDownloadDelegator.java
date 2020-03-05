package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
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
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author weilu
 * @date 2020/2/28 10:27
 */
@Delegator(value = "IntranetUserDownload")
@Slf4j
public class UserDownloadDelegator {

    @Autowired
    private IntranetUserService intranetUserService;
    public void action(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        String [] ids = (String [])ParamUtil.getSessionAttr(bpc.request, "ids");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        byte[] xml = createXML(bpc,ids);
        bpc.request.setAttribute("xml", xml);
    }
    private byte[] createXML(BaseProcessClass bpc,String [] ids) {
        byte[] bytes = null;
        try {
            //1.create dom
            Document document = DocumentHelper.createDocument();
            //2.create rootElement
            Element userGroups = document.addElement("user-groups");
            for(String id :ids){
                OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(id);
                String userIdText = orgUserDto.getUserId();
                String displayNameText = orgUserDto.getDisplayName();
                String userDomainText = orgUserDto.getUserDomain();
                String salutationText = orgUserDto.getSalutation();
                Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
                Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
                String startStr = DateUtil.formatDate(accountActivateDatetime, "yyyy-MM-dd");
                String endStr = DateUtil.formatDate(accountDeactivateDatetime, "yyyy-MM-dd");
                String firstNameText = orgUserDto.getFirstName();
                String lastNameText = orgUserDto.getLastName();
                String organizationText = orgUserDto.getOrganization();
                String mobileNoText = orgUserDto.getMobileNo();
                String officeTelNoText = orgUserDto.getOfficeTelNo();
                String branchUnitText = orgUserDto.getBranchUnit();
                String emailText = orgUserDto.getEmail();
                String statusText = orgUserDto.getStatus();
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
                Element startDate = userGroup.addElement("startDate");
                if (!StringUtil.isEmpty(startStr)) {
                    startDate.setText(startStr);
                }
                Element endDate = userGroup.addElement("endDate");
                if (!StringUtil.isEmpty(endStr)) {
                    endDate.setText(endStr);
                }
                Element salutation = userGroup.addElement("salutation");
                if (!StringUtil.isEmpty(salutationText)) {
                    salutation.setText(salutationText);
                }
                Element firstName = userGroup.addElement("firstName");
                if (!StringUtil.isEmpty(firstNameText)) {
                    firstName.setText(firstNameText);
                }
                Element lastName = userGroup.addElement("lastName");
                if (!StringUtil.isEmpty(lastNameText)) {
                    lastName.setText(lastNameText);
                }
                Element mobileNo = userGroup.addElement("mobileNo");
                if (!StringUtil.isEmpty(mobileNoText)) {
                    mobileNo.setText(mobileNoText);
                }
                Element officeNo = userGroup.addElement("officeNo");
                if (!StringUtil.isEmpty(officeTelNoText)) {
                    officeNo.setText(officeTelNoText);
                }
                Element email = userGroup.addElement("email");
                if (!StringUtil.isEmpty(emailText)) {
                    email.setText(emailText);
                }
                Element organization = userGroup.addElement("organization");
                if (!StringUtil.isEmpty(organizationText)) {
                    organization.setText(organizationText);
                }
                Element branchUnit = userGroup.addElement("branchUnit");
                if (!StringUtil.isEmpty(branchUnitText)) {
                    branchUnit.setText(branchUnitText);
                }
                Element status = userGroup.addElement("status");
                if (!StringUtil.isEmpty(statusText)) {
                    status.setText(statusText);
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            File tempFile = File.createTempFile("temp", ".xml");
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(tempFile), outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
            bytes = FileUtils.readFileToByteArray(tempFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}