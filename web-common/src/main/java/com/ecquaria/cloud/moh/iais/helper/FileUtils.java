package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.sz.commons.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
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


    public static void writeFileResponseProcessContent(HttpServletRequest request, File outputFile){
        byte[] bytes = FileUtils.readFileToByteArray(outputFile);
        request.setAttribute(IaisEGPConstant.PROCESS_DOWNLOAD_FILE_BYTE_DATA, bytes);
        request.setAttribute(IaisEGPConstant.PROCESS_DOWNLOAD_FILE_NAME, outputFile.getName());
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

    private static void copyInputStreamToFile(final InputStream source, final File file) {
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(source, file);
        } catch (IOException e) {
            throw new IaisRuntimeException("the file encounter an error with input by stream.", e);
        }

    }

    public static void deleteTempFile(final File file) {
        if (file != null) {
            log.info(StringUtil.changeForLog("delete temp file uri" + file.toURI()));
            boolean success = file.delete();
            if (success){
                log.info("delete temp file success");
            }else {
                log.info("delete temp file failure");
            }
        }

    }

    public static <T> List<T> transformToJavaBean(final File file, final Class<?> clz) throws Exception {
        List<?> objects = ExcelReader.readerToBean(file, clz);
        return (List<T>) objects;
    }

    public static List<String> transformToList(final File file, int sheetAt, Map<Integer, List<Integer>> map) throws Exception {
        return ExcelReader.readerToList(file,sheetAt, map);
    }

    public static byte[] readFileToByteArray(final File file){
        try {
            return  org.apache.commons.io.FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new IaisRuntimeException("the file encounter an error with convert to byte[].", e);
        }
    }

    public static boolean outFileSize(long fileSize){
        double sizeDbl = Calculator.div(fileSize, Calculator.mul(0x400, 0x400), 1);
        sizeDbl = Math.ceil(sizeDbl);
        if (sizeDbl > 0x10) {
            return true;
        }
        return false;
    }


    public static boolean isExcel(String originalFileName){
        if (originalFileName.endsWith("." + ExcelReader.EXCEL_TYPE_XSSF)){
            return true;
        }

        return false;
    }



}
