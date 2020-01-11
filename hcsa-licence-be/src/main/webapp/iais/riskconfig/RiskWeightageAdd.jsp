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
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskLegislativeConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Risk Configuration</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service Name</th>
                                        <th>Last Inspection</th>
                                        <th>Second Last Inspection</th>
                                        <th>Financial Scheme Audit</th>
                                        <th>Leadership And Governance</th>
                                        <th>Legislative Breaches</th>
                                        <th>Effective Start Date</th>
                                        <th>Effective End Date</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="leg" items="${wightageDto.weightageDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${leg.edit}">
                                                        <p><strong>${leg.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${leg.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>last" name="<c:out value="${leg.serviceCode}"/>last"value="<c:out value="${leg.doLastInp}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}lastInp" var = "lastInp"/>
                                                <span class="error-msg" id="<c:out value="${lastInp}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>secLast" name="<c:out value="${leg.serviceCode}"/>secLast"value="<c:out value="${leg.doSecLastInp}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}secLastInp" var = "secLastInp"/>
                                                <span class="error-msg" id="<c:out value="${secLastInp}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>fin" name="<c:out value="${leg.serviceCode}"/>fin"value="<c:out value="${leg.doFinancial}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}fin" var = "fin"/>
                                                <span class="error-msg" id="<c:out value="${fin}"/>" name="iaisErrorMsg"></span>
                                            </td>

                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>lea" name="<c:out value="${leg.serviceCode}"/>lea"value="<c:out value="${leg.doLeadship}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}lea" var = "lea"/>
                                                <span class="error-msg" id="<c:out value="${lea}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>leg" name="<c:out value="${leg.serviceCode}"/>leg"value="<c:out value="${leg.doLegislative}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}leg" var = "legis"/>
                                                <span class="error-msg" id="<c:out value="${legis}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${leg.serviceCode}instartdate" name = "${leg.serviceCode}instartdate" value="${leg.doEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.serviceCode}inEffDate" var = "inEffDate"/>
                                                <span class="error-msg" id="<c:out value="${inEffDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${leg.serviceCode}inenddate" name = "${leg.serviceCode}inenddate" value="${leg.doEndDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.serviceCode}inEndDate" var = "inEndDate"/>
                                                <span class="error-msg" id="<c:out value="${inEndDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <c:set value = "error_${leg.serviceCode}totalw" var = "totalw"/>
                                                <span class="error-msg" id="<c:out value="${totalw}"/>" name="iaisErrorMsg"></span>
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
                                <p><a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a></p>
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
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
</script>
