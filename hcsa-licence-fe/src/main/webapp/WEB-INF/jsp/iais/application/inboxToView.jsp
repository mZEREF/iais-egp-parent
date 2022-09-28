<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./inboxView/dashboard.jsp" %>
<c:if test="${applicationDto.applicationType == 'APTY008'}">
    <jsp:include page="/WEB-INF/jsp/iais/newApplication/cessationViewApp.jsp" />
</c:if>
<c:if test="${applicationDto.applicationType != 'APTY008'}">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type_tab" value="">
        <div class="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <div style="font-size: 16px;margin-left: 89%;padding-bottom: 1%">
                            <p class="print">
                                <a onclick="printRLPDF()"><em class="fa fa-print"></em>Print</a>
                            </p>
                        </div>
                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" id="accordion" role="tablist"
                                                     aria-multiselectable="true" style="margin-top: 40px" >
                                                    <jsp:include page="/WEB-INF/jsp/iais/application/inboxView/inboxLicensee.jsp"/>
                                                    <jsp:include page="/WEB-INF/jsp/iais/application/inboxView/inboxPremise.jsp" />
                                                    <jsp:include page="/WEB-INF/jsp/iais/application/inboxView/inboxSpecialised.jsp" />
                                                    <jsp:include page="/WEB-INF/jsp/iais/application/inboxView/viewForm.jsp" />
                                                    <c:if test="${AppSubmissionDto.appType != 'APTY009'}">
                                                        <%@include file="/WEB-INF/jsp/iais/common/declarations/declarations.jsp"%>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="container">
                                                <div class="col-xs-12 col-md-6 text-left">
                                                    <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                                                            class="fa fa-angle-left"></em> Back</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</c:if>
<input type="hidden" value="${RFC_eqHciNameChange}" id="RFC_eqHciNameChange">
<script>
    $(document).ready(function(){
        $(':input', '#declarations').prop('disabled', true);
    });

    function printRLPDF(){
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFePrintView/1/",request)%>';
        var rfc= (url.indexOf('?') < 0 ? "?" : "&" )+"RFC_eqHciNameChange="+$('#RFC_eqHciNameChange').val();
        var group_renewal_app_rfc = $("#group_renewal_app_rfc").val();
        if(group_renewal_app_rfc == '1'){
            rfc+= "&group_renewal_app_rfc=1"
        }
        window.open(url+rfc,'_blank');
       // window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohAppealPrint?whichPage=relatePage",'_blank');
    }
</script>

