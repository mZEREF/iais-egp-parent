<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <h3>
                        <span>Request For Information List</span>
                    </h3>
                    <div class="table-gp">
                        <iais:action style="text-align:left;">
                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doNew()">New</button>
                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doBack()">Back</button>
                        </iais:action>
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="Email" value="Email"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="REQUEST_DATE" value="Start Date"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty licPreReqForInfoDtoList}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${licPreReqForInfoDtoList}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value="${pool.licenceNo}"/></td>
                                            <td><c:out value=""/></td>
                                            <td><c:out value="${pool.requestDate}" />/></td>
                                            <td>
                                                <iais:action style="text-align:center;">
                                                    <button type="button"  class="btn btn-default" onclick="javascript:doView('${pool.reqInfoId}');" >View</button>
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
    function doNew(){
        SOP.Crud.cfxSubmit("mainForm", "new");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doView(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "view",reqInfoId);
    }

</script>