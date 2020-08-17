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
<%@include file="dashboard.jsp" %>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <c:choose>
                    <c:when test="${DashboardTitle != null && DashboardTitle !=''}">
                        <div class="col-xs-12"><h1>${DashboardTitle}</h1></div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${'APTY005' == AppSubmissionDto.appType}">
                                <%@include file="../resForChange/amendHeader.jsp"%>
                            </c:when>
                            <c:when test="${'APTY004' == AppSubmissionDto.appType}">
                                <%@include file="../withoutrenewal/renewalHeader.jsp"%>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    </div>
    <br/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">

                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <br/>
                                <h2>Payment Summary</h2>
                                <%--<p >--%>
                                    <%--Total amount due: ${dAmount}--%>
                                <%--</p>--%>
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
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td><p>Total amount due:</p></td>
                                        <td><p><strong> <c:out value="${dAmount}"></c:out></strong></p></td>
                                    </tr>
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
    $('#proceed').click(function () {var flag=false;
        $("input[name='payMethod']").each(function () {

            if ( $(this).prop("checked")){
                flag=true;
            }
        });
        if(!flag){
            $('#error_pay').html("The field is mandatory.");
            return;
        }
        doSubmitForm('prePayment','', '');
    });

</script>