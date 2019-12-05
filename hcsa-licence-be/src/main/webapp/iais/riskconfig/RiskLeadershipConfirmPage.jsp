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
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service Name</th>
                                        <th>Threshold</th>
                                        <th>Source</th>
                                        <th>Mininum Number of Cases</th>
                                        <th>Maximun Number of Cases</th>
                                        <th>Risk Rating</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="fin" items="${financialShowDto.financeList}" varStatus="status">
                                        <c:choose>
                                            <c:when test="${fin.finSource eq 'SOURCE001'}">
                                                <tr>
                                                    <td>
                                                        <p>${fin.serviceName}</p>
                                                    </td>
                                                    <td>
                                                        <p><input type="text" value="<c:out value="${fin.thershold}"></c:out>">
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" value="<c:out value="${fin.serviceCode}"></c:out>Institution">
                                                        <p>Institution</p>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="0"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value="${fin.lowCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5"value="${fin.highCaseCountth}"></div></div>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="${fin.lowCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value="${fin.highCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5" value="999"></div></div>
                                                    </td>
                                                    <td>
                                                        <div style="width: 100px;margin-top: 15px;">Low</div>
                                                        <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                        <div style="width: 100px;margin-top: 45px;">High</div>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td>
                                                        <p>${fin.serviceName}</p>
                                                    </td>
                                                    <td>
                                                        <p><input type="text" value=""></p>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" value="<c:out value="${fin.serviceCode}"></c:out>Institution">
                                                        <p>Institution</p>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="0"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5"value=""></div></div>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5" value="999"></div></div>
                                                    </td>
                                                    <td>
                                                        <div style="width: 100px;margin-top: 15px;">Low</div>
                                                        <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                        <div style="width: 100px;margin-top: 45px;">High</div>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>


                                        <c:choose>
                                            <c:when test="${fin.finSource eq 'SOURCE002'}">
                                                <tr>
                                                    <td>
                                                        <p>${fin.serviceName}</p>
                                                    </td>
                                                    <td>
                                                        <p><input type="text" value="<c:out value="${fin.thershold}"></c:out>"></p>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" value="<c:out value="${fin.serviceCode}"></c:out>Practitioner">
                                                        <p>Practitioner${fin.finSource}</p>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="0"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value="${fin.lowCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5"value="${fin.highCaseCountth}"></div></div>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="${fin.lowCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value="${fin.highCaseCountth}"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5" value="999"></div></div>
                                                    </td>
                                                    <td>
                                                        <div style="width: 100px;margin-top: 15px;">Low</div>
                                                        <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                        <div style="width: 100px;margin-top: 45px;">High</div>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td>
                                                        <p>${fin.serviceName}</p>
                                                    </td>
                                                    <td>
                                                        <p><input type="text" value=""></p>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" value="<c:out value="${fin.serviceCode}"></c:out>Practitioner">
                                                        <p>Practitioner</p>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value="0"></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5"value=""></div></div>
                                                    </td>
                                                    <td>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="a2"  maxlength="5" value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="b2"  maxlength="5"value=""></div></div>
                                                        <div><div style="width: 80px;"></div><div style="width: 80px;float: left"><input type="text" name="c2"  maxlength="5" value="999"></div></div>
                                                    </td>
                                                    <td>
                                                        <div style="width: 100px;margin-top: 15px;">Low</div>
                                                        <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                        <div style="width: 100px;margin-top: 45px;">High</div>
                                                    </td>
                                                </tr>

                                            </c:otherwise>
                                        </c:choose>

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
        SOP.Crud.cfxSubmit("mainForm","doNext");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","back");
    }
</script>
