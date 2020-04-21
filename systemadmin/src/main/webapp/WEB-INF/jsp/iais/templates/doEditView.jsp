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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                            <label class="col-xs-12 col-md-4 control-label" for="msgType">Message Type</label>

                            <div class="col-xs-8 col-sm-6 col-md-4">
                                <iais:select name="msgType" id="msgType" options="messageTypeSelect" disabled="true"></iais:select>
                                <span id="error_msgTypeErr" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="templateName">Template Name</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <input id="templateName" type="text" value="${MsgTemplateDto.templateName}"
                                       name="templateName" maxlength="500">
                                <span id="error_templateName" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Delivery Mode</label>
                            <div class="col-xs-8 col-sm-6 col-md-4">
                                <iais:select name="deliveryMode" id="deliveryMode"
                                             options="deliveryModeSelect" disabled="true"></iais:select>
                                <span id="error_deliveryModeErr" name="iaisErrorMsg"
                                      class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <iais:datePicker id="esd" name="esd" dateVal="${MsgTemplateDto.effectiveFrom}"/>
                                <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date</label>
                            <div class="col-xs-8 col-sm-6 col-md-6">
                                <iais:datePicker id="eed" name="eed"
                                                 dateVal="${MsgTemplateDto.effectiveTo}"></iais:datePicker>
                                <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-12 control-label">Message Content</label>
                        </div>
                        <div class="form-group">
                            <textarea cols="120" rows="40" name="messageContent" class="textarea" id="htmlEditor"
                                      title="content">
                                ${MsgTemplateDto.messageContent}
                            </textarea>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-2 col-sm-2" style="padding-top: 30px;">
                                <a href="/system-admin-web/eservice/INTRANET/MohAlertNotificationTemplate"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div class="col-xs-10 col-sm-10 text-right">
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
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
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
                'noneditable','code ax_wordlimit','autoresize'
            ],
            toolbar: 'undo redo | formatselect | ' +
            ' bold italic backcolor | alignleft aligncenter ' +
            ' alignright alignjustify | bullist numlist outdent indent |' +
            ' removeformat | help | code',
            ax_wordlimit_num: 10,
            ax_wordlimit_callback: function(editor,txt,num){

            }
        });
    });

    tinymce.PluginManager.add('ax_wordlimit', function(editor) {
        var global$1 = tinymce.util.Tools.resolve('tinymce.util.Tools');
        var global$2 = tinymce.util.Tools.resolve('tinymce.util.Delay');
        var ax_wordlimit_type = editor.getParam('ax_wordlimit_type', 'letter' );
        var ax_wordlimit_num = editor.getParam('ax_wordlimit_num', false );
        var ax_wordlimit_delay = editor.getParam('ax_wordlimit_delay', 500 );
        var ax_wordlimit_callback = editor.getParam('ax_wordlimit_callback', function(){});
        var ax_wordlimit_event = editor.getParam('ax_wordlimit_event', 'SetContent Undo Redo Keyup' );
        var onsign=1;
        //size
        var sumLetter = function(){
            var html = editor.getContent();
            var re1 = new RegExp("<.+?>","g");
            var txt = html.replace(re1,'');
            txt = txt.replace(/\n/g,'');
            txt = txt.replace(/&nbsp;/g,' ');
            var num=txt.length;
            return {txt:txt,num:num}
        };
        var onAct = function(){
            if(onsign){
                onsign=0;
                switch(ax_wordlimit_type){
                    case 'letter':
                    default:
                        var res = sumLetter();
                }

                if( res.num > ax_wordlimit_num ){
                    ax_wordlimit_callback(editor, res.txt, ax_wordlimit_num);
                }
                setTimeout(function(){onsign=1}, ax_wordlimit_delay);
            }

        };
        var setup = function(){
            if( ax_wordlimit_num>0 ){
                global$2.setEditorTimeout(editor, function(){
                    var doth = editor.on(ax_wordlimit_event, onAct);
                }, 300);
            }
        };

        setup();
    });

</script>