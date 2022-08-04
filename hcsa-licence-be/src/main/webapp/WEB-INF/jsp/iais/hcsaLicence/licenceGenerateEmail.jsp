<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<input type="hidden" id="nextStage" name="nextStage" value="PROCEMAIL"/>
<link href="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT%>css/rightpanelstyle.css" rel="stylesheet"  >
<br/><br/>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Email to Applicant</h4>
    </strong>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <iais:section title="" id="process_email">
                <div class="form-group">
                    <iais:row>
                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
                        <div class="col-sm-9">
                            <p><input name="subject" type="text" id="subject"
                                      title="subject" readonly
                                      value="${appPremisesUpdateEmailDto.subject}"></p>
                        </div>
                    </iais:row>
                </div>
                <div class="form-group">
                    <iais:row>
                        <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
                        <div class="col-sm-9">
            <textarea name="mailContent" cols="108" rows="50"
                      id="htmlEditroArea"
                      title="content">${appPremisesUpdateEmailDto.mailContent}</textarea>
                        </div>
                    </iais:row>
                </div>
                <div class="form-group">
                    <div class="text ">
                        <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                        </p>
                    </div>
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" width="30%">Document</th>
                            <th scope="col" width="20%">File</th>
                            <th scope="col" width="10%">Size</th>
                            <th scope="col" width="20%">Submitted By</th>
                            <th scope="col" width="10%">Date Submitted</th>
                            <th scope="col" width="10%">Action</th>
                        </tr>
                        </thead>
                        <tbody id="tbodyFileListId">
                        <c:choose>
                            <c:when test="${empty applicationViewDto.appIntranetDocDtoList}">
                                <tr>
                                    <td colspan="6" align="left">
                                        <iais:message key="GENERAL_ACK018"
                                                      escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}"
                                           varStatus="status">
                                    <c:if test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT}">
                                        <tr>
                                            <td >
                                                <p>
                                                    Officer Document Upload
                                                </p>
                                            </td>
                                            <td >
                                                <p>
                                                    <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${interalFile.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(interalFile.docName, StandardCharsets.UTF_8.toString())}.${interalFile.docType}"
                                                       title="Download" class="downloadFile"><span id="${interalFile.fileRepoId}Down">trueDown</span></a>
                                                    <a href="javascript:void(0);" onclick="doVerifyFileGo('${interalFile.fileRepoId}')">
                                                        <c:out value="${interalFile.docName}.${interalFile.docType}"/>
                                                    </a>
                                                </p>
                                            </td>
                                            <td >
                                                <p><c:out value="${interalFile.docSize}"/></p>
                                            </td>
                                            <td >
                                                <p><c:out value="${interalFile.submitByName}"/></p>
                                            </td>
                                            <td >
                                                <p>${interalFile.submitDtString}</p>
                                            </td>
                                            <td >
                                                <button type="button" class="btn btn-secondary-del btn-sm"
                                                        onclick="javascript:deleteFile(this,'<iais:mask name="interalFileId"
                                                                                                        value="${interalFile.id}"/>');">
                                                    Delete</button>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                        </tbody>
                    </table>
                </div>
            </iais:section>

        </div>
    </div>
</div>

<div class="cd-panel cd-panel--from-right js-cd-panel-main">
    <div class="cd-panel__header">
        <h3>Preview</h3>
        <a  class="cd-panel__close js-cd-close">Close</a>
    </div>
    <div class="cd-panel__container">
        <div class="cd-panel__content quickBodyDiv">

        </div> <!-- cd-panel__content -->
    </div> <!-- cd-panel__container -->
</div>
<p class="text-right text-center-mobile">

    <iais:action >
        <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
        <button type="button" style="float:right" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
            Upload Document
        </button>
        <button type="button" onclick="javascript:doOpenEmailView();"   data-panel="main" class=" btn btn-secondary cd-btn js-cd-panel-trigger">
            Preview
        </button>
        <button class="btn btn-primary next" style="float:right" type="button" onclick="javascript:doSaveDraftEmail();">Save Draft</button>
        <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">
            Submit
        </button>
    </iais:action>
</p>

<script>
    function doSaveDraftEmail(){
        showWaiting();
        var subject = $('#subject').val();
        var mailContent = $('#htmlEditroArea').val();

        var data = {
            'subject':subject,
            'mailContent':mailContent
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/save-draft-email',
            'dataType':'json',
            'data':data,
            'type':'POST',
            'success':function (data) {

                // setValue();
            },
            'error':function () {

            }
        });
        dismissWaiting();
    }

    function doOpenEmailView() {
        var subject = $('#subject').val();
        var mailContent = $('#htmlEditroArea').val();

        var data = {
            'subject':subject,
            'mailContent':mailContent
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/email-view',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                $('.quickBodyDiv').html(data);


            },
            'error':function () {
            }
        });
    }

    $('#selectFile').change(function (event) {
        var maxFileSize = $('#configFileSize').val();
        console.log('maxFileSize : '+maxFileSize);
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectFile');
        console.log(error)
        if (error == "N"){
            $('#error_fileUploadError').html($("#fileMaxMBMessage").val());
            $("#selectFile").val('');
            $(".filename").html("");
        }else if(error == "Y"){
            if("Y" == validateAllFileSize()){
                callAjaxUploadFile();
                $('#error_fileUploadError').html('');
            }else{
                $('#error_fileUploadError').html($("#fileMaxMBMessage").val());
                $("#selectFile").val('');
            }
        }
    });

    function validateAllFileSize(){
        var maxSize = $('#configFileSize').val();
        var fileSize = (Math.floor(getAllFileSize() / 1024));
        console.log('all file size : ' + fileSize);
        if(fileSize >= maxSize){
            return "N";
            console.log('validate all fileSize flag : N');
        }
        console.log('validate all fileSize flag : Y');
        return "Y";
    }

    function getAllFileSize(){
        var allSize = 0;
        $('input[name="fileSize"]').each(function(){
            allSize += Math.round($(this).val());
        });
        var fileId= '#selectFile';
        var fileV = $(fileId).val();
        var file = $(fileId).get(0).files[0];
        console.log(fileV)
        console.log(file)
        console.log(file.size / (1024))
        var currentFileSize = 0;
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            currentFileSize = 0;
        }else{
            currentFileSize = Math.round(file.size / (1024)) + Math.round(allSize);
            console.log('test currentFileSize1 : ' + currentFileSize);
        }
        console.log('currentFileSize2 : ' + currentFileSize);
        console.log('all size : ' + allSize);
        return currentFileSize;
    }

    function writeMessageDeleteFile(deleteWriteMessageFileId){
        showWaiting();
        console.log(deleteWriteMessageFileId)
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/deleteWriteMessageFile",
            data: {deleteWriteMessageFileId:deleteWriteMessageFileId},
            dataType: "text",
            success: function (data) {
                $('#uploadFileBox').html(data);
                dismissWaiting();
            },
            error: function (msg) {
                alert("error");
            }
        });
    }

    function callAjaxUploadFile(){
        var formData = new FormData($("#mainForm")[0]);
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/uploadWriteMessageFile",
            data: formData,
            async:true,
            processData: false,
            contentType: false,
            dataType: "text",
            success: function (data) {
                $('#uploadFileBox').html(data);
            },
            error: function (msg) {
                alert("error");
            }
        });
    }
</script>


