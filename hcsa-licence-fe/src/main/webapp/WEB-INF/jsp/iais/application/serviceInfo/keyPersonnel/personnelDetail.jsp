

<div class="col-md-12 col-xs-12 form-horizontal person-content">
    <input type="hidden" class="not-refresh assignSelVal" name="assignSelVal" value="${person.assignSelect}"/>
    <input type="hidden" class="not-refresh licPerson" name="licPerson${index}" value="${person.licPerson ? 1 : 0}"/>
    <input type="hidden" class="not-refresh" name="isPartEdit" value="0"/>
    <input type="hidden" class="not-refresh indexNo" name="indexNo" value="${currentCgo.indexNo}"/>
    <%--<input type="hidden" class="not-refresh" name="existingPsn" value="0"/>--%>
    <input type="hidden" class="not-refresh psnEditField" name="psnEditField" value="<c:out value="${person.psnEditFieldStr}" />"/>

    <iais:row cssClass="edit-content">
        <c:if test="${canEdit}">
            <div class="text-right app-font-size-16">
                <a class="edit psnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${singleName} <span class="psnHeader">${index+1}</span></p>
        </div>
        <div class="col-xs-12 col-md-4 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <c:if test="${isRfc || isRenew || isRfi}">
        <iais:row>
            <div class="col-sm-10">
                <label class="control-font-label">
                    <c:if test="${!empty person.name && !empty person.idNo && !empty person.idType}">
                        ${person.name}, ${person.idNo} (<iais:code code="${person.idType}"/>)
                    </c:if>
                </label>
            </div>
        </iais:row>
    </c:if>

    <iais:row cssClass="assignSelDiv ${canEdit && '-1' != person.assignSelect && not empty person.assignSelect ? 'hidden':''}">
        <iais:field width="5" mandatory="true" value="Assign a ${singleName} Person"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="assignSel" name="assignSelect${index}" options="personSelectOpts"
                         value="${person.assignSelect}"/>
        </iais:value>
    </iais:row>

    <div class="person-detail">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Name"/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select cssClass="salutation" name="salutation${index}" firstOption="Please Select"
                             codeCategory="CATE_ID_SALUTATION" value="${person.salutation}" />
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input cssClass="name" maxLength="66" type="text" name="name${index}" value="${person.name}" />
            </iais:value>
        </iais:row>

        <iais:row cssClass="ind-no">
            <iais:field width="5" mandatory="true" value="ID No."/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select name="idType${index}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${person.idType}"
                             cssClass="idTypeSel"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input maxLength="20" type="text" name="idNo${index}" value="${person.idNo}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="Designation"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="designation" name="designation${index}" value="${person.designation}" options="designationOpList"
                             firstOption="Please Select" onchange="toggleOnSelect(this, '.othe-Designation-${index}', 'DES999');"/>
            </iais:value>
        </iais:row>

        <iais:row cssClass="${person.designation=='DES999' ? '' : 'hidden'} other-designation othe-Designation-${index}">
            <iais:field width="5" value=""/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation${index}" value="${person.otherDesignation}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="false" value="Professional Board"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="professionBoard" name="professionBoard${index}" codeCategory="CATE_ID_PROFESSION_BOARD" value="${person.professionBoard}"
                             firstOption="Please Select"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="false" value="Professional Type"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="professionType" name="professionType${index}" codeCategory="CATE_ID_PROFESSIONAL_TYPE" value="${person.professionType}"
                             firstOption="Please Select"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Professional Regn. No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" cssClass="profRegNo" name="profRegNo${index}" value="${person.profRegNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Type of Registration Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="typeOfCurrRegi${index}" value="${person.typeOfCurrRegi}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Current Registration Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="currRegiDate field-date" name="currRegiDate${index}" value="${person.currRegiDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Practicing Certificate End Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="praCerEndDate field-date" name="praCerEndDate${index}" value="${person.praCerEndDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Type of Register"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="typeOfRegister${index}" value="${person.typeOfRegister}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Specialty"/>
            <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                <c:out value="${person.speciality}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Sub-Specialty"/>
            <iais:value width="7" cssClass="col-md-7 subSpeciality" display="true">
                <c:out value="${person.subSpeciality}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Other Specialties"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" cssClass="specialityOther" name="specialityOther${index}" value="${person.specialityOther}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Date when specialty was obtained"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="specialtyGetDate field-date" name="specialtyGetDate${index}" value="${person.specialtyGetDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Qualification"/>
            <iais:value width="7" cssClass="col-md-7 qualification" display="true">
                <c:out value="${person.qualification}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Other Qualification"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" cssClass="otherQualification" name="otherQualification${index}" value="${person.otherQualification}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="Mobile No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="8" cssClass="mobileNo" type="text" name="mobileNo${index}" value="${person.mobileNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="Email Address"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="320" type="text" cssClass="emailAddr" name="emailAddr${index}" value="${person.emailAddr}"/>
            </iais:value>
        </iais:row>
    </div>
    <hr/>
</div>
<c:if test="${index == 0}">
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsErrorMsg">
                          <iais:message key="GENERAL_ERR0048" escape="false" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="$('#PRS_SERVICE_DOWN').modal('hide');">OK</button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT" >
<script type="text/javascript">
    $(function() {
        assignSelectEvent();
        addPersonnelEvent();
        removePersonEvent();

        $('div.person-content').each(function(k, v) {
            checkPersonContent($(v), true);
        });
        if ($('div.person-content').length == 1) {
            $('div.person-content').find('.psnHeader').html('');
        }

        if($('#PRS_SERVICE_DOWN_INPUT').val()=='PRS_SERVICE_DOWN'){
            $('#PRS_SERVICE_DOWN').modal('show');
        }
    });

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.psnHeader').html(k + 1);
        resetIndex($target, k);
    }

    var addPersonnelEvent = function () {
        $('.addPersonnelBtn').unbind('click');
        $('.addPersonnelBtn').on('click', function () {
            var $target = $('div.person-content:last');
            var src = $target.clone();
            $target.after(src);
            var $currContent = $('div.person-content').last();
            clearFields($currContent);
            $currContent.find('.assignSelVal').val('-1');
            refreshPerson($currContent, $('div.person-content').length - 1);
            $('div.person-content:first').find('.psnHeader').html('1');
            checkPersonContent($currContent, true);
        });
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn:not(:first)').on('click', function () {
            showWaiting();
            $(this).closest('.person-content').remove();
            $('div.person-content').each(function(k, v) {
                refreshPerson($(v), k);
            });
            if ($('div.person-content').length == 1) {
                $('div.person-content').find('.psnHeader').html('');
            }
            dismissWaiting();
        });
    }

    var assignSelectEvent = function () {
        $('.assignSel').unbind('change');
        $('.assignSel').on('change', function() {
            showWaiting();
            var assignVal = $(this).val();
            var $currContent = $(this).closest('.person-content');
            $currContent.find('.assignSelVal').val(assignVal);
            checkPersonContent($currContent, false);
            removePersonEvent();
        });
    }

    function checkPersonContent($currContent, onlyInit, fromUser, callback) {
        var assignVal = $currContent.find('.assignSelVal').val();
        var $content = $currContent.find('.person-detail');
        if('-1' == assignVal || isEmpty(assignVal)) {
            hideTag($content);
            $content.find('.designation').trigger('change');
            dismissWaiting();
        } else if('newOfficer' == assignVal) {
            showTag($content);
            clearFields($content);
            $content.find('.designation').trigger('change');
            $currContent.find('input.licPerson').val('0');
            dismissWaiting();
        } else {
            showTag($content);
            if (onlyInit) {
                dismissWaiting();
                return;
            }
            var url = "/person-info";
            if (fromUser) {
                url = "/user-account-info";
            }
            var indexNo = $currContent.find('input.indexNo').val();
            var arr = assignVal.split(',');
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            var jsonData = {
                'nationality':nationality,
                'idType':idType,
                'idNo':idNo,
                'indexNo':indexNo
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}' + url,
                'dataType':'json',
                'data':jsonData,
                'type':'GET',
                'success':function (data) {
                    if (data == null) {
                        clearFields($content);
                        return;
                    }
                    if (typeof callback === 'function') {
                        callback($currContent, data);
                    } else {
                        fillForm($currContent, data);

                        $currContent.find('.speciality p').html(data.specialty);
                        $currContent.find('.subSpeciality p').html(data.subspecialty);
                        $currContent.find('.qualification p').html(data.qualification);
                        $currContent.find('.licPerson').val(data.licPerson ? 1 : 0);
                        $currContent.find('input[name="isPartEdit"]').val(1);
                        $currContent.find('input[name="indexNo"]').val(data.indexNo);
                        $currContent.find('.psnEditField').val(data.psnEditFieldStr);
                        checkPersonDisabled($currContent);
                        $currContent.find('.designation').trigger('change');
                    }
                    dismissWaiting();
                },
                'error':function () {
                    dismissWaiting();
                }
            });
        }
    }

    function checkPersonDisabled($currContent) {
        var data;
        try{
            data = $.parseJSON($currContent.find('.psnEditField:input').val());
        } catch (e) {
            data = {};
        };
        if ('1' == $currContent.find('.licPerson:input').val()) {
            $.each(data, function(i, val) {
                //console.info(i + " : " + val);
                var $input = $current.find('.' + i + ':input');
                if ($input.length > 0 && !val) {
                    disableContent($input);
                }
            });
        }
    }

    var profRegNoEvent = function () {
        $('.profRegNo').unbind('blur');
        $('.profRegNo').on('blur', function () {
            showWaiting();
            var prgNo = $(this).val();
            var $currContent = $(this).closest('.person-content');

            var assignSelectVal = $currContent.find('select.assignSelVal').val();
            var appType = $('input[name="applicationType"]').val();
            var licPerson = $currContent.find('input.licPerson').val();
            var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
            console.log("isNeedControlName: " + needControlName + " assignSelectVal:" + assignSelectVal
                + " licPerson:" + licPerson + " appType:" + appType);
            checkProfRegNo($currContent, prgNo, needControlName);
        });
    };

    function checkProfRegNo($currContent, prgNo, needControlName, callback) {
        showWaiting();
        if (isEmpty(prgNo)) {
            fillPrsInfo($currContent, null, needControlName);
            disablePrsInfo(false);
            dismissWaiting();
            if (typeof callback === 'function') {
                callback($currContent, null);
            }
            return;
        }
        var jsonData = {
            'prgNo': prgNo
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                var canFill = false;
                if (isEmpty(data)) {
                    console.log("The return data is null for PRS");
                } else if ('-1' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if ('-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');

                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else {
                    canFill = true;
                }
                fillPrsInfo($currContent, canFill? data : null, needControlName);
                disablePrsInfo(canFill);
                if (typeof callback === 'function') {
                    callback($currContent, canFill? data : null);
                }
                dismissWaiting();
            },
            'error': function () {
                fillPrsInfo($currContent, null, needControlName);
                disablePrsInfo(false);
                dismissWaiting();
            }
        });
    }

    function fillPrsInfo($currContent, data, needControlName) {
        var name = '';
        var specialty = '';
        var subspecialty = '';
        var qualification = '';
        var specialtyGetDate = '';
        var typeOfCurrRegi = '';
        var currRegiDate = '';
        var praCerEndDate = '';
        var typeOfRegister = '';
        if (!isEmpty(data)) {
            if (!isEmpty(data.name)) {
                name = data.name;
            }
            if (!isEmpty(data.specialty)) {
                specialty = data.specialty;
            }
            if (!isEmpty(data.subspecialty)) {
                subspecialty = data.subspecialty;
            }
            if (!isEmpty(data.qualification)) {
                qualification = data.qualification;
            }
            if (!isEmpty(data.entryDateSpecialist)) {
                specialtyGetDate = data.entryDateSpecialist[0];
            }
            if (!isEmpty(data.registration)) {
                var registration = data.registration[0];
                typeOfCurrRegi = registration['Registration Type'];
                currRegiDate = registration['Registration Start Date'];
                praCerEndDate = registration['PC End Date'];
                typeOfRegister = registration['Register Type'];
            }
        }
        if (needControlName && !isEmpty(data.name)) {
            $currContent.find('.name').val(name);
        }
        $currContent.find('.speciality p').html(specialty);
        $currContent.find('.subSpeciality p').html(subspecialty);
        $currContent.find('.qualification p').html(qualification);

        $currContent.find('.specialtyGetDate').val(specialtyGetDate);
        $currContent.find('.typeOfCurrRegi').val(typeOfCurrRegi);
        $currContent.find('.currRegiDate').val(currRegiDate);
        $currContent.find('.praCerEndDate').val(praCerEndDate);
        $currContent.find('.typeOfRegister').val(typeOfRegister);
    }

    function disablePrsInfo(flag) {
        if (flag) {
            disableContent($currContent.find('.specialtyGetDate'));
            disableContent($currContent.find('.typeOfCurrRegi'));
            disableContent($currContent.find('.currRegiDate'));
            disableContent($currContent.find('.praCerEndDate'));
            disableContent($currContent.find('.typeOfRegister'));
        } else {
            unDisableContent($currContent.find('.specialtyGetDate'));
            unDisableContent($currContent.find('.typeOfCurrRegi'));
            unDisableContent($currContent.find('.currRegiDate'));
            unDisableContent($currContent.find('.praCerEndDate'));
            unDisableContent($currContent.find('.typeOfRegister'));
        }
    }

    function isNeedControlName(assignSelectVal, licPerson, appType) {
        return /*'newOfficer' == assignSelectVal &&*/ '1' != licPerson && 'APTY002' == appType;
    }

</script>
</c:if>