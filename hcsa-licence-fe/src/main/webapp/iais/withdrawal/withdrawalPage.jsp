<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<div class="main-content">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="app_action_type" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="internet-content">
                        <div class="bg-title">
                            <div class="center-content">
                                <h2>Withdrawal Form</h2>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-12">You are withdrawing for:</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-12">
                                    <span>${applicationNo}</span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-12">Reason for Withdrawal:</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-7">
                                    <iais:select name="withdrawalReason" id="withdrawalReason"
                                                 options="withdrawalReasonList"
                                                 firstOption="Please select a withdrawal reason"
                                                 onchange="withdrawalReasons(this.value);"></iais:select>
                                    <span id="error_withdrawnReason" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                        <div id="reason" hidden>
                            <div class="row">
                                <div class="center-content">
                                    <label class="col-md-4">Any supporting remarks:</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="center-content">
                                    <div class="col-md-6">
                        <textarea name="withdrawnRemarks" cols="58" rows="15" id="htmlEditroArea"
                                  title="content"></textarea>
                                        <span id="error_withdrawnRemarks" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-12">File Upload for Appeal Reasons</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-7">
                                    <div class="file-upload-gp">
                                        <input id="withdrawFile" type="file" name="withdrawFile" style="display: none;"><a
                                            class="btn btn-file-upload btn-secondary">Upload</a>
                                        <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-2  col-md-offset-7">
                                    <div class="components">
                                        <a class="btn btn-primary">Cancel</a>
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
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp" %>
</div>
<script type="text/javascript">
    function withdrawalReasons(obj) {
        console.log(obj)
        if (obj == "Others") {
            $("#reason").show();
        } else {
            $("#reason").css("display", "none")
        }
    }

    function submit(action) {
        $("[name='app_action_type']").val(action);
        $("#mainForm").submit();
    }

    function doSubmit() {
        showWaiting();
        submit("withdrawalStep");
    }
</script>