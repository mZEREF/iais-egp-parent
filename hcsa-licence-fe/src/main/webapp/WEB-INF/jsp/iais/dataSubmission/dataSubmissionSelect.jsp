<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String webrootDS=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="New Data Submission" />

<%@ include file="assistedReproduction/common/arHeader.jsp" %>
<c:forEach items="${iais_Login_User_Info_Attr.privileges}" var="privilege">
  <c:if test="${privilege.id == 'HALP_MOH_DS_ART_CRE'}">
    <c:set var="dataSubARTPrivilege" value="1"/>
  </c:if>
  <c:if test="${privilege.id == 'HALP_MOH_DS_DP_CRE'}">
    <c:set var="dataSubDPPrivilege" value="1"/>
  </c:if>
  <c:if test="${privilege.id == 'HALP_MOH_DS_TOP_CRE'}">
    <c:set var="dataSubTOPPrivilege" value="1"/>
  </c:if>
  <c:if test="${privilege.id == 'HALP_MOH_DS_LDT_CRE'}">
    <c:set var="dataSubLDTPrivilege" value="1"/>
  </c:if>
  <c:if test="${privilege.id == 'HALP_MOH_DS_VSS_CRE'}">
    <c:set var="dataSubVSSPrivilege" value="1"/>
  </c:if>
</c:forEach>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <div class="row">
    <div class="container">
      <div class="col-xs-12">
        <h3>Please select the submission you wish to make</h3>
      </div>
    </div>
  </div>
  <div class="row" >
        <div class="container">
          <c:if test="${dataSubARTPrivilege == 1}">
            <div class="col-xs-12" style="height: 60px">
              <div class="col-xs-8 col-md-4 text-left">
                <a class="btn btn-primary next premiseId" onclick="submit('AR')"  href="javascript:void(0);">Assisted Reproduction</a>
              </div>
            </div>
          </c:if>
          <c:if test="${dataSubDPPrivilege == 1}">
              <div class="col-xs-12" style="height: 60px">
              <div class="col-xs-8 col-md-4 text-left">
                <a class="btn btn-primary next premiseId" onclick="submit('DP')"  href="javascript:void(0);">Drug Practices</a>
              </div>
            </div>
          </c:if>
          <c:if test="${dataSubLDTPrivilege == 1}">
          <div class="col-xs-12"  style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('LDT')"  href="javascript:void(0);">Laboratory Developed Test</a>
            </div>
          </div>
          </c:if>
           <c:if test="${dataSubTOPPrivilege == 1}">
          <div class="col-xs-12" style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('TP')"  href="javascript:void(0);">Termination of Pregnancy</a>
            </div>
          </div>
           </c:if>
          <c:if test="${dataSubVSSPrivilege == 1}">
          <div class="col-xs-12"  style="height: 60px">
            <div class="col-xs-8 col-md-4 text-left">
              <a class="btn btn-primary next premiseId" onclick="submit('VS')"  href="javascript:void(0);">Voluntary Sterilisation</a>
            </div>
          </div>
         </c:if>
        </div>
  </div>
  <div class="row">
    <div class="container">
        <div class="col-xs-12">
        <div class="application-tab-footer">
          <div class="col-xs-12 col-sm-4 col-md-2 text-left">
            <a style="padding-left: 5px;" class="back" id="backBtn" href="/main-web">
              <em class="fa fa-angle-left">&nbsp;</em> Back
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>


</form>
<link rel="stylesheet" href="<%=webrootDS%>css/data_submission.css">