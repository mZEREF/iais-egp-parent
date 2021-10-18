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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskLeadershipConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskLeaderShipVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">



            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Leadership and Governance Risk Configuration</h2>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" width="10%" >Service Name</th>
                                        <th scope="col" width="10%">Threshold  (X Years)</th>
                                        <th scope="col" >Source</th>
                                        <th scope="col" >Effective Start Date</th>
                                        <th scope="col" >Effective End Date</th>
                                        <th scope="col" width="8%">Minimum Number of Cases</th>
                                        <th scope="col" width="8%">Maximum Number of Cases</th>
                                        <th scope="col" >Risk Rating</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="lea" items="${leaderShowDto.leaderShipDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:set value = "error_${lea.svcCode}both" var = "errboth"/>
                                                <span class="error-msg" id="<c:out value="${errboth}"/>" name="iaisErrorMsg"></span>
                                                <c:choose>
                                                    <c:when test="${lea.adIsEdit}">
                                                        <p><strong>${lea.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${lea.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p>
                                                    <iais:select name="${lea.svcCode}inthershold" id="${lea.svcCode}inthershold" options="yearSelectOptions" firstOption="-" value="${lea.adThershold}"></iais:select>
                                                </p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${lea.svcCode}"></c:out>insource" name="<c:out value="${lea.svcCode}"></c:out>insource" value="LGRAT001">
                                                <p>Audit</p>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}instartdate" name = "${lea.svcCode}instartdate" value="${lea.adEffectiveStartDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}inenddate" name = "${lea.svcCode}inenddate" value="${lea.adEffectiveEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${lea.adLeftModCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>inleftmod" name = "<c:out value="${lea.svcCode}"/>inleftmod" maxlength="3"value="${lea.adLeftModCaseCounth == null ? "" : lea.adLeftModCaseCounth}"
                                                           onchange="doChangeMc('${lea.svcCode}','I',true)"
                                                    >
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${lea.adLeftHighCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>inlefthigh" readonly name = "<c:out value="${lea.svcCode}"/>inlefthigh" maxlength="3"
                                                           value="${lea.adLeftHighCaseCounth  == null ? 1 : lea.adLeftHighCaseCounth}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${lea.adRightLowCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>inrightlow"  readonly name = "<c:out value="${lea.svcCode}"/>inrightlow"  maxlength="3" value="${lea.adRightLowCaseCounth == null ? 0 : lea.adRightLowCaseCounth}"
                                                    >
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text"  style="color: <c:if test="${lea.adRightModCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>inrightmod" name = "<c:out value="${lea.svcCode}"/>inrightmod"  maxlength="3"value="${lea.adRightModCaseCounth == null ? "" : lea.adRightModCaseCounth}"   onchange="doChangeMc('${lea.svcCode}','I',false)">
                                                </div></div>
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
                                        <tr>
                                            <td colspan="8">
                                                <c:set value = "error_${lea.svcCode}inThershold" var = "inther"/>
                                                <span class="error-msg" id="<c:out value="${inther}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inRightLowCaseCounth" var = "inrightlow"/>
                                                <span class="error-msg" id="<c:out value="${inrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inLeftModCaseCounth" var = "inleftmod"/>
                                                <span class="error-msg" id="<c:out value="${inleftmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inRightModCaseCounth" var = "inrightmod"/>
                                                <span class="error-msg" id="<c:out value="${inrightmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${lea.svcCode}inLeftHighCaseCounth" var = "inlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${inlefthigh}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${lea.dpIsEdit}">
                                                        <p><strong>${lea.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${lea.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p>     <iais:select name="${lea.svcCode}prthershold" id="${lea.svcCode}prthershold" options="yearSelectOptions" firstOption="-" value="${lea.dpThershold}"></iais:select></p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${lea.svcCode}"></c:out>prsource" name="<c:out value="${lea.svcCode}"></c:out>prsource" value="SOURCE002">
                                                <p>Disciplinary</p>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}prstartdate" name = "${lea.svcCode}prstartdate" value="${lea.dpEffectiveStartDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}prenddate" name = "${lea.svcCode}prenddate" value="${lea.dpEffectiveEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text"  style="color: <c:if test="${lea.dpLeftModCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>prleftmod" name = "<c:out value="${lea.svcCode}"/>prleftmod"  maxlength="3"value="${lea.dpLeftModCaseCounth == null ? "" : lea.dpLeftModCaseCounth}"
                                                           onchange="doChangeMc('${lea.svcCode}','P',true)">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${lea.dpLeftHighCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>prlefthigh" name = "<c:out value="${lea.svcCode}"/>prlefthigh"  maxlength="3"value="${lea.dpLeftHighCaseCounth == null ? 1 : lea.dpLeftHighCaseCounth}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${lea.dpRightLowCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>prrightlow" readonly name = "<c:out value="${lea.svcCode}"/>prrightlow" maxlength="3" value="${lea.dpRightLowCaseCounth == null ? 0 : lea.dpRightLowCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${lea.dpRightModCaseCountherr}">red</c:if>" id="<c:out value="${lea.svcCode}"/>prrightlmod" name = "<c:out value="${lea.svcCode}"/>prrightmod" maxlength="3"value="${lea.dpRightModCaseCounth == null ? "" : lea.dpRightModCaseCounth}" onchange="doChangeMc('${lea.svcCode}','P',false)">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly value="999" style="border-color: white;">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>


                                        </tr>
                                        <tr> <td colspan="8">
                                            <c:set value = "error_${fin.svcCode}prThershold" var = "prther"/>
                                            <span class="error-msg" id="<c:out value="${prther}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prEffDate" var = "prEffDate"/>
                                            <span class="error-msg" id="<c:out value="${prEffDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prEndDate" var = "prEndDate"/>
                                            <span class="error-msg" id="<c:out value="${prEndDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prRightLowCaseCounth" var = "prrightlow"/>
                                            <span class="error-msg" id="<c:out value="${prrightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prLeftModCaseCounth" var = "prleftmod"/>
                                            <span class="error-msg" id="<c:out value="${prleftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prRightModCaseCounth" var = "prrightmod"/>
                                            <span class="error-msg" id="<c:out value="${prrightmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${lea.svcCode}prLeftHighCaseCounth" var = "prlefthigh"/>
                                            <span class="error-msg" id="<c:out value="${prlefthigh}"/>" name="iaisErrorMsg"></span>
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

    function  doChangeMc(svcCode,name,left) {
        var  moId = "#"+svcCode;
        var id = "#"+svcCode;
        if("I" == name){
            if(left){
                moId +="inleftmod";
                id  += "inrightlow";
            }else {
                moId +="inrightmod";
                id  += "inlefthigh";
            }
            doChangeVal( moId,id,left);
        }else if ("P" == name){
            if(left){
                moId +="prleftmod";
                id  += "prrightlow";
            }else {
                moId +="prrightlmod";
                id  += "prlefthigh";
            }
            doChangeVal( moId,id,left);
        }
    }
</script>
