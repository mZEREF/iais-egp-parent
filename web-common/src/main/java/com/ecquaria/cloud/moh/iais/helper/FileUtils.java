package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.sz.commons.util.Calculator;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: yichen
 * @date time:12/31/2019 1:48 PM
 * @description:
 */
@Slf4j
public final class FileUtils {
    private static String DATE_FORMATTER = "yyyymmddhhmmsssss";
    public static final String EXCEL_TYPE_XSSF			= "xlsx";
    public static final String CSV_TYPE			        = "csv";

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
        response.setContentType("application/octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
    }

    public static File multipartFileToFile(final MultipartFile file, String tempFolder) throws Exception {
        return multipartFileToFile(file, tempFolder, null);
    }

    public static File multipartFileToFile(final MultipartFile file, String tempFolder, String fileName) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IaisRuntimeException("MultipartFile is null.");
        } else {
            InputStream ins = file.getInputStream();
            File sessFolder = MiscUtil.generateFolderInTempFolder("ajaxUpload" + tempFolder);
            File toFile = MiscUtil.generateFile(sessFolder,
                    StringUtil.isEmpty(fileName) ? FilenameUtils.getName(file.getOriginalFilename()) : fileName);
            copyInputStreamToFile(ins, toFile);
            return toFile;
        }
    }

    public static void copyInputStreamToFile(final InputStream source, final File file) {
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
                file.deleteOnExit();
                log.info(StringUtil.changeForLog("delete temp file failure, will be deleted after vm exits normally" + file.getName()));
                log.info(StringUtil.changeForLog("path " + file.getPath()));
            }
        }

    }

    public static String generationFileName(String fileName, String suffix) {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        String jointText = IaisEGPHelper.parseToString(date, DATE_FORMATTER) + "." + suffix;
        return fileName + "-" + jointText;
    }

    public static <T> List<T> transformCsvToJavaBean(final File file, final Class<T> clz) {
        return SimpleCsvReader.readToBean(file, clz);
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

    public static void copyFilesToOtherPosition(String src, String dst) throws IOException {
        File file = MiscUtil.generateFile(src);
        if (!file.exists()){
            log.info("don't have file");
            return;
        }

        File[] files = file.listFiles();
        if (files != null && files.length > 0){
            log.info("Start to copy ===>");
            for (File f : files){
                String srcName = f.getName();
                String path = dst + srcName;
                File dstFile = MiscUtil.generateFile(path);
                MiscUtil.deleteFile(dstFile);
                if (dstFile.createNewFile()){
                    org.apache.commons.io.FileUtils.copyFile(f, dstFile);
                }
            }
        }
    }

    public static boolean outFileSize(long fileSize){
        double sizeDbl = Calculator.div(fileSize, Calculator.mul(0x400, 0x400), 1);
        sizeDbl = Math.ceil(sizeDbl);
        if (sizeDbl > SystemParamUtil.getFileMaxLimit()) {
            return true;
        }
        return false;
    }

    public static boolean fileNameLimitBy(String fileName, int limitLength){
        if (StringUtil.isNotEmpty(fileName) && fileName.length() > limitLength){
            return true;
        }
        return false;
    }
    
    public static boolean isExcel(String originalFileName) {
        if (StringUtil.isEmpty(originalFileName)) {
            return false;
        }
        return originalFileName.toLowerCase(AppConsts.DFT_LOCALE).endsWith("." + EXCEL_TYPE_XSSF);
    }

    public static boolean isCsv(String originalFileName) {
        if (StringUtil.isEmpty(originalFileName)) {
            return false;
        }
        return originalFileName.toLowerCase(AppConsts.DFT_LOCALE).endsWith("." + CSV_TYPE);
    }

    public static String[] fileTypeToArray(String str){
        if (StringUtils.isEmpty(str)){
            return null;
        }


        return str.split(",");
    }

    public static  String getStringFromSystemConfigString(String configString){
        return getStringFromStrings(fileTypeToArray(configString));
    }

    public static String getStringFromStrings( String[] strings){
        if( strings == null) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        for(String s : strings){
            if( StringUtil.isEmpty(stringBuffer.toString())) {
                stringBuffer.append(s);
            }else {
                stringBuffer.append(',').append(s);
            }
        }
        return stringBuffer.toString();
    }

    public static  String  getFileTypeMessage(String type){
        String[] fileTypelist = type.split(",");
        if(fileTypelist.length >5) {
            StringBuilder stringBiff = new StringBuilder();
            stringBiff.append("<br/>");
            for(int indexlist = 0;indexlist <fileTypelist.length; indexlist++){
                if(indexlist== fileTypelist.length-1){
                    stringBiff .append( fileTypelist[indexlist]);
                }  else if(indexlist >0 && indexlist %5 == 0) {
                    stringBiff .append(fileTypelist[indexlist]).append(",<br/>");

                }else {
                    stringBiff.append(fileTypelist[indexlist]).append(',') ;
                }
            }
            return stringBiff.toString();
        }else {
            return "<br/>" + type;
        }
    }
}
