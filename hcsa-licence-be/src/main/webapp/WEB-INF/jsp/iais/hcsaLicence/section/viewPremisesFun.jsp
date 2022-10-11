<script type="text/javascript">
    $(function () {
        init()
    })

    function init() {
        addrTypeEvent()
        addOperationalEvnet();
        delOperationEvent();
        addPremEvent();
        removeBtnEvent();
        retrieveAddrEvent();
        editEvent()
        changeCount()
    }


    let retrieveAddrEvent = function () {
        $('.retrieveAddr').click(function () {
            let $postalCodeEle = $(this).closest('div.postalCodeDiv');
            let postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.viewPrem').find('div.address'));
        })
    }

    let saveEvent = function ($target){
        $('#Save').unbind('click')
        $('#Save').click(function () {
            let list = new Array();
            clearFields($target);
            $('.viewPrem').each(function (k, v) {
                // TODO
                let appPremisesOperationalUnitDtos = new Array();
                let id = $(v).find('.id').val()
                let postalCode = $(v).find('.postalCode').val()
                let addrType = $(v).find('.addressType').find("option:selected").val()
                addrType = addrType=='ADDTY002' ? 'Without Apt Blk' : (addrType == 'ADDTY001' ? 'Apt Blk' : '')
                let blkNo = $(v).find('.blkNo').val()
                let floorNo = $(v).find('.operationDiv:first').find('.floorNo').val()
                let unitNo = $(v).find('.operationDiv:first').find('.unitNo').val()
                $(v).find('.operationDivGroup .operationDiv').each(function (index, item) {
                    let floorNo = $(item).find('.floorNo').val()
                    let unitNo = $(item).find('.unitNo').val()
                    let id = $(item).find('.othersId').val()
                    let others = {
                        'floorNo' : floorNo,
                        'unitNo' : unitNo,
                        'id' : id
                    }
                    appPremisesOperationalUnitDtos.push(others)
                })
                let streetName = $(v).find('.streetName').val()
                let buildingName = $(v).find('.buildingName').val()
                let appGrpSecondAddrDto = {
                    "id" : id,
                    'postalCode': postalCode,
                    'addrType': addrType,
                    'blkNo': blkNo,
                    'floorNo': floorNo,
                    'unitNo': unitNo,
                    'appPremisesOperationalUnitDtos': appPremisesOperationalUnitDtos,
                    'streetName': streetName,
                    'buildingName': buildingName
                }
                list.push(appGrpSecondAddrDto)
            })
            console.log(list,"sendData=============>")

            <%--    var opt = {--%>
            <%--        url: '${pageContext.request.contextPath}/save-second-address',--%>
            <%--        type: 'post',--%>
            <%--        data: JSON.stringify(list),--%>
            <%--    };--%>
            <%--console.log(JSON.stringify(list),"=====d>")--%>
            <%--    console.log(list,"=====v>")--%>
            <%--    callCommonAjax(opt, "premSelectCallback", $target);--%>

            $.ajax({
                url: '${pageContext.request.contextPath}/save-second-address',
                data: JSON.stringify(list),
                dataType:"json",
                contentType: "application/json;charset=utf-8",
                type: 'post',
                success: function(result) {
                    if (result.code == "ok"){
                        premSelectCallback(result.data,$target)
                        console.log('backData==========>',result.data)
                    }
                    if (result.code == "error"){
                        clearErrorMsg($('.viewPrem'))
                        doValidationParse(result.data)
                    }
                }
            });
        })
    }

    function premSelectCallback(data, $target) {
        if (data == null || isEmptyNode($target)) {
            dismissWaiting();
            return;
        }
        fillInfoMation(data[0],$target)
        let length = data.length;
        // TODO
        if (length > 1){
            $.each(data.splice(1,1),function (index,items){
                let $targets = $target.clone();
                console.log("enter==================>",items)
                clearFields($targets)
                $('.premisesContent').last().after($targets)
                fillInfoMation(items,$('.premisesContent').last())
                editEvent()
            })
        }
    }

    function fillInfoMation(data,$target){
        $('.contents').removeClass('hidden')
        $('.viewPrem').addClass('hidden')
        $('.btns').addClass('hidden')
        $target.find('.postalCode').text(data.postalCode)
        $target.find('.addrType').text(data.addrType)
        $target.find('.blkNo').text(data.blkNo)
        // TODO  secondAddress id
        $target.find('.id').val(data.id)
        let mm = data.floorNo + "-" + data.unitNo
        $target.find('.floorNo-unitNo').text(mm)
        $target.find('.streetName').text(data.streetName)
        $target.find('.buildingName').text(data.buildingName)
        let content  = `<div class="row addmore">
             <input type="hidden" class="othersId" value="">
            <div class="col-md-6">
            </div>
        <div class="col-md-6">
            <div class="col-md-6">
                   <span class="newVal addmorecontent" attr="">
                   </span>
            </div>
        </div>
    </div>`

        $('.addmore').remove()

        $.each(data.appPremisesOperationalUnitDtos,function (index,value){
            if (index == 0){
                $target.find('.appendContent').after(content)
            }else {
                $('.addmore').last().after(content)
            }
            // TODO
            $target.find('.addmore').eq(index).find('.othersId').val(value.id)
            let numbere = value.floorNo + "-" + value.unitNo
            $target.find('.addmore').eq(index).find('.addmorecontent').text(numbere)
        })
        changeCount()
        dismissWaiting();
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
                    $('#postalCodePop').modal('show');
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                }
                dismissWaiting();
            },
            'error': function () {
                $('#postalCodePop').modal('show');
                clearFields($addressSelectors.find(':input'));
                unReadlyContent($addressSelectors);
                dismissWaiting();
            }
        });
        fillFormData()
    }

    let addrTypeEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addrType').unbind('change');
        $target.find('.addrType').on('change', function () {
            var $premContent = $(this).closest('div.viewPrem');
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

    let addOperationalEvnet = function (target) {
        let $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this);
        });
    }

    function addFloorUnit(ele,flag,value) {
        let $premContent
        if (flag){
            $premContent = $(ele)
        }else {
            $premContent =  $(ele).closest('div.viewPrem');
        }
        var src = $premContent.find('div.operationDiv').first().clone();
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.viewPrem').index($premContent));
        delOperationEvent($premContent);
        if (flag){
            fillValue($premContent.find('.operationDivGroup .operationDiv').last().find('.floorNo'),value.floorNo.trim())
            fillValue($premContent.find('.operationDivGroup .operationDiv').last().find('.unitNo'),value.unitNo.trim())
            // TODO  othersID
            fillValue($premContent.find('.operationDivGroup .operationDiv').last().find('.othersId'),value.id)
        }
    }

    let delOperationEvent = function (target) {
        let $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            let $premContent = $(this).closest('div.viewPrem');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.viewPrem').index($premContent));
        });
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
    }

    function addPremEvent() {
        $('#addPremBtn').unbind('click');
        $('#addPremBtn').on('click', addPremEventFun);
    }

    function addPremEventFun() {
        showWaiting();
        var $target = $('div.viewPrem:last');
        var src = $target.clone();
        $('div.adds').before(src);
        var $premContent = $('div.viewPrem').last();
        clearFields($premContent);
        removeAdditional($premContent);
        refreshPremise($premContent, $('div.viewPrem').length - 1);
        $('div.viewPrem:first').find('.premHeader').html('1');
        init();
        dismissWaiting();
    }

    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
    }


    function refreshPremise($premContent, k) {
        var $target = getJqueryNode($premContent);
        if (isEmptyNode($target)) {
            return;
        }
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.premHeader').html(k + 1);
        resetIndex($target, k);
        refreshFloorUnit($target, k);
    }

    let removeBtnEvent = function () {
        $('.removeBtn').click(function () {
            var $psnContentEle = $(this).closest('.viewPrem');
            $psnContentEle.remove();
            $('div.viewPrem').each(function (k, v) {
                refreshPremise($(v), k);
            });
            if ($('div.viewPrem').length == 1) {
                $('div.viewPrem').find('.premHeader').html('');
            }
            dismissWaiting();
        });
    }

    $('#addressClick').click(function () {
        var jQuery = $('#addressShowOrHidden').attr('style');
        if (jQuery.match("display: none")) {
            var a = $('#addressShowOrHidden').height();
            var b = $('#collapsePremise .panel-body').height();
            if (a > b) {
                $('#addressShowOrHidden').height(b)
            } else {
                $('#addressShowOrHidden').height(a)
            }
            $('#addressShowOrHidden').show();
            $('#hciNameShowOrHidden').hide();
        } else {
            $('#addressShowOrHidden').hide();
        }
    });

    //edit
    let editEvent = function () {
        $('.viewPremisesEdit').unbind('click')
        $('.viewPremisesEdit').click(function () {
            let target = $(this).closest('div.premisesContent');
            $('.viewPrem').removeClass('hidden');
            $('.btns').removeClass('hidden')
            $('.save').removeClass('hidden')
            //all hide
            $('.contents').addClass('hidden');
            $('.viewPrem:not(:first)').remove()
            clearFields($('.viewPrem'))
            $('.viewPrem').find('.premHeader').html('')
            //    TODO
            getEditInformationAndFill(target)
            saveEvent(target)
        })
    }

    let changeCount = function (){
        let length  =  $('.premisesContent').length
        $('.premisesContent').each(function (k,v){
            if (length == 1){
                $(v).find('.assign-psn-item').html('')
            }else {
                $(v).find('.assign-psn-item').html(k+1)
            }
        })
    }
    let getEditInformationAndFill = function (target) {
        let postalCode = target.find('.postalCode').text()
        let addrType = target.find('.addrType').text()
        let blkNo = target.find('.blkNo').text()
        let others = target.find('.floorNo-unitNo').text().split("-")
        let floorNo = others[0]
        let unitNo = others[1]
        let streetName = target.find('.streetName').text()
        let buildingName = target.find('.buildingName').text()
        let id = target.find('.id').val()
        let appPremisesOperationalUnitDtos = new Array()
        target.find('.addmore').each(function (index, item) {
            let otherId = $(item).find('.othersId').val()
            let others =  $(item).find('.addmorecontent').text().split("-")
            let floorNo = others[0]
            let unitNo = others[1]
            appPremisesOperationalUnitDtos.push({"id":otherId,"floorNo":floorNo,"unitNo":unitNo})
        })
        let data = {
            'id' : id,
            'postalCode': postalCode,
            'addrType': addrType,
            'blkNo': blkNo,
            'floorNo': floorNo,
            'unitNo': unitNo,
            'appPremisesOperationalUnitDtos': appPremisesOperationalUnitDtos,
            'streetName': streetName,
            'buildingName': buildingName
        }
        console.log('editData==============>',data)
        let $premContent = $('.viewPrem')
        fillValue($premContent.find('.postalCode'), data.postalCode.trim());
        fillValue($premContent.find('.blkNo'), data.blkNo.trim());
        fillValue($premContent.find('.operationDiv:first').find('.floorNo'), data.floorNo.trim());
        fillValue($premContent.find('.operationDiv:first').find('.unitNo'), data.unitNo.trim());
        fillValue($premContent.find('.streetName'), data.streetName.trim());
        fillValue($premContent.find('.buildingName'), data.buildingName.trim());
        fillValue($premContent.find('.id'), data.id.trim());
        let code = data.addrType.trim() == 'Without Apt Blk' ? 'ADDTY002' : (data.addrType.trim() == 'Apt Blk' ? 'ADDTY001' : '')
        fillValue($premContent.find('.addrType'), code);
        $premContent.find('.addrType').trigger('change')
        let ele = $('.viewPrem')
        let flag = true
        ele.find('div.operationDivGroup .operationDiv').remove();
        $.each(appPremisesOperationalUnitDtos,function (index, value) {
            addFloorUnit(ele,flag,value)
        })
    }
    $('#hciNameClick').click(function () {
        var jQuery = $('#hciNameShowOrHidden').attr('style');
        if (jQuery.match("display: none")) {
            var a = $('#hciNameShowOrHidden').height();
            var b = $('#collapsePremise .panel-body').height();
            if (a > b) {
                $('#hciNameShowOrHidden').height(b)
            } else {
                $('#hciNameShowOrHidden').height(a)
            }
            $('#hciNameShowOrHidden').show();
            $('#addressShowOrHidden').hide();
        } else {
            $('#hciNameShowOrHidden').hide();
        }
    });


</script>