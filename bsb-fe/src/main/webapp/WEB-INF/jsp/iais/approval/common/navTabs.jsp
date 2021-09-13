<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    if(StringUtil.isEmpty(action)){
        action = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    }
    String flag = ParamUtil.getRequestString(request,"flag");
%>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approvalapplication.js"></script>
<input type="hidden" name="crud_action_type_form_page" value="">
<input type="hidden" id = "controlLi" value="<%=action%>">
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id="PrepareFormsli" role="presentation" class="${empty coMap.information ? 'incomplete' : 'complete'}">
        <a id="PrepareForms" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Approval Info</a>
    </li>
    <li id="PrepareDocumentsli" role="presentation" class="${empty coMap.document ? 'incomplete' : 'complete'}">
        <a id = "PrepareDocuments" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary Documents</a>
    </li>
    <li id="PreparePreviewli" role="presentation" class="${empty coMap.previewli ? 'incomplete' : 'complete'}">
        <a id="PreparePreview" aria-controls="previewTab" role="tab" data-toggle="tab">Preview & Submit</a>
    </li>
</ul>