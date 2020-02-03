<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/30/2019
  Time: 4:02 PM
  To change this template use File | Settings | File Templates.
--%>


<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
</style>

  <div class="main-content">
    <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <input type="hidden" name="crud_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">


      <br><br><br>
      <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
      <div class="row">

        <br><br><br>
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="documentsTab" role="tabpanel">
                <div class="document-info-list">
                  <ul>
                    <li>
                      <p>The maximum file size for each upload is 10MB. </p>
                    </li>
                    <li>
                      <p>Acceptable file formats are XLSX. </p>
                    </li>
                  </ul>
                </div>

                <div class="document-upload-gp">
                  <c:if test="${switchUploadPage eq 'checklistItem'}">
                    <h2>Checklist Item Upload</h2>
                  </c:if>
                  <c:if test="${switchUploadPage eq 'regulation'}">
                    <h2>Checklist Regulation Upload</h2>
                  </c:if>
                  <div class="document-upload-list">
                    <div class="file-upload-gp">
                      <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                    </div>
                  </div>
                </div>

                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6">
                      <p><a id = "docBack" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                      <div class="button-group"><a class="btn btn-primary next" id="docNext">Next</a></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </form>
  </div>

<%@include file="/include/validation.jsp"%>
<script>
    $('#docBack').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

    $('#selectedFile').change(function () {
        var file = $(this).val();
    });

    $('#docNext').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doUpload");
    });
</script>