<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<c:set var="isRfi" value="${requestInformationConfig != null}"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="${isRFi ? '1' : '0'}"/>
<input id="isEditHiddenVal" type="hidden" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row">
    <div class="form-group">
        <h4><iais:message key="NEW_ACK030"/></h4>
    </div>
    <div class="form-group">
        <div class="row control control-caption-horizontal">
            <div class="control-label col-md-5 col-xs-5">
                <label  class="control-label control-set-font control-font-label">
                    <div style="font-weight: 600;font-size: 2.2rem">
                        <c:out value="${currStepName}"/>
                    </div>
                </label>
            </div>
            <div class="col-md-7 col-xs-7 text-right">
                <c:if test="${AppSubmissionDto.needEditController }">
                    <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                        <div class="app-font-size-16">
                            <a class="back" id="RfcSkip" href="javascript:void(0);">
                                Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                            </a>
                        </div>
                    </c:if>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                </c:if>
            </div>
        </div>
        <span class="error-msg" name="iaisErrorMsg" id="error_errorSECLDR"></span>
    </div>
</div>

<div class="row">
    <c:choose>
    <c:when test="${empty sectionLeaderList && sectionLeaderConfig.mandatoryCount > 1}">
        <c:set var="pageLength" value="${sectionLeaderConfig.mandatoryCount}"/>
    </c:when>
    <c:when test="${empty sectionLeaderList}">
        <c:set var="pageLength" value="1"/>
    </c:when>
    <c:when test="${sectionLeaderConfig.mandatoryCount > sectionLeaderList.size() }">
        <c:set var="pageLength" value="${sectionLeaderConfig.mandatoryCount}"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageLength" value="${sectionLeaderList.size()}"/>
    </c:otherwise>
    </c:choose>
    <input type="hidden" name="slLength" value="${pageLength}" />

    <c:forEach begin="0" end="${pageLength - 1}" step="1" varStatus="slStat">
        <c:set var="index" value="${slStat.index}" />
        <c:set var="sectionLeader" value="${sectionLeaderList[index]}"/>
        <%@include file="sectionLeaderDetail.jsp" %>
    </c:forEach>

    <div class="col-md-12 col-xs-12 addSectionLeaderDiv" style="${!needAddPsn ? 'display:none;' : ''}">
        <span class="addSectionLeaderBtn" style="color:deepskyblue;cursor:pointer;">
            <span style="">+ Add Another ${singleName}</span>
        </span>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        initSectionLeader();
        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}" var="isSpecial">
        disableContent('div.sectionLaderContent');
        <c:if test="${!canEdit}">
        $('.removeBtn').closest('div').remove();
        </c:if>
        </c:if>
        if("${errormapIs}"=='error'){
            $('.edit').trigger('click');
        }
    });

    function initSectionLeader() {
        addSectionLeaderBtn();
        removeSectionLeader();
        doEdite();
        refreshBtn();
    }

    function refreshSectionLeaderIndex() {
        var slLength = $('.sectionLaderContent').length;
        $('input[name="slLength"]').val(slLength);
        console.info("length: " + slLength)
        var $content = $('div.sectionLaderContent');
        refreshIndex($content);
        $content.each(function (k,v) {
            if (slLength <= 1 && k == 0) {
                $(this).find('.assign-psn-item').html('');
            } else {
                $(this).find('.assign-psn-item').html(k + 1);
            }
        });
    }

    function refreshBtn() {
        var $content = $('div.sectionLaderContent');
        var slLength = $content.length;
        $('input[name="slLength"]').val(slLength);
        var isEdit =  $('#isEditHiddenVal').val();
        $content.each(function (index,v) {
            if (index < '${sectionLeaderConfig.mandatoryCount}') {
                $(v).find('.removeSectionLeaderDiv').remove();
            } else {
                $(v).find('.removeSectionLeaderDiv').show();
            }
        });
        <c:if test="${!isRfi && (AppSubmissionDto.appType == 'APTY002' || canEdit)}" var="canShowAddBtn">
        // display add more
        if (slLength < '${sectionLeaderConfig.maximumCount}') {
            $('.addSectionLeaderDiv').show();
        } else {//hidden add more
            $('.addSectionLeaderDiv').hide();
        }
        </c:if>
        <c:if test="${!canShowAddBtn}">
        $('.addSectionLeaderDiv').remove();
        </c:if>
    }

    function addSectionLeaderBtn () {
        $('.addSectionLeaderBtn').unbind('click');
        $('.addSectionLeaderBtn').click(function () {
            showWaiting();
            var slLength = $('.sectionLaderContent').length;

            $.ajax({
                url: '${pageContext.request.contextPath}/section-leader-html',
                dataType: 'json',
                data: {
                    "slLength": slLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addSectionLeaderDiv').before(data.resultJson+'');
                        // init
                        $('div.sectionLaderContent select').niceSelect();
                        refreshSectionLeaderIndex();
                        initSectionLeader();
                        $('#isEditHiddenVal').val('1');
                    }
                    dismissWaiting();
                },
                error: function (data) {
                    console.log("err");
                    dismissWaiting();
                }
            });
        });
    };

    function removeSectionLeader () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            showWaiting();
            var slLength = $('.sectionLaderContent').length;
            if (slLength <= '${sectionLeaderConfig.mandatoryCount}') {
                dismissWaiting();
                return;
            }
            var $currContent = $(this).closest('div.sectionLaderContent');
            $currContent.remove();
            $('#isEditHiddenVal').val('1');
            refreshSectionLeaderIndex();
            initSectionLeader();
            dismissWaiting();
        });
    }

    var doEdite = function () {
        $('.edit-content a').unbind('click');
        $('.edit-content a').click(function () {
            var $currContent = $(this).closest('div.sectionLaderContent');
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
            $currContent.find('.isPartEdit').val('1');
            refreshBtn();
            $(this).hide();
        });
    }
</script>

