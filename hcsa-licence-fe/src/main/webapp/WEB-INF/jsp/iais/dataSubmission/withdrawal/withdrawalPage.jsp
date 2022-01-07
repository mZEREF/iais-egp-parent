<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../../common/dashboard.jsp" %>
<style>

    .application-tab-footer {
        margin-top: 0;
        padding-top: 0;
        border-top: 1px solid #BABABA;
        margin-left: 0;
        margin-right: 0;
    }
    .withdraw-content-box {
        background-color: #fafafa;
        border-radius: 14px;
        padding: 20px;
        border:1px solid #d1d1d1;
        margin-bottom: 0;
    }

    .withdraw-info-gp .withdraw-info-row .withdraw-info p:before {
        color: #a2d9e7;
    }

    a {
        text-decoration: none;
    }
</style>
<div class="container">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="app_action_type" value="">
        <input type="hidden" name="print_action_type" value="">
        <input type="hidden" name="withdraw_app_list" value="">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="navigation-gp">
            <p class="print"><div style="font-size: 16px;text-align: right"><a href="javascript:void(0);" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>

            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="center-content">
                            <h2>You are withdrawing for</h2>
                            <div class="row">
                                <div class="col-lg-8 col-xs-12">
                                    <c:forEach items="${addWithdrawnDtoList}" var="wdList">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a href="javascript:void(0);" class="appNo" onclick="toDsView('${wdList.dataSubmissionDto.submissionNo}','${wdList.cycleDto.dsType}')">${wdList.dataSubmissionDto.submissionNo}</a></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                            </div>
                        </div>
                        <div style="padding: 0px 90px;">
                            <div class="row">
                                <label class="col-md-4" style="font-size:2rem">Remarks <span
                                        style="color: #ff0000"> *</span> </label>
                            </div>
                        </div>
                        <div class="center-content">
                            <div class="row">
                                <div class="col-md-6">
                                        <textarea name="withdrawnRemarks" cols="90" rows="3" id="withdrawnRemarks"
                                                  title="content" maxlength="100" >${withdrawnRemarks}</textarea>
                                    <span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row" style="padding-top: 10px;">
                                <div class="col-xs-12 col-sm-6">
                                    <span style="padding-right: 10%" class="components">
                                        <a href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox"><em
                                                class="fa fa-angle-left"></em> Back</a>
                                    </span>
                                </div>
                                <a class="btn btn-primary" style="float:right" onclick="doSubmit()"
                                   href="javascript:void(0);">Submit</a>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
    </form>
</div>
<script type="text/javascript">




    function doSubmit() {
        showWaiting();
        $("#mainForm").submit();
    }
    function printWDPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohArWithdrawal/1/printStep",'_blank');
    }

    function toDsView(submissionNo,dsType) {
        let url = "";
        console.log(submissionNo);
        console.log(dsType);
        url = '${pageContext.request.contextPath}/eservice/INTERNET/MohDsAction?dsType='+dsType +"&type=preview"+"&submissionNo="+ submissionNo ;
        showPopupWindow(url);
    }

</script>