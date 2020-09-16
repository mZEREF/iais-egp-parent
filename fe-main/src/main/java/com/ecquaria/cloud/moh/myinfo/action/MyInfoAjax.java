package com.ecquaria.cloud.moh.myinfo.action;

import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;


import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.myinfo.client.model.MyinfoUtil;
import com.ecquaria.sz.commons.util.StringUtil;
import com.lowagie.text.pdf.codec.Base64;
import lombok.extern.slf4j.Slf4j;


import ecq.commons.config.Config;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class MyInfoAjax {


    public MyInfoDto getMyInfo(HttpServletRequest request){

		MyInfoDto dto = new MyInfoDto();

		LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request,AppConsts.SESSION_ATTR_LOGIN_USER);
		
		if (loginContext != null) {
			try{
				String responseStr = getMyInfoResponse(loginContext.getNricNum());
				if (responseStr != null){
					dto = updateDtoFromResponse(dto, responseStr);
				}
			} catch (Exception e) {
				ParamUtil.setRequestAttr(request, "myinfoErrorFlag", e.getMessage());
			}
		}
		return dto;
	}

	private static MyInfoDto updateDtoFromResponse(MyInfoDto dto, String response) {
		if (StringUtil.isEmpty(response))
			return dto;
		
		JSONObject jsonObject = JSONObject.fromObject(response);
		
		if (!jsonObject.getJSONObject("regadd").isNullObject()) {
		    String floor = jsonObject.getJSONObject("regadd").getString("floor");
		    if (!StringUtil.isEmpty(floor) && !"null".equalsIgnoreCase(floor))
				dto.setFloor(floor);

		    String postal = jsonObject.getJSONObject("regadd").getString("postal");
            if (!StringUtil.isEmpty(postal) && !"null".equalsIgnoreCase(postal))
				dto.setPostalCode(postal);

            String unit = jsonObject.getJSONObject("regadd").getString("unit");
            if (!StringUtil.isEmpty(unit) && !"null".equalsIgnoreCase(unit))
				dto.setUnitNo(unit);

            String block = jsonObject.getJSONObject("regadd").getString("block");
            if (!StringUtil.isEmpty(block) && !"null".equalsIgnoreCase(block))
               dto.setBlockNo(block);

            String building = jsonObject.getJSONObject("regadd").getString("building");
            if (!StringUtil.isEmpty(building) && !"null".equalsIgnoreCase(building))
				dto.setBuildingName(building);

            String street = jsonObject.getJSONObject("regadd").getString("street");
            if (!StringUtil.isEmpty(street) && !"null".equalsIgnoreCase(street))
				dto.setStreetName(street);

		}
		
		if (!jsonObject.getJSONObject("mobileno").isNullObject()) {
			String mobileno = jsonObject.getJSONObject("mobileno").getString("nbr");
			if (!StringUtil.isEmpty(mobileno) && !"null".equalsIgnoreCase(mobileno))
				dto.setMobileNo(mobileno);
		}
		
		if (!jsonObject.getJSONObject("email").isNullObject()) {
			String email = jsonObject.getJSONObject("email").getString("value");
			if (!StringUtil.isEmpty(email) && !"null".equalsIgnoreCase(email))
				dto.setEmail(email);
		}
		if (!jsonObject.getJSONObject("homeno").isNullObject()) {
			String homeno = jsonObject.getJSONObject("homeno").getString("nbr");
			if (!StringUtil.isEmpty(homeno) && !"null".equalsIgnoreCase(homeno))
				dto.setRelationShipsIdNum(homeno);
		}
		return dto;
	}

	private static String getMyInfoResponse(String idNum) throws Exception {
    	String keyStore                     = Config.get("myinfo.jws.clientkey");
		String	appId 						= Config.get("myinfo.application.id");
		String 	clientId 					= Config.get("myinfo.client.id");
		String singPassEServiceId 			= Config.get("myinfo.singpass.eservice.id");
		String	realm 						= Config.get("myinfo.realm");
		String txnNo					    = "Moh" + Formatter.formatDateTime(new Date(), Formatter.DATE_REF_NUMBER);
		List<String> list = getAttrList();
		String baseStr = null;
        String authorization = null;
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(MyinfoUtil.getPrivateKey(keyStore)); // Get private key from keystore
			// get nonce
			Random rand = SecureRandom.getInstance("SHA1PRNG");
			long nonce = rand.nextLong();
			// get timestamp
			long timestamp = System.currentTimeMillis();
            baseStr = MyinfoUtil.getBaseString(idNum, list, clientId, singPassEServiceId, txnNo);
            log.info("baseString =====> " + baseStr);
            sig.update(baseStr.getBytes("UTF-8")); 
            byte[] signedData= sig.sign(); 
            String finalStr= Base64.encodeBytes(signedData);
            log.info("Base64 signedData =====> " + finalStr);
            authorization = MyinfoUtil.getAuthorization(realm, finalStr.replace("\n",""), appId, nonce, timestamp);
            log.info("Authorization ========>" + authorization);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("A response from MyInfo has not been received. Please try again later.");
        }

        //call get data
		String response = MyinfoUtil.getPersonBasic(authorization, idNum, list,clientId, singPassEServiceId, txnNo);
		return response;
	}

	private static List<String> getAttrList() {
		String[] ss = {"name","email", "homeno", "mobileno","regadd"};
		List<String> list = Arrays.asList(ss);
		return list;
	}

}
