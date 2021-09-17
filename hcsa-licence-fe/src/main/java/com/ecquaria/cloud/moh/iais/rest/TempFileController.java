package com.ecquaria.cloud.moh.iais.rest;

import com.ecquaria.cloud.moh.iais.common.annotation.LogInfo;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * TempFileController
 *
 * @author Jinhua
 * @date 2021/9/16 15:56
 */
@Slf4j
@RestController
@RequestMapping(value = "tempFile-handler")
public class TempFileController {
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Save the file at once")
    @LogInfo(funcName = "Save file")
    public ResponseEntity<String> saveFileToFolder(
            @RequestPart("selectedFile") MultipartFile selectedFile,
            @ApiParam(name = "fileName", required = true)
            @RequestParam("fileName") String fileName,
            @ApiParam(name = "folderName", required = true)
            @RequestParam("folderName") String folderName) {
        File folder = MiscUtil.generateFolderInTempFolder(folderName);
        File file = MiscUtil.generateFile(folder, fileName);
        try (OutputStream fos = Files.newOutputStream(file.toPath())) {
            log.info(StringUtil.changeForLog("The file pathName ==> " + file.getCanonicalPath()));
            fos.write(selectedFile.getBytes());
            fos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return ResponseEntity.ok().body("success");
    }
}
