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
  <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Task Re-assignment Confirm</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <iais:row>
                    <iais:field value="${groupRoleFieldDto.groupMemBerName}:"/>
                    <iais:value width="10">
                      <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="name">
                        <p><label><c:out value="${name.text}"/></label></p>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Remarks:"/>
                    <iais:value width="6">
                      <textarea disabled style="resize:none" name="reassignRemarks" cols="65" rows="6" title="content" MAXLENGTH="2000"><c:out value="${inspectionTaskPoolListDto.reassignRemarks}"/></textarea>
                    </iais:value>
                  </iais:row>
                  <iais:action>
                    <a href="#" class="back" id="Back" onclick="javascript:doInspectionReassignTaskConfirmBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionReassignTaskConfirmSubmit()">Submit</button>
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
<script type="text/javascript">
  function doInspectionReassignTaskConfirmBack() {
    inspectionSupAssignTaskConfirmSubmit('assign');
  }

  function doInspectionReassignTaskConfirmSubmit() {
    inspectionSupAssignTaskConfirmSubmit('success');
  }
  function inspectionSupAssignTaskConfirmSubmit(action){
    $("[name='InspectionSupSearchSwitchType']").val(action);
    var mainPoolForm = document.getElementById('mainConfirmForm');
    mainPoolForm.submit();
  }
</script>

