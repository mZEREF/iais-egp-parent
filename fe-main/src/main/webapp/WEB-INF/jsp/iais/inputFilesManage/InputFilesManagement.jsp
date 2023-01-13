<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content" style="min-height: 73vh;">
  <div class="bg-title">
    <h2>
      <span>Assisted Reproduction Input Files Management</span>
    </h2>
  </div>  <div class="bg-title">
  <h2>
    <span>SEARCH BY</span>
  </h2>
</div>
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

    <input type="hidden" id="CrudActionType" name="crud_action_type" >

    <div class="col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <iais:body>
            <iais:section title="" id = "demoList">
              <div>
                <iais:row>
                  <iais:field value="File Name"/>
                  <iais:value width="18">
<%--                    <iais:select id="AR_Centre" name="AR_Centre" options="ar_centre_options" needSort="true"--%>
<%--                                 cssClass="application_status" firstOption="All"></iais:select>--%>
                    <input type="text" name="fileName" value="${inputFilesSearchParam.params['fileName']}"/>
                  </iais:value>
                </iais:row>
                <iais:row>
                  <iais:field value="File Type"/>
                  <iais:value width="18">
<%--                    <input type="text" name="Submission_ID" />--%>
                    <iais:select id="FileType" name="fileType" options="fileTypeOptions" needSort="true"
                                 cssClass="application_status" firstOption="All" value="${inputFilesSearchParam.params['fileType']}"></iais:select>
                  </iais:value>
                </iais:row>

                <iais:row>
                  <iais:field value="Status"/>
                  <iais:value width="18">
<%--                    <iais:select id="Cycle_Stages" name="Cycle_Stages" options="stageOpts" needSort="true"--%>
<%--                                 cssClass="application_status" firstOption="All"></iais:select>--%>
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
                <iais:action style="text-align:right;">
                  <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                  <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                </iais:action>
              </div>
            </iais:section>
            <br>
            <br>
            <h3>
              <span>Search Results</span>
            </h3>
            <iais:pagination param="inputFilesSearchParam" result="inputFilesSearchResult"/>
            <div class="table-gp">
              <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                <thead>
                <tr>
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
                </tr>
                </thead>
                <c:choose>
                  <c:when test="${empty inputFilesSearchResult.rows}">
                    <tr>
                      <td colspan="7">
                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="item" items="${inputFilesSearchResult.rows}"
                               varStatus="status">
                      <tr style="display: table-row;" id="advfilter${(status.index + 1) + (inputFilesSearchParam.pageNo - 1) * inputFilesSearchParam.pageSize}">
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
                       </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </table>
            </div>
          </iais:body>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/include/utils.jsp" %>
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