package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangyu and chenlei on 4/19/2022.
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileAjaxController {

    @Autowired
    private SystemParamConfig systemParamConfig;

    @PostMapping(value = "ajax-upload-file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> ajaxUpload(HttpServletRequest request, /*@RequestParam("selectedFile") */MultipartFile selectedFile,
            @RequestParam("fileAppendId") String fileAppendId, @RequestParam("uploadFormId") String uploadFormId,
            @RequestParam("reloadIndex") int reloadIndex,
            @RequestParam(value = "needGlobalMaxIndex", required = false) boolean needMaxGlobal) {
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX
                + fileAppendId);
        int size = 0;
        if (needMaxGlobal && ParamUtil.getSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR) != null) {
            size = (int) ParamUtil.getSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        }
        if (map == null) {
            map = IaisCommonUtils.genNewHashMap();
        } else if (size <= 0) {
            size = (Integer) ParamUtil.getSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX
                    + fileAppendId + IaisEGPConstant.SEESION_FILES_MAP_AJAX_MAX_INDEX);
        }
        String needReUpload = ParamUtil.getString(request, "_needReUpload");
        String fileType = ParamUtil.getString(request, "_fileType");
        String fileMaxSize = ParamUtil.getString(request, "fileMaxSize");
        int maxSize = 0;
        if (StringUtil.isDigit(fileMaxSize)) {
            maxSize = Integer.parseInt(fileMaxSize);
        }
        String errorMessage = getErrorMessage(selectedFile, fileType, maxSize);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(errorMessage)) {
            result.put("msgType", "N");
            result.put("description", errorMessage);
            return result;
        } else {
            result.put("msgType", "Y");
        }
        File toFile;
        String tempFolder;
        try {
            String toFileName = FilenameUtils.getName(selectedFile.getOriginalFilename());
            if (reloadIndex == -1) {
                ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId
                        + IaisEGPConstant.SEESION_FILES_MAP_AJAX_MAX_INDEX, size + 1);
                if (needMaxGlobal) {
                    ParamUtil.setSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, size + 1);
                }
                tempFolder = request.getSession().getId() + fileAppendId + size;
                toFile = FileUtils.multipartFileToFile(selectedFile, tempFolder, toFileName);
                map.put(fileAppendId + size, toFile);
            } else {
                tempFolder = request.getSession().getId() + fileAppendId + reloadIndex;
                toFile = FileUtils.multipartFileToFile(selectedFile, tempFolder, toFileName);
                map.put(fileAppendId + reloadIndex, toFile);
                size = reloadIndex;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("----------change MultipartFile to file  falie-----------------");
            result.put("msgType", "N");
            result.put("description", "Cannot upload the file");
            return result;
        }
        // Save File to other nodes
        saveFileToOtherNodes(selectedFile, toFile, tempFolder);

        ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId, (Serializable) map);
        String html = getFileShowHtml(selectedFile.getOriginalFilename(), fileAppendId, size, uploadFormId,
                !AppConsts.NO.equals(needReUpload), request);
        result.put("description", html);
        checkAddtionalData(result, toFile, request);
        log.info("-----------ajax-upload-file end------------");
        return result;
    }

    private void checkAddtionalData(Map<String, Object> result, File toFile, HttpServletRequest request) {
        // premise - co-location non-licenced
        boolean forNonLicenced = !IaisCommonUtils.isEmpty(ParamUtil.getStrings(request, "premType"));
        boolean isUpload = !IaisCommonUtils.isEmpty(ParamUtil.getStrings(request, "isUpload"));
        if (forNonLicenced) {
            parsFile(result, toFile, "appPremNonLicRelationDtos", 2, (data) -> {
                AppPremNonLicRelationDto dto = new AppPremNonLicRelationDto();
                dto.setBusinessName(data.get(0));
                dto.setProvidedService(data.get(1));
                return dto;
            });
        }
        if (isUpload) {
            parsFile(result, toFile, "appSvcPersonnelDto", 19, (data) -> {
                AppSvcPersonnelDto dto = new AppSvcPersonnelDto();
                dto.setSalutation(data.get(0));
                dto.setName(data.get(1));
                dto.setDesignation(data.get(2));
                dto.setOtherDesignation(data.get(3));
                dto.setProfessionBoard(data.get(4));
                dto.setProfessionType(data.get(5));
                dto.setProfRegNo(data.get(6));
                dto.setSpeciality(data.get(7));
                dto.setSubSpeciality(data.get(8));
                dto.setSpecialityOther(data.get(9));
                dto.setSpecialtyGetDate(data.get(10));
                dto.setTypeOfCurrRegi(data.get(11));
                dto.setCurrRegiDate(data.get(12));
                dto.setPraCerEndDate(data.get(13));
                dto.setTypeOfRegister(data.get(14));
                dto.setQualification(data.get(15));
                dto.setWrkExpYear(data.get(16));
                dto.setBclsExpiryDate(data.get(17));
                dto.setCprExpiryDate(data.get(18));
                return dto;
            });
            // others
        }
    }

    private <T> void parsFile(Map<String, Object> result, File toFile, String name, int fieldCount, Function<List<String>, T> func) {
        List<List<String>> data = null;
        try {
            data = ExcelReader.readerToList(toFile, 0, 1, null);
        } catch (Exception e) {
            log.warn(StringUtil.changeForLog(e.getMessage()), e);
        }
        if (IaisCommonUtils.isEmpty(data) || IaisCommonUtils.isEmpty(data.get(0))) {
            // PRF_ERR006 - No records found.
            result.put("description", MessageUtil.getMessageDesc("PRF_ERR006"));
            result.put("msgType", "N");
        } else if (data.get(0).size() < fieldCount) {
            // GENERAL_ERR0070 - Could not parse file content. Please download new template to do this.
            result.put("description", MessageUtil.getMessageDesc("GENERAL_ERR0070"));
            result.put("msgType", "N");
        } else {
            // data
            List<T> objList = data.stream().map(func)
                    .collect(Collectors.toList());
            result.put(name, objList);
        }
    }

    private String getFileShowHtml(String fileName, String fileAppendId, int index, String uploadFormId,
            boolean needReUpload, HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        if (fileName != null) {
            boolean isBackend = AppConsts.USER_DOMAIN_INTRANET.equals(ConfigHelper.getString("iais.current.domain"));
            log.info(StringUtil.changeForLog("isBackend: " + isBackend));
            String cssClass;
            if (isBackend) {
                cssClass = "btn btn-secondary-del btn-sm";
            } else {
                cssClass = "btn btn-secondary btn-sm";
            }
            String[] fileSplit = fileName.split("\\.");
            String csrfToken = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
            data.append("<div ").append(" id ='").append(fileAppendId).append("Div").append(index).append("' >")
                    .append("<a href=\"").append(request.getContextPath())
                    .append("/file/download-session-file?fileAppendIdDown=").append(fileAppendId)
                    .append("&fileIndexDown=").append(index)
                    .append("&OWASP_CSRFTOKEN=").append(StringUtil.getNonNull(csrfToken))
                    .append("\" title=\"Download\" class=\"downloadFile\">")
                    .append(IaisCommonUtils.getDocNameByStrings(fileSplit))
                    .append('.').append(fileSplit[fileSplit.length - 1])
                    .append("</a>")
                    .append(" <button type=\"button\" class=\"").append(cssClass)
                    .append("\" onclick=\"javascript:deleteFileFeAjax('")
                    .append(fileAppendId).append("', ").append(index)
                    .append(");\">Delete</button>");
            if (needReUpload) {
                data.append(" <button type=\"button\" class=\"")
                        .append(cssClass)
                        .append("\" onclick=\"javascript:reUploadFileFeAjax('")
                        .append(fileAppendId).append("', ").append(index)
                        .append(", '").append(uploadFormId)
                        .append("');\">ReUpload</button>");
            }
            data.append("</div>");
        }
        return data.toString();
    }

    @RequestMapping(value = "/deleteFeCallFile", method = RequestMethod.POST)
    public String deleteFeCallFile(HttpServletRequest request) {
        log.info("-----------deleteFeCallFile start------------");
        String fileAppendId = ParamUtil.getString(request, "fileAppendId");
        String index = ParamUtil.getString(request, "fileIndex");
        if (!StringUtil.isEmpty(fileAppendId) && !StringUtil.isEmpty(index)) {
            Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,
                    IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
            if (!IaisCommonUtils.isEmpty(map)) {
                log.info(StringUtil.changeForLog("------ fileAppendId : " + fileAppendId + "-----------"));
                log.info(StringUtil.changeForLog("------ fileAppendIndex : " + index + "-----------"));
                map.remove(fileAppendId + index);
                ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId, (Serializable) map);
            }
        }
        log.info("-----------deleteFeCallFile end------------");

        return AppConsts.YES;
    }

    private String getErrorMessage(MultipartFile selectedFile, String fileTypesString, int maxSize) {
        if (selectedFile == null || selectedFile.isEmpty()) {
            return MessageUtil.getMessageDesc("GENERAL_ACK018");
        }
        if (maxSize <= 0) {
            maxSize = systemParamConfig.getUploadFileLimit();
        }
        if (StringUtil.isEmpty(fileTypesString)) {
            fileTypesString = systemParamConfig.getUploadFileType();
        }
        log.info(StringUtil.changeForLog("File Type: " + fileTypesString));
        List<String> fileTypes = Arrays.asList(fileTypesString.split("\\s*,\\s*"));
        Map<String, Boolean> booleanMap = ValidationUtils.validateFile(selectedFile, fileTypes, (maxSize * 1024 * 1024L));
        Boolean fileSize = booleanMap.get("fileSize");
        Boolean fileType = booleanMap.get("fileType");
        //size
        if (!fileSize) {
            return MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(maxSize), "sizeMax");
        }
        //type
        if (!fileType) {
            String type = FileUtils.getFileTypeMessage(fileTypesString);
            return MessageUtil.replaceMessage("GENERAL_ERR0018", type, "fileType");
        }

        //name
        String orginName = selectedFile.getOriginalFilename();
        if (orginName != null) {
            String[] fileSplit = orginName.split("\\.");
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit) + "." + fileSplit[fileSplit.length - 1];
            if (fileName.length() > 100) {
                return MessageUtil.getMessageDesc("GENERAL_ERR0022");
            }
        }
        return "";
    }

    @RequestMapping(value = "/download-session-file", method = RequestMethod.GET)
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("download-session-file start ...."));

        String fileAppendId = ParamUtil.getString(request, "fileAppendIdDown");
        String index = ParamUtil.getString(request, "fileIndexDown");

        if (!StringUtil.isEmpty(fileAppendId) && !StringUtil.isEmpty(index)) {
            Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,
                    IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
            if (!IaisCommonUtils.isEmpty(map)) {
                log.info(StringUtil.changeForLog("------ fileAppendId : " + fileAppendId + "-----------"));
                log.info(StringUtil.changeForLog("------ fileAppendIndex : " + index + "-----------"));
                File file = map.get(fileAppendId + index);
                if (file != null) {
                    byte[] fileData = FileUtils.readFileToByteArray(file);
                    if (fileData != null) {
                        try {
                            String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
                            response.addHeader("Content-Disposition", "attachment;filename=\""
                                    + fileName.replaceAll("\\+", "%20") + "\"");
                            response.addHeader("Content-Length", "" + fileData.length);
                            response.setContentType("application/x-octet-stream");
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        OutputStream ops = null;
                        try {
                            ops = new BufferedOutputStream(response.getOutputStream());
                            ops.write(fileData);
                            ops.flush();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            if (ops != null) {
                                ops.close();
                            }
                        }
                    }
                    return;
                } else {
                    log.info(StringUtil.changeForLog("------no find file :" + fileAppendId + index + " parh -----------"));
                }
                ParamUtil.setSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId, (Serializable) map);
            }
        }
        log.debug(StringUtil.changeForLog("download-session-file end ...."));
    }

    private void saveFileToOtherNodes(MultipartFile selectedFile, File toFile, String tempFolder) {
        List<String> ipAddrs = ServicesSysteminfo.getInstance().getAddressesByServiceName("hcsa-licence-web");
        if (ipAddrs != null && ipAddrs.size() > 1 && toFile != null) {
            String localIp = MiscUtil.getLocalHostExactAddress();
            log.info(StringUtil.changeForLog("Local Ip is ==>" + localIp));
            RestTemplate restTemplate = new RestTemplate();
            for (String ip : ipAddrs) {
                if (localIp.equals(ip)) {
                    continue;
                }
                try {
                    String port = ConfigHelper.getString("server.port", "8080");
                    StringBuilder apiUrl = new StringBuilder("http://");
                    apiUrl.append(ip).append(':').append(port).append("/hcsa-licence-web/tempFile-handler");
                    log.info("Request URL ==> {}", apiUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
                    HttpHeaders fileHeader = new HttpHeaders();
                    byte[] content = selectedFile.getBytes();
                    String fileName = StringUtil.obscured(toFile.getName());
                    ByteArrayResource fileContentAsResource = new ByteArrayResource(content) {
                        @Override
                        public String getFilename() {
                            return fileName;
                        }
                    };
                    HttpEntity<ByteArrayResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                    multipartRequest.add("selectedFile", fileEnt);
                    HttpHeaders jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> jsonPart = new HttpEntity<>(fileName, jsonHeader);
                    multipartRequest.add("fileName", jsonPart);
                    jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    jsonPart = new HttpEntity<>("ajaxUpload" + tempFolder, jsonHeader);
                    multipartRequest.add("folderName", jsonPart);
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
                    restTemplate.postForObject(apiUrl.toString(), requestEntity, String.class);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @PostMapping("/init")
    public String initFileData() {
        Map<String, String> data = new HashMap<>(3);
        String fileMaxSize = String.valueOf(SystemParamUtil.getSystemParamConfig().getUploadFileLimit());
        data.put("fileMaxSize", fileMaxSize);
        String fileMaxMessage = MessageUtil.replaceMessage("GENERAL_ERR0019", fileMaxSize, "sizeMax");
        data.put("fileMaxMBMessage", fileMaxMessage);
        String fileType = String.valueOf(SystemParamUtil.getSystemParamConfig().getUploadFileType());
        data.put("fileType", fileType);
        return JsonUtil.parseToJson(data);
    }


}
