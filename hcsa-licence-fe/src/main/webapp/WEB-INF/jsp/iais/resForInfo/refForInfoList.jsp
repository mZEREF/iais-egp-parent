<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <h3>
                        <span>Request For Information</span>
                    </h3>
                    <div class="table-gp">
                        <iais:action style="text-align:left;">
                            <div align="left"><span><a  href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initMsgView"><em class="fa fa-angle-left"> </em> Back</a></span></div>
                        </iais:action>
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="REQUEST_DATE " value="Start Date"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="DUE_DATE " value="Due Date"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="REQUESTER_ID" value="Requester ID"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty reqForInfoSearchList}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${reqForInfoSearchList}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value="${pool.licenceNo}"/></td>
                                            <td><fmt:formatDate value="${pool.requestDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                            <td><fmt:formatDate value="${pool.dueDateSubmission}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                            <td><c:out value="${pool.requestUser}" /></td>
                                            <td>
                                                <iais:action >
                                                    <a onclick="javascript:doView('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.id)}');" >View</a>
                                                </iais:action>
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
    </div>
</form>
<script type="text/javascript">

    function doView(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "detail",reqInfoId);
    }

</script>