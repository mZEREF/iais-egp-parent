/*
 * (c) Copyright 2000-2011, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 * No part of this material may be copied, reproduced, transmitted,stored in a
 * retrieval system, reverse engineered, decompiled, disassembled,localised,
 * ported, adapted, varied, modified, reused, customised or translated into
 * any language in any form or by any means, electronic, mechanical,
 * photocopying, recording or otherwise, without the prior written permission
 * of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.entity.sopprojectuserassignment;

import java.nio.charset.StandardCharsets;

public class SMCStringHelperUtil {

	public static String newString(byte[] bytes){
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static byte[] getStringBytes(String string){
		return string.getBytes(StandardCharsets.UTF_8);
	}
	
	public static String transferSpaceAscii(String value){
		if(value == null) {
            return null;
        }
		char[] ch = value.toCharArray();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < ch.length; i++) {
			int loc = (int)ch[i] ; 
			if(loc>=128 && (loc-32)%128==0){
				buffer.append((char)32);
			}else{
				buffer.append(ch[i]);
			}
		}
		return buffer.toString();
	}
	
}
