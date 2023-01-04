<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:set var="isRfi" value="${not empty requestInformationConfig}"/>
<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && !isRfi}">
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
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${isRfi}">1</c:if>"/>

<div class="vehiclesForm">
    <c:choose>
        <c:when test="${empty vehicleDtoList}">
            <c:set var="pageLength" value="1"/>
        </c:when>
        <c:when test="${vehicleConfigDto.mandatoryCount > vehicleDtoList.size() }">
            <c:set var="pageLength" value="${vehicleConfigDto.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageLength" value="${vehicleDtoList.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="vehiclesLength" value="${pageLength}"/>
    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="vehicleStat">
        <c:set var="vehicleDto" value="${vehicleDtoList[vehicleStat.index]}"/>
        <div class="form-horizontal vehicleContent">
            <input type="hidden" class="isPartEdit" name="isPartEdit${vehicleStat.index}" value="0"/>
            <input type="hidden" class="vehicleIndexNo" name="vehicleIndexNo${vehicleStat.index}" value="${vehicleDto.vehicleIndexNo}"/>
            <iais:row>
                <iais:value width="12" cssClass="col-md-12 col-sm-12 col-xs-12 text-right edit-content">
                    <div class="text-right app-font-size-16">
                        <a class="edit" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                        </a>
                    </div>
                </iais:value>
            </iais:row>
            <div class="col-md-12 col-xs-12">
                <p class="app-title"></p>
            </div>
            <br/>
            <iais:row>
                <iais:value width="6" cssClass="col-md-6">
                    <strong>
                        <c:out value="${singleName}"/>
                        <label class="assign-psn-item"><c:if test="${vehicleDtoList.size() > 1}">${vehicleStat.index+1}</c:if></label>
                    </strong>
                </iais:value>
                <iais:value width="6" cssClass="col-md-6 text-right vehicleRemoveBtn">
                    <h4 class="text-danger">
                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                    </h4>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" value="Vehicle number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="vehicleName" maxLength="10" type="text" name="vehicleName${vehicleStat.index}"
                                value="${vehicleDto.dummyVehNum? '' : vehicleDto.displayName}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" mandatory="true" value="Chassis number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="chassisNum" maxLength="25" type="text" name="chassisNum${vehicleStat.index}"
                                value="${vehicleDto.chassisNum}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" mandatory="true" value="Engine number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="engineNum" maxLength="25" type="text" name="engineNum${vehicleStat.index}"
                                value="${vehicleDto.engineNum}"/>
                </iais:value>
            </iais:row>
            <hr>
        </div>
    </c:forEach>

    <c:choose>
        <c:when test="${!empty vehicleDtoList}">
            <c:set var="vehicleLength" value="${vehicleDtoList.size()}"/>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${AppSubmissionDto.needEditController}">
                    <c:set var="vehicleLength" value="0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="vehicleLength" value="${vehicleConfigDto.mandatoryCount}"/>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <c:set var="needAddPsn" value="true"/>
    <c:choose>
        <c:when test="${vehicleConfigDto.status =='CMSTAT003'}">
            <c:set var="needAddPsn" value="false"/>
        </c:when>
        <c:when test="${vehicleLength >= vehicleConfigDto.maximumCount}">
            <c:set var="needAddPsn" value="false"/>
        </c:when>
        <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
            <c:set var="needAddPsn" value="false"/>
        </c:when>
    </c:choose>
    <div class="col-md-12 col-xs-12 addVehicleDiv <c:if test="${!needAddPsn}">hidden</c:if>" style="padding-left: 0px">
        <span class="addVehicleBtn" style="color:deepskyblue;cursor:pointer;">
            <span>+ Add Another Vehicle</span>
        </span>
    </div>
</div>
<iais:confirm msg="NEW_ACK031" needCancel="false" callBack="$('#support').modal('hide')" popupOrder="support"/>

<script>
    $(document).ready(function () {
        addVehicle();
        removeVehicle();
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //rfc,renew,rfi
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disableContent($('.vehiclesForm'));
            doEdit();
            doEdite();
        }
        $('div.vehicleContent').each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(this).not(':empty')) {
                        $(v).find('.isPartEdit').val(1);
                        $('#isEditHiddenVal').val('1');
                        unDisableContent($(v));
                    }
                });
            }
        });
        refreshVehicle();
    });

    var addVehicle = function(){
        $('.addVehicleBtn').unbind('click');
        $('.addVehicleBtn').click(function () {
            showWaiting();
            var $target = $('div.vehicleContent:first');
            var src = $target.clone();
            clearFields(src);
            $('.addVehicleDiv').before(src);
            var $currContent = $('div.vehicleContent').last();
            $currContent.find('.isPartEdit').val(1);
            unDisableContent($currContent);
            removeVehicle();
            refreshVehicle();
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        });
    }

    var removeVehicle = function () {
        const $removeBtn = $('.removeBtn');
        $removeBtn.unbind('click');
        $removeBtn.click(function () {
            showWaiting();
            var vehicleLength = $('.vehicleContent').length;
            if (vehicleLength == 1) {
                dismissWaiting();
                $('#support').modal('show');
                return;
            }
            if (vehicleLength <= '${vehicleConfigDto.mandatoryCount}') {
                dismissWaiting();
                return;
            }
            var $currVehicleContent = $(this).closest('div.vehicleContent');
            $currVehicleContent.remove();
            $('#isEditHiddenVal').val('1');
            refreshVehicle();
            dismissWaiting();
        });
    }

    function refreshVehicle() {
        var vehicleLength = $('.vehicleContent').length;
        $('input[name="vehiclesLength"]').val(vehicleLength);
        if (vehicleLength == 1) {
            $('.vehicleRemoveBtn').show();
        } else if (vehicleLength <= '${vehicleConfigDto.mandatoryCount}') {
            $('.vehicleRemoveBtn').hide();
        } else {
            $('.vehicleRemoveBtn').show();
        }
        //reset number
        $('div.vehicleContent').each(function (k) {
            $(this).find('.assign-psn-item').html(k + 1);
            $(this).find('.vehicleName').prop('name', 'vehicleName' + k);
            $(this).find('.chassisNum').prop('name', 'chassisNum' + k);
            $(this).find('.engineNum').prop('name', 'engineNum' + k);
            $(this).find('.isPartEdit').prop('name', 'isPartEdit' + k);
            $(this).find('.vehicleIndexNo').prop('name', 'vehicleIndexNo' + k);
        });
        // display add more
        if (vehicleLength < '${vehicleConfigDto.maximumCount}') {
            $('.addVehicleDiv').removeClass('hidden');
        } else {//hidden add more
            $('.addVehicleDiv').addClass('hidden');
        }
        if (vehicleLength <= 1) {
            $('.vehicleContent:eq(0) .assign-psn-item').html('');
        }
    }

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }

    var doEdit = function () {
        $('a.edit').click(function () {
            var $currContent = $(this).closest('div.vehicleContent');
            $currContent.find('input.isPartEdit').val('1');
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
            hideTag($currContent.find('a.edit'));
        });
    }

    var doEdite = function () {
        var rfiObj = $('input[name="rfiObj"]').val();
        if ('1' == rfiObj){
            return;
        }
        $('.vehicleContent').each(function (){
            var $vehicleContent = $(this);
            $vehicleContent.find('input[type="text"]').each(function (){
                var $input = $(this);
                if ($input.is(':visible')) {
                    var v = $input.val();
                    if (v == null || v == 'undefined' || v == ''){
                        $vehicleContent.find('input.isPartEdit').val('1');
                        $vehicleContent.find('.edit-content').addClass('hidden');
                        $vehicleContent.find('input[type="text"]').prop('disabled', false);
                        $vehicleContent.find('div.nice-select').removeClass('disabled');
                        $vehicleContent.find('input[type="text"]').css('border-color', '');
                        $vehicleContent.find('input[type="text"]').css('color', '');
                        $('#isEditHiddenVal').val('1');
                    }
                }
            });
        });
    }

</script>