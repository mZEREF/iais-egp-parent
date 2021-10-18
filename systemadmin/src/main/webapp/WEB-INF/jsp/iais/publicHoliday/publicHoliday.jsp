<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<style type="text/css">
    .btn.btn-md {
        font-size: .986rem;
        font-weight: 600;
        padding: 10px 25px;
        text-transform: uppercase;
        border-radius: 30px;
    }
</style>
<div class="main-content">
    <form method="post" id="mainForm"  enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" id="crud_action_type" value=""/>
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Public Holiday</h2>
                        </div>
                        <div class="row">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-md-3 control-label" >Year <span class="mandatory">*</span></label>
                                    <div class="col-md-3 searchdiv">
                                        <iais:select id="year" name="year" options="yearOption" cssClass="yearOption" firstOption="Please Select"
                                                     value="${year}" ></iais:select>
                                        <span id="yearErr" class="error-msg">${yearErr}</span>
                                    </div>
                                    <label class="col-md-3 control-label">Non-working Date</label>
                                    <div class="col-md-3">
                                        <iais:datePicker id="nonWorking" name="nonWorking" value="${nonWorking}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-horizontal" >
                                <div class="form-group">
                                    <label class="col-md-3 control-label">Holiday Description</label>
                                    <div class="col-md-3 searchdiv">
                                        <iais:select id="phCode" name="phCode" codeCategory="CATE_ID_PUBLIC_HOLIDAY" cssClass="yearOption" firstOption="Please Select"
                                                     value="${phCode}"></iais:select>
                                    </div>
                                    <label class="col-md-3 control-label">Status</label>
                                    <div class="col-md-3 searchdiv">
                                        <iais:select id="searchStatus" name="searchStatus" options="statusOption" cssClass="statusOption" firstOption="Please Select"
                                                     value="${searchStatus}"></iais:select>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-11 col-md-11">
                                        <div class="text-right">
                                                <a class="btn btn-secondary" id="clear">Clear</a>
                                            <a class="btn btn-primary" id="search">Search</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="holidaySearchParam" result="HolidaySearchResult"/>

                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                    <tr >
                                        <th scope="col" style="display: none"></th>
                                        <iais:sortableHeader needSort="false" field="" value=" "/>
                                        <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                        <iais:sortableHeader needSort="true" field="FROM_DATE" value="Year"/>
                                        <iais:sortableHeader needSort="true"  field="FROM_DATE" value="Non-working Date"/>
                                        <iais:sortableHeader needSort="true"  field="PH_DESC" value="Holiday Description"/>
                                        <iais:sortableHeader needSort="true" field="status" value="Status"/>
                                        <iais:sortableHeader needSort="false" field="" value="Action"/>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty HolidaySearchResult.rows}">
                                            <tr>
                                                <td colspan="7">
                                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="item" items="${HolidaySearchResult.rows}" varStatus="status">
                                                <tr>
                                                    <td><input type="checkbox" name="deleteId" value="<iais:mask name="deleteId" value="${item.id}"/>"></td>
                                                    <td><c:out value="${(status.index + 1) + (holidaySearchParam.pageNo - 1) * holidaySearchParam.pageSize}"/></td>
                                                    <td><c:out value="${item.year}"/></td>
                                                    <td><c:out value="${item.nonWorking}"/></td>
                                                    <td><iais:code code="${item.phCode}"></iais:code></td>
                                                    <td><iais:code code="${item.status}"></iais:code></td>
                                                    <td><a class="editHoliday btn btn-secondary btn-md" data-holiday="<iais:mask name="holidayId" value="${item.id}"/>">Edit</a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <div class="file-upload-gp" style="display: inline">
                                            <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                        </div>
                                        <a class="btn btn-secondary" id="delete">Delete</a>
                                        <a class="btn btn-primary" id="createholiday">Create</a>
                                    </div>
                                </div>
                            </div>
                            <p style="margin-left: 100px">
                                <span id="error_selectedFile" name="iaisErrorMsg" class="error-msg"></span>
                                <c:if test="${not empty duplicateDateStrList}">
                                    <c:forEach var="duplicateDateStr" items="${duplicateDateStrList}">
                                        <br><span class="error-msg"><c:out value="${duplicateDateStr}"></c:out></span>
                                    </c:forEach>
                                </c:if>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        <input id="holidayId" name="holidayId" hidden value="">
        <iais:confirm msg="Please select record for deletion."  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <iais:confirm msg="Are you sure you want to delete this item?" yesBtnCls="okBtn btn btn-primary"   needCancel="true" callBack="deleteDis()" popupOrder="deleteSupport" ></iais:confirm>
    </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">

    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $('#mainForm').submit();
    }

    $('#createholiday').click(function () {
        submit("create");
    });

    function deleteDis() {
        submit("delete");
    }
    function cancel() {
        $('#support').modal('hide');
    }

    $('.editHoliday').click(function () {
        console.log("1111")
        var id = $(this).attr('data-holiday');
        console.log(id)
        $("#holidayId").val(id);
        submit("edit");
    });

    $('#delete').click(function () {
        if ($("input:checkbox:checked").length > 0) {
            $('#deleteSupport').modal('show');
        } else {
            $('#support').find("span").eq(1).html("Please select record for deletion.");
            $('#support').modal('show');
        }
    })

    $('#search').click(function () {
        $("[name='crud_action_type']").val("search")
        $("#mainForm").submit();
    });
    
    $('#clear').click(function () {
        $('input[name="description"]').val("");
        $('input[name="nonWorking"]').val("");
        $("#searchStatus option:first").prop("selected", 'selected');
        $("#phCode option:first").prop("selected", 'selected');
        $("#year option:first").prop("selected", 'selected');
        $(".searchdiv .current").text("Please Select");
        $(".error-msg").text("");

        $.ajax({
            data: {remove: 1},
            type: "POST",
            dataType: 'json',
            url: '/system-admin-web/emailAjax/removePublicSearchParam.do',
            error: function (data) {

            },
            success: function (data) {

            }
        })
    })
    function jumpToPagechangePage() {
        submit("page");
    }

    $('#selectedFile').change(function () {
        submit("upload");
    })

    $('#selectedFile').change(function () {
        var file = $(this).val();
        var fileName = Utils.getFileName(file);
        $(".fileNameDisplay").text(fileName);
    });


</script>