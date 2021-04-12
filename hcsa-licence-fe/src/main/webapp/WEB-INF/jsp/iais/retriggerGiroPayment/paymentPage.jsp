<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/10/27
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form method="post" class="table-responsive" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" id="paymentMessageValidateMessage" value="${paymentMessageValidateMessage}">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <h2 style="border-bottom: none;">Payment Summary</h2>
                                <c:choose>
                                    <c:when test="${'APTY005' ==AppSubmissionDto.appType || 'APTY002' ==AppSubmissionDto.appType}">
                                        <div class="table-responsive">
                                            <%@include file="../common/newOrRfcPayment.jsp"%>
                                        </div>
                                    </c:when>
                                    <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
                                        <%@include file="../common/renewPayment.jsp"%>
                                    </c:when>
                                </c:choose>
                                <%@include file="../newApplication/paymentMethod.jsp"%>
                            </div>
                            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script>


    $(document).ready(function () {
        $('#nav-tabs-ul').css('display','none');
        $('#paymentTab').css('margin-top','3%');
        $('.progress-tracker').css('display','none');
        $('#BACK').remove();

        $('#premisesli').removeClass('incomplete');
        $('#premisesli').addClass('complete');

        $('#documentsli').removeClass('incomplete');
        $('#documentsli').addClass('complete');

        $('#serviceFormsli').removeClass('incomplete');
        $('#serviceFormsli').addClass('complete');

        $('#previewli').removeClass('incomplete');
        $('#previewli').addClass('complete');

        $('.proceed').click(function () {
            showWaiting();
            $("[name='crud_action_value']").val('next');
            var mainForm = document.getElementById('mainForm');
            mainForm.submit();
        });
    });
</script>