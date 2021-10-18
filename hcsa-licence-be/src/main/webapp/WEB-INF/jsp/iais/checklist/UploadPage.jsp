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

  .btn.btn-danger{
    font-size: 1rem;
    font-weight: 700;
    background: white;
    border: 1px solid #333333;
    color: black;
    padding: 5px 15px;
    text-transform: uppercase;
    border-radius: 30px;
  }

</style>
<div class="main-content">
  <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <br>

    <br><br>
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="document-info-list">
          <ul>
            <li>
              <p><iais:message key="GENERAL_ERR0043" replaceName="configNum" propertiesKey="iais.system.upload.file.limit"></iais:message></p>
            </li>
            <li>
              <p>Acceptable file formats are XLSX. </p>
            </li>
          </ul>
        </div>

      <div class="tab-content">
        <div class="document-upload-gp">
            <h2>${switchUploadPage}</h2>
          <div class="document-upload-list">
            <div class="error-msg"></div>

            <div class="file-upload-gp">
                <p>
                  <span class="fileNameDisplay">
                  </span>
                  <span class="existFile delBtn hidden">
                      <button type="button" class="btn btn-danger">
                        Delete
                      </button>
                  </span>
                </p>
              <input id="selectedFile" name="selectedFile" type="file" style="display: none;"  onclick="fileClicked(event)" onchange="fileChanged(event)" aria-label="selectedFile1">

              <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
            </div>

          </div>
        </div>

      </div>

      <div>
        <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-6 col-md-4">
        <a href="#" id = "docBack" class="back"><em class="fa fa-angle-left"></em> Back</a>
      </div>
      <div class="col-xs-6 col-md-8 text-right">
        <div class="text-center-mobile">
          <div class="button-group"><a class="btn btn-primary next" id="docNext">Next</a></div>
        </div>
      </div>
    </div>
  </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
    $('#docBack').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

    $('#selectedFile').change(function () {
        let maxFileSize = 100;
        let error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectedFile');
        if (error == "N"){
          $(this).closest('.document-upload-list').find('.error-msg').html($("#fileMaxMBMessage").val());
          $(".fileNameDisplay").text("");
          $(this).val(null);
        }else{
          $(this).closest('.document-upload-list').find('.error-msg').html('');
        }

        let file = $(this).val();
        let fileName = Utils.getFileName(file);
        $(".fileNameDisplay").text(fileName);
    })

    $('#docNext').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doUpload");
    });

    $('#selectedFile').change(function () {
        $('.existFile').removeClass("hidden")
    });

    $('.existFile').click(function () {
      let selectFile = $('#selectedFile');
      selectFile.val(null);
      $('.existFile').addClass("hidden");
      $(".fileNameDisplay").text(null);
    });
</script>