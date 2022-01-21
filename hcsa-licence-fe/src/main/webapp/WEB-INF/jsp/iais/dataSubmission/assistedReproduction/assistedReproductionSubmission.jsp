<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String continueURL = "";
  if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
    continueURL = process.runtime.continueURL();
  }
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=continueURL%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3 style="font-size: 36px">New Data Submission</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <%@include file="section/arSubmissionSection.jsp" %>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>
<%--
There is an existing draft for Patient Information. Please either resume from draft or continue to submit for a different patient.(DS_MSG001)
There is an existing draft for Cycle Stage. Please either resume from draft or continue to submit for a different patient or cycle stage.(DS_MSG002)
There is an existing draft for Donor Sample. Please either resume from draft or continue to submit a different donor sample.(DS_MSG008)
--%>
<c:if test="${hasDraft && arSuperDataSubmissionDto.submissionType eq 'AR_TP001'}">
  <iais:confirm msg="DS_MSG001" callBack="submit('confirm', 'resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
                cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                cancelBtnDesc="Continue" cancelFunc="submit('confirm', 'delete')" needFungDuoJi="false"/>
</c:if>
<c:if test="${hasDraft && arSuperDataSubmissionDto.submissionType eq 'AR_TP002'}">
  <iais:confirm msg="DS_MSG002" callBack="submit('confirm', 'resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
                cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                cancelBtnDesc="Continue" cancelFunc="submit('confirm', 'delete');" needFungDuoJi="false"/>
</c:if>
<c:if test="${hasDraft && arSuperDataSubmissionDto.submissionType eq 'AR_TP003'}">
  <iais:confirm msg="DS_MSG008" callBack="submit('confirm', 'resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
                cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                cancelBtnDesc="Continue" cancelFunc="submit('confirm', 'delete');" needFungDuoJi="false"/>
</c:if>
