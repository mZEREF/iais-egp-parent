<script>
    var premType = function () {
        $('.premTypeRadio').change(function () {
            var checkedType = $(this).val();
            var $premSelect = $(this).closest('div.premContent');
            var $premSelctDivEle = $(this).closest('div.premisesTypeDiv');
            clearFields($premSelect.find('.premSelect'));
            if('ONSITE'==checkedType){
                //reset indexNo for clear
                unDisabledPartPage($premSelect.find('.onSiteSelect'));
                $premSelect.find('.onSiteSelect').removeClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').addClass('hidden');
                $premSelect.find('.easMtsSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelect.find('.new-premise-form-eas-mts').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->

            }else if('CONVEYANCE' == checkedType){
                //reset indexNo for clear
                unDisabledPartPage($premSelect.find('.conveyanceSelect'));
                $premSelect.find('.conveyanceSelect').removeClass('hidden');
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').addClass('hidden');
                $premSelect.find('.easMtsSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelect.find('.new-premise-form-eas-mts').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
            }else if('OFFSITE' == checkedType){
                //reset indexNo for clear
                unDisabledPartPage($premSelect.find('.offSiteSelect'));
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.easMtsSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').removeClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelect.find('.new-premise-form-eas-mts').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->

            }else if('EASMTS' == checkedType){
                unDisabledPartPage($premSelect.find('.easMtsSelect'));
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').addClass('hidden');
                $premSelect.find('.easMtsSelect').removeClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelect.find('.new-premise-form-eas-mts').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
            }
        });
    }


    var premSelect = function(){
        $('.premSelect').change(function () {
            showWaiting();
            clearErrorMsg();
            var premSelectVal = $(this).val();
            var $premContent = $(this).closest('div.premContent');
            var thisId = $(this).attr('id');
            var premisesIndexNo = $premContent.find(".premisesIndexNo").val();
            <!--remove disable -->
            unreadonlyPartPage($premContent);
            /*remove hidden*/
            $premContent.find('a.retrieveAddr').removeClass('hidden');
            $premContent.find('button.addPubHolDay').removeClass('hidden');
            $premContent.find('span.radio-disabled').removeClass('radio-disabled');
            $premContent.find('.removePhBtn').removeClass('hidden');
            $premContent.find('.addOperational').removeClass('hidden');
            $premContent.find('.opDel').removeClass('hidden');
            $premContent.find('.addWeekly').removeClass('hidden');
            $premContent.find('.addPubHolDay').removeClass('hidden');
            $premContent.find('.addEvent').removeClass('hidden');
            //init opertation
            $premContent.find('div.operationDivGroup div.operationDiv').remove();
            $premContent.find('input[name="opLength"]').val(0);
            $premContent.find('input[name="retrieveflag"]').val(0);
            $premContent.find('input[name="weeklyLength"]').val(1);
            $premContent.find('input[name="eventLength"]').val(1);
            $premContent.find('input[name="phLength"]').val(1);
            $premContent.find('.allDay').attr('disabled',false);
            $premContent.find('.addWeeklyDiv').removeClass('hidden');
            $premContent.find('.addPhDiv').removeClass('hidden');
            $premContent.find('.addEventDiv').removeClass('hidden');
            $premContent.find('.date_picker').attr('disabled',false);
            //remove placeHolder disabled style
            $premContent.find('.date_picker').removeClass('disabled-placeHolder');
            $premContent.find('span.multi-select-button').css('border-color','');
            $premContent.find('span.multi-select-button').css('color','');
            $premContent.find('.multi-select-container input[type="checkbox"]').prop('disabled',false);
            <!--regen ph form  -->
            var premDivName = "";
            if("onSiteSel" == thisId){
                premDivName = 'new-premise-form-on-site';
                //
                /*if('0' != init){
                    replaceFireIssueDateHtml($premContent,'');
                }*/
            }else if ("conveyanceSel" == thisId) {
                premDivName = 'new-premise-form-conv';
            }else if ('offSiteSel' == thisId){
                premDivName = 'new-premise-form-off-site';
            }else if('easMtsSel' == thisId){
                premDivName = 'new-premise-form-eas-mts';
            }
            // initPhForm(premDivName,$premContent);
            if("newPremise" == premSelectVal){
                $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                if("onSiteSel" == thisId){
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    $premContent.find('.new-premise-form-eas-mts').addClass('hidden');
                    var data = {};
                    fillForm('onSite',data,$premContent);
                    setAddress('onSite',data,$premContent);
                    //initPhForm('onSite',$premContent);

                    //gen weekly html
                    var $weeklyDivEle = $premContent.find('.'+premDivName).find('div.weeklyContent');
                    genWeeklyHtml($premContent,$weeklyDivEle);
                    //gen weekly html
                    var $phDivEle = $premContent.find('.'+premDivName).find('div.pubHolDayContent');
                    genPhHtml($premContent,$phDivEle);
                    //gen event html
                    var $eventHtml = $premContent.find('.'+premDivName).find('div.eventContent');
                    genEventHtml($premContent,$eventHtml);
                }else if ("conveyanceSel" == thisId) {
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    $premContent.find('.new-premise-form-eas-mts').addClass('hidden');
                    var data = {};
                    fillForm('conveyance',data,$premContent);
                    setAddress('conveyance',data,$premContent);
                    // initPhForm('conveyance',$premContent);
                    //gen weekly html
                    var $weeklyDivEle = $premContent.find('.'+premDivName).find('div.weeklyContent');
                    genWeeklyHtml($premContent,$weeklyDivEle);
                    //gen weekly html
                    var $phDivEle = $premContent.find('.'+premDivName).find('div.pubHolDayContent');
                    genPhHtml($premContent,$phDivEle);
                    //gen event html
                    var $eventHtml = $premContent.find('.'+premDivName).find('div.eventContent');
                    genEventHtml($premContent,$eventHtml);
                }else if('offSiteSel' == thisId){
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-eas-mts').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').removeClass('hidden');
                    var data = {};
                    fillForm('offSite',data,$premContent);
                    setAddress('offSite',data,$premContent);
                    // initPhForm('offSite',$premContent);
                    //gen weekly html
                    var $weeklyDivEle = $premContent.find('.'+premDivName).find('div.weeklyContent');
                    genWeeklyHtml($premContent,$weeklyDivEle);
                    //gen weekly html
                    var $phDivEle = $premContent.find('.'+premDivName).find('div.pubHolDayContent');
                    genPhHtml($premContent,$phDivEle);
                    //gen event html
                    var $eventHtml = $premContent.find('.'+premDivName).find('div.eventContent');
                    genEventHtml($premContent,$eventHtml);
                }else if('easMtsSel' == thisId){
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    $premContent.find('.new-premise-form-eas-mts').removeClass('hidden');
                    var data = {};
                    fillForm('easMts',data,$premContent);
                    setAddress('easMts',data,$premContent);
                }
                //if Moving to new address need value from page 68859
                $("input[name='isPartEdit']").val('1');
                $("input[name='isEdit']").val('1');
                $("input[name='chooseExistData']").val('0');
                dismissWaiting();
            } else if("-1" == premSelectVal) {
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                $premContent.find('.new-premise-form-on-site').addClass('hidden');
                $premContent.find('.new-premise-form-off-site').addClass('hidden');
                var data = {};
                fillForm('onSite',data,$premContent);
                fillForm('conveyance',data,$premContent);
                fillForm('offSite',data,$premContent);
                fillForm('easMtsSel',data,$premContent);
                setAddress('onSite',data,$premContent);
                setAddress('conveyance',data,$premContent);
                setAddress('offSite',data,$premContent);
                setAddress('easMtsSel',data,$premContent);
                dismissWaiting();
            } else {
                <!--choose already exist premises -->
                var premisesType = '';
                var premiseIndex='0';
                if("onSiteSel" == thisId){
                    premisesType = 'onSite';
                    premiseIndex= $premContent.find('.premiseIndex').val();
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                }else if ("conveyanceSel" == thisId) {
                    premisesType = 'conveyance';
                    premiseIndex= $premContent.find('.premiseIndex').val();
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                }else if ("offSiteSel" == thisId){
                    premisesType = 'offSite';
                    premiseIndex= $premContent.find('.premiseIndex').val();
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').removeClass('hidden');
                }else if('easMtsSel' == thisId){
                    premisesType = 'easMts';
                    premiseIndex= $premContent.find('.premiseIndex').val();
                    $premContent.find('.new-premise-form-eas-mts').removeClass('hidden');
                }
                if(init == 0){
                    dismissWaiting();
                    return;
                }
                var jsonData = {
                    'premIndexNo': premisesIndexNo,
                    'premSelectVal': premSelectVal,
                    'premisesType': premisesType,
                    'premiseIndex': premiseIndex
                };
                $.ajax({
                    'url':'${pageContext.request.contextPath}/lic-premises',
                    'dataType':'json',
                    'data':jsonData,
                    'type':'GET',
                    'success':function (data) {
                        if (data == null) {
                            dismissWaiting();
                            return;
                        }
                        if (premisesType != '') {
                            fillForm(premisesType,data,$premContent);
                            setAddress(premisesType,data,$premContent);
                            var eqHciCode= data.eqHciCode;
                            $("input[name='isEdit']").val('1');

                            //copy ph form
                            //copyPhForm(premisesType,data.appPremPhOpenPeriodList,$premContent);
                            <!--set ph -->
                            //fillPhForm(premisesType,data.appPremPhOpenPeriodList,$premContent);
                            <!--set opertaion -->
                            var $currForm;
                            if(premisesType == 'onSite') {
                                $currForm = $premContent.find('.new-premise-form-on-site');
                            }else if(premisesType == 'conveyance'){
                                $currForm = $premContent.find('.new-premise-form-conv');
                            }else if (premisesType == 'offSite'){
                                $currForm = $premContent.find('.new-premise-form-off-site');
                            }else if(premisesType == 'easMts'){
                                $currForm = $premContent.find('.new-premise-form-eas-mts');
                                if (data.easMtsUseOnly == 'UOT002'){
                                    $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().children('label').children().remove();
                                    $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().next().children('label').children().remove();
                                }
                            }
                            $currForm.find('div.addOpDiv').before(data.operationHtml);

                            $currForm.find('div.operationDivGroup div.operationDiv').each(function (k) {
                                var opData = data.appPremisesOperationalUnitDtos[k];
                                $(this).find('input.floorNo').val(opData.floorNo);
                                $(this).find('input.unitNo').val(opData.unitNo);
                            });
                            operationDel();
                            var length = $currForm.find('div.operationDiv').length;
                            console.log('Floor and Unit (premSelect): ' + length);
                            $premContent.find('.opLength').val(length);

                            var allDayTime = "0";
                            //weekly
                            //remove weekly div
                            var $weeklyCountent = $currForm.find('div.weeklyContent');
                            if(data.weeklyDtoList != null && data.weeklyDtoList != '' && data.weeklyDtoList != undefined){
                                $weeklyCountent.find('div.weeklyDiv').remove();
                                //add html
                                $weeklyCountent.find('div.addWeeklyDiv').before(data.weeklyHtml);
                                //remove first field
                                $weeklyCountent.find('div.weeklyDiv:eq(0) div:eq(0) div:eq(0)').remove();
                                //remove first del btn
                                $weeklyCountent.find('div.weeklyDiv .weeklyDel').remove();
                                //gen multi dropdown
                                $weeklyCountent.find('select.Weekly').each(function () {
                                    $(this).multiSelect();
                                });
                                //fill data
                                $weeklyCountent.find('div.weeklyDiv').each(function (k,v) {
                                    var $thisDiv = $(this);
                                    var weeklyData = data.weeklyDtoList[k];
                                    if(weeklyData == null || weeklyData =='' || weeklyData == undefined){
                                        dismissWaiting();
                                        return;
                                    }

                                    var selectAllDay = weeklyData.selectAllDay;
                                    if(typeof(selectAllDay) === 'undefined'){
                                        selectAllDay = false;
                                    }
                                    if(selectAllDay){
                                        fillWeekly($thisDiv,premisesType,'Weekly','','','','',selectAllDay);
                                    }else{
                                        var startHHVal = weeklyData.startFromHH;
                                        if(typeof(startHHVal) === 'undefined'){
                                            startHHVal = '';
                                        }
                                        var startMMVal = weeklyData.startFromMM;
                                        if(typeof(startMMVal) === 'undefined'){
                                            startMMVal = '';
                                        }
                                        var endHHVal = weeklyData.endToHH;
                                        if(typeof(endHHVal) === 'undefined'){
                                            endHHVal = '';
                                        }
                                        var endMMVal = weeklyData.endToMM;
                                        if(typeof(endMMVal) === 'undefined'){
                                            endMMVal = '';
                                        }
                                        fillWeekly($thisDiv,premisesType,'Weekly',startHHVal,startMMVal,endHHVal,endMMVal,selectAllDay);
                                    }


                                });
                            }


                            //ph
                            var $phCountent = $currForm.find('div.pubHolDayContent');
                            //remove ph div
                            $phCountent.find('div.pubHolidayDiv').remove();
                            //add html
                            $phCountent.find('div.addPhDiv').before(data.phHtml);
                            //remove first field
                            //$phCountent.find('div.form-group:eq(0)').remove();
                            //remove first del btn
                            $phCountent.find('.pubHolidayDel').remove();
                            //gen multi dropdown
                            $phCountent.find('select.PubHoliday').each(function () {
                                $(this).multiSelect();
                            });
                            if(data.phDtoList != null && data.phDtoList != '' && data.phDtoList != undefined){
                                //fill data
                                $phCountent.find('div.pubHolidayDiv').each(function (k,v) {
                                    var $thisDiv = $(this);
                                    var phData = data.phDtoList[k];
                                    if(phData == null || phData =='' || phData == undefined){
                                        dismissWaiting();
                                        return;
                                    }

                                    var selectAllDay = phData.selectAllDay;
                                    if(typeof(selectAllDay) === 'undefined'){
                                        selectAllDay = false;
                                    }
                                    if(selectAllDay){
                                        fillWeekly($thisDiv,premisesType,'Ph','','','','',selectAllDay);
                                    }else{
                                        var startHHVal = phData.startFromHH;
                                        if(typeof(startHHVal) === 'undefined'){
                                            startHHVal = '';
                                        }
                                        var startMMVal = phData.startFromMM;
                                        if(typeof(startMMVal) === 'undefined'){
                                            startMMVal = '';
                                        }
                                        var endHHVal = phData.endToHH;
                                        if(typeof(endHHVal) === 'undefined'){
                                            endHHVal = '';
                                        }
                                        var endMMVal = phData.endToMM;
                                        if(typeof(endMMVal) === 'undefined'){
                                            endMMVal = '';
                                        }
                                        fillWeekly($thisDiv,premisesType,'Ph',startHHVal,startMMVal,endHHVal,endMMVal,selectAllDay);
                                    }
                                });
                            }
                            //event
                            //remove event div
                            var $eventContent = $currForm.find('div.eventContent');
                            if(data.eventDtoList != null && data.eventDtoList != '' && data.eventDtoList != undefined){
                                $eventContent.find('div.eventDiv').remove();
                                //add html
                                $eventContent.find('div.addEventDiv').before(data.eventHtml);
                                //remove first field

                                //remove first del btn
                                $eventContent.find('.eventDel').remove();
                                //fill data
                                $eventContent.find('div.eventDiv').each(function (k,v) {
                                    var $thisDiv = $(this);
                                    var eventData = data.eventDtoList[k];
                                    if(eventData == null || eventData =='' || eventData == undefined){
                                        dismissWaiting();
                                        return;
                                    }

                                    $thisDiv.find('input.Event').val(eventData.eventName);
                                    $thisDiv.find('input.EventStart').val(eventData.startDateStr);
                                    $thisDiv.find('input.EventEnd').val(eventData.endDateStr);
                                });
                            }


                            <!--disable this form -->
                            var $premFormOnsite = $premContent.find('div.new-premise-form-on-site');
                            readonlyPartPage($premFormOnsite);
                            var $premFormConveyance = $premContent.find('div.new-premise-form-conv');
                            readonlyPartPage($premFormConveyance);
                            var $premFormOffSite = $premContent.find('div.new-premise-form-off-site');
                            readonlyPartPage($premFormOffSite);
                            var $premFormOffSite = $premContent.find('div.new-premise-form-eas-mts');
                            readonlyPartPage($premFormOffSite);
                            <!--hidden btn -->
                            $premContent.find('a.retrieveAddr').addClass('hidden');
                            $premContent.find('button.addPubHolDay').addClass('hidden');
                            $premContent.find('.removePhBtn').addClass('hidden');
                            $premContent.find('.addOperational').addClass('hidden');
                            $premContent.find('.opDel').addClass('hidden');
                            $premContent.find('.addWeekly').addClass('hidden');
                            $premContent.find('.addPubHolDay').addClass('hidden');
                            $premContent.find('.addEvent').addClass('hidden');
                            //
                            $premContent.find('input[name="chooseExistData"]').val('1');
                            //disable allday
                            $premContent.find('.allDay').prop('disabled',true);
                            $premContent.find('.date_picker').prop('disabled',true);
                            //add placeHolder disabled style
                            $premContent.find('.date_picker').addClass('disabled-placeHolder');
                            $premContent.find('span.multi-select-button').css('border-color','#ededed');
                            $premContent.find('span.multi-select-button').css('color','#999');
                            $premContent.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
                            if (eqHciCode=='true') {
                                $("input[name='isPartEdit']").val('1');
                                $("input[name='chooseExistData']").val('0');
                                $('.premisesEdit').trigger('click');
                            } else {
                                $("input[name='chooseExistData']").val('1');
                            }
                        }
                        dismissWaiting();
                    },
                    'error':function () {
                        dismissWaiting();
                    }
                });
            }
        });
    }

    var retrieveAddr = function(){
        $('.retrieveAddr').click(function(){
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var $premContent = $(this).closest('div.premContent');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            var thisId = $(this).attr('id');
            //alert(postalCode);
            var re=new RegExp('^[0-9]*$');
            var data = {
                'postalCode':postalCode
            };
            var premType = '';
            var prefixName = '';
            if("conveyance" == thisId){
                prefixName = 'conveyance';
            }else if("onSite" == thisId){
                prefixName = 'site';
            }else if("offSite" == thisId){
                prefixName = 'offSite';
            }else if('easMts' == thisId){
                prefixName = 'easMts';
            }
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
                        $premContent.find('.'+prefixName+'BlkNo').val('');
                        $premContent.find('.'+prefixName+'StreetName').val('');
                        $premContent.find('.'+prefixName+'BuildingName').val('');

                        $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                        $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                        $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);

                        $premContent.find('input[name="retrieveflag"]').val('0');
                    }else{
                        $premContent.find('.'+prefixName+'BlkNo').val(data.blkHseNo);
                        $premContent.find('.'+prefixName+'StreetName').val(data.streetName);
                        $premContent.find('.'+prefixName+'BuildingName').val(data.buildingName);

                        $premContent.find('.'+prefixName+'BlkNo').prop('readonly',true);
                        $premContent.find('.'+prefixName+'StreetName').prop('readonly',true);
                        $premContent.find('.'+prefixName+'BuildingName').prop('readonly',true);

                        $premContent.find('input[name="retrieveflag"]').val('1');
                    }
                    dismissWaiting();
                },
                'error':function () {
                    //$postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    $premContent.find('.'+prefixName+'BlkNo').val('');
                    $premContent.find('.'+prefixName+'StreetName').val('');
                    $premContent.find('.'+prefixName+'BuildingName').val('');

                    $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                    $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                    $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);

                    $premContent.find('input[name="retrieveflag"]').val('0');
                    dismissWaiting();
                }
            });

        });
    }

    //add premises testing.......
    $('#addPremBtn').click(function () {
        //console.log($('.premContent').html());
        showWaiting();
        var data = {
            'currentLength':$('.premContent').length,
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/premises-html',
            'data':data,
            'dataType':'text',
            'type':'GET',
            'success':function (data) {
                $('div.premContent:last').after(data);
                premType();
                premSelect();
                $('.removeBtn').unbind('click');
                removePremises();
                retrieveAddr();
                $('.addPubHolDay').unbind('click');
                removePH();
                coLocation();
                cl();
                preperChange();
                addOperational();
                addPubHolDayHtml();
                addWeeklyHtml();
                addEventHtml();
                removePh();
                removeEvent();
                $('.date_picker').datepicker({
                    format:"dd/mm/yyyy",
                    autoclose:true,
                    todayHighlight:true,
                    orientation:'bottom'
                });
                <!--set Scrollbar -->
                /*$("div.premSelect->ul").mCustomScrollbar({
                        advanced:{
                            updateOnContentResize: true
                        }
                    }
                );*/
                <!--trigger tooltip -->
                $("[data-toggle='tooltip']").tooltip();
                dismissWaiting();
            },
            'error':function (data) {
                dismissWaiting();
            }
        });
    });

    var removePremises = function () {
        $('.removeBtn').click(function () {
            var $premContentEle= $(this).closest('div.premContent');
            var $pageContentEle =  $(this).closest('div.premises-content');
            $premContentEle.remove();
            <!--reset premval -->
            $pageContentEle.find('div.premContent').each(function (k,v) {
                $(this).find('input[name="premValue"]').val(k);
                $(this).find('strong.premHeader').html('Mode of Service Delivery '+(k+1));
            });
        });

    };

    var doEdit = function () {
        $('.premisesEdit').click(function () {
            var premContent = $(this).closest('.premContent');
            <!--hidden edit btn -->
            premContent.find('.premises-summary-preview').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            premContent.find('input[name="isPartEdit"]').val('1');
            var premType = premContent.find('input[name="premType"]').val();
            var existingData = premContent.find("input[name='chooseExistData']").val();
            console.log("Exist Data: " + existingData);
            if ('1' == existingData) {
                var $premSel = null;
                if ("ONSITE" == premType) {
                    $premSel = premContent.find('.onSiteSelect');
                } else if ("CONVEYANCE" == premType) {
                    $premSel = premContent.find('.conveyanceSelect');
                } else if ('OFFSITE' == premType){
                    $premSel = premContent.find('.offSiteSelect');
                } else if ('EASMTS' == premType) {
                    $premSel = premContent.find('.easMtsSelect');
                }
                if (!isEmpty($premSel) && $premSel.length > 0) {
                    unDisabledPartPage($premSel);
                    unreadonlyPartPage($premSel);
                }
                unDisabledPartPage($('#premisesType'));
                unreadonlyPartPage($('#premisesType'));
                return;
            }
            <!--unDisabled -->
            unDisabledPartPage(premContent);
            unreadonlyPartPage(premContent);
            premContent.find('.retrieveAddr').removeClass('hidden');
            <!--replace fire issued date -->
            //var fireIssueDate = premContent.find('.fireIssuedDate').val();
            //replaceFireIssueDateHtml(premContent,fireIssueDate);
            <!--remove ph hidden-->
            premContent.find('.addPubHolDay').removeClass('hidden');
            // premContent.find('div.other-lic-content span.check-circle').removeClass('radio-disabled');
            premContent.find('span.radio-disabled').removeClass('radio-disabled');
            <!--remove placeHolder disabled style -->
            premContent.find('.date_picker').removeClass('disabled-placeHolder');

            premContent.find('.addOperational').removeClass('hidden');
            premContent.find('.opDel').removeClass('hidden');
            premContent.find('button.addPubHolDay').removeClass('hidden');
            premContent.find('.removePhBtn').removeClass('hidden');

            premContent.find('.weeklyDel').removeClass('hidden');
            premContent.find('.pubHolidayDel').removeClass('hidden');
            premContent.find('.eventDel').removeClass('hidden');

            premContent.find('span.multi-select-button').css('border-color','');
            premContent.find('span.multi-select-button').css('color','');
            premContent.find('.multi-select-container input[type="checkbox"]').prop('disabled',false);

            console.log(premContent.find('.weeklyDiv').length
                + ' : ' + premContent.find('.pubHolidayDiv').length
                + ' : ' + premContent.find('.eventDiv').length);
            var premDivName = "";
            if ("ONSITE" == premType) {
                premDivName = 'new-premise-form-on-site';
            } else if ("CONVEYANCE" == premType) {
                premDivName = 'new-premise-form-conv';
            } else if ('OFFSITE' == premType){
                premDivName = 'new-premise-form-off-site';
            } else if ('EASMTS' == premType) {
                premDivName = 'new-premise-form-eas-mts';
            }

            if(premContent.find('.'+premDivName+' .weeklyDiv').length < ${weeklyCount}){
                premContent.find('.addWeeklyDiv').removeClass('hidden');
            }
            if(premContent.find('.'+premDivName+' .pubHolidayDiv').length < ${phCount}){
                premContent.find('.addPhDiv').removeClass('hidden');
            }
            if(premContent.find('.'+premDivName+' .eventDiv').length < ${eventCount}){
                premContent.find('.addEventDiv').removeClass('hidden');
            }

            premContent.find('input.allDay:checked').each(function(){
                var $allDayDiv = $(this).closest('div.all-day-div');
                disabeleForAllDay($allDayDiv);
            });
        });
    };

    var coLocation = function () {
        $('.co-location').unbind('click');
        $('.co-location').click(function () {
            var val = $(this).val();
            var $coLocationEle = $(this).closest('div.co-location-div');
            $coLocationEle.find('input.co-location-val').val(val);
        });

    };

    var removePH = function () {
        $('.removePhBtn').click(function () {
            var $pubHolidayContentEle = $(this).closest('div.pubHolidayContent');
            var $contentDivEle = $(this).closest('div.form-horizontal');
            var $premContentEle = $(this).closest('div.premContent');
            $pubHolidayContentEle.remove();
            <!--change hidden length value -->
            var length =  $contentDivEle.find('div.pubHolidayContent').length;
            $premContentEle.find('.phLength').val(length);

            <!-- get current premValue-->
            var premValue = $premContentEle.find('.premValue').val();

            var nameVal = $premContentEle.find('.premTypeValue').val();
            if('ONSITE' == nameVal){
                nameVal = 'onSite';
            }else if('CONVEYANCE' == nameVal){
                nameVal = 'conveyance';
            }else if('OFFSITE' == nameVal){
                nameVal = 'conveyance';
            }

            $contentDivEle.find('div.pubHolidayContent').each(function (k,v) {
                var publicHoliday = nameVal + 'PubHoliday';
                var PbHolDayStartHH = nameVal + 'PbHolDayStartHH';
                var PbHolDayStartMM = nameVal + 'PbHolDayStartMM';
                var PbHolDayEndHH = nameVal + 'PbHolDayEndHH';
                var PbHolDayEndMM = nameVal + 'PbHolDayEndMM';

                $(this).find('.public-holiday').attr("name",premValue+publicHoliday+k);
                $(this).find('.PbHolDayStartHH').attr("name",premValue+PbHolDayStartHH+k);
                $(this).find('.PbHolDayStartMM').attr("name",premValue+PbHolDayStartMM+k);
                $(this).find('.PbHolDayEndHH').attr("name",premValue+PbHolDayEndHH+k);
                $(this).find('.PbHolDayEndMM').attr("name",premValue+PbHolDayEndMM+k);
            });
        });
    }

    var setAddress = function (premisesType, data, $Ele) {
        var $AddrEle = $Ele;
        var $addrType;
        var addrVal = '';
        if ('onSite' == premisesType) {
            addrVal = data.addrType;
            $addrType = $AddrEle.find('select[name="onSiteAddressType"]');
        } else if ('conveyance' == premisesType) {
            addrVal = data.conveyanceAddressType;
            $addrType = $AddrEle.find('select[name="conveyanceAddressType"]');
        } else if ('offSite' == premisesType) {
            addrVal = data.conveyanceAddressType;
            $addrType = $AddrEle.find('select[name="offSiteAddressType"]');
        } else if ('easMts' == premisesType) {
            addrVal = data.easMtsAddressType;
            $addrType = $AddrEle.find('select[name="easMtsAddrType"]');
        }
        if (!isEmpty($addrType) && $addrType.length > 0) {
            fillValue($addrType, addrVal);
            $addrType.trigger('change');
        }
    }

    var fillForm = function (premisesType,data,$Ele) {
        var $premSelect = $Ele;
        if('onSite' == premisesType){
            //init localWithOther
            $premSelect.find('.other-lic-content input[type="radio"]').prop('checked',false);

            $premSelect.find('input[name="'+premisesType+'HciName"]').val(data.hciName);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.postalCode);
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.blkNo);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.floorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.unitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.buildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.streetName);
            $premSelect.find('input[name="'+premisesType+'ScdfRefNo"]').val(data.scdfRefNo);
            $premSelect.find('.fireIssuedDate').val(data.certIssuedDtStr);
            $premSelect.find('input[name="'+premisesType+'OffTelNo"]').val(data.offTelNo);
            $premSelect.find('input[name="'+premisesType+'IsOtherLic"]').val(data.locateWithOthers);
            $premSelect.find('input[name="'+premisesType+'Email"]').val(data.easMtsPubEmail);
            $premSelect.find('input.other-lic').each(function () {
                if($(this).val() == data.locateWithOthers){
                    $(this).prop("checked",true);
                    $(this).closest('div').find('.check-circle').addClass('radio-disabled');
                }
            });

            var startHHVal = data.onsiteStartHH;
            if(typeof(startHHVal) === 'undefined'){
                startHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(startHHVal);
            var startHH = $premSelect.find('.'+premisesType+'StartHH option[value="' + startHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);

            var startMMVal = data.onsiteStartMM;
            if(typeof(startMMVal) === 'undefined'){
                startMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartMM"]').val(startMMVal);
            var startMM = $premSelect.find('.'+premisesType+'StartMM option[value="' + startMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);

            var endHHVal = data.onsiteEndHH;
            if(typeof(endHHVal) === 'undefined'){
                endHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndHH"]').val(endHHVal);
            var endHH = $premSelect.find('.'+premisesType+'EndHH option[value="' + endHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);

            var endMMVal = data.onsiteEndMM;
            if(typeof(endMMVal) === 'undefined'){
                endMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndMM"]').val(endMMVal);
            var endMM = $premSelect.find('.'+premisesType+'EndMM option[value="' + endMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);
        }else if('conveyance' == premisesType){
            $premSelect.find('input[name="'+premisesType+'HciName"]').val(data.conveyanceHciName);
            $premSelect.find('input[name="'+premisesType+'VehicleNo"]').val(data.conveyanceVehicleNo);
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.conveyanceBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.conveyancePostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.conveyanceFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.conveyanceUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.conveyanceBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.conveyanceStreetName);
            $premSelect.find('input[name="'+premisesType+'Email"]').val(data.conveyanceEmail);

            // $premSelect.find('select[name="'+premisesType+'StartHH"]').val(data.conStartHH);
            // var startHH = $premSelect.find('option[value="' + data.conStartHH + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);
            // $premSelect.find('select[name="'+premisesType+'StartMM"]').val(data.conStartMM);
            // var startMM = $premSelect.find('option[value="' + data.conStartMM + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);
            // $premSelect.find('select[name="'+premisesType+'EndHH"]').val(data.conEndHH);
            // var endHH = $premSelect.find('option[value="' + data.conEndHH + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);
            // $premSelect.find('select[name="'+premisesType+'EndMM"]').val(data.conEndMM);
            // var endMM = $premSelect.find('option[value="' + data.conEndMM + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);

            //
            var startHHVal = data.conStartHH;
            if(typeof(startHHVal) === 'undefined'){
                startHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(startHHVal);
            var startHH = $premSelect.find('.'+premisesType+'StartHH option[value="' + startHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);

            var startMMVal = data.conStartMM;
            if(typeof(startMMVal) === 'undefined'){
                startMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartMM"]').val(startMMVal);
            var startMM = $premSelect.find('.'+premisesType+'StartMM option[value="' + startMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);

            var endHHVal = data.conEndHH;
            if(typeof(endHHVal) === 'undefined'){
                endHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndHH"]').val(endHHVal);
            var endHH = $premSelect.find('.'+premisesType+'EndHH option[value="' + endHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);

            var endMMVal = data.conEndMM;
            if(typeof(endMMVal) === 'undefined'){
                endMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndMM"]').val(endMMVal);
            var endMM = $premSelect.find('.'+premisesType+'EndMM option[value="' + endMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);
        }else if('offSite' == premisesType){
            $premSelect.find('input[name="'+premisesType+'HciName"]').val(data.offSiteHciName);
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.offSiteBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.offSitePostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.offSiteFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.offSiteUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.offSiteBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.offSiteStreetName);
            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(data.offSiteStartHH);
            $premSelect.find('input[name="'+premisesType+'Email"]').val(data.offSiteEmail);
            // var startHH = $premSelect.find('option[value="' + data.offSiteStartHH + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);
            // $premSelect.find('select[name="'+premisesType+'StartMM"]').val(data.offSiteStartMM);
            // var startMM = $premSelect.find('option[value="' + data.offSiteStartMM + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);
            // $premSelect.find('select[name="'+premisesType+'EndHH"]').val(data.offSiteEndHH);
            // var endHH = $premSelect.find('option[value="' + data.offSiteEndHH + '"]').html(-
            // $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);
            // $premSelect.find('select[name="'+premisesType+'EndMM"]').val(data.offSiteEndMM);
            // var endMM = $premSelect.find('option[value="' + data.offSiteEndMM + '"]').html();
            // $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);

            //
            var startHHVal = data.offSiteStartHH;
            if(typeof(startHHVal) === 'undefined'){
                startHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(startHHVal);
            var startHH = $premSelect.find('.'+premisesType+'StartHH option[value="' + startHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);

            var startMMVal = data.offSiteStartMM;
            if(typeof(startMMVal) === 'undefined'){
                startMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'StartMM"]').val(startMMVal);
            var startMM = $premSelect.find('.'+premisesType+'StartMM option[value="' + startMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);

            var endHHVal = data.offSiteEndHH;
            if(typeof(endHHVal) === 'undefined'){
                endHHVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndHH"]').val(endHHVal);
            var endHH = $premSelect.find('.'+premisesType+'EndHH option[value="' + endHHVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);

            var endMMVal = data.offSiteEndMM;
            if(typeof(endMMVal) === 'undefined'){
                endMMVal = '';
            }
            $premSelect.find('select[name="'+premisesType+'EndMM"]').val(endMMVal);
            var endMM = $premSelect.find('.'+premisesType+'EndMM option[value="' + endMMVal + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);
        }else if('easMts' == premisesType){
            $premSelect.find('input[name="'+premisesType+'HciName"]').val(data.easMtsHciName);
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.easMtsBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.easMtsPostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.easMtsFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.easMtsUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.easMtsBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.easMtsStreetName);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.easMtsBuildingName);
            $premSelect.find('input[name="'+premisesType+'PubEmail"]').val(data.easMtsPubEmail);
            $premSelect.find('input[name="'+premisesType+'PubHotline"]').val(data.easMtsPubHotline);
            //co-location
            $premSelect.find('input.co-location').each(function () {
                if($(this).val() == data.easMtsCoLocation){
                    $(this).prop("checked",true);
                    $(this).closest('div').find('.check-circle').addClass('radio-disabled');
                }else {
                    $(this).prop("checked",false);
                }
            });
            $premSelect.find('input[name="easMtsCoLocationVal"]').val(data.easMtsCoLocation);
            //use type
            $premSelect.find('input.useType').each(function () {
                if($(this).val() == data.easMtsUseOnly){
                    $(this).prop("checked",true);
                    $(this).closest('div').find('.check-circle').addClass('radio-disabled');
                }else {
                    $(this).prop("checked",false);
                }
            });
            $premSelect.find('input[name="easMtsUseOnly"]').val(data.easMtsUseOnly);
        }
    }

    var useType = function () {
        $('.useType').unbind('click')
        $('.useType').click(function () {
            var thisVal = $(this).val();
            $(this).closest('div.premContent').find('input[name="easMtsUseOnlyVal"]').val(thisVal);
        });
    }

    var fillPhForm = function (premisesType,phList,$Ele) {
        var $currPrem = $Ele;
        if(premisesType == 'onSite') {
            $currPrem = $Ele.find('.new-premise-form-on-site');
        }else if(premisesType == 'conveyance'){
            $currPrem = $Ele.find('.new-premise-form-conv');
        }else if (premisesType == 'offSite'){
            $currPrem = $Ele.find('.new-premise-form-off-site');
        }
        if(phList != null && phList.length>0){
            $currPrem.find('.pubHolidayContent').each(function (k, v) {
                var currData = phList[k];
                fillFormData(premisesType,currData,$(this));
            });
        }else{

        }

    }

    var reloadPage = function () {
        <!--for reload -->
        $('.premTypeValue').each(function (k,v) {
            var checkedType = $(this).val();
            var $premCountEle = $(this).closest('div.premContent');
            var retrieveFlag = $premCountEle.find('input[name="retrieveflag"]').val();
            if('ONSITE'==checkedType){
                $premCountEle.find('.onSiteSelect').removeClass('hidden');
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                $premCountEle.find('.new-premise-form-off-site').addClass('hidden');
                $premCountEle.find('.easMtsSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-eas-mts').addClass('hidden');
                var premSelValue =  $premCountEle.find('.onSiteSelect .premSelect').val();
                if(premSelValue == "newPremise"){
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                    $premCountEle.find('a.retrieveAddr').removeClass('hidden');
                    if('1' == retrieveFlag){
                        $premCountEle.find('input[name="onSiteBlkNo"]').prop('readonly',true);
                        $premCountEle.find('input[name="onSiteStreetName"]').prop('readonly',true);
                        $premCountEle.find('input[name="onSiteBuildingName"]').prop('readonly',true);
                    }
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormOnsite = $premCountEle.find('div.new-premise-form-on-site');
                    readonlyPartPage($premFormOnsite);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                    $premCountEle.find('div.other-lic-content .other-lic:checked').closest('div').find('span.check-circle').addClass('radio-disabled');;
                    $premCountEle.find('.removePhBtn').addClass('hidden');
                    $premCountEle.find('.addOperational').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.addWeeklyDiv').addClass('hidden');
                    $premCountEle.find('.addPhDiv').addClass('hidden');
                    $premCountEle.find('.addEventDiv').addClass('hidden');
                    $premCountEle.find('input.allDay').attr('disabled',true);
                    $premCountEle.find('.weeklyDel').addClass('hidden');
                    $premCountEle.find('.pubHolidayDel').addClass('hidden');
                    $premCountEle.find('.eventDel').addClass('hidden');
                    $premCountEle.find('.date_picker').attr('disabled',true);
                    //add placeHolder disabled style
                    $premCountEle.find('.date_picker').addClass('disabled-placeHolder');
                    $premCountEle.find('span.multi-select-button').css('border-color','#ededed');
                    $premCountEle.find('span.multi-select-button').css('color','#999');
                    $premCountEle.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
                }
            }else if('CONVEYANCE' == checkedType){
                $premCountEle.find('.conveyanceSelect').removeClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                $premCountEle.find('.new-premise-form-off-site').addClass('hidden');
                $premCountEle.find('.easMtsSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-eas-mts').addClass('hidden');
                var premSelValue =  $premCountEle.find('.conveyanceSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                    $premCountEle.find('a.retrieveAddr').removeClass('hidden');
                    if('1' == retrieveFlag){
                        $premCountEle.find('input[name="conveyanceBlkNo"]').prop('readonly',true);
                        $premCountEle.find('input[name="conveyanceStreetName"]').prop('readonly',true);
                        $premCountEle.find('input[name="conveyanceBuildingName"]').prop('readonly',true);
                    }
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-conv');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                    $premCountEle.find('.removePhBtn').addClass('hidden');
                    $premCountEle.find('.addOperational').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.addWeeklyDiv').addClass('hidden');
                    $premCountEle.find('.addPhDiv').addClass('hidden');
                    $premCountEle.find('.addEventDiv').addClass('hidden');
                    $premCountEle.find('input.allDay').attr('disabled',true);
                    $premCountEle.find('.weeklyDel').addClass('hidden');
                    $premCountEle.find('.pubHolidayDel').addClass('hidden');
                    $premCountEle.find('.eventDel').addClass('hidden');
                    $premCountEle.find('.date_picker').attr('disabled',true);
                    //add placeHolder disabled style
                    $premCountEle.find('.date_picker').addClass('disabled-placeHolder');
                    $premCountEle.find('span.multi-select-button').css('border-color','#ededed');
                    $premCountEle.find('span.multi-select-button').css('color','#999');
                    $premCountEle.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
                }
            }else if('OFFSITE' == checkedType){
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').removeClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                $premCountEle.find('.easMtsSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-eas-mts').addClass('hidden');
                var premSelValue =  $premCountEle.find('.offSiteSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-off-site').removeClass('hidden');
                    $premCountEle.find('a.retrieveAddr').removeClass('hidden');
                    if('1' == retrieveFlag){
                        $premCountEle.find('input[name="offSiteBlkNo"]').prop('readonly',true);
                        $premCountEle.find('input[name="offSiteStreetName"]').prop('readonly',true);
                        $premCountEle.find('input[name="offSiteBuildingName"]').prop('readonly',true);
                    }
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-off-site').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-off-site');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                    $premCountEle.find('.removePhBtn').addClass('hidden');
                    $premCountEle.find('.addOperational').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.addWeeklyDiv').addClass('hidden');
                    $premCountEle.find('.addPhDiv').addClass('hidden');
                    $premCountEle.find('.addEventDiv').addClass('hidden');
                    $premCountEle.find('input.allDay').attr('disabled',true);
                    $premCountEle.find('.weeklyDel').addClass('hidden');
                    $premCountEle.find('.pubHolidayDel').addClass('hidden');
                    $premCountEle.find('.eventDel').addClass('hidden');
                    $premCountEle.find('.date_picker').attr('disabled',true);
                    //add placeHolder disabled style
                    $premCountEle.find('.date_picker').addClass('disabled-placeHolder');
                    $premCountEle.find('span.multi-select-button').css('border-color','#ededed');
                    $premCountEle.find('span.multi-select-button').css('color','#999');
                    $premCountEle.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
                }

            }else if('EASMTS' == checkedType){
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                $premCountEle.find('.easMtsSelect').removeClass('hidden');
                $premCountEle.find('.new-premise-form-eas-mts').addClass('hidden');
                var premSelValue =  $premCountEle.find('.easMtsSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-eas-mts').removeClass('hidden');
                    $premCountEle.find('a.retrieveAddr').removeClass('hidden');
                    if('1' == retrieveFlag){
                        $premCountEle.find('input[name="easMtsBlkNo"]').prop('readonly',true);
                        $premCountEle.find('input[name="easMtsStreetName"]').prop('readonly',true);
                        $premCountEle.find('input[name="easMtsBuildingName"]').prop('readonly',true);
                    }
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-eas-mts').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-eas-mts');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                    $premCountEle.find('.removePhBtn').addClass('hidden');
                    $premCountEle.find('.addOperational').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.opDel').addClass('hidden');
                    $premCountEle.find('.addWeeklyDiv').addClass('hidden');
                    $premCountEle.find('.addPhDiv').addClass('hidden');
                    $premCountEle.find('.addEventDiv').addClass('hidden');
                    $premCountEle.find('input.allDay').attr('disabled',true);
                    $premCountEle.find('.weeklyDel').addClass('hidden');
                    $premCountEle.find('.pubHolidayDel').addClass('hidden');
                    $premCountEle.find('.eventDel').addClass('hidden');
                    $premCountEle.find('.date_picker').attr('disabled',true);
                    //add placeHolder disabled style
                    $premCountEle.find('.date_picker').addClass('disabled-placeHolder');
                    $premCountEle.find('span.multi-select-button').css('border-color','#ededed');
                    $premCountEle.find('span.multi-select-button').css('color','#999');
                    $premCountEle.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
                    $premCountEle.find('input.useType:checked').closest('div').find('label span.check-circle').addClass('radio-disabled');
                    $premCountEle.find('.useType').prop('disabled', true);
                }

            }
        });


    }

    var replaceFireIssueDateHtml = function($premContent,value){
        var issueDate = "dd/mm/yyyy";
        var val = '';
        if('' != value){
            issueDate = value;
            val = value;
        }
        var fireIssueDateHtml = '<input type="text" autocomplete="off" class="date_picker form-control fireIssuedDate" name="onSiteFireSafetyCertIssuedDate"  value="'+val+'" placeholder="'+ issueDate +'" maxlength="10">';
        $premContent.find('div.fireIssuedDateDiv').html(fireIssueDateHtml);
        $premContent.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });

    }

    var copyPhForm = function (premisesType,phList,$premContent) {
        if(phList != null){
            var currPhForm = $premContent;
            if(premisesType == 'onSite') {
                currPhForm = $premContent.find('.new-premise-form-on-site');
            }else if(premisesType == 'conveyance'){
                currPhForm = $premContent.find('.new-premise-form-conv');
            }else if(premisesType == 'offSite'){
                currPhForm = $premContent.find('.new-premise-form-off-site');
            }
            var phHtml = currPhForm.find('.pubHolidayContent').get(0).outerHTML;
            // console.log(phHtml);
            //remove
            currPhForm.find('.pubHolidayContent').remove();
            if(phList.length>0){
                $.each(phList,function (k,v) {
                    currPhForm.find('.phFormMarkPoint').after(phHtml);
                });
            }else{
                currPhForm.find('.phFormMarkPoint').after(phHtml);
            }


            //$("#qipa250").attr('id','newQipa250');
        }
    }

    var setDropDownDisplay = function($Ele,data){
        if(typeof(data) === 'undefined'){
            data = '';
        }
        var displayName = $Ele.find('option[value="' +data+ '"]').html();
        $Ele.find('.current').html(displayName);
    }


    var initPhForm = function (premisesType,$premContent) {
        //init form
        var currPhForm = $premContent;
        if(premisesType == 'onSite') {
            currPhForm = $premContent.find('.new-premise-form-on-site');
        }else if(premisesType == 'conveyance'){
            currPhForm = $premContent.find('.new-premise-form-conv');
        }else if(premisesType == 'offSite'){
            currPhForm = $premContent.find('.new-premise-form-off-site');
        }
        var phHtml = currPhForm.find('.pubHolidayContent').get(0).outerHTML;
        currPhForm.find('.pubHolidayContent').remove();
        currPhForm.find('.phFormMarkPoint').after(phHtml);
        /*$("div.premSelect->ul").mCustomScrollbar({
                advanced:{
                    updateOnContentResize: true
                }
            }
        );*/
        //init data
        var data = {};
        var $Ele = currPhForm.find('.pubHolidayContent:eq(0)');
        fillFormData(premisesType,data,$Ele);
    }

    var fillFormData = function (premisesType,data,$Ele) {
        if(premisesType == 'onSite') {
            var phDate = $Ele.find('.form-group:eq(0) .onSitePubHoliday');
            var startHH = $Ele.find('.form-group:eq(1) .onSitePbHolDayStartHH');
            var startMM = $Ele.find('.form-group:eq(1) .onSitePbHolDayStartMM');
            var endHH = $Ele.find('.form-group:eq(2) .onSitePbHolDayEndHH');
            var endMM = $Ele.find('.form-group:eq(2) .onSitePbHolDayEndMM');
            setDropDownDisplay(phDate,data.phDateStr);
            setDropDownDisplay(startHH,data.onsiteStartFromHH);
            setDropDownDisplay(startMM,data.onsiteStartFromMM);
            setDropDownDisplay(endHH,data.onsiteEndToHH);
            setDropDownDisplay(endMM,data.onsiteEndToMM);
        }else if(premisesType == 'conveyance'){
            var phDate = $Ele.find('.form-group:eq(0) .conveyancePubHoliday');
            var startHH = $Ele.find('.form-group:eq(1) .conveyancePbHolDayStartHH');
            var startMM = $Ele.find('.form-group:eq(1) .conveyancePbHolDayStartMM');
            var endHH = $Ele.find('.form-group:eq(2) .conveyancePbHolDayEndHH');
            var endMM = $Ele.find('.form-group:eq(2) .conveyancePbHolDayEndMM');
            setDropDownDisplay(phDate,data.phDateStr);
            setDropDownDisplay(startHH,data.convStartFromHH);
            setDropDownDisplay(startMM,data.convStartFromMM);
            setDropDownDisplay(endHH,data.convEndToHH);
            setDropDownDisplay(endMM,data.convEndToMM);
        }else if (premisesType == 'offSite'){
            var phDate = $Ele.find('.form-group:eq(0) .offSitePubHoliday');
            var startHH = $Ele.find('.form-group:eq(1) .offSitePbHolDayStartHH');
            var startMM = $Ele.find('.form-group:eq(1) .offSitePbHolDayStartMM');
            var endHH = $Ele.find('.form-group:eq(2) .offSitePbHolDayEndHH');
            var endMM = $Ele.find('.form-group:eq(2) .offSitePbHolDayEndMM');
            setDropDownDisplay(phDate,data.phDateStr);
            setDropDownDisplay(startHH,data.offSiteStartFromHH);
            setDropDownDisplay(startMM,data.offSiteStartFromMM);
            setDropDownDisplay(endHH,data.offSiteEndToHH);
            setDropDownDisplay(endMM,data.offSiteEndToMM);
        }
    }

    var addOperational = function () {
        $('.addOperational').unbind('click');
        $('.addOperational').click(function () {
            showWaiting();
            var $premContentEle = $(this).closest('div.premContent');
            var premType = $premContentEle.find('.premTypeValue').val();
            var premVal = $premContentEle.find('input[name="premValue"]').val();
            var $currPremForm = $(this).closest('div.form-horizontal');
            var opCount = $currPremForm.find('.operationDiv').length;
            var data = {
                'premIndex':premVal,
                'premType':premType,
                'opCount':opCount
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/premises-operational-html',
                'data':data,
                'dataType':'json',
                'type':'POST',
                'success':function (data) {
                    if(data.resCode == 200){
                        // console.log(data.resultJson)
                        $currPremForm.find('div.addOpDiv').before(data.resultJson+'');
                        var length = $currPremForm.find('div.operationDiv').length;
                        console.log('Floor and Unit (addOperational): ' + length);
                        $premContentEle.find('.opLength').val(length);

                        operationDel();
                    }
                    dismissWaiting();
                },
                'error':function (data) {
                    dismissWaiting();
                }
            });

        });
    }

    var operationDel = function () {
        $('.opDel').click(function () {
            var $operationDivGroup = $(this).closest('div.operationDivGroup');
            var $premContentEle = $(this).closest('div.premContent');
            var $currPremForm = $(this).closest('div.form-horizontal');
            $(this).closest('div.operationDiv').remove();
            var premValue = $premContentEle.find('.premValue').val();
            var premType = $premContentEle.find('.premTypeValue').val();
            var premTypeVal = '';
            if('ONSITE' == premType){
                premTypeVal = 'onSite';
            }else if('CONVEYANCE' == premType){
                premTypeVal = 'conveyance';
            }else if('OFFSITE' == premType){
                premTypeVal = 'offSite';
            }else if('EASMTS' == premType){
                premTypeVal = 'easMts';
            }
            $operationDivGroup.find('div.operationDiv').each(function (k,v) {
                $(this).find('input.floorNo').attr("name",premValue+premTypeVal+'FloorNo'+k);
                $(this).find('input.unitNo').attr("name",premValue+premTypeVal+'UnitNo'+k);
            });
            var length = $currPremForm.find('div.operationDiv').length;
            console.log('Floor and Unit (operationDel): ' + length);
            $premContentEle.find('.opLength').val(length);
        });
    }

    var addWeeklyHtml = function () {
        $('.addWeekly').unbind('click');
        $('.addWeekly').click(function () {
            showWaiting();
            var $premContentEle = $(this).closest('div.premContent');
            var premType = $premContentEle.find('.premTypeValue').val();
            var premVal = $premContentEle.find('input[name="premValue"]').val();
            var $currPremForm = $(this).closest('div.weeklyContent');
            var weeklyCount = $currPremForm.find('.weeklyDiv').length;
            var data = {
                'premIndex':premVal,
                'premType':premType,
                'weeklyCount':weeklyCount
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/operation-weekly-html',
                'data':data,
                'dataType':'json',
                'type':'POST',
                'success':function (data) {
                    if(data.resCode == 200){
                        // console.log(data.resultJson)
                        $currPremForm.find('div.addWeeklyDiv').before(data.resultJson+'');
                        var length =  $currPremForm.find('div.weeklyDiv').length;
                        $premContentEle.find('input[name="weeklyLength"]').val(length);

                        removeWeekly();
                        clickAllDay();
                        if(length >= '${weeklyCount}'){
                            $currPremForm.find('.addWeeklyDiv').addClass('hidden');
                        }
                        $currPremForm.find('select.Weekly').each(function () {
                            $(this).multiSelect();
                        });
                    }
                    dismissWaiting();
                },
                'error':function (data) {
                    dismissWaiting();
                }
            });

        });
    }

    var addPubHolDayHtml = function () {
        $('.addPubHolDay').unbind('click');
        $('.addPubHolDay').click(function () {
            showWaiting();
            var $premContentEle = $(this).closest('div.premContent');
            var premType = $premContentEle.find('.premTypeValue').val();
            var premVal = $premContentEle.find('input[name="premValue"]').val();
            var $currPremForm = $(this).closest('div.pubHolDayContent');
            var phCount = $currPremForm.find('.pubHolidayDiv').length;
            var data = {
                'premIndex':premVal,
                'premType':premType,
                'phCount':phCount
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/operation-public-holiday-html',
                'data':data,
                'dataType':'json',
                'type':'POST',
                'success':function (data) {
                    if(data.resCode == 200){
                        // console.log(data.resultJson)
                        $currPremForm.find('div.addPhDiv').before(data.resultJson+'');
                        var length =  $currPremForm.find('div.pubHolidayDiv').length;
                        $premContentEle.find('input[name="phLength"]').val(length);

                        removePh();
                        clickAllDay();
                        if(length >= '${phCount}'){
                            $currPremForm.find('.addPhDiv').addClass('hidden');
                        }
                        $currPremForm.find('select.PubHoliday').each(function () {
                            $(this).multiSelect();
                        });
                    }
                    dismissWaiting();
                },
                'error':function (data) {
                    dismissWaiting();
                }
            });
        });
    }


    var addEventHtml = function () {
        $('.addEvent').unbind('click');
        $('.addEvent').click(function () {
            showWaiting();
            var $premContentEle = $(this).closest('div.premContent');
            var premType = $premContentEle.find('.premTypeValue').val();
            var premVal = $premContentEle.find('input[name="premValue"]').val();
            var $currPremForm = $(this).closest('div.eventContent');
            var eventCount = $currPremForm.find('.eventDiv').length;
            var data = {
                'premIndex':premVal,
                'premType':premType,
                'eventCount':eventCount
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/operation-event-html',
                'data':data,
                'dataType':'json',
                'type':'POST',
                'success':function (data) {
                    if(data.resCode == 200){
                        // console.log(data.resultJson)
                        $currPremForm.find('div.addEventDiv').before(data.resultJson+'');
                        var length =  $currPremForm.find('div.eventDiv').length;
                        $premContentEle.find('.eventLength').val(length);
                        $('.date_picker').datepicker({
                            format:"dd/mm/yyyy",
                            autoclose:true,
                            todayHighlight:true,
                            orientation:'bottom'
                        });
                        removeEvent();
                        if(length >= '${eventCount}'){
                            $currPremForm.find('.addEventDiv').addClass('hidden');
                        }
                    }
                    dismissWaiting();
                },
                'error':function (data) {
                    dismissWaiting();
                }
            });

        });
    }

    var removeWeekly = function () {
        $('.weeklyDel').click(function () {
            var $premContent = $(this).closest('.premContent');
            var $weeklyContent = $(this).closest('.weeklyContent');
            $(this).closest('.weeklyDiv').remove();
            var weeklyLength = $weeklyContent.find('.weeklyDiv').length;
            $premContent.find('input[name="weeklyLength"]').val(weeklyLength);
            //reset name
            var premValue = $premContent.find('.premValue').val();
            var premType = $premContent.find('.premTypeValue').val();
            var premTypeVal = getPagePremType(premType);
            $weeklyContent.find('div.weeklyDiv').each(function (k) {
                // $(this).find('div.multi-select label->input').attr("name",premValue+premTypeVal+'Weekly'+k);
                var $thisWeekly = $(this);
                $thisWeekly.find('.WeeklyStartHH').attr('name',premValue+premTypeVal+'WeeklyStartHH'+k);
                $thisWeekly.find('.WeeklyStartMM').attr('name',premValue+premTypeVal+'WeeklyStartMM'+k);
                $thisWeekly.find('.WeeklyEndHH').attr('name',premValue+premTypeVal+'WeeklyEndHH'+k);
                $thisWeekly.find('.WeeklyEndMM').attr('name',premValue+premTypeVal+'WeeklyEndMM'+k);
                $thisWeekly.find('div.multi-select').children('div').children('label').each(function (weeklyCount) {
                    console.log("weeklyCount:"+weeklyCount);
                    var $thisSelect = $(this);
                    $thisSelect.find('input').attr('name',premValue+premTypeVal+'Weekly'+k);
                    $thisSelect.find('input').attr('id',premValue+'Weekly'+k+weeklyCount);
                    $thisSelect.find('label').attr('for',premValue+'Weekly'+k+weeklyCount);
                });

            });

            if(weeklyLength < '${weeklyCount}'){
                $weeklyContent.find('.addWeeklyDiv').removeClass('hidden');
            }

        });
    }

    var removePh = function () {
        $('.pubHolidayDel').click(function () {
            var $premContent = $(this).closest('.premContent');
            var $phContent = $(this).closest('.pubHolDayContent');
            $(this).closest('.pubHolidayDiv').remove();
            var phLength = $phContent.find('.pubHolidayDiv').length;
            $premContent.find('input[name="phLength"]').val(phLength);

            //reset name
            var premValue = $premContent.find('.premValue').val();
            var premType = $premContent.find('.premTypeValue').val();
            var premTypeVal = getPagePremType(premType);
            $phContent.find('div.pubHolidayDiv').each(function (k) {
                var $thisPh = $(this);
                $thisPh.find('.PhStartHH').attr('name',premValue+premTypeVal+'PhStartHH'+k);
                $thisPh.find('.PhStartMM').attr('name',premValue+premTypeVal+'PhStartMM'+k);
                $thisPh.find('.PhEndHH').attr('name',premValue+premTypeVal+'PhEndHH'+k);
                $thisPh.find('.PhEndMM').attr('name',premValue+premTypeVal+'PhEndMM'+k);
                $thisPh.find('div.multi-select').children('div').children('label').each(function (phCount) {
                    var $thisSelect = $(this);
                    $thisSelect.find('input').attr('name',premValue+premTypeVal+'PubHoliday'+k);
                    $thisSelect.find('input').attr('id',premValue+'PubHoliday'+k+phCount);
                    $thisSelect.find('label').attr('for',premValue+'PubHoliday'+k+phCount);
                });
            });

            if(phLength < '${phCount}'){
                $phContent.find('.addPhDiv').removeClass('hidden');
            }

        });
    }

    var removeEvent = function () {
        $('.eventDel').click(function () {
            var $premContent = $(this).closest('.premContent');
            var $eventContent = $(this).closest('.eventContent');
            $(this).closest('.eventDiv').remove();
            var eventLength = $eventContent.find('.eventDiv').length;
            $premContent.find('input[name="eventLength"]').val(eventLength);

            //reset name
            var premValue = $premContent.find('.premValue').val();
            var premType = $premContent.find('.premTypeValue').val();
            var premTypeVal = getPagePremType(premType);
            $eventContent.find('div.eventDiv').each(function (k) {
                console.log('k'+k);
                var $thisEvent = $(this);
                $thisEvent.find('input.Event').attr('name',premValue+premTypeVal+'Event'+k);
                $thisEvent.find('input.EventStart').attr('name',premValue+premTypeVal+'EventStart'+k);
                $thisEvent.find('input.EventEnd').attr('name',premValue+premTypeVal+'EventEnd'+k);
            });

            if(eventLength < '${eventCount}'){
                $eventContent.find('.addEventDiv').removeClass('hidden');
            }
        });
    }


    var genWeeklyHtml = function ($premContentEle,$contentDivEle) {
        var name = $premContentEle.find('.premTypeValue').val();
        var premVal = $premContentEle.find('input[name="premValue"]').val();
        var type = '';
        if('ONSITE' == name){
            type = 'onSite';
        }else if('CONVEYANCE' == name){
            type = 'conveyance';
        }else if('OFFSITE' == name){
            type = 'offSite';
        }

        var jsonData={
            'premType':type,
            'premIndex': premVal,
            'weeklyCount': '0'
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/operation-weekly-html',
            'dataType':'json',
            'data':jsonData,
            'type':'POST',
            'success':function (data) {
                if(data.resCode == '200'){
                    //remove weekly div
                    $contentDivEle.find('div.weeklyDiv').remove();
                    //init html
                    $contentDivEle.find('div.addWeeklyDiv').before(data.resultJson+'');
                    $premContentEle.find('input[name="weeklyLength"]').val(1);

                    //remove field name
                    // $contentDivEle.find('.weeklyDiv:eq(0) .div:eq(0) div.col-md-12:eq(0)').remove();
                    //console.log($contentDivEle.find('.weeklyDiv:eq(0)').children('div:eq(0)').children('div:eq(0)').html());
                    $contentDivEle.find('.weeklyDiv:eq(0)').children('div:eq(0)').children('div:eq(0)').remove();
                    //remove del btn
                    $contentDivEle.find('.weeklyDel').remove();
                    clickAllDay();
                    $contentDivEle.find('select.Weekly').each(function () {
                        $(this).multiSelect();
                    });
                }


            },
            'error':function (data) {
                //alert('error');
            }
        });
    }

    var genPhHtml = function ($premContentEle,$contentDivEle) {
        var name = $premContentEle.find('.premTypeValue').val();
        var premVal = $premContentEle.find('input[name="premValue"]').val();
        var type = '';
        if('ONSITE' == name){
            type = 'onSite';
        }else if('CONVEYANCE' == name){
            type = 'conveyance';
        }else if('OFFSITE' == name){
            type = 'offSite';
        }

        var jsonData={
            'premType':type,
            'premIndex': premVal,
            'phCount': 0
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/operation-public-holiday-html',
            'dataType':'json',
            'data':jsonData,
            'type':'POST',
            'success':function (data) {
                if(data.resCode == '200'){
                    //remove weekly div
                    $contentDivEle.find('div.pubHolidayDiv').remove();
                    //init html
                    $contentDivEle.find('div.addPhDiv').before(data.resultJson+'');
                    $premContentEle.find('input[name="phLength"]').val(1);

                    //remove field name
                    $contentDivEle.find('.pubHolidayDiv:eq(0) .div:eq(0) .col-md-12:eq(0)').remove();
                    //remove del btn
                    $contentDivEle.find('.pubHolidayDel').remove();
                    clickAllDay();
                    $contentDivEle.find('select.PubHoliday').each(function () {
                        $(this).multiSelect();
                    });
                }


            },
            'error':function () {
            }
        });
    }

    var genEventHtml = function ($premContentEle,$contentDivEle) {
        var name = $premContentEle.find('.premTypeValue').val();
        var premVal = $premContentEle.find('input[name="premValue"]').val();
        var type = '';
        if('ONSITE' == name){
            type = 'onSite';
        }else if('CONVEYANCE' == name){
            type = 'conveyance';
        }else if('OFFSITE' == name){
            type = 'offSite';
        }

        var jsonData={
            'premType':type,
            'premIndex': premVal,
            'eventCount': 0
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/operation-event-html',
            'dataType':'json',
            'data':jsonData,
            'type':'POST',
            'success':function (data) {
                if(data.resCode == '200'){
                    //remove weekly div
                    $contentDivEle.find('div.eventDiv').remove();
                    //init html
                    $contentDivEle.find('div.addEventDiv').before(data.resultJson+'');
                    $premContentEle.find('input[name="eventLength"]').val(1);

                    //remove del btn
                    $contentDivEle.find('.eventDel').remove();
                    $('.date_picker').datepicker({
                        format:"dd/mm/yyyy",
                        autoclose:true,
                        todayHighlight:true,
                        orientation:'bottom'
                    });
                }


            },
            'error':function () {
            }
        });
    }

    var getPagePremType = function (premType) {
        var premTypeVal ='';
        if('ONSITE' == premType){
            premTypeVal = 'onSite';
        }else if('CONVEYANCE' == premType){
            premTypeVal = 'conveyance';
        }else if('OFFSITE' == premType){
            premTypeVal = 'offSite';
        }else{
            premTypeVal = premType;
        }
        return premTypeVal;
    }

    var fillWeekly = function($thisDiv,premisesType,prefix,startHHVal,startMMVal,endHHVal,endMMVal,isAllDay) {
        $thisDiv.find('select.'+prefix+'StartHH').val(startHHVal);
        var startHH =$thisDiv.find('.'+prefix+'StartHH option[value="' + startHHVal + '"]').html();
        $thisDiv.find('select.'+prefix+'StartHH').next().find('.current').html(startHH);
        //

        $thisDiv.find('select.'+prefix+'StartMM').val(startMMVal);
        var startMM =$thisDiv.find('.'+prefix+'StartMM option[value="' + startMMVal + '"]').html();
        $thisDiv.find('select.'+prefix+'StartMM').next().find('.current').html(startMM);
        //

        $thisDiv.find('select.'+prefix+'EndHH').val(endHHVal);
        var endHH =$thisDiv.find('.'+prefix+'EndHH option[value="' + endHHVal + '"]').html();
        $thisDiv.find('select.'+prefix+'EndHH').next().find('.current').html(endHH);
        //

        $thisDiv.find('select.'+prefix+'EndMM').val(endMMVal);
        var endMM =$thisDiv.find('.'+prefix+'EndMM option[value="' + endMMVal + '"]').html();
        $thisDiv.find('select.'+prefix+'EndMM').next().find('.current').html(endMM);

        $thisDiv.find('.allDay').attr('checked',isAllDay);
    }

    var clickAllDay = function () {
        $('.allDay').unbind('click');
        $('.allDay').click(function () {
            var $allDayDiv = $(this).closest('div.all-day-div');
            if($(this).is(':checked')){
                disabeleForAllDay($allDayDiv);
            }else{
                unDisableContent($allDayDiv.siblings('.start-div'));
                unDisableContent($allDayDiv.siblings('.end-div'));
            }
        });
    }

    var disabeleForAllDay = function ($allDayDiv) {
        clearFields($allDayDiv.siblings('.start-div'));
        disableContent($allDayDiv.siblings('.start-div'));

        clearFields($allDayDiv.siblings('.end-div'));
        disableContent($allDayDiv.siblings('.end-div'));
    }

    var genMulti = function(){
        $('select').each(function () {
            if($(this).prop('multiple')){
                console.log('...multiple...');
                $(this).multiSelect();
            }
        });
    }
</script>