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
                                    <c:choose>
                                        <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
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
                                        </c:when>

                                        <c:otherwise>
                                            <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto">
                                                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                                                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                                                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                                                <!--todo:includedSvcFeeExtList -->
                                                <!--base -->
                                                <tr>
                                                    <td>
                                                        <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                                            <p> <c:out value="${svcName}"></c:out></p>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <p>
                                                            New Licence
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <p>
                                                            <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <p>
                                                            <c:out value="${baseSvcFeeExt.amountStr}"></c:out>
                                                        </p>
                                                    </td>
                                                </tr>

                                                <!--simpleSpecifiedFeeExt -->
                                                <c:if test="${simpleSpecifiedFeeExt.svcNames.size()>0 }">
                                                    <tr>
                                                        <td>
                                                            <p>&nbsp;&nbsp;Simple Specified Services</p>
                                                            <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                                            </c:forEach>

                                                        </td>
                                                        <td>
                                                            <p></p>
                                                        </td>
                                                        <td>
                                                            <p> </p>
                                                        </td>
                                                        <td>
                                                            <p >
                                                                <c:out value="${simpleSpecifiedFeeExt.amountStr}"></c:out>
                                                            </p>
                                                        </td>
                                                    </tr>
                                                </c:if>

                                                <!--complexSpecifiedFeeExt -->
                                                <c:if test="${complexSpecifiedFeeExt.svcNames.size()>0 }">
                                                    <tr>
                                                        <td class="breakdown">
                                                            <p>&nbsp;&nbsp;<em>Complex Specified Services (${complexSpecifiedFeeExt.svcNames.size()})</em></p>
                                                            <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <p></p>
                                                        </td>
                                                        <td>
                                                            <p> </p>
                                                        </td>
                                                        <td>
                                                            <p >
                                                                <c:out value="${complexSpecifiedFeeExt.amountStr}"></c:out>
                                                            </p>
                                                        </td>
                                                    </tr>
                                                </c:if>

                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>

                                    </tbody>
                                </table>
                                <h2>Payment Method</h2>
                                <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
                                <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp
                                <%--<input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="GIRO">
                                <label class="form-check-label" ><span class="check-circle"></span>GIRO</label>--%>
                                <span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
                                <br>

                                &nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">&nbsp
                                <img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                <%--<img src="<%=webroot1%>img/payments.png" width="36" height="30" alt="GIRO">--%>
                                <p class="visible-xs visible-sm table-row-title">Proceed</p>
                                <p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/include/formHidden.jsp" %>
</form>
<script src=""></script>
<script type="text/javascript">

    $('.proceed').click(function () {
        var flag=false;
        $("input[name='payMethod']").each(function () {

        if ( $(this).prop("checked")){
            flag=true;
        }
        });
        if(!flag){
            $('#error_pay').html("The field is mandatory.");
            return;
        }

        submit('payment','ack',null);
    });
</script>

