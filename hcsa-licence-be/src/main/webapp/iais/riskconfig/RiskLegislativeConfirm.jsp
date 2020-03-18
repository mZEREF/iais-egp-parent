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
                                        <th>Threshold</th>
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
                                                <p><input type="text" id="<c:out value="${leg.svcCode}"/>inthershold" name="<c:out value="${leg.svcCode}"/>inthershold"value="<c:out value="${leg.doThershold}"></c:out>">
                                                </p>
                                                <c:set value = "error_${leg.svcCode}inThershold" var = "inther"/>
                                                <span class="error-msg" id="<c:out value="${inther}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${leg.svcCode}instartdate" name = "${leg.svcCode}instartdate" value="${leg.doEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.svcCode}inEffDate" var = "inEffdate"/>
                                                <span class="error-msg" id="<c:out value="${inEffdate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${leg.svcCode}inenddate" name = "${leg.svcCode}inenddate" value="${leg.doEndDate}"></iais:datePicker>
                                                <c:set value = "error_${leg.svcCode}inEndDate" var = "inEnddate"/>
                                                <span class="error-msg" id="<c:out value="${inEnddate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inleftmod" name = "<c:out value="${leg.svcCode}"/>inleftmod" maxlength="5"value="${leg.doLeftModCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inlefthigh" name = "<c:out value="${leg.svcCode}"/>inlefthigh" maxlength="5"value="${leg.doLeftHighCaseCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inrightlow" name = "<c:out value="${leg.svcCode}"/>inrightlow"  maxlength="5" value="${leg.doRightLowCaseCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${leg.svcCode}"/>inrightmod" name = "<c:out value="${leg.svcCode}"/>inrightmod"  maxlength="5"value="${leg.doRightModCaseCounth}">
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
                                                <c:set value = "error_${leg.svcCode}inLeftModCaseCounth" var = "inleftmod"/>
                                                <span class="error-msg" id="<c:out value="${inleftmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.svcCode}inLeftHighCaseCounth" var = "inlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${inlefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.svcCode}inRightLowCaseCounth" var = "inrightlow"/>
                                                <span class="error-msg" id="<c:out value="${inrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.svcCode}inRightModCaseCounth" var = "inrightmod"/>
                                                <span class="error-msg" id="<c:out value="${inrightmod}"/>" name="iaisErrorMsg"></span>
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
                               <a class="back" href="javascript:void (0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doNext();">Submit</a></div>
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
