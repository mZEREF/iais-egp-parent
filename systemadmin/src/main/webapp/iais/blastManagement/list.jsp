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
                        <span>Distribution List</span>
                    </h3>
                    <iais:section title="" id="supPoolList">
                        <iais:row>
                            <iais:field value="Description Name"/>
                            <iais:value width="18">
                                <input type="text" name="descriptionSwitch" id="descriptionSwitch" value="${descriptionSwitch}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="MsgName"/>
                            <iais:value width="18">
                                <input type="text" name="msgName" id="msgName" value="${msgName}"/>
                            </iais:value>
                        </iais:row>
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
                    <iais:pagination param="blastSearchParam" result="blastSearchResult"/>
                    <div class="col-xs-12 col-sm-12">
                        <div class="button-group col-xs-6 col-sm-6">
                            <a class="btn btn-file-upload btn-secondary" id="addlist" onclick="addList()">Add Blast Management</a>
                        </div>
                        <div class="button-group col-xs-6 col-sm-6" style="float: right">
                            <a class="btn btn-file-upload btn-secondary" id="delete" onclick="deleteList()">Delete</a>
                        </div>
                    </div>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th></th>
                                <th>Message ID</th>
                                <th>Message</th>
                                <th>Distribution Name</th>
                                <th>Mode of Delivery</th>
                                <th>Scheduled Send date and time</th>
                                <th>Actual send date and time</th>
                                <th>Attachment</th>
                                <th>Status</th>
                                <th>Eidt</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty blastSearchResult.rows}">
                                        <tr>
                                            <td  colspan="10" >
                                                <iais:message key="No Result!" escape="true"></iais:message>
                                                <!--No Record!!-->
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${blastSearchResult.rows}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <td>
                                                    <p><input type="checkbox" name="checkboxlist" value="${item.id}"></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.id}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.msgName}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.distributionName}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.mode}"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.schedule}" pattern="MM/dd/yyyy"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.actual}" pattern="MM/dd/yyyy"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.docName}"/></p>
                                                </td>
                                                <td>
                                                    <p><iais:code code="${item.status}"></iais:code></p>
                                                </td>
                                                <td>
                                                    <p><a onclick="edit('${item.id}')">eidt</a></p>
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