<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="AppSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto"--%>
<%--@elvariable id="vehicleDtoList" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto>"--%>
<%--@elvariable id="vehicleConfigDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto"--%>
<%--@elvariable id="requestInformationConfig" type="java.lang.String"--%>
<%--@elvariable id="singleName" type="java.lang.String"--%>
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
<input type="hidden" name="rfiObj"
       value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${isRfi}">1</c:if>"/>

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
            <input type="hidden" class="vehicleIndexNo" name="vehicleIndexNo${vehicleStat.index}"
                   value="${vehicleDto.vehicleIndexNo}"/>

            <div class="col-md-12 col-xs-12">
                <p class="app-title"></p>
            </div>
            <br/>
            <iais:row>
                <iais:value width="6" cssClass="col-md-6">
                    <strong>
                        <c:out value="${singleName}"/>
                        <label class="assign-psn-item"><c:if
                                test="${vehicleDtoList.size() > 1}">${vehicleStat.index+1}</c:if></label>
                    </strong>
                </iais:value>
                <iais:value width="6" cssClass="col-md-6 text-right vehicleRemoveBtn">
                    <h4 class="text-danger">
                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                    </h4>
                </iais:value>

                <iais:value width="12" cssClass="col-md-12 col-sm-12 col-xs-12 text-right edit-content">
                    <div class="text-right app-font-size-16">
                        <a class="editBtn" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                        </a>
                    </div>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" value="Vehicle Number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="vehicleName" maxLength="10" type="text" name="vehicleName${vehicleStat.index}"
                                value="${vehicleDto.dummyVehNum? '' : vehicleDto.displayName}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" mandatory="true" value="Chassis Number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="chassisNum" maxLength="25" type="text" name="chassisNum${vehicleStat.index}"
                                value="${vehicleDto.chassisNum}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" mandatory="true" value="Engine Number" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input cssClass="engineNum" maxLength="25" type="text" name="engineNum${vehicleStat.index}"
                                value="${vehicleDto.engineNum}"/>
                </iais:value>
            </iais:row>

            <div class="col-md-12 col-xs-12">
                <hr>
            </div>
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
    <div class="col-md-12 col-xs-12 addVehicleDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addVehicleBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Vehicle</span>
            </span>
    </div>

</div>
<iais:confirm msg="NEW_ACK031" needCancel="false" callBack="$('#support').modal('hide')" popupOrder="support"/>


<script>
    $(document).ready(function () {
        addVehicle();
        removeVehicle();
        disabledPage();
        refreshVehicle();
        $('.editBtn').click(doEdit);
    });

    var addVehicle = function () {
        $('.addVehicleBtn').click(function () {
            showWaiting();
            const vehicleLength = $('.vehicleContent').length;
            $.ajax({
                url: '${pageContext.request.contextPath}/vehicle-html',
                dataType: 'json',
                data: {
                    "vehicleLength": vehicleLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addVehicleDiv').before(data.resultJson + '');
                        removeVehicle();//bind event
                        refreshVehicle();
                        $('#isEditHiddenVal').val('1');
                    }
                    dismissWaiting();
                },
                error: function () {
                    console.log("err");
                    dismissWaiting();
                }
            });

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

    function doEdit(){
        const $currContent = $(this).closest('div.vehicleContent');
        unDisabledPartPage($currContent);
        $currContent.find('.edit-content').hide();
        $currContent.find('input.isPartEdit').val('1');
        $('#isEditHiddenVal').val('1');
    }

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }

</script>