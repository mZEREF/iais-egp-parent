package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author: yichen
 * @date time:12/31/2019 1:48 PM
 * @description:
 */
@Slf4j
public final class FileUtils {

    private FileUtils(){
        throw new IaisRuntimeException("FileUtils structure error.");
    }

    public static void writeFileResponseContent(final HttpServletResponse response, final File file) throws IOException {
        Objects.requireNonNull(response);
        Objects.requireNonNull(file);

        String fileName = file.getName();
        byte[] fileData = FileUtils.readFileToByteArray(file);
        Objects.requireNonNull(fileData);

        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("applicatoin/octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
    }

    public static File multipartFileToFile(final MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IaisRuntimeException("MultipartFile is null.");
        } else {
            InputStream ins = file.getInputStream();
            File toFile = new File(file.getOriginalFilename());
            copyInputStreamToFile(ins, toFile);
            return toFile;
        }
    }


    private static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file)){
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


    private static void copyInputStreamToFile(final InputStream source, final File file) {
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(source, file);
        } catch (IOException e) {
            throw new IaisRuntimeException("the file encounter an error with input by stream.");
        }

    }

    public static void deleteTempFile(final File file) {
        if (file != null) {
            File del = new File(file.toURI());
            log.debug("delete temp file uri" + file.toURI());
            boolean success = del.delete();
            if (success){
                log.info("delete temp file success");
            }else {
                log.info("delete temp file failure");
            }
        }

    }

    public static <T> List<T> transformToJavaBean(final File file, final Class<?> clz){
        List<?> objects = ExcelReader.excelReader(file, clz);
        return (List<T>) objects;
    }

    public static byte[] readFileToByteArray(final File file){
        try {
            return  org.apache.commons.io.FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new IaisRuntimeException("the file encounter an error with convert to byte[].");
        }
    }
}
