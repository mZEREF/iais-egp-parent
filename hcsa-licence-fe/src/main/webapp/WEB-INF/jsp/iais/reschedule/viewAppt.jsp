
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
<%@include file="../common/dashboard.jsp"%>
<style>
    .form-check {
        display: revert;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <iais:pagination  param="SearchParam" result="SearchResult"/>

                    <br>
                    <div class="table-responsive">

                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" class="form-check" >
                                </th>
                                <iais:sortableHeader needSort="false" field="" value="S/N" ></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="BLK_NO" value="Mode of Service Delivery"  isFE="true"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Service(s)" ></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="RECOM_IN_DATE" isFE="true"  value="Date and Time of Inspection"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty apptViewDtos}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${apptViewDtos}" varStatus="status">
                                        <tr>
                                            <c:if test="${pool.canReschedule}">
                                                <td class="form-check" >
                                                    <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                           name="appIds" value="${pool.viewCorrId}" onclick="javascript:controlCease()"  >
                                                    <label class="form-check-label" for="licence${status.index + 1}"><span
                                                            class="check-square"></span>
                                                    </label>
                                                </td>
                                            </c:if>
                                            <c:if test="${!pool.canReschedule}">
                                                <td >
                                                </td>
                                            </c:if>
                                            <td class="row_no"><c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/></td>
                                            <td><c:out value="${pool.address}"/></td>
                                            <td>
                                                <c:forEach var="svcId" items="${pool.svcIds}">
                                                    <iais:service value="${svcId}"></iais:service><br>
                                                </c:forEach>
                                            </td>
                                            <td><fmt:formatDate value="${pool.inspStartDate}" pattern="${AppConsts.DEFAULT_DATE_TIME_FORMAT}" /></td>

                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <iais:row>
                        <iais:action style="text-align:left;">
                            <div align="left"><span><a  href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"> </em> Back</a></span></div>
                        </iais:action>
                        <br>
                        <iais:action style="text-align:right;"  >
                            <button class="btn btn-primary RescheduleButton" type="button" disabled onclick="doRequest()">Request to reschedule</button>
                        </iais:action>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
    <h3></h3>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
    function checkAll() {
        if ($('#checkboxAll').is(':checked')) {
            $("input[name='appIds']").attr("checked", "true");
            var chk = $("[name='appIds']:checked");
            var dropIds = new Array();
            chk.each(function () {
                dropIds.push($(this).val());
            });
            if(dropIds.length!==0){
                $('.RescheduleButton').prop('disabled',false);

            }

        } else {
            $("input[name='appIds']").removeAttr("checked");
            $('.RescheduleButton').prop('disabled',true);
        }
    }


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

    function doRequest(){
        showWaiting();
        $("[name='crud_action_type']").val("request");
        $("#mainForm").submit();
    }
    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val("sort");
        $("#mainForm").submit();
    }
    function jumpToPagechangePage(){
        $("[name='crud_action_type']").val("page");
        $("#mainForm").submit();
    }
</script>