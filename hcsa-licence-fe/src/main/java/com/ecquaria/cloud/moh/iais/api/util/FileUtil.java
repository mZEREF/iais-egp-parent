/*
 * This file is generated by ECQ project skeleton automatically at Sep 19, 2014.
 *
 * Copyright 2014-2015, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 * No part of this material may be copied, reproduced, transmitted,
 * stored in a retrieval system, reverse engineered, decompiled,
 * disassembled, localised, ported, adapted, varied, modified, reused,
 * customised or translated into any language in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise,
 * without the prior written permission of Ecquaria Technologies Pte Ltd.
 */
package com.ecquaria.cloud.moh.iais.api.util;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.newInputStream;

/**
 * FileUtil.java
 * 
 * @author zhuhua
 */
@Slf4j
public class FileUtil {

	
	public static File[] listFilesWithPrefix(File file, final String prefix){
		if(file != null && file.isDirectory()){
			return file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String fileName) {
					if(fileName.toLowerCase().startsWith(prefix.toLowerCase())){
						return true;
					}else{ 
						return false;
					}
				}
			});
		}else{
			return new File[0];
		}
	}

	public static void appendToFile(String string, String fileName) {
		FileWriter writer = null;
		try {
			File file=MiscUtil.generateFile(fileName);
			writer = new FileWriter(file,true);
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	public static boolean writeToFile(String path,String fileName,String data){
		boolean result = false;
		if (fileName == null || fileName.trim().length() == 0){
			return result;
		}
		File f = MiscUtil.generateFile(path,fileName);
		return writeToFileFileByData(f,data);
	}

	public static boolean writeToFile(String fileName, String data) {
		boolean result = false;
		if (fileName == null || fileName.trim().length() == 0){
			return result;
		}

		log.info(StringUtil.changeForLog("----------- writeToFile fileName :"+ fileName +"-----------"));
		String[] fileNames = fileName.split(ConfigHelper.getString("giro.sftp.linux.seperator"));
		File f = MiscUtil.generateFile(fileName.substring(0, fileName.lastIndexOf(ConfigHelper.getString("giro.sftp.linux.seperator"))),fileNames[fileNames.length-1]);
		log.info(StringUtil.changeForLog("----- file :" +f.toPath() +" ---"));
		log.info(StringUtil.changeForLog("----- file.getAbsolutePath() :" +f.getAbsolutePath() +" --"));
		log.info(StringUtil.changeForLog("----- file.getPath() :" +f.getPath() +" -"));
	    return writeToFileFileByData(f,data);
	}

	public static boolean  writeToFileFileByData( File f,String data){
		log.info("---------- writeToFileFileByData start ----------");
		boolean result = false;
		if (f.exists()){
			return result;
		}else if (f.isDirectory()){
			return result;
		}else {
			PrintWriter pw = null;
			try {
				if (f.createNewFile()) {
					pw = new PrintWriter(f);
					pw.write(data);
					pw.flush();
					result = true;
				}
			} catch (Exception e) {
				log.error(StringUtil.changeForLog("write file for " + f.getName() + " error!" + e.getMessage()));
				log.error(e.getMessage(), e);
				result = false;
			} finally {
				if (pw != null){
					pw.close();
				}
			}
		}
		log.info("---------- writeToFileFileByData end ----------");
		return result;
	}
	public static void generateFolder(String folderPath) {
		File f= MiscUtil.generateFile(folderPath);
		if (!f.isFile() && !f.exists()) {
			 if(f.mkdirs()){
				 log.info(StringUtil.changeForLog("create folder:" + folderPath));
			 }else {
				 log.info(StringUtil.changeForLog("create folder:" + folderPath + " have failed"));
			 }
		}
	}

	public static String getString(InputStream is) throws Exception {
		return getString(is, "UTF-8");
	}

	public static String getString(String fileName) throws Exception {
		File f= MiscUtil.generateFile(fileName);
		if (f.exists() && f.isFile()) {
			String xml;
			try(InputStream is = newInputStream(f.toPath())) {
				xml = getString(is);
				if (!f.delete()) {
					log.debug(StringUtil.changeForLog(fileName + " is inexistence in service."));
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				return null;
			}
			return xml;
		} else {
			throw new Exception(StringUtil.changeForLog("FileUtil: " + fileName
					+ ", is not exist or not a file."));
		}
	}

	public static void generateTempFile(File f, String content) throws Exception {
		int length = 0;
		char[] buffer = new char[2048];
		try (BufferedReader br = new BufferedReader(new StringReader(content));
			 BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			while ((length = br.read(buffer)) != -1) {
				bw.write(new String(buffer, 0, length));
			}
		}
	}

	public static String getString(InputStream is, String charsetName) throws Exception {
		if (is == null){
			return null;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static String getFileBaseName(String absolutePath) {
		if (StringUtil.isEmpty(absolutePath)){
			return null;
		}
		
		int startIndex = absolutePath.lastIndexOf(File.separator) + 1;
		int endIndex = absolutePath.lastIndexOf('.');
		return absolutePath.substring(startIndex, endIndex);
	}

	public static String generateFileName(String originalFileName) {
		String fileExt = getFileExtension(originalFileName);
		return System.nanoTime() + "." + fileExt;
	}

	public static String getFileExtension(String fileName) {
		int dotPos = fileName.lastIndexOf('.');
		if (dotPos == -1){
			return null;
		}
		dotPos ++;
		return fileName.substring(dotPos);
	}

	public static String getFileUrl(String filePath) {
		return ConfigHelper.getString("file.server.url") + filePath;
	}

	public static boolean copyFile(String source, String target) {
		return copyFile(MiscUtil.generateFile(source), MiscUtil.generateFile(target));
	}

	public static boolean copyFile(File source, File target) {
		if (!source.exists()) {
			log.debug(StringUtil.changeForLog("source file :" + source.getName() + " not exist!"));
			return false;
		}
		if (!target.getParentFile().exists()) {
			generateFolder(target.getParentFile().getPath());
		}
		InputStream inStream = null;
	    OutputStream fs = null;
		try {
			int byteread = 0;
			if (source.exists()) {
				inStream = newInputStream(source.toPath());
				fs =  Files.newOutputStream(target.toPath());
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return target.exists();
	}

	/**
	 * get the byte array from the given file
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromFile(String fileName) {
		byte[] content = null;
		if(fileName != null){
			File inFile = MiscUtil.generateFile(fileName);
			content = readBytesFromFile(inFile);
		}

		return content;
	}

	/**
	 * @description: Get the byte array from the given file
	 *
	 * @author: Jinhua on 2022/11/7 14:15
	 * @param: [file]
	 * @return: byte[]
	 */
	public static byte[] readBytesFromFile(File file) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if(file != null && file.exists()){
			try(InputStream fis = newInputStream(file.toPath())) {
				byte[] b = new byte[1024];
				int n = fis.read(b);
				while(n != -1){
					out.write(b, 0, n);
					n = fis.read(b);
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				return out.toByteArray();
			} finally {
				try {
					out.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
		}

		return out.toByteArray();
	}

	private FileUtil() {
	}

	public static String getContentByPostfixNotation(String postfixNotation,String downPath, List<String> remoteFileNames) throws Exception{
		if(IaisCommonUtils.isEmpty(remoteFileNames)){
			return "";
		}
		for(String remoteFileName : remoteFileNames){
			if(remoteFileName.contains(postfixNotation)){
				return FileUtil.getString(downPath + remoteFileName);
			}
		}
		return "";
	}

	public static String getFileName(List<String> remoteFileNames,String postfixNotation){
		if(IaisCommonUtils.isEmpty(remoteFileNames)){
			return "";
		}
		for(String remoteFileName : remoteFileNames){
			if(remoteFileName.contains(postfixNotation)){
				return remoteFileName;
			}
		}
		return "";
	}

	public static List<String> getRemoteFileNames(String fileName, String remotePath){
		File[] files = MiscUtil.generateFile(remotePath).listFiles();
		if(files == null){
			return null;
		}
		List<String> remoteFileNames = new ArrayList<>(3);
		for(File file :files) {
			String sftpName = file.getName();
			if (sftpName.indexOf(fileName) != -1) {
				remoteFileNames.add(sftpName);
			}

			String sftpfullName = sftpName;
			String[] sftpfullNmaes = sftpName.split("\\.");
			if (sftpfullNmaes != null && sftpfullNmaes.length > 0) {
				sftpfullName = sftpfullNmaes[0];
			}
			if (fileName.equals(sftpfullName)) {
				remoteFileNames.clear();
				remoteFileNames.add(sftpName);
				break;
			}
			//
		}
		return remoteFileNames;
	}

	public static void deleteFilesByFileNames(List<String> fileNames,String downPath){
		if(!IaisCommonUtils.isEmpty(fileNames)){
			log.info(StringUtil.changeForLog("------------downpath :" + downPath + "----------------"));
			File[] files = MiscUtil.generateFile(downPath).listFiles();
			if(files != null){
				for(File file :files){
					for(String fileName : fileNames){
						if(fileName.equalsIgnoreCase(file.getName())){
							if(file.isFile()){
								if(!file.delete()){
									log.debug(StringUtil.changeForLog(fileName + " is inexistence in service."));
								}
							}
						}
					}

				}
			}else {
				log.info(StringUtil.changeForLog("------------downpath :" + downPath + " is no files----------------"));
			}
		}
	}

	public static boolean checkFileNamesExistKeyWords(List<String> fileNames,String ... keyWord){
		if(IaisCommonUtils.isEmpty(fileNames)){
			return false;
		}
		String remoteFileNames = fileNames.toString();
		for (int i = 0; i < keyWord.length; i++) {
			if(remoteFileNames.contains(keyWord[i])){
				return true;
			}
		}
		return false;
	}
}
