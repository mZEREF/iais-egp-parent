package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Shicheng
 * @date 2020/9/17 16:12
 **/
@Delegator(value = "intrantUserRoleExport")
@Slf4j
public class ExportUserRoleDelegator {
    @Autowired
    private IntranetUserService intranetUserService;

    /**
     * StartStep: intrantUserRoleExportStart
     *
     * @param bpc
     * @throws
     */
    public void intrantUserRoleExportStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the intrantUserRoleExportStart start ...."));

    }

    /**
     * StartStep: intrantUserRoleExportDo
     *
     * @param bpc
     * @throws
     */
    public void intrantUserRoleExportDo(BaseProcessClass bpc) throws IOException, DocumentException {
        log.debug(StringUtil.changeForLog("the intrantUserRoleExportDo start ...."));
        String[] ids = (String[]) ParamUtil.getSessionAttr(bpc.request, "userIdsSess");
        if(ids!=null){
            byte[] xml = createXML(bpc,ids);
            bpc.request.setAttribute("xml", xml);
            bpc.request.setAttribute("fileName", "intranetUserRole.xml");
        }
    }

    private byte[] createXML(BaseProcessClass bpc,String [] ids) {
        byte[] bytes = null;
        try {
            List<WorkingGroupDto> workingGroups = intranetUserService.getWorkingGroups();
            //1.create dom
            Document document = DocumentHelper.createDocument();
            //2.create rootElement
            Element userGroups = document.addElement("user-groups");
            for(String id :ids){
                String maskUserId = MaskUtil.unMaskValue("maskUserId", id);
                OrgUserDto orgUserDto = intranetUserService.findIntranetUserById(maskUserId);
                List<UserGroupCorrelationDto> userGroupsByUserId = intranetUserService.getUserGroupsByUserId(maskUserId);
                if(!IaisCommonUtils.isEmpty(userGroupsByUserId)){
                    for(UserGroupCorrelationDto dto : userGroupsByUserId){
                        String groupId = dto.getGroupId();
                        String groupName = intranetUserService.getWrkGrpById(groupId);
                        Element userGroup = userGroups.addElement("user-group");
                        Element userId = userGroup.addElement("userId");
                        userId.setText(orgUserDto.getUserId());
                        Element roleId = userGroup.addElement("roleId");
                        Element workingGroupId = userGroup.addElement("workingGroupId");
                        workingGroupId.setText(groupId);
                        Integer isLeadForGroup = dto.getIsLeadForGroup();
                        if(groupName.equals("Admin Screening officer")&&isLeadForGroup==0){
                            roleId.setText(RoleConsts.USER_ROLE_ASO);
                        }else if(groupName.equals("Admin Screening officer")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_ASO_LEAD);
                        }else if(groupName.equals("Professional")&&isLeadForGroup==0){
                            roleId.setText(RoleConsts.USER_ROLE_PSO);
                        }else if(groupName.equals("Professional")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_PSO_LEAD);
                        }else if(groupName.equals("Inspection")&&isLeadForGroup==0){
                            roleId.setText(RoleConsts.USER_ROLE_INSPECTIOR);
                        }else if(groupName.equals("Inspection")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_MASTER_INSPECTION_LEAD);
                        }else if(groupName.equals("Level 1 Approval")&&isLeadForGroup==0){
                            roleId.setText(RoleConsts.USER_ROLE_AO1);
                        }else if(groupName.equals("Level 1 Approval")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_AO1_LEAD);
                        }else if(groupName.equals("Level 2 Approval")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_AO2);
                        }else if(groupName.equals("Level 2 Approval")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_AO2_LEAD);
                        }else if(groupName.equals("Level 3 Approval")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_AO3);
                        }else if(groupName.equals("Level 3 Approval")&&isLeadForGroup==1){
                            roleId.setText(RoleConsts.USER_ROLE_AO3_LEAD);
                        }
                    }
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            File tempFile = File.createTempFile("temp", ".xml");
            XMLWriter xmlWriter = new XMLWriter(Files.newOutputStream(tempFile.toPath()), outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
            bytes = FileUtils.readFileToByteArray(tempFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        return bytes;
    }
}
