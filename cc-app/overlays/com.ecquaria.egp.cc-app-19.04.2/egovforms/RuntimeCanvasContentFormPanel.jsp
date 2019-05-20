<%@page import="ecq.crud.helper.View"%>
<%@page import="com.ecquaria.egp.core.forms.validation.FormValidationHelper"%>
<%@page import="com.ecquaria.egp.core.forms.instance.FormConstant"%>
<%@page import="com.ecquaria.egp.core.forms.validation.ValidationResult"%>
<%@page import="com.ecquaria.egp.core.forms.util.entity.FormHiddenField"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="sop.config.ConfigUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.ecquaria.egp.core.bat.FormHelper"%>
<%@page import="sop.forms.util.UnicodeUtil"%>
<%@page import="org.json.JSONWriter"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.Writer"%>
<%@page import="com.ecquaria.egp.core.forms.util.entity.FormButton"%>
<%@page import="ecq.commons.exception.BaseRuntimeException"%>
<%@page import="com.ecquaria.egp.core.forms.flows.Stage"%>
<%@page import="java.util.List"%>
<%@page import="sop.webflow.def5.BaseDef"%>
<%@page import="sop.util.WebDataUtil"%>
<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%@page import="sop.webflow.process5.util.FileUtil"%>
<%@page import="sop.webflow.rt.api.BaseRuntime"%>
<%@page import="sop.webflow.rt.api.BaseProcessClass"%>
<%@page import="ecq.commons.helper.ArrayHelper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="com.ecquaria.egp.core.forms.util.FormRuntimeUtil"%>
<%@page import="com.ecquaria.egp.core.forms.definition.FormDefinition"%>
<%@page import="com.ecquaria.egp.core.forms.instance.Form"%>
<%@page import="com.ecquaria.egp.core.forms.instance.FormInstanceStatusConstant"%>
<%@page import="com.ecquaria.egp.core.forms.generator.FormScriptGenerator"%>
<%@page import="com.ecquaria.egp.core.forms.servlet.RuntimeForm"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ecquaria.cloud.forms.service.ComponentRegistry"%>
<%@page import="com.ecquaria.egp.core.forms.util.ApplicationContextUtil"%>
<%@page import="java.io.StringReader"%>
<%@page import="com.ecquaria.egp.core.forms.util.WebContextUtil"%>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %>
<%
    String webroot = "/web";
    String servletroot = EgpcloudPortFactory.servletRoot;
    String webcontext = webroot+"/_statics/";
    String context = webroot+"/_statics/";
    Map<String, Object> buttons = (Map<String, Object>)request.getAttribute(FormRuntimeUtil.PARAM_FORM_BUTTONS);
    Map<String, Object> hiddens = (Map<String, Object>)request.getAttribute(FormRuntimeUtil.PARAM_FORM_HIDDENS);
%>
<%!private String getStageIdByStageName(Form form, String stageName) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    FormDefinition formDef = form.getFormDef();
    List flows = formDef.getFlows();
    String stageId = null;
    for(int i = 0; i < flows.size(); i++) {
        Stage stage = (Stage)flows.get(i);
        if(stage.getProperties().get("stageName").equals(stageName)) {
            stageId = stage.getId();
            break;
        }
    }
    if(stageId == null) {
        logger.debug("Stage name [" + stageName + "] is not exist.");
        return null;
    }
    return stageId;
}

    private String generateMetaJSON(Form form) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if (form == null){return null;}
        String jsonStr = null;
        try{
            Writer sw = new StringWriter();
            JSONWriter jw = new JSONWriter(sw);

            jw.object();

            jw.key("formId").value(form.getId());
            jw.key("refNumber").value(form.getRefNumber());

            jw.endObject();

            jsonStr = UnicodeUtil.escapeUnicode(sw.toString());
        } catch(Exception ex){
            logger.debug(ex.getMessage());
            throw new RuntimeException(ex);
        }
        return jsonStr;
    }

    private String simpleFormDefPath(String realFormDefPath){
        String[] strs = StringHelper.split(realFormDefPath, "/");
        int index = strs.length;
        String project = strs[index - 5];
        String processName = strs[index - 4];
        String version = strs[index - 3];
        String form = strs[index - 1];
        form = form.substring(0, form.lastIndexOf(".xml"));
        return project + "." + processName + "." + version + "." + form;
    }
%>
<%=WebContextUtil.JS_SOPFORM_CONTEXT%>
<%=WebContextUtil.JS_EGOVFORM_CONTEXT%>
<%=WebContextUtil.JS_EGOVFORM_WEBCONTEXT%>
<%
    String serverRender = WebDataUtil.getString(request, "serverRender");
    if(StringHelper.isEmpty(serverRender)) {
        serverRender = ConfigUtil.getString("egp.form.render.server", "false");
    }
    Boolean isServerRender = "true".equals(serverRender);

    String stageType = request.getParameter(RuntimeForm.PARAM_URL_STAGE_TYPE);
    stageType = stageType==null?(String)request.getAttribute(RuntimeForm.PARAM_URL_STAGE_TYPE):stageType;

    String _pfMode = WebDataUtil.getString(request, "pfMode");
    boolean pfMode = _pfMode==null||_pfMode.equals("false")?false:true;

    if(pfMode) isServerRender = false;

    String _editable = WebDataUtil.getString(request, "editable");
    boolean editable = _editable==null||_editable.equals("true")?true:false;

    Form egovForm = (Form)request.getAttribute(FormRuntimeUtil.ATTR_FORM);
    FormDefinition formDefinition = egovForm.getFormDef();

    String _singlePage = WebDataUtil.getString(request, "singlePage");
    boolean singlePage = _singlePage==null||_singlePage.equals("false")?false:true;

    editable = editable && egovForm.isEditable();

    String _formFormat = WebDataUtil.getString(request, "formFormat");

    String formFormat = "tab";
    String formTpye = egovForm.getFormInstance().getFormType();


    if (egovForm != null) {
        String status = egovForm.getInstanceStatus();
        if (FormInstanceStatusConstant.FORM_STATUS_NEW.equals(status)
                || FormInstanceStatusConstant.FORM_STATUS_DRAFT.equals(status)) {

            formFormat = StringUtils.defaultString(formDefinition.getStringProperty("submissionMode"), "tab");
        } else {
            formFormat = StringUtils.defaultString(formDefinition.getStringProperty("processingMode"), "tab");
        }


        if("external".equals(formTpye)) {
            stageType = getStageIdByStageName(egovForm, stageType);
        }
    }
    if(!StringHelper.isEmpty(_formFormat) && ArrayHelper.contains(new String[]{"tab", "wizard"}, _formFormat)){
        formFormat = _formFormat;
    }
    BaseProcessClass process = (BaseProcessClass)request.getAttribute("process");

    String formDefinitionPath = egovForm.getDefXmlFilePath();
    //request.setAttribute("FormDefinitionPath", formDefinitionPath);
    String path = EgpcloudPortFactory.servletRoot + "/egovforms/componentdynamictemplatescript";
    if(!StringHelper.isEmpty(formDefinitionPath)){
        String param = URLEncoder.encode(simpleFormDefPath(formDefinitionPath));
        path = path + "?FormDefinitionPath=" + param;
    }else{
        String param = URLEncoder.encode(egovForm.getMasterFormId());
        path = path + "?MasterFormId=" + param;
    }

    Object formType = request.getAttribute(FlowConstants.ATTR_COMPONENT_TYPE);
    boolean ignoreVisibility = false;
    List<String> globalError = FormHelper.getGlobalErrorMessages(request);
    Map<String, Map> fieldError = FormHelper.getFieldErrorMessages(request);

    //For embedForm
    String pageEditLinkText = WebDataUtil.getString(request, "pageEditLinkText");

    String pageIdForEdit = (String)session.getAttribute("pageIdForEdit");
    String pageEditSkipWizard = (String)session.getAttribute("pageEditSkipWizard");
    if(!StringHelper.isEmpty(pageIdForEdit)) {
        session.setAttribute("pageIdForEdit", null);
    }
    if(!StringHelper.isEmpty(pageEditSkipWizard)) {
        session.setAttribute("pageEditSkipWizard", null);
    }

    String editPageIndex = WebDataUtil.getString(request, FormHelper.KEY_EDIT_PAGE_INDEX);

    if(buttons == null && editable && !formFormat.equals("wizard") && !pfMode) {
        buttons = new LinkedHashMap<String, Object>();
        buttons.put(FormRuntimeUtil.BUTTON_SUBMIT_DEFAULT_LABEL,new FormButton(FormRuntimeUtil.BUTTON_SUBMIT_DEFAULT_LABEL, "submit"));
    }
%>
<%
    if(!pfMode){
%>
<div id="sop-mask-contenter" style="display: block;"><div id="sop-mask"></div><div class="sop-mask-progress-warting">Please Wait...</div></div>
<%
    }
%>
<script type="text/javascript">
    var servletRoot  = "<%=EgpcloudPortFactory.servletRoot%>";
    var sopFormTheme = "<%=EgpcloudPortFactory.servletRoot%>/_themes/egov/";
</script>
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/egov/css/egovforms/default.css"/>
<link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/egov/css/egovforms/sopform.flow.css"/>
<link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/egov/css/egovforms/sopform.form.css">
<link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/egov/css/egovforms/custom.css"/>
<style type="text/css">
    .control-set-alignment.control-label-span {
        width: 90%;
    }

    .control-set-alignment.control-input-span {
        width: 90%;
    }

    .control-caption-horizontal .control-set-alignment.control-label-span {
        width: 48%;
    }

    .control-caption-horizontal .control-set-alignment.control-input-span {
        width: 48%;
    }

    .repeatable-row.control-caption-horizontal .control-set-alignment.control-label-span {
        width: 90%;
    }

    .repeatable-row.control-caption-horizontal .control-set-alignment.control-input-span {
        width: 90%;
    }

    .control-set-alignment.control-input-span.control-button-wrapper {
        text-align: center;
        width: 90%;
    }
    <%if(!isServerRender) {%>
    div.normal-indicator {
        overflow: hidden;
    }
    <%}%>
</style>

<meta content="width=device-width, initial-scale=1.0" name="viewport">
<script type="text/javascript">
    function inIframe() {
        <%if(pfMode || singlePage) {%>
        return false
        <%}%>
        try {
            return window.self !== window.top;
        } catch (e) {
            return true;
        }
    }

    //if (inIframe()) {
    $(window).resize(adjustPage);
    //}
    var buttonsCount = <%=buttons == null ? 0 : buttons.size()%>;
    var wizardMode = <%=formFormat.equals("wizard")%>;
    var isServerRender = <%=isServerRender%>;
    var pagesCount = 0;
    var lessHeight = 27;
    var timerid;
    var __rrColumnWidth = 140;
    var __rrTwoColumnWidth = 420;

    function __respRR() {
        var outerWidth = $(window).width();
        $(".repeatable-row").each(function() {
            var control = Form.getControl($(this));
            var id = control.properties.id;
            var sectionCols = 1, pageCols = 1;
            var isTwoColumn = false;
            if(control.isInSection()) {
                sectionCols = control.getContainingSection().properties.cols;
            }
            pageCols = control.getContainingPage().properties.cols;
            containerWidth = outerWidth / pageCols / sectionCols;
            if(containerWidth < __rrTwoColumnWidth) {
                containerWidth = 9999;
                isTwoColumn = true;
            }
            var isRefresh = control.getGrid().columns("responseWidthCss", __rrColumnWidth, containerWidth);

            if(isRefresh == true) {
                window.validationManager._tempErrorForResponsive = $(".error_placements:not(:empty)", control.getElement());
                control.refresh();
                if(Form.isView) control.setViewData();
                else control.setRuntimeData();

                if (window.conditionManager) conditionManager.doRepeatableRowCondition(control);
                if (window.aclManager) aclManager.processACLDisabledChildren([control],Form.currentPage);


                window.validationManager._tempErrorForResponsive.each(function() {
                    var content = $(this).text();
                    if(content) {
                        $("#"+$(this).attr("id")).text(content).css("display", "inline");
                    }
                });
            }
            var $val = __respCSSItems[id];
            var len = $val.length;
            var val = __respCSSNodes[id]?__respCSSNodes[id]:[];
            if(isTwoColumn) {
                for (var i = 0; i < len; i++) {
                    var parent = $($val[i]).parent();
                    if (parent.is('.control-caption-horizontal')) {
                        parent.removeClass('control-caption-horizontal');
                        parent.addClass('control-caption-horz-disabled');
                    }
                }

                var $head = $('head');
                for (var i = 0; i < val.length; i++) {
                    if ($('#rrrcss-' + id + '-' + i).length <= 0) {
                        var valItem = val[i];
                        var $valItem = $(valItem);
                        $head.append($valItem);
                        $valItem.attr('id', 'rrrcss-' + id + '-' + i);
                    }
                }
            }else {
                for (var i = 0; i < len; i++) {
                    var parent = $($val[i]).parent();
                    if (parent.is('.control-caption-horz-disabled')) {
                        parent.removeClass('control-caption-horz-disabled');
                        parent.addClass('control-caption-horizontal')
                    }
                }

                for (var i = 0; i < val.length; i++) {
                    $('#rrrcss-' + id + '-' + i).remove();
                }
            }

        });
    }
    var __respCSSItems = {};
    var __respCSSNodes = {};
    function __addRespCSSItem(id, selector, extra) {
        if (extra) {
            var stylesheet = '<style type="text/css">\n';
            stylesheet += selector + ' table, ';
            stylesheet += selector + ' thead, ';
            stylesheet += selector + ' tbody, ';
            stylesheet += selector + ' th, ';
            stylesheet += selector + ' tr, ';
            stylesheet += selector + ' td {display: block}\n';
            stylesheet += selector + ' tr.table_header {position: absolute; top: -9999px; left: -9999px;}\n';
            // stylesheet += selector + ' tr {border: 1px solid #ccc;}\n';
            stylesheet += selector + ' td {border: none; border-bottom: 1px solid #eee; position: relative; padding-left: 50%; height: auto;}\n';
            stylesheet += selector + ' td:before {position: absolute; top: 6px; left: 6px; width: 45%; padding-right: 10px;}\n';
            stylesheet += selector + ' td:nth-of-type(1):before { content: "No."; }\n';
            for (var i = 1; i <= extra.length; i++) {
                stylesheet += selector + ' td:nth-of-type(' + (i + 1) + '):before { content: "' + extra[i - 1].replace(/"/g, '\\"') + '"; }\n';
            }
            stylesheet += selector + ' td:nth-of-type(' + (extra.length + 2) + '):before { content: ""; }\n';
            stylesheet += '</style>';

            var val = __respCSSNodes[id];
            if (val)
                val.push(stylesheet);
            else
                __respCSSNodes[id] = [stylesheet];
        } else {
            var val = __respCSSItems[id];
            if (val)
                val.push(selector);
            else
                __respCSSItems[id] = [selector];
        }
    }

    function __generateRespCSSItem() {
        Form.traverse(function(control){
            if(control.isRepeatableRow()) {
                var id = control.properties.id;
                var pageCols = control.getContainingPage().properties.cols;
                var names = [];
                if(control.children) {
                    $(control.children).each(function() {
                        names.push(this.properties.caption);
                    });
                }
                if(names.length) {
                    __addRespCSSItem(id, " .control.container-p-"+pageCols+" .control.container-rr-id-"+id, names);
                }
                __addRespCSSItem(id, " .control.container-p-"+pageCols+" .control-label-span");
            }
        });
        $(".repeatable-row").each(function() {
            var control = Form.getControl($(this));

        });
        window.isRespCSSItemReady = true;
    }

    function adjustPage() {
        if(window.Form && Form.currentMode == 'printerFriendly') return;
        if(window.Form && !window.isRespCSSItemReady) {
            __generateRespCSSItem();
        }

        try {
            if (window.parent && window.parent.EGP && window.parent.EGP.Common && window.parent.EGP.Common.getAutoHeightMode() == 1)
                return;
        } catch (e) {
        }

        var tmpHeight = lessHeight;
        if (wizardMode) {
            tmpHeight += 71;
        } else {
            if (buttonsCount > 0)
                tmpHeight += 47;
            if (pagesCount > 1)
                tmpHeight += 41;
        }
        var globalError = $('#global-error');
        if(globalError)
            tmpHeight += globalError.height();

        (timerid && clearTimeout(timerid));
        timerid = setTimeout(function () {
            $(".control-area").height($(window).height() - tmpHeight);
            __respRR();
        }, 10);
    }
</script>

<!-- Base -->
<script type="text/javascript" src="<%=webcontext%>js/egovforms/firebug/firebugx.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/base/1.1/Base.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jscache/jscache.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/navigation.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery.pagination.js"></script>

<!-- SOP Form -->
<%@ include file="/egovforms/validation-common-include.jsp" %>
<script type="text/javascript" src="<%=webroot%>/sds/js/form/EgovFormJSSupport.js"></script>

<%
    if(isServerRender) {
%>

<script type="text/javascript" src="<%=webcontext%>js/egovforms/EGOVForm.js"></script>
<script type="text/javascript" src="<%=webroot%>/javascripts/sop/sop.sopforms.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/scrollable.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.runtime.wizard.js"></script>
<%
    String	WEBURL_SOP									= "/sop";
    String 	SESSION_EXPIRED_PAGE						= WEBURL_SOP + "/system/sessionExpired.jsp";
%>
<script type="text/javascript">

    // script for server render

    function doFormSubmit(action, options){
        if(!options) {
            options = {
                needValidate: true
            };
        }

        if(options.formTarget && options.formTarget != "null") {
            $("form[name='formRender']").attr("target", options.formTarget);
        }

        $("input[name='" + jsonName + "']")[0].value = JSON.stringify(EGOVForm.getRuntimeData(formModel));
        $('[name="crud_action_type"]').val(action);
        if(options.needValidate) {
            EGOVForm.Validation.ajaxServerValidateCall();
            return false;
        }else {
            return true;
        }
    }
    //init
    $(function() {
        EGOVForm.Condition.init(formModel);
        EGOVForm.Validation.init(ValidationModel);

        for (var i = 0; i < EGOVForm.Condition._visibilitySequence.length; i++) {
            var controlId = EGOVForm.Condition._visibilitySequence[i];
            var control = EGOVForm.Condition._flattenedModel[controlId];
            EGOVForm.Condition.triggerConditionChange(EGOVForm.Condition.getJSONlSign(controlId, control.type));
        }

        var formTab = $('.tab-panel ul');
        formTab.sopformTabs();
        SOP.SOPForms.setupScrollable($(".tab-panel>.page-tab"), {next:'.arrow-nxt', prev:'.arrow-prev'});
        if(formTab.sopformTabs("length") <= 1) $('.tab-panel').hide();
        $(".action-content.action-button").show();

        <%if(formFormat.equals("wizard")) {%>
        formTab.sopformTabs('hide');
        $('#formPanel').children(':first').before('<div id="wizard-page-title"></div>');
        EGOVForm.Wizard.init();
        <%}%>
        SOP.Common.hideMask();
        $('#formPanel').show();
        $('.action-button').show();
    });

    var SESSION_EXPIRED_URL = '<%=SESSION_EXPIRED_PAGE%>';
    var jsonName = "<%=FormRuntimeUtil.PARAM_DATA%>";

</script>



<%
}else {
%>

<!-- JQuery -->
<script type="text/javascript" src="<%=webcontext%>js/egovforms/trimpath/template.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/jquery.form.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/jquery.field.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/jquery.timeentry.min.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/jquery.maskedinput-1.2.2.min.js"></script>

<!-- Sarissa -->
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sarissa/0.9.9.4/sarissa.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sarissa/0.9.9.4/sarissa_ieemu_xpath.js"></script>

<!-- Overlib -->
<script type="text/javascript" src="<%=webcontext%>js/egovforms/overlib/4.17/overlib.js"></script>

<!-- SOP Form -->
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.loader.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.formcontrol.design.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopforms.api.js"></script>



<script language="javascript">
    Form.Loader.setBase(sopFormWebContext + 'plugins/components/');
    EgovFormJSSupport.AJAX_VALIDATE_URL = "<%=request.getAttribute("FormCustomAjaxValidateURL")%>";
</script>

<script type="text/javascript" src="<%=path%>"></script>
<script type="text/javascript" src="<%=EgpcloudPortFactory.servletRoot%>/egovforms/componentscript"></script>
<%
    ApplicationContext appContext = ApplicationContextUtil.getApplicationContext();
    ComponentRegistry registry = (ComponentRegistry) appContext.getBean("egovComponentRegistry");
%>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/Map.js"></script>
<script type="text/javascript" src="<%=webroot%>/javascripts/sop/sop.sopforms.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/Parameter.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/Parameters.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/JSON_Modified.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.runtime.model.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.runtime.wizard.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.sortabledroppable.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.optionlookup.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.condition.manager.js"></script>
<script type="text/javascript" src="<%=EgpcloudPortFactory.servletRoot%>/_statics/js/egovforms/sopform.form.validation.manager.message.js.jsp"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.validation.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.generate.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.runtime.acl.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.developer.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.calc.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.aggregation.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.attachment.manager.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.grid.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.columns.js"></script>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/jquery-plugin/scrollable.js"></script>






<%
    // put this after all other css declarations.
    if(pfMode){
%>
<link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/egov/css/egovforms/print_overrides.css"/>
<script type="text/javascript" src="<%=webcontext%>js/egovforms/sopform.form.pf.manager.js"></script>
<%
    }
%>

<script>
    var isRuntime = true;
    var isSinglePage = <%=singlePage%>;
    var sopformsApi = new SOPFormsAPI();
    <%if(pfMode){%>
    var pfManager = new PrinterFriendlyManager();
    <%}%>
    var optionLookupManager = new OptionLookupManager();
    var conditionManager = new ConditionManager();
    var validationManager = new ValidationManager();
    var aclManager = new ACLManager('<%=StringHelper.escapeHtmlChars(stageType)%>');
    var generateManager = new GenerateManager();
    var developerManager = new DeveloperManager();
    var calculationManager = new CalculationManager();
    var aggregationManager = new AggregationManager();
    var attachmentManager = new AttachmentManager();

    var modelManager = new ModelManager();

    var data_json_str = "<%=new String(Base64.encodeBase64(egovForm.getData().getBytes("UTF-8")), "UTF-8") %>";
    eval("var data_json = " + Base64.decode(data_json_str));
    Form.instanceMeta = <%=generateMetaJSON(egovForm)%>;

</script>

<%
    }
%>



<%
    List<String> globalErrors = FormValidationHelper.getGlobalErrorMessages(process.currentCase, stageType);
%>


<%
    if((globalError!=null &&globalError.size()>0) || (globalErrors!=null &&globalErrors.size()>0) ){
%>
<div id="global-error">
    <%
        for(int i=0;i<globalError.size();i++){
            String gm = globalError.get(i);

            if(!StringHelper.isEmpty(gm)){
    %>
    <div class="error_placements"><%=gm%></div>
    <%
            }
        }
    %>

    <%
        for(int i=0;i<globalErrors.size();i++){
            String gm = globalErrors.get(i);

            if(!StringHelper.isEmpty(gm)){
    %>
    <div class="error_placements"><%=gm%></div>
    <%
            }
        }
    %>
</div>
<%
    }
%>

<div class="wrapper">
    <div class="form-inner-content <%=editable?"editableMode":""%>">
        <div></div>
        <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
        <%
            String action = "empty";
            long sopEngineTabRef = 0;
            if(!pfMode){
                action = process.runtime.continueURL();
                sopEngineTabRef = process.rtStatus.getTabRef();
            }else{
                String currentTabRef = (String)request.getAttribute("currentTabRef");
                if(!StringHelper.isEmpty(currentTabRef))
                    sopEngineTabRef = Long.parseLong(currentTabRef);
            }
            String appNo = (String)request.getAttribute("appNo");
            if(StringHelper.isEmpty(appNo)){
                appNo = "";
            }
            String caseid = (String)request.getAttribute("caseid");
            if(StringHelper.isEmpty(caseid)){
                caseid = "";
            }
        %>
        <form action="<%=action%>" name="formRender" method="post">
            <input type="hidden" name="sopEngineTabRef" value="<%=sopEngineTabRef%>">
            <input type="hidden" name="pageIndex">
            <input type="hidden" name="appNo" value="<%=appNo%>">
            <input type="hidden" name="caseid" value="<%=caseid%>">
            <%
                if(isServerRender) {
            %>
            <input type="hidden" name="formName" value="<%=process.rtStatus.getCurrentComponent().getName()%>">

            <%
            }else {
            %>
            <input type="hidden" name="formName" value="<%=StringHelper.escapeHtmlChars(stageType)%>">
            <%
                }
            %>
            <div class="canvasContent">
                <%
                    if(isServerRender){
                        //out.print(FormHtmlGenerator.getInstance().renderHtmlbyFormDefinition(egovForm));
                %>
                <div class="action-content action-button" style="display: none">
                    <% if(formFormat.equals("wizard")) { %>
                    <button id="leftButton" type="button" onclick="EGOVForm.Wizard.prev()">
                        Previous
                    </button>
                    <button id="rightButton" type="button" onclick="EGOVForm.Wizard.next()">
                        Next
                    </button>
                    <button id="submitButton" type="button" onclick="doFormSubmit('submit')" style="display:none;">
                        Submit
                    </button>
                    <% } %>
                    <%
                        if(!pfMode){
                            if(buttons!=null){
                                for(String key : buttons.keySet()){
                                    FormButton formButton = (FormButton)buttons.get(key);
                                    if(formButton != null){
                                        out.print(formButton.generateButton());
                                    }
                                }
                            }

                        }

                        if(hiddens!=null){
                            for(String key : hiddens.keySet()){
                                FormHiddenField formHidden = (FormHiddenField)hiddens.get(key);
                                if(formHidden != null){
                                    out.print(formHidden.generateHidden());
                                }
                            }
                        }
                    %>
                </div>

                <%}else {%>

                <!-- Place holder for tabbed interface. -->
                <div id="formPanel" class="sopform">
                    <div id="tab-panel" class="tab-panel" style="display: none">
                        <div class="arrow-prev">
                            <a href="javascript:void(0)">
                                <img title="arrow-prev" alt="arrow-prev" src="<%=webroot%>/_themes/egov/images/general/arrow-prev.png">
                            </a>
                        </div>
                        <div class="page-tab">
                            <ul>
                                <li style="display:none;"><a href="#demo"><span>demo</span></a></li>
                            </ul>
                        </div>
                        <div class="arrow-nxt">
                            <a href="javascript:void(0)">
                                <img title="arrow-prev" alt="arrow-prev" src="<%=webroot%>/_themes/egov/images/general/arrow-nxt.png">
                            </a>
                        </div>
                    </div>
                    <div id="demo" style="display:none;"></div>
                </div>
                <div class="action-content action-button" style="display:none">
                    <% if(formFormat.equals("wizard")) { %>
                    <button id="leftButton" type="button" onclick="wizard.prev()">
                        Previous
                    </button>
                    <button id="rightButton" type="button" onclick="wizard.next()">
                        Next
                    </button>
                    <button id="submitButton" type="button" onclick="doFormSubmit('submit')" style="display:none;">
                        Submit
                    </button>
                    <% } %>
                    <%
                        if(!pfMode){
                            if(buttons!=null){
                                for(String key : buttons.keySet()){
                                    FormButton formButton = (FormButton)buttons.get(key);
                                    if(formButton != null){
                                        out.print(formButton.generateButton());
                                    }
                                }
                            }
                        }

                        if(hiddens!=null){
                            for(String key : hiddens.keySet()){
                                FormHiddenField formHidden = (FormHiddenField)hiddens.get(key);
                                if(formHidden != null){
                                    out.print(formHidden.generateHidden());
                                }
                            }
                        }
                    %>
                    <div class="clear"></div>
                </div>
                <%}%>




                <input type="hidden" name="<%=FormRuntimeUtil.PARAM_MASTER_FORM_ID %>" value="<%=egovForm.getMasterFormId() %>" />
                <input type="hidden" name="<%=RuntimeForm.PARAM_JSON_STRING %>" value="" />
                <input type="hidden" name="<%=RuntimeForm.PARAM_SERVLET_ACTION %>" value="<%=RuntimeForm.ACTION_SUBMIT_FORM %>" />
                <%=View.outputCrudHiddenAction() %>
            </div> <!-- closes div id="center" -->
        </form>
    </div>


    <%if(isServerRender) {%>
    <script>
        $(function() {
            if(inIframe())
                adjustPage();
            try {
                if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
                    window.parent.EGP.Common.onEgovFormReady(document.documentElement.scrollHeight);
            } catch (e) {}
            try {
                if (parent && parent.setFormDlgTitle)
                    parent.setFormDlgTitle('<%= formDefinition.getName() %>');
            } catch (e) {}
        });

    </script>
    <%}else { %>
    <script>
        Form.$ROOT_PANEL = $('#formPanel');
        var wizard = null;
        Form.isView = <%=!editable %>;
        Form.defaultEmptyValue = "<%=FormRuntimeUtil.getDefaultEmptyValue() %>";
        function doFormSubmit(action, options){
            if(wizard){
                var page = Form.PAGES[wizard.currentPageIndex];
                var events = $(document).data("events");
                if (events && events['preEGovFormWizardPageChange'+page.properties.id])
                    $(document).trigger('preEGovFormWizardPageChange'+page.properties.id);
                if(!wizard.canNext){
                    return;
                }
            }
            if(!options) {
                options = {
                    needValidate: true
                };
            }
            var validationSuccess = true;
            if(options.needValidate) {
                validationSuccess = validationManager.doValidationTrigger();
            }
            if(options.formTarget && options.formTarget != "null") {
                $("form[name='formRender']").attr("target", options.formTarget);
            }
            if(options.func && typeof eval(options.func) == "function") {

                if(!eval(options.func+"()")) {
                    return false;
                }
            }
            if(validationSuccess){
                //$("input[name='Action']")[0].value = "Submit";
                var n = "<%=FormRuntimeUtil.PARAM_DATA %>";
                if(!Form.isView){
                    $("input[name='" + n + "']")[0].value = modelManager.generateJSONStringForSubmission();
                }
                $('[name="crud_action_type"]').val(action);
                $('[name="pageIndex"]').val(Form.getPageIndexByPageId(Form.currentPage.properties.id));
                $('form[name="formRender"]').submit();
            }

        }

        jQuery(function($jq) {
            var $panel = $('#formPanel');
            <%
                if(!pfMode){
            %>
            SOP.Common.showMask();
            $('.action-button').hide();
            <%
                }
            %>
            $panel.hide();

            <%
            if(pfMode){
                %>
            Form.currentMode = 'printerFriendly';
            <%
        }
        else{
            %>
            Form.currentMode = 'runtime';
            <%
        }
        %>

            Form.createRuntimePageTabs();

            <%
                if(formFormat.equals("tab"))
                {
                %>
            function doRuntimeRenderForTabsInterface(){
                <%
                if (ignoreVisibility) {
                    out.println(FormScriptGenerator.RUNTIME_TABBED.generate(formDefinition, null));
                } else {
                    out.println(FormScriptGenerator.RUNTIME_TABBED.generate(formDefinition, stageType));
                }
                %>
                if(Form.PAGES.length == 0){
                    $("#buttons_for_preview_div").html("According to ACL of the form, You do not have access to any pages during this stage of the form.");
                }
                else{
                    if (Form.PAGES.length > 1)
                        $("#tab-panel").show();
                    Form.PAGES[0].refresh();
                    Form.Runtime.pageRendered(0);
                    //if (inIframe()) {
                    pagesCount = Form.PAGES.length;
                    Form.TABS.sopformTabs({
                        show: function() {
                            adjustPage();
                        }
                    });
                    //}
                    Form.Runtime.populateForm(0);
                    Form.TABS.sopformTabs('select', 0);//reusing addPageTab <- selects the page last added
                }
            }

            doRuntimeRenderForTabsInterface();
            <%
            }
            else if(formFormat.equals("wizard"))
            {
            %>
            Form.TABS.sopformTabs('hide');
            $('#formPanel').children(':first').before('<div id="wizard-page-title"></div>');
            function doRuntimeRenderForWizardInterface(){
                <%
                if (ignoreVisibility) {
                    out.println(FormScriptGenerator.RUNTIME_WIZARD.generate(formDefinition, null));
                } else {
                    out.println(FormScriptGenerator.RUNTIME_WIZARD.generate(formDefinition, stageType));
                }
                %>

                if(Form.PAGES.length == 0){
                    $("#buttons_for_preview_div").html("According to ACL of the form, You do not have access to any pages during this stage of the form.");
                } else {
                    Form.PAGES[0].refresh();
                    Form.Runtime.pageRendered(0);
                    //if (inIframe()) {
                    pagesCount = Form.PAGES.length;
                    Form.TABS.sopformTabs({
                        show: function() {
                            adjustPage();
                        }
                    });
                    //}
                    Form.Runtime.populateForm(0);
                    Form.TABS.sopformTabs('select', 0);//reusing addPageTab <- selects the page last added
                }
            }

            doRuntimeRenderForWizardInterface();
            wizard = new Wizard(Form);
            wizard.doSetup();
            <%
            }
        %>

            try {
                if (parent && parent.setFormDlgTitle)
                    parent.setFormDlgTitle('<%= formDefinition.getName() %>');
            } catch (e) {
                // in case cross-domain js call
            }

            <%
		            if(process!=null && process.currentCase!=null){
		            	Map<String, ValidationResult>  map = FormValidationHelper.getValidationResultMap(process.currentCase,stageType);
		            	Logger logger = LoggerFactory.getLogger(this.getClass());
		            	logger.debug("===================="+map.size());
		            	if(map!=null){
		            		for(String controlID: map.keySet()){
		            			ValidationResult result = map.get(controlID);
		            			String success = result.isSuccess()?"true":"false";
		    					String message = result.getMessage();
		    					int rowIndex = result.getRowIndex();
		    					%>
            //controlID, success, message, rowIndex
            validationManager.doShowErrorMessage(new ValidationResult("<%=result.getControlID() %>", "<%=success %>", "<%=message %>", <%=rowIndex %>));
            <%
        }
    }
}
%>
            <%
                if(!pfMode){
            %>
            conditionManager.executeAllCondition(-1);
            <%  
                }
            %>

            // Execute ACLs
            aclManager.processACLDisabled();

            validationManager.doMandatoryIndicators();

            // Execute Aggregation
            //aggregationManager.doAllAggregation();

            <%if(!StringHelper.isEmpty(pageIdForEdit)) {%>
            Form.showEditPage(<%=pageIdForEdit%>, <%=pageEditSkipWizard%>);
            <%
            }
            
            if(!StringHelper.isEmpty(editPageIndex)) {%>
            Form.showEditPageByIndex(<%=editPageIndex%>);
            <%
            }
            
            if(!StringHelper.isEmpty(pageEditLinkText)) {%>
            Form.setupPageEditLink("<%=pageEditLinkText%>");
            <%
            }
            
            if(!pfMode){
            	if(fieldError != null && fieldError.size() > 0) {
            		for(Iterator iterator = fieldError.keySet().iterator(); iterator.hasNext();) {
            			String fieldName = (String)iterator.next();
            			Map msgMap = fieldError.get(fieldName);
            			for(Iterator msgKeys = msgMap.keySet().iterator(); msgKeys.hasNext();) {
            				Integer rowIndex = (Integer)msgKeys.next();
            				String message = (String)msgMap.get(rowIndex);
            				%>
            validationManager.addFieldErrorMessage('<%=fieldName%>', '<%=message%>', '<%=rowIndex%>');
            <%
        }
    }
%>
            validationManager.switchToFirstErrorPage();
            validationManager.doErrorIndicators();
            <%	
            	}
            %>
            setTimeout(function(){
                <%
                if(singlePage){
                %>
                Form.setupSinglePage();
                $panel.show();
                <%
                }else {
                %>
                $panel.show();
                $('.action-button').show();
                <%
                }
                %>

                try {
                    if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common){
                        var tabPanel = $("#tab-panel");
                        window.parent.$(window.parent.document).bind("eGovFormReady", function() {
                            var _arrWidth = 0;
                            $(".page-tab li", tabPanel).each(function() {
                                _arrWidth += $(this).outerWidth();
                            });
                            if ($(".page-tab", tabPanel).width() > _arrWidth) {
                                tabPanel.addClass("hide-arrow");
                            }


                        });
                        window.parent.EGP.Common.onEgovFormReady(document.documentElement.scrollHeight);
                    }

                } catch (e) {}
                SOP.SOPForms.setupScrollable($(Form.TABS).parent(), {next:'.arrow-nxt', prev:'.arrow-prev'});
                SOP.Common.hideMask();
                setTimeout(function(){
                    $(document).trigger('eGovFormReady');
                }, 1000);
            }, 1000);

            var timerId;
            window.onDocumentChange = function() {
                (timerId && clearTimeout(timerId));
                timerId = setTimeout(function(){
                    if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
                        window.parent.EGP.Common.onFrameDocumentChange(document.documentElement.scrollHeight);
                }, 1);
            }
            <%  
            }
            %>
        });
    </script>

    <%
        if(pfMode){
    %>
    <!-- This is just a place holder for the page divs that we will put in. -->
    <div id="custom_content_div" class="content"></div>
    <script language="JavaScript">
        $(document).ready(function() {
            pfManager.setupPFPage();

        });
        function runAfterPfModeReady() {
            try {
                if (window !== window.parent && window.parent && window.top.__eGovFormPfModeReady)
                    window.parent.__eGovFormPfModeReady(document.documentElement.scrollHeight);

                if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
                    window.parent.EGP.Common.onEgovFormReady(document.documentElement.scrollHeight);
            } catch (e) {
                console.log(e);
            }
            setTimeout(function(){
                $(document).trigger('eGovFormReady');
            }, 1000);
        }
    </script>
    <%
            }
        }
    %>
</div>