<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MiscUtil" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
          String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<c:forEach items="${iais_Login_User_Info_Attr.privileges}" var="privilege">
    <c:if test="${privilege.id eq 'HALP_HCSA_INBOX'}">
        <c:set var="msgTab" value="1"/>
        <c:set var="appTab" value="1"/>
        <c:set var="licTab" value="1"/>
    </c:if>
    <c:if test="${StringUtil.stringContain(privilege.id,'HALP_MOH_DS_')}">
        <c:set var="msgTab" value="1"/>
        <c:set var="dssTab" value="1"/>
        <c:if test="${privilege.id == 'HALP_MOH_DS_ART'}">
            <c:set var="dataSubARTPrivilege" value="1"/>
        </c:if>
        <c:if test="${privilege.id == 'HALP_MOH_DS_LDT'}">
            <c:set var="dataSubLDTPrivilege" value="1"/>
        </c:if>
    </c:if>
</c:forEach>
<c:set var="roleMenuForEServices" value="${appTab == 1 && dssTab == 1 ? 2 : (appTab == 1 ? 1 : 0)}" />


