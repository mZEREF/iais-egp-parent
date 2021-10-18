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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">

        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2>Resend Email</h2>
                    </div>
                    <div class="row">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Send Date From</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="start" name="start" value="${start}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_start"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Send Date To</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="end" name="end" value="${end}"/>

                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" onclick="clearSearch()">Clear</a>
                                        <a class="btn btn-primary" onclick="searchResult()">Search</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h3>
                        <span>Search Results</span>
                    </h3>
                    <iais:pagination param="resendSearchParam" result="resendSearchResult"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr >
                                <th scope="col" >Sender's Email</th>
                                <th scope="col" >Subject</th>
                                <th scope="col" >Recipients' List Email</th>
                                <th scope="col" >Sent Date / Time</th>
                                <th scope="col" >Status</th>
                                <th scope="col" >Reason for Failure</th>
                                <th scope="col" >Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty resendSearchResult.rows}">
                                    <tr>
                                        <td colspan="10">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
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
                                                <p><fmt:formatDate value="${item.sentTime}"
                                                                   pattern="dd/MM/yyyy HH:mm"/></p>
                                            </td>
                                            <td>
                                                <p>
                                                    <c:choose>
                                                        <c:when test="${item.status == 1}">Delivered</c:when><c:otherwise>Failed</c:otherwise>
                                                    </c:choose>
                                                 </p>
                                            </td>
                                            <td>
                                                <p>
                                                    <c:choose>
                                                        <c:when test="${item.status == 1}">NIL</c:when><c:otherwise>${item.logMsg}</c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </td>
                                            <td>
                                                <p>
                                                    <c:choose>
                                                        <c:when test="${item.status != 1}"><a onclick="edit('<iais:mask name="emailId" value="${item.clientQueryCode}" />','<iais:mask name="notiId" value="${item.notificationId}" />')">Resend</a></c:when>
                                                    </c:choose>
                                                    </p>
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
        <input hidden id="emailId" name="emailId" value="">
        <input hidden id="notiId" name="notiId" value="">
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function edit(id,notiId) {
        $("#emailId").val(id);
        $("#notiId").val(notiId);
        SOP.Crud.cfxSubmit("mainForm", "resend");
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function jumpToPagechangePage() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "page");
    }

    function searchResult() {
        console.log('111')
        SOP.Crud.cfxSubmit("mainForm", "search");
    }

    function clearSearch() {
        $("#xxdays option:first").prop("selected", 'selected');
        $(".current").text("Please Select");
        $('input[name="start"]').val("");
        $('input[name="end"]').val("");
        $("#error_start").hide();
    }

</script>