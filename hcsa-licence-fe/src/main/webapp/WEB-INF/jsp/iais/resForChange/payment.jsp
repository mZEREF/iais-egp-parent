<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="dashboard.jsp" %>
<%@include file="amendDashboard.jsp" %>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_additional" value=""/>
    <input type="hidden" name="psnSwitch" value="">
    <input type="hidden" name="eqHciCode" value="${eqHciCode}">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <br/>
                                <h2>Payment Summary</h2>
                                <%--<p >--%>
                                    <%--Total amount due: ${dAmount}--%>
                                <%--</p>--%>
                                <div class="table-responsive">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col" >Service</th>
                                            <th scope="col" >Application Type</th>
                                            <th scope="col" >Application No.</th>
                                            <th scope="col" >Amount</th>
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
                                </div>
                                <c:choose>
                                    <c:when test="${dAmount=='$0'}">
                                        <input type="hidden" value="false" name="noNeedPayment">
                                        <%@include file="../application/section/noNeedPayment.jsp"%>
                                    </c:when>
                                    <c:otherwise>
                                        <%@include file="../application/section/paymentMethod.jsp"%>
                                    </c:otherwise>
                                </c:choose>

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

<script>
    $(document).ready(function () {
        $('#comDashboard div.navigation-gp').css('margin-left','6.5%');
    });

    $('#BACK').click(function () {
        showWaiting();
        $('input[name="psnSwitch"]').val('back');
        doSubmitForm('prePremisesEdit','','back');
    });

    $('#proceed').click(function () {
        /*var flag=false;
        if($("input[name='payMethod']").length<=0){
            flag=true;
        }else {
            $("input[name='payMethod']").each(function () {
                if ( $(this).prop("checked")){
                    flag=true;
                }
            });
        }
        if(!flag){
            $('#error_pay').html("The field is mandatory.");
            return;
        }*/
        $('input[name="psnSwitch"]').val('next');
        doSubmitForm('jumpBank','', 'next');
    });

</script>