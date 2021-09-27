package com.ecquaria.cloud.moh.iais.rest;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * MoveFileController
 *
 * @author Jinhua
 * @date 2021/9/17 15:50
 */
@Slf4j
@RestController
@RequestMapping(value = "moveFile")
public class MoveFileController {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;

    @DeleteMapping()
    public ResponseEntity<String> moveFile(@RequestParam("filePathName") String filePathName) {
        File moveFile = MiscUtil.generateFile(filePathName);
        if (moveFile != null && moveFile.exists()) {
            String name = moveFile.getName();
            log.info(StringUtil.changeForLog("file name is  {}"+name));
            File outFile = MiscUtil.generateFile(sharedPath + File.separator + "move", name);
            try (OutputStream fileOutputStream = newOutputStream(outFile.toPath());
                 InputStream fileInputStream = newInputStream(moveFile.toPath())) {
                int count;
                byte []size=new byte[1024];
                count = fileInputStream.read(size);
                while (count != -1) {
                    fileOutputStream.write(size,0,count);
                    count= fileInputStream.read(size);
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                throw new IaisRuntimeException(e);
            }
            MiscUtil.deleteFile(moveFile);

            return ResponseEntity.ok().body("success");
        }

        return ResponseEntity.ok().body("no file");
    }
}
