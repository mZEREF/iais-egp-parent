<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<div class="main-content" style="min-height: 73vh;">
    <form class="form-horizontal" method="post" id="PreviewForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Alert Notification Template View</h2>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Message Type</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 2rem;font-weight:600;">
                                ${MsgTemplateDto.messageType}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Template Name</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 2rem;font-weight:600;">
                                ${MsgTemplateDto.templateName}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Delivery Mode</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 2rem;font-weight:600;">
                                ${MsgTemplateDto.deliveryMode}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Effective Start Date</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 2rem;font-weight:600;">
                                <fmt:formatDate value="${MsgTemplateDto.effectiveFrom}"
                                                pattern="MM/dd/yyyy HH:mm:ss"/>
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Effective End Date</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 2rem;font-weight:600;">
                                <fmt:formatDate value="${MsgTemplateDto.effectiveTo}"
                                                pattern="MM/dd/yyyy HH:mm:ss"/>
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-12">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Message Content</span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-xs-12 col-sm-12 col-md-12">
                                <textarea  rows="40" class="textarea" id="htmlEditor" title="content">${MsgTemplateDto.messageContent}</textarea>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-xs-12 col-md-12">
                                <a href="#" id="BackMain"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>


<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<%@include file="initTinyMceCom.jsp" %>
<script>

    $("#BackMain").click(function () {
        $("#PreviewForm").submit();
    })
    $(window).on("load", function(){
        setTimeout("intiTinymce()", 1000);
    });

    function tinymce_getContentLength() {
        var count = removeHTMLTag(tinymce.get(tinymce.activeEditor.id).contentDocument.body.innerText).length;
        console.log(count);
        return count;
    }

    function intiTinymce() {
        showWaiting();
        tinymce.init({
            height:600,
            branding: false,
            readonly: 1,
            elementpath: false,
            selector: "#htmlEditor",  // change this value according to your HTML
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
            max_chars: 8000,
            setup: function (ed) {
                var content;
                var allowedKeys = [8,13, 46]; // backspace, delete and cursor keys
                ed.on('keydown', function (e) {
                    if (allowedKeys.indexOf(e.keyCode) != -1) return true;
                    // if (tinymce_getContentLength()>= this.settings.max_chars) {
                    //     e.preventDefault();
                    //     e.stopPropagation();
                    //     return false;
                    // }
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
                tinymce_updateCharCounter(editor, len + text.length);
            }
        });
        dismissWaiting();
    }

</script>
