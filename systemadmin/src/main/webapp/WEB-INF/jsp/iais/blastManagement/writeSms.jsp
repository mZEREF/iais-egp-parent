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
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                        </ul>
                        <h3>New Mass SMS</h3>
                        <div class="form-group">
                            <iais:field value="Header" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="subject" type="text" name="subject" value="${edit.getSubject()}">
                                    <span id="error_subject" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Select Recipients" required="true"/>
                            <iais:value width="10">
                                <iais:select name="distribution" firstOption="Please Select" options="distribution" value="${edit.getDistributionId()}"></iais:select>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Text" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea id="contentArea" style="width: 100%;height: 200px" name="messageContent" class="textarea" id="messageContent" title="content">${edit.msgContent}</textarea>
                                    <input id="contentCount" style="width: 100%;text-align: right; " value="Characters left: 160" readonly>
                                    <span id="error_msgContent" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label"></label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5" >
                                    <div style="display: flex;border:1px solid #000">
                                        <span style="font-size: 35px;text-align: center;align-self: center; width: 15%;" class="glyphicon glyphicon-info-sign"></span><label style="width: 85%;">"Please note that system counts a SMS is 160 characters long. If your texts are exceeded 160 characters, system will count as multiple SMSes"</label>
                                    </div>
                                </div>
                            </iais:value>

                        </div>

                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <a class="btn btn-primary" id="saveDis" >Send</a>
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
        $("#action").val("back")
        $("#mainForm").submit();
    });
    $(function(){
        var value = $("#contentArea").val();
        value = value.replace(/\n|\r/gi,"");
        var res = 160 - value.length;
        $("#contentCount").val("Characters left: "+res);
    });
    $("#contentArea").on("keyup",function () {
        var value = $(this).val();
        value = value.replace(/\n|\r/gi,"");
        var res = 160 - value.length;
        $("#contentCount").val("Characters left: "+res);
    })
</script>