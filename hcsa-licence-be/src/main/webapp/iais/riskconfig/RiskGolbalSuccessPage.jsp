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
                        <h2>Risk Success</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <div style="overflow-x: auto; width: 100%;">
                                <table class="table-gp" >
                                    <thead>
                                    <tr>
                                        <th><span>Service Name</span></th>
                                        <th><span>Maximum<br> Licence Tenure</span></th>
                                        <th><span>Last Inspection<br> was more than (X Years)</span></th>
                                        <th><span>Eligible<br> for <br>auto renewal</span></th>
                                        <th><span>Type of<br> Inspection Required(New)</span></th>
                                        <th><span>Pre/Post<br> Licensing Inspection(New)</span></th>
                                        <th><span>Type of <br>Inspection Required(Renew)</span></th>
                                        <th><span>Pre/Post <br>Licensing Inspection(Renew)</span></th>
                                        <th><span>Effective Start Date</span></th>
                                        <th><span>Effective End Date</span></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="go" items="${golbalShowDto.goalbalTotalList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${go.totalEdit}">
                                                        <p><strong>${go.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${go.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="2" style="width: 60px" id="<c:out value="${go.serviceCode}"/>maxLic" name="<c:out value="${go.serviceCode}"/>last"value="<c:out value="${go.doMaxLic}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}maxl" var = "maxl"/>
                                                <span class="error-msg" id="<c:out value="${maxl}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="2" style="width: 60px" id="<c:out value="${go.serviceCode}"/>doLast" name="<c:out value="${go.serviceCode}"/>secLast"value="<c:out value="${go.doLastInspection}"></c:out>">
                                                <c:set value = "error_${leg.serviceCode}last" var = "last"/>
                                                <span class="error-msg" id="<c:out value="${last}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <c:set var="svc" value="${go.serviceCode}"></c:set>
                                                <iais:select name="${svc}autoreop" options="autoRenewOp" firstOption="Please select" value="${go.doAutoRenew}"></iais:select>
                                                <c:set value = "error_${leg.serviceCode}auto" var = "auto"/>
                                                <span class="error-msg" id="<c:out value="${auto}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:select name="${svc}newinpTypeOps" options="inpTypeOp" firstOption="Please select" value="${go.donewInspectType}"></iais:select>
                                                <c:set value = "error_${leg.serviceCode}newit" var = "newit"/>
                                                <span class="error-msg" id="<c:out value="${newit}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:select name="${svc}newPreOrPostOps" options="PreOrPostOp" firstOption="Please select" value="${go.donewIsPreInspect}"></iais:select>
                                                <c:set value = "error_${leg.serviceCode}newpp" var = "newpp"/>
                                                <span class="error-msg" id="<c:out value="${newpp}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:select name="${svc}renewinpTypeOps" options="inpTypeOp" firstOption="Please select" value="${go.dorenewInspectType}"></iais:select>
                                                <c:set value = "error_${leg.serviceCode}renewit" var = "renewit"/>
                                                <span class="error-msg" id="<c:out value="${renewit}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:select name="${svc}renewPreOrPostOps" options="PreOrPostOp" firstOption="Please select" value="${go.dorenewIsPreInspect}"></iais:select>
                                                <c:set value = "error_${leg.serviceCode}renewpp" var = "renewpp"/>
                                                <span class="error-msg" id="<c:out value="${renewpp}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${go.serviceCode}instartdate" name = "${go.serviceCode}instartdate" value="${go.doEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.serviceCode}inEffDate" var = "inEffDate"/>
                                                <span class="error-msg" id="<c:out value="${inEffDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${go.serviceCode}inenddate" name = "${go.serviceCode}inenddate" value="${go.doEndDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.serviceCode}inEndDate" var = "inEndDate"/>
                                                <span class="error-msg" id="<c:out value="${inEndDate}"/>" name="iaisErrorMsg"></span>
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
                    </div>
                </div>
            </div>
        </div>

    </div>
</form>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">

</script>
