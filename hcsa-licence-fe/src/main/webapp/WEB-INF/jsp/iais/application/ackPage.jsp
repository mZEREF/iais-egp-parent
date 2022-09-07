<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="/WEB-INF/jsp/iais/common/dashboard.jsp" %>
<style>
    .margin-bottom-10{
        margin-bottom:10px;
    }
    .aMarginleft{
        margin-left: 9px;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type"  value=""/>
    <input type="hidden" name="appGroupId" value="<iais:mask name="appGroupId" value="${AppSubmissionDto.appGrpId}"/>"/>
    <input type="hidden" name="selfDeclAction" value="new"/>
    <input type="hidden" name="appType" value="${AppSubmissionDto.appType}">
    <div class="main-content">
        <div class="container">
            <div class="row center" <c:if test="${AckMessage!=null || AckMessage!=''}">style="margin-left: 8px" </c:if>>

                <c:choose>
                    <c:when test="${'error' != AckStatus}">
                        <%@include file="../common/newAppAck.jsp"%>
                    </c:when>
                    <c:otherwise>
                        <h3>${AckMessage}</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-12 col-md-1">
                    <c:if test="${requestInformationConfig == null && 'error' != AckStatus}">
                        <p class="print"><a href="javascript:void(0);" id="ackPrint"> <em class="fa fa-print"></em>Print</a></p>
                    </c:if>
                </div>
                <div class="col-xs-11 col-md-11">
                    <c:choose>
                    <c:when test="${requestInformationConfig == null && 'error' != AckStatus}">
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right" id="toDashBoard" href="javascript:void(0);">Go to <br>Dashboard</a>
                        <c:if test="${AppSubmissionDto.appType!='APTY005'}">
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" href="javascript:void(0);" id="toChooseSvc" >Apply for <br>Another Licence</a>
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doSelfAssessment" href="javascript:void(0);">Submit <br>Self-Assessment</a>
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doPrefInsDate" href="javascript:void(0);">Indicate preferred<br>Inspection Date</a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right" id="toDashBoard" href="javascript:void(0);">Go to <br>Dashboard</a>
                    </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $(document).ready(function () {
        $('#newSvc').before('<br/><br/>');
        $('#dashboard').css('padding-bottom','0px');

    });

    $('#doSelfAssessment').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('MohAppPremSelfDecl');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#doPrefInsDate').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('MohSubmitInspectionDate');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#toDashBoard').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('DashBoard');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#toChooseSvc').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('ChooseSvc');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $("#ackPrint").click(function () {
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFeAckPrintView/1/",request)%>';
        var appType = $('input[name="appType"]').val();
        var suffix = "appType=" + appType;
        if(url.indexOf('MohFeAckPrintView/1/?') != -1){
            url = url + '&' + suffix;
        }else{
            url = url + '?' + suffix;
        }
        window.open(url,'_blank');
    })
</script>



