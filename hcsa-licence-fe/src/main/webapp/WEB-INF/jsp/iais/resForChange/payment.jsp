<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>

<webui:setLayout name="iais-internet"/>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">

    <%@include file="dashboard.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">

                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <br/>
                                <h2>Payment Summary</h2>
                                <p >
                                    Total amount due:${dAmount}
                                </p>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service</th>
                                        <th>Application Type</th>
                                        <th>Application No.</th>
                                        <th>Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${appSubmissionDtos}" var="appSubmissionDto">

                                            <tr>
                                                <td>
                                                    <p><c:out value="${appSubmissionDto.appSvcRelatedInfoDtoList[0].serviceName}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p>Amendment</p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${appSubmissionDto.appGrpNo}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${appSubmissionDto.amountStr}"></c:out></p>
                                                </td>
                                            </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>
                                <%@include file="../newApplication/paymentMethod.jsp"%>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script>
    $('#proceed').click(function () {
        doSubmitForm('prePayment','', '');
    });

</script>