<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="org.springframework.web.multipart.MultipartHttpServletRequest" %>
<%@ page import="sop.servlet.webflow.HttpHandler" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp" %>
<div class="main-content">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="app_action_type" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="internet-content">
                    <div class="row">
                        <div class="center-content">
                            <iais:field value="You are withdrawing for" required="false" style="font-size:3rem"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="center-content">
                            <div class="col-md-12">
                                <span style="font-size:2rem">${withdrawAppNo}</span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="center-content">
                            <iais:field value="Reason for Withdrawal" required="true" style="font-size:3rem"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="center-content">
                            <div class="col-md-7">
                                <iais:select name="withdrawalReason" id="withdrawalReason"
                                             options="withdrawalReasonList"
                                             onchange="withdrawalReasons(this.value);"
                                             value="${withdrawDtoView.withdrawnReason}"/>
                                <span id="error_withdrawnReason" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>

                    <div id="reason"
                         <c:if test="${withdrawDtoView.withdrawnReason != 'WDR005' || withdrawDtoView.withdrawnReason== null}">hidden</c:if>>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-4" style="font-size:3rem">Remarks</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-6">
                                    <div class="file-upload-gp">
                                            <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                                      title="content"
                                                      maxlength="500">${withdrawDtoView.withdrawnRemarks}</textarea>
                                    </div>
                                    <span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="center-content">
                        <iais:field value="File Upload for Withdrawal  Reasons" required="false"
                                    style="font-size:3rem"/>
                    </div>
                </div>
                <div class="row">
                    <div class="center-content">
                        <div class="col-md-4">
                            <div class="file-upload-gp">
                                <input id="withdrawFile" type="file" name="selectedFile" style="display: none;"><a
                                    class="btn btn-file-upload btn-secondary">Upload</a>
                            </div>
                            <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                        <div id="delFile" class="col-md-9" style="margin-top: 13px;color: #1F92FF;margin-left: -20%"
                             hidden="hidden">
                            <strong id="fileName">${file_upload_withdraw}</strong>
                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteWdFile()"><em
                                    class="fa fa-times"></em></button>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="center-content">
                        <div class="col-md-2 col-md-offset-8" style="text-align: right">
                            <div class="components">
                                <a class="btn btn-secondary"
                                   href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp">Cancel</a>
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="components">
                                <a class="btn btn-primary" onclick="doSubmit()">Submit</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>
<script type="text/javascript">
    $(function () {
        var reason = $("#withdrawalReason").val();
        if (reason == "WDR005") {
            $("#reason").show();
        }
        if (${file_upload_withdraw != null && file_upload_withdraw != ""}) {
            $("#delFile").removeAttr("hidden");
        }
    });

    function withdrawalReasons(obj) {
        console.log(obj);
        if (obj == "WDR005") {
            $("#reason").show();
        } else {
            $("#reason").css("display", "none")
        }
    }

    function submit(action) {
        $("[name='app_action_type']").val(action);
        $("#mainForm").submit();
    }

    $("#withdrawFile").change(function () {
        $("#delFile").removeAttr("hidden");
        var fileName = $("#withdrawFile").val();
        var pos = fileName.lastIndexOf("\\");
        $("#fileName").html(fileName.substring(pos + 1));
    });

    function deleteWdFile() {
        // document.getElementById("withdrawFile").files[0] = null;
        wdfile = $("#withdrawFile");
        wdfile.after(wdfile.clone().val(""));
        wdfile.remove();
        $("#delFile").attr("hidden", "hidden");
    }

    function doSubmit() {
        showWaiting();
        submit("withdrawalStep");
    }
</script>