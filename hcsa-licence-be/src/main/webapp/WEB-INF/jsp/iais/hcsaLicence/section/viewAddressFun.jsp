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
        checkHightLightChanges('.premisesContent', 'newVal', 'oldVal',true);
        removeEditEvent();
        handleEditAndDelete();
    }
    let handleEditAndDelete = function () {
        if($("input:checkbox[name='editCheckbox']").length > 0){
            hideTag($('.viewPremisesEdit'));
            hideTag($('.removeEditDiv'));

        }
    }


    let retrieveAddrEvent = function () {
        $('.retrieveAddr').click(function () {
            let $postalCodeEle = $(this).closest('div.postalCodeDiv');
            let postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.viewPrem').find('div.address'));
        })
    }

    let saveEvent = function ($target,code){
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
                    "preCode" : code,
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
            console.log(JSON.stringify(list),"sendData=============>")

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
                        console.log(result.data,'resulet===============>>>>',result.data.length)
                        premSelectCallback(result.data,$target)
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
            $.each(data,function (index,items){
                if(index == 0){
                    fillInfoMation(data[0],$target,$('#oldAppSubmissionDto').val() == 'true')
                }else{
                    let $targets = $target.clone();
                    clearFields($targets)
                    $('.premisesContent').last().after($targets)
                    fillInfoMation(items,$('.premisesContent').last(),$('#oldAppSubmissionDto').val() == 'true')
                }
                editEvent()
            })
        removeEditEvent()
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

        $postalCode.attr("attr",data.postalCode)
        $addrTypes.attr("attr",addrType)
        $floorNoUnitNo.attr("attr",mm)
        $streetName.attr("attr",data.streetName)
        $buildingName.attr("attr",data.buildingName)
        $blkNo.attr("attr",data.blkNo)

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
            let numbere = value.floorNo + "-" + value.unitNo;
            let html
            if (flag){
                html = '<span class="newVal addmorecontent" attr="">'+numbere+'</span>'
                $target.find('.addmore').eq(index).find('.target').html(html)
                $target.find('.addmore').eq(index).find('.target').attr("attr",numbere)
            }else {
                html = '<span class="newVal compareTdStyle" attr="">'+numbere+'</span>'
                $target.find('.addmore').eq(index).find('.target').next().html(html)
                $target.find('.addmore').eq(index).find('.target').next().attr("attr",numbere)
            }
        })
        changeCount()
        checkHightLightChanges('.premisesContent', 'newVal', 'oldVal');
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
                    readonlyContent($addressSelectors);
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
        var $target = $('div.viewPrem:last');
        var src = $target.clone();
        $('div.adds').before(src);
        var $premContent = $('div.viewPrem').last();
        clearFields($premContent);
        $premContent.find($('.blkNoLabel .mandatory')).remove()
        $premContent.find($('.floorUnitLabel .mandatory')).remove()
        removeAdditional($premContent);
        refreshPremise($premContent, $('div.viewPrem').length - 1);
        $('div.viewPrem:first').find('.premHeader').html('1');
        init();
        unReadlyContent($premContent)
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
            let content = getEditInformationAndFill(target,$('#oldAppSubmissionDto').val() == 'true')
            console.log(content,'contetn=========================<><><>')
/*            let postalCode = '';
            if (!isEmpty(target.find(".postalCode").text())){
                postalCode = target.find(".postalCode").text().trim();
            }*/
            saveEvent(target,content)
        })
    }

    let changeCount = function (){
        let length  =  $('.premisesContent').length
        $('.premisesContent').each(function (k,v){
            if (length == 1){
                $(v).find('.assign-psn-item').html('')
                let postCode;
                if ($('#oldAppSubmissionDto').val() == 'true'){
                    postCode = $(v).find('.postalCode').html().trim();
                }else {
                    postCode = $(v).find('.oldPostaCode').children('div').html().trim();
                }
                console.log(postCode,'postCode------------??>>',isEmpty(postCode))
                if (isEmpty(postCode)){
                    hideTag($(v).find('.removeBtnss'))
                }else {
                    showTag($(v).find('.removeBtnss'))
                }
            }else {
                $(v).find('.assign-psn-item').html(k+1)
                showTag($(v).find('.removeBtnss'))
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
            let content = data.floorNo.trim() + data.blkNo.trim() + data.postalCode.trim() + data.unitNo.trim();
            $.each(appPremisesOperationalUnitDtos,function (index, value) {
                addFloorUnit(ele,flags,value);
                content += value.floorNo.trim() + value.unitNo.trim();
            })
        if (!isEmpty(postalCode.trim())){
            $premContent.find('.blkNo').prop('readonly', true);
            $premContent.find('.streetName').prop('readonly', true);
            $premContent.find('.buildingName').prop('readonly', true);
        }


        return content;
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


    function checkHightLightChanges(content, newValClass, oldValClass,flag) {
        $(content).find('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().children('.'+newValClass);
            if (newEle.length <= 0) {
                newEle = $(this).parent().prev().find('.' + newValClass);
            }
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if ($('#oldAppSubmissionDto').val() == 'false') {
                if (oldVal.length > 0 || newVal.length > 0) {
                    if (oldVal != newVal) {
                        $(this).show();
                        var newHtml = '';
                        var oldHtml = $(this).html();

                        if (flag) {
                            if (newEle.length > 0) {
                                newHtml = newEle.html();
                            }
                            $(this).html(newHtml);
                            if (newEle.length > 0) {
                                newEle.html(oldHtml);
                            }
                        }else {
                            if (newEle.length > 0) {
                                newHtml = newEle.html();
                            }
                            $(this).html(oldHtml);
                            if (newEle.length > 0) {
                                newEle.html(newHtml);
                            }
                        }
                        $(this).attr("class","newVal compareTdStyle");
                    } else {
                        $(this).hide();
                    }
                }
            }

        });
    }

    let removeEditEvent = function () {
        let $target = $(document);
        $target.find('.removeEditDiv').unbind('click');
        $target.find('.removeEditDiv').on('click', function () {
            let premisesContent = $(this).closest($('.premisesContent'));
            let index = $('.removeEditDiv').index($(this));
            let id = premisesContent.find('.id').val();
            if ($('.removeBtnss').length > 1){
                premisesContent.remove();
            }else {
                premisesContent.find('.id').val('')
                premisesContent.find('.newVal').html('')
                premisesContent.find('.floorNo-unitNo').html('-')
            }
            changeCount();
            deleteAddress(id);
        });
    };

    let deleteAddress = function (id){
        let content = {"id":id};
        $.ajax({
            'url': '${pageContext.request.contextPath}/delete-address',
            'dataType': 'json',
            'data': content,
            'type': 'GET',
            'success': function (data) {
            },
            'error': function () {
            }
        })
    }




</script>