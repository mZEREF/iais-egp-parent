<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>

<c:if test="${ DashboardTitle != 'Withdrawal Form'}">
    <webui:setLayout name="iais-internet"/>
</c:if>
<c:if test="${ DashboardTitle == 'Withdrawal Form'}">
    <webui:setLayout name="iais-blank"/>
</c:if>
<%@ include file="previewHeader.jsp" %>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <c:if test="${ DashboardTitle != 'Withdrawal Form'}">
                        <div class="col-xs-12 col-md-10">
                                <%--<strong style="font-size: 2rem;">Preview & Submit</strong>--%>
                        </div>
                        <div class="col-xs-12 col-md-2 text-right">
                            <p class="print" style="font-size: 16px;">
                                <a onclick="printData()" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a>
                            </p>
                        </div>
                    </c:if>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:choose>
                        <c:when test="${dsType == 'ART'}">
                            <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/preview/previewAR.jsp" />
                        </c:when>
                        <c:when test="${dsType == 'LDT'}">
                            <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/labDevelopedTest/section/prviewLdtSection.jsp" />
                        </c:when>
                    </c:choose>
                </div>
                <c:if test="${DashboardTitle != 'Withdrawal Form'}">
                    <%@ include file="previewFooter.jsp" %>
                </c:if>

            </div>
        </div>
    </div>
</form>
<input type="hidden" name="printflag" id="printflag" value="${dsType}">
