package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.sz.commons.util.StringUtil;
import com.lowagie.text.pdf.codec.Base64;
import ecq.commons.config.Config;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Controller
@Slf4j
public class MyInfoAjax {


    public MyInfoDto getMyInfo(String NircNum){

		String flag = Config.get("moh.halp.myinfo.enable");
		if("Y".equalsIgnoreCase(flag)){

			if(StringUtil.isEmpty(NircNum)){
				log.info("----nircnum is null----");
				return null;
			}
			try{
				MyInfoDto dto = new MyInfoDto();
				String responseStr = getMyInfoResponse(NircNum);
				if (responseStr != null){
					dto = updateDtoFromResponse(dto, responseStr);
				}else {
					log.info("----get myinfo is null----");
				}
				log.info(JsonUtil.parseToJson(dto));
				return dto;
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}

		}else {
			log.info("---myinfo flag is closed----");
		}
		return null;
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
		if (!jsonObject.getJSONObject("name").isNullObject()) {
			String name = jsonObject.getJSONObject("name").getString("value");
			if (!StringUtil.isEmpty(name) && !"null".equalsIgnoreCase(name))
				dto.setUserName(name);
		}
		return dto;
	}

	private static String getMyInfoResponse(String idNum) throws Exception {
    	String keyStore                     = Config.get("myinfo.jws.priclientkey");
		String	appId 						= Config.get("myinfo.application.id");
		String 	clientId 					= Config.get("myinfo.client.id");
		String singPassEServiceId 			= Config.get("myinfo.singpass.eservice.id");
		String	realm 						= Config.get("myinfo.realm");
		//String txnNo					    = "Moh" + Formatter.formatDateTime(new Date(), Formatter.DATE_REF_NUMBER);
		String txnNo                        =Config.get("myinfo.txnNo");
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
		String[] ss = {"name","email", "mobileno","regadd"};
		List<String> list = Arrays.asList(ss);
		return list;
	}

}
