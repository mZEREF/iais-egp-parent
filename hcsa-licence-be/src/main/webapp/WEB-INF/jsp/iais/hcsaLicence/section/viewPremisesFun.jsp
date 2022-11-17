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
        removeMandary();
        checkHightLightChange('.premisesContent', 'newVal', 'oldVal');
        console.log($('#oldAppSubmissionDto').val(),'========================>>>>>')
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
                console.log(addrType,'type==============>>>>')
                // //
                // addrType = addrType=='ADDTY002' ? 'Without Apt Blk' : (addrType == 'ADDTY001' ? 'Apt Blk' : '')
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
        fillInfoMation(data[0],$target,$('#oldAppSubmissionDto').val() == 'true')
        let length = data.length;
        // TODO
        if (length > 1){
            $.each(data.splice(1,1),function (index,items){
                let $targets = $target.clone();
                console.log("enter==================>",items)
                clearFields($targets)
                $('.premisesContent').last().after($targets)
                fillInfoMation(items,$('.premisesContent').last(),$('#oldAppSubmissionDto').val() == 'true')
                editEvent()
            })
        }
    }

    function fillInfoMation(data,$target,flag){
        $('.contents').removeClass('hidden')
        $('.viewPrem').addClass('hidden')
        $('.btns').addClass('hidden')
        let  addrType = data.addrType =='ADDTY002' ? 'Without Apt Blk' : (data.addrType == 'ADDTY001' ? 'Apt Blk' : '')
        let mm = data.floorNo + "-" + data.unitNo
        let $postalCode
        let $addrTypes
        let $blkNo
        let $floorNoUnitNo
        let $streetName
        let $buildingName
        let contentHtml
        if (flag){
             $postalCode = $target.find('.postalCode')
             $addrTypes = $target.find('.addrType')
             $blkNo = $target.find('.blkNo')
             $floorNoUnitNo = $target.find('.floorNo-unitNo')
             $streetName = $target.find('.streetName')
             $buildingName = $target.find('.buildingName')
             contentHtml  = `<div class="row addmore">
             <input type="hidden" class="othersId" value="">
            <div class="col-md-6"></div>
            <div class="col-md-6">
               <div class="col-md-6 target">

               </div>
               <div class="col-md-6">

               </div>
            </div>
            </div>`
        }else {
            $postalCode = $target.find('.postalCode').parent().next().children(":first")
            $addrTypes = $target.find('.addrType').parent().next().children(":first")
            $blkNo = $target.find('.blkNo').parent().next().children(":first")
            $floorNoUnitNo = $target.find('.floorNo-unitNo').parent().next().children(":first")
            $streetName = $target.find('.streetName').parent().next().children(":first")
            $buildingName = $target.find('.buildingName').parent().next().children(":first")
            contentHtml  = `<div class="row addmore">
             <input type="hidden" class="othersId" value="">
            <div class="col-md-6"></div>
            <div class="col-md-6">
            <div class="col-md-6 target">

            </div>
            <div class="col-md-6">

            </div>
            </div>
            </div>`
        }
        $postalCode.text(data.postalCode)
        $addrTypes.text(addrType)
        $blkNo.text(data.blkNo)
        $floorNoUnitNo.text(mm)
        $streetName.text(data.streetName)
        $buildingName.text(data.buildingName)
        $target.find('.id').val(data.id)
        $('.addmore').remove()
        $.each(data.appPremisesOperationalUnitDtos,function (index,value){
            if (index == 0){
                $target.find('.appendContent').after(contentHtml)
            }else {
                $('.addmore').last().after(contentHtml)
            }
            // TODO
            $target.find('.addmore').eq(index).find('.othersId').val(value.id)
            let numbere = value.floorNo + "-" + value.unitNo
            let html
            if (flag){
                html = '<span class="newVal addmorecontent" attr="">'+numbere+'</span>'
                $target.find('.addmore').eq(index).find('.target').html(html)
            }else {
                html = '<span class="newVal compareTdStyle" attr="">'+numbere+'</span>'
                $target.find('.addmore').eq(index).find('.target').next().html(html)
            }
        })
        changeCount()
        checkHightLightChange('.premisesContent', 'newVal', 'oldVal');
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
            removeMandary();
            $('.btns').removeClass('hidden')
            $('.save').removeClass('hidden')
            //all hide
            $('.contents').addClass('hidden');
            $('.viewPrem:not(:first)').remove()
            clearFields($('.viewPrem'))
            $('.viewPrem').find('.premHeader').html('')
            //    TODO
            getEditInformationAndFill(target,$('#oldAppSubmissionDto').val() == 'true')
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
    let getEditInformationAndFill = function ($target,flag) {
        let $postalCode
        let $addrTypes
        let $blkNo
        let $floorNoUnitNo
        let $streetName
        let $buildingName
        if (flag){
            $postalCode = $target.find('.postalCode')
            $addrTypes = $target.find('.addrType')
            $blkNo = $target.find('.blkNo')
            $floorNoUnitNo = $target.find('.floorNo-unitNo')
            $streetName = $target.find('.streetName')
            $buildingName = $target.find('.buildingName')
        }else {
            $postalCode = $target.find('.postalCode').parent().next().children(":first")
            $addrTypes = $target.find('.addrType').parent().next().children(":first")
            $blkNo = $target.find('.blkNo').parent().next().children(":first")
            $floorNoUnitNo = $target.find('.floorNo-unitNo').parent().next().children(":first")
            $streetName = $target.find('.streetName').parent().next().children(":first")
            $buildingName = $target.find('.buildingName').parent().next().children(":first")
        }
            let postalCode = $postalCode.text()
            let addrType = $addrTypes.text()
            let blkNo = $blkNo.text()
            let others =$floorNoUnitNo.text().split("-")
            let streetName = $streetName.text()
            let buildingName = $buildingName.text()
            let id = $target.find('.id').val()
            let floorNo = others[0]
            let unitNo = others[1]
            if (!unitNo){
                unitNo = ''
            }
            if (!floorNo){
                floorNo = ''
            }
            let appPremisesOperationalUnitDtos = new Array()
        $target.find('.addmore').each(function (index, item) {
                let otherId = $(item).find('.othersId').val()
                let others
                if (flag){
                    others =  $(item).find('.target').text().split("-")
                }else {
                    others =  $(item).find('.target').next().text().split("-")
                }
                let floorNos = others[0].trim();
                let unitNos = others[1].trim();
                if (!floorNos){
                    floorNos = '';
                }
                if (!unitNos) {
                    floorNos = '';
                }
                appPremisesOperationalUnitDtos.push({"id":otherId,"floorNo":floorNos,"unitNo":unitNos})
            })
            let data = {
                'id' : id,
                'postalCode': postalCode.trim(),
                'addrType': addrType.trim(),
                'blkNo': blkNo.trim(),
                'floorNo': floorNo.trim(),
                'unitNo': unitNo.trim(),
                'appPremisesOperationalUnitDtos': appPremisesOperationalUnitDtos,
                'streetName': streetName.trim(),
                'buildingName': buildingName.trim()
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
            let flags = true
            ele.find('div.operationDivGroup .operationDiv').remove();
            $.each(appPremisesOperationalUnitDtos,function (index, value) {
                addFloorUnit(ele,flags,value)
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

    let removeMandary = function () {
        $('.mandatory').removeAttr("style","")
    }


</script>