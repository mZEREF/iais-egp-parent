<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Licensee Details</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/companyDetail.jsp" %>
                    <c:forEach var="licensee" items="${subLicenseeDtoList}" varStatus="status">
                        <c:set var="index" value="${status.index + 1}" />
                        <%@include file="section/licenseeDetail.jsp" %>
                    </c:forEach>
                </div>
                <div class="container-footer">
                    <div class="col-sm-4 col-md-2 text-left">
                        <a style="padding-left: 5px;" class="back" id="back">
                            <em class="fa fa-angle-left">&nbsp;</em> Back
                        </a>
                    </div>
                    <div class="col-sm-12 col-md-10 text-right">
                        <button class="btn btn-primary" id="refresh" >Refresh and Save Data</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $("#back").click(function () {
        $("[name='crud_action_type']").val('back');
        $('#mainForm').submit();
    })

    $("#refresh").click(function () {
        $("[name='crud_action_type']").val('refresh');
        $('#mainForm').submit();
    })
</script>