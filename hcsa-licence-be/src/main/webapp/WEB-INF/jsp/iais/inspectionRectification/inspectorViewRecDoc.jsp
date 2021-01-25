<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/17
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainCheckListForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <iais:section title="" id = "">
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Inspection Date</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><fmt:formatDate value='${inspectionReportDto.inspectionDate}' pattern='dd/MM/yyyy' /></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Inspection Start Time (HH MM)</label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${empty inspectionReportDto.inspectionStartTime}">
                        <span style="font-size: 16px"><c:out value="-"/></span>
                      </c:if>
                      <c:if test="${not empty inspectionReportDto.inspectionStartTime}">
                        <span style="font-size: 16px"><c:out value="${inspectionReportDto.inspectionStartTime}"/></span>
                      </c:if>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Inspection End Time (HH MM)</label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${empty inspectionReportDto.inspectionStartTime}">
                        <span style="font-size: 16px"><c:out value="-"/></span>
                      </c:if>
                      <c:if test="${not empty inspectionReportDto.inspectionStartTime}">
                        <span style="font-size: 16px"><c:out value="${inspectionReportDto.inspectionEndTime}"/></span>
                      </c:if>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Inspector Lead</label>
                    </div>
                    <div class="col-md-6">
                      <c:forEach var = "insepctionLead" items = "${inspectionReportDto.inspectorLeads}">
                        <span style="font-size: 16px"><c:out value="${insepctionLead}"/></span>
                      </c:forEach>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Inspection Officer(s)</label>
                    </div>
                    <div class="col-md-6">
                      <c:forEach var = "insepctor" items = "${inspectionReportDto.inspectors}">
                        <span style="font-size: 16px"><c:out value="${insepctor}"/></span>
                      </c:forEach>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Other Inspection Officer(s)</label>
                    </div>
                    <div class="col-md-6">
                      <c:forEach var = "otherInsepctor" items = "${inspectionReportDto.inspectOffices}">
                        <span style="font-size: 16px"><c:out value="${otherInsepctor}"/></span>
                      </c:forEach>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">No. of Non-Compliance</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionReportDto.ncCount}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Remarks</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionReportDto.taskRemarks}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Best Practices</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${inspectionReportDto.bestPractice}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Letter written to Licensee</label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${fileRepoDto != null}">
                        <div class="fileList ">
                            <span class="filename server-site" id="140" style="font-size: 16px">
                              <u><a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo0&fileRo0=<iais:mask name="fileRo0" value="${fileRepoDto.id}"/>&fileRepoName=${fileRepoDto.fileName}" title="Download" class="downloadFile">${fileRepoDto.fileName}</a></u>
                            </span>
                        </div>
                      </c:if>
                      <c:if test="${fileRepoDto == null}">
                        <span style="font-size: 16px">-</span>
                      </c:if>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">TCU</label>
                    </div>
                    <div class="col-md-6">
                      <input type="checkbox" name="inspectorCheck" disabled id="inspectorCheck" <c:if test="${'Yes' eq inspectionReportDto.markedForAudit}">checked="checked"</c:if>/>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">TCU Date</label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${inspectionReportDto.tcuDate != null}">
                        <span style="font-size: 16px"><fmt:formatDate value='${inspectionReportDto.tcuDate}' pattern='dd/MM/yyyy' /></span>
                      </c:if>
                      <c:if test="${inspectionReportDto.tcuDate == null}">
                        <span style="font-size: 16px">-</span>
                      </c:if>
                    </div>
                  </div>
                  <p></p>
                  <iais:action >
                    <a class="back" id="Back" onclick="javascript:doInspRecCheckListBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspRecCheckListView()">View Checklist</button>
                  </iais:action>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<script>
    function doInspRecCheckListBack() {
        showWaiting();
        $("#actionValue").val('back');
        inspRecCheckListSubmit('back');
    }

    function doInspRecCheckListView() {
        showWaiting();
        $("#actionValue").val('list');
        inspRecCheckListSubmit('list');
    }

    function inspRecCheckListSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainCheckListForm');
        mainPoolForm.submit();
    }
</script>
