<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>Blast Management</span>
                    </h3>
                    <iais:section title="" id="supPoolList">
                        <div class="form-group">
                            <iais:value>
                                <label class="col-xs-2 col-md-2 control-label">SendDate Start</label>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <iais:datePicker id="start" name="start"  value="${start}" />
                                </div>
                            </iais:value>
                            <iais:value>
                                <label class="col-xs-2 col-md-2 control-label">SendDate End</label>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <iais:datePicker id="end" name="end"  value="${end}" />
                                </div>
                            </iais:value>
                        </div>
                        <iais:action style="text-align:center;">
                            <button class="btn btn-lg" id="searchbtn" type="button"
                                    style="background:#2199E8; color: white"
                                    onclick="javascript:search()">Search
                            </button>
                            <button class="btn btn-lg" id="clearbtn" type="button"
                                    style="background:#2199E8; color: white"
                                    onclick="javascript:clearSearch()">Clear
                            </button>
                        </iais:action>
                    </iais:section>

                    <iais:pagination param="resendSearchParam" result="resendSearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th>From</th>
                                <th>Subject</th>
                                <th>To</th>
                                <th>Send Date</th>
                                <th>Status</th>
                                <th>Reason for Failure</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty resendSearchResult.rows}">
                                        <tr>
                                            <td  colspan="10" >
                                                <iais:message key="No Result!" escape="true"></iais:message>
                                                <!--No Record!!-->
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${resendSearchResult.rows}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <td>
                                                    <p><c:out value="${item.sender}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.subject}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.recipient}"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.sentTime}" pattern="MM/dd/yyyy HH:MM"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.status}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.logMsg}"/></p>
                                                </td>
                                                <td>
                                                    <p><a onclick="edit('${item.requestRefNum}')">eidt</a></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
      <input hidden id="editBlast" name="editBlast" value="">
    </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
function edit(id) {
    $("#editBlast").val(id);
    SOP.Crud.cfxSubmit("mainForm","edit");
}

function jumpToPagechangePage() {
    SOP.Crud.cfxSubmit("mainForm","search");
}
function search() {
    SOP.Crud.cfxSubmit("mainForm","search");
}
function clearSearch(){
    $('input[name="descriptionSwitch"]').val("");
    $('input[name="msgName"]').val("");
}
</script>