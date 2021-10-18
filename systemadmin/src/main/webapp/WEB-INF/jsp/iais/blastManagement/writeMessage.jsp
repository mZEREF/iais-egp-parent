<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<style>
    .btn.btn-sm {
        font-size: .775rem;
        font-weight: 500;
        padding: 6px 10px;
        text-transform: uppercase;
        border-radius: 30px;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <input type="hidden" id="configFileSize" value="${configFileSize}"/>
        <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                            <li class="tracker-item ">Select Recipients to send</li>
                        </ul>
                        <h3>New Mass Email</h3>
                        <div class="form-group">
                            <iais:field value="Subject" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="subject" type="text" name="subject" maxlength="255" value="${edit.getSubject()}">
                                    <span id="error_subject" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Content" required="true"/>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <textarea maxlength="4000" rows="30" name="messageContent" class="textarea" id="htmlEditroAreaWriteMessage" title="content">${edit.getMsgContent()}</textarea>
                                <span id="error_content" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>


                        <div class="form-group">
                            <iais:field value="Attachments" required="false"/>
                            <div class="document-upload-gp col-xs-8 col-md-8">
                                <div class="document-upload-list">
                                    <div class="file-upload-gp">
                                        <input id="selectFile" name="selectFile" type="file" class="iptFile" style="display: none;">
                                        <div id="uploadFileBox" class="file-upload-gp">
                                            <c:forEach var="attachmentDto" items="${blastManagementDto.attachmentDtos}"
                                                       varStatus="status">
                                                <p class="fileList">${attachmentDto.docName}&emsp;<button type="button" class="btn btn-secondary btn-sm" onclick="writeMessageDeleteFile('${attachmentDto.id}')">Delete</button><input hidden name='fileSize' value='${attachmentDto.docSize}'/></p>
                                            </c:forEach>
                                        </div>
                                        <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                        <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
                                        <ul class="upload-enclosure-ul">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <a class="btn btn-primary" id="saveDis" >Continue</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <iais:confirm msg='ESB_ERR004'  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <input hidden value="${id}" id="blastId" >
        <input hidden value="" id="action" name="action">
        <input hidden value="0" id="fileChange" name="fileChange">
        <input hidden value="0" id="filecount" name="filecount">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script type="text/javascript">
    $('#saveDis').click(function(){
        var length = tinymce_getContentLength();
        console.log(length)
        if(length > 4000){
            $('#support').modal('show');
        }else{
            $("#action").val("save")
            $("#mainForm").submit();
        }

    });

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }
    function cancel() {
        $('#support').modal('hide');
    }
    $('#back').click(function(){
        $("#action").val("back");
        $("#mainForm").submit();

    });

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

    $(window).on("load", function(){
        $("#htmlEditroAreaWriteMessage").hide();
        setTimeout("intiTinymce()", 1000);
    });

    function intiTinymce() {
        console.log('init1')
        $("#htmlEditroAreaWriteMessage").show();
        tinymce.init({
            selector: "#htmlEditroAreaWriteMessage",  // change this value according to your HTML
            menubar: 'file edit view insert format tools',
            plugins: ['print preview fullpage',
                'advlist autolink lists link image charmap print preview anchor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table paste code help wordcount',
                'noneditable'
            ],
            toolbar: 'undo redo | formatselect | ' +
                ' bold italic backcolor | alignleft aligncenter ' +
                ' alignright alignjustify | bullist numlist outdent indent |' +
                ' removeformat | help',
            max_chars: 4000,
            setup: function (ed) {
                var content;
                var allowedKeys = [8,13, 46]; // backspace, delete and cursor keys
                ed.on('keydown', function (e) {
                    // if (allowedKeys.indexOf(e.keyCode) != -1) return true;
                    // if (tinymce_getContentLength() + 1 >= this.settings.max_chars) {
                    //     e.preventDefault();
                    //     e.stopPropagation();
                    //     return false;
                    // }
                    return true;
                });
                ed.on('keyup', function (e) {
                    tinymce_updateCharCounter(this, tinymce_getContentLength());
                });
            },
            init_instance_callback: function () { // initialize counter div
                $('#' + this.id).prev().append('<div class="char_count" style="text-align:right"></div>');
                tinymce_updateCharCounter(this, tinymce_getContentLength());
            },
            paste_preprocess: function (plugin, args) {
                var editor = tinymce.get(tinymce.activeEditor.id);
                var len = editor.contentDocument.body.innerText.length;
                var text = $(args.content).text();

                // if (tinymce_getContentLength() > editor.settings.max_chars) {
                //     $('#support').modal('show');
                //     args.content = '';
                // } else {
                    tinymce_updateCharCounter(editor, len + text.length);
                // }
            }
        });

    }

    function tinymce_updateCharCounter(el, len) {
        $('#' + el.id).prev().find('.char_count').text(len + '/' + el.settings.max_chars);
    }
    function removeHTMLTag(str) {
        str = str.replace(/<\/?[^>]*>/g, '');
        str = str.replace(/[\r\n]/g,"");
        return str;
    }

    function tinymce_getContentLength() {
        var count = removeHTMLTag(tinymce.get(tinymce.activeEditor.id).contentDocument.body.innerText).length;
        console.log(count)
        return count;
    }

    function suffixName(file_name){
        var result = /\.[^\.]+/.exec(file_name);
        return result;
    }
</script>