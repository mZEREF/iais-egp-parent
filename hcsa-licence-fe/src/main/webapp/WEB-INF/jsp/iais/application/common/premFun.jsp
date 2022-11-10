<script type="text/javascript">
    function initPremisePage() {
        initPremiseEvent();
        premTypeChangeEvent();
        checkSelectedLicence();
        checkAddPremBtn(2);
        $('div.premContent').each(function (k, v) {
            let $target = $(v);
            checkPremiseContent($target, k);
            let retrieveflag = $target.find('input[name="retrieveflag"]').val();
            if (retrieveflag == '1') {
                readonlyContent($target.find('div.address'));
            } else {
                unReadlyContent($target.find('div.address'));
            }
        });
        if ($('div.premContent').length == 1) {
            $('div.premContent').find('.premHeader').html('');
        }
    }

    function initPremiseEvent() {
        editPremEvent();

        addFloorUnitEvent();
        delFloorUnitEvent();

        easMtsUseOnlyEvent();

        locateWtihNonHcsaEvent();
        addNonHcsaEvent();
        delNonHcsaEvent();

        premTypeEvent();
        premSelectEvent();

        addrTypeEvent();
        retrieveAddrEvent();

        addPremEvent();
        removeBtnEvent();

        fileUploadEvent();

        checkSelectedLicenceEvent();

        initFormNodes();
    }

    function checkPremiseContent($premContent, index) {
        checkEasMtsUseOnly($premContent);
        checkAddressMandatory($premContent);
        checkLocateWtihNonHcsa($premContent);
        checkRemoveBtn($premContent, index);

        let premType = $premContent.find('.premTypeValue').val();
        let premSelectVal = $premContent.find('.premSelValue').val();
        checkPremSelect($premContent, premSelectVal, true);
        if ('PERMANENT' === premType) {
            showTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            showTag($premContent.find('.scdfRefNoRow'));
            showTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            showTag($premContent.find('.co-location-div'));
        } else if ('CONVEYANCE' === premType) {
            hideTag($premContent.find('.permanentSelect'));
            showTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            showTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            showTag($premContent.find('.co-location-div'));
        } else if ('EASMTS' === premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            showTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            showTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
        } else if ('MOBILE' === premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            showTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
        } else if ('REMOTE' === premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            showTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
        } else {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find(".new-premise-form"));
        }
    }

    function editPremEvent() {
        let $target = $('.premisesEdit');
        if (isEmptyNode($target)) {
            return;
        }
        $target.unbind('click');
        $target.on('click', function () {
            let $premContent = $(this).closest('div.premContent');
            doEditPremise($premContent);
        });
    }

    function doEditPremise($premContent, isEdit) {
        // check whether the edit button is hidden or not,
        // if not, return false, or return true
        if (hideEditBtn($premContent)) {
            return;
        }
        console.info("------------doEditPremise----------------");
        $('#isEditHiddenVal').val('1');
        $premContent.find('.isPartEdit').val('1');
        unDisableContent($premContent);
        showTag($premContent.find('.retrieveAddr'));
        showTag($premContent.find('.addOpDiv'));
        let existData = $premContent.find('.chooseExistData').val();
        if ('1' == existData) {
            if (isEmpty(isEdit) || !isEdit) {
                checkPremDisabled($premContent, true);
            }
        }
        // Non HCSA
        let $nonHcsaContent = $premContent.find('.nonHcsaRowDiv');
        if ($nonHcsaContent.is(':visible')) {
            showTag($premContent.find('.delNonHcsaSvcRow:not(:first)'));
            showTag($premContent.find('.addNonHcsaSvcRow'));
            showTag($premContent.find('.file-upload-gp'));
        }
        checkEditBtn($premContent, false);
    }

    function hideEditBtn($premContent) {
        let $target = $premContent.find('.removeEditDiv');
        if (isEmptyNode($target)) {
            return true;
        }
        return $target.is(':hidden');
    }

    function checkEditBtn($premContent, show) {
        let $target = $premContent.find('.removeEditDiv');
        if (isEmptyNode($target)) {
            return;
        }
        if ($target.is(':hidden') && show) {
            showTag($target);
        }
        if (!isEmptyNode($target.find('.premisesEdit'))) {
            toggleTag($target.find('.premisesEdit'), show);
        }
    }

    function disablePremiseContent($premContent) {
        disableContent($premContent);
        hideTag($premContent.find('.retrieveAddr'));
        hideTag($premContent.find('.opDelDiv'));
        hideTag($premContent.find('.addOpDiv'));
        let $target = $premContent.find('.premisesEdit');
        if (isEmptyNode($target)) {
            hideTag($premContent.find('.opDelDiv'));
        } else {
            let existData = $premContent.find('.chooseExistData').val();
            if ('1' == existData) {
                checkPremDisabled($premContent, true);
            } else {
                showTag($premContent.find('.opDelDiv:not(:first)'));
            }
        }
        // Non HCSA
        let $nonHcsaContent = $premContent.find('.nonHcsaRowDiv');
        if ($nonHcsaContent.is(':visible')) {
            if (isEmptyNode($target)) {
                hideTag($premContent.find('.delNonHcsaSvcRow'));
            } else {
                showTag($premContent.find('.delNonHcsaSvcRow:not(:first)'));
            }
            hideTag($premContent.find('.addNonHcsaSvcRow'));
            hideTag($premContent.find('.file-upload-gp'));
        }
        checkEditBtn($premContent, true);
    }

    function addPremEvent() {
        $('#addPremBtn').unbind('click');
        $('#addPremBtn').on('click', addPremEventFun);
    }

    function addPremEventFun() {
        showWaiting();
        var $target = $('div.premContent:last');
        var premType = $target.find('input.premTypeRadio:checked').val();
        var locateWtihHcsa = $target.find('input[name^="locateWtihHcsa"]:checked').val();
        var locateWtihNonHcsa = $target.find('input.locateWtihNonHcsa:checked').val();
        var src = $target.clone();
        $('div.premises-content').append(src);
        // for preventing the radio auto cleared
        fillValue($target.find('input.premTypeRadio'), premType);
        fillValue($target.find('input[name^="locateWtihHcsa"]'), locateWtihHcsa);
        fillValue($target.find('input.locateWtihNonHcsa'), locateWtihNonHcsa);
        // init new section
        var $premContent = $('div.premContent').last();
        initFormNodes($premContent);
        clearFields($premContent);
        removeAdditional($premContent);
        refreshPremise($premContent, $('div.premContent').length - 1);
        $('div.premContent:first').find('.premHeader').html('1');
        $premContent.find('.chooseExistData').val('0');
        $premContent.find('.retrieveflag').val('0');
        initPremiseEvent();
        dismissWaiting();
    }

    var removeBtnEvent = function () {
        var $target = $(document);
        $target.find('.removeBtn').unbind('click');
        $target.find('.removeBtn').not(':first').on('click', function () {
            showWaiting();
            $(this).closest('div.premContent').remove();
            $('div.premContent').each(function (k, v) {
                refreshPremise($(v), k);
            });
            if ($('div.premContent').length === 1) {
                $('div.premContent').find('.premHeader').html('');
            }
            /*premTypeEvent();
            premSelectEvent();
            removeBtnEvent();*/
            dismissWaiting();
        });
    }

    function refreshPremise($premContent, k) {
        var $target = getJqueryNode($premContent);
        if (isEmptyNode($target)) {
            return;
        }
        //premIndex
        $target.find('.premIndex').val(k);
        $target.find('.premHeader').html(k + 1);
        resetIndex($target, k);
        refreshFloorUnit($target, k);
        refreshNonHcsa($target, k);
        checkPremiseContent($target, k);
        // file
        $target.find('div.uploadFileShowDiv').attr('id', "uploadFile" + k + "ShowId");
        var filePart = $target.find('div.uploadFileShowDiv div');
        if (filePart.length > 0) {
            filePart.each(function () {
                var id = $(this).attr('id');
                var part = /(.*\D+)(\d+)/g.exec(id);
                $(this).attr('id', 'uploadFile' + k + 'Div' + part[2]);
            });
        }
        $target.find('div.uploadFileErrorDiv .error-msg').attr('id', "error_uploadFile" + k + "Error");
    }

    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
        $premContent.find('div.nonHcsaRowDiv .nonHcsaRow:not(:first)').remove();
        $premContent.find('div.uploadFileShowDiv').empty();
    }

    function checkRemoveBtn($premContent, index) {
        if (isEmpty(index)) {
            return;
        }
        let $target = $premContent.find('.removeEditDiv');
        if (isEmptyNode($target)) {
            return;
        }
        if ($target.is(':hidden') && index !== 0) {
            showTag($target);
        }
        if (!isEmptyNode($target.find('.removeBtn'))) {
            toggleTag($target.find('.removeBtn'), index !== 0);
        }
    }

    var premTypeEvent = function () {
        $('.premTypeRadio').unbind('click');
        $('.premTypeRadio').on('click', function () {
            premTypeEventFun($(this));
            let premType = getValue($(this));
            if ((premType == 'PERMANENT' || premType == 'CONVEYANCE') && $('#autoCheckRandM').val() == '1') {
                autoCheckPremiseType('MOBILE');
                autoCheckPremiseType('REMOTE');
            }
        });
    }

    function premTypeEventFun($target) {
        clearErrorMsg();
        var $premContent = $target.closest('div.premContent');
        var premType = $premContent.find('.premTypeRadio:checked').val();
        if (isEmpty(premType)) {
            premType = "";
        }
        $premContent.find('.premTypeValue').val(premType);
        // refer to premTypeChangeEvent method
        $premContent.find('.premTypeValue').trigger('change');
        $premContent.find('.premSelValue').val('-1');
        $premContent.find('.chooseExistData').val('0');
        checkPremiseContent($premContent);
    }

    var premSelectEvent = function () {
        $('.premSelect').unbind('change');
        $('.premSelect').on('change', function () {
            showWaiting();
            clearErrorMsg();
            checkAddPremBtn();
            var premSelectVal = $(this).val();
            var $premContent = $(this).closest('div.premContent');
            $premContent.find('.premSelValue').val(premSelectVal);
            checkPremSelect($premContent, premSelectVal, false);
        });
    }

    function checkPremSelect($premContent, premSelectVal, onlyInit) {
        var $premMainContent = $premContent.find(".new-premise-form");
        if ("-1" == premSelectVal || isEmpty(premSelectVal)) {
            hideTag($premMainContent);
            if (onlyInit) {
                checkPremDisabled($premContent, false);
            } else {
                $premContent.find('.chooseExistData').val("0");
            }
            dismissWaiting();
        } else if ("newPremise" == premSelectVal) {
            if (!onlyInit) {
                removeAdditional($premContent);
                clearFields($premMainContent);
                checkEasMtsUseOnly($premContent);
                checkAddressMandatory($premContent);
                checkLocateWtihNonHcsa($premContent);
                checkPremDisabled($premContent, false);
                $premContent.find('.chooseExistData').val("0");
            }
            showTag($premMainContent);
            //showTag($premContent.find('.retrieveAddr'));
            dismissWaiting();
        } else {
            showTag($premMainContent);
            if (onlyInit) {
                let existData = $premContent.find('.chooseExistData').val();
                checkPremDisabled($premContent, '1' == existData);
                dismissWaiting();
                return;
            }
            //clearFields($premMainContent);
            var premType = $premContent.find('.premTypeValue').val();
            var premisesIndexNo = $premContent.find(".premisesIndexNo").val();
            var data = {
                'premIndexNo': premisesIndexNo,
                'premSelectVal': premSelectVal,
                'premisesType': premType
            };
            var opt = {
                url: '${pageContext.request.contextPath}/lic-premises',
                type: 'GET',
                data: data
            };
            callCommonAjax(opt, "premSelectCallback", $premContent);
        }
    }

    function premSelectCallback(data, $premContent) {
        if (data == null || isEmptyNode($premContent)) {
            dismissWaiting();
            return;
        }
        //fillFormData($premContent, data, "", $('div.premContent').index($premContent));
        var suffix = $('div.premContent').index($premContent);
        //fillValue($premContent.find('.vehicleNo'), data.vehicleNo);
        fillValue($premContent.find('.hciName'), data.hciName);
        fillValue($premContent.find('.postalCode'), data.postalCode);
        fillFormData($premContent.find('.address'), data, "", suffix);
        fillValue($premContent.find('.addrType'), data.addrType);
        fillFloorUnit($premContent, data);

        $premContent.find('.chooseExistData').val(data.existingData);
        $premContent.find('.premSelValue').val(data.premisesSelect);
        $premContent.find('.premTypeValue').val(data.premisesType);
        // refer to premTypeChangeEvent method
        $premContent.find('.premTypeValue').trigger('change');
        $premContent.find('.premisesIndexNo').val(data.premisesIndexNo);
        //$premContent.find('.isPartEdit').val('0');

        checkEasMtsUseOnly($premContent);
        checkAddressMandatory($premContent);
        let existData = $premContent.find('.chooseExistData').val();
        checkPremDisabled($premContent, '1' == existData);
        dismissWaiting();
    }

    function checkPremDisabled($premContent, disabled) {
        //checkDisabled($premContent.find('.vehicleNo'), disabled);
        checkDisabled($premContent.find('.hciName'), disabled);
        checkDisabled($premContent.find('.postalCode'), disabled);
        toggleTag($premContent.find('.retrieveAddr'), !disabled);
        checkDisabled($premContent.find('.address'), disabled);
        checkDisabled($premContent.find('.addrType'), disabled);
        checkDisabled($premContent.find('.operationDiv'), disabled);
        hideTag($premContent.find('.operationAdlDiv:first'));
        toggleTag($premContent.find('.opDelDiv:not(:first)'), !disabled);
        toggleTag($premContent.find('.addOpDiv'), !disabled);
    }

    function autoCheckPremiseType(premType) {
        //const premType = 'REMOTE';
        let $premType = $('input.premTypeRadio[value="' + premType + '"');
        if (isEmptyNode($premType) || isChecked($premType)) {
            return;
        }
        let nodeChecked = false;
        let $target = null;
        $premType.each(function () {
            if ($(this).is(':checked')) {
                nodeChecked = true;
            } else if (isEmptyNode($target)) {
                // check the prem type at the same level
                let name = $(this).attr('name');
                if (!isChecked($('input[name="' + name + '"'))) {
                    $target = $(this);
                }
            }
        });
        if (!nodeChecked && isEmptyNode($target) && !isEmptyNode($('#addPremBtn'))) {
            addPremEventFun();
            $target = $('input[value="' + premType + '"').last();
        }
        if (!isEmptyNode($target)) {
            $target.prop('checked', true);
            premTypeEventFun($target);
        }
    }

    var checkSelectedLicenceEvent = function () {
        $('input[name="selectedLicence"]').unbind('click');
        $('input[name="selectedLicence"]').on('click', function () {
            checkSelectedLicence($(this));
        });
    }

    function checkSelectedLicence($tag) {
        let isSingle = true;
        if (isEmpty($tag) || $tag.length === 0) {
            $tag = $('input[name="selectedLicence"]');
            isSingle = false;
        }
        if ($tag.length === 0) {
            return;
        }
        let nonChecked = false;
        let allChecked = false;
        $tag.each(function () {
            let $input = $(this);
            if (!$input.is(':checked')) {
                return;
            }
            let data = $input.val();
            if ('NON' == data) {
                nonChecked = true;
            }
            if ('ALL' == data) {
                allChecked = true;
            }
        });
        unDisableContent($('input[name="selectedLicence"]'));
        if (nonChecked) {
            $('input[name="selectedLicence"]:not([value="NON"])').each(function () {
                $(this).prop('checked', false);
            });
            disableContent($('input[name="selectedLicence"]:not([value="NON"])'));
        } else if (allChecked) {
            disableContent($('input[name="selectedLicence"][value="NON"]'));
            if (isSingle && $tag.val() != 'ALL') {
                $('input[name="selectedLicence"][value="ALL"]').prop('checked', false);
            } else {
                $('input[name="selectedLicence"]:not([value="NON"])').each(function () {
                    $(this).prop('checked', true);
                });
            }
        } else {
            $('input[name="selectedLicence"][value="ALL"]').prop('checked', false);
        }
    }

    var easMtsUseOnlyEvent = function () {
        $('.useType').unbind('click');
        $('.useType').on('click', function () {
            let $premContent = $(this).closest('div.premContent');
            checkEasMtsUseOnly($premContent);
        });
    }

    function checkEasMtsUseOnly($premContent) {
        let data = getValue($premContent.find('.useType'));
        $premContent.find('.pubEmailLabel').find('.mandatory').remove();
        $premContent.find('.pubHotlineLabel').find('.mandatory').remove();
        if ('UOT002' != data) {
            $premContent.find('.pubEmailLabel').append('<span class="mandatory">*</span>');
            $premContent.find('.pubHotlineLabel').append('<span class="mandatory">*</span>');
        }
    }

    function checkLocateWtihNonHcsa($premContent) {
        var $row = $premContent.find('.locateWtihNonHcsaRow');
        if (isEmptyNode($row)) {
            return;
        }
        if ($row.find('input.locateWtihNonHcsa[value="1"]').is(':checked')) {
            showTag($row.next('.nonHcsaRowDiv'));
            hideTag($premContent.find('.delNonHcsaSvcRow:first'));
        } else {
            var $nonHcsaContent = $row.next('.nonHcsaRowDiv');
            hideTag($nonHcsaContent);
            $nonHcsaContent.find('.nonHcsaRow:not(:first)').remove();
        }
    }

    var locateWtihNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.locateWtihNonHcsa').unbind('click');
        $target.find('.locateWtihNonHcsa').on('click', function () {
            checkLocateWtihNonHcsa($(this).closest('.premContent'));
        });
    }

    function fillNonHcsa($premContent, data) {
        $premContent.find('div.nonHcsaRowDiv .nonHcsaRow:not(:first)').remove();
        if (isEmpty(data) || !$.isArray(data)) {
            clearFields($premContent.find('div.nonHcsaRowDiv'), true);
            return;
        }
        var $parent = $premContent.find('div.nonHcsaRowDiv');
        var len = data.length;
        for (var i = 0; i < len; i++) {
            var $target = $parent.find('.nonHcsaRow').eq(i);
            if ($target.length == 0) {
                addNonHcsa($parent);
                $target = $parent.find('.nonHcsaRow').eq(i);
            }
            fillValue($target.find('input.coBusinessName'), data[i].businessName);
            fillValue($target.find('input.coSvcName'), data[i].providedService);
        }
    }

    function refreshNonHcsa(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.nonHcsaRow').each(function (k, v) {
            toggleTag($(v).find('.delNonHcsaSvcRow'), k !== 0);
            resetField(v, k, prefix);
        });
        var length = $target.find('div.nonHcsaRow').length;
        $target.closest('div.premContent').find('.nonHcsaLength').val(length);
    }

    function addNonHcsa(ele) {
        showWaiting();
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.nonHcsaRow:first').clone();
        $premContent.find('div.addNonHcsaSvcRow').before(src);
        var $target = $premContent.find('div.nonHcsaRow:last');
        initFormNodes($target);
        clearFields($target);
        refreshNonHcsa($premContent.find('div.nonHcsaRowDiv'), $('div.premContent').index($premContent));
        delNonHcsaEvent($premContent);
        dismissWaiting();
    }

    var addNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addNonHcsaSvc').unbind('click');
        $target.find('.addNonHcsaSvc').on('click', function () {
            addNonHcsa(this);
        });
    }

    var delNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.nonHcsaSvcDel').unbind('click');
        $target.find('.nonHcsaSvcDel').not(':first').on('click', function () {
            var $premContent = $(this).closest('div.premContent');
            var $nonHcsaContent = $(this).closest('div.nonHcsaRowDiv');
            $(this).closest('div.nonHcsaRow').remove();
            refreshNonHcsa($nonHcsaContent, $('div.premContent').index($premContent));
            doEditPremise($premContent);
        });
    }

    function fillFloorUnit($currContent, data) {
        if (isEmpty(data)) {
            return;
        }
        // base
        var $firstFU = $currContent.find('.operationDiv:first');
        fillValue($firstFU.find('input.floorNo'), data.floorNo);
        fillValue($firstFU.find('input.unitNo'), data.unitNo);
        // additional
        $currContent.find('div.operationDivGroup .operationDiv').remove();
        var floorUnits = data.appPremisesOperationalUnitDtos;
        if (isEmpty(floorUnits)) {
            $currContent.find('div.operationDivGroup .operationDiv').remove();
        } else {
            var $parent = $currContent.find('div.operationDivGroup');
            var len = floorUnits.length;
            for (var i = 0; i < len; i++) {
                var $target = $parent.find('.operationDiv').eq(i);
                if ($target.length === 0) {
                    addFloorUnit($parent);
                    $target = $parent.find('.operationDiv').eq(i);
                }
                fillValue($target.find('input.floorNo'), floorUnits[i].floorNo);
                fillValue($target.find('input.unitNo'), floorUnits[i].unitNo);
            }
        }
    }

    function refreshFloorUnit(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.operationDiv').each(function (i, ele) {
            if (i === 0) {
                hideTag($(ele).find('.operationAdlDiv'));
            } else {
                showTag($(ele).find('.operationAdlDiv'));
                showTag($(ele).find('.opDelDiv'));
                $(ele).find('.floorUnitLabel').html('');
            }
            resetField(ele, i, prefix);
        });
        var length = $target.find('.operationDiv').length;
        //$target.find('.addressSize').val(length);//BE secondary address
        $target.find('.opLength').val(length);
    }

    function addFloorUnit(ele) {
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.operationDiv:first').clone();
        initFormNodes(src);
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.premContent').index($premContent));
        delFloorUnitEvent($premContent);
    }

    var addFloorUnitEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this);
        });
    }

    var delFloorUnitEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            var $premContent = $(this).closest('div.premContent');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.premContent').index($premContent));
            doEditPremise($premContent);
        });
    }

    var addrTypeEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addrType').unbind('change');
        $target.find('.addrType').on('change', function () {
            var $premContent = $(this).closest('div.premContent');
            checkAddressMandatory($premContent);
        });
    }

    function checkAddressMandatory($premContent) {
        var addrType = $premContent.find('.addrType').val();
        $premContent.find('.blkNoLabel .mandatory').remove();
        $premContent.find('.floorUnitLabel .mandatory').remove();
        if ('ADDTY001' == addrType) {
            $premContent.find('.blkNoLabel').append('<span class="mandatory">*</span>');
            $premContent.find('.floorUnitLabel:first').append('<span class="mandatory">*</span>');
        }
    }

    var retrieveAddrEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.retrieveAddr').unbind('click');
        $target.find('.retrieveAddr').on('click', function () {
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.premContent').find('div.address'));
        });
    }

    function retrieveAddr(postalCode, target) {
        var $addressSelectors = $(target);
        var data = {
            'postalCode': postalCode
        };
        showWaiting();
        $.ajax({
            'url': '${pageContext.request.contextPath}/retrieve-address',
            'dataType': 'json',
            'data': data,
            'type': 'GET',
            'success': function (data) {
                let $currContent = $addressSelectors.closest('div.premContent');
                if (data == null) {
                    //show pop
                    $('#postalCodePop').modal('show');
                    //handleVal($addressSelectors.find(':input'), '', false);
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    $currContent.find('input[name="retrieveflag"]').val('0');
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                    readonlyContent($addressSelectors);
                    $currContent.find('input[name="retrieveflag"]').val('1');
                }
                dismissWaiting();
            },
            'error': function () {
                //show pop
                $('#postalCodePop').modal('show');
                clearFields($addressSelectors.find(':input'));
                unReadlyContent($addressSelectors);
                $currContent.find('input[name="retrieveflag"]').val('0');
                dismissWaiting();
            }
        });
    }

    function initUploadFileData() {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX");
        $('#_singleUpload').val("1");
    }

    var fileUploadEvent = function () {
        $('.file-upload').unbind('click');
        $('.file-upload').click(function () {
            var index = $(this).closest('.premContent').find('input[name="premIndex"]').val();
            var uploadKey = "uploadFile" + index;
            /*$('input[name="uploadKey"]').val(uploadKey);*/
            clearFlagValueFEFile();
            $('#selectFileDiv').html('<input id="' + uploadKey + '" class="selectedFile" name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="ajaxCallUpload(\'mainForm\',\'' + uploadKey + '\');" aria-label="selectedFile">');
            $('input[type="file"]').click();
        });
    }

    function doActionAfterUploading(data, fileAppendId) {
        fillNonHcsa($("#" + fileAppendId + "ShowId").closest('div.premContent'), data.appPremNonLicRelationDtos);
        if (data.msgType == 'Y') {
            $('#' + fileAppendId + 'ShowId').empty();
        }
    }

    function reSetPremTypeTooltip($currContent) {
        if (isEmptyNode($currContent)) {
            $currContent = $(document);
        }
        $currContent.find('.premisesTypeDiv .form-check-label').each(function () {
            let targetSpan = $(this).find('span')[1];
            let leftWidth = targetSpan.offsetLeft + targetSpan.offsetWidth + 5;
            let $target = $(this).find('a');
            $target.css('left', leftWidth + 'px');
            $target.css('right', '');
        });
    }

    // 0: hide; 1: show; 2: hide on condition
    function checkAddPremBtn(action = 1) {
        let $premAddBtn = $('#addPremBtn');
        if (isEmptyNode($premAddBtn)) {
            return;
        }
        if (action == 0) {
            hideTag($premAddBtn);
        } else if (action == 1) {
            showTag($premAddBtn);
        } else if (action == 2) {
            if (isEmpty(getValue('.premTypeRadio'))) {
                hideTag($premAddBtn);
            }
        }
    }
</script>