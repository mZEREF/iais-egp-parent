/*
 * This file is generated by ECQ project skeleton automatically at Oct 10, 2014.
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

/**
 * SFTPUtil.java
 *
 * @author zhuhua
 */

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import static java.nio.file.Files.newOutputStream;

@Slf4j
public class SFTPUtil {
    

	private static final String seperator = ConfigHelper.getString("giro.sftp.linux.seperator");
    
	private static ChannelSftp sftp = null;

	public static void connect() {
        try {
            if(sftp != null){
                log.info(StringUtil.changeForLog("sftp is not null..."));
            } else {
            	JSch jsch = new JSch();
            	Session sshSession = jsch.getSession(ConfigHelper.getString("giro.sftp.username"),ConfigHelper.getString("giro.sftp.host"), Integer.parseInt(ConfigHelper.getString("giro.sftp.port")));
            	sshSession.setPassword(ConfigHelper.getString("giro.sftp.wordmima"));
            	Properties sshConfig = new Properties();
            	sshConfig.put("StrictHostKeyChecking", "no");
            	sshSession.setConfig(sshConfig);
            	sshSession.connect();
            	Channel channel = sshSession.openChannel("sftp");
            	channel.connect();
            	sftp = (ChannelSftp) channel;
            }
        } catch (Exception e) {
        	log.debug(StringUtil.changeForLog("Failed to connect to remote SFTP Server."));
            log.error(e.getMessage(), e);
        }
    }
    
	public static ChannelSftp connect(String host, String username, String password, int port) {
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            log.info(StringUtil.changeForLog("Connected to " + host + "."));
            
            return (ChannelSftp) channel;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        return null;
    }
	
    public static void upload(ChannelSftp sftp, File file, String remoteFilePath) {
        try {
            if(file.isFile()){
                sftp.put(file.getAbsolutePath(), remoteFilePath);
            }
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        
    }
	
    public void disconnect(ChannelSftp sftp) {
        if(sftp != null){
            if(sftp.isConnected()){
            	sftp.disconnect();
            }else if(sftp.isClosed()){
            	log.info(StringUtil.changeForLog("sftp is closed already"));
            }
            sftp = null;
        }
    }
    
	public static void disconnect() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			} else if (sftp.isClosed()) {
				log.info(StringUtil.changeForLog("sftp is closed already"));
			}
			sftp = null;
		}

	}

	@SuppressWarnings("unchecked")
	public static boolean checkRemoteFileExists(String fileName,String remotePath) throws Exception {
		connect();
		Vector<LsEntry> v = sftp.ls(remotePath);
		for (int i = 0; i < v.size(); i++) {
			LsEntry entry = v.get(i);
			String remoteFileName = entry.getFilename();
			if (remoteFileName.indexOf(fileName) != -1) {
				return true;
			}
		}

		disconnect();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getRemoteFileNames(String fileName, String remotePath) throws Exception {
		connect();
		Vector<LsEntry> v = sftp.ls(remotePath);
		List<String> remoteFileNames = new ArrayList<>();
		for (int i = 0; i < v.size(); i++) {
			LsEntry entry = v.get(i);
			String sftpName = entry.getFilename();
			if (sftpName.indexOf(fileName) != -1) {
				remoteFileNames.add(sftpName);
			}

			String sftpfullName=sftpName;
			String[] sftpfullNmaes=sftpName.split("\\.");
			if(sftpfullNmaes!=null&&sftpfullNmaes.length>0){
				sftpfullName=sftpfullNmaes[0];
			}			
			if(fileName.equals(sftpfullName)){
				remoteFileNames.clear();
				remoteFileNames.add(sftpName);
				break;
			}
			//
		}
		disconnect();
		return remoteFileNames;
	}
	
    public static boolean download(String localPath, String fileName, String remotePath) throws Exception {
	    if(StringUtil.isEmpty(remotePath)){
	        log.info(StringUtil.changeForLog("down load remotepath is null"));
	        return false;
        }
    	FileUtil.generateFolder(localPath);
    	List<String> remoteFileNames = getRemoteFileNames(fileName,remotePath);
    	if (remoteFileNames != null && !remoteFileNames.isEmpty()) {
    		connect();
    		for(String remoteFileName : remoteFileNames){
	    		String src = remotePath + seperator + remoteFileName;
	    		String dest = localPath + remoteFileName;
	    		sftp.get(src, dest);
	    		log.info(StringUtil.changeForLog("download " + remoteFileName + " success!"));
    		}
    		disconnect();
    		return true;
    	}
    	return false;
    }

    public static void download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
	    File outFile = MiscUtil.generateFile(saveFile);
        try(OutputStream os = newOutputStream(outFile.toPath())) {
            sftp.cd(directory);
            sftp.get(downloadFile, os);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public static boolean upload(String fileName, String remotePath) {
    	boolean result = false;
    	if(StringUtil.isEmpty(remotePath)){
    	    return result;
        }
        try {
        	connect();
            File f = MiscUtil.generateFile(fileName);
            if(f.isFile()){
            	log.info(StringUtil.changeForLog("localFile : " + f.getAbsolutePath()));
                String remoteFile = remotePath + seperator + f.getName();
                log.info(StringUtil.changeForLog("remotePath:" + remoteFile));

                sftp.put(f.getAbsolutePath(), remoteFile);
                if(!f.delete()){
                    log.debug(StringUtil.changeForLog(fileName + " is inexistence in service."));
                }
                result = true;
                log.info(StringUtil.changeForLog("file: [" + fileName + "] uploaded to remote server. "));
            }else{
            	log.debug(StringUtil.changeForLog("[" + fileName + "] not a file"));
            }
        } catch (Exception e) {
        	log.error(StringUtil.changeForLog("upload file [" + fileName + "] to remote Server failed."));
        	result = false;
    		log.error(e.getMessage(), e);
        }finally{
        	disconnect();
        }

		
		return result;
    }

    @SuppressWarnings("unused")
	private void createDir(String filepath, ChannelSftp sftp){
        boolean bcreated = false;
        boolean bparent = false;
        File file = MiscUtil.generateFile(filepath);
        String ppath = file.getParent();
        try {
            sftp.cd(ppath);
            bparent = true;
        } catch (SftpException e1) {
            bparent = false;
            log.error(e1.getMessage(), e1);
        }
        try {
            if(bparent){
                try {
                    sftp.cd(filepath);
                    bcreated = true;
                } catch (Exception e) {
                    bcreated = false;
                    log.error(e.getMessage(), e);
                }
                if(!bcreated){
                    sftp.mkdir(filepath);
                }
                return;
            }else{
                createDir(ppath,sftp);
                sftp.cd(ppath);
                sftp.mkdir(filepath);
            }
        } catch (SftpException e) {
            log.info(StringUtil.changeForLog("mkdir failed :" + filepath));
            log.error(e.getMessage(), e);
        }
        
        try {
            sftp.cd(filepath);
        } catch (SftpException e) {
        	log.error(e.getMessage(), e);
            log.info(StringUtil.changeForLog("can not cd into :" + filepath));
        }
        
    }

}

