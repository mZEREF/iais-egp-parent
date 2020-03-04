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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskIndividualConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaInspectionValidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Individual Risk Configuration</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service Name</th>
                                        <th>Risk Level</th>
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
                                    <c:forEach var="fin" items="${inShowDto.inspectionDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:set value = "error_${fin.svcCode}both" var = "errboth"/>
                                                <span class="error-msg" id="<c:out value="${errboth}"/>" name="iaisErrorMsg"></span>
                                                <c:choose>
                                                    <c:when test="${fin.caEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>calevel" name="<c:out value="${fin.svcCode}"></c:out>calevel" value="C">
                                                <p>Critical</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}castartdate" name = "${fin.svcCode}castartdate" value="${fin.doCaEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}caEffDate" var = "caEffdate"/>
                                                <span class="error-msg" id="<c:out value="${caEffdate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}caenddate" name = "${fin.svcCode}caenddate" value="${fin.doCaEndDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}caEndDate" var = "caEnddate"/>
                                                <span class="error-msg" id="<c:out value="${caEnddate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;">
                                                </div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div>
                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doCaLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>caleftmod" name = "<c:out value="${fin.svcCode}"/>caleftmod" maxlength="5"value="${fin.doCaLeftModCounth}">
                                                </div>
                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doCaLeftHighCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>calefthigh" name = "<c:out value="${fin.svcCode}"/>calefthigh" maxlength="5"value="${fin.doCaLeftHighCounth}">
                                                </div>
                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text"  style="color: <c:if test="${fin.doCaRightLowCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>carightlow" name = "<c:out value="${fin.svcCode}"/>carightlow"  maxlength="5" value="${fin.doCaRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doCaRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>carightmod" name = "<c:out value="${fin.svcCode}"/>carightmod"  maxlength="5"value="${fin.doCaRightModCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly  maxlength="5" value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                            <td>
                                                <c:set value = "error_${fin.svcCode}caLeftModCaseCounth" var = "caleftmod"/>
                                                <span class="error-msg" id="<c:out value="${caleftmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}caLeftHighCaseCounth" var = "calefthigh"/>
                                                <span class="error-msg" id="<c:out value="${calefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}caRightLowCaseCounth" var = "carightlow"/>
                                                <span class="error-msg" id="<c:out value="${carightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}caRightModCaseCounth" var = "carightmod"/>
                                                <span class="error-msg" id="<c:out value="${carightmod}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.mjEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>mjlevel" name="<c:out value="${fin.svcCode}"></c:out>mjlevel" value="SOURCE002">
                                                <p>Major</p>

                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mjstartdate" name = "${fin.svcCode}mjstartdate" value="${fin.doMjEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}mjEffDate" var = "mjEffDate"/>
                                                <span class="error-msg" id="<c:out value="${mjEffDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mjenddate" name = "${fin.svcCode}mjenddate" value="${fin.doMjEndDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}mjEndDate" var = "mjEndDate"/>
                                                <span class="error-msg" id="<c:out value="${mjEndDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjleftmod" name = "<c:out value="${fin.svcCode}"/>mjleftmod"  maxlength="5"value="${fin.doMjLeftModCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjLeftHighCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjlefthigh" name = "<c:out value="${fin.svcCode}"/>mjlefthigh"  maxlength="5"value="${fin.doMjLeftHighCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjRightLowCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjrightlow" name = "<c:out value="${fin.svcCode}"/>mjrightlow" maxlength="5" value="${fin.doMjRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjrightlmod" name = "<c:out value="${fin.svcCode}"/>mjrightmod" maxlength="5"value="${fin.doMjRightModCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                            <td>
                                                <c:set value = "error_${fin.svcCode}mjRightModCaseCounth" var = "mjrightmod"/>
                                                <span class="error-msg" id="<c:out value="${mjrightmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjRightLowCaseCounth" var = "mjrightlow"/>
                                                <span class="error-msg" id="<c:out value="${mjrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjLeftHighCaseCounth" var = "mjlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${mjlefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjLeftModCaseCounth" var = "mjleftmod"/>
                                                <span class="error-msg" id="<c:out value="${mjleftmod}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.miEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>milevel" name="<c:out value="${fin.svcCode}"></c:out>milevel" value="SOURCE002">
                                                <p>Minor</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mistartdate" name = "${fin.svcCode}mistartdate" value="${fin.doMiEffectiveDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}miEffDate" var = "miEffDate"/>
                                                <span class="error-msg" id="<c:out value="${miEffDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mienddate" name = "${fin.svcCode}mienddate" value="${fin.doMiEndDate}"></iais:datePicker>
                                                <c:set value = "error_${fin.svcCode}miEndDate" var = "miEndDate"/>
                                                <span class="error-msg" id="<c:out value="${miEndDate}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mileftmod" name = "<c:out value="${fin.svcCode}"/>mileftmod"  maxlength="5"value="${fin.doMiLeftModCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiLeftHighCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>milefthigh" name = "<c:out value="${fin.svcCode}"/>milefthigh"  maxlength="5"value="${fin.doMiLeftHighCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiRightLowCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mirightlow" name = "<c:out value="${fin.svcCode}"/>mirightlow" maxlength="5" value="${fin.doMiRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mirightlmod" name = "<c:out value="${fin.svcCode}"/>mirightmod" maxlength="5"value="${fin.doMiRightModCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                            <td>
                                                <c:set value = "error_${fin.svcCode}mjRightModCaseCounth" var = "mjrightmod"/>
                                                <span class="error-msg" id="<c:out value="${mjrightmod}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjRightLowCaseCounth" var = "mjrightlow"/>
                                                <span class="error-msg" id="<c:out value="${mjrightlow}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjLeftHighCaseCounth" var = "mjlefthigh"/>
                                                <span class="error-msg" id="<c:out value="${mjlefthigh}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${fin.svcCode}mjLeftModCaseCounth" var = "mjleftmod"/>
                                                <span class="error-msg" id="<c:out value="${mjleftmod}"/>" name="iaisErrorMsg"></span>
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
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <p><a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"> Back</em></a></p>
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
