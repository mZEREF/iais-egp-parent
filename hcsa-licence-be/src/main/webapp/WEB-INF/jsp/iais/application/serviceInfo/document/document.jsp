<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<script type="text/javascript" src="<%=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT%>js/file-upload.js"></script>

<div class="row form-horizontal  block-error">
<c:if test="${isRfi}">
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
        <div class="text-right app-font-size-16">
            <a class="back" id="RfcSkip" href="javascript:void(0);">
                Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
            </a>
        </div>
    </c:if>
</c:if>

<h2>Service-related Documents</h2>

<c:if test="${AppSubmissionDto.needEditController}">
    <c:if test="${!isClickEdit}">
        <c:set var="locking" value="true"/>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        <div id="edit-content">
            <c:choose>
                <c:when test="${canEdit}">
                    <div class="text-right app-font-size-16">
                        <a id="edit" class="svcDocEdit" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                        </a>
                    </div>
                </c:when>
                <c:otherwise>

                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</c:if>
<input type="hidden" name="uploadKey" value=""/>
<div id="selectFileDiv">
    <input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
</div>
<c:forEach var="docShowDto" items="${currSvcInfoDto.documentShowDtoList}" varStatus="stat">
    <iais:row>
        <div class="col-xs-12">
            <div class="app-title"><c:out value="${docShowDto.premName}"/></div>
            <div class="font-18 bold">Address: <c:out value="${docShowDto.premAddress}"/></div>
        </div>
    </iais:row>
    <div class="panel-group doc-svc-content" id="${docShowDto.premisesVal}" role="tablist" aria-multiselectable="true">
        <c:forEach var="secDto" items="${docShowDto.docSectionList}" varStatus="secStat">
            <c:set var="panelKey">${docShowDto.premisesVal}-${secDto.svcId}</c:set>
            <div class="panel panel-default doc-panel">
                <div class="panel-heading" role="tab">
                    <h4 class="panel-title">
                        <a role="button" class="" data-toggle="collapse" href="#${panelKey}" aria-expanded="true" aria-controls="${panelKey}">
                            <c:out value="${secDto.sectionName}"/>
                        </a>
                    </h4>
                </div>
                <div id="${panelKey}" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <div class="panel-main-content">
                            <c:if test="${not empty secDto.docSecDetailList}" var="hasDocConfig">
                                <c:forEach var="doc" items="${secDto.docSecDetailList}" varStatus="docStat">
                                    <c:set var="configIndex" value="${docStat.index}svcDoc${secDto.svcCode}${docShowDto.premisesVal}"/>
                                    <%@include file="docContent.jsp"%>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not hasDocConfig}">
                                <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:forEach>
</div>
<script>
    $(document).ready(function () {
        if (${AppSubmissionDto.needEditController && !isClickEdit}) {
            disableContent('.doc-panel');
            hideTag('.file-upload');
            hideTag('.delFileBtn');
            hideTag('.reUploadFileBtn');
        }

        doEditDocEvent();
        if($("#errorMapIs").val()=='error'){
            $('#edit').trigger('click');
        }

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            clearFlagValueFEFile();
            $('#selectFileDiv').html('<input id="' + index + '"  class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">');
            $('input[type="file"]').click();
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

    $('.delBtn').click(function () {
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileNameSpan').html('');
        documentDiv.find('.delBtn').html('');
        documentDiv.find('.delBtn').addClass('hidden');
        documentDiv.find('input.svcDoc').val('');
        $(this).closest('.fileContent').find('input.selectedFile').val('');
        $(this).closest('.fileContent').find('input.delFlag').val('Y');
    });

    var doEditDocEvent = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            unDisableContent('.doc-panel');
            showTag('.file-upload');
            showTag('.delFileBtn');
            showTag('.reUploadFileBtn');
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

</script>