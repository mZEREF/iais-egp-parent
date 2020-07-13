<script>
    var premType = function () {
        $('.premTypeRadio').click(function () {
            var checkedType = $(this).val();
            var $premSelect = $(this).closest('div.premContent');
            var $premSelctDivEle = $(this).closest('div.premisesTypeDiv');
            if('ONSITE'==checkedType){
                $premSelect.find('.onSiteSelect').removeClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-on-site div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);
            }else if('CONVEYANCE' == checkedType){
                $premSelect.find('.conveyanceSelect').removeClass('hidden');
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-conv div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);
            }else if('OFFSIET' == checkedType){
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.offSiteSelect').removeClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-off-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-on-site div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);

            }

        });
    }


    var premSelect = function(){
        $('.premSelect').change(function () {
            var premSelectVal = $(this).val();
            var $premContent = $(this).closest('div.premContent');
            var thisId = $(this).attr('id');
            <!--remove disable -->
            unreadonlyPartPage($premContent);
            /*remove hidden*/
            $premContent.find('a.retrieveAddr').removeClass('hidden');
            $premContent.find('button.addPubHolDay').removeClass('hidden');
            <!--regen ph form  -->
            var premDivName = "";
            if("onSiteSel" == thisId){
                premDivName = 'new-premise-form-on-site';
                //
                if('0' != init){
                    replaceFireIssueDateHtml($premContent,'');
                }
            }else if ("conveyanceSel" == thisId) {
                premDivName = 'new-premise-form-on-site';
            }else if ('offSiteSel' == thisId){
                premDivName = 'new-premise-form-off-site';
            }
            // initPhForm(premDivName,$premContent);
            if("newPremise" == premSelectVal){
                $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                if("onSiteSel" == thisId){
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    var data = {};
                    fillForm('onSite',data,$premContent);
                    setAddress('onSite',data,$premContent);
                    initPhForm('onSite',$premContent);
                }else if ("conveyanceSel" == thisId) {
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    var data = {};
                    fillForm('conveyance',data,$premContent);
                    setAddress('conveyance',data,$premContent);
                    initPhForm('conveyance',$premContent);
                }else if('offSiteSel' == thisId){
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').removeClass('hidden');
                    var data = {};
                    fillForm('offSite',data,$premContent);
                    setAddress('offSite',data,$premContent);
                    initPhForm('offSite',$premContent);
                }
            }else if("-1" == premSelectVal){
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                $premContent.find('.new-premise-form-on-site').addClass('hidden');
                var data = {};
                fillForm('onSite',data,$premContent);
                fillForm('conveyance',data,$premContent);
                fillForm('offSite',data,$premContent);
                setAddress('onSite',data,$premContent);
                setAddress('conveyance',data,$premContent);
                setAddress('offSite',data,$premContent);
            }else{
                <!--choose already exist premises -->
                var premisesType = '';
                if("onSiteSel" == thisId){
                    premisesType = 'onSite';
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                }else if ("conveyanceSel" == thisId) {
                    premisesType = 'conveyance';
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                }else if ("offSiteSel" == thisId){
                    premisesType = 'offSite';
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').removeClass('hidden');
                }
                if(init == 0){
                    return;
                }
                var jsonData = {
                    'premIndexNo':premSelectVal
                };
                $.ajax({
                    'url':'${pageContext.request.contextPath}/lic-premises',
                    'dataType':'json',
                    'data':jsonData,
                    'type':'GET',
                    'success':function (data) {
                        if(data == null){
                            return;
                        }
                        if(premisesType != ''){
                            fillForm(premisesType,data,$premContent);
                            setAddress(premisesType,data,$premContent);
                            //copy ph form
                            copyPhForm(premisesType,data.appPremPhOpenPeriodList,$premContent);
                            <!--set ph -->
                            fillPhForm(premisesType,data.appPremPhOpenPeriodList,$premContent);

                            <!--disable this form -->
                            var $premFormOnsite = $premContent.find('div.new-premise-form-on-site');
                            readonlyPartPage($premFormOnsite);
                            var $premFormConveyance = $premContent.find('div.new-premise-form-conv');
                            readonlyPartPage($premFormConveyance);
                            var $premFormOffSite = $premContent.find('div.new-premise-form-off-site');
                            readonlyPartPage($premFormOffSite);
                            <!--hidden btn -->
                            $premContent.find('a.retrieveAddr').addClass('hidden');
                            $premContent.find('button.addPubHolDay').addClass('hidden');
                            //
                            $premContent.find('input[name="chooseExistData"]').val('1');
                        }
                    },
                    'error':function () {
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
            var errMsg = '';
            if(''== postalCode ){
                errMsg = 'the postal code could not be null';
            }else if(postalCode.length != 6){
                errMsg = 'the postal code length must be 6';
            }else if(!re.test(postalCode)){
                errMsg = 'the postal code must be numbers';
            }
            if("" != errMsg){
                $postalCodeEle.find('.postalCodeMsg').html(errMsg);
                return;
            }
            var data = {
                'postalCode':postalCode
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/retrieve-address',
                'dataType':'json',
                'data':data,
                'type':'GET',
                'success':function (data) {
                    if(data == null){
                        $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                        return;
                    }
                    var premType = '';
                    var prefixName = '';
                    if("conveyance" == thisId){
                        prefixName = 'conveyance';
                    }else if("onSite" == thisId){
                        prefixName = 'site';
                    }else if("offSite" == thisId){
                        prefixName = 'offSite';
                    }
                    $premContent.find('.'+prefixName+'BlkNo').val(data.blkHseNo);
                    $premContent.find('.'+prefixName+'StreetName').val(data.streetName);
                    $premContent.find('.'+prefixName+'BuildingName').val(data.buildingName);

                    $premContent.find('.'+prefixName+'BlkNo').prop('readonly',true);
                    $premContent.find('.'+prefixName+'StreetName').prop('readonly',true);
                    $premContent.find('.'+prefixName+'BuildingName').prop('readonly',true);
                },
                'error':function () {
                    $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                }
            });

        });
    }

    //add premises testing.......
    $('#addPremBtn').click(function () {
        //console.log($('.premContent').html());
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
                addPubHolDay();
                removePH();
                otherLic();
                cl();
                preperChange();
                $('.date_picker').datepicker({
                    format:"dd/mm/yyyy"
                });
                <!--set Scrollbar -->
                $("div.premSelect->ul").mCustomScrollbar({
                        advanced:{
                            updateOnContentResize: true
                        }
                    }
                );
                <!--trigger tooltip -->
                $("[data-toggle='tooltip']").tooltip();
            },
            'error':function (data) {
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
                $(this).find('strong.premHeader').html('Premises '+(k+1));
            });
        });

    }

    var doEdit = function () {
        $('.premisesEdit').click(function () {
            var premContent = $(this).closest('.premContent');
            <!--hidden edit btn -->
            premContent.find('.premises-summary-preview').addClass('hidden');
            <!--unDisabled -->
            unDisabledPartPage(premContent);
            unreadonlyPartPage(premContent);
            premContent.find('.retrieveAddr').removeClass('hidden');
            $('#isEditHiddenVal').val('1');
            premContent.find('input[name="isPartEdit"]').val('1');
            <!--replace fire issued date -->
            var fireIssueDate = premContent.find('.fireIssuedDate').val();
            replaceFireIssueDateHtml(premContent,fireIssueDate);
            <!--remove ph hidden-->
            premContent.find('.addPubHolDay').removeClass('hidden');
        });
    }

    var otherLic = function () {
        $('.other-lic').click(function () {
            var val = $(this).val();
            var $otherLicEle = $(this).closest('div.other-lic-content');
            $otherLicEle.find('input[name="onSiteIsOtherLic"]').val(val);
        });

    }

    var addPubHolDay = function () {
        $('.addPubHolDay').click(function () {

            var $premContentEle = $(this).closest('div.premContent');
            var $contentDivEle = $(this).closest('div.form-horizontal');
            // genPubHolDayFun($premContentEle,$contentDivEle,'Y');
            var name = $premContentEle.find('.premTypeValue').val();
            var premVal = $premContentEle.find('input[name="premValue"]').val();
            console.log("Name"+name);
            console.log("premVal"+premVal);
            var type = '';
            if('ONSITE' == name){
                name = premVal+'onSite';
                type = 'onSite';
            }else if('CONVEYANCE' == name){
                name = premVal+"conveyance";
                type = 'conveyance';
            }else if('OFFSIET' == name){
                name = premVal+"offSite";
                type = 'offSite';
            }
            var currentLength = $contentDivEle.find('div.pubHolidayContent').length;
            var jsonData={
                'type':type,
                'premVal': name,
                'phLength': currentLength
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/public-holiday-html',
                'dataType':'text',
                'data':jsonData,
                'type':'GET',
                'success':function (data) {
                    if(data == null){
                        return;
                    }
                    <!--use ph mark point -->
                    $contentDivEle.find('div.phFormMarkPoint').addClass('pubHolidayContent');
                    <!--add html -->
                    $contentDivEle.find('div.pubHolidayContent:last').after(data);
                    <!--init ph mark point -->
                    $contentDivEle.find('div.phFormMarkPoint').removeClass('pubHolidayContent');
                    <!--change hidden length value -->
                    var length = $contentDivEle.find('div.pubHolidayContent').length;
                    $premContentEle.find('.phLength').val(length);
                    //Prevent duplicate binding
                    $('.removePhBtn').unbind('click');
                    removePH();

                    $("div.premSelect->ul").mCustomScrollbar({
                            advanced:{
                                updateOnContentResize: true
                            }
                        }
                    );
                },
                'error':function () {
                }
            });
        });
    }

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


    var setAddress = function(premisesType,data,$Ele){
        var $AddrEle = $Ele;

        if('onSite' == premisesType){
            var addrVal = data.addrType;
            if(addrVal == undefined){
                addrVal = '';
            }
            $AddrEle.find('select[name="onSiteAddressType"]').val(addrVal);
            var addressVal = $AddrEle.find('option[value="' + addrVal + '"]').html();
            $AddrEle.find('select[name="onSiteAddressType"]').next().find('.current').html(addressVal);
        }else if('conveyance' == premisesType){
            var addrVal = data.conveyanceAddressType;
            if(addrVal == undefined){
                addrVal = '';
            }
            $AddrEle.find('select[name="conveyanceAddressType"]').val(addrVal);
            var addressVal = $AddrEle.find('option[value="' + addrVal + '"]').html();
            $AddrEle.find('select[name="conveyanceAddrType"]').next().find('.current').html(addressVal);
        }else if('offSite' == premisesType){
            var addrVal = data.conveyanceAddressType;
            if(addrVal == undefined){
                addrVal = '';
            }
            $AddrEle.find('select[name="offSiteAddressType"]').val(addrVal);
            var addressVal = $AddrEle.find('option[value="' + addrVal + '"]').html();
            $AddrEle.find('select[name="offSiteAddrType"]').next().find('.current').html(addressVal);
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
            $premSelect.find('input[name="'+premisesType+'FireSafetyCertIssuedDate"]').val(data.certIssuedDtStr);
            $premSelect.find('input[name="'+premisesType+'OffTelNo"]').val(data.offTelNo);
            $premSelect.find('input[name="'+premisesType+'IsOtherLic"]').val(data.locateWithOthers);
            $premSelect.find('input.other-lic').each(function () {
                if($(this).val() == data.locateWithOthers){
                    $(this).prop("checked",true);
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
            $premSelect.find('input[name="'+premisesType+'VehicleNo"]').val(data.conveyanceVehicleNo);
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.conveyanceBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.conveyancePostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.conveyanceFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.conveyanceUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.conveyanceBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.conveyanceStreetName);

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
            $premSelect.find('input[name="'+premisesType+'BlkNo"]').val(data.offSiteBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.offSitePostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.offSiteFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.offSiteUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.offSiteBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.offSiteStreetName);
            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(data.offSiteStartHH);
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
        }
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
        $currPrem.find('.pubHolidayContent').each(function (k, v) {
            var currData = phList[k];
            fillFormData(premisesType,currData,$(this));
        });
    }

    var reloadPage = function () {
        <!--for reload -->
        $('.premTypeValue').each(function (k,v) {
            checkedType = $(this).val();
            var $premCountEle = $(this).closest('div.premContent');
            if('ONSITE'==checkedType){
                $premCountEle.find('.onSiteSelect').removeClass('hidden');
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                $premCountEle.find('.new-premise-form-off-site').addClass('hidden');
                var premSelValue =  $premCountEle.find('.onSiteSelect .premSelect').val();
                if(premSelValue == "newPremise"){
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormOnsite = $premCountEle.find('div.new-premise-form-on-site');
                    readonlyPartPage($premFormOnsite);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                }
            }else if('CONVEYANCE' == checkedType){
                $premCountEle.find('.conveyanceSelect').removeClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                $premCountEle.find('.new-premise-form-off-site').addClass('hidden');
                var premSelValue =  $premCountEle.find('.conveyanceSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-conv');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                }
            }else if('OFFSIET' == checkedType){
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.offSiteSelect').removeClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                var premSelValue =  $premCountEle.find('.offSiteSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-off-site').removeClass('hidden');
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-off-site').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-off-site');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
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
            format:"dd/mm/yyyy"
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
            //remove
            currPhForm.find('.pubHolidayContent').remove();
            $.each(phList,function (k,v) {
                currPhForm.find('.phFormMarkPoint').after(phHtml);
            });
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


</script>