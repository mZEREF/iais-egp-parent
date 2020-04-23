<%--
  Created by IntelliJ IDEA.
  User: wangyu
  Date: 2020/04/01
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
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="actionTodo" id="actionTodo" />
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>${modulename}</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>HCI Code</th>
                                        <th>HCI Name</th>
                                        <th>Address</th>
                                        <th>Service Name</th>
                                        <th>Audit Type</th>
                                        <th>Inspector</th>
                                        <th>Cancel Reason</th>
                                        <th>Select for Audit</th>
                                        <th>Reason</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                  <c:if test="${empty auditTaskDataDtos}">
                                      <tr>
                                          <td colspan="8" align="center">
                                              <iais:message key="ACK018" escape="true"/>
                                          </td>
                                      </tr>
                                  </c:if>
                                    <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>
                                            <td><c:out value="${item.hclCode}"/></td>
                                            <td><c:out value="${item.hclName}"/></td>
                                            <td><c:out value="${item.address}"/></td>
                                            <td><c:out value="${item.svcName}"/></td>
                                            <td>
                                                <iais:code code= "${item.auditType}"/>
                                                <input name="${id}auditType" id="${id}auditType" type="hidden" value="${item.auditType}">
                                            </td>
                                            <td>
                                                <label>${item.inspector}</label>
                                            </td>
                                            <td>
                                                <textarea cols="30" disabled rows="5" name="${id}reason"  maxlength="2000"><c:out value="${item.cancelReason}"></c:out></textarea>
                                            </td>
                                            <td>
                                                <input name="<c:out value="${id}"/>selectForAd" id="<c:out value="${id}"/>selectForAd" type="checkbox" value="ad" <c:if test="${item.selectedForAudit}">checked</c:if>/>
                                                <br/>
                                                <c:set value="error_${id}select" var="errselect"/>
                                                <span class="error-msg" id="<c:out value="${errselect}"/>"
                                                      name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <textarea cols="30" rows="5" name="${id}newReason"  maxlength="2000"><c:out value="${item.reasonForAO}"></c:out></textarea>
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

                    <iais:action style="text-align:right;">
                        <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit('1');">
                          Confirm
                        </button>
                        <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit('0');">
                            Reject
                        </button>
                    </iais:action>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function cancelAudit(action) {
         $("#actionTodo").val(action);
        SOP.Crud.cfxSubmit("mainForm","cancelTask");
    }

</script>
