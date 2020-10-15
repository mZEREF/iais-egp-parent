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
                                    <label class="col-md-3 control-label">Year</label>
                                    <div class="col-md-3 searchdiv">
                                        <iais:select id="year" name="year" options="yearOption" cssClass="yearOption" firstOption="Please Select"
                                                     value="${year}"></iais:select>
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
                                    <div class="col-md-3">
                                        <input id="description" name="description" type="text" maxlength="255"
                                               value="${description}">
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
                            <table class="table">
                                <thead>
                                <tr align="center">
                                    <iais:sortableHeader needSort="false" field="" value=" "/>
                                    <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                    <iais:sortableHeader needSort="true" field="FROM_DATE" value="Year"/>
                                    <iais:sortableHeader needSort="true"  field="FROM_DATE" value="Non-working Date"/>
                                    <iais:sortableHeader needSort="true"  field="DESCRIPTION" value="Holiday Description"/>
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
                                                    <td><c:out value="${item.description}"/></td>
                                                    <td><iais:code code="${item.status}"></iais:code></td>
                                                    <td><a class="editHoliday" data-holiday="<iais:mask name="holidayId" value="${item.id}"/>">Edit</a></td>
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
                            <p style="margin-left: 100px"><span id="error_selectedFile" name="iaisErrorMsg" class="error-msg"></span></p>
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
    $('#createholiday').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "create");
    });

    function deleteDis() {
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }
    function cancel() {
        $('#support').modal('hide');
    }

    $('.editHoliday').click(function () {
        console.log("1111")
        var id = $(this).attr('data-holiday');
        console.log(id)
        $("#holidayId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "edit");
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
        $("#year option:first").prop("selected", 'selected');
        $(".searchdiv .current").text("Please Select");

    })

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "page");
    }

    $('#selectedFile').change(function () {
        SOP.Crud.cfxSubmit("mainForm", "upload");
    })

    $('#selectedFile').change(function () {
        var file = $(this).val();
        var fileName = Utils.getFileName(file);
        $(".fileNameDisplay").text(fileName);
    });
</script>