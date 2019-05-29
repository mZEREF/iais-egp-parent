<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
    header-ext
</webui:setAttribute>

<webui:setLayout name="saas" />

<webui:setAttribute name="title">
    <egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel>
</webui:setAttribute>
<c:set var="subHeader" value="subHeader" scope="request"></c:set>

<egov-core:eServiceTitle title = "Suocheng Test"></egov-core:eServiceTitle>

<section>
    <div id="formdiv"></div>
</section>

<div class="text-center btn-wrapper"></div>

<form method="post" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input id="proceed" type="submit" class="small blue" value="Proceed" style="display:none;"/>
</form>

<script type="text/javascript">
    EGP.Common.setPreSubmitListener(function(e) {
        // TODO Add your logic to control the eGovForm dialog to popup or not
        return true;
    });
    $(function(){
        $('#proceed').click();
    })
</script>