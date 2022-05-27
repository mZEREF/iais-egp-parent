<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <%--<c:choose>
                <c:when test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
                    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                </c:when>
                <c:otherwise>
                    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                </c:otherwise>
            </c:choose>--%>
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
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

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
        <div class="panel-heading " id="generate-charges-heading"  role="tab">
            <h4 class="panel-title">
                <a role="button" data-toggle="collapse" href="#generate-charges-content" aria-expanded="true" aria-controls="chargesContent">General Conveyance Charges</a>
            </h4>
        </div>
        <div class="panel-collapse collapse in" id="generate-charges-content" role="tabpanel" aria-labelledby="generate-charges-heading">
            <div class="row panel-body">
                <div class="panel-main-content">
                    <div class="col-md-12 col-xs-12">
                        <div class="row control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="control-label formtext col-md-5 col-xs-5">
                                    <label  class="control-label control-set-font control-font-label">Type of Charge</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="control-label formtext col-md-4 col-xs-4">
                                    <label  class="control-label control-set-font control-font-label">Amount</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="control-label formtext col-md-2 col-xs-2">
                                    <label  class="control-label control-set-font control-font-label">Remarks</label>
                                </div>
                                <div class="control-label formtext col-md-1 col-xs-1">

                                </div>
                            </div>
                        </div>
                    </div>
                    <c:set var="generalChargesDtoList" value="${generalChargesDtoList}"/>
                    <c:choose>
                        <c:when test="${empty generalChargesDtoList}">
                            <c:set var="pageLength" value="1"/>
                        </c:when>
                        <c:when test="${generalChargesConfig.mandatoryCount > generalChargesDtoList.size() }">
                            <c:set var="pageLength" value="${generalChargesConfig.mandatoryCount}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="pageLength" value="${generalChargesDtoList.size()}"/>
                        </c:otherwise>
                    </c:choose>
                    <input type="hidden" class="generalChargeLength" name="generalChargeLength" value="${pageLength}" />
                    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="gcStat">
                        <c:set var="generalChargesDto" value="${generalChargesDtoList[gcStat.index]}"/>
                        <div class="general-charges-content charges-content">
                            <input type="hidden" class ="isPartEdit" name="isPartEdit${gcStat.index}" value="0"/>
                            <input type="hidden" class="chargesIndexNo" name="chargesIndexNo${gcStat.index}" value="${generalChargesDto.chargesIndexNo}"/>
                            <div class="col-md-12 col-xs-12">
                                <div class="edit-content">
                                    <c:if test="${canEdit}">
                                        <div class="text-right app-font-size-16">
                                            <a class="edit chargesEdit" href="javascript:void(0);">
                                                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12 ">
                                <div class="row control control-caption-horizontal">
                                    <div class=" form-group form-horizontal formgap">
                                        <div class="control-label formtext col-md-5 col-xs-5">
                                            <iais:select cssClass="chargesType"  name="chargesType${gcStat.index}" codeCategory="CATE_ID_GENERAL_CONVEYANCE_CHARGES_TYPE" value="${generalChargesDto.chargesType}" firstOption="Please Select" />
                                        </div>
                                        <div class="control-label formtext col-md-4 col-xs-4">
                                            <div class="row">
                                                <div class="col-xs-5 col-md-5">
                                                    <iais:input maxLength="4" type="text" cssClass="minAmount" name="minAmount${gcStat.index}" value="${generalChargesDto.minAmount}"></iais:input>
                                                </div>
                                                <div class="col-xs-2 col-md-2 text-center">
                                                    <label  class="control-label control-set-font control-font-label">To</label>
                                                </div>
                                                <div class="col-xs-5 col-md-5">
                                                    <iais:input maxLength="4" type="text" cssClass="maxAmount" name="maxAmount${gcStat.index}" value="${generalChargesDto.maxAmount}"></iais:input>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="control-label formtext col-md-2 col-xs-2">
                                            <iais:input maxLength="150" type="text" cssClass="remarks" name="remarks${gcStat.index}" value="${generalChargesDto.remarks}"></iais:input>
                                        </div>
                                        <div class="control-label formtext col-md-1 col-xs-1">
                                            <c:if test="${(gcStat.index - generalChargesConfig.mandatoryCount >= 0) &&
                                            (!AppSubmissionDto.needEditController || canEdit)}">
                                                <h4 class="text-danger">
                                                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                                </h4>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${ requestInformationConfig==null}">
                        <c:choose>
                            <c:when test="${!empty generalChargesDtoList}">
                                <c:set var="generalChargesLength" value="${generalChargesDtoList.size()}"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${AppSubmissionDto.needEditController}">
                                        <c:set var="generalChargesLength" value="0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="generalChargesLength" value="${generalChargesConfig.mandatoryCount}"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <c:set var="needAddPsn" value="true"/>
                        <c:choose>
                            <c:when test="${generalChargesConfig.status =='CMSTAT003'}">
                                <c:set var="needAddPsn" value="false"/>
                            </c:when>
                            <c:when test="${generalChargesLength >= generalChargesConfig.maximumCount}">
                                <c:set var="needAddPsn" value="false"/>
                            </c:when>
                            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                                <c:set var="needAddPsn" value="false"/>
                            </c:when>
                        </c:choose>
                        <div class="col-md-12 col-xs-12 addGeneralChargesDiv <c:if test="${!needAddPsn}">hidden</c:if>">
                            <span class="addGeneralChargesBtn" style="color:deepskyblue;cursor:pointer;">
                                <span style="">+ Add Other Conveyance Related Charges</span>
                            </span>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
        <div class="panel-heading " id="other-charges-heading"  role="tab">
            <h4 class="panel-title">
                <a role="button" data-toggle="collapse" href="#other-charges-content" aria-expanded="true" aria-controls="chargesContent">Medical Equipment and Other Charges</a>
            </h4>
        </div>
        <div class="panel-collapse collapse in" id="other-charges-content" role="tabpanel" aria-labelledby="other-charges-heading">
            <div class="row panel-body">
                <div class="panel-main-content">
                    <div class="">
                        <div class="col-md-12 col-xs-12">
                            <div class="row control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                    <div class="control-label formtext col-md-5 col-xs-5">
                                        <div class="row">
                                            <div class="col-md-6 col-xs-6">
                                                <label  class="control-label control-set-font control-font-label">Category</label>
                                                <span class="mandatory">*</span>
                                            </div>
                                            <div class="col-md-6 col-xs-6">
                                                <label  class="control-label control-set-font control-font-label">Type of Charge</label>
                                                <span class="mandatory">*</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="control-label formtext col-md-4 col-xs-4">
                                        <label  class="control-label control-set-font control-font-label">Amount</label>
                                        <span class="mandatory">*</span>
                                    </div>
                                    <div class="control-label formtext col-md-2 col-xs-2">
                                        <label  class="control-label control-set-font control-font-label">Remarks</label>
                                    </div>
                                    <div class="control-label formtext col-md-1 col-xs-1">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <c:set var="otherChargesDtoList" value="${otherChargesDtoList}"/>
                        <c:choose>
                            <c:when test="${empty otherChargesDtoList}">
                                <c:set var="otherChargesLength" value="1"/>
                            </c:when>
                            <c:when test="${otherChargesConfig.mandatoryCount > otherChargesDtoList.size() }">
                                <c:set var="otherChargesLength" value="${otherChargesConfig.mandatoryCount}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="otherChargesLength" value="${otherChargesDtoList.size()}"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" class="otherChargeLength" name="otherChargeLength" value="${otherChargesLength}" />
                        <c:forEach begin="0" end="${otherChargesLength-1}" step="1" varStatus="ocStat">
                            <c:set var="otherChargesDto" value="${otherChargesDtoList[ocStat.index]}"/>
                            <div class="other-charges-content charges-content">
                                <input type="hidden" class ="isPartEdit" name="otherChargesIsPartEdit${ocStat.index}" value="0"/>
                                <input type="hidden" class="chargesIndexNo" name="otherChargesIndexNo${ocStat.index}" value="${otherChargesDto.chargesIndexNo}"/>
                                <div class="col-md-12 col-xs-12">
                                    <div class="edit-content">
                                        <c:if test="${'true' == canEdit}">
                                            <div class="text-right app-font-size-16">
                                                <a class="edit otherChargesEdit" href="javascript:void(0);">
                                                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="col-md-12 col-xs-12 other-charges-div">
                                    <input type="hidden" class="currChargesSuffix" name="currChargesSuffix" value="${ocStat.index}"/>
                                    <div class="row control control-caption-horizontal">
                                        <div class=" form-group form-horizontal formgap">
                                            <div class="control-label formtext col-md-5 col-xs-5">
                                                <div class="row">
                                                    <div class="col-md-6 col-xs-6">
                                                        <iais:select cssClass="otherChargesCategory"  name="otherChargesCategory${ocStat.index}" codeCategory="CATE_ID_MEDICAL_EQUIPMENT_AND_OTHER_CHARGES_CATEGORY" value="${otherChargesDto.chargesCategory}" firstOption="Please Select" />
                                                    </div>
                                                    <div class="col-md-6 col-xs-6 other-charges-type-div">
                                                        <iais:select cssClass="otherChargesType"  name="otherChargesType${ocStat.index}" codeCategory="${otherChargesDto.cateRelationType}" value="${otherChargesDto.chargesType}" firstOption="Please Select" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="control-label formtext col-md-4 col-xs-4">
                                                <div class="row">
                                                    <div class="col-xs-5 col-md-5">
                                                        <iais:input maxLength="4" type="text" cssClass="otherAmountMin" name="otherAmountMin${ocStat.index}" value="${otherChargesDto.minAmount}"></iais:input>
                                                    </div>
                                                    <div class="col-xs-2 col-md-2 text-center">
                                                        <label  class="control-label control-set-font control-font-label">To</label>
                                                    </div>
                                                    <div class="col-xs-5 col-md-5">
                                                        <iais:input maxLength="4" type="text" cssClass="otherAmountMax" name="otherAmountMax${ocStat.index}" value="${otherChargesDto.maxAmount}"></iais:input>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="control-label formtext col-md-2 col-xs-2">
                                                <iais:input maxLength="150" type="text" cssClass="otherRemarks" name="otherRemarks${ocStat.index}" value="${otherChargesDto.remarks}"></iais:input>
                                            </div>
                                            <div class="control-label formtext col-md-1 col-xs-1">
                                                <c:if test="${(ocStat.index - otherChargesConfig.mandatoryCount >= 0) && (!AppSubmissionDto.needEditController || canEdit)}">
                                                    <h4 class="text-danger">
                                                        <em class="fa fa-times-circle del-size-36 ocRemoveBtn cursorPointer"></em>
                                                    </h4>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${ requestInformationConfig==null}">
                            <c:choose>
                                <c:when test="${!empty otherChargesDtoList}">
                                    <c:set var="otherChargesLength" value="${otherChargesDtoList.size()}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${AppSubmissionDto.needEditController}">
                                            <c:set var="otherChargesLength" value="0"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="otherChargesLength" value="${otherChargesConfig.mandatoryCount}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <c:set var="needAddPsn" value="true"/>
                            <c:choose>
                                <c:when test="${otherChargesConfig.status =='CMSTAT003'}">
                                    <c:set var="needAddPsn" value="false"/>
                                </c:when>
                                <c:when test="${otherChargesLength >= otherChargesConfig.maximumCount}">
                                    <c:set var="needAddPsn" value="false"/>
                                </c:when>
                                <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                                    <c:set var="needAddPsn" value="false"/>
                                </c:when>
                            </c:choose>
                            <div class="col-md-12 col-xs-12 addOtherChargesDiv <c:if test="${!needAddPsn}">hidden</c:if>">
                                <span class="addOtherChargesBtn" style="color:deepskyblue;cursor:pointer;">
                                    <span style="">+ Add Medical Equipment And Other Charges</span>
                                </span>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {

        searchChargesTypeByCategory();
        removeGeneralChargesHtml();
        removeOtherChargesHtml();
        addGeneralChargesHtml();
        addOtherChargesHtml();

        doEdite();
        doOhterChargesEdite();
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //rfc,renew,rfi
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
        }

    });

    var searchChargesTypeByCategory = function () {
        $('select.otherChargesCategory').unbind('change');
        $('select.otherChargesCategory').change(function () {
            var thisVal = $(this).val();
            var $currContent = $(this).closest('.other-charges-div');
            var chargesSuffix = $currContent.find('.currChargesSuffix').val();
            showWaiting();
            $.ajax({
                url: '${pageContext.request.contextPath}/search-charges-type',
                dataType: 'json',
                data: {
                    "category": thisVal,
                    'chargesSuffix':chargesSuffix
                },
                type: 'POST',
                success: function (data) {
                    if('200' == data.resCode){
                        $currContent.find('div.other-charges-type-div').html(data.resultJson + '');
                        $currContent.find('select.otherChargesType').each(function () {
                            $(this).niceSelect();
                        });
                    }
                },
                error: function (data) {
                }
            });
            dismissWaiting();
        });
    };

    var addGeneralChargesHtml = function () {
        $('.addGeneralChargesBtn').click(function () {
            showWaiting();
            var generalChargeLength = $('.general-charges-content').length;
            $.ajax({
                url: '${pageContext.request.contextPath}/general-charges-html',
                dataType: 'json',
                data: {
                    "generalChargeLength": generalChargeLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addGeneralChargesDiv').before(data.resultJson+'');
                        //
                        removeGeneralChargesHtml();

                        var generalChargeLength = $('.general-charges-content').length;
                        $('input[name="generalChargeLength"]').val(generalChargeLength);
                        //hidden add more
                        if (generalChargeLength >= '${generalChargesConfig.maximumCount}') {
                            $('.addGeneralChargesDiv').addClass('hidden');
                        }
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

    var addOtherChargesHtml = function () {
        $('.addOtherChargesBtn').click(function () {
            showWaiting();
            var otherChargeLength = $('.other-charges-content').length;
            $.ajax({
                url: '${pageContext.request.contextPath}/other-charges-html',
                dataType: 'json',
                data: {
                    "otherChargeLength": otherChargeLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addOtherChargesDiv').before(data.resultJson+'');
                        //
                        removeOtherChargesHtml();
                        searchChargesTypeByCategory();

                        var otherChargeLength = $('.other-charges-content').length;
                        $('input[name="otherChargeLength"]').val(otherChargeLength);
                        //hidden add more
                        if (otherChargeLength >= '${generalChargesConfig.maximumCount}') {
                            $('.addOtherChargesDiv').addClass('hidden');
                        }
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

    var removeGeneralChargesHtml = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            var $currContent = $(this).closest('div.general-charges-content');
            $currContent.remove();
            //reset number
            var generalChargeLength = $('.general-charges-content').length;
            $('input[name="generalChargeLength"]').val(generalChargeLength);
            $('.general-charges-content').each(function (k,v) {
                $(this).find('select.chargesType').prop('name','chargesType'+ k);
                $(this).find('input.minAmount').prop('name','minAmount'+ k);
                $(this).find('input.maxAmount').prop('name','maxAmount'+ k);
                $(this).find('input.remarks').prop('name','remarks'+ k);
                $(this).find('.isPartEdit').prop('name','isPartEdit'+k);
                $(this).find('.chargesIndexNo').prop('name','chargesIndexNo'+k);
            });
            //display add btn
            if (generalChargeLength < '${generalChargesConfig.maximumCount}') {
                $('.addGeneralChargesDiv').removeClass('hidden');
            }
            $('#isEditHiddenVal').val('1');
        });
    };


    var removeOtherChargesHtml = function () {
        $('.ocRemoveBtn').unbind('click');
        $('.ocRemoveBtn').click(function () {
            var $currContent = $(this).closest('div.other-charges-content');
            $currContent.remove();
            //reset number
            var otherChargeLength = $('.other-charges-content').length;
            $('input[name="otherChargeLength"]').val(otherChargeLength);
            $('.other-charges-content').each(function (k,v) {
                $(this).find('select.otherChargesCategory').prop('name','otherChargesCategory'+ k);
                $(this).find('select.otherChargesType').prop('name','otherChargesType'+ k);
                $(this).find('input.otherAmountMin').prop('name','otherAmountMin'+ k);
                $(this).find('input.otherAmountMax').prop('name','otherAmountMax'+ k);
                $(this).find('input.otherRemarks').prop('name','otherRemarks'+ k);
                $(this).find('.isPartEdit').prop('name','otherChargesIsPartEdit'+k);
                $(this).find('.chargesIndexNo').prop('name','otherChargesIndexNo'+k);
            });
            //display add btn
            if (otherChargeLength < '${otherChargesConfig.maximumCount}') {
                $('.addOtherChargesDiv').removeClass('hidden');
            }
            $('#isEditHiddenVal').val('1');
        });
    };

    var doEdite = function () {
        $('a.chargesEdit').click(function () {
            var $currContent = $(this).closest('div.general-charges-content');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');

            $('#isEditHiddenVal').val('1');
        });
    };

    var doOhterChargesEdite = function () {
        $('a.otherChargesEdit').click(function () {
            var $currContent = $(this).closest('div.other-charges-content');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');

            $('#isEditHiddenVal').val('1');
        });
    };


</script>
