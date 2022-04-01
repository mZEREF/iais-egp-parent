<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp" %>
<style>
    .document-upload-gp .document-upload-list {
        background-color: #F8F8F8;
        border-radius: 14px;
        padding: 30px;
        margin-bottom: 0;
    }
    .document-upload-list {
        background-color: #F8F8F8;
        border-radius: 14px;
        padding: 30px;
        margin-bottom: 0;
    }
    .application-tab-footer {
        margin-top: 0;
        padding-top: 0;
        border-top: 1px solid #BABABA;
        margin-left: 0;
        margin-right: 0;
    }
    .withdraw-content-box {
        background-color: #fafafa;
        border-radius: 14px;
        padding: 20px;
        border:1px solid #d1d1d1;
        margin-bottom: 0;
    }
    .modal-header .close {
        padding: 1rem 1rem;
        margin: 0rem 0rem 0rem auto;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-info p:before {
        color: #a2d9e7;
    }

    .withdraw-info-gp .withdraw-info-row .withdraw-delete {
        margin-left: 20px;
        width: 70px;
        display: inline-block;
        text-align: right;
    }
</style>
<div class="container">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="app_action_type" value="">
        <input type="hidden" name="print_action_type" value="">
        <input type="hidden" name="withdraw_app_list" value="">
        <input type="hidden" id="configFileSize" value="${configFileSize}"/>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
        <div class="navigation-gp">
            <c:choose>
                <c:when test="${isDoView eq 'Y'}">
                    <p class="print"><div style="font-size: 16px;text-align: right"><a href="javascript:void(0);" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
                </c:when>
                <c:otherwise>
                    <p class="print"><div style="font-size: 16px;text-align: right"><a href="javascript:void(0);" onclick="printApplyPrint()"> <em class="fa fa-print"></em>Print</a></div></p>
                </c:otherwise>
            </c:choose>
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
                                                    <p><a href="javascript:void(0);" class="appNo" onclick="toApplicationView('<iais:mask name="appNo" value="${withdrawAppNo}"/>','${withdrawAppNo}')">${withdrawAppNo}</a></p>
                                                </div>
                                                <div class="withdraw-delete"></div>
                                            </div>
                                        </div>
                                    </div>
                                    <c:forEach items="${addWithdrawnDtoList}" var="wdList">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a href="javascript:void(0);" class="appNo">${wdList.applicationNo}</a></p>
                                                    </div>
                                                    <div class="withdraw-delete">
                                                        <p><a href="javascript:void(0);" onclick="deleteWithdraw(this)"><em
                                                                class="fa fa-trash-o"></em>Delete</a></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <c:if test="${isDoView != 'Y'}">
                                    <div class="col-lg-4 col-xs-12">
                                        <div class="withdraw-addmore gradient-light-grey">
                                            <a  href="#newappModal" data-toggle="modal" data-target="#newappModal"><h4>
                                                <em
                                                        class="fa fa-plus-circle"></em> Add more applications</h4>
                                            </a> <%--NOSONAR--%>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div id="newappModal" class="modal fade" tabindex="-1" role="dialog" style="top:10px">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <!-- Modal content-->
                                <div class="modal-content" style="top:30px">
                                    <div class="modal-header">
                                        <div class="modal-title" id="gridSystemModalLabel" style="font-size: 2rem;">Select application for withdrawal</div>
                                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                                    </div>
                                    <div id="withdrawPagDiv"></div>
                                    <table aria-describedby="" class="table">
                                        <thead style="display: none">
                                        <tr>
                                            <th scope="col" ></th>
                                        </tr>
                                        </thead>
                                        <tbody id="withdrawBodyDiv"></tbody>
                                    </table>
                                    <div class="modal-footer">
                                        <a  class="btn btn-primary withdraw-next" href="javascript:void(0);">Done</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="center-content">
                            <h3>Reason for Withdrawal <c:if test="${isDoView != 'Y'}"> <span
                                    style="color: #ff0000"> *</span> </c:if></h3>
                            <div class="row">
                                <div class="col-md-7">
                                    <c:choose>
                                        <c:when test="${isDoView eq 'Y'}">
                                            <iais:select name="withdrawalReason" id="withdrawalReason"
                                                         options="withdrawalReasonList"
                                                         onchange="withdrawalReasons(this.value);"
                                                         value="${withdrawDtoView.withdrawnReason}" disabled="true"/>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:select name="withdrawalReason" id="withdrawalReason"
                                                         options="withdrawalReasonList"
                                                         onchange="withdrawalReasons(this.value);"
                                                         value="${withdrawDtoView.withdrawnReason}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <span id="error_withdrawnReason" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                        <div id="reason"
                             <c:if test="${withdrawDtoView.withdrawnReason != 'WDR005' || withdrawDtoView.withdrawnReason== null}">style="display: none"</c:if>>
                            <div class="row">
                                <div class="center-content">
                                    <label class="col-md-4" style="font-size:2rem">Remarks <c:if
                                            test="${isDoView != 'Y'}"> <span
                                            style="color: #ff0000"> *</span> </c:if></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="center-content">
                                    <div class="col-md-6">
                                        <div class="file-upload-gp">
                                            <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                                      title="content"
                                                      maxlength="500" <c:if test="${isDoView eq 'Y'}"> readonly="readonly"</c:if>>${withdrawDtoView.withdrawnRemarks}</textarea>
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
                                                        <span name="selectedWdFileShowId" id="selectedWdFileShowId">
                                                        <c:forEach items="${withdrawPageShowFiles}" var="withdrawPageShowFile"
                                                                   varStatus="ind">
                                                          <div id="${withdrawPageShowFile.fileMapId}">
                                                              <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                                                    <iais:downloadLink fileRepoIdName="fileRo${ind.index}" fileRepoId="${withdrawPageShowFile.fileUploadUrl}" docName="${withdrawPageShowFile.fileName}"/>
                                                              </span>
                                                              <span class="error-msg" name="iaisErrorMsg"
                                                                    id="file${ind.index}"></span>
                                                              <span class="error-msg" name="iaisErrorMsg"
                                                                    id="error_${configIndex}error"></span>
                                                            <button type="button" class="btn btn-secondary btn-sm"
                                                                    onclick="javascript:deleteFileFeAjax('selectedWdFile',${withdrawPageShowFile.index});">
                                                            Delete</button>  <button type="button"
                                                                                     class="btn btn-secondary btn-sm"
                                                                                     onclick="javascript:reUploadFileFeAjax('selectedWdFile',${withdrawPageShowFile.index},'mainForm');">
                                                          ReUpload</button>
                                                          </div>
                                                        </c:forEach>
                                                        </span>
                                                    <input id="selectedFile" name="selectedFile"
                                                           class="selectedFile commDoc"
                                                           type="file" style="display: none;"
                                                           aria-label="selectedFile1"
                                                           onclick="fileClicked(event)"
                                                           onchange="doUserRecUploadConfirmFile(event)"/><a href="javascript:void(0);"
                                                                                                            class="btn btn-file-upload btn-secondary"
                                                                                                            onclick="doFileAddEvent()">Upload</a>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span name="selectedWdFileShowId" id="selectedWdFileShowId">
                                                <c:forEach items="${withdrawPageShowFiles}" var="withdrawPageShowFile"
                                                           varStatus="ind">
                                                  <div id="${withdrawPageShowFile.fileMapId}">
                                                      <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                                            <iais:downloadLink fileRepoIdName="fileRo${ind.index}" fileRepoId="${withdrawPageShowFile.fileUploadUrl}" docName="${withdrawPageShowFile.fileName}"/>
                                                      </span>
                                                      <span class="error-msg" name="iaisErrorMsg"
                                                            id="file${ind.index}"></span>
                                                      <span class="error-msg" name="iaisErrorMsg"
                                                            id="error_${configIndex}error"></span>
                                                  </div>
                                                </c:forEach>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"
                                              style="color: #D22727; font-size: 1.6rem"></span>
                                        <span id="error_selectedWdFileError" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="center-content">
                            <c:if test="${isDoView == 'Y'}">
                                <div class="application-tab-footer">
                                    <div class="row" style="padding-top: 10px;">
                                        <div class="col-xs-12 col-sm-6">
                                <span style="padding-right: 10%" class="components">
                                    <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                                            class="fa fa-angle-left"></em> Back</a>
                                </span>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${isDoView != 'Y'}">
                                <div class="application-tab-footer">
                                    <div class="row" style="padding-top: 10px;">
                                        <div class="col-xs-12 col-sm-6">
                                    <span style="padding-right: 10%" class="components">
                                        <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                                                class="fa fa-angle-left"></em> Back</a>
                                    </span>
                                        </div>
                                        <a class="btn btn-primary" style="float:right" onclick="doSubmit()"
                                           href="javascript:void(0);">Submit</a>
                                        <span style="float:right">&nbsp;</span>
                                        <a class="btn btn-secondary" style="float:right"
                                           href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp">Cancel</a>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="isAppealModal" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <%--                    div class="modal-header">--%>
                    <%--                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
                    <%--                    </div>--%>
                    <div class="modal-body" style="text-align: center;">
                        <div class="row">
                            <div class="col-md-12"><span style="font-size: 2rem;"> ${ARR} </span></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
        <%@ include file="../appeal/FeFileCallAjax.jsp" %>
    </form>
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
        if ('${appIsWithdrawal}') {
            $('#isAppealModal').modal('show');
        }

        var result = '${apply_page_print}';
        console.log(result);
        if ("Y" == result){
            // alert("print")
            window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohAppealPrint?whichPage=wdPage",'_blank');
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

    function printApplyPrint(){
        $("[name='print_action_type']").val("applyPagePrint");
        doSubmit();
    }

    function doFileAddEvent() {
        clearFlagValueFEFile();
    }

    function doUserRecUploadConfirmFile(event) {
        uploadFileValidate();
    }

    function uploadFileValidate() {
        var configFileSize = $("#configFileSize").val();
        console.log('maxFileSize : '+configFileSize);

        var error = validateUploadSizeMaxOrEmpty(configFileSize, 'selectedFile');
        console.log(error);

        if (error == "Y") {
            $('#error_litterFile_Show').html("");
            $('#error_selectedWdFileError').html("");
            $("#delFile").removeAttr("hidden");
            let fileName = $("#selectedFile").val();
            let pos = fileName.lastIndexOf("\\");
            $("#fileName").html(fileName.substring(pos + 1));
            ajaxCallUploadForMax('mainForm', "selectedWdFile",true);
        } else if(error == "E"){
            $('#error_litterFile_Show').html("");
            $('#error_selectedWdFileError').html("");
        } else{
            $("#selectedFile").val("");
            $('#error_selectedWdFileError').html("");
            $('#error_litterFile_Show').html($("#fileMaxMBMessage").val());
            $("#fileName").html("");
        }
    }

    function deleteWdFile() {
        let wdfile = $("#selectedFile");
        wdfile.val("");
        $("#delFile").attr("hidden", "hidden");
    }

    function deleteWithdraw(it) {
        console.log("delete withdraw app");
        $(it).parent().parent().parent().parent().parent().remove();
    }

    $(".withdraw-next").click(function () {
        $("input[name='checkboxSample']:checked").each(function () {
            let appNoList = [];
            let withdrawContent$ = $(".withdraw-content-box");
            withdrawContent$.find(".withdraw-info p").each(function () {
                appNoList.push($(this).text())
            });
            let appNo = $(this).parent().parent().parent().find(".withdraw-info").find("p").eq(0).text();
            let appmask = "'" + $(this).parent().parent().parent().find(".withdraw-info").find("p").eq(1).text() + "'";
            if ($.inArray(appNo, appNoList) == -1) {
                let appStrNo = "'" + appNo + "'";
                withdrawContent$.last().parent().append('<div class="withdraw-content-box">\n' +
                    '                                    <div class="withdraw-info-gp">\n' +
                    '                                        <div class="withdraw-info-row">\n' +
                    '                                            <div class="withdraw-info">\n' +
                    '                                                <p><a href="javascript:void(0);" class="appNo" onclick="toApplicationView('+appmask+','+appStrNo+')">'+ appNo+'</a></p>\n'+
                    '                                            </div>\n' +
                    '                                            <div class="withdraw-delete">\n' +
                    '                                                <p ><a href="javascript:void(0);" onclick="deleteWithdraw(this)"><i class="fa fa-trash-o"></i>Delete</a></p>\n' +
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
        withdrawContent$.find(".withdraw-info p").each(function () {
            appNoList = appNoList+$(this).find('a').text()+"#";
        });
        $("[name='withdraw_app_list']").val(appNoList);
        submit("withdrawalStep");
    }
    function printWDPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohAppealPrint?whichPage=wdPage",'_blank');
    }

    function toApplicationView(appMaskNo, appNo) {
        let url = "";
        console.log(appMaskNo);
        var appNoStr = appNo.substr(0, 2);
        console.log(appNoStr);
        if ('AA' == appNoStr) {
            url = '${pageContext.request.contextPath}/eservice/INTERNET/MohFeApplicationView?appNo=' + appMaskNo + "&app_type=ar";
        } else {
            url = '${pageContext.request.contextPath}/eservice/INTERNET/MohFeApplicationView?appNo=' + appMaskNo + "&app_type=an";
        }
        showPopupWindow(url);
    }

</script>