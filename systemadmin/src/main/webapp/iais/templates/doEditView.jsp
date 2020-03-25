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
<div class="main-content">
    <form class="form-horizontal" method="post" id="TemplateEditForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Edit Alert Notification Template</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="msgType">Message Type:</label>
                            <div class="col-xs-8 col-sm-6 col-md-4">
                                <iais:select name="msgType" id="msgType" options="messageTypeSelect" disabled="true"></iais:select>
                                <span id="error_msgTypeErr" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="templateName">Template Name</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <input id="templateName" type="text" value="${MsgTemplateDto.templateName}"
                                       name="templateName">
                                <span id="error_templateName" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Delivery Mode:</label>
                            <div class="col-xs-8 col-sm-6 col-md-4">
                                <iais:select name="deliveryMode" id="deliveryMode"
                                             options="deliveryModeSelect" disabled="true"></iais:select>
                                <span id="error_deliveryModeErr" name="iaisErrorMsg"
                                      class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <iais:datePicker id="esd" name="esd" dateVal="${MsgTemplateDto.effectiveFrom}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <iais:datePicker id="eed" name="eed"
                                                 dateVal="${MsgTemplateDto.effectiveTo}"></iais:datePicker>
                                <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-12 control-label">Message Content:</label>
                        </div>
                        <div class="form-group">
                            <textarea cols="120" rows="40" name="messageContent" class="textarea" id="htmlEditor"
                                      title="content">
                                ${MsgTemplateDto.messageContent}
                            </textarea>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-12 text-right">
                                <button type="button" class="btn btn-primary " onclick="doEdit('${MsgTemplateDto.id}')">
                                    SUBMIT
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp" %>
</div>


<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<script>

    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#TemplateEditForm").submit();
    }

    function doEdit(mcId) {
        $("[name='crud_action_value']").val(mcId);
        submit("edit");
    }

    $(function () {
        tinymce.init({
            branding: false,
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
        });
    });

</script>