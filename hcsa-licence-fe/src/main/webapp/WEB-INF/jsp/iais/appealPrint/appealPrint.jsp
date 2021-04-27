<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-blank"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="container" >
  <form id="mainForm" method="post" style="margin-left: 3%" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div id="div_print">
      <div class="form-group">
        <div class="col-xs-12 col-md-10" style="margin-left: 2%">
          <label style="font-size: 25px">You are appealing for:</label>
        </div>

        <div  class="col-xs-12 col-md-10">
          <div class="col-xs-12 col-md-6" style="margin-left: 1%">
            <a type="text" name="appealingFor" id="appealingFor"  value="${appealNo}" onclick="link()" >${appealNo}</a>
            <span class="appMaskNo" style="display: none"><iais:mask name="appNo" value="${appealNo}"/></span>
            <input type="hidden" value="${id}" id="licenceId">
            <input type="hidden" value="${type}" id="parametertype">
            <span name="iaisErrorMsg" class="error-msg" id="error_submit"></span>
          </div>
        </div>
      </div>
      <div>
        <div class="form-group">
          <div class="col-xs-12 col-md-10" style="margin-left: 1%">
            <div class="col-xs-12 col-md-6" style="margin-bottom: 20px">
              <label style="font-size: 25px">Reason For Appeal<span class="mandatory"> *</span></label>
              <select id="reasonSelect" name="reasonSelect" style="margin-left: 2%">
                <option value="">Please Select</option>
                <c:forEach items="${selectOptionList}" var="selectOption">
                  <option value="${selectOption.value}" <c:if test="${appPremiseMiscDto.reason==selectOption.value}">selected="selected"</c:if> >${selectOption.text}</option>
                </c:forEach>
              </select>

              <div style="margin-top: 1%"> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>


              <div class="col-xs-12 col-md-10" id="othersReason" style="display: none" >
                <label style="font-size: 20px;margin-top: 1%">Others reason<span class="mandatory"> *</span></label>
                <input type="text" maxlength="100"   name="othersReason" value="${appPremiseMiscDto.otherReason}" >
                <span class="error-msg" name="iaisErrorMsg" id="error_otherReason"></span>
              </div>

              <div class="form-check-gp" id="selectHciNameAppeal" style="display: none" class="col-xs-12 col-md-6">
                <label style="font-size: 20px">Select HCI Name To Appeal</label>
                <c:forEach items="${hciNames}" var="hciName" >
                  <div >
                    <div class="form-check" >
                      <input class="form-check-input"  onclick="isCheck(this)" type="checkbox" <c:if test="${fn:length(hciNames)==1}">checked="checked" </c:if> name="selectHciName" aria-invalid="false" value="${hciName}">
                      <label class="form-check-label"><span class="check-square"></span>${hciName}</label>
                    </div>

                    <div class="col-xs-12 col-md-10" id="proposedHciName" style="display: none" >
                      <label style="font-size: 20px">Proposed  HCI Name<span class="mandatory"> *</span></label>
                      <input type="text" maxlength="100" name="proposedHciName" value="${appPremiseMiscDto.newHciName}">
                      <span class="error-msg" name="iaisErrorMsg" id="error_proposedHciName"></span>
                    </div>
                  </div>

                </c:forEach>
              </div>

            </div>

          </div>
        </div>
      </div>

      <div style="display: none;margin-top: 10px;margin-left: 1%" id="cgo" class="col-xs-12 col-md-9" >
        <%--     <a class="btn  btn-secondary" onclick="deletes()" style="margin-left: 20px;"  >delete</a>--%>
        <%@include file="../appeal/cgo.jsp"%>

      </div>
      <div class="col-xs-12 col-md-10" style="margin-left: 2%">

        <label style="font-size: 25px">Any supporting remarks<span class="mandatory"> *</span></label>

      </div >
      <div  class="col-xs-12 col-md-10" style="margin-left: 2%" >

        <textarea cols="120" style="font-size: 20px;width: 100%" rows="10" name="remarks" maxlength="300" >${appPremiseMiscDto.remarks}</textarea>

        <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

</body>
</html>
