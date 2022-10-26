<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainCommonPoolSuccessForm" action=<%=process.runtime.continueURL()%>>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <%--@elvariable id="assignResult" type="java.lang.String"--%>
                                <h2>${assignResult}</h2>
                            </div>
                        </div>
                        <div  style="text-align:left"><span><a href="/bsb-web/eservice/INTRANET/MohBsbSupervisorAssignmentPool"><em class="fa fa-angle-left"></em> Back</a></span></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>