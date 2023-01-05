<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/4/22
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>

<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
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
<input type="hidden"  name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="panel-group chargesContent" id="accordion" role="tablist" aria-multiselectable="true">
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
                        <c:when test="${empty generalChargesDtoList && generalChargesConfig.mandatoryCount > 0}">
                            <c:set var="pageLength" value="${generalChargesConfig.mandatoryCount}"/>
                        </c:when>
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
                    <input type="hidden" name="rfcChargeEdit" class="rfcChargeEdit" value="">
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
                                    <div class=" form-group form-horizontal formgap copy">
                                        <div class="control-label formtext col-md-5 col-xs-5">
                                            <iais:select cssClass="chargesType"  name="chargesType${gcStat.index}" codeCategory="CATE_ID_GENERAL_CONVEYANCE_CHARGES_TYPE" value="${generalChargesDto.chargesType}" firstOption="Please Select" />
                                        </div>
                                        <div class="control-label formtext col-md-4 col-xs-4">
                                            <div class="row">
                                                <div class="col-xs-5 col-md-5">
                                                    <iais:input maxLength="4" type="text" cssClass="minAmount" name="minAmount${gcStat.index}" value="${generalChargesDto.minAmount}"/>
                                                </div>
                                                <div class="col-xs-2 col-md-2 text-center">
                                                    <label  class="control-label control-set-font control-font-label">To</label>
                                                </div>
                                                <div class="col-xs-5 col-md-5">
                                                    <iais:input maxLength="4" type="text" cssClass="maxAmount" name="maxAmount${gcStat.index}" value="${generalChargesDto.maxAmount}"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="control-label formtext col-md-2 col-xs-2">
                                            <iais:input maxLength="150" type="text" cssClass="remarks" name="remarks${gcStat.index}" value="${generalChargesDto.remarks}"/>
                                        </div>
                                        <div class="control-label formtext col-md-1 col-xs-1 general-remove removeBtn <c:if test="${gcStat.first}">hidden</c:if>">
                                            <c:if test="${!isRfi}">
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
                    <c:if test="${!isRfi}">
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
                        <c:if test="${!isRfi}">
                            <div class="col-md-12 col-xs-12 addGeneralChargesDiv <c:if test="${!needAddPsn}">hidden</c:if>">
                            <span class="addGeneralChargesBtn" style="color:deepskyblue;cursor:pointer;">
                                <span style="">+ Add other conveyance related charges</span>
                            </span>
                            </div>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="panel-group chargesContent" id="accordion" role="tablist" aria-multiselectable="true">
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
                            <c:when test="${empty otherChargesDtoList && otherChargesConfig.mandatoryCount > 0}">
                                <c:set var="pageLength" value="${otherChargesConfig.mandatoryCount}"/>
                            </c:when>
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
                        <input type="hidden" name="rfcOtherChargeEdit" class="rfcOtherChargeEdit" value="">
                        <c:forEach begin="0" end="${otherChargesLength-1}" step="1" varStatus="ocStat">
                            <c:set var="otherChargesDto" value="${otherChargesDtoList[ocStat.index]}"/>
                            <div class="others-charges-content charges-content">
                                <input type="hidden" class ="otherChargesIsPartEdit" name="otherChargesIsPartEdit${ocStat.index}" value="0"/>
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
                                            <div class="control-label formtext col-md-1 col-xs-1 other-remove removeBtn">
                                                <c:if test="${!isRfi}">
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
                            <c:if test="${!isRfi}">
                                <div class="col-md-12 col-xs-12 addOtherChargesDiv <c:if test="${!needAddPsn}">hidden</c:if>">
                                <span class="addOtherChargesBtn" style="color:deepskyblue;cursor:pointer;">
                                    <span style="">+ Add Medical Equipment and Other Charges</span>
                                </span>
                                </div>
                            </c:if>
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
        removeGeneralChargesEvent();
        removeOtherChargesHtml();
        refreshOther();
        refreshGeneralCharges();
        addGeneralChargesEvent();
        addOtherChargesEvent();

        doEditChargesEvent();
        doEditOtherChargesEvent();
        //rfc,renew,rfi
        <c:if test="${AppSubmissionDto.needEditController}">
        disableChargesContent();
        </c:if>
    });

    let searchChargesTypeByCategory = function () {
        $('select.otherChargesCategory').unbind('change');
        $('select.otherChargesCategory').change(function () {
            let thisVal = $(this).val();
            let $currContent = $(this).closest('.other-charges-div');
            let chargesSuffix = $currContent.find('.currChargesSuffix').val();
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
                            refreshOther();
                        });
                    }
                },
                error: function (data) {
                }
            });
            dismissWaiting();
        });
    };

    function addGeneralChargesEvent() {
        $('.addGeneralChargesBtn').unbind('click');
        $('.addGeneralChargesBtn').on('click', function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.chargesEdit').trigger('click');
            }
            let target = $('div.general-charges-content:first');
            let src = target.clone();
            $('div.addGeneralChargesDiv').before(src);
            $(".generalChargeLength").attr("value", $('.general-charges-content').length + 1);
            clearFields($('div.general-charges-content:last'));
            removeGeneralChargesEvent();
            refreshGeneralCharges();
            let rfcEdit = $('input.rfcChargeEdit').val();
            console.log("addRfcEdit:"+rfcEdit);
            if (!isEmpty(rfcEdit) && 'doChargeEdit' == rfcEdit){
                $('input.isPartEdit').val('1');
            }
            dismissWaiting();
        })
    }

    function refreshGeneralCharges() {
        let generalChargeLength = $('.general-charges-content').length;
        $('input[name="generalChargeLength"]').val(generalChargeLength);
        if (generalChargeLength <= '${generalChargesConfig.mandatoryCount}') {
            $('.general-remove').hide();
        } else {
            $('.general-remove').show();
        }
        $('.general-charges-content').each(function (k, v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
            $(this).find('select.chargesType').prop('name', 'chargesType' + k);
            $(this).find('select.chargesType').prop('id', 'chargesType' + k);
            $(this).find('input.minAmount').prop('name', 'minAmount' + k);
            $(this).find('input.maxAmount').prop('name', 'maxAmount' + k);
            $(this).find('input.remarks').prop('name', 'remarks' + k);
            $(this).find('.isPartEdit').prop('name', 'isPartEdit' + k);
            $(this).find('.chargesIndexNo').prop('name', 'chargesIndexNo' + k);
            <c:if test="${AppSubmissionDto.appType == 'APTY002'}" >
            if (k < '${generalChargesConfig.mandatoryCount}') {
                $(this).find('.general-remove').hide();
            }
            </c:if>
        });
        refreshGeneralAddBtn();
        $('#isEditHiddenVal').val('1');
    }

    function addOtherChargesEvent(){
        let otherChargeLength = $('.others-charges-content').length;
        refreshOther();
        $('.addOtherChargesBtn').unbind('click');
        $('.addOtherChargesBtn').on('click', function () {
            showWaiting();
            if (${AppSubmissionDto.needEditController }){
                $('a.otherChargesEdit').trigger('click');
            }
            let target = $('div.others-charges-content:first');
            let src = target.clone();
            $('div.addOtherChargesDiv').before(src);
            $(".otherChargeLength").attr("value",)
            otherChargeLength = otherChargeLength+1;
            $(".otherChargeLength").attr("value",otherChargeLength);
            clearFields($('div.others-charges-content:last'));
            removeOtherChargesHtml();
            searchChargesTypeByCategory();
            refreshOther();
            let rfcEdit = $('input.rfcOtherChargeEdit').val();
            console.log("addRfcEdit:"+rfcEdit);
            if (!isEmpty(rfcEdit) && 'doOtherChargeEdit' == rfcEdit){
                $('input.otherChargesIsPartEdit').val('1');
            }
            dismissWaiting();
        })

    }

    function refreshOther(){
        let otherChargeLength = $('.others-charges-content').length;
        $('input[name="otherChargeLength"]').val(otherChargeLength);
        if (otherChargeLength <= '${otherChargesConfig.mandatoryCount}') {
            $('.other-remove').hide();
        } else {
            $('.other-remove').show();
        }
        $('.others-charges-content').each(function (k,v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
            $(this).find('select.otherChargesCategory').prop('name','otherChargesCategory'+ k);
            $(this).find('select.otherChargesType').prop('name','otherChargesType'+ k);
            $(this).find('input.otherAmountMin').prop('name','otherAmountMin'+ k);
            $(this).find('input.otherAmountMax').prop('name','otherAmountMax'+ k);
            $(this).find('input.otherRemarks').prop('name','otherRemarks'+ k);
            $(this).find('.otherChargesIsPartEdit').prop('name','otherChargesIsPartEdit'+k);
            $(this).find('.chargesIndexNo').prop('name','otherChargesIndexNo'+k);
            <c:if test="${AppSubmissionDto.appType == 'APTY002'}" >
            if (k < '${otherChargesConfig.mandatoryCount}') {
                $(this).find('.other-remove').hide();
            }
            </c:if>
        });
        //display add btn
        refreshOtherAddBtn();
        $('#isEditHiddenVal').val('1');
    }

    let removeGeneralChargesEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            let $currContent = $(this).closest('div.general-charges-content');
            $currContent.remove();
            //reset number
            let generalChargeLength = $('.general-charges-content').length;
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
            refreshGeneralAddBtn();
            $('#isEditHiddenVal').val('1');
        });
    };


    let removeOtherChargesHtml = function () {
        $('.ocRemoveBtn').unbind('click');
        $('.ocRemoveBtn').click(function () {
            let $currContent = $(this).closest('div.others-charges-content');
            $currContent.remove();
            //reset number
            let otherChargeLength = $('.others-charges-content').length;
            $('input[name="otherChargeLength"]').val(otherChargeLength);
            $('.others-charges-content').each(function (k,v) {
                $(this).find('select.otherChargesCategory').prop('name','otherChargesCategory'+ k);
                $(this).find('select.otherChargesType').prop('name','otherChargesType'+ k);
                $(this).find('input.otherAmountMin').prop('name','otherAmountMin'+ k);
                $(this).find('input.otherAmountMax').prop('name','otherAmountMax'+ k);
                $(this).find('input.otherRemarks').prop('name','otherRemarks'+ k);
                $(this).find('.otherChargesIsPartEdit').prop('name','otherChargesIsPartEdit'+k);
                $(this).find('.chargesIndexNo').prop('name','otherChargesIndexNo'+k);
            });
            //display add btn
            refreshOtherAddBtn();
            $('#isEditHiddenVal').val('1');
        });
    };

    function refreshGeneralAddBtn() {
        toggleTag('.addGeneralChargesDiv', $('.general-charges-content').length < '${generalChargesConfig.maximumCount}');
    }

    function refreshOtherAddBtn() {
        toggleTag('.addOtherChargesDiv', $('.others-charges-content').length < '${otherChargesConfig.maximumCount}');
    }

    let doEditChargesEvent = function () {
        $('a.chargesEdit').click(function () {
            var $currContent = $(this).closest('div.general-charges-content');
            $('input.rfcChargeEdit').val('doChargeEdit');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    };

    let doEditOtherChargesEvent = function () {
        $('a.otherChargesEdit').click(function () {
            var $currContent = $(this).closest('div.others-charges-content');
            $('input.rfcOtherChargeEdit').val('doOtherChargeEdit');
            $currContent.find('input.otherChargesIsPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    };

    function disableChargesContent() {
        // edit btn
        let $currContent = $('.chargesContent');
        let $target= $currContent.find('.chargesEdit');
        if (!isEmptyNode($target)) {
            showTag($currContent.find('.general-remove:not(:first)'));
            refreshGeneralAddBtn()
        } else {
            hideTag($currContent.find('.general-remove'));
            hideTag($currContent.find('.addGeneralChargesDiv'));
        }
        $target= $currContent.find('.otherChargesEdit');
        if (!isEmptyNode($target)) {
            showTag($currContent.find('.other-remove:not(:first)'));
            refreshOtherAddBtn();
        } else {
            hideTag($currContent.find('.other-remove'));
            hideTag($currContent.find('.addOtherChargesDiv'));
        }
        disableContent($currContent);
    }
</script>
