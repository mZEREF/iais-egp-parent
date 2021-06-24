<div class="row form-horizontal licenseeContent">
    <iais:row>
        <iais:field width="5" mandatory="true" value="Add/Assign a licensee"/>
        <iais:value width="7">
            <iais:select name="assignSelect" options="LICENSEE_OPTIONS" value="${dto.assignSelect}" />
            <%--<span name="iaisErrorMsg" class="error-msg" id="error_assignSelect"></span>--%>
        </iais:value>
    </iais:row>

    <%-- License Detail Content --%>
    <div class="licensee-detail <c:if test="${empty dto.assignSelect || '-1' == dto.assignSelect}">hidden</c:if>">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Licensee Type"/>
            <iais:value width="7">
                <iais:select name="licenseeType" firstOption="Please Select" codeCategory="CATE_ID_LICENSEE_TYPE" value="${dto.licenseeType}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="ID No."/>
            <iais:value width="3">
                <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${dto.idType}" />
            </iais:value>
            <iais:value width="3">
                <iais:input maxLength="9" type="text" name="idNumber" value="${dto.idNumber}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="Licensee Name" mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input maxLength="66"  type="text" name="licenseeName" id="licenseeName" value="${dto.licenseeName}"/>
            </iais:value>
        </iais:row>

        <%-- Address start --%>
        <iais:row cssClass="postalCodeDiv">
            <iais:field value="Postal Code " mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${dto.postalCode}" />
            </iais:value>
            <iais:value width="2">
            <%--<div class="col-xs-7 col-sm-6 col-md-3">--%>
                <p><a class="retrieveAddr <%--<c:if test="${!canEdit || readOnly}">hidden</c:if>--%>">Retrieve your address</a></p>
            <%--</div>--%>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Address Type" mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select"
                             value="${dto.addrType}" />
            </iais:value>
        </iais:row>
        <iais:row cssClass="address">
            <iais:field value="Block / House No." width="5"/>
            <iais:value width="7">
                <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${dto.blkNo}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Floor / Unit No." mandatory="true" width="5"/>
            <iais:value width="2">
                <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${dto.floorNo}"/>
            </iais:value>
            <iais:value width="1" cssClass="col-sm-2 col-md-1 text-center"><p>-</p></iais:value>
            <iais:value width="2">
                <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${dto.unitNo}"/>
            </iais:value>
        </iais:row>
        <iais:row cssClass="address">
            <iais:field value="Street Name" mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input maxLength="32" type="text" name="streetName" id="streetName" value="${dto.streetName}"/>
            </iais:value>
        </iais:row>

        <iais:row cssClass="address">
            <iais:field value="Building Name" width="5"/>
            <iais:value width="7">
                <iais:input maxLength="66" type="text" name="buildingName" id="buildingName" value="${dto.buildingName}"/>
            </iais:value>
        </iais:row>
        <%-- Address end --%>

        <iais:row>
            <iais:field value="Telephone No." mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input type="text" name="telephoneNo" maxLength="8" value="${dto.telephoneNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="Email Address" mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input type="text" name="emilAddr" maxLength="66" value="${dto.emilAddr}"/>
            </iais:value>
        </iais:row>
    </div>
</div>

<iais:confirm msg="${NEW_ACK016}" needCancel="false" callBack="$('#postalCodePop').modal('hide');" popupOrder="postalCodePop" needEscapHtml="false"
              needFungDuoJi="false" />
<script type="text/javascript">
    $(document).ready(function() {
        assignSelectBindEvent();

        $('.retrieveAddr').click(function() {
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.licenseeContent').find('div.address'));
        });

        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}">
            disableContent('div.licensee-detail');
        </c:if>
    });

    /*function editContent(targetSelector) {
        var $currContent = $(targetSelector);
        $currContent.find('.edit-content').addClass('hidden');
        $currContent.find('input[type="text"]').prop('disabled', false);
        $currContent.find('div.nice-select').removeClass('disabled');
        $currContent.find('input[type="text"]').css('border-color', '');
        $currContent.find('input[type="text"]').css('color', '');
        $currContent.find('.date_picker').removeClass('disabled-placeHolder');
        $currContent.find('input.holdCerByEMS').each(function () {
            $(this).closest('div').find('label span.check-circle').removeClass('radio-disabled');
        });
        $currContent.find('input[type="checkbox"]').prop('disabled',false);
        $currContent.find('input[type="radio"]').prop('disabled',false);
        $currContent.find('.assignSel').prop('disabled',false);
        $('#isEditHiddenVal').val('1');

        var appType = $('input[name="applicationType"]').val();
        var assignSelectVal = $currContent.find('select.assignSel').val();
        var licPerson = $currContent.find('input.licPerson').val();
        var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
        if(needControlName){
            var prgNo = $currContent.find('input.profRegNo').val();
            if(!isEmpty(prgNo)){
                controlEdit($currContent.find('input.field-name'), 'disabled', false);
            }
        }
    }*/

    function assignSelectBindEvent() {
        $('#assignSelect').on('change', function() {
            clearErrorMsg();
            assignSelect(this, $(this).closest('div.licenseeContent').find('div.licensee-detail'));
        });
    }

    var assignSelect = function (srcSelector, targetSelector) {
        var assignSelVal = $(srcSelector).val();
        console.info(assignSelVal);
        var $content = $(targetSelector);
        // init
        unDisableContent(targetSelector);
        if('-1' == assignSelVal) {
            $content.addClass('hidden');
            clearFields(targetSelector);
        } else if('newOfficer' == assignSelVal) {
            $content.removeClass('hidden');
            clearFields(targetSelector);
        } else {
            $content.removeClass('hidden');
            var arr = assignSelVal.split(',');
            var idType = arr[0];
            var idNo = arr[1];
            loadSelectLicensee($content, idType, idNo, fillLicensee);
        }
    }

    var loadSelectLicensee = function ($CurrentPsnEle, idType, idNo, callback) {
        showWaiting();
        var spcEle = $CurrentPsnEle.find('.specialty');
        var jsonData = {
            'idType':idType,
            'idNo':idNo,
            'psnType':psnType
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/licesee-detail',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                // if(data == null){
                //     return;
                // }
                if (typeof callback === 'function') {
                    callback($CurrentPsnEle, data, psnType);
                } else {
                    console.info(data);
                    //fillPsnForm($CurrentPsnEle, data, psnType);
                }
                dismissWaiting();
            },
            'error':function () {
                dismissWaiting();
            }
        });
    };

    function fillLicensee($current, data, diableAll) {
        if (isEmpty($current)) {
            return;
        }
        if (isEmpty(data)) {
            $current.addClass('hidden');
            clearFields($content);
        }
        $.each(data, function(i, val) {
            var $input = $current.find('.' + i + ':input', '[name="'+ i +'"]:input');
            if ($input.length == 0) {
                return;
            }
            var type = $input[0].type, tag = $input[0].tagName.toLowerCase();
            //console.info("Field - " + i + " : " + val);
            //console.info("Tag - " + tag + " : " + type);
            if (type == 'radio') {
                $input.filter('[value="' + val + '"]').prop('checked', true);
                $input.filter(':not([value="' + val + '"])').prop('checked', false);
            } else if (type == 'checkbox') {
                if ($.isArray(val)) {
                    $input.prop('checked', false);
                    for (var v in val) {
                        if (curVal == v) {
                            $(this).prop('checked', true);
                        }
                    }
                } else {
                    $input.filter('[value="' + val + '"]').prop('checked', true);
                    $input.filter(':not([value="' + val + '"])').prop('checked', false);
                }
            } else if (tag == 'select') {
                var oldVal = $input.val();
                $input.val(val);
                if (isEmpty($input.val())) {
                    $input[0].selectedIndex = 0;
                }
                if ($input.val() != oldVal) {
                    $input.niceSelect("update");
                }
            } else {
                $input.val(val);
            }
            if (diableAll) {
                disableContent($input);
            }
        });
    }

    function retrieveAddr(postalCode, target) {
        //var $postalCodeEle = $(selector).closest('div.postalCodeDiv');
        var $addressSelectors = $(target);
        //var postalCode = $postalCodeEle.find('.postalCode').val();
        //var thisId = $(selector).attr('id');
        //alert(postalCode);
        var re=new RegExp('^[0-9]*$');
        var data = {
            'postalCode':postalCode
        };
        var premType = '';
        showWaiting();
        $.ajax({
            'url':'${pageContext.request.contextPath}/retrieve-address',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    // $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    handleVal($addressSelectors.find(':input'), '', false);
                    // $premContent.find('.'+prefixName+'BlkNo').val('');
                    // $premContent.find('.'+prefixName+'StreetName').val('');
                    // $premContent.find('.'+prefixName+'BuildingName').val('');
                    //
                    // $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                    // $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                    // $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);
                    //
                    // $premContent.find('input[name="retrieveflag"]').val('0');
                } else {
                    handleVal($addressSelectors.find('input[name="blkNo"]'), data.blkHseNo, true);
                    handleVal($addressSelectors.find('input[name="streetName"]'), data.streetName, true);
                    handleVal($addressSelectors.find('input[name="buildingName"]'), data.buildingName, true);
                }
                dismissWaiting();
            },
            'error':function () {
                //$postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                //show pop
                $('#postalCodePop').modal('show');
                handleVal($addressSelectors.find(':input'), '', false);
                // $premContent.find('.'+prefixName+'BlkNo').val('');
                // $premContent.find('.'+prefixName+'StreetName').val('');
                // $premContent.find('.'+prefixName+'BuildingName').val('');
                //
                // $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                // $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                // $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);

                //$premContent.find('input[name="retrieveflag"]').val('0');
                dismissWaiting();
            }
        });
    }

    function handleVal(selector, val, readonly) {
        $(selector).val(val);
        $(selector).prop('readonly', readonly);
    }
</script>