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
        <input type="hidden" id="configFileSize" value="${configFileSize}"/>
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

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <c:forEach items="${addWithdrawnDtoList}" var="wdList">
                                    <div class="withdraw-content-box">
                                        <div class="withdraw-info-gp">
                                            <div class="withdraw-info-row">
                                                <div class="withdraw-info">
                                                    <p>${wdList.applicationNo}</p>
                                                </div>
                                                <div class="withdraw-delete">
                                                    <p ><a onclick="deleteWithdraw(this)"><em class="fa fa-trash-o"></em>Delete</a></p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <c:if test="${isDoView != 'Y'}">
                                <div class="col-lg-4 col-xs-12">
                                    <div class="withdraw-addmore gradient-light-grey">
                                        <a href="#newappModal" data-toggle="modal" data-target="#newappModal"><h4><em
                                                class="fa fa-plus-circle"></em> Add more applications</h4></a> <%--NOSONAR--%>
                                    </div>
                                </div>
                            </c:if>
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
                                        <a class="btn btn-primary withdraw-next" href="javascript:void(0);">Done</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <div class="center-content">
                        <h3>Reason for Withdrawal  <c:if test="${isDoView != 'Y'}"> <span style="color: #ff0000"> *</span> </c:if> </h3>
                        <div class="row">
                            <div class="col-md-7">
                                <c:choose>
                                    <c:when test="${isDoView eq 'Y'}">
                                        <iais:select name="withdrawalReason" id="withdrawalReason"
                                                     options="withdrawalReasonList"
                                                     onchange="withdrawalReasons(this.value);"
                                                     value="${withdrawDtoView.withdrawnReason}"  disabled="true" />
                                    </c:when>
                                    <c:otherwise>
                                        <iais:select name="withdrawalReason" id="withdrawalReason"
                                                     options="withdrawalReasonList"
                                                     onchange="withdrawalReasons(this.value);"
                                                     value="${withdrawDtoView.withdrawnReason}"  />
                                    </c:otherwise>
                                </c:choose>
                                <span id="error_withdrawnReason" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                    <div id="reason"
                         <c:if test="${withdrawDtoView.withdrawnReason != 'WDR005' || withdrawDtoView.withdrawnReason== null}">hidden</c:if>>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-4" style="font-size:2rem">Remarks <c:if test="${isDoView != 'Y'}"> <span style="color: #ff0000"> *</span> </c:if></label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-6">
                                    <div class="file-upload-gp">
                                            <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                                      title="content"
                                                      maxlength="500" <c:if test="${isDoView eq 'Y'}"> readonly="readonly"</c:if>   >${withdrawDtoView.withdrawnRemarks}</textarea>
                                    </div>
                                    <span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="center-content">
                        <div class="">
                            <div class="document-upload-gp">
                                <div class="document-upload-list">
                                    <h3>File upload for Withdrawal Reasons</h3>
                                    <c:choose>
                                        <c:when test="${isDoView != 'Y'}">
                                            <div class="file-upload-gp">
                                                <input id="selectedFile" type="file" style="display: none;" name = "selectedFile"
                                                       aria-label="selectedFile"><a class="btn btn-file-upload btn-secondary"
                                                                                    href="javascript:void(0);">Upload</a>

                                                <div id="delFile" style="margin-top: 13px;color: #1F92FF;"
                                                     hidden="hidden">
                                                    <strong id="fileName">${file_upload_withdraw}</strong>
                                                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteWdFile()"><em
                                                            class="fa fa-times"></em></button>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="fileRpId" value="${withdrawDtoView.appPremisesSpecialDocDto.fileRepoId}"></c:set>
                                            <c:set var="fileDocName" value="${withdrawDtoView.appPremisesSpecialDocDto.docName}"></c:set>

                                            <%--NewApplicationAjaxController method - > file-repo--%>
                                            <a class="" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${fileRpId}&fileRo${fileRpId}=<iais:mask name="fileRo${fileRpId}"
                                            value="${fileRpId}"/>&fileRepoName=${fileDocName}"  >${fileDocName}</a>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
                                    <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${isDoView != 'Y'}">
                    <div class="center-content">
                        <div class="components">
                            <a class="btn btn-primary" style="float:right" onclick="doSubmit()" href="javascript:void(0);">Submit</a>
                            <span style="float:right">&nbsp;</span>
                            <a class="btn btn-secondary" style="float:right"
                               href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp">Cancel</a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="modal fade" id="isAppealModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    </div>
                    <div class="modal-body" style="text-align: center;">
                        <div class="row">
                            <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;"> ${ARR} </span></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
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
        if('${appIsWithdrawal}'){
            $('#isAppealModal').modal('show');
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



    $("#selectedFile").change(function () {
        var configFileSize = $("#configFileSize").val();
        var error  = validateUploadSizeMaxOrEmpty(configFileSize,'selectedFile');
        if (error == "Y") {
            $('#error_litterFile_Show').html("");
            $("#delFile").removeAttr("hidden");
            let fileName = $("#selectedFile").val();
            let pos = fileName.lastIndexOf("\\");
            $("#fileName").html(fileName.substring(pos + 1));
        }else{
            $("#selectedFile").val("");
            $('#error_litterFile_Show').html('The file has exceeded the maximum upload size of '+ configFileSize + 'M.');
            $("#fileName").html("");
        }
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