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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskFinancialConfigDelegator"/>
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
                                        <th>Threshold</th>
                                        <th>Source</th>
                                        <th>Effective Start Date</th>
                                        <th>Effective End Date</th>
                                        <th>Mininum Number of Cases</th>
                                        <th>Maximun Number of Cases</th>
                                        <th>Risk Rating</th>
                                        <th></th>
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
                                                        <p><b>${fin.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><input type="text" id="<c:out value="${fin.serviceCode}"/>inthershold" name="<c:out value="${fin.serviceCode}"/>inthershold"value="<c:out value="${fin.inThershold}"></c:out>">
                                                </p>
                                                <c:set value = "error_${fin.serviceCode}inThershold" var = "inther"/>
                                                <span class="error-msg" id="<c:out value="${inther}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>insource" name="<c:out value="${fin.serviceCode}"></c:out>insource" value="SOURCE001">
                                                <p>Institution</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}instartdate" name = "${fin.serviceCode}instartdate" value="${fin.inEffectiveStartDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.serviceCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}inenddate" name = "${fin.serviceCode}inenddate" value="${fin.inEffectiveEndDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.serviceCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.inLeftModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inleftmod" name = "<c:out value="${fin.serviceCode}"/>inleftmod" maxlength="5"value="${fin.inLeftModCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.inLeftHighCaseCounterr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inlefthigh" name = "<c:out value="${fin.serviceCode}"/>inlefthigh" maxlength="5"value="${fin.inLeftHighCaseCount}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text"  style="color: <c:if test="${fin.inRightLowCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inrightlow" name = "<c:out value="${fin.serviceCode}"/>inrightlow"  maxlength="5" value="${fin.inRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.inRightModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>inrightmod" name = "<c:out value="${fin.serviceCode}"/>inrightmod"  maxlength="5"value="${fin.inRightModCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly  maxlength="5" value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                            <td>
                                                <c:set value = "error_${fin.serviceCode}inLeftModCaseCounth" var = "inleftmod"/>
                                                <span class="error-msg" id="<c:out value="${inleftmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inLeftHighCaseCounth" var = "inlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${inlefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inRightLowCaseCounth" var = "inrightlow"/>
                                                <span class="error-msg" id="<c:out value="${inrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}inRightModCaseCounth" var = "inrightmod"/>
                                                <span class="error-msg" id="<c:out value="${inrightmod}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.prIsEdit}">
                                                        <p><b>${fin.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><input type="text" id="<c:out value="${fin.serviceCode}"/>prthershold" name="<c:out value="${fin.serviceCode}"/>prthershold" value="<c:out value="${fin.prThershold}"></c:out>"></p>
                                                <c:set value = "error_${fin.serviceCode}prThershold" var = "prther"/>
                                                <span class="error-msg" id="<c:out value="${prther}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>prsource" name="<c:out value="${fin.serviceCode}"></c:out>prsource" value="SOURCE002">
                                                <p>Practitioner</p>

                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}prstartdate" name = "${fin.serviceCode}prstartdate" value="${fin.prEffectiveStartDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.serviceCode}prEffDate" var = "prEffDate"/>
                                                <span class="error-msg" id="<c:out value="${prEffDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${fin.serviceCode}prenddate" name = "${fin.serviceCode}prenddate" value="${fin.prEffectiveEndDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.serviceCode}prEndDate" var = "prEndDate"/>
                                                <span class="error-msg" id="<c:out value="${prEndDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prLeftModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prleftmod" name = "<c:out value="${fin.serviceCode}"/>prleftmod"  maxlength="5"value="${fin.prLeftModCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prLeftHighCaseCounterr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prlefthigh" name = "<c:out value="${fin.serviceCode}"/>prlefthigh"  maxlength="5"value="${fin.prLeftHighCaseCount}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prRightLowCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prrightlow" name = "<c:out value="${fin.serviceCode}"/>prrightlow" maxlength="5" value="${fin.prRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.prRightModCaseCountherr}">red</c:if>" id="<c:out value="${fin.serviceCode}"/>prrightlmod" name = "<c:out value="${fin.serviceCode}"/>prrightmod" maxlength="5"value="${fin.prRightModCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                            <td>
                                                <c:set value = "error_${fin.serviceCode}prRightModCaseCounth" var = "prrightmod"/>
                                                <span class="error-msg" id="<c:out value="${prrightmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}prRightLowCaseCounth" var = "prrightlow"/>
                                                <span class="error-msg" id="<c:out value="${prrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}prLeftHighCaseCounth" var = "prlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${prlefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.serviceCode}prLeftModCaseCounth" var = "prleftmod"/>
                                                <span class="error-msg" id="<c:out value="${prleftmod}"/>" name="iaisErrorMsg"></span>
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
