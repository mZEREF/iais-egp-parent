<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="java.util.Properties" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    Properties properties = (Properties) ConfigHelper.getProperty("iais.current.domain");
    String prop = properties.getProperty("iais.current.domain");
    if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(prop)) {
%>
<webui:setLayout name="iais-internet"/>
<%
} else if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(prop)) {
%>
<webui:setLayout name="iais-intranet"/>
<%
    }
%>
<div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2><iais:message key="GENERAL_ERR0044" escape="true"></iais:message></h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
