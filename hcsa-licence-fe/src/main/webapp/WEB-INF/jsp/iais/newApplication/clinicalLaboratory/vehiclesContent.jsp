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

<c:set var="vehicleDtoList" value="${vehicleDtoList}"/>
<div class="row vehiclesForm">
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
    <input type="hidden" name="vehiclesLength" value="${pageLength}" />
    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="vehicleStat">
        <c:set var="vehicleDto" value="${vehicleDtoList[vehicleStat.index]}"/>
        <div class="vehicleContent">
            <input type="hidden" class ="isPartEdit" name="isPartEdit${vehicleStat.index}" value="0"/>
            <input type="hidden" class="vehicleIndexNo" name="vehicleIndexNo${vehicleStat.index}" value="${vehicleDto.vehicleIndexNo}"/>

            <div class="col-md-12 col-xs-12">
                <p style="font-weight: 600;font-size: 2.2rem"></p>
            </div>
            <br/>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">
                                <div class="cgo-header">
                                    <strong>Vehicle <label class="assign-psn-item"><c:if test="${vehicleDtoList.size() > 1}">${vehicleStat.index+1}</c:if></label></strong>
                                </div>
                            </label>
                        </div>

                        <div class="col-md-7 col-xs-7 text-right ">
                            <c:if test="${!isRfi}"><%-- && (vehicleStat.index - vehicleConfigDto.mandatoryCount >= 0)--%>
                                <div class="vehicleRemoveBtn">
                                    <h4 class="text-danger">
                                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                    </h4>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Vehicle Number</label>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input cssClass="vehicleName" maxLength="10" type="text" name="vehicleName${vehicleStat.index}" value="${vehicleDto.dummyVehNum? '' : vehicleDto.displayName}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Chassis Number</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input cssClass="chassisNum" maxLength="25" type="text" name="chassisNum${vehicleStat.index}" value="${vehicleDto.chassisNum}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Engine Number</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input cssClass="engineNum" maxLength="25" type="text" name="engineNum${vehicleStat.index}" value="${vehicleDto.engineNum}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <hr>
            </div>
        </div>
    </c:forEach>



    <c:if test="${!isRfi}">
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
        <div class="col-md-12 col-xs-12 addVehicleDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addVehicleBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Vehicle</span>
            </span>
        </div>
    </c:if>

</div>
<iais:confirm msg="NEW_ACK031"  needCancel="false" callBack="tagConfirmCallbacksupport()" popupOrder="support" ></iais:confirm>
<script>
    $(document).ready(function () {
        addVehicle();
        removeVehicle();

        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //rfc,renew,rfi
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
            doEdite();
        }
        refreshVehicle();
    });

    var addVehicle = function(){
        $('.addVehicleBtn').click(function () {
            showWaiting();
            var vehicleLength = $('.vehicleContent').length;
            $.ajax({
                url: '${pageContext.request.contextPath}/vehicle-html',
                dataType: 'json',
                data: {
                    "vehicleLength": vehicleLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addVehicleDiv').before(data.resultJson+'');
                        removeVehicle();//bind event
                        refreshVehicle();
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
    }

    var removeVehicle = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            showWaiting();
            var vehicleLength = $('.vehicleContent').length;
            if (vehicleLength == 1){
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
        if (vehicleLength == 1){
            $('.vehicleRemoveBtn').show();
        } else if (vehicleLength <= '${vehicleConfigDto.mandatoryCount}') {
            $('.vehicleRemoveBtn').hide();
        } else {
            $('.vehicleRemoveBtn').show();
        }
        //reset number
        $('div.vehicleContent').each(function (k,v) {
            $(this).find('.assign-psn-item').html(k+1);
            $(this).find('.vehicleName').prop('name','vehicleName'+k);
            $(this).find('.chassisNum').prop('name','chassisNum'+k);
            $(this).find('.engineNum').prop('name','engineNum'+k);
            $(this).find('.isPartEdit').prop('name','isPartEdit'+k);
            $(this).find('.vehicleIndexNo').prop('name','vehicleIndexNo'+k);
            <c:if test="${AppSubmissionDto.appType == 'APTY002'}" >
            if (k == 0) {
                $(this).find('.vehicleRemoveBtn').hide();
            }
            </c:if>
        });
        <c:if test="${AppSubmissionDto.appType == 'APTY002' || 'true' == canEdit}">
        // display add more
        if (vehicleLength < '${vehicleConfigDto.maximumCount}') {
            $('.addVehicleDiv').removeClass('hidden');
        }else{//hidden add more
            $('.addVehicleDiv').addClass('hidden');
        }
        </c:if>
        if (vehicleLength <= 1) {
            $('.vehicleContent:eq(0) .assign-psn-item').html('');
        }
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

    function tagConfirmCallbacksupport(){
        $('#support').modal('hide');
    }

</script>