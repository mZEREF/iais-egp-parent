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
                        <iais:error>
                            <c:if test = "${not empty errorMap}">
                                <div class="error">
                                    <c:forEach items="${errorMap}" var="map">
                                        ${map.key}  ${map.value} <br/>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </iais:error>
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
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="lea" items="${leaderShowDto.leaderShipDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${lea.isAdEdit}">
                                                        <p><b>${lea.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${lea.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><input type="text" id="<c:out value="${lea.svcCode}"/>inthershold" name="<c:out value="${lea.svcCode}"/>inthershold"value="<c:out value="${lea.adThershold}"></c:out>">
                                                </p>
                                            </td>

                                            <td>
                                                <input type="hidden" id="<c:out value="${lea.svcCode}"></c:out>insource" name="<c:out value="${lea.svcCode}"></c:out>insource" value="LGRAT001">
                                                <p>Institution</p>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}instartdate" name = "${lea.svcCode}instartdate" value="${lea.inEffectiveStartDate}"></iais:datePicker></td>
                                            <td><iais:datePicker id = "${lea.svcCode}inenddate" name = "${lea.svcCode}inenddate" value="${lea.inEffectiveEndDate}"></iais:datePicker></td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>inleftmod" name = "<c:out value="${lea.svcCode}"/>inleftmod" maxlength="5"value="${lea.adLeftModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>inlefthigh" name = "<c:out value="${lea.svcCode}"/>inlefthigh" maxlength="5"value="${lea.adLeftHighCaseCount}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>inrightlow" name = "<c:out value="${lea.svcCode}"/>inrightlow"  maxlength="5" value="${lea.adRightLowCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>inrightmod" name = "<c:out value="${lea.svcCode}"/>inrightmod"  maxlength="5"value="${lea.adRightModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly  maxlength="5" value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>

                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${lea.prIsEdit}">
                                                        <p><b>${lea.serviceName}</b></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${lea.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><input type="text" id="<c:out value="${lea.svcCode}"/>prthershold" name="<c:out value="${lea.svcCode}"/>prthershold" value="<c:out value="${lea.dpThershold}"></c:out>"></p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${lea.svcCode}"></c:out>prsource" name="<c:out value="${lea.svcCode}"></c:out>prsource" value="SOURCE002">
                                                <p>Practitioner</p>
                                            </td>
                                            <td><iais:datePicker id = "${lea.svcCode}prstartdate" name = "${lea.svcCode}prstartdate" value="${lea.dpEffectiveStartDate}"></iais:datePicker></td>
                                            <td><iais:datePicker id = "${lea.svcCode}prenddate" name = "${lea.svcCode}prenddate" value="${lea.dpEffectiveEndDate}"></iais:datePicker></td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>prleftmod" name = "<c:out value="${lea.svcCode}"/>prleftmod"  maxlength="5"value="${lea.dpLeftModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>prlefthigh" name = "<c:out value="${lea.svcCode}"/>prlefthigh"  maxlength="5"value="${lea.dpLeftHighCaseCount}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>prrightlow" name = "<c:out value="${lea.svcCode}"/>prrightlow" maxlength="5" value="${lea.dpRightLowCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" id="<c:out value="${lea.svcCode}"/>prrightlmod" name = "<c:out value="${lea.svcCode}"/>prrightmod" maxlength="5"value="${lea.dpRightModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly value="999">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
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
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
</script>
