<c:set var="companyType" value="LICTSUB001" />
<c:set var="individualType" value="LICTSUB002" />
<c:set var="soloType" value="LICT002" />

<c:set var="isRfi" value="${requestInformationConfig != null}" />
<c:set var="isNew" value="${'APTY002' == AppSubmissionDto.appType}" />
<c:set var="isRFC" value="${'APTY005' == AppSubmissionDto.appType}" />
<c:set var="showClaimFields"
       value="${isRFC && !isRfi && (dto.licenseeType eq soloType || dto.licenseeType eq individualType)}" />

<div class="form-horizontal licenseeContent">
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <strong class="app-font-size-22 premHeader">Licensee Details</strong>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>
    <c:if test="${showClaimFields}">
        <label>If you intend to transfer this licence to your corporate entity, please provide the UEN no. and Name of your Corporate Entity below:</label>
        <br>
        <iais:row cssClass="claimFeilds">
            <iais:field value="UEN of your Corporate Entity" mandatory="false" width="5"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="2000" type="text" name="claimUenNo" id="claimUenNo" value="${dto.claimUenNo}"/>
            </iais:value>
        </iais:row>
        <iais:row cssClass="claimFeilds">
            <iais:field value="Name of your Corporate Entity" mandatory="false" width="5"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="2000" type="text" name="claimCompanyName" id="claimCompanyName"
                            value="${dto.claimCompanyName}"/>
            </iais:value>
        </iais:row>
        <hr>
    </c:if>
    <c:if test="${dto.licenseeType ne soloType}">
        <c:if test="${isNew}">
            <iais:row cssClass="assignSelectRow">
                <iais:field width="5" value="Add/Assign a licensee" cssClass="assignSelectLabel"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="assignSelect" options="LICENSEE_OPTIONS" value="${dto.assignSelect}" cssClass="assignSel"/>
                </iais:value>
            </iais:row>
        </c:if>
        <c:if test="${!isNew}">
            <iais:input cssClass="not-clear" type="hidden" name="assignSelect" value="${dto.assignSelect}"/>
        </c:if>

        <iais:row cssClass="licenseeType">
            <iais:field width="5" mandatory="true" value="Licensee Type"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="licenseeType" firstOption="Please Select" codeCategory="CATE_ID_LICENSEE_SUB_TYPE"
                             cssClass="not-disabled licenseeTypeSel" value="${dto.licenseeType}" />
            </iais:value>
        </iais:row>
        <%-- License Detail Content --%>
        <%@include file="licenseeDetailContent.jsp"%>
    </c:if>
    <c:if test="${dto.licenseeType eq soloType}">
        <iais:input cssClass="not-clear" type="hidden" name="licenseeType" id="licenseeType" value="${soloType}"/>
    </c:if>
    <%@include file="previewLicenseeCom.jsp"%>
</div>

<iais:confirm msg="NEW_ACK016" needCancel="false" callBack="$('#postalCodePop').modal('hide');"
              popupOrder="postalCodePop" needEscapHtml="false" needFungDuoJi="false" />
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
        $('#licenseeType').on('change', function() {
            clearErrorMsg();
            clearFields('.licensee-detail');
            unDisableContent('div.licensee-detail');
            $('.retrieveAddr').removeClass('hidden');
            checkLicenseeType();
        });

        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}" var="isSpecial">
        disableContent('div.licenseeContent');
        $('.retrieveAddr').addClass('hidden');
        </c:if>
    });

    function initLicenseePage() {
        checkLicenseeType();
        var assignSel = $('#assignSelect').val();
        var type = $('#licenseeType').val();
        console.info(assignSel + " --- " + type);
        if ($('#assignSelect').length > 0 && ('-1' == assignSel || isEmpty(assignSel)) && type != '${companyType}') {
            $('.licenseeType').addClass('hidden');
            $('.licensee-detail').hide();
        } else if ($('.licenseeType').length > 0) {
            $('.licenseeType').removeClass('hidden');
        }
        checkAddressManatory();
        var $postalCode = $('div.postalCodeDiv').find('.postalCode');
        if ($postalCode.length > 0) {
            $('.retrieveAddr').toggleClass('hidden', $postalCode.prop('disabled'));
        }
        if (!isEmpty(assignSel) && '-1' != assignSel && 'newOfficer' != assignSel) {
            disableContent('div.licensee-detail');
            $('.retrieveAddr').addClass('hidden');
        }
    }

    function checkLicenseeType() {
        var type = "-1";
        if ($('#licenseeType').length > 0) {
            type = $('#licenseeType').val();
        }
        console.log("Type: " + type);
        $('.assignSelectLabel .mandatory').remove();
        if (type == '${companyType}') {
            $('.company-no').removeClass('hidden');
            $('.ind-no').addClass('hidden');
            $('.retrieveAddr').addClass('hidden');
            $('.licensee-com').show();
            $('.licensee-detail').hide();
            // init data
            fillValue('#assignSelect', 'newOfficer');
            var tagName = this.tagName;
            if (!isEmpty(tagName) && tagName.toLowerCase() == 'select') {
                clearFields('.licensee-detail');
            }
        } else if (type == '${individualType}') {
            $('.company-no').addClass('hidden');
            $('.ind-no').removeClass('hidden');
            $('.retrieveAddr').removeClass('hidden');
            $('.licensee-com').hide();
            $('.licensee-detail').show();
            $('.assignSelectLabel').append('<span class="mandatory">*</span>');
        } else if (type == '-1') {
            $('.licensee-com').show();
            $('.editDiv').remove();
        } else if (type == '${soloType}') {
            $('.licensee-com').show();
        } else {
            $('.company-no').addClass('hidden');
            $('.ind-no').addClass('hidden');
            $('.retrieveAddr').removeClass('hidden');
            $('.licensee-com').hide();
            $('.licensee-detail').show();
            $('.assignSelectLabel').append('<span class="mandatory">*</span>');
        }
    }

    function editContent() {
        $('#isEditHiddenVal').val('1');
        <c:if test="${isNewApp && not empty dto.licenseeType}">
        unDisableContent('div.assignSelectRow');
        unDisableContent('div.licenseeType');
        unDisableContent('div.licensee-detail');
        $('.retrieveAddr').removeClass('hidden');
        </c:if>
        <c:if test="${!isNewApp && not empty dto.licenseeType}">
        unDisableContent('div.licensee-detail');
        $('.retrieveAddr').removeClass('hidden');
        disableContent('div.ind-no');
        disableContent('#licenseeName');
        </c:if>
        <c:if test="${showClaimFields}">
        unDisableContent('div.claimFeilds');
        </c:if>
        initLicenseePage();
        $(this).closest('div').hide();
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
        $('.retrieveAddr').show();
        if('-1' == assignSelVal) {
            var type = "";
            if ($('#licenseeType').length > 0) {
                type = $('#licenseeType').val();
            }
            $content.hide();
            clearFields(targetSelector);
            clearFields('.licenseeType');
            initLicenseePage();
            if (type == '${individualType}') {
                $('#licenseeType').val(type);
                $('#licenseeType').niceSelect("update");
            }
        } else if('newOfficer' == assignSelVal) {
            $content.show();
            clearFields(targetSelector);
            clearFields('.licenseeType');
            initLicenseePage();
        } else {
            $content.show();
            var arr = assignSelVal.split(',');
            var idType = arr[0];
            var idNo = arr[1];
            $('.retrieveAddr').hide();
            loadSelectLicensee($content, idType, idNo, fillLicensee);
        }
    }

    var loadSelectLicensee = function ($target, idType, idNo, callback) {
        showWaiting();
        var jsonData = {
            'idType':idType,
            'idNo':idNo
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

    function checkAddressManatory() {
        var addrType = $('#addrType').val();
        $('.blkNoLabel .mandatory').remove();
        $('.floorUnitLabel .mandatory').remove();
        if ('ADDTY001' == addrType) {
            $('.blkNoLabel').append('<span class="mandatory">*</span>');
            $('.floorUnitLabel').append('<span class="mandatory">*</span>');
        }
    }

</script>