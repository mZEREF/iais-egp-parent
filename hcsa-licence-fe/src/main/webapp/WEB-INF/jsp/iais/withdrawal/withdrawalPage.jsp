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
        <input type="hidden" name="withdraw_app_list" value="">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="internet-content">
                    <div class="center-content">
                        <h2>You are withdrawing for</h2>
                        <div class="row">
                            <div class="col-lg-8 col-xs-12">
                                <div class="withdraw-content-box">
                                    <div class="withdraw-info-gp">
                                        <div class="withdraw-info-row">
                                            <div class="withdraw-info">
                                                <p>${withdrawAppNo}</p>
                                            </div>
                                            <div class="withdraw-delete">
                                                <!--   <p><a href="#"><i class="fa fa-trash-o"></i>Delete</a></p> -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-xs-12">
                                <div class="withdraw-addmore gradient-light-grey">
                                    <a href="#newappModal" data-toggle="modal" data-target="#newappModal"><h4><em
                                            class="fa fa-plus-circle"></em> Add more applications</h4></a> <%--NOSONAR--%>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="newappModal" class="modal fade" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Select application for withdrawal</h4>
                                    </div>
                                    <div id="withdrawPagDiv"></div>
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody id="withdrawBodyDiv"></tbody>
                                    </table>
                                    <div class="modal-footer">
                                        <a class="btn btn-primary withdraw-next" href="#">Done</a>
                                    </div>
                                </div>

                            </div>
                        </div>
                    <div class="row">
                        <div class="center-content">
                            <h3>Reason for Withdrawal<span style="color: red"> *</span></h3>
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
                    <div class="row">
                        <div class="center-content">
                            <div class="document-upload-gp">
                                <div class="document-upload-list">
                                    <h3>File upload for Withdrawal Reasons</h3>
                                    <div class="file-upload-gp">
                                        <input id="withdrawFile" type="file" style="display: none;" name = "selectedFile"
                                               aria-label="selectedFile"><a class="btn btn-file-upload btn-secondary"
                                                                             href="#">Upload</a>
                                        <div id="delFile" style="margin-top: 13px;color: #1F92FF;"
                                             hidden="hidden">
                                            <strong id="fileName">${file_upload_withdraw}</strong>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteWdFile()"><em
                                                    class="fa fa-times"></em></button>
                                        </div>
                                    </div>
                                    <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="row" style="padding-bottom: 2%">
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
        let fileName = $("#withdrawFile").val();
        let pos = fileName.lastIndexOf("\\");
        $("#fileName").html(fileName.substring(pos + 1));
    });

    function deleteWdFile() {
        // document.getElementById("withdrawFile").files[0] = null;
        let wdfile = $("#selectedFile");
        wdfile.val("");
        $("#delFile").attr("hidden", "hidden");
    }

    // $(".delete-withdraw").click(function () {
    //     console.log("delete withdraw app");
    //     $(this).parent().parent().parent().parent().remove();
    // });
    function deleteWithdraw(it){
        console.log("delete withdraw app");
        $(it).parent().parent().parent().parent().parent().remove();
    }

    $(".withdraw-next").click(function () {
        $("input[name='checkboxSample']:checked").each(function () {
            let appNoList = [];
            let withdrawContent$ = $(".withdraw-content-box");
            withdrawContent$.find(".withdraw-info p").each(function (){
                appNoList.push($(this).text())
            });
            let appNo = $(this).parent().parent().parent().find(".withdraw-info").find("p").text();
            if ($.inArray(appNo,appNoList) == -1){
                withdrawContent$.last().parent().append('<div class="withdraw-content-box">\n' +
                    '                                    <div class="withdraw-info-gp">\n' +
                    '                                        <div class="withdraw-info-row">\n' +
                    '                                            <div class="withdraw-info">\n' +
                    '                                                <p>'+appNo+'</p>\n' +
                    '                                            </div>\n' +
                    '                                            <div class="withdraw-delete">\n' +
                    '                                                <p ><a onclick="deleteWithdraw(this)"><i class="fa fa-trash-o"></i>Delete</a></p>\n' +
                    '                                            </div>\n' +
                    '                                        </div>\n' +
                    '                                    </div>\n' +
                    '                                </div>')
            }
        });
        $('#newappModal').modal('hide');
    });

    function doSubmit() {
        showWaiting();
        let appNoList = "";
        let withdrawContent$ = $(".withdraw-content-box");
        withdrawContent$.find(".withdraw-info p").each(function (){
            appNoList = appNoList + $(this).text() + "#";
        });
        $("[name='withdraw_app_list']").val(appNoList);

        submit("withdrawalStep");
    }
</script>