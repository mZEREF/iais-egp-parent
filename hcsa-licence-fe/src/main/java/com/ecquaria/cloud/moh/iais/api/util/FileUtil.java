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

import java.io.*;
import java.nio.file.Files;


import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import ecq.commons.config.Config;
import lombok.extern.slf4j.Slf4j;

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
			writer = new FileWriter(fileName, true);
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

	public static boolean writeToFile(String fileName, String data) {
		boolean result = false;
		if (fileName == null || fileName.trim().length() == 0){
			return result;
		}
			
		try {
			generateFolder(fileName.substring(0, fileName.lastIndexOf("\\")));
		} catch (Exception e) {
			log.error(StringUtil.changeForLog("generate folder for " + fileName + " error!" + e.getMessage()));
			log.error(e.getMessage(), e);
		}
		File f = new File(fileName);
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
					result = true;
				}
			} catch (Exception e) {
				log.error(StringUtil.changeForLog("write file for " + fileName + " error!" + e.getMessage()));
				log.error(e.getMessage(), e);
				result = false;
			} finally {
				if (pw != null){
					pw.close();
				}
			}
		}
		return result;
	}

	public static void generateFolder(String folderPath) {
		File f = new File(folderPath);
		if (!f.isFile() && !f.exists()) {
			f.mkdirs();
			log.info(StringUtil.changeForLog("create folder:" + folderPath));
		}
	}

	public static String getString(InputStream is) throws Exception {
		return getString(is, "UTF-8");
	}

	public static String getString(String fileName) throws Exception {
		File f = new File(fileName);
		if (f.exists() && f.isFile()) {
			InputStream is = Files.newInputStream(f.toPath());
			String xml = getString(is);
			if( !f.delete()){
             log.error(StringUtil.changeForLog(fileName + " is inexistence in service."));
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
		BufferedReader br = new BufferedReader(new StringReader(content));
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		while ((length = br.read(buffer)) != -1) {
			bw.write(new String(buffer, 0, length));
		}
		bw.close();
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
		return Config.get("file.server.url") + filePath;
	}

	public static boolean copyFile(String source, String target) {
		return copyFile(new File(source), new File(target));
	}

	public static boolean copyFile(File source, File target) {
		if (!source.exists()) {
			log.error(StringUtil.changeForLog("source file :" + source.getName() + " not exist!"));
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
				inStream = Files.newInputStream(source.toPath());
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
	public static byte[] readBytesFromFile(String fileName) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if(fileName != null){
			File file = new File(fileName);
			InputStream fis = null;
			try {
				fis = Files.newInputStream(file.toPath());
				byte[] b = new byte[1024];
				int n = fis.read(b);
				while(n != -1){
					out.write(b, 0, n);
					n = fis.read(b);
				}
			} finally {				
								
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
				
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
}
