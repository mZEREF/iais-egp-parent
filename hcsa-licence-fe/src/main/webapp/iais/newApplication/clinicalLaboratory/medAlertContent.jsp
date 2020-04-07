<style>
    .cursorPointer{
        cursor: pointer;
    }
</style>
<div class="row">
    <div class="col-xs-12">
        <h2>MedAlert Person</h2>
        <br/>
        <div class="medAlertContent">
        </div>
        <c:choose>
            <c:when test="${AppSvcMedAlertPsn.size()>mandatoryCount}">
                <c:set var="pageLength" value="${AppSvcMedAlertPsn.size()}"/>
            </c:when>
            <c:otherwise>
                <c:set var="pageLength" value="${mandatoryCount}"/>
            </c:otherwise>
        </c:choose>
        <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="status">
            <c:set var="medAlertPsn" value="${AppSvcMedAlertPsn[status.index]}"/>
            <div class="medAlertContent">
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-5">
                                <div class="cgo-header">
                                    <strong>MedAlert Person <label class="assign-psn-item">${status.index+1}</label></strong>
                                </div>
                            </div>
                            <div class="col-sm-5 col-md-7 text-right">
                                <c:if test="${status.index - mandatoryCount >=0}">
                                    <h4 class="text-danger"><em class="fa fa-times-circle mapDelBtn cursorPointer"></em></h4>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-6 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Assign a MedAlert Person</label>
                                </div>
                                <div class="col-sm-5 col-md-7" id="assignSelect">
                                    <div class="">
                                        <iais:select cssClass="assignSel"  name="assignSel" options="MedAlertAssignSelect"  value="${medAlertPsn.assignSelect}" ></iais:select>
                                        <span id="error_assignSelect${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="medAlertPerson hidden">
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Name</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-3">
                                    <iais:select cssClass="salutation"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${medAlertPsn.salutation}" firstOption="Please Select"></iais:select>
                                    <span class="error-msg" id="error_salutation${status.index}" name="iaisErrorMsg"></span>
                                </div>

                                <div class="col-sm-4">
                                    <iais:input maxLength="66" type="text" name="name" value="${medAlertPsn.name}"></iais:input>
                                    <span class="error-msg" id="error_name${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                                        <span class="mandatory">*</span>
                                    </label>
                                </div>
                                <div class="col-sm-3">
                                    <div class="">
                                        <iais:select cssClass="idType"  name="idType"  value="${medAlertPsn.idType}" options="IdTypeSelect"></iais:select>
                                        <span class="error-msg" id="error_idTyp${status.index}" name="iaisErrorMsg"></span>
                                    </div>
                                </div>
                                <div class="col-sm-4">
                                    <iais:input maxLength="9" type="text" name="idNo" value="${medAlertPsn.idNo}"></iais:input>
                                    <span class="error-msg" id="error_idNo${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-4 col-md-7">
                                    <iais:input maxLength="8" type="text" name="mobileNo" value="${medAlertPsn.mobileNo}"></iais:input>
                                    <span class="error-msg" id="error_mobileNo${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Email Address</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-4 col-md-7">
                                    <iais:input maxLength="66" type="text" name="emailAddress" value="${medAlertPsn.emailAddr}"></iais:input>
                                    <span class="error-msg" id="error_emailAddr${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Preferred Mode of Receiving MedAlert</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-3 control-label formtext col-md-7 preferredModeDiv">
                                    <input class="preferredModes" type="hidden" name="preferredModes" value="${medAlertPsn.preferredMode}" />
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input preferredMode" id="EmailCheckbox" type="checkbox" name="preferredMode${status.index}" value = "Email" aria-invalid="false">
                                                <label class="form-check-label" for="EmailCheckbox"><span class="check-square"></span>Email</label>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input preferredMode" id="SMSCheckbox" type="checkbox" name="preferredMode${status.index}" value = "SMS" aria-invalid="false">
                                                <label class="form-check-label" for="SMSCheckbox"><span class="check-square"></span>SMS</label>
                                            </div>
                                        </div>
                                    </div>
                                    <span class="error-msg" id="error_preferredModeVal${status.index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <br/>
            </div>
        </c:forEach>
        <div class="row">
            <div class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-3 control-label formtext col-md-5">
                        <span id="addMapBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another MedAlert Person</span>
                    </div>
                    <div class="col-sm-3 control-label formtext col-md-7">
                        <span class="error-msg mapErrorMsg" style=""></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    $(function () {

        addMAP();

        assignSel();

        mapDel();


        $('select.assignSel').trigger('change');

        //reload preferred Mode
        $('.preferredModes').each(function () {
            var preferredMode = $(this).val();
            if(preferredMode != null && preferredMode !='undefined' && preferredMode != ''){
                var $currentPreModeEle = $(this).closest('div.preferredModeDiv');
                var arr = preferredMode.split(';');
                $.each(arr,function (k,v) {
                    var arrVal = v;
                    $currentPreModeEle.find('input.preferredMode').each(function () {
                        if(arrVal == $(this).val()){
                            $(this).prop('checked',true);
                        }
                    });
                });
            }
        });


    })


    var mapDel = function () {
        $('.mapDelBtn').click(function () {
            $(this).closest('div.medAlertContent').remove();
            changePsnItem();
        });
    }


    var assignSel= function () {

        $('.assignSel').change(function () {
            var assignSelVal = $(this).val();
            var $medAlertContentEle = $(this).closest('div.medAlertContent');
            if('-1' == assignSelVal){
                $medAlertContentEle.find('div.medAlertPerson').addClass('hidden');
            }else if('newOfficer' == assignSelVal){
                $medAlertContentEle.find('div.medAlertPerson').removeClass('hidden');
            }else{

            }

        });
    }


    var addMAP = function(){
        $('#addMapBtn').click(function () {
            var hasNumber = $('.medAlertContent').size() - 1;
            console.log("hasNumber" + hasNumber);
            $.ajax({
                url:'${pageContext.request.contextPath}/med-alert-person-html',
                dataType:'json',
                type:'POST',
                data:{
                    'HasNumber':hasNumber,
                },
                'success':function (data) {
                    if ('success' == data.res) {
                        console.log("suc");
                        $('.medAlertContent:last').after(data.sucInfo);
                        $('.assignSel').unbind();

                        assignSel();

                        mapDel();

                        changePsnItem();
                    }else{
                        $('.mapErrorMsg').html(data.errInfo);
                    }
                },
                error:function (data) {
                    console.log("err");
                }
            });
        });
    }



</script>