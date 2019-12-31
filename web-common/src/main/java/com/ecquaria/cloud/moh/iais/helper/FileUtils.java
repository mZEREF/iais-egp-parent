package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: yichen
 * @date time:12/31/2019 1:48 PM
 * @description:
 */
@Slf4j
public final class FileUtils {

    private FileUtils(){
        throw new IaisRuntimeException("FileUtils structure error");
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IaisRuntimeException("MultipartFile is null");
        } else {
            InputStream ins = file.getInputStream();
            File toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            return toFile;
        }

    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0,
                    8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }

    }
}
