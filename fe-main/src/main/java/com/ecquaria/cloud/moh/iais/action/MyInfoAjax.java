package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.lowagie.text.pdf.codec.Base64;
import ecq.commons.config.Config;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Controller
@Slf4j
public class MyInfoAjax {
	@Autowired
	private EicGatewayFeMainClient eicGatewayFeMainClient;
	private static  String[] ss = {"name","email", "mobileno","regadd"};
    public MyInfoDto getMyInfo(String NircNum){

		String flag = Config.get("moh.halp.myinfo.enable");
		if("Y".equalsIgnoreCase(flag)){

			if(StringUtil.isEmpty(NircNum)){
				log.info("----nircnum is null----");
				return null;
			}
			try{
				String responseStr = getMyInfoResponse(NircNum);
				if (!StringUtil.isEmpty(responseStr)){
					MyInfoDto dto = new MyInfoDto();
					dto = updateDtoFromResponse(dto, responseStr);
					log.info(JsonUtil.parseToJson(dto));
					return dto;
				}else {
					log.info("----get myinfo is null----");
				}
				return null;
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				MyInfoDto dto = new MyInfoDto();
				dto.setServiceDown(true);
				return dto;
			}

		}else {
			log.info("---myinfo flag is closed----");
		}
		return null;
	}

	private  MyInfoDto updateDtoFromResponse(MyInfoDto dto, String response) {
		if (StringUtil.isEmpty(response))
			return dto;
		
		JSONObject jsonObject = JSONObject.fromObject(response);
		JSONObject jsonObjectRegadd = jsonObject.getJSONObject("regadd");
		if (!jsonObjectRegadd.isNullObject()) {
		
		    String floor = jsonObjectRegadd.getString("floor");
		    if (!StringUtil.isEmpty(floor) && !"null".equalsIgnoreCase(floor))
				dto.setFloor(floor);

		    String postal = jsonObjectRegadd.getString("postal");
            if (!StringUtil.isEmpty(postal) && !"null".equalsIgnoreCase(postal))
				dto.setPostalCode(postal);

            String unit = jsonObjectRegadd.getString("unit");
            if (!StringUtil.isEmpty(unit) && !"null".equalsIgnoreCase(unit))
				dto.setUnitNo(unit);

            String block = jsonObjectRegadd.getString("block");
            if (!StringUtil.isEmpty(block) && !"null".equalsIgnoreCase(block))
               dto.setBlockNo(block);

            String building = jsonObjectRegadd.getString("building");
            if (!StringUtil.isEmpty(building) && !"null".equalsIgnoreCase(building))
				dto.setBuildingName(building);

            String street = jsonObjectRegadd.getString("street");
            if (!StringUtil.isEmpty(street) && !"null".equalsIgnoreCase(street))
				dto.setStreetName(street);

		}
		JSONObject jsonObjectMobileno = jsonObject.getJSONObject("mobileno");
		if (!jsonObjectMobileno.isNullObject()) {
			String mobileno = jsonObjectMobileno.getString("nbr");
			if (!StringUtil.isEmpty(mobileno) && !"null".equalsIgnoreCase(mobileno))
				dto.setMobileNo(mobileno);
		}
		JSONObject jsonObjectEmail = jsonObject.getJSONObject("email");
		if (!jsonObjectEmail.isNullObject()) {
			String email = jsonObjectEmail.getString("value");
			if (!StringUtil.isEmpty(email) && !"null".equalsIgnoreCase(email))
				dto.setEmail(email);
		}
		JSONObject jsonObjectName = jsonObject.getJSONObject("name");
		if (!jsonObjectName.isNullObject()) {
			String name = jsonObjectName.getString("value");
			if (!StringUtil.isEmpty(name) && !"null".equalsIgnoreCase(name))
				dto.setUserName(name);
		}
		return dto;
	}

	private  String getMyInfoResponse(String idNum) throws Exception {
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
            log.info(StringUtil.changeForLog("baseString =====> " + baseStr));
            sig.update(baseStr.getBytes(StandardCharsets.UTF_8));
            byte[] signedData= sig.sign(); 
            String finalStr= Base64.encodeBytes(signedData);
            log.info(StringUtil.changeForLog("Base64 signedData =====> " + finalStr));
            authorization = MyinfoUtil.getAuthorization(realm, finalStr.replace("\n",""), appId, nonce, timestamp);
            log.info(StringUtil.changeForLog("Authorization ========>" + authorization));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("A response from MyInfo has not been received. Please try again later.",e);
        }
         String myInfoPath                     = Config.get("myinfo.path.prdfix");
        if(StringUtil.isEmpty(myInfoPath)){
        	log.info("-----myinfo.path.prdfix is null---------");
        	return null;
        }
        //call get data
		String response = getPersonBasic(authorization, idNum, list,clientId, singPassEServiceId, txnNo,myInfoPath);
		return response;
	}
	/**
	 * Retrieves Person data from MyInfo
	 *
	 * Retrieves Person data from MyInfo based on UIN/FIN. This API does not require authorisation token, and retrieves only a user&#39;s basic profile (i.e. excluding CPF and IRAS data)  The available returned attributes from this API includes  - name: Name - hanyupinyinname: HanYuPinYin - aliasname: Alias - hanyupinyinaliasname: HanYuPinYinAlias - marriedname: MarriedName - sex: Sex - race: Race - dialect: Dialect - nationality: Nationality - dob: DOB - birthcountry: BirthCountry - vehno: VehNo - regadd: RegAdd - mailadd: MailAdd - billadd: BillAdd - housingtype: HousingType - hdbtype: HDBType - email: Email - homeno: HomeNo - mobileno: MobileNo - marital: Marital - marriagedate: MarriageDate - divorcedate: DivorceDate - householdincome: HouseholdIncome - relationships: Relationships - edulevel: EduLevel - gradyear: GradYear - schoolname: SchoolName - occupation: Occupation - employment: Employment  Note - null values indicate that the field is unavailable
	 * @throws Exception
	 */
	public  String getPersonBasic( String authorization,String idNumber,List<String> attributes,String clientId,String singPassEServiceId,String txnNo,String myInfoPath) throws Exception {
		ApplicationContext context = SpringContextHelper.getContext();
		if (context == null){
			return null;
		}
		String  encipheredData = eicGatewayFeMainClient.getMyInfoEic(authorization,idNumber,attributes.toArray(new String[attributes.size()]),clientId,singPassEServiceId,txnNo, myInfoPath).getBody();
		return MyinfoUtil.decodeEncipheredData(encipheredData);
	}
	private List<String> getAttrList() {
		List<String> list = Arrays.asList(ss);
		return list;
	}

}
