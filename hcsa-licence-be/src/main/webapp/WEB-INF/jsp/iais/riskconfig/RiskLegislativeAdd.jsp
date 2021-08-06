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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskLegislativeConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">



            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Legislative Breaches Risk Configuration</h2>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" width="10%">Service Name</th>
                                        <th scope="col" width="10%">Threshold  (X Years)</th>
                                        <th scope="col" >Effective Start Date</th>
                                        <th scope="col" >Effective End Date</th>
                                        <th scope="col" width="8%">Minimum Number of Cases</th>
                                        <th scope="col" width="8%">Maximum Number of Cases</th>
                                        <th scope="col" >Risk Rating</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="leg" items="${legShowDto.legislativeList}" varStatus="status">
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
                                                <p>
                                                    <iais:select name="${leg.svcCode}inthershold" id="${leg.svcCode}inthershold" options="yearSelectOptions" firstOption="-" value="${leg.doThershold}"></iais:select>
                                                </p>
                                            </td>
                                            <td><iais:datePicker id = "${leg.svcCode}instartdate" name = "${leg.svcCode}instartdate" value="${leg.doEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${leg.svcCode}inenddate" name = "${leg.svcCode}inenddate" value="${leg.doEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0"  style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inleftmod" name = "<c:out value="${leg.svcCode}"/>inleftmod" maxlength="3"value="${leg.doLeftModCaseCounth == null ? "" : leg.doLeftModCaseCounth}"
                                                           onchange="doChangeMc('${leg.svcCode}',true)">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;" id="<c:out value="${leg.svcCode}"/>inlefthigh" readonly name = "<c:out value="${leg.svcCode}"/>inlefthigh" maxlength="3"value="${leg.doLeftHighCaseCounth == null ? 1 : leg.doLeftHighCaseCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text"  style="border-color: white;"id="<c:out value="${leg.svcCode}"/>inrightlow"  readonly name = "<c:out value="${leg.svcCode}"/>inrightlow"  maxlength="3" value="${leg.doRightLowCaseCounth == null ? 0 : leg.doRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inrightmod" name = "<c:out value="${leg.svcCode}"/>inrightmod"  maxlength="3"value="${leg.doRightModCaseCounth == null ? "" : leg.doRightModCaseCounth}"   onchange="doChangeMc('${leg.svcCode}',false)">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly  maxlength="3" value="999" style="border-color: white;">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>

                                        </tr>
                                        <tr> <td colspan="7">
                                            <c:set value = "error_${leg.svcCode}inThershold" var = "inther"/>
                                            <span class="error-msg" id="<c:out value="${inther}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inEffDate" var = "inEffdate"/>
                                            <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inEndDate" var = "inEnddate"/>
                                            <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inRightLowCaseCounth" var = "inrightlow"/>
                                            <span class="error-msg" id="<c:out value="${inrightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inLeftModCaseCounth" var = "inleftmod"/>
                                            <span class="error-msg" id="<c:out value="${inleftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inRightModCaseCounth" var = "inrightmod"/>
                                            <span class="error-msg" id="<c:out value="${inrightmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${leg.svcCode}inLeftHighCaseCounth" var = "inlefthigh"/>
                                            <span class="error-msg" id="<c:out value="${inlefthigh}"/>" name="iaisErrorMsg"></span>
                                        </td></tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
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
<%@ include file="RiskModerateChange.jsp" %>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
    function  doChangeMc(svcCode,left) {
        var  moId = "#"+svcCode;
        var id = "#"+svcCode;
        if(left){
            moId +="inleftmod";
            id  += "inrightlow";
        }else {
            moId +="inrightmod";
            id  += "inlefthigh";
        }
        doChangeVal( moId,id,left);
    }
</script>
