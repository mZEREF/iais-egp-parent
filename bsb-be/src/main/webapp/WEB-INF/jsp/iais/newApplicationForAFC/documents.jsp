<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <%--<%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>--%>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="documentsTab" role="tabpanel">
                            </div>
                            <div class="document-content ">
                                <input type="hidden" name="uploadKey" value=""/>
                                <div id="selectFileDiv">
                                    <input id="selectedFile" class="selectedFile" name="selectedFile" type="file"
                                           style="display: none;" onclick="fileClicked(event)"
                                           onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
                                </div>
                                <div class="document-info-list">
                                    <ul>
                                        <li>
                                            <p> The maximum file size for each upload is 5MB. </p>
                                        </li>
                                        <li>
                                            <p>Acceptable file formats are PDF, word, JPG, Excel and PNG/></p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="document-upload-gp">
                                    <h2>PRIMARY DOCUMENTS</h2>
                                    <%@include file="common/docContent.jsp" %>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <a class="back" id="Back" href="/main-web/eservicecontinue/INTERNET/NewApplicationForAFC/1/CretifyingTeamInfo">
                                                <em class="fa fa-angle-left"></em> Back
                                            </a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id="SaveDraft" href="javascript:void(0);">Save as Draft</a>
                                                <a class="btn btn-primary next" id="Cancle" href="/main-web/eservice/INTERNET/MohInternetInbox">Cancle</a>
                                                <a class="btn btn-primary next" id="Next" href="javascript:void(0);">Next</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%--    <%@ include file="FeFileCallAjax.jsp" %>--%>
    <input type="hidden" name="pageCon" value="valPremiseList">
    <c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
        <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft"
                      yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary"
                      yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
    </c:if>
</form>

<script type="text/javascript">
    $(document).ready(function () {
        if ($('#saveDraftSuccess').val() == 'success') {
            $('#saveDraft').modal('show');
        }
        if (${(AppSubmissionDto.needEditController && !isClickEdit)}) {
            disabledPage();
            $('.file-upload').addClass('hidden');
            $('.delFileBtn').addClass('hidden');
            $('.reUploadFileBtn').addClass('hidden');
        }

        $('#SaveDraft').click(function () {
            showWaiting();
            submit('documents', 'saveDraft', $('#selectDraftNo').val());
        });
        $('#Next').click(function () {
            showWaiting();
            document.getElementById("mainForm").submit();
        });

        $('.commDoc').change(function () {
            var maxFileSize = $('#sysFileSize').val();
            var error = validateUploadSizeMaxOrEmpty(maxFileSize, $(this));
            if (error == "N") {
                $(this).closest('.file-upload-gp').find('.error-msg').html($("#fileMaxMBMessage").val());
                $(this).closest('.file-upload-gp').find('span.delBtn').trigger('click');
                dismissWaiting();
            } else {
                $(this).closest('.file-upload-gp').find('.error-msg').html('');
                dismissWaiting();
            }

        });


        doEdit();
        if ($("#errorMapIs").val() == 'error') {
            $('#edit').trigger("click");
        }

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            clearFlagValueFEFile();
            $('#selectFileDiv').html('<input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">');
            $('input[type="file"]').click();
        });
    });

    function validateUploadSizeMaxOrEmpty(maxSize, $fileEle) {
        var fileV = $fileEle.val();
        var file = $fileEle.get(0).files[0];
        if (fileV == null || fileV == "" || file == null || file == undefined) {
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if (fileSize >= maxSize) {
            return "N";
        }
        return "Y";
    }

</script>