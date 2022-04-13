<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@include file="dashboard.jsp" %>
<c:set var="showUserMenu" value="true"/>
<%@include file="amendDashboard.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<style>
    .ack-font-16 {
        font-size: 16px;
    }

    .margin-bottom-10 {
        margin-bottom:10px;
    }
</style>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <c:choose>
                    <c:when test="${AckMessage != null}">
                        ${AckMessage}
                    </c:when>
                    <c:otherwise>
                        <label style="font-size: 20px">Submission Successful</label>
                        <c:forEach items="${appSubmissionDtos}" var="appSubmissionDto">
                            <p>- <strong><c:out value="${appSubmissionDto.appSvcRelatedInfoDtoList[0].serviceName}"></c:out></strong></p>
                        </c:forEach>
                        <div class="ack-font-16">
                            <p class="col-xs-12">A confirmation email will be sent to ${emailAddress}.</p>
                            <p class="col-xs-12"><iais:message key="NEW_ACK005" escape="false"></iais:message></p>
                            <c:if test="${dAmount!='$0.0'}">
                                <p class="col-xs-12">Transactional details:</p>
                                <div class="col-xs-12 col-sm-12 margin-bottom-10 table-responsive">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col" >Application No.</th>
                                                <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                                    <th scope="col" >Transactional No.</th>
                                                </c:if>--%>
                                            <th scope="col" >Transactional No.</th>
                                            <th scope="col" >Date & Time</th>
                                            <th scope="col" >Amount Deducted</th>
                                            <th scope="col" >Payment Method</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>${appSubmissionDtos.get(0).appGrpNo}</td>
                                                <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                                    <td><c:out value="${pmtRefNo}"/></td>
                                                </c:if>--%>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty txnRefNo || empty payMethod}">
                                                        N/A
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${txnRefNo}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${createDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                            <td>${dAmount}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty payMethod}">N/A</c:when>
                                                    <c:otherwise> <iais:code code="${payMethod}"/></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </c:if>
                        </div>

                        <div class="col-xs-12 col-sm-12 margin-bottom-10">
                            <div class="button-group col-xs-12 col-sm-6">
                                <p class="print"><div style="font-size: 16px;"><a id="Acknowledgement" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a></div></p>
                            </div>
                            <div class="button-group col-xs-12 col-sm-6 ">
                                <a class="btn btn-primary aMarginleft col-md-6 pull-right" id="GotoDashboard" href="javascript:void(0);">Go to Dashboard</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</form>


<script>
$('#Acknowledgement').click(function () {

    var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFeAckPrintView/1/",request)%>';
    var appType = $('input[name="appType"]').val();
    var suffix = "appType=APTY005&menuRfc=rfc";
    if(url.indexOf('MohFeAckPrintView/1/?') != -1){
        url = url + '&' + suffix;
    }else{
        url = url + '?' + suffix;
    }
    window.open(url,'_blank');
});
$('#GotoDashboard').click(function () {
    Utils.submit('menuListForm','dashboard','','','');
});
</script>