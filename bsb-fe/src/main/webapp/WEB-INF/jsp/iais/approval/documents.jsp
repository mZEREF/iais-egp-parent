
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" id="page_id" name="page_id" value="document_page">
    <input type="hidden" id="actionType" name="actionType" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="documentsTab" role="tabpanel">
                            </div>
                            <div class="document-content ">
                                <input type="hidden" name="uploadKey" value=""/>
                                <div id="selectFileDiv">
                                    <input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
                                </div>
                                <div class="document-info-list">
                                    <ul>
                                        <li>
                                            <p>The maximum file size for each upload is <c:out value="${sysFileSize}"/>MB. </p>
                                        </li>
                                        <li>
                                            <p>Acceptable file formats are<c:out value="${sysFileType}"/></p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="document-upload-gp">
                                    <h2>PRIMARY DOCUMENTS</h2>
                                    <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                                        <c:choose>
                                            <c:when test="${'1' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'2' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'3' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'4' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'5' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'6' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'7' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'8' == config.index}">
                                                <c:set var="docIndex" value="${config.index}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="common/docContent.jsp"%>
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em>Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id = "SaveDraft"  href="javascript:void(0);">Save as Draft</a>
                                                <a class="btn btn-primary" id="Next" href="javascript:void(0);">Next</a>
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
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="common/FeFileCallAjax.jsp" %>
</form>
<script type="text/javascript">
    $(document).ready(function () {
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
        if($("#errorMapIs").val()=='error'){
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

    function fileClicked(event) {
        var fileElement = event.target;
        if (fileElement.value != "") {
            console.log("Clone( #" + fileElement.id + " ) : " + fileElement.value.split("\\").pop())
            clone[fileElement.id] = $(fileElement).clone(); //'Saving Clone'
        }
        //What ever else you want to do when File Chooser Clicked
    }

    // FileChanged()
    function fileChangedLocal(obj, event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            fileChanged(event);
        } else {
            var file = obj.value;
            if (file != null && file != '' && file != undefined) {
                var configIndex = $('input[name="uploadKey"]').val();
                ajaxCallUploadForMax('mainForm',configIndex,true);
            }
        }
    }


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


    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }


    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().html();
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');

    });

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('input[type="file"]').prop('disabled', false);
            $('.existFile').removeClass('hidden');
            $('.existFile').removeClass('existFile');
            $('.file-upload').removeClass('hidden');
            $('.delFileBtn').removeClass('hidden');
            $('.reUploadFileBtn').removeClass('hidden');
        });
    }

    function saveDraft() {
        submit('documents', 'saveDraft', $('#selectDraftNo').val());
    }

    function cancelSaveDraft() {
        submit('documents', 'saveDraft', 'cancelSaveDraft');
    }

    function cancel() {
        $('#saveDraft').modal('hide');
    }

    function jumpPage() {
        submit('premises', 'saveDraft', 'jumpPage');
    }
</script>

