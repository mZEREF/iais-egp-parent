
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

                    <div class="table-gp">

                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value=""></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="ture"  field="BLK_NO" value="Premises"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="REQUEST_DATE " value="Date and Time of Inspection"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty apptViewDtos}">
                                        <tr>
                                            <td colspan="7">
                                                <iais:message key="ACK018" escape="true"></iais:message>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="pool" items="${apptViewDtos}" varStatus="status">
                                            <tr>
                                                <td class="form-check"  >
                                                    <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                           name="appIds" value="${pool.appId}"   >
                                                    <label class="form-check-label" for="licence${status.index + 1}"><span
                                                            class="check-square"></span>
                                                    </label>
                                                </td>
                                                <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                                <td><c:out value="${pool.address}"/></td>
                                                <td><fmt:formatDate value="${pool.inspStartDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                        <iais:action style="text-align:left;">
                            <div align="left"><span><a  href="/main-web/eservice/INTERNET/MohInternetInbox">< Back</a></span></div>
                        </iais:action>
                        <iais:action style="text-align:right;">
                            <button class="btn btn-secondary" type="button"  onclick="doReschedule()">Reschedule</button>
                        </iais:action>
                        <div class="modal fade" id="apptReschedule" tabindex="-1" role="dialog" aria-labelledby="apptReschedule"
                             style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-primary" onclick="doCommonPool()">Common Pool</button>
                                        <button type="button" class="btn btn-primary" onclick="doRoundRobin()">Round Robin</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function doReschedule(){
        $('#apptReschedule').modal('show');
    }

    function sortRecords(sortFieldName, sortType) {
        SOP.Crud.cfxSubmit("mainForm", "sort", sortFieldName, sortType);
    }
    function doCommonPool() {
        SOP.Crud.cfxSubmit("mainForm", "comm")
    }
    function doRoundRobin() {
        SOP.Crud.cfxSubmit("mainForm", "round")
    }
</script>