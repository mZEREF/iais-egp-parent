<script type="text/javascript">
    function initSecondAddressPage() {
        let addressContent = '.premContents';
        initPremiseEvents(addressContent);
        disableContent($(addressContent));
        $(addressContent).each(function (k,v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(v).not(':empty')) {
                        $(v).find('.isPartEdit').val("1");
                        $(v).find('.isChange').val("1");
                        $('#isEditHiddenVal').val('1');
                        unDisableContent($(v));hideTag($(v).find('.removeEditDiv '));


                    }
                });
            }
        });
        refreshHeaderIndex();
        refreshAddress(addressContent);
        $('div.premContents').each(function (k, v) {
            let $target = $(v);
            let retrieveflag = $target.find('input[name="addressretrieveflags"]').val();
            if (retrieveflag == '1') {
                readonlyContent($target.find('div.address'));
            } else {
                unReadlyContent($target.find('div.address'));
            }
        });
    }

    function refreshAddress(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k,v){
            resetIndex($(v), k);
            refreshFloorUnit($(v), k);
        })
    }

    function initPremiseEvents(addrContent) {
        addFloorUnitEvents(addrContent);
        delFloorUnitEvents(addrContent);
        addrTypeEvents(addrContent);
        retrieveAddrEvents(addrContent);
        addSecondAddressEvents();
        removeBtnEvents();
        initFormNodes(addrContent);
        editPremEvents();
        checkCount();
    }

    function hideEditBtn($premContent) {
        let $target = $premContent.find('.removeEditDiv');
        if (isEmptyNode($target)) {
            return true;
        }
        return $target.is(':hidden');
    }

    function checkEditBtns($premContent, show) {
        let $target = $premContent.find('.removeEditDiv');
        if (isEmptyNode($target)) {
            return;
        }
        if ($target.is(':hidden') && show) {
            showTag($target);
        }
        if (!isEmptyNode($target.find('.addressEdit'))) {
            toggleTag($target.find('.addressEdit'), show);
        }
    }

    function addSecondAddressEvents() {
        $('#addSecondAddress').unbind('click');
        $('#addSecondAddress').on('click', addSecondEventFun);
    }

    function addSecondEventFun() {
        showWaiting();
        var $target = $('div.premContents:last');
        var src = $target.clone();
        $('div.premises-content').append(src);
        var $premContent = $('div.premContents').last();
        initFormNodes($premContent);
        clearFields($premContent);
        removeAdditional($premContent);
        refreshPremises($premContent, $('div.premContents').length - 1);
        $('#isEditHiddenVal').val('1');
        $premContent.find('.isPartEdit').val("1");
        $premContent.find('.isChange').val("1");
        removeBtnEvents();
        hideTag($premContent.find('.addressEdit'));
        retrieveAddrEvents($premContent);
        addFloorUnitEvents($premContent);
        unDisableContent($premContent)
        unReadlyContent($premContent.find('.address'));
        checkCount();
        showTag($premContent.find('.retrieveAddr') )
        showTag($premContent.find('.addOpDiv') )
        showTag($('.premContents').find('.removeBtns') )

        dismissWaiting();
    }



    function checkCount() {
        if ($('.premContents').length >= 4){
            hideTag($('.btns'))
        }else {
            showTag($('.btns'))
        }

    }

    var removeBtnEvents = function () {
        var $target = $(document);
        $target.find('.removeBtns').unbind('click');
        $target.find('.removeBtns').on('click', function () {
            let premisesContent = $(this).closest('div.premContents');
            if ($('.removeBtns').length > 1){
                premisesContent.remove();
            }
            if ($('.removeBtns').length == 1){
                premisesContent.find('.premHeader').html('');
                unDisableContent(premisesContent);
                clearFields(premisesContent);
                removeAdditional(premisesContent)
                showTag(premisesContent.find('.retrieveAddr'));
                showTag(premisesContent.find('.addOpDiv'))
                hideTag(premisesContent.find('.removeEditDiv '));
                unReadlyContent(premisesContent.find('.address'));
                premisesContent.find('.isPartEdit').val('1');
            }
            $('#isEditHiddenVal').val('1');
            $('div.premContents').each(function (k, v) {
                refreshPremises($(v), k);
/*                let value = $(v).find('.isPartEdit').val();
                if (value == 0){
                    disableContent($(v))
                }*/
            });
            checkCount();
            dismissWaiting();
        });
    }

    function refreshPremises($premContent, k) {
        var $target = getJqueryNode($premContent);
        if (isEmptyNode($target)) {
            return;
        }
        refreshHeaderIndex();
        // toggleTag($target.find('.removeDIV'), k != 0);
        resetIndex($target, k);
        refreshFloorUnit($target, k);
    }
    function refreshHeaderIndex(){
        $('.premContents').each(function (k,v){
            $(v).find('.premHeader').html(k + 1)
        })
        if ($('div.premContents').length == 1) {
            $('div.premContents').find('.premHeader').html('');
            let postalCode = $('.premContents').find('.postalCode').val();
            if (isEmpty(postalCode)){
                hideTag($('div.premContents').find('.removeBtns'))
            }else {
                showTag($('div.premContents').find('.removeBtns'))
            }

        }
    }


    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
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
        $target.find('.addressSize').val(length);
    }

    function addFloorUnits(ele) {
        var $premContent = $(ele).closest('div.premContents');
        var src = $premContent.find('div.operationDiv:first').clone();
        initFormNodes(src);
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.premContents').index($premContent));
        delFloorUnitEvents($premContent);
    }

    var addFloorUnitEvents = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnits(this);
        });
    }

    var delFloorUnitEvents = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            var $premContent = $(this).closest('div.premContents');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.premContents').index($premContent));
            doEditPremises($premContent);
        });
    }

    var addrTypeEvents = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addrType').unbind('change');
        $target.find('.addrType').on('change', function () {
            var $premContent = $(this).closest('div.premContents');
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

    var retrieveAddrEvents = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.retrieveAddr').unbind('click');
        $target.find('.retrieveAddr').on('click', function () {
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddrs(postalCode, $(this).closest('div.premContents').find('div.address'));
        });
    }

    function retrieveAddrs(postalCode, target) {
        var $addressSelectors = $(target);
        var data = {
            'postalCode': postalCode
        };

            dismissWaiting();

        $.ajax({
            'url': '${pageContext.request.contextPath}/retrieve-address',
            'dataType': 'json',
            'data': data,
            'type': 'GET',
            'success': function (data) {
                let $currContent = $addressSelectors.closest('div.premContents');
                if (data == null) {
                    //show pop

                        $('#postalCodePop').modal('show');

                    //handleVal($addressSelectors.find(':input'), '', false);
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    $currContent.find('input[name="addressretrieveflags"]').val('0');
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                    readonlyContent($addressSelectors);
                    $currContent.find('input[name="addressretrieveflags"]').val('1');
                }

                    dismissWaiting();

            },
            'error': function () {
                //show pop

                    $('#postalCodePop').modal('show');
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    let $currContent = $addressSelectors.closest('div.premContents');
                    $currContent.find('input[name="addressretrieveflags"]').val('0');
                    dismissWaiting();

            }
        });
    }

    function editPremEvents() {
        let $target = $('.addressEdit');
        if (isEmptyNode($target)) {
            return;
        }
        $target.unbind('click');
        $target.on('click', function () {
            let $premContent = $(this).closest('div.premContents');
            doEditPremises($premContent);
        });
    }

    function doEditPremises($premContent) {
        // check whether the edit button is hidden or not,
        // if not, return false, or return true
        if (hideEditBtn($premContent)) {
            return;
        }
        console.info("------------doEditPremise----------------");
        $('#isEditHiddenVal').val('1');
        $premContent.find('.isPartEdit').val('1');
        $premContent.find('.isChange').val('1');
        unDisableContent($premContent);
        showTag($premContent.find('.retrieveAddr'));
        showTag($premContent.find('.addOpDiv'));
        checkEditBtns($premContent, false);
        hideTag($premContent.find('.removeBtns'))

    }
</script>