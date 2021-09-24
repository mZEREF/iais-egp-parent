<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    if (StringUtil.isEmpty(action)) {
        action = (String) ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    }
    String flag = ParamUtil.getRequestString(request, "flag");
%>

<%--<%
    if (!StringUtil.isEmpty(flag) && "transfer".equals(flag)) {

    } else {
%>--%>

<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id="serviceFormsli" role="presentation">
        <a id="serviceForms" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Organisation<br>Info</a>
    </li>
    <li id="documentsli" role="presentation">
        <a id="documents" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary<br> Documents</a>
    </li>
    <li id="previewli" role="presentation">
        <a id="preview" aria-controls="previewTab" role="tab" data-toggle="tab">Preview & Submit</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#serviceInformationTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Organisation Info</a></div>
        <div class="swiper-slide"><a href="#documentsTab" aria-controls="tabApplication" role="tab" data-toggle="tab">Primary Documents</a></div>
        <div class="swiper-slide"><a href="#previewTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Preview & Submit</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>
<%--<%}%>--%>


<script type="text/javascript">

</script>