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
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>Audit Trail</span>
                    </h3>
                    <iais:pagination param="auditSearchParam" result="searchResult"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <c:choose>
                                <c:when test="${'Email'.equals(mode)}">
                                    <tr >
                                        <th scope="col" >S/N</th>
                                        <th scope="col" >Recipient</th>
                                        <th scope="col" >Subject</th>
                                        <th scope="col" >Content</th>
                                        <th scope="col" >Number of attempts</th>
                                        <th scope="col" >Log message</th>
                                        <th scope="col" >Sent date time</th>
                                        <th scope="col" >Created By</th>
                                        <th scope="col" >Created Date</th>
                                        <th scope="col" >Modified By</th>
                                        <th scope="col" >Modified Date</th>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr >
                                        <th scope="col" >S/N</th>
                                        <th scope="col" >Recipient</th>
                                        <th scope="col" >Header</th>
                                        <th scope="col" >Text</th>
                                        <th scope="col" >Number of attempts</th>
                                        <th scope="col" >Log message</th>
                                        <th scope="col" >Sent date time</th>
                                        <th scope="col" >Created By</th>
                                        <th scope="col" >Created Date</th>
                                        <th scope="col" >Modified By</th>
                                        <th scope="col" >Modified Date</th>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty searchResult.rows}">
                                        <tr>
                                            <td colspan="9">
                                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                <!--No Record!!-->
                                            </td>
                                        </tr>
                                        <input hidden id="rows" value="0">
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${searchResult.rows}" varStatus="status">
                                            <c:set var="massIndex" value="${(status.index + 1) + (auditSearchParam.pageNo - 1) * auditSearchParam.pageSize}"></c:set>
                                            <tr style="display: table-row;">
                                                <td>
                                                    <p><c:out
                                                            value="${massIndex}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.recipient}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.subject}"/></p>
                                                </td>
                                                <td>
                                                    <div><c:out value="${item.content}"/></div>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.numberAttempts}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.logMsg}"/></p>
                                                </td>
                                                <td>
                                                    <p><fmt:formatDate value="${item.sentTime}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${createby}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${createDt}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${modifiedBy}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${modifiedDt}"/></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <div class="row">
                        <div class="col-xs-6 col-sm-6 ">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-6 col-sm-6 text-right ">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/audit-repo" title="Download">Download</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      <input hidden id="editBlast" name="editBlast" value="${editBlast}">
      <input hidden id="mode" name="mode" value="${mode}">
      <input hidden id="msgId" name="msgId" value="${msgId}">
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

<script type="text/javascript">
    $("#back").click(function () {
        $("#crud_action_type").val("back")
        $('#mainForm').submit();
    })

    function jumpToPagechangePage () {
        $("#crud_action_type").val("page")
        $('#mainForm').submit();
    }
</script>