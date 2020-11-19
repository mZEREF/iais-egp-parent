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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;





import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SFTPUtil {
    
	private static Properties property;
	
	static{
		property = new Properties();
		try {
			property.load(SFTPUtil.class.getClassLoader().getResourceAsStream("sftp.properties"));
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static final String seperator = property.getProperty("sftp.linux.seperator");
    
	private static ChannelSftp sftp = null;

	public static void connect() {
        try {
            if(sftp != null){
                log.info("sftp is not null...");
            } else {
            	JSch jsch = new JSch();
            	Session sshSession = jsch.getSession(property.getProperty("sftp.username"), property.getProperty("sftp.host"), Integer.parseInt(property.getProperty("sftp.port")));
            	sshSession.setPassword(property.getProperty("sftp.wordm"));
            	Properties sshConfig = new Properties();
            	sshConfig.put("StrictHostKeyChecking", "no");
            	sshSession.setConfig(sshConfig);
            	sshSession.connect();
            	Channel channel = sshSession.openChannel("sftp");
            	channel.connect();
            	sftp = (ChannelSftp) channel;
            }
        } catch (Exception e) {
        	log.error("Failed to connect to remote SFTP Server.");
            log.error(e.getMessage(), e);
        }
    }
    
	public static ChannelSftp connect(String host, String username, String password, int port) {
        try {
            JSch jsch = new JSch();
//            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            log.info("Connected to " + host + ".");
            
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
            	log.info("sftp is closed already");
            }
            sftp = null;
        }
    }
    
	public static void disconnect() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			} else if (sftp.isClosed()) {
				log.info("sftp is closed already");
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
			//bug 0079087
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
	
    public static void download(String localPath, String fileName, String remotePath) throws Exception {
    	//FileUtil.generateFolder(localPath);
    	List<String> remoteFileNames = getRemoteFileNames(fileName,remotePath);
    	if (remoteFileNames != null && remoteFileNames.size() > 0) {
    		connect();
    		for(String remoteFileName : remoteFileNames){
	    		String src = remotePath + seperator + remoteFileName;
	    		String dest = localPath + remoteFileName;
	    		sftp.get(src, dest);
	    		log.info("download " + remoteFileName + " success!" );
    		}
    		disconnect();
    	}
    }

    public static void download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public static boolean upload(String fileName, String remotePath) {
    	boolean result = false;
        try {
        	connect();
            File file = new File(fileName);
            if(file.isFile()){
            	log.info("localFile : " + file.getAbsolutePath());
                String remoteFile = remotePath + seperator + file.getName();
                log.info("remotePath:" + remoteFile);
                
				/*File rfile = new File(remoteFile);
                String rpath = rfile.getParent();
                try {
                    createDir(rpath, sftp);
                } catch (Exception e) {
                    System.out.println("*******create path failed" + rpath);
                }*/

                sftp.put(file.getAbsolutePath(), remoteFile);
                result = true;
                log.info("file: [" + fileName + "] uploaded to remote server. ");
            }else{
            	log.error("[" + fileName + "] not a file");
            }
        } catch (Exception e) {
        	log.error("upload file [" + fileName + "] to remote Server failed.");
        	result = false;
//    		disconnect();
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
        File file = new File(filepath);
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
                    bcreated = true;
                }
                return;
            }else{
                createDir(ppath,sftp);
                sftp.cd(ppath);
                sftp.mkdir(filepath);
            }
        } catch (SftpException e) {
            log.info("mkdir failed :" + filepath);
            log.error(e.getMessage(), e);
        }
        
        try {
            sftp.cd(filepath);
        } catch (SftpException e) {
        	log.error(e.getMessage(), e);
            log.info("can not cd into :" + filepath);
        }
        
    }

}

