<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%--@elvariable id="reqForInfoSearchList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiListViewDto>"--%>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="rfiListGo" value="1">
    <div class="main-content">
        <br>
        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <br>
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr >
                                    <th scope="col" style="display: none"></th>
                                    <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                    <iais:sortableHeader needSort="false" field="" value="Request No."/>
                                    <iais:sortableHeader needSort="false" field="" value="Facility Name"/>
                                    <iais:sortableHeader needSort="false" field="" value="Facility No."/>
                                    <iais:sortableHeader needSort="false" field="" value="Start Date"/>
                                    <iais:sortableHeader needSort="false" field="" value="Due Date"/>
                                    <iais:sortableHeader needSort="false" field="" value="Action"/>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty reqForInfoSearchList}">
                                        <tr>
                                            <td colspan="7">
                                                <iais:message key="GENERAL_ACK018" escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="pool" items="${reqForInfoSearchList}" varStatus="status">
                                            <tr>
                                                <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                                <td><c:out value="${pool.requestNo}"/></td>
                                                <td><c:out value="${pool.facilityName}" /></td>
                                                <td><c:out value="${pool.facilityNo}" /></td>
                                                <td><iais-bsb:format-LocalDate localDate='${pool.startDate}'/></td>
                                                <td><iais-bsb:format-LocalDate localDate='${pool.dueDate}'/></td>
                                                <td>
                                                    <iais:action >
                                                        <a href="#" onclick="javascript:doView('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.id)}');" >View</a>
                                                    </iais:action>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>

                                </tbody>
                            </table>
                            <iais:action style="text-align:left;">
                                <div style="text-align: left"><span><a  href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"> </em> Previous</a></span></div>
                            </iais:action>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">

    function doView(reqInfoId) {
        showWaiting();
        $("[name='crud_action_value']").val(reqInfoId);
        $("#mainForm").submit();
    }

</script>