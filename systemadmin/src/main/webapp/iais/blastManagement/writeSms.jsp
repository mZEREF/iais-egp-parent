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
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>New Blast Management List</h2>
                        </div>
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                        </ul>
                        <h3>New Mass Email</h3>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label" >Subject</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="subject" type="text" name="subject" value="${edit.getSubject()}">
                                    <span id="error_subject" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Content</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea cols="50" rows="10" name="messageContent" class="textarea" id="messageContent" title="content">${edit.msgContent}</textarea>
                                    <span id="error_addr" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                            <span id="error_msgContent" name="iaisErrorMsg" class="error-msg"></span>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Select Recipients</label>
                            <iais:value width="10">
                                <iais:select name="distribution" options="distribution" value=""></iais:select>
                            </iais:value>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-sm-11">
                                <div class="text-right">
                                    <a class="btn btn-primary" id="back" >Back</a>
                                    <a class="btn btn-primary" id="saveDis" >Save</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input hidden value="${id}" id="blastId" >
        <input hidden value="" id="action" name="action">
    </form>
    <%@include file="/include/validation.jsp"%>
</div>

<%@include file="/include/utils.jsp"%>

<script type="text/javascript">
    $('#saveDis').click(function(){
        $("#action").val("save")
        $("#mainForm").submit();
    });
    $('#back').click(function(){
        $("#action").val("back")
        $("#mainForm").submit();
    });

</script>