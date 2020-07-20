<%--
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="sg.gov.msf.ecda.ems.common.util.StringUtil"%>
<%@page import="sg.gov.msf.ecda.ems.common.SOPLoginUtil"%>
<%@page import="ecq.commons.config.Config"%>
<%@page import="sg.gov.msf.ecda.ems.common.util.ParamUtil"%>
<%@ page import="ncs.secureconnect.sim.entities.Constants" %>
<%@ page import="com.ncs.secureconnect.sim.common.LoginInfo" %>
<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil4Corpass" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.AuthorizationInfo" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.AuthorizationInfoParameter" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.UserInfoToken" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.UserAuthorizationToken" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.ThirdPartyAuthorizationToken" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.ThirdPartyEServiceInfo" %>
<%@ page import="ncs.secureconnect.sim.entities.corpass.ThirdPartyClientInfo" %>

<html>
<head>
    <title>eService Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>
<%!
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("corpPassCallback.jsp");
%>
<%
    if ("true".equals(Config.get("test.mode"))) {
        logger.info("<== Now Start Fake CorpPass Login ==>");
        SOPLoginUtil.testCorpPassLogin(request, response);
    } else {
        logger.info("<== Now Start True CorpPass Login ==>");
        String samlArt = (String)request.getParameter(Constants.SAML_ART);
        LoginInfo oLoginInfo = SIMUtil4Corpass.doCorpPassArtifactResolution(request, samlArt);
//        if (oLoginInfo != null) {
//            logger.info("OLogin Error Code: " + oLoginInfo.getErrorCode());
//            logger.info("OLogin Status: " + oLoginInfo.getStatus());
//       }
        String userId = "";
        String uenId = "";
        UserInfoToken userInfo = SIMUtil4Corpass.getUserInfo(request);
        if (userInfo != null ) {
            userId = userInfo.getUserIdentity();
            uenId = userInfo.getEntityId();
//            logger.info("Me1 UserId: " + userId);
//            logger.info("CPAccType: " + userInfo.getCorpPassAccountType());
//            logger.info("CPUID: " + userInfo.getUserIdentity());
//            logger.info("CPUID_Country: " + userInfo.getUserIdentityCountry());
//            logger.info("CPUID_FullName: " + userInfo.getUserFullName());
//            logger.info("CPSystemID: " + userInfo.getCorpPassSysId());
//            logger.info("ISSPHolder: " + userInfo.getISSPHolder());
//            logger.info("CPEntID: " + userInfo.getEntityId());
//            logger.info("CPEntStatus: " + userInfo.getEntityStatus());
//            logger.info("CPNonUEN_RegNo: " + userInfo.getNonUENRegistrationNumber());
//            logger.info("CPNonUEN_Country: " + userInfo.getNonUENCountry());
//            logger.info("CPNonUEN_Name: " + userInfo.getNonUENName());
//            logger.info("Me1 UENID: " + uenId);
        }
        ParamUtil.setSessionAttr(request, "corpPass.loginfo.userid", userId);
        ParamUtil.setSessionAttr(request, "corpPass.loginfo.uenId", uenId);
  
        // Authorization
        UserAuthorizationToken authAccess = SIMUtil4Corpass.getAuthAccess(request);
        if (authAccess!=null) {
            logger.info("CPESrvcID: " + authAccess.geteServiceId());
            List<AuthorizationInfo> roles =  authAccess.getAuthorizations();
            List<AuthorizationInfoParameter> parameters;
            for (int i = 0; i < roles.size(); i++) {
                AuthorizationInfo role = roles.get(i);
//                logger.info("CPEntID_SUB: " + role.getSubEntityId());
//                logger.info("CPRole: " + role.getRole());
//                logger.info("StartDate: " + role.getStartDate());
//                logger.info("EndDate: " + role.getEndDate());
                parameters = role.getParameters();
                for(int p = 0; p < parameters.size(); p++) {
                    AuthorizationInfoParameter parameter = parameters.get(p);
 //                   logger.info("Parameter: Name = " +  parameter.getName() + ", Value = " + parameter.getValue());
                }
            }
        }
        // ThirdPartyAuthorization
        ThirdPartyAuthorizationToken tpauthAccess = SIMUtil4Corpass.getTpAuthAccess(request);
        if (tpauthAccess != null) {
//            logger.info("CPEntID: " + tpauthAccess.getEntityId());
//            logger.info("CPEnt_Status: " + tpauthAccess.getEntityStatus());
//            logger.info("CPEnt_TYPE: " + tpauthAccess.getEntityType());
            List<ThirdPartyEServiceInfo> eService = tpauthAccess.geteServices();
            for (int e = 0; e < eService.size(); e++) {
                ThirdPartyEServiceInfo eServiceInfo = eService.get(e);
//                logger.info("CPESrvcID: " + eServiceInfo.geteServiceId());
                List<ThirdPartyClientInfo> client = eServiceInfo.getClients();
                for (int c = 0; c < client.size(); c++) {
                    ThirdPartyClientInfo clientInfo = client.get(c);
//                    logger.info("CP_CInt_ID :" + clientInfo.getClientEntityId());
//                    logger.info("CP_CIntEnt_TYPE: " + clientInfo.getClientEntityType());
                    List<AuthorizationInfo> roles =  clientInfo.getAuthorizations();
                    for (int i = 0; i < roles.size(); i++) {
                        AuthorizationInfo role = roles.get(i);
//                        logger.info("CPEntID_SUB: " + role.getSubEntityId());
//                        logger.info("CPRole: " + role.getRole());
//                        logger.info("StartDate: " + role.getStartDate());
//                        logger.info("EndDate: " + role.getEndDate());
                        List<AuthorizationInfoParameter> parameters = role.getParameters();
                        for (int p = 0; p < parameters.size(); p++) {
                            AuthorizationInfoParameter parameter = parameters.get(p);
//                            logger.info("Parameter: Name = " +  parameter.getName() + ", Value = " + parameter.getValue());
                        }
                    }
                }
            }
//            logger.info("Third Party XML: " + tpauthAccess.toXMLString());
        }
        response.sendRedirect(SIMUtil4Corpass.getHomePage());
    }
    out.println("<br><b><a href=\"/eService/sim/corpPassLogout\">Logout CorpPass</a></b><br>");
%>

<br></br>
</body>
</html>
--%>
