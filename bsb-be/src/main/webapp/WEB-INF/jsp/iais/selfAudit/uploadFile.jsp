<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="modal-title" id="myModalLabel" style="font-size:2rem;">Upload Internal Document</div>
            </div>
            <div class="modal-body">
                <form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data"
                      action="<%=process.runtime.continueURL()%>" method="post">
                    <input type="hidden" id="uploadFile" name="uploadFile" value="">
<%--                    <input type="hidden" name="taskId" value="<iais:mask name="taskId" value="${taskId}"/>">--%>
                    <input type="hidden" name="sopEngineTabRef"
                           value="<%=process.rtStatus.getTabRef()%>">
<%--                    action="" method="post">--%>
                      <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Document</label>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <p><input type="text" maxlength="50" id="fileRemark" name="fileRemark" value="${fileRemarkString}"></p>
                                <br /> <small class="error"></small>
                                <span id="error_fileRemark" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                          <div class="form-group">
                              <label class="col-xs-12 col-md-4 control-label">Upload your files <span style="color: red"> *</span></label>
                              <div class="col-xs-8 col-sm-8 col-md-8">
                                  <p><input class = "inputtext-required" id = "selectedFile" name = "selectedFile" type="file"/></p>
                                  <br /> <small class="error"></small>
                                  <span id="error_selectedFile" name="iaisErrorMsg" class="error-msg"></span>
                              </div>
                          </div>

                      </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeUploadDoc()">cancel</button>
                <button type="button" id="uploadFileButton" class="btn btn-primary" onclick="uploadInternalDoc()">upload</button>
            </div>
        </div>
    </div>
</div>
