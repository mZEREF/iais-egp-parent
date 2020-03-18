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
                        <h2>Risk Configuration Confrim</h2>
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
                                        <th>Minimum Number of Cases</th>
                                        <th>Maximum Number of Cases</th>
                                        <th>Risk Rating</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="fin" items="${financialShowDto.financeList}" varStatus="status">
                                        <tr>
                                            <td>
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
                                                <p><input type="text" disabled id="<c:out value="${fin.serviceCode}"/>inthershold" name="<c:out value="${fin.serviceCode}"/>inthershold"value="<c:out value="${fin.inThershold}"></c:out>">
                                                </p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>insource" name="<c:out value="${fin.serviceCode}"></c:out>insource" value="SOURCE001">
                                                <p>Institution</p>
                                            </td>
                                            <td><c:out value="${fin.inEffectiveStartDate}"></c:out></td>
                                            <td><c:out value="${fin.inEffectiveEndDate}"></c:out></td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>inleftmod" name = "<c:out value="${fin.serviceCode}"/>inleftmod" maxlength="5"value="${fin.inLeftModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>inlefthigh" name = "<c:out value="${fin.serviceCode}"/>inlefthigh" maxlength="5"value="${fin.inLeftHighCaseCount}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>inrightlow" name = "<c:out value="${fin.serviceCode}"/>inrightlow"  maxlength="5" value="${fin.inRightLowCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>inrightmod" name = "<c:out value="${fin.serviceCode}"/>inrightmod"  maxlength="5"value="${fin.inRightModCaseCounth}">
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
                                                    <c:when test="${fin.prIsEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <p><input type="text" disabled id="<c:out value="${fin.serviceCode}"/>prthershold" name="<c:out value="${fin.serviceCode}"/>prthershold" value="<c:out value="${fin.prThershold}"></c:out>"></p>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.serviceCode}"></c:out>prsource" name="<c:out value="${fin.serviceCode}"></c:out>prsource" value="SOURCE002">
                                                <p>Practitioner</p>
                                            </td>
                                            <td><c:out value="${fin.prEffectiveStartDate}"></c:out></td>
                                            <td><c:out value="${fin.prEffectiveEndDate}"></c:out></td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled readonly maxlength="5" value="0">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>prleftmod" name = "<c:out value="${fin.serviceCode}"/>prleftmod"  maxlength="5"value="${fin.prLeftModCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>prlefthigh" name = "<c:out value="${fin.serviceCode}"/>prlefthigh"  maxlength="5"value="${fin.prLeftHighCaseCount}">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>prrightlow" name = "<c:out value="${fin.serviceCode}"/>prrightlow" maxlength="5" value="${fin.prRightLowCaseCounth}">
                                                </div></div>
                                                <div><div style="width: 80px;"></div><div style="width: 80px;float: left">
                                                    <input type="text" disabled id="<c:out value="${fin.serviceCode}"/>prrightlmod" name = "<c:out value="${fin.serviceCode}"/>prrightmod" maxlength="5"value="${fin.prRightModCaseCounth}">
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
                                <p><a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a></p>
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
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","submit");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","back");
    }
</script>
