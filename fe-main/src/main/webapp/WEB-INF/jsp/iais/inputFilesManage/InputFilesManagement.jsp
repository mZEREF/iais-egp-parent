<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp"%>
<form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="row form-horizontal">

                    <div class="col-xs-12 col-md-12">
                        <iais:row>

                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Processing File Management" />
                            <div class="col-md-8">
                            </div>
                        </iais:row>

                        <hr>
                        <iais:row>
                            <iais:field width="4" value="File Name" />
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" name="fileName" value="${inputFilesSearchParam.params['fileName']}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="File Type"/>
                            <iais:value width="4" cssClass="col-md-4" >
                                <iais:select id="FileType" name="fileType" options="fileTypeOptions" needSort="true"
                                             cssClass="application_status" firstOption="All" value="${inputFilesSearchParam.params['fileType']}"></iais:select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Status"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select id="Status" name="status" options="statusOptions" needSort="true"
                                             cssClass="application_status" firstOption="All" value="${inputFilesSearchParam.params['status']}"></iais:select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Upload Date Range"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker name="dateFrom" value="${inputFilesSearchParam.params['dateFrom']}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker name="dateTo" value="${inputFilesSearchParam.params['dateTo']}"/>
                            </iais:value>
                        </iais:row>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        id="clearBtn">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        id="searchBtn">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                <div class="components">
                    <iais:pagination param="inputFilesSearchParam" result="inputFilesSearchResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                <thead>
                                <tr >

                                    <iais:sortableHeader needSort="false" field="FILE_NAME" value="File Name"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="FILE_TYPE" value="File Type"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PROCESSING_START" value="Processing Start"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PROCESSING_END" value="Processing End"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="RECORDS_NUM" value="Records Num"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="PROCESSED_RECORDS_NUM" value="Processed Records Num"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="ERROR_RECORDS_NUM" value="Error Records Num"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="SAVED_RECORDS_NUM" value="Saved Records Num"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="STATUS" value="Status"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="CREATED_DT" value="Upload Date"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="" value="Result File"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="" value="Error Records"></iais:sortableHeader>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty inputFilesSearchResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item"
                                                   items="${inputFilesSearchResult.rows}"
                                                   varStatus="status">
                                            <tr >

                                                <td><c:out value="${item.fileName}"/></td>
                                                <td><c:out value="${item.fileType}"/></td>
                                                <td><c:out value="${item.processingStart eq null ? '-' : item.processingStart}"/></td>
                                                <td><c:out value="${item.processingEnd eq null ? '-' : item.processingEnd}"/></td>
                                                <td><c:out value="${item.recordsNum}"/></td>
                                                <td><c:out value="${item.processedRecordsNum}"/></td>
                                                <td><c:out value="${item.errorRecordsNum}"/></td>
                                                <td><c:out value="${item.savedRecordsNum}"/></td>
                                                <td><c:out value="${item.status}"/></td>
                                                <td><fmt:formatDate value='${item.uploadDate}' pattern='dd/MM/yyyy' /></td>
                                                <td>-</td>
                                                <td>-</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <iais:action >
                        <div class="col-xs-12 col-md-2 text-left">
                            <a style="padding-left: 5px;" class="back " href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">
                                <em class="fa fa-angle-left">&nbsp;</em> Back
                            </a>
                        </div>
                    </iais:action>
                </div>
            </div>
        </div>
    </div>
</form>

<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    $("#clearBtn").click(function () {
        $('input[name="fileName"]').val("");
        $('input[name="dateFrom"]').val("");
        $('input[name="dateTo"]').val("");
        $("#Status option:first").prop("selected", 'selected');
        $("#FileType option:first").prop("selected", 'selected');
    })

    $("#searchBtn").click(function () {
        showWaiting();
        $('#CrudActionType').val('search');
        $('#mainForm').submit();
    })


</script>
