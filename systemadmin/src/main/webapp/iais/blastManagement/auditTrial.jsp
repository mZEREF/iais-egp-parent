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
                        <span>Audit Trial</span>
                    </h3>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th>Notification Type</th>
                                <th>Recipient</th>
                                <th>Sender</th>
                                <th>Subject</th>
                                <th>Content</th>
                                <th>Number Attempts</th>
                                <th>log Msg</th>
                                <th>Sent Time</th>
                                <th>File Name</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty SearchResult.rows}">
                                        <tr>
                                            <td  colspan="10" >
                                                <iais:message key="No Result!" escape="true"></iais:message>
                                                <!--No Record!!-->
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${SearchResult.rows}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <td>
                                                    <p><c:out value="${item.type}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.recipient}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.sender}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.subject}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.content}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.numberAttempts}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.logMsg}"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.sentTime}" pattern="MM/dd/yyyy"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.fileName}"/></p>
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
function addList() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm","create");
}
function deleteList() {
    SOP.Crud.cfxSubmit("mainForm","delete");
}
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