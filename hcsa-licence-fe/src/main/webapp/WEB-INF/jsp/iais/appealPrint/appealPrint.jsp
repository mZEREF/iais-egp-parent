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

      </div>

      <div class="form-group">
        <div >
          <div>
            <div class="col-xs-12 col-md-10" >
              <label style="font-size: 25px;margin-top: 25px;margin-left: 1%" >File Upload For Appeal Reasons</label>
            </div>

            <div class="col-xs-12">
              <div class="document-upload-list" >
                <div class="file-upload-gp row" >
                  <div class="fileContent col-xs-5">
                    <input class="selectedFile"  id="selectedFile" name = "selectedFile"  onclick="fileClicked(event)"  onchange="javascript:doUserRecUploadConfirmFile(event)" type="file" style="display: none;" aria-label="selectedFile1" >
                    <a class="btn btn-file-upload btn-secondary" href="javascript:void(0);" onclick="doFileAddEvent()">Upload</a>
                  </div>
                </div>
                <span class="error-msg" name="iaisErrorMsg" id="error_selectedFileError"></span>
                <div class="col-xs-12" >
            <span  name="selectedFileShowId" id="selectedFileShowId">
            <c:forEach items="${pageShowFiles}" var="pageShowFileDto" varStatus="ind">
              <div id="${pageShowFileDto.fileMapId}">
                  <span  name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                  ${pageShowFileDto.fileName}
                <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('selectedFile',${pageShowFileDto.index});">
                Delete</button>  <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:reUploadFileFeAjax('selectedFile',${pageShowFileDto.index},'mainForm');">
              ReUpload</button>
              </div>

            </c:forEach>
            </span>
                </div>
              </div>
              <div class="col-xs-12">
                <span name="iaisErrorMsg" class="error-msg" id="error_file"></span>
                <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
              </div>

            </div>

          </div>
        </div>
      </div>

      <br>
      <br> <br> <br>
      <div >
        <div class="row">
          <div class="col-xs-12 col-sm-12" style="margin-bottom: 1%">
            <div class="text-right text-center-mobile">
              <div class="col-xs-12 col-sm-1">
                <p class="print text-right"><a href="#" id="print-review"> <em class="fa fa-print"></em>Print</a></p>
              </div>
              <a class="btn btn-secondary" href="javascript:void(0);" id="cancel">Cancel</a>
              <a class="btn btn-secondary" href="javascript:void(0);" id="save">Save</a>
              <a class="btn btn-primary" href="javascript:void(0);" id="submit">Submit</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<style>

</style>
<script type="text/javascript">
    $(document).ready(function () {
        doPrint();

    });
    var doPrint = function () {
        $('a').prop('disabled',true);
        window.print();
        window.close();
    }

</script>

</>
