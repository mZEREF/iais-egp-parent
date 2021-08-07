<%@ page import="java.util.Date" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <h2 style="border-bottom: none;">Payment Summary</h2>
                                <div class="table-responsive">
                                <%@include file="../common/newOrRfcPayment.jsp"%>
                                </div>
                                <c:choose>
                                    <c:when test="${AppSubmissionDto.amountStr=='$0'}">
                                        <input type="hidden" value="false" name="noNeedPayment">
                                        <%@include file="../newApplication/noNeedPayment.jsp.jsp"%>
                                    </c:when>
                                    <c:otherwise>
                                        <%@include file="paymentMethod.jsp"%>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
<script src=""></script>
<script type="text/javascript">
    $(document).ready(function () {
        unbindAllTabs();
    });

    $('#BACK').click(function () {
        showWaiting();
        submit('preview','back',null);
    });

    $('.proceed').click(function () {

        showWaiting();
        submit('jumpBank','next',null);
    });
</script>

