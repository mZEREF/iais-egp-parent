<script type="text/javascript">
    //TODO edit
    function doEditPremise(premContent, isEdit) {

    }

    function initPremiseEvent() {
        addOperationalEvnet();
        delOperationEvent();

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

        $("[data-toggle='tooltip']").tooltip();
    }

    function checkPremiseContent($premContent) {
        checkAddressMandatory($premContent);
        checkLocateWtihNonHcsa($premContent);

        var premType = $premContent.find('.premTypeValue').val();
        var premSelectVal = $premContent.find('.premSelValue').val();
        checkPremSelect($premContent, premSelectVal, true);
        if ('PERMANENT' === premType) {
            showTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            showTag($premContent.find('.scdfRefNoRow'));
            showTag($premContent.find('.certIssuedDtRow'));
            showTag($premContent.find('.co-location-div'));
        } else if ('CONVEYANCE' === premType) {
            hideTag($premContent.find('.permanentSelect'));
            showTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
            showTag($premContent.find('.vehicleRow'));
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
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
            showTag($premContent.find('.easMtsAddFields'));
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

    function addPremEvent() {
        $('#addPremBtn').unbind('click');
        $('#addPremBtn').on('click', function () {
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
            clearFields($premContent);
            removeAdditional($premContent);
            refreshPremise($premContent, $('div.premContent').length - 1);
            $('div.premContent:first').find('.premHeader').html('1');
            $premContent.find('.chooseExistData').val('0');
            initPremiseEvent();
            dismissWaiting();
        });
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
            if ($('div.premContent').length == 1) {
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
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.premHeader').html(k + 1);
        resetIndex($target, k);
        refreshFloorUnit($target, k);
        refreshNonHcsa($target, k);
        checkPremiseContent($target);
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

    var premTypeEvent = function () {
        $('.premTypeRadio').on('click', function () {
            clearErrorMsg();
            var $premContent = $(this).closest('div.premContent');
            var premType = $premContent.find('.premTypeRadio:checked').val();
            if (isEmpty(premType)) {
                premType = "";
            }
            $premContent.find('.premTypeValue').val(premType);
            $premContent.find('.premTypeValue').trigger('change');
            $premContent.find('.premSelValue').val('-1');
            $premContent.find('.chooseExistData').val('0');
            checkPremiseContent($premContent);
        });
    }

    var premSelectEvent = function () {
        $('.premSelect').change(function () {
            showWaiting();
            clearErrorMsg();
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
            }
            dismissWaiting();
        } else if ("newPremise" == premSelectVal) {
            if (!onlyInit) {
                removeAdditional($premContent);
                clearFields($premMainContent);
                checkAddressMandatory($premContent);
                checkLocateWtihNonHcsa($premContent);
                checkPremDisabled($premContent, false);
            }
            showTag($premMainContent);
            //showTag($premContent.find('.retrieveAddr'));
            dismissWaiting();
        } else {
            showTag($premMainContent);
            if (onlyInit) {
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
        //fillForm($premContent, data, "", $('div.premContent').index($premContent));
        var suffix = $('div.premContent').index($premContent);
        fillValue($premContent.find('.vehicleNo'), data.vehicleNo);
        fillValue($premContent.find('.hciName'), data.hciName);
        fillValue($premContent.find('.postalCode'), data.postalCode);
        fillForm($premContent.find('.address'), data, "", suffix);
        fillValue($premContent.find('.addrType'), data.addrType);
        fillFloorUnit($premContent, data);

        $premContent.find('.chooseExistData').val(data.existingData);
        $premContent.find('.premSelValue').val(data.premisesSelect);
        $premContent.find('.premTypeValue').val(data.premisesType);
        $premContent.find('.premisesIndexNo').val(data.premisesIndexNo);
        //$premContent.find('.isPartEdit').val('0');

        checkAddressMandatory($premContent);
        let existData = $premContent.find('.chooseExistData').val();
        checkPremDisabled($premContent, '1' == existData);
        dismissWaiting();
    }

    function checkPremDisabled($premContent, disabled) {
        checkDisabled($premContent.find('.vehicleNo'), disabled);
        checkDisabled($premContent.find('.hciName'), disabled);
        checkDisabled($premContent.find('.postalCode'), disabled);
        toggleTag($premContent.find('.retrieveAddr'), !disabled);
        checkDisabled($premContent.find('.address'), disabled);
        checkDisabled($premContent.find('.addrType'), disabled);
        checkDisabled($premContent.find('.operationDiv'), disabled);
        toggleTag($premContent.find('.opDel:first'), !disabled);
    }

    function checkLocateWtihNonHcsa($premContent) {
        var $row = $premContent.find('.locateWtihNonHcsaRow');
        if ($row.find('input.locateWtihNonHcsa[value="1"]').is(':checked')) {
            showTag($row.next('.nonHcsaRowDiv'));
            hideTag($premContent.find('.delNonHcsaSvcRow:first'));
        } else {
            var $nonHcsaRowDiv = $row.next('.nonHcsaRowDiv');
            hideTag($nonHcsaRowDiv);
            $nonHcsaRowDiv.find('.nonHcsaRow:not(:first)').remove();
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
            clearFields($premContent.find('div.nonHcsaRowDiv'));
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
            toggleTag($(v).find('.delNonHcsaSvcRow'), k != 0);
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
        clearFields($target);
        refreshNonHcsa($premContent.find('div.nonHcsaRowDiv'), $('div.premContent').index($premContent));
        delOperationEvent($premContent);
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
            var $nonHcsaRowDiv = $(this).closest('div.nonHcsaRowDiv');
            $(this).closest('div.nonHcsaRow').remove();
            refreshNonHcsa($nonHcsaRowDiv, $('div.premContent').index($premContent));
        });
    }

    function fillFloorUnit($premContent, data) {
        if (isEmpty(data)) {
            return;
        }
        // base
        var $firstFU = $premContent.find('.operationDiv:first');
        fillValue($firstFU.find('input.floorNo'), data.floorNo);
        fillValue($firstFU.find('input.unitNo'), data.unitNo);
        // additional
        $premContent.find('div.operationDivGroup .operationDiv').remove();
        var floorUnits = data.appPremisesOperationalUnitDtos;
        var $parent = $premContent.find('div.operationDivGroup');
        var len = floorUnits.length;
        for (var i = 0; i < len; i++) {
            var $target = $parent.find('.operationDiv').eq(i);
            if ($target.length == 0) {
                addFloorUnit($parent);
                $target = $parent.find('.operationDiv').eq(i);
            }
            fillValue($target.find('input.floorNo'), floorUnits[i].floorNo);
            fillValue($target.find('input.unitNo'), floorUnits[i].unitNo);
        }
    }

    function refreshFloorUnit(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.operationDiv').each(function (i, ele) {
            if (i == 0) {
                hideTag($(ele).find('.operationAdlDiv'));
            } else {
                showTag($(ele).find('.operationAdlDiv'));
                $(ele).find('.floorUnitLabel').html('');
            }
            resetField(ele, i, prefix);
        });
        var length = $target.find('.operationDiv').length;
        $target.find('.opLength').val(length);
    }

    function addFloorUnit(ele) {
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.operationDiv:first').clone();
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.premContent').index($premContent));
        delOperationEvent($premContent);
    }

    var addOperationalEvnet = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this);
        });
    }

    var delOperationEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            var $premContent = $(this).closest('div.premContent');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.premContent').index($premContent));
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
                if (data == null) {
                    // $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    //handleVal($addressSelectors.find(':input'), '', false);
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    //$premContent.find('input[name="retrieveflag"]').val('0');
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                    //readonlyContent($addressSelectors);
                }
                dismissWaiting();
            },
            'error': function () {
                //show pop
                $('#postalCodePop').modal('show');
                clearFields($addressSelectors.find(':input'));
                unReadlyContent($addressSelectors);
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
    }
</script>