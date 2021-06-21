<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip">Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em></a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </div>
</div>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

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
                <div class="edit-content">
                    <c:if test="${'true' == canEdit}">
                        <p>
                        <div class="text-right app-font-size-16">
                            <a class="edit vehicleEdit"><em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit</a>
                        </div>
                        </p>
                    </c:if>
                </div>
            </div>


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
                                    <strong>Vehicle <label class="assign-psn-item">${vehicleStat.index+1}</label></strong>
                                </div>
                            </label>
                        </div>

                        <div class="col-md-7 col-xs-7 text-right">
                            <c:if test="${vehicleStat.index - vehicleConfigDto.mandatoryCount >=0}">
                                <div class="">
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
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input cssClass="vehicleName" maxLength="10" type="text" name="vehicleName${vehicleStat.index}" value="${vehicleDto.vehicleName}"></iais:input>
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



    <c:if test="${ requestInformationConfig==null}">
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
        </c:choose>
        <div class="col-md-12 col-xs-12 addVehicleDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addVehicleBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Vehicle</span>
            </span>
        </div>
    </c:if>

</div>

<script>
    $(document).ready(function () {
        addVehicle();
        removeVehicle();
        doEdite();

        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //rfc,renew,rfi
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
        }


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
                        //
                        removeVehicle();

                        var vehicleLength = $('.vehicleContent').length;
                        $('input[name="vehiclesLength"]').val(vehicleLength);
                        //hidden add more
                        if (vehicleLength >= '${vehicleConfigDto.maximumCount}') {
                            $('.addVehicleDiv').addClass('hidden');
                        }
                        if(vehicleLength <= '${vehicleConfigDto.mandatoryCount}'){
                            //remove del btn for mandatory count

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
    }

    var removeVehicle = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            var $currVehicleContent = $(this).closest('div.vehicleContent');
            $currVehicleContent.remove();
            var vehicleLength = $('.vehicleContent').length;
            $('input[name="vehiclesLength"]').val(vehicleLength);
            //reset number
            $('div.vehicleContent').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k+1);
                $(this).find('.vehicleName').prop('name','vehicleName'+k);
                $(this).find('.chassisNum').prop('name','chassisNum'+k);
                $(this).find('.engineNum').prop('name','engineNum'+k);
                $(this).find('.isPartEdit').prop('name','isPartEdit'+k);
                $(this).find('.vehicleIndexNo').prop('name','vehicleIndexNo'+k);
            });
            //display add more
            if (vehicleLength < '${vehicleConfigDto.maximumCount}') {
                $('.addVehicleDiv').removeClass('hidden');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

    var doEdite = function () {
        $('a.vehicleEdit').click(function () {
            var $currContent = $(this).closest('div.vehicleContent');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');

            $('#isEditHiddenVal').val('1');
        });
    }

</script>