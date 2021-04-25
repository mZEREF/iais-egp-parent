<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/4/19
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<c:set var="clinicalDirectorDtoList" value="${clinicalDirectorDtoList}"/>
<div class="row cdForm">
    <div class="col-md-12 col-xs-12">
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">
                        <div style="font-weight: 600;font-size: 2.2rem">
                            <strong>Clinical Director</strong>
                        </div>
                    </label>
                </div>
            </div>
        </div>
    </div>


    <c:choose>
        <c:when test="${empty clinicalDirectorDtoList}">
            <c:set var="pageLength" value="1"/>
        </c:when>
        <c:when test="${clinicalDirectorConfig.mandatoryCount > clinicalDirectorDtoList.size() }">
            <c:set var="pageLength" value="${clinicalDirectorConfig.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageLength" value="${clinicalDirectorDtoList.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="cdLength" value="${pageLength}" />
    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="cdStat">
        <c:set var="clinicalDirectorDto" value="${clinicalDirectorDtoList[cdStat.index]}"/>
        <div class="clinicalDirectorContent">
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">
                                <div class="cgo-header">
                                    <strong>Clinical Director <label class="assign-psn-item">${cdStat.index+1}</label></strong>
                                </div>
                            </label>
                        </div>

                        <div class="col-md-7 col-xs-7 text-right">
                            <c:if test="${cdStat.index - clinicalDirectorConfig.mandatoryCount >=0}">
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
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Professional Board</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:select cssClass="professionBoard"  name="professionBoard${cdStat.index}" codeCategory="CATE_ID_PROFESSION_BOARD" value="${clinicalDirectorDto.professionBoard}" firstOption="Please Select"></iais:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Profession Regn No.</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="profRegNo${cdStat.index}" value="${clinicalDirectorDto.profRegNo}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${'MTS' == currentSvcCode}">
                <div class="col-md-12 col-xs-12">
                    <div class="row control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap noRegWithProfBoardDiv">
                            <div class="control-label formtext col-md-5 col-xs-5">
                                <label  class="control-label control-set-font control-font-label">Not registered with a Professional Board</label>
                            </div>
                            <div class="control-label formtext col-md-7 col-xs-7 noRegWithProfBoardDiv">
                                <label  class="control-label control-set-font control-font-label">
                                    <input type="hidden" class="noRegWithProfBoardVal" name="noRegWithProfBoardVal${cdStat.index}" value="${clinicalDirectorDto.noRegWithProfBoard}"/>
                                    <input type="checkbox" <c:if test="${'1' == clinicalDirectorDto.noRegWithProfBoard}">checked="checked"</c:if>  name="noRegWithProfBoard" class="control-input noRegWithProfBoard" value="1" >
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="row control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="control-label formtext col-md-5 col-xs-5">
                                <label  class="control-label control-set-font control-font-label">Years of experience in patient transport</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="col-md-2 col-xs-12">
                                <iais:input maxLength="2" type="text" cssClass="transportYear" name="transportYear${cdStat.index}" value="${clinicalDirectorDto.transportYear}"></iais:input>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Name</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <iais:select cssClass="salutation"  name="salutation${cdStat.index}" codeCategory="CATE_ID_SALUTATION" value="${clinicalDirectorDto.salutation}" firstOption="Please Select"></iais:select>
                        </div>
                        <div class="col-sm-4 col-xs-12">
                            <iais:input maxLength="66" type="text" cssClass="name" name="name${cdStat.index}" value="${clinicalDirectorDto.name}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">ID No.</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-3 col-xs-12">
                            <div class="">
                                <iais:select cssClass="idType"  name="idType${cdStat.index}" needSort="false" value="${clinicalDirectorDto.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-12">
                            <iais:input cssClass="idNo" maxLength="9" type="text" name="idNo${cdStat.index}" value="${clinicalDirectorDto.idNo}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Designation</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12" id="designation">
                            <iais:select cssClass="designation" name="designation${cdStat.index}" codeCategory="CATE_ID_DESIGNATION" value="${clinicalDirectorDto.designation}" firstOption="Please Select"></iais:select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Specialty</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12" >
                            <iais:select cssClass="specialty" name="specialty${cdStat.index}" options="easMtsSpecialtySelectList" needSort="false" value="${clinicalDirectorDto.specialty}"></iais:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12 otherSpecialtyDiv hidden">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                        </div>
                        <div class="col-md-7 col-xs-12" >
                            <iais:input maxLength="100" type="text" cssClass="otherSpecialty" name="otherSpecialty${cdStat.index}" value="${clinicalDirectorDto.otherSpecialty}" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Date when specialty was gotten</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-3 col-xs-12">
                            <iais:datePicker cssClass="specialtyGetDate" name="specialtyGetDate${cdStat.index}" value="${clinicalDirectorDto.specialtyGetDateStr}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Type of Current Registration</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="typeOfCurrRegi${cdStat.index}" value="${clinicalDirectorDto.typeOfCurrRegi}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Current Registration Date</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-3 col-xs-12">
                            <iais:datePicker cssClass="currRegiDate" name="currRegiDate${cdStat.index}" value="${clinicalDirectorDto.currRegiDateStr}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Practicing Certificate End Date</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-3 col-xs-12">
                            <iais:datePicker cssClass="praCerEndDate" name="praCerEndDate${cdStat.index}" value="${clinicalDirectorDto.praCerEndDateStr}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Type of Register</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="typeOfRegister${cdStat.index}" value="${clinicalDirectorDto.typeOfRegister}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Relevant Experience</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="180" type="text" cssClass="relevantExperience" name="relevantExperience${cdStat.index}" value="${clinicalDirectorDto.relevantExperience}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>


            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap holdCerByEMSDiv">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Clinical Director (CD) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop&nbsp;<span class="mandatory">*</span></label>
                        </div>
                        <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal${cdStat.index}" value="${clinicalDirectorDto.holdCerByEMS}"/>
                        <div class="form-check col-md-3 col-xs-3">
                            <input class="form-check-input holdCerByEMS" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${cdStat.index}" value = "1" aria-invalid="false">
                            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                        </div>
                        <div class="form-check col-md-3 col-xs-3">
                            <input class="form-check-input holdCerByEMS" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${cdStat.index}" value = "0" aria-invalid="false">
                            <label class="form-check-label" ><span class="check-circle"></span>No</label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">ACLS Expiry Date</label>
                            <c:if test="${'EAS' == currentSvcCode}">
                                <span class="mandatory">*</span>
                            </c:if>
                        </div>
                        <div class="col-md-3 col-xs-12">
                            <iais:datePicker cssClass="aclsExpiryDate" name="aclsExpiryDate${cdStat.index}" value="${clinicalDirectorDto.aclsExpiryDateStr}" />
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${'MTS' == currentSvcCode}">
                <div class="col-md-12 col-xs-12">
                    <div class="row control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="control-label formtext col-md-5 col-xs-5">
                                <label  class="control-label control-set-font control-font-label">BCLS and AED Expiry Date</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="col-md-3 col-xs-12">
                                <iais:datePicker cssClass="bclsExpiryDate" name="bclsExpiryDate${cdStat.index}" value="${clinicalDirectorDto.bclsExpiryDateStr}" />
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Mobile No</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="8" cssClass="mobileNo" type="text" name="mobileNo${cdStat.index}" value="${clinicalDirectorDto.mobileNo}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">Email Address</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-md-7 col-xs-12">
                            <iais:input maxLength="66" type="text" cssClass="emailAddr" name="emailAddr${cdStat.index}" value="${clinicalDirectorDto.emailAddr}"></iais:input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 col-xs-12">
                <hr/>
            </div>
        </div>
    </c:forEach>

    <c:if test="${ requestInformationConfig==null}">
        <c:choose>
            <c:when test="${!empty clinicalDirectorDtoList}">
                <c:set var="cdLength" value="${clinicalDirectorDtoList.size()}"/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${AppSubmissionDto.needEditController}">
                        <c:set var="cdLength" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cdLength" value="${clinicalDirectorConfig.mandatoryCount}"/>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${clinicalDirectorConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${cdLength >= clinicalDirectorConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Clinical Director</span>
            </span>
        </div>
    </c:if>
</div>

<script>
    $(document).ready(function () {

        holdCerByEMS();
        noRegWithProfBoard();
        addClinicalDirectorBtn();
        removeClinicalDirector();
        showOtherSpecialty();

        $('select.specialty').trigger('change');
    });

    var holdCerByEMS = function() {
        $('.holdCerByEMS').unbind('click');
        $('.holdCerByEMS').click(function () {
            var holdCerByEMSVal = $(this).val();
            $(this).closest('div.holdCerByEMSDiv').find('input[name="holdCerByEMSVal"]').val(holdCerByEMSVal);
        });
    };

    var noRegWithProfBoard = function () {
        $('.noRegWithProfBoard').unbind('click');
        $('.noRegWithProfBoard').click(function () {
            var noRegWithProfBoardVal = "";
            if($(this).prop('checked')){
                noRegWithProfBoardVal = $(this).val();
            }
            $(this).closest('div.noRegWithProfBoardDiv').find('input.noRegWithProfBoardVal').val(noRegWithProfBoardVal);
        });
    };

    var showOtherSpecialty = function () {
        $('select.specialty').unbind('change');
        $('select.specialty').change(function () {
            var $otherSpecialtyEle = $(this).closest('.clinicalDirectorContent').find('div.otherSpecialtyDiv');
            var val = $(this).val();
            if ('EAMS006' == val) {
                $otherSpecialtyEle.removeClass('hidden');
            } else {
                $otherSpecialtyEle.addClass('hidden');
            }
        });
    };

    var addClinicalDirectorBtn = function () {
        $('.addClinicalDirectorBtn').unbind('click');
        $('.addClinicalDirectorBtn').click(function () {
            showWaiting();
            var cdLength = $('.clinicalDirectorContent').length;

            $.ajax({
                url: '${pageContext.request.contextPath}/clinical-director-html',
                dataType: 'json',
                data: {
                    "cdLength": cdLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addClinicalDirectorDiv').before(data.resultJson+'');
                        //
                        removeClinicalDirector();
                        showOtherSpecialty();
                        $('.date_picker').datepicker({
                            format:"dd/mm/yyyy",
                            autoclose:true,
                            todayHighlight:true,
                            orientation:'bottom'
                        });


                        var cdLength = $('.clinicalDirectorContent').length;
                        $('input[name="cdLength"]').val(cdLength);
                        //hidden add more
                        if (cdLength >= '${clinicalDirectorConfig.maximumCount}') {
                            $('.addClinicalDirectorDiv').addClass('hidden');
                        }
                        if(cdLength <= '${clinicalDirectorConfig.mandatoryCount}'){
                            //remove del btn for mandatory count

                        }
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

    var removeClinicalDirector = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            $(this).closest('div.clinicalDirectorContent').remove();

            var cdLength = $('.clinicalDirectorContent').length;
            $('input[name="cdLength"]').val(cdLength);
            //reset number
            $('div.clinicalDirectorContent').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k+1);

                $(this).find('input.profRegNo').prop('name','profRegNo'+k);
                $(this).find('input.name').prop('name','name'+k);
                $(this).find('input.idNo').prop('name','idNo'+k);
                $(this).find('input.otherSpecialty').prop('name','otherSpecialty'+k);
                $(this).find('input.specialtyGetDate').prop('name','specialtyGetDate'+k);
                $(this).find('input.typeOfCurrRegi').prop('name','typeOfCurrRegi'+k);
                $(this).find('input.currRegiDate').prop('name','currRegiDate'+k);
                $(this).find('input.praCerEndDate').prop('name','praCerEndDate'+k);
                $(this).find('input.typeOfRegister').prop('name','typeOfRegister'+k);
                $(this).find('input.holdCerByEMSVal').prop('name','holdCerByEMSVal'+k);
                $(this).find('input.holdCerByEMS').prop('name','holdCerByEMS'+k);
                $(this).find('input.relevantExperience').prop('name','relevantExperience'+k);
                $(this).find('input.aclsExpiryDate').prop('name','aclsExpiryDate'+k);
                $(this).find('input.bclsExpiryDate').prop('name','bclsExpiryDate'+k);
                $(this).find('input.mobileNo').prop('name','mobileNo'+k);
                $(this).find('input.emailAddr').prop('name','emailAddr'+k);
                $(this).find('input.noRegWithProfBoardVal').prop('name','noRegWithProfBoardVal'+k);
                $(this).find('input.noRegWithProfBoard').prop('name','noRegWithProfBoard'+k);
                $(this).find('input.transportYear').prop('name','transportYear'+k);

                $(this).find('select.professionBoard').prop('name','professionBoard'+k);
                $(this).find('select.salutation').prop('name','salutation'+k);
                $(this).find('select.idType').prop('name','idType'+k);
                $(this).find('select.designation').prop('name','designation'+k);
                $(this).find('select.specialty').prop('name','specialty'+k);
            });
            //display add more
            if (cdLength < '${clinicalDirectorConfig.maximumCount}') {
                $('.addClinicalDirectorDiv').removeClass('hidden');
            }
        });


    }

</script>