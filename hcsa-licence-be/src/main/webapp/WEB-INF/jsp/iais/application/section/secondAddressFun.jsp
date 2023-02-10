<script type="text/javascript">
    function initSecondAddressPage() {
        initPremiseEvents();
        let addressContent = '.premContents';
        disableContent($(addressContent));
        $(addressContent).each(function (k,v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(v).not(':empty')) {
                        $(v).find('.isPartEdit').val("1");
                        $(v).find('.isChange').val("1");
                        $('#isEditHiddenVal').val('1');
                        $(v).find('a.addressEdit').trigger('click');
                    }
                });
            }
        });
        refreshHeaderIndex();
        refreshAddress(addressContent);
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

    function initPremiseEvents() {
        addFloorUnitEvents();
        delFloorUnitEvents();
        addrTypeEvents();
        retrieveAddrEvents();
        addSecondAddressEvents();
        removeBtnEvents();
        initFormNodes();
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
        checkCount();
        showTag($premContent.find('.retrieveAddr') )
        showTag($premContent.find('.addOpDiv') )
        showRemoveBtn();
        dismissWaiting();
    }

    function showRemoveBtn() {
        showTag($('.premContents').find('.removeBtns'))
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
                showTag(premisesContent.find('.retrieveAddr'));
                hideTag(premisesContent.find('.addressEdit '));
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

    function retrieveAddrs(postalCode, target,flag) {
        var $addressSelectors = $(target);
        var data = {
            'postalCode': postalCode
        };
        if (!flag){
            dismissWaiting();
        }
        $.ajax({
            'url': '${pageContext.request.contextPath}/retrieve-address',
            'dataType': 'json',
            'data': data,
            'type': 'GET',
            'success': function (data) {
                let $currContent = $addressSelectors.closest('div.premContents');
                if (data == null) {
                    //show pop
                    if (!flag) {
                        $('#postalCodePop').modal('show');
                    }
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
                if (!flag){
                    dismissWaiting();
                }
            },
            'error': function () {
                //show pop
                if (!flag) {
                    $('#postalCodePop').modal('show');
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    let $currContent = $addressSelectors.closest('div.premContents');
                    $currContent.find('input[name="retrieveflag"]').val('0');
                    dismissWaiting();
                }
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
        hideTag($premContent.find('.removeDIV'))
        let postalCode = $premContent.find('.postalCode').val();
        if(!isEmpty(postalCode)){
            retrieveAddrs(postalCode,$premContent.find('div.address'),true)
        }

    }
</script>