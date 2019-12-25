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
    <form class="form-horizontal" method="post" id="PreviewForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
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
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Message Type:</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                ${MsgTemplateDto.messageType}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Template Name:</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                ${MsgTemplateDto.templateName}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Delivery Mode:</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                ${MsgTemplateDto.deliveryMode}
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Effective Start Date:</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                <fmt:formatDate value="${MsgTemplateDto.effectiveFrom}"
                                                pattern="MM/dd/yyyy HH:mm:ss"/>
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-4">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Effective End Date:</span>
                            </div>
                            <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                <fmt:formatDate value="${MsgTemplateDto.effectiveTo}"
                                                pattern="MM/dd/yyyy HH:mm:ss"/>
                            </span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-md-12">
                                <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Message Content:</span>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-xs-12 col-sm-12 col-md-12">
                                <textarea cols="120" rows="40" class="textarea" id="htmlEditor" title="content"
                                          readonly>
                                    ${MsgTemplateDto.messageContent}
                                </textarea>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom: 40px;">
                            <div class="col-xs-12 col-md-12">
                                <div class="text-right"><a class="btn btn-primary" id="BackMain">Back</a></div>
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
<script>

    $("#BackMain").click(function () {
        $("#PreviewForm").submit();
    })

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
