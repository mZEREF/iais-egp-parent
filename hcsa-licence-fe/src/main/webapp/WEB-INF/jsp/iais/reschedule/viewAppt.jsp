
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
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <iais:pagination  param="SearchParam" result="SearchResult"/>

                    <br>
                    <div class="table-gp">

                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value=""></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Premises"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="RECOM_IN_DATE" value="Date and Time of Inspection"></iais:sortableHeader>
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
                                                           name="appIds" value="${pool.appId}|${pool.appCorrId}" onclick="javascript:controlCease()"  >
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

                        <iais:row>
                            <iais:action style="text-align:left;">
                                <div align="left"><span><a  href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"> </em> Back</a></span></div>
                            </iais:action>
                            <br>
                            <iais:action style="text-align:right;"  >
                                <button class="btn btn-primary RescheduleButton" type="button" disabled onclick="doReschedule()">Reschedule</button>
                            </iais:action>
                        </iais:row>
                        <iais:row></iais:row>

                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">


    function controlCease() {
        var checkOne = false;
        var checkBox = $("input[name='appIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;
        if (checkOne) {
            $('.RescheduleButton').prop('disabled',false);
        } else {
            $('.RescheduleButton').prop('disabled',true);
        }
    }

    function doReschedule(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "comm")

    }
    function sortRecords(sortFieldName, sortType) {
        showWaiting();
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        SOP.Crud.cfxSubmit("mainForm", "sort", sortFieldName, sortType);
    }
    function jumpToPagechangePage(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "page")
    }
</script>