package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoTakenDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import sop.webflow.rt.api.BaseProcessClass;


@Controller
@Slf4j
public class MyInfoAjax {
	@Autowired
	private EicGatewayFeMainClient eicGatewayFeMainClient;
	private static  String[] ss = {"name","email", "mobileno","regadd"};
	public void setVerifyTakenAndAuthoriseApiUrl(HttpServletRequest request,String redirectUriPostfix,String nric){
		String myinfoOpen = ConfigHelper.getString("myinfo.true.open");
		if (!AppConsts.YES.equalsIgnoreCase( myinfoOpen)){
			log.info("-----------myinfo.true.open is No-------");
			ParamUtil.setSessionAttr(request,"myinfoTrueOpen",null);
			return ;
		}
		ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,null);
		ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,null);
		ParamUtil.setSessionAttr(request,"myinfoTrueOpen","Y");
		nric = getNric(nric, request);
		if(StringUtil.isNotEmpty(nric)){
			String redirectUri = "https://"+request.getServerName()+"/eservice/INTERNET/"+redirectUriPostfix;
			ParamUtil.setSessionAttr(request,MyinfoUtil.CALL_MYINFO_PROCESS_SESSION_NAME_NRIC,nric);
			ParamUtil.setSessionAttr(request,MyinfoUtil.CALL_MYINFO_PROCESS_SESSION_NAME+"_"+nric,redirectUriPostfix);
			ParamUtil.setSessionAttr(request,"callAuthoriseApiUri",getAuthoriseApiUrl(redirectUri,nric));
			String takenStartTime = (String) ParamUtil.getSessionAttr(request,MyinfoUtil.KEY_MYINFO_TAKEN_START_TIME+nric);
			if(takenStartTime == null){
				ParamUtil.setSessionAttr(request,"verifyTakenConfiguration","-1");
			}else {
				long takenStartTimeL = Long.parseLong(takenStartTime);
				long time = System.currentTimeMillis();
				if(time - takenStartTimeL < MyinfoUtil.TAKEN_DURATION_TIME){
					ParamUtil.setSessionAttr(request,"verifyTakenConfiguration",takenStartTime);
				}else {
					ParamUtil.setSessionAttr(request,"verifyTakenConfiguration","-1");
					setTakenSession(MyinfoUtil.clearSessionForMyInfoTaken(nric),request);
				}
			}
		}
	}
	public void setVerifyTakenAndAuthoriseApiUrl(HttpServletRequest request,String redirectUriPostfix){
		setVerifyTakenAndAuthoriseApiUrl(request,redirectUriPostfix,"");
	}
	public MyInfoDto noTakenCallMyInfo(BaseProcessClass bpc,String redirectUriPostfix,String nric) throws NoSuchAlgorithmException {
		String myinfoOpen = ConfigHelper.getString("myinfo.true.open");
		if (!AppConsts.YES.equalsIgnoreCase( myinfoOpen)) {
			log.info("-----------myinfo.true.open is No-------");
			return null;
		}
		HttpServletRequest request = bpc.request;
		String code = ParamUtil.getString(request,"code");
		String state = ParamUtil.getString(request,"state");
		String error = ParamUtil.getString(request,"error");
		String errorDescription = ParamUtil.getString(request,"error_description");
		log.info(StringUtil.changeForLog("--------state :" + StringUtil.getNonNull(state) + "----------"));
		if ("500".equalsIgnoreCase(error)) {
			log.info("Unknown or other server side errors");
		} else if ("503".equalsIgnoreCase(error)) {
			log.info("MyInfo under maintenance. Error description will also be given in error_description parameter");
		} else if ("access_denied".equalsIgnoreCase(error)) {
			log.info("When user did not give consent, refer to error_description parameter for the reason");
			log.info(errorDescription);
		} else {
			log.info(StringUtil.changeForLog("--------- Assembly data acquisition get taken start ----"));
			log.info(StringUtil.changeForLog("--------- Assembly data acquisition code : " + StringUtil.getNonNull(code)));
			nric = getNric(nric, request);
			if (StringUtil.isNotEmpty(nric)) {
				String redirectUri = ConfigHelper.getString("myinfo.common.call.back.url", redirectUriPostfix);
				MyInfoTakenDto accessTokenDto = getTakenCallMyInfo(code, nric, redirectUri);
				if (accessTokenDto != null) {
					log.info(StringUtil.changeForLog("Myinfo Token type => " + accessTokenDto.getToken_type()
							+ " => Token value =>" + accessTokenDto.getAccess_token()));
					setTakenSession(MyinfoUtil.getSessionForMyInfoTaken(nric, accessTokenDto.getToken_type(), accessTokenDto.getAccess_token()), request);
					MyInfoDto myInfoDto = getMyInfoByTrue(nric, accessTokenDto.getToken_type(), accessTokenDto.getAccess_token());
					if(state.contains(nric) && !state.equalsIgnoreCase(nric)){
						myInfoDto.setAddrType(state.replace(nric,""));
					}
					return myInfoDto;
				} else {
					log.info("------------The myinfo fetch taken connection failed-----------");
				}
			} else {
				log.info("-----------prepareGetTakenFromMyInfo no find loginContext-----------------");
			}
		}

		return null;
	}

	private String getNric(String nric,HttpServletRequest request){
		if(StringUtil.isNotEmpty(nric)){
			return nric;
		}
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
		if(loginContext != null){
			return loginContext.getNricNum();
		}
		log.info(StringUtil.changeForLog("-------------- getNric is null ------------"));
		return "";
	}

	private MyInfoTakenDto getTakenCallMyInfo(String code,String state,String redirectUri) throws NoSuchAlgorithmException {
		String grantType = ConfigHelper.getString("myinfo.taken.grant.type","authorization_code");
		String priclientkey = ConfigHelper.getString("myinfo.common.priclientkey");
		String clientId = ConfigHelper.getString("myinfo.common.client.id");
		String clientSecret =  ConfigHelper.getString("myinfo.common.client.secret");
		String requestUrl = ConfigHelper.getString("myinfo.taken.authUrl");
		MyInfoTakenDto myInfoTakenDto = MyinfoUtil.getTakenCallMyInfo(AcraConsts.POST_METHOD,grantType,code, priclientkey,clientSecret,requestUrl,clientId,state,redirectUri);
		return myInfoTakenDto;
	}
	public MyInfoDto getMyInfo(String NircNum, HttpServletRequest request){
		String myinfoOpen = ConfigHelper.getString("myinfo.true.open");
		String taken = (String) ParamUtil.getSessionAttr(request,MyinfoUtil.KEY_MYINFO_TAKEN+NircNum);
		String takenType = (String) ParamUtil.getSessionAttr(request,MyinfoUtil.KEY_MYINFO_TAKEN+NircNum+MyinfoUtil.KEY_TAKEN_TYPE);
		MyInfoDto myInfoDto = null;
		if(AppConsts.YES.equalsIgnoreCase( myinfoOpen)){
			myInfoDto = getMyInfoByTrue(NircNum,takenType,taken);
		}

		return myInfoDto;
	}


	public void  setTakenSession(Map<String,String> map,HttpServletRequest request){
		for (Map.Entry<String, String> m : map.entrySet()) {
			ParamUtil.setSessionAttr(request,m.getKey(),m.getValue());
		}
	}
	private  MyInfoDto updateDtoFromResponse(MyInfoDto dto, String response) {
		if (StringUtil.isEmpty(response))
			return dto;

		JSONObject jsonObject = JSONObject.fromObject(response);
		JSONObject jsonObjectRegadd = jsonObject.getJSONObject("regadd");
		if (!jsonObjectRegadd.isNullObject()) {
			String floor = getStringKeyByObjName("floor",jsonObjectRegadd);
			if (!StringUtil.isEmpty(floor) && !"null".equalsIgnoreCase(floor))
				dto.setFloor(floor);

			String postal = getStringKeyByObjName("postal",jsonObjectRegadd);
			if (!StringUtil.isEmpty(postal) && !"null".equalsIgnoreCase(postal))
				dto.setPostalCode(postal);

			String unit = getStringKeyByObjName("unit",jsonObjectRegadd);
			if (!StringUtil.isEmpty(unit) && !"null".equalsIgnoreCase(unit))
				dto.setUnitNo(unit);

			String block = getStringKeyByObjName("block",jsonObjectRegadd);
			if (!StringUtil.isEmpty(block) && !"null".equalsIgnoreCase(block))
				dto.setBlockNo(block);

			String building = getStringKeyByObjName("building",jsonObjectRegadd);
			if (!StringUtil.isEmpty(building) && !"null".equalsIgnoreCase(building))
				dto.setBuildingName(building);

			String street = getStringKeyByObjName("street",jsonObjectRegadd);
			if (!StringUtil.isEmpty(street) && !"null".equalsIgnoreCase(street))
				dto.setStreetName(street);

		}
		JSONObject jsonObjectMobileno = jsonObject.getJSONObject("mobileno");
		if (!jsonObjectMobileno.isNullObject()) {
			String mobileno = getStringKeyByObjName("nbr",jsonObjectMobileno);
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

	private String getStringKeyByObjName(String objName,JSONObject jsonObject){
		JSONObject floorJson = jsonObject.getJSONObject(objName);
		if(!floorJson.isNullObject()){
			return floorJson.getString("value");
		}
		return "";
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

	public String getAuthoriseApiUrl(String redirectUri,String nric){
		String authApiUrl                   = ConfigHelper.getString("myinfo.authorise.url");
		String	clientId 					= ConfigHelper.getString("myinfo.common.client.id");
		String 	purpose 					= ConfigHelper.getString("myinfo.authorise.purpose");
		String spEsvcId                     = ConfigHelper.getString("myinfo.common.sp.esvcId");
		redirectUri                         = ConfigHelper.getString("myinfo.common.call.back.url",redirectUri);
		return MyinfoUtil.getAuthoriseApiUrl(authApiUrl,nric,clientId,MyinfoUtil.getAttrsStringByListAttrs(getAttrList()),spEsvcId,purpose,nric,redirectUri);
	}

	public MyInfoDto getMyInfoByTrue(String nric,String takenType,String taken){
		log.info("-------getMyInfoByTrue start ---------");
		String keyStore = ConfigHelper.getString("myinfo.common.priclientkey");
		String	clientId = ConfigHelper.getString("myinfo.common.client.id");
		String spEsvcId = ConfigHelper.getString("myinfo.common.sp.esvcId");
		String  uri = ConfigHelper.getString("myinfo.person.authUrl")+nric+'/';
		String attrs =MyinfoUtil.getAttrsStringByListAttrs(getAttrList());
		String authorizationHeader = MyinfoUtil.generateAuthorizationHeaderForMyInfo(AcraConsts.GET_METHOD,clientId,attrs,keyStore,spEsvcId,uri,takenType,taken);
		log.info(StringUtil.changeForLog("Myinfo person header => " + authorizationHeader));
		Map <String,Object> param = IaisCommonUtils.genNewHashMap();
		param.put(AcraConsts.CLIENT_ID, clientId);
		param.put(AcraConsts.ATTRIBUTE, attrs);
		param.put(AcraConsts.SP_ESVCID, spEsvcId);
		ResponseEntity<String> resEntity;
		try {
			String eicUrl = ConfigHelper.getString("myinfo.person.url") + nric + '/';
			resEntity = IaisCommonUtils.callEicGatewayWithParam(eicUrl, HttpMethod.GET, param, MediaType.APPLICATION_JSON, null,
					authorizationHeader, null, null, String.class);
			AuditTrailDto auditTrailDto = new AuditTrailDto();
			auditTrailDto.setOperation(AuditTrailConsts.OPERATION_FOREIGN_INTERFACE);
			auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
			auditTrailDto.setModule("MyInfo");
			auditTrailDto.setFunctionName("getMyInfoByTrue");
			auditTrailDto.setBeforeAction(JsonUtil.parseToJson(param));
			auditTrailDto.setAfterAction(resEntity.getBody());
			AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
			// HttpStatus httpStatus = resEntity.getStatusCode();
			log.info(StringUtil.changeForLog("Myinfo person response string encrypt => " + resEntity.getBody()));
			String responseStr = MyinfoUtil.decodeEncipheredData(resEntity.getBody());
			log.info(StringUtil.changeForLog("Myinfo person response string decrypt => " + responseStr));
			MyInfoDto dto = new MyInfoDto();
			dto = updateDtoFromResponse(dto, responseStr);
			log.info(JsonUtil.parseToJson(dto));
			return dto;
		}catch (HttpClientErrorException e){
			// resEntity = ResponseEntity.badRequest().body(e.getResponseBodyAsString());
			// httpStatus = e.getStatusCode();
			log.error(e.getMessage(), e);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public MyInfoDto getMyInfoData(HttpServletRequest request){
		String myinfoOpen = ConfigHelper.getString("myinfo.true.open");
		if(AppConsts.YES.equalsIgnoreCase( myinfoOpen)){
			String nric =(String) ParamUtil.getSessionAttr(request,MyinfoUtil.CALL_MYINFO_PROCESS_SESSION_NAME_NRIC);
			MyInfoDto myInfoDto = (MyInfoDto) ParamUtil.getSessionAttr(request,MyinfoUtil.CALL_MYINFO_DTO_SEESION+"_"+ nric);
			if( myInfoDto != null ){
				if(myInfoDto.isServiceDown()){
					ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
				}
			}
			ParamUtil.setSessionAttr(request,MyinfoUtil.CALL_MYINFO_DTO_SEESION+"_"+ nric,null);
			ParamUtil.setSessionAttr(request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK,null);
			return myInfoDto;
		}else {
			return null;
		}
	}
}
