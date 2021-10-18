<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/23
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <input type="hidden" id="maxFileSize" name="maxFileSize" value="${inspSetMaskValueDto.sqlFileSize}">
      <input type="hidden" id="fileId" name="fileId" value="">
      <input type="hidden" id="remarksError" name="remarksError" value="">
      <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body >
                  <iais:section title="" id = "upload_Rectification">
                    <div class="table-responsive">
                      <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                          <th scope="col" >Vehicle Number</th>
                          <th scope="col" >NC Clause</th>
                          <th scope="col" >Checklist Question</th>
                          <th scope="col" >Findings/Non-Compliances</th>
                          <th scope="col" >Action Required</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                          <td><c:out value="${inspecUserRecUploadDto.vehicleNo}"/></td>
                          <td><c:out value="${inspecUserRecUploadDto.checkClause}"/></td>
                          <td><c:out value="${inspecUserRecUploadDto.checkQuestion}"/></td>
                          <td><c:out value="${inspecUserRecUploadDto.appPremisesPreInspectionNcItemDto.ncs}"/></td>
                          <td><c:out value="${inspecUserRecUploadDto.appPremisesPreInspectionNcItemDto.beRemarks}"/></td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="bg-title">
                      <h3 style="margin-bottom: 0px;border-bottom: 0px solid;padding-bottom: 0px;">Attachments</h3>
                    </div>
                    <c:if test="${inspecUserRecUploadDto != null}">
                      <div>
                        <div class="row">
                          <label class="col-xs-0 col-md-6 control-label">Choose a file to attach<span style="color: red"> *</span></label>
                        </div>
                        <ul>
                          <li><span>The maximum file size for each upload is <c:out value="${inspSetMaskValueDto.sqlFileSize}"/>MB.</span></li>
                          <li><span>Acceptable file formats are <c:out value="${recFileTypeHint}"/>.</span></li>
                        </ul>
                        <div class="form-group">
                          <div class="col-sm-7 col-md-8 col-xs-10">
                            <input class="selectedFile premDoc" id="recFileUpload" name = "selectedFile" type="file" onchange="javascript:doUserRecUploadConfirmFile(this.value)" style="display: none;" aria-label="selectedFile1"/>
                            <button type="button" class="btn btn-file-upload btn-secondary" onclick="javascript:doUserRecUploadConfirmUpload()">Upload</button>
                            <c:if test="${empty inspecUserRecUploadDto.fileRepoDtos}">
                              &nbsp;<label>No files selected.</label>
                            </c:if>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_recFile"></span>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-4">
                          <label style="margin-bottom: 0px;border-bottom: 0px solid;font-size: 1.6rem;padding-bottom: 10px;">Current File Attachments</label>
                        </div>
                      </div>
                      <iais:row>
                        <iais:value width="300">
                          <c:if test="${inspecUserRecUploadDto.fileRepoDtos != null}">
                            <c:forEach items="${inspecUserRecUploadDto.fileRepoDtos}" var="recFile" varStatus="status">
                              <iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${recFile.id}" docName="${recFile.realFileName}"/>
                              &nbsp;<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:doUserRecUploadConfirmDel('<iais:mask name="fileId" value="${recFile.id}"/>')">Delete</button>
                              <br><br>
                            </c:forEach>
                          </c:if>
                        </iais:value>
                      </iais:row>
                    </c:if>
                    <div class="row">
                      <div class="col-md-4">
                        <h3 style="margin-bottom: 0px;border-bottom: 0px solid;">Remarks<span style="color: red"> *</span></h3>
                      </div>
                    </div>
                    <iais:row>
                      <div class="col-sm-7 col-md-8 col-xs-10 table-responsive">
                        <textarea id="uploadRemarks" name="uploadRemarks" cols="70" rows="7" maxlength="300"><c:out value="${inspecUserRecUploadDto.uploadRemarks}"></c:out></textarea>
                        <br><span class="error-msg" name="iaisErrorMsg" id="error_remarks"></span>
                        <span class="error-msg"><c:out value="${inspecUserRecUploadDto.remarksMsg}"></c:out></span>
                      </div>
                    </iais:row>
                    <iais:action >
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmSave()">Save</button>
                      <span style="float:right">&nbsp;</span>
                      <button class="btn btn-secondary" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmCancel()">Cancel</button>
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
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function userRecUploadConfirmSubmit(action){
        $("[name='inspecUserRecUploadType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doUserRecUploadConfirmCancel() {
        showWaiting();
        $("#actionValue").val('cancel');
        userRecUploadConfirmSubmit('review');
    }

    function doUserRecUploadConfirmSave() {
        showWaiting();
        $("#actionValue").val('success');
        userRecUploadConfirmSubmit('success');
    }

    function doUserRecUploadConfirmDel(fileId) {
        showWaiting();
        let remarksError = $("#error_remarks").text();
        $("#remarksError").val(remarksError);
        $("#actionValue").val('delete');
        $("#fileId").val(fileId);
        userRecUploadConfirmSubmit('delete');
    }

    function doUserRecUploadConfirmUpload() {
        $("#recFileUpload").trigger('click');
        dismissWaiting();
    }

    function doUserRecUploadConfirmFile(value) {
        showWaiting();
        var maxFileSize = $("#maxFileSize").val();
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, "recFileUpload");
        if (error == "N"){
            $('#error_recFile').html($("#fileMaxMBMessage").val());
            dismissWaiting();
        } else {
            let remarksError = $("#error_remarks").text();
            $("#remarksError").val(remarksError);
            $("#actionValue").val('add');
            userRecUploadConfirmSubmit('add');
        }
    }

    function validateUploadSizeMaxOrEmpty(maxSize,selectedFileId) {
        var fileId= '#'+selectedFileId;
        var fileV = $( fileId).val();
        var file = $(fileId).get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            return "N";
        }
        return "Y";
    }

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }
</script>
