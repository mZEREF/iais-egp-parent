<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");

    String webroot2 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=webroot2%>js/file-upload.js"></script>

<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <%--<%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>--%>
    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity"
           value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <input type="hidden" name="sysFileSize" id="sysFileSize" value="${sysFileSize}"/>
    <div class="main-content">
        <div class="center-content">
            <div class="row">
                <div class="col-xs-12 intranet-content">
                    <div class="tab-gp steps-tab tab-be">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>

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
                                    <c:if test="${requestInformationConfig == null}">
                                        <c:set var="isClickEdit" value="true"/>
                                    </c:if>
                                    <c:if test="${AppSubmissionDto.needEditController}">
                                        <c:set var="isClickEdit" value="false"/>
                                        <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                                            <c:if test="${'APPPN02' == clickEditPage}">
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
                                        <c:if test="${'true' != isClickEdit}">
                                            <c:set var="locking" value="true"/>
                                            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.docEdit}"/>
                                            <div id="edit-content">
                                                <c:choose>
                                                    <c:when test="${canEdit}">
                                                        <p>
                                                        <div class="text-right app-font-size-16">
                                                            <a id="edit" class="primaryDocEdit">
                                                                <em class="fa fa-pencil-square-o"></em>
                                                                <span>&nbsp;</span>Edit
                                                            </a>
                                                        </div>
                                                        </p>
                                                    </c:when>
                                                    <c:otherwise>

                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                    </c:if>
                                    <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                                        <c:choose>
                                            <c:when test="${'0' == config.dupForPrem}">
                                                <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                                                <c:set var="configIndex" value="${configStat.index}primaryDoc"/>
                                                <%@include file="section/docContent.jsp"%>
                                            </c:when>
                                            <c:when test="${'1' == config.dupForPrem}">
                                                <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                                                    <c:set var="mapKey" value="${prem.premisesIndexNo}${config.id}"/>
                                                    <c:set var="fileList" value="${docReloadMap[mapKey]}"/>
                                                    <c:set var="configIndex" value="${configStat.index}primaryDoc${prem.premisesIndexNo}"/>
                                                    <%@include file="section/docContent.jsp"%>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>

                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <!--cr-082 end -->
                                </div>
                                <%@ include file="/WEB-INF/jsp/iais/application/common/appFooter.jsp"%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <input type="hidden" name="pageCon" value="valPremiseList">
    <c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
        <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft"
                      yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary"
                      yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
    </c:if>
</form>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        if ($('#saveDraftSuccess').val() == 'success') {
            $('#saveDraft').modal('show');
        }
        <%--if(${(AppSubmissionDto.needEditController && !isClickEdit) || AppSubmissionDto.onlySpecifiedSvc }){--%>
        if (${(AppSubmissionDto.needEditController && !isClickEdit)}) {
            disabledPage();
            $('.file-upload').addClass('hidden');
            $('.delFileBtn').addClass('hidden');
            $('.reUploadFileBtn').addClass('hidden');
        }

        //Binding method
        $('#Back').click(function () {
            showWaiting();
            submit('premises', 'back', null);
        });
        $('#SaveDraft').click(function () {
            showWaiting();
            submit('documents', 'saveDraft', $('#selectDraftNo').val());
        });
        $('#Next').click(function () {
            showWaiting();
            submit('serviceForms', "next", null);
        });

        doEdit();
        if($("#errorMapIs").val()=='error'){
            $('#edit').trigger("click");
        }

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            clearFlagValueFEFile();
            //68932
            $('#selectFileDiv').html('<input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">');
            $('input[type="file"]').click();
        });
    });

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