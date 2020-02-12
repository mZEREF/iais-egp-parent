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
                                    Total amount due:
                                    <c:out value="${AppSubmissionDto.amountStr}"></c:out>
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
                                    <c:forEach var="svc" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}">
                                        <tr>
                                            <td>
                                                <p><c:out value="${svc.serviceName}"></c:out></p>
                                            </td>
                                            <td>
                                                <p>Amendment</p>
                                            </td>
                                            <td>
                                                <p><c:out value="${AppSubmissionDto.appGrpNo}"></c:out></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${AppSubmissionDto.amountStr}"></c:out></p>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>
                                <h2>Payment Method</h2>
                                <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
                                <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp
                                <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="GIRO">
                                <label class="form-check-label" ><span class="check-circle"></span>GIRO</label>
                                <span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
                                <br>

                                &nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">&nbsp
                                <img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                <img src="<%=webroot1%>img/payments.png" width="36" height="30" alt="GIRO">
                                <p class="visible-xs visible-sm table-row-title">Proceed</p>
                                <p id="previewAndSub" class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script>
    $('#previewAndSub').click(function () {
        doSubmitForm('prePayment','', '');
    });

</script>