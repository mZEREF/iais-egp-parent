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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainCheckListForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <iais:section title="" id = "">
                  <iais:row>
                    <iais:field value="Inspection Date"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspectionReportDto.inspectionDate}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspection Start Time"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspectionReportDto.inspectionStartTime}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspection End Time"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspectionReportDto.inspectionEndTime}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspector Lead"/>
                    <iais:value width="7">
                      <c:forEach var = "insepctionLead" items = "${inspectionReportDto.inspectorLeads}">
                        <p><label><c:out value="${insepctionLead}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Inspection Officer (s)"/>
                    <iais:value width="7">
                      <c:forEach var = "insepctor" items = "${inspectionReportDto.inspectors}">
                        <p><label><c:out value="${insepctor}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Other Inspection Officers"/>
                    <iais:value width="7">
                      <c:forEach var = "otherInsepctor" items = "${inspectionReportDto.inspectOffices}">
                        <p><label><c:out value="${otherInsepctor}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Non-Compliance"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionReportDto.ncCount}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Remarks"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionReportDto.taskRemarks}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Best Practices"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionTaskPoolListDto.bestPractice}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Letter written to Licensee"/>
                    <iais:value width="7">
                      <p><label><c:out value="${inspectionReportDto.serviceName}"/></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="TCU"/>
                    <iais:value width="7">
                      <p><input type="checkbox" name="inspectorCheck" disabled id="inspectorCheck" <c:if test="${'Yes' eq inspectionReportDto.markedForAudit}">checked="checked"</c:if>/></p>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="TCU Date"/>
                    <iais:value width="7">
                      <p><label><fmt:formatDate value='${inspectionReportDto.tcuDate}' pattern='dd/MM/yyyy' /></label></p>
                    </iais:value>
                  </iais:row>
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
        $("#actionValue").val('back');
        inspRecCheckListSubmit('back');
    }

    function doInspRecCheckListView() {
        $("#actionValue").val('list');
        inspRecCheckListSubmit('list');
    }

    function inspRecCheckListSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainCheckListForm');
        mainPoolForm.submit();
    }
</script>
