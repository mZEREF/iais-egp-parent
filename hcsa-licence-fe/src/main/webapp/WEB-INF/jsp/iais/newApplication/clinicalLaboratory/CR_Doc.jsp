<input type="hidden" name="sysFileSize" id="sysFileSize" value="${sysFileSize}"/>
<c:if test="${requestInformationConfig == null}">
    <c:set var="isClickEdit" value="true"/>
</c:if>
<c:if test="${AppSubmissionDto.needEditController}">
    <c:set var="isClickEdit" value="false"/>
    <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
        <c:if test="${'APPSPN06' == clickEditPage}">
            <c:set var="isClickEdit" value="true"/>
        </c:if>
    </c:forEach>
    <c:choose>
        <c:when test="${'true' != isClickEdit}">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        </c:when>
        <c:otherwise>
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
        </c:otherwise>
    </c:choose>
    <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
        <p>
        <div class="text-right app-font-size-16"><a class="back" id="RfcSkip">Skip<span>&nbsp;</span><em
                class="fa fa-angle-right"></em></a></div>
        </p>
    </c:if>
    <c:if test="${'true' != isClickEdit}">
        <c:set var="locking" value="true"/>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        <div id="edit-content">
            <c:choose>
                <c:when test="${'true' == canEdit}">
                    <p>
                    <div class="text-right app-font-size-16"><a id="edit" class="svcDocEdit" ><em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit</a>
                    </div>
                    </p>
                </c:when>
                <c:otherwise>

                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</c:if>
<input type="hidden" name="uploadKey" value=""/>
<input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
<c:set var="docType" value="svcDoc"/>
<c:forEach var="config" items="${svcDocConfig}" varStatus="configStat">
    <c:choose>
        <c:when test="${'0' == config.dupForPrem}">
            <c:choose>
                <c:when test="${empty config.dupForPerson}">
                    <c:set var="fileList" value="${svcDocReloadMap[config.id]}"/>
                    <c:set var="configIndex" value="${configStat.index}svcDoc${currentSvcCode}"/>
                    <%@include file="../../common/docContent.jsp"%>
                    <br/>
                </c:when>
                <c:otherwise>
                    <c:set var="premIndexNo" value=""/>
                    <%@include file="dupForPerson.jsp"%>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:when test="${'1' == config.dupForPrem}">
            <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                <c:choose>
                    <c:when test="${empty config.dupForPerson}">
                        <c:set var="mapKey" value="${prem.premisesIndexNo}${config.id}"/>
                        <c:set var="fileList" value="${svcDocReloadMap[mapKey]}"/>
                        <c:set var="configIndex" value="${configStat.index}svcDoc${currentSvcCode}${prem.premisesIndexNo}"/>
                        <%@include file="../../common/docContent.jsp"%>
                        <br/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="premIndexNo" value="${prem.premisesIndexNo}"/>
                        <%@include file="dupForPerson.jsp"%>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>
</c:forEach>

<input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
<%@ include file="../../appeal/FeFileCallAjax.jsp" %>
<script>
    $(document).ready(function () {
        if (${AppSubmissionDto.needEditController && !isClickEdit}) {
            disabledPage();
            $('.file-upload').addClass('hidden');
            $('.delFileBtn').addClass('hidden');
            $('.reUploadFileBtn').addClass('hidden');
        }

        doEdit();
        if($("#errorMapIs").val()=='error'){
            $('#edit').trigger('click');
        }

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            $('input[type="file"]').click();
            clearFlagValueFEFile();
            $('#selectedFile').val('');
        });
    });

    <!-- 108635 start-->
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

    <!-- 108635 end-->

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    /*$('.selectedFile').change(function () {
        var file = $(this).val();
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileNameSpan').html(getFileName(file));
        documentDiv.find('.delBtn').html('&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-sm"><em class="fa fa-times"></em></button>');
        documentDiv.find('.delBtn').removeClass('hidden');
        var $fileUploadContentEle = $(this).closest('div.file-upload-gp');
        $fileUploadContentEle.find('.delBtn').removeClass('hidden');
    });*/

    $('.delBtn').click(function () {
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileNameSpan').html('');
        documentDiv.find('.delBtn').html('');
        documentDiv.find('.delBtn').addClass('hidden');
        documentDiv.find('input.svcDoc').val('');
        $(this).closest('.fileContent').find('input.selectedFile').val('');
        $(this).closest('.fileContent').find('input.delFlag').val('Y');
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

    $('.svcDoc').change(function () {
        var maxFileSize = $('#sysFileSize').val();
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, $(this));
        if (error == "N") {
            $(this).closest('.document-upload-list').find('.error-msg').html($("#fileMaxMBMessage").val());
            $(this).closest('.document-upload-list').find('span.delBtn').trigger('click');
            dismissWaiting();
        } else {
            $(this).closest('.document-upload-list').find('.error-msg').html('');
            dismissWaiting();
        }

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