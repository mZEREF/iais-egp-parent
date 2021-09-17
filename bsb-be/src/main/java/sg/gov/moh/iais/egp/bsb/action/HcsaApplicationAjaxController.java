package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class HcsaApplicationAjaxController {

    private static final String FACILITY = "facility";

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private DocClient docClient;

    //upload file
    @RequestMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadInternalFile(HttpServletRequest request, @RequestParam("selectedFile") MultipartFile selectedFile, @RequestParam("fileRemark")String remark) {
        String data = "";
        request.setAttribute("selectedFile", selectedFile);
        String CSRF = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
//        HcsaApplicationUploadFileValidate uploadFileValidate = new HcsaApplicationUploadFileValidate();
//        Map<String, String> errorMap = uploadFileValidate.validate(request);
//        if(!errorMap.isEmpty()){
//            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
//            appIntranetDocDto.setFileSn(-1);
//            appIntranetDocDto.setNoFilesMessage(errorMap.get("selectedFile"));
//            return  JsonUtil.parseToJson(appIntranetDocDto);
//        }else{
        //As a method parameter
        FacilityDoc facilityDoc = new FacilityDoc();
        //Only used for page display
        FacilityDoc doc = new FacilityDoc();
        if (selectedFile != null && !StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
            //size
            long size = selectedFile.getSize();
            facilityDoc.setSize(size / 1024);
            doc.setSize(size / 1024);
            log.info(StringUtil.changeForLog("HcsaApplicationAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
            //type
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
            String fileType = fileSplit[fileSplit.length - 1];
            doc.setDocType(fileType);
            //name
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit);
            facilityDoc.setName(fileName);
            doc.setName(fileName);

            doc.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            Facility facility=new Facility();
            Facility facility1 = (Facility) ParamUtil.getSessionAttr(request, FACILITY);
            facility.setId(facility1.getId());
            facilityDoc.setFacility(facility);
            doc.setFacility(facility);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            facilityDoc.setSubmitAt(new Date());
            doc.setSubmitAt(new Date());
            doc.setSubmitAtStr(formatter.format(doc.getSubmitAt()));
            facilityDoc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            doc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            doc.setSubmitByName(doc.getAuditTrailDto().getMohUserId());

            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setFileName(fileName);
            fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fileRepoDto.setRelativePath(AppConsts.FALSE);

            //save file to file DB
            String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
            facilityDoc.setFileRepoId(repo_id);
            doc.setFileRepoId(repo_id);
            //            appIntranetDocDto.set
            String id = docClient.saveFacilityDoc(facilityDoc).getEntity();
            facilityDoc.setId(id);
            doc.setId(id);


            // set auditDocDto to seesion
            AuditDocDto auditDocDto = (AuditDocDto)ParamUtil.getSessionAttr(request,"auditDocDto");
            if (auditDocDto==null){
                auditDocDto = new AuditDocDto();
            }
            List<FacilityDoc> facilityDocs;
            if(auditDocDto != null && auditDocDto.getFacilityDocs() != null){
                facilityDocs = auditDocDto.getFacilityDocs();
            }else {
                facilityDocs = new ArrayList<>(5);
            }
            Integer index = (Integer) ParamUtil.getSessionAttr(request,"AppIntranetDocDtoIndex");
            int fileSizes = facilityDocs.size();
            if(index == null){
                index = fileSizes;
            }else {
                index++;
            }
            ParamUtil.setSessionAttr(request,"AppIntranetDocDtoIndex",index);
            String  mask =MaskUtil.maskValue("fileRo"+index, facilityDoc.getFileRepoId());
            String url ="<a href=\"pageContext.request.contextPath/file-repo?filerepo=fileRostatus.index&fileRostatus.index=maskDec&fileRepoName=interalFile.docName&OWASP_CSRFTOKEN=csrf\" title=\"Download\" class=\"downloadFile\">";
            try{
                String docName = selectedFile.getOriginalFilename() == null ? "" : URLEncoder.encode(selectedFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                url= url.replaceAll("pageContext.request.contextPath","/bsb-be").replaceAll("status.index",String.valueOf(index)).
                        replaceAll("interalFile.docName", docName).replaceAll("maskDec",mask).replaceAll("csrf",CSRF);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            doc.setUrl(url);
//            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
//            doc.setFileSn((serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? 999:fileSizes);
            doc.setFileSn(fileSizes==0 ? 999:fileSizes);
            doc.setIsUpload(Boolean.TRUE);
            facilityDocs.add(doc);
            if (auditDocDto != null){
                auditDocDto.setFacilityDocs(facilityDocs);
                auditDocDto.setIsUpload(Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request,"auditDocDto", auditDocDto);
            //call back upload file succeeded
            if( !StringUtil.isEmpty( facilityDoc.getId())){
//                facilityDoc.setMaskId(MaskUtil.maskValue("interalFileId", facilityDoc.getId()));
            }
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(doc);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
        }
//    }
        return data;
    }

    @RequestMapping(value = "/deleteInternalFile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteInternalFile(HttpServletRequest request){
        String guid = request.getParameter("appDocId");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        AuditDocDto auditDocDto = (AuditDocDto)ParamUtil.getSessionAttr(request,"auditDocDto");
        if(auditDocDto != null && auditDocDto.getFacilityDocs() != null){
            List<FacilityDoc> facilityDocs = auditDocDto.getFacilityDocs();
            FacilityDoc facilityDoc  = null;
            for(FacilityDoc doc : facilityDocs){
                if(doc.getFileRepoId().equalsIgnoreCase(guid)){
                    docClient.deleteByFileRepoId( doc.getFileRepoId());
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setId(doc.getFileRepoId());
                    fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fileRepoClient.removeFileById(fileRepoDto);
                    facilityDoc = doc;
                }
            }
            if(facilityDoc!= null)
                facilityDocs.remove( facilityDoc);
            boolean isUpload = false;
            for(FacilityDoc facilityDoc1 : facilityDocs){
                if(facilityDoc1.getIsUpload() != null && facilityDoc1.getIsUpload()){
                    isUpload = true;
                    break;
                }
            }
            auditDocDto.setIsUpload(isUpload);
            ParamUtil.setSessionAttr(request,"auditDocDto", auditDocDto);
            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
            map.put("fileSn", (serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? -1 : facilityDocs.size());
            if(facilityDocs.size() == 0){
                map.put("noFilesMessage", MessageUtil.getMessageDesc("GENERAL_ACK018"));
            }
        }
        return map;
    }


    @RequestMapping(value = "/verifyFileExist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verifyFileExist(HttpServletRequest request){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String reportId  = ParamUtil.getString(request,"repoId");
        if(StringUtil.isEmpty(reportId)){
            map.put("verify","N");
            return map;
        }
        byte[] data = fileRepoClient.getFileFormDataBase(reportId).getEntity();;
        if(data == null){
            map.put("verify","N");
            return map;
        }
        map.put("verify","Y");
        return map;
    }

    @GetMapping(value = "/file-repo")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
//        String fileRepoName = "NewTextdocument";
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
//        String fileRepoId = "687F55D0-5B17-EC11-BE6E-000C298D317C";
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName+"\"");
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
}
