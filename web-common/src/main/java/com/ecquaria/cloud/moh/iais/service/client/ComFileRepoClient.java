package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * ComFileRepoClient
 *
 * @author Jinhua
 * @date 2021/3/11 10:27
 */
@Component
@Slf4j
public class ComFileRepoClient {
    @Autowired
    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate;

    public List<String> saveFileRepo(List<File> files) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        if (IaisCommonUtils.isEmpty(files)) {
            return null;
        }
        for (File file : files) {
            HttpHeaders fileHeader = new HttpHeaders();
            try (InputStream fis = Files.newInputStream(file.toPath());
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] con = new byte[1024];
                int count = fis.read(con);
                while (count != -1) {
                    baos.write(con, 0, count);
                    count = fis.read(con);
                }

                byte[] content =  baos.toByteArray();
                ByteArrayResource fileContentAsResource = new ByteArrayResource(content){
                    @Override
                    public String getFilename() {
                        return file.getName();
                    }
                };
                HttpEntity<ByteArrayResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                multipartRequest.add("selectedFiles", fileEnt);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
            }
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);

        AuditTrailDto dto = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            dto = (AuditTrailDto) ParamUtil.getSessionAttr(attributes.getRequest(), AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        }
        if (dto == null) {
            dto = AuditTrailDto.getThreadDto();
        }

        if (dto != null) {
            headers.add("currentAuditTrail", JsonUtil.parseToJson(dto));
        }

        return restTemplate.postForObject("http://file-repository/saveFiles", requestEntity, List.class);
    }
}
