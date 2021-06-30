<c:set var="companyType" value="LICTSUB001" />
<c:set var="individualType" value="LICTSUB002" />
<div class="row form-horizontal licenseeContent">
    <iais:row>
        <iais:value width="6">
            <strong class="app-font-size-22 premHeader">Licensee Details</strong>
        </iais:value>
        <iais:value width="6" cssClass="text-right">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;Edit</em>
                </a>
            </c:if>
        </iais:value>
    </iais:row>

    <iais:row cssClass="assignSelectRow">
        <iais:field width="5" mandatory="true" value="Add/Assign a licensee"/>
        <iais:value width="7">
            <iais:select name="assignSelect" options="LICENSEE_OPTIONS" value="${dto.assignSelect}" />
        </iais:value>
    </iais:row>

    <iais:row cssClass="licenseeType">
        <iais:field width="5" mandatory="true" value="Licensee Type"/>
        <iais:value width="7">
            <iais:select name="licenseeType" firstOption="Please Select" codeCategory="CATE_ID_LICENSEE_SUB_TYPE"
                         cssClass="not-disabled" value="${dto.licenseeType}"/>
        </iais:value>
    </iais:row>
    <%-- License Detail Content --%>
    <div class="licensee-detail">
        <iais:row cssClass="company-no ${dto.licenseeType == companyType ? '' : 'hidden'}">
            <iais:field width="5" value="UEN No."/>
            <iais:value width="7">
                <iais:code code="${dto.uenNo}" />
            </iais:value>
        </iais:row>

        <iais:row cssClass="ind-no ${dto.licenseeType == individualType ? '' : 'hidden'}">
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
                <iais:input maxLength="66" type="text" name="licenseeName" id="licenseeName" value="${dto.licenseeName}"/>
            </iais:value>
        </iais:row>

        <%-- Address start --%>
        <iais:row cssClass="postalCodeDiv">
            <iais:field value="Postal Code " mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${dto.postalCode}" />
            </iais:value>
            <iais:value width="2">
                <p><a class="retrieveAddr <%--<c:if test="${!canEdit || readOnly}">hidden</c:if>--%>">Retrieve your address</a></p>
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
            <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
            <iais:value width="7">
                <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${dto.blkNo}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
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
            <iais:field value="Mobile No." mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input type="text" name="telephoneNo" maxLength="8" value="${dto.telephoneNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="Email Address" mandatory="true" width="5"/>
            <iais:value width="7">
                <iais:input type="text" name="emailAddr" maxLength="66" value="${dto.emailAddr}"/>
            </iais:value>
        </iais:row>
    </div>
    <%@include file="previewLicenseeCom.jsp"%>
</div>

<iais:confirm msg="${NEW_ACK016}" needCancel="false" callBack="$('#postalCodePop').modal('hide');" popupOrder="postalCodePop" needEscapHtml="false"
              needFungDuoJi="false" />
<script type="text/javascript">
    $(document).ready(function() {
        // init page
        initLicenseePage();

        // bind event
        $('#assignSelect').on('change', function() {
            clearErrorMsg();
            assignSelect(this, $(this).closest('div.licenseeContent').find('div.licensee-detail'));
        });
        $('#edit').on('click', editContent);

        $('.retrieveAddr').click(function() {
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.licenseeContent').find('div.address'));
        });

        $('#licenseeType').change(checkLicenseeType);

        $('#addrType').on('change', checkAddressManatory);


        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}" var="isSpecial">
        disableContent('div.licenseeContent');
        </c:if>
        <c:if test="${!isSpecial}">
        /*
        var type = $('#licenseeType').val();
        if (type == '${companyType}') {
            $('div.licenseeContent').find(':input').each(function(i, ele) {
                var val = $(ele).val(), $input = $(ele);
                if (!$input.hasClass('not-disabled') && !isEmpty(val) && val != '-' && val != '-1') {
                    disableContent($input);
                }
            });
            $('div.licenseeContent').find('div.licensee-detail').removeClass('hidden');
        }
        */
        </c:if>
    });

    function initLicenseePage() {
        checkLicenseeType();
        var assignSel = $('#assignSelect').val();
        var type = $('#licenseeType').val();
        console.info(assignSel + " --- " + type);
        if (('-1' == assignSel || isEmpty(assignSel)) && type != '${companyType}') {
            $('.licenseeType').addClass('hidden');
            $('.licensee-detail').addClass('hidden');
        } else {
            $('.licenseeType').removeClass('hidden');
        }
        checkAddressManatory();
        var $postalCode = $('div.postalCodeDiv').find('.postalCode');
        if ($postalCode.length > 0) {
            $('.retrieveAddr').toggleClass('hidden', $postalCode.prop('disabled'));
        }
    }

    function checkLicenseeType() {
        var type = $('#licenseeType').val();
        if (type == '${companyType}') {
            $('.company-no').removeClass('hidden');
            $('.ind-no').addClass('hidden');
            $('.retrieveAddr').addClass('hidden');
            //loadCompanyLicensee($('div.licenseeContent'));
            $('.licensee-com').show();
            $('.licensee-detail').hide();
            // init data
            clearFields('.assignSelectRow');
            var tagName = this.tagName;
            if (!isEmpty(tagName) && tagName.toLowerCase() == 'select') {
                clearFields('.licensee-detail');
            }
        } else if (type == '${individualType}') {
            $('.company-no').addClass('hidden');
            $('.ind-no').removeClass('hidden');
            $('.retrieveAddr').removeClass('hidden');
            //unDisableContent('div.licenseeContent');
            $('.licensee-com').hide();
            $('.licensee-detail').show();
        } else {
            $('.company-no').addClass('hidden');
            $('.ind-no').addClass('hidden');
            $('.retrieveAddr').removeClass('hidden');
            //unDisableContent('div.licenseeContent');
            $('.licensee-com').hide();
            $('.licensee-detail').show();
        }
    }

    function checkAddressManatory() {
        var addrType = $('#addrType').val();
        if ('ADDTY001' == addrType) {
            $('.blkNoLabel').append('<span class="mandatory">*</span>');
            $('.floorUnitLabel').append('<span class="mandatory">*</span>');
        } else {
            $('.blkNoLabel .mandatory').remove();
            $('.floorUnitLabel .mandatory').remove();
        }
    }

    function editContent() {
        $('#isEditHiddenVal').val('1');
        <c:if test="${!isNewApp && not empty dto.licenseeType}">
            unDisableContent('div.licensee-detail');
        </c:if>
        <c:if test="${!isNewApp && empty dto.licenseeType || isNewApp}">
            unDisableContent('div.licenseeContent');
        </c:if>
    }

    var loadCompanyLicensee = function ($target) {
        showWaiting();
        var jsonData = {};
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/company-licesee',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                fillLicensee($target, data);
                disableContent('#assignSelect');
                dismissWaiting();
            },
            'error':function () {
                dismissWaiting();
            }
        });
    };

    var assignSelect = function (srcSelector, targetSelector) {
        var assignSelVal = $(srcSelector).val();
        // console.info(assignSelVal);
        var $content = $(targetSelector);
        // init
        unDisableContent(targetSelector);
        $('.retrieveAddr').removeClass('hidden');
        if('-1' == assignSelVal) {
            $content.addClass('hidden');
            clearFields(targetSelector);
            clearFields('.licenseeType');
            initLicenseePage();
        } else if('newOfficer' == assignSelVal) {
            $content.removeClass('hidden');
            clearFields(targetSelector);
            clearFields('.licenseeType');
            initLicenseePage();
        } else {
            $content.removeClass('hidden');
            var arr = assignSelVal.split(',');
            var idType = arr[0];
            var idNo = arr[1];
            $('.retrieveAddr').addClass('hidden');
            loadSelectLicensee($content, idType, idNo, fillLicensee);
        }
    }

    var loadSelectLicensee = function ($target, idType, idNo, callback) {
        showWaiting();
        var jsonData = {
            'idType':idType,
            'idNo':idNo,
            'psnType':psnType
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/individual-licesee',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                // if(data == null){
                //     return;
                // }
                if (typeof callback === 'function') {
                    callback($target, data);
                } else {
                    console.info(data);
                }
                initLicenseePage();
                dismissWaiting();
            },
            'error':function () {
                initLicenseePage();
                dismissWaiting();
            }
        });
    };

    function fillLicensee($current, data) {
        if (isEmpty($current)) {
            return;
        }
        console.info("data - " + data);
        if (isEmpty(data)) {
            //$current.addClass('hidden');
            clearFields($content);
        }
        $.each(data, function(i, val) {
            var $input = $current.find('[name="'+ i +'"]:input');
            if ($input.length == 0) {
                $input = $current.find('.' + i + ':input');
            }
            if ($input.length == 0) {
                return;
            }
            var type = $input[0].type, tag = $input[0].tagName.toLowerCase();
            // console.info("Field - " + i + " : " + val);
            // console.info("Tag - " + tag + " : " + type);
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
            if (!$input.hasClass('not-disabled') && !isEmpty(val) && val != '-' && val != '-1') {
                disableContent($input);
            }
        });
        // init licensee type
        $('#licenseeType').val('${individualType}');
        $('#licenseeType').niceSelect('update');
    }

    function retrieveAddr(postalCode, target) {
        var $addressSelectors = $(target);
        var re=new RegExp('^[0-9]*$');
        var data = {
            'postalCode':postalCode
        };
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
                } else {
                    handleVal($addressSelectors.find('input[name="blkNo"]'), data.blkHseNo, true);
                    handleVal($addressSelectors.find('input[name="streetName"]'), data.streetName, true);
                    handleVal($addressSelectors.find('input[name="buildingName"]'), data.buildingName, true);
                }
                dismissWaiting();
            },
            'error':function () {
                //show pop
                $('#postalCodePop').modal('show');
                handleVal($addressSelectors.find(':input'), '', false);
                dismissWaiting();
            }
        });
    }

    function handleVal(selector, val, readonly) {
        $(selector).val(val);
        $(selector).prop('readonly', readonly);
    }
</script>