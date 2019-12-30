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
                                        <th>Maximum Licence Tenure</th>
                                        <th>Last Inspection was more than (X Years)</th>
                                        <th>Eligible for auto renewal</th>
                                        <th>Type of Inspection Required(New)</th>
                                        <th>Pre/Post Licensing Inspection(New)</th>
                                        <th>Type of Inspection Required(Renew)</th>
                                        <th>Pre/Post Licensing Inspection(Renew)</th>
                                        <th>Effective Start Date</th>
                                        <th>Effective End Date</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="go" items="${golbalShowDto.goalbalTotalList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${go.totalEdit}">
                                                        <p><b>${go.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${go.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>maxLic" name="<c:out value="${go.serviceCode}"/>last"value="<c:out value="${go.doMaxLic}"></c:out>">

                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>doLast" name="<c:out value="${go.serviceCode}"/>secLast"value="<c:out value="${go.doLastInspection}"></c:out>">

                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>autoRenew" name="<c:out value="${go.serviceCode}"/>fin"value="<c:out value="${go.doAutoRenew}"></c:out>">

                                            </td>

                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>newInpT" name="<c:out value="${go.serviceCode}"/>lea"value="<c:out value="${go.donewInspectType}"></c:out>">

                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>newIsp" name="<c:out value="${go.serviceCode}"/>leg"value="<c:out value="${go.donewIsPreInspect}"></c:out>">

                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>renewInpT" name="<c:out value="${go.serviceCode}"/>lea"value="<c:out value="${go.dorenewInspectType}"></c:out>">

                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${go.serviceCode}"/>renewIsp" name="<c:out value="${go.serviceCode}"/>leg"value="<c:out value="${go.dorenewIsPreInspect}"></c:out>">

                                            </td>
                                            <td>
                                                <iais:datePicker id = "${go.serviceCode}instartdate" name = "${go.serviceCode}instartdate" value="${go.doEffectiveDate}"></iais:datePicker>

                                            </td>
                                            <td>
                                                <iais:datePicker id = "${go.serviceCode}inenddate" name = "${go.serviceCode}inenddate" value="${go.doEndDate}"></iais:datePicker>
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
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
</script>
