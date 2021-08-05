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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskFinancialConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">



            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Financial Scheme Audit Risk Configuration</h2>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" width="10%">Service Name</th>
                                        <th scope="col" width="10%">Threshold (X Years)</th>
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
                                    <c:forEach var="fin" items="${financialShowDto.financeList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:set value = "error_${fin.serviceCode}both" var = "errboth"/>
                                                <span class="error-msg" id="<c:out value="${errboth}"/>" name="iaisErrorMsg"></span>
                                                <c:choose>
                                                    <c:when test="${fin.inIsEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p>
                                                    <iais:select name="${fin.serviceCode}inthershold" id="${fin.serviceCode}inthershold" options="yearSelectOptions" firstOption="-" value="${fin.inThershold}"></iais:select>
                                                </p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>insource" name="<c:out value="${fin.serviceCode}"></c:out>insource" value="SOURCE001">
                                                <p>Institution</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}instartdate" name = "${fin.serviceCode}instartdate" value="${fin.inEffectiveStartDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}inenddate" name = "${fin.serviceCode}inenddate" value="${fin.inEffectiveEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.inLeftModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inleftmod" name = "<c:out value="${fin.serviceCode}"/>inleftmod" maxlength="3"value="${fin.inLeftModCaseCounth ==null ? "" : fin.inLeftModCaseCounth}"
                                                           onchange="doChangeMc('${fin.serviceCode}','I',true)">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.inLeftHighCaseCounterr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inlefthigh" readonly name = "<c:out value="${fin.serviceCode}"/>inlefthigh" maxlength="3"value="${fin.inLeftHighCaseCount ==null ? 1 : fin.inLeftHighCaseCount}" >
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text"  style="border-color: white;color: <c:if test="${fin.inRightLowCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inrightlow" readonly name = "<c:out value="${fin.serviceCode}"/>inrightlow"  maxlength="3" value="${fin.inRightLowCaseCounth ==null ? 0 : fin.inRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.inRightModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inrightmod" name = "<c:out value="${fin.serviceCode}"/>inrightmod"  maxlength="3"value="${fin.inRightModCaseCounth == null ? "" : fin.inRightModCaseCounth}"    onchange="doChangeMc('${fin.serviceCode}','I',false)">
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
                                        <tr>
                                            <td colspan="7">
                                                <c:set value = "error_${fin.serviceCode}inThershold" var = "inther"/>
                                                <span class="error-msg" id="<c:out value="${inther}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inRightLowCaseCounth" var = "inrightlow"/>
                                                <span class="error-msg" id="<c:out value="${inrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inLeftModCaseCounth" var = "inleftmod"/>
                                                <span class="error-msg" id="<c:out value="${inleftmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inRightModCaseCounth" var = "inrightmod"/>
                                                <span class="error-msg" id="<c:out value="${inrightmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inLeftHighCaseCounth" var = "inlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${inlefthigh}"/>" name="iaisErrorMsg"></span>
                                        </td></tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.prIsEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p>
                                                    <iais:select name="${fin.serviceCode}prthershold" id="${fin.serviceCode}prthershold" options="yearSelectOptions" firstOption="-" value="${fin.prThershold}"></iais:select>
                                                  </p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>prsource" name="<c:out value="${fin.serviceCode}"></c:out>prsource" value="SOURCE002">
                                                <p>Practitioner</p>

                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}prstartdate" name = "${fin.serviceCode}prstartdate" value="${fin.prEffectiveStartDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}prenddate" name = "${fin.serviceCode}prenddate" value="${fin.prEffectiveEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prLeftModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prleftmod" name = "<c:out value="${fin.serviceCode}"/>prleftmod"  maxlength="3"value="${fin.prLeftModCaseCounth == null ? "" : fin.prLeftModCaseCounth}"
                                                           onchange="doChangeMc('${fin.serviceCode}','P',true)">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.prLeftHighCaseCounterr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prlefthigh" readonly name = "<c:out value="${fin.serviceCode}"/>prlefthigh"  maxlength="3"value="${fin.prLeftHighCaseCount == null ? 1 : fin.prLeftHighCaseCount}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.prRightLowCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prrightlow"  readonly name = "<c:out value="${fin.serviceCode}"/>prrightlow" maxlength="3" value="${fin.prRightLowCaseCounth == null ? 0 : fin.prRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prRightModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prrightlmod" name = "<c:out value="${fin.serviceCode}"/>prrightmod" maxlength="3"value="${fin.prRightModCaseCounth == null ? "" : fin.prRightModCaseCounth}"   onchange="doChangeMc('${fin.serviceCode}','P',false)">
                                                </div>

                                                </div>
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
                                        <tr>  <td colspan="8">
                                            <c:set value = "error_${fin.serviceCode}prThershold" var = "prther"/>
                                            <span class="error-msg" id="<c:out value="${prther}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prEffDate" var = "prEffDate"/>
                                            <span class="error-msg" id="<c:out value="${prEffDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prEndDate" var = "prEndDate"/>
                                            <span class="error-msg" id="<c:out value="${prEndDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prRightLowCaseCounth" var = "prrightlow"/>
                                            <span class="error-msg" id="<c:out value="${prrightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prLeftModCaseCounth" var = "prleftmod"/>
                                            <span class="error-msg" id="<c:out value="${prleftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prRightModCaseCounth" var = "prrightmod"/>
                                            <span class="error-msg" id="<c:out value="${prrightmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.serviceCode}prLeftHighCaseCounth" var = "prlefthigh"/>
                                            <span class="error-msg" id="<c:out value="${prlefthigh}"/>" name="iaisErrorMsg"></span>
                                        </td></tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
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
