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
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
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
                            <div class="col-xs-7 col-sm-7 col-md-7">
                                <textarea maxlength="4000" rows="30" name="messageContent" class="textarea" id="htmlEditroAreaWriteMessage" title="content">${edit.getMsgContent()}</textarea>
                                <label class="col-xs-4 col-md-4 control-label"></label>
                                <span id="error_msgContent" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Attachments</label>
                            <div class="document-upload-gp">
                                <div class="document-upload-list">
                                    <div class="file-upload-gp">
                                        <div class="fileNameDisplay">${edit.getDocName()}</div>
                                        <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
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
        <input hidden value="${id}" id="blastId" >
        <input hidden value="" id="action" name="action">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script type="text/javascript">
    $('#saveDis').click(function(){
        $("#action").val("save")
        $("#mainForm").submit();
    });
    $('#back').click(function(){
        $("#action").val("back");
        $("#mainForm").submit();

    });

    $('#selectedFile').change(function () {
        var file = $(this).val();
        var fileName = Utils.getFileName(file);
        $(".fileNameDisplay").text(fileName);
    });

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
            setup: function(ed) {
                ed.on('keydown', function(ed) {
                    var tinymax, tinylen;
                    tinymax = 4000;
                    var key = ed.keyCode;
                    tinylen =  tinyMCE.activeEditor.getContent().length;
                console.log(tinylen);
                    if (tinylen>tinymax && key != 8 && key != 46){
                        ed.stopPropagation();
                        ed.preventDefault();
                    }
                });
                },
            // init_instance_callback : function(editor) {
            //     editor.setContent($("#msgContent").val());
            // }
        });
    }
</script>