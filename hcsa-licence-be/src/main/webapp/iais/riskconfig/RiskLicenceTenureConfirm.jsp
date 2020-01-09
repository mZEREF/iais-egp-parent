<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    .removeBtn{
        color:red;
        cursor: pointer;
    }
    .new-premise-form-conv{
        border-top:1px solid #BABABA;
        padding-top:40px;
    }
    .underLine{
        border-top:1px solid #BABABA;
        padding-top:40px;
    }
</style>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskFinancialConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <input type="hidden" id="removeValue" name ="removeValue" >
    <input type="hidden" id="addValue" name ="addValue" >
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">

                        <h2>Risk Confirm</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service Name</th>
                                        <th>Effective Start Date</th>
                                        <th>Effective End Date</th>
                                        <th>Minimum Risk Score (greater than)</th>
                                        <th>Maximum Risk Score (less than or equal to)</th>
                                        <th>DateType</th>
                                        <th>Licence Tenure</th>

                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="ten" items="${tenShowDto.licenceTenureDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${ten.edit}">
                                                        <p><b>${ten.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${ten.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><iais:datePicker id = "${ten.svcCode}instartdate" name = "${ten.svcCode}instartdate" value="${ten.doEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${ten.svcCode}inenddate" name = "${ten.svcCode}inenddate" value="${ten.doEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id = "<c:out value="${tenName}leftdiv"/>">
                                                                    <input type="text" maxlength="5" name="<c:out value="${tenName}left"/>" value="<c:out value="${sub.columLeft}"/>">
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
                                                                <input type="text" maxlength="5" name="<c:out value="${sub.id}left"/>" value="">
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <div>
                                                                <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                                <div style="width: 120px;float: left" id="<c:out value="${tenName}right"/>div">
                                                                    <input type="text" maxlength="5" name="<c:out value="${tenName}right"/>" value="<c:out value="${sub.columRight}"/>">
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div>
                                                            <div style="width: 120px;float: left">
                                                                <input type="text" maxlength="5" name="<c:out value="${sub.id}right"/>" value="<c:out value="${sub.columRight}"/>">
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                            <div style="width: 100px;" id = "<c:out value="${tenName}ltdiv"/>">
                                                                <input type="text" maxlength="5" name="<c:out value="${tenName}lt"/>" value="<c:out value="${sub.licenceTenure}"/>">
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 100px;">
                                                            <input type="text" maxlength="5" name="<c:out value="${sub.id}right"/>" value="<c:out value="${sub.licenceTenure}"/>">
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${ten.subDtoList!=null&&!empty ten.subDtoList}">
                                                        <c:forEach var="sub" items="${ten.subDtoList}" varStatus="status">
                                                            <c:set var="tenName" value="${ten.svcCode}${sub.orderNum}"> </c:set>
                                                            <div style="width: 120px;height:50px;margin-bottom:15px;" id ="<c:out value="${tenName}timediv"/>">
                                                                <iais:select name="${tenName}type" options="timeType" firstOption="Please select" value="${sub.dateType}" ></iais:select>
                                                            </div>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div style="width: 120px;height:50px;margin-bottom:15px;">
                                                            <iais:select name="${sub.id}type" options="timeType" firstOption="Please select" value="${sub.dateType}" ></iais:select>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <p><a class="back" href="#" onclick="doBack()"><i class="fa fa-angle-left"></i> Back</a></p>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doNext();">Next</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","submit");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","back");
    }


</script>
