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
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskGolbalRiskConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskGolbalVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">



            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Global Risk Configuration</h2>
                        <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" width ="5%"><span>Service Name</span></th>
                                <th scope="col" width ="8%"><span>Maximum Licence Tenure (X Years)</span></th>
                                <th scope="col" width ="8%"><span>Last Inspection was more than (X Years)</span></th>
                                <th scope="col" width ="12%"><span>Eligible for auto renewal</span></th>
                                <th scope="col" width ="5%"><span>Application Type</span></th>
                                <th scope="col" width ="21%"><span>Type of Inspection Required</span></th>
                                <th scope="col" width ="11%"><span>Pre/Post Licensing Inspection</span></th>
                                <th scope="col" width ="15%"><span>Effective Start Date</span></th>
                                <th scope="col" width ="15%"><span>Effective End Date</span></th>
                            </tr>
                            </thead>
                            <tbody>
                            <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
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
                                        <iais:select name="${go.serviceCode}maxLic" options="yearSelectOptions" firstOption="-" value="${go.doMaxLic}"></iais:select>
                                    </td>
                                    <td>
                                        <iais:select name="${go.serviceCode}doLast" id="${go.serviceCode}doLast" options="yearSelectOptions" firstOption="-" value="${go.doLastInspection}"></iais:select>
                                    </td>
                                    <td>

                                        <c:set var="svc" value="${go.serviceCode}"></c:set>
                                        <iais:select name="${svc}autoreop" options="autoRenewOp" firstOption="Please Select" value="${go.doAutoRenew}"></iais:select>

                                    </td>
                                    <td> New Application</td>
                                    <td>
                                        <iais:select name="${svc}newinpTypeOps" options="inpTypeOp" firstOption="Please Select" value="${go.donewInspectType}"></iais:select>

                                    </td>
                                    <td>
                                        <iais:select name="${svc}newPreOrPostOps" options="PreOrPostOp" firstOption="Please Select" value="${go.donewIsPreInspect}"></iais:select>

                                    </td>
                                    <td>
                                        <iais:datePicker id = "${go.serviceCode}instartdate" name = "${go.serviceCode}instartdate" value="${go.doEffectiveDate}"></iais:datePicker>

                                    </td>
                                    <td>
                                        <iais:datePicker id = "${go.serviceCode}inenddate" name = "${go.serviceCode}inenddate" value="${go.doEndDate}"></iais:datePicker>

                                    </td>
                                </tr>
                                <tr>
                                    <td></td> <td></td>   <td></td> <td></td> <td> Renewal </td>
                                    <td>
                                        <iais:select name="${svc}renewinpTypeOps" options="inpTypeOp" firstOption="Please Select" value="${go.dorenewInspectType}"></iais:select>
                                    </td>
                                    <td>
                                        <iais:select name="${svc}renewPreOrPostOps" options="PreOrPostOp" firstOption="Please Select" value="${go.dorenewIsPreInspect}"></iais:select>
                                    </td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td colspan="9">
                                        <c:set value = "error_${go.serviceCode}maxl" var = "maxl"/>
                                        <span class="error-msg" id="<c:out value="${maxl}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}last" var = "last"/>
                                        <span class="error-msg" id="<c:out value="${last}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}auto" var = "auto"/>
                                        <span class="error-msg" id="<c:out value="${auto}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}newit" var = "newit"/>
                                        <span class="error-msg" id="<c:out value="${newit}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}newpp" var = "newpp"/>
                                        <span class="error-msg" id="<c:out value="${newpp}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}renewit" var = "renewit"/>
                                        <span class="error-msg" id="<c:out value="${renewit}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}renewpp" var = "renewpp"/>
                                        <span class="error-msg" id="<c:out value="${renewpp}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}inEffDate" var = "inEffDate"/>
                                        <span class="error-msg" id="<c:out value="${inEffDate}"/>" name="iaisErrorMsg"></span>
                                        <c:set value = "error_${go.serviceCode}inEndDate" var = "inEndDate"/>
                                        <span class="error-msg" id="<c:out value="${inEndDate}"/>" name="iaisErrorMsg"></span>
                                    </td>
                                </tr>

                            </c:forEach>
                            </tbody>
                        </table>
                        </div>
                                <div class="table-footnote">
                                </div>
                        <div>
                       <c:if test="${backButtonNeed == 'Y'}">
                            <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                       </c:if>
                            <div style="float:right">
                                <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Submit</button>
                            </div>
                        </div>
                        </div>

                </div>
            </div>


</form>
</div>
<%@ include file="validationForRisk.jsp" %>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
</script>
