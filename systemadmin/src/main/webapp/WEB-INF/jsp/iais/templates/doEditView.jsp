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
    <form class="form-horizontal" method="post" id="TemplateEditForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_delivery_mode" value="">
        <input type="hidden" id="template_content_size" name="template_content_size" value="-1">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Edit Alert Notification Template</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="msgType">Message Type</label>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <label name="msgType" id="msgType" options="messageTypeSelect"  class="control-label"><iais:code code="${MsgTemplateDto.messageType}"></iais:code></label>
                                <span id="error_msgType" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Template Name" required="true"/>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <textarea style="width: 100%;" id="templateName" rows="10" cols="70" name="templateName" maxlength="500" >${MsgTemplateDto.templateName}</textarea>
                                <span id="error_templateName" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Delivery Mode</label>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <label name="deliveryMode" id="deliveryModeCode"
                                       options="deliveryModeSelect" class="control-label" ><iais:code code="${MsgTemplateDto.deliveryMode}"></iais:code></label>
                                <span id="error_deliveryMode" name="iaisErrorMsg"
                                      class="error-msg"></span>
                            </div>
                        </div>
                        </div>
                            <div class="form-group">
                                <iais:field value="To Recipients" required="true"/>
                                <div class="col-xs-5 col-sm-5 col-md-5">
                                    <iais:multipleSelect name="recipient" selectValue="${recipientString}" options="recipient" disabled="${('MTTP002' == MsgTemplateDto.messageType || 'MTTP005' == MsgTemplateDto.messageType) ? true : false}"></iais:multipleSelect>
                                    <span id="error_toRecipients" name="iaisErrorMsg"
                                          class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">CC Recipients</label>
                                <div class="col-xs-5 col-sm-5 col-md-5">
                                    <iais:multipleSelect name="ccrecipient" selectValue="${ccrecipientString}" options="recipient" disabled="${('MTTP002' == MsgTemplateDto.messageType || 'MTTP005' == MsgTemplateDto.messageType || 'DEMD002' == MsgTemplateDto.deliveryMode) ? true : false}"></iais:multipleSelect>
                                    <span id="error_ccRecipients" name="iaisErrorMsg"
                                          class="error-msg"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">BCC Recipients</label>
                                <div class="col-xs-5 col-sm-5 col-md-5">
                                    <iais:multipleSelect name="bccrecipient" selectValue="${bccrecipientString}" options="recipient" disabled="${('MTTP002' == MsgTemplateDto.messageType || 'MTTP005' == MsgTemplateDto.messageType || 'DEMD002' == MsgTemplateDto.deliveryMode) ? true : false}"></iais:multipleSelect>
                                    <span id="error_bccRecipients" name="iaisErrorMsg"
                                          class="error-msg"></span>
                                </div>
                            </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Process</label>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <label id="processEdit" type="text" name="processEdit" class="control-label"
                                       maxlength="25">
                                    <c:choose>
                                        <c:when test="${empty MsgTemplateDto.process}">
                                            N/A
                                        </c:when>
                                        <c:otherwise>
                                            <iais:code code="${MsgTemplateDto.process}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective Start Date" required="true"/>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <iais:datePicker id="esd" name="esd" dateVal="${MsgTemplateDto.effectiveFrom}"/>
                                <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Effective End Date" required="true"/>
                            <div class="col-xs-5 col-sm-5 col-md-5">
                                <iais:datePicker id="eed" name="eed"
                                                 dateVal="${MsgTemplateDto.effectiveTo}"/>
                                <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message Content" required="true"/>
                        </div>
                        <div class="form-group">
                            <c:choose>
                                <c:when test="${'DEMD002' eq MsgTemplateDto.deliveryMode}">
                                    <textarea rows="20" cols="140" name="messageContent" class="textarea" id="msgContentTxtArea">${MsgTemplateDto.messageContent}</textarea>
                                </c:when>
                                <c:otherwise>
                                    <textarea rows="40" name="messageContent" class="textarea" id="htmlEditor" title="content">${MsgTemplateDto.messageContent}</textarea>
                                </c:otherwise>
                            </c:choose>
                            <br>
                            <span id="error_messageContent" name="iaisErrorMsg" class="error-msg"></span>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-2 col-sm-2" style="padding-top: 30px;">
                                <a href="/system-admin-web/eservice/INTRANET/MohAlertNotificationTemplate"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div class="col-xs-10 col-sm-10 text-right">
                                <button type="button" class="btn btn-primary " onclick="doEdit('${MsgTemplateDto.id}','${MsgTemplateDto.deliveryMode}')">
                                    SUBMIT
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    <iais:confirm msg="${confirm_err_msg}" needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
    <iais:confirm msg="${confirm_err_msg}" needCancel="false" callBack="smscancel()" popupOrder="smssupport" ></iais:confirm>
    <input hidden name="mcId" id="mcId" value="">
    <input hidden name="deliveryMode" id="deliveryMode" value="">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>

<%@include file="initTinyMceCom.jsp" %>
<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<script>
    function submit(action) {
        clearDisabled();
        $("[name='crud_action_type']").val(action);
        $("#TemplateEditForm").submit();
    }

    function clearDisabled(){
        $('input[name="recipient"]').each(function(){
            $(this).attr("disabled",false);
        });
        $('input[name="ccrecipient"]').each(function(){
            $(this).attr("disabled",false);
        });
        $('input[name="bccrecipient"]').each(function(){
            $(this).attr("disabled",false);
        });
    }

    function cancel() {
        $('#support').modal('hide');
    }
    function smscancel() {
        $('#support').modal('hide');
        var length = $("#msgContentTxtArea").val().length
        var mcId = $("#mcId").val();
        var deliveryMode = $("#deliveryMode").val();
        $("#template_content_size").val(length);
        $("[name='crud_action_value']").val(mcId);
        $("[name='crud_action_delivery_mode']").val(deliveryMode);
        submit("edit");
    }

    function doEdit(mcId,deliveryMode) {
        var deliveryMode = deliveryMode;
        console.log("deliveryMode-->"+deliveryMode);
        if ('DEMD002' == deliveryMode) {
            var length = $("#msgContentTxtArea").val().length
            if(length > 160){
                $('#smssupport').modal('show');
            }else {
                $("#template_content_size").val(length);
                $("[name='crud_action_value']").val(mcId);
                $("[name='crud_action_delivery_mode']").val(deliveryMode);
                submit("edit");
            }
        }else{
            var length = tinymce_getContentLength();
            var length = $('.tox-statusbar__wordcount').text().split(" ")[0];
            if(length > 8000){
                $('#support').modal('show');
            }else {
                $("#template_content_size").val(length);
                $("[name='crud_action_value']").val(mcId);
                $("[name='crud_action_delivery_mode']").val(deliveryMode);
                submit("edit");
            }
        }
    }

    $(window).on("load", function(){
        setTimeout("intiTinymce()", 1000);
    });

    function intiTinymce() {
        showWaiting();
        tinymce.init({
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
            max_chars: 80000,
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

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }

</script>