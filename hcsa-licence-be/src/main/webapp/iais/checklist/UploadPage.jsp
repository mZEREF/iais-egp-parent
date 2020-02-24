<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>

<%@ page contentType="text/html; charset=UTF-8" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<style>
  .col-md-10 {
    width: 100%;
  }

  .btn.btn-primary {
    font-size: 12px;
  }

</style>
<div class="main-content">
  <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
    <%@ include file="/include/formHidden.jsp" %>
    <br><br>
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
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

      <div class="tab-content">
        <div class="document-upload-gp">
          <c:if test="${switchUploadPage eq 'checklistItem'}">
            <h2>Checklist Item Upload</h2>
          </c:if>
          <c:if test="${switchUploadPage eq 'regulation'}">
            <h2>Checklist Regulation Upload</h2>
          </c:if>
          <div class="document-upload-list">
            <div class="file-upload-gp">
              <div class="fileNameDisplay"></div>
              <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
            </div>
          </div>
        </div>

      </div>
    </div>


      <div class="row">
        <div class="col-xs-6 col-md-4">
          <p><a id = "docBack" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
        </div>
        <div class="col-xs-50 col-md-10 text-right">
          <div class="nav">

            <br><br><br>
            <div class="text-right text-center-mobile">
                <div class="button-group"><a class="btn btn-primary next" id="docNext">Next</a></div>


            </div>
          </div>


        </div>
      </div>



</div>
  </form>
</div>

<%@include file="/include/validation.jsp" %>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">

    $('#docBack').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

    $('#selectedFile').change(function () {
        var file = $(this).val();
        var fileName = Utils.getFileName(file);
        $(".fileNameDisplay").text(fileName);
    });

    $('#docNext').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doUpload");
    });
</script>