<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <%--@elvariable id="internalFileMaxSize" type="java.lang.Integer"--%>
    <%--@elvariable id="internalFileAllowTypes" type="java.util.List<java.lang.String>"--%>
    <%--@elvariable id="appId" type="java.lang.String"--%>
    <input type="hidden" id="internalFileMaxSize" name="internalFileMaxSize" value="${internalFileMaxSize}" disabled>
    <input type="hidden" id="internalFileAllowTypes" name="internalFileAllowTypes" value="${internalFileAllowTypes}" disabled>
    <input type="hidden" id="fileMaxLengthMessage" name="fileMaxLengthMessage" value="<iais:message key="GENERAL_ERR0022"/>">
    <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />" disabled>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeUploadDoc()">&times;</button>
                <div class="modal-title" id="myModalLabel" style="font-size: 2rem;">Upload Internal Document</div>
            </div>
            <div class="modal-body">
                <form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data" action="" method="post">
                    <input type="hidden" name="curAppId" value="${MaskUtil.maskValue('uploadInternalDoc', appId)}" />
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Document Type <span style="color: red"> *</span></label>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <iais:select name="internalFileType" cssClass="fileTypeDrop" id="internalFileType" codeCategory="CATE_ID_DOC_TYPE_MOH"
                                             firstOption="Please Select" value=""/>
                                <small class="error" ><span id ="internalFileTypeErrorSpan" style="color: #D22727; font-size: 1.6rem"></span></small>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">File to Upload <span style="color: red"> *</span></label>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <div style="padding:0" class="col-md-4">
                                    <p><input id = "selectedFileShowText" name = "selectedFileShowText"  type="text"  value="Browse" readonly></p>
                                </div>
                                <div style="padding: 0 0 0 15px" class="col-md-8">
                                    <input id = "selectedFileShowTextName" name = "selectedFileShowTextName" type="text" readonly>
                                    <small class="error"><span id="internalFileSelectErrorSpan" style="color: #D22727; font-size: 1.6rem"></span></small>
                                </div>
                                <div hidden><input class="inputtext-required" id="internalFileSelectedFile" name="internalFileSelectedFile" type="file"></div>
                            </div>

                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelDoc" onclick="closeUploadDoc()">cancel</button>
                <button type="button" id="uploadInternalFileBtn" class="btn btn-primary" onclick="uploadInternalDoc()">upload</button>
            </div>
        </div>
    </div>
</div>