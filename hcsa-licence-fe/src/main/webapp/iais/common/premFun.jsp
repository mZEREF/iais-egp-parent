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
            <!--remove hidden-->
            $premContent.find('a.retrieveAddr').removeClass('hidden');
            $premContent.find('button.addPubHolDay').removeClass('hidden');
            <!--regen ph form  -->
            var premDivName = "";
            if("onSiteSel" == thisId){
                premDivName = 'new-premise-form-on-site';
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
                }else if ("conveyanceSel" == thisId) {
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').addClass('hidden');
                    var data = {};
                    fillForm('conveyance',data,$premContent);
                    setAddress('conveyance',data,$premContent);
                }else if('offSiteSel' == thisId){
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    $premContent.find('.new-premise-form-off-site').removeClass('hidden');
                    var data = {};
                    fillForm('offSite',data,$premContent);
                    setAddress('offSite',data,$premContent);
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
                            <!--set ph -->
                            fillPhForm(premisesType,data.appPremPhOpenPeriodList,$premContent);

                            <!--disable this form -->
                            var $premFormOnsite = $premContent.find('div.new-premise-form-on-site');
                            readonlyPartPage($premFormOnsite);
                            var $premFormConveyance = $premContent.find('div.new-premise-form-conv');
                            readonlyPartPage($premFormConveyance);
                            <!--hidden btn -->
                            $premContent.find('a.retrieveAddr').addClass('hidden');
                            $premContent.find('button.addPubHolDay').addClass('hidden');

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
            var postalCode = $postalCodeEle.find('.sitePostalCode').val();
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

                    if("conveyance" == thisId){
                        $premContent.find('.conveyanceBlockNo').val(data.blkHseNo);
                        $premContent.find('.conveyanceStreetName').val(data.streetName);
                        $premContent.find('.conveyanceBuildingName').val(data.buildingName);
                    }else{
                        $premContent.find('.siteBlockNo').val(data.blkHseNo);
                        $premContent.find('.siteStreetName').val(data.streetName);
                        $premContent.find('.siteBuildingName').val(data.buildingName);
                        /*if(null == data.addressType || ''== data.addressType){
                        }else{
                        }*/
                    }
                    $premContent.find('.siteBlockNo').prop('readonly',true);
                    $premContent.find('.siteStreetName').prop('readonly',true);
                    $premContent.find('.siteBuildingName').prop('readonly',true);
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
        $('#edit').click(function () {
            $('.premises-summary-preview').addClass('hidden');
            $('.premises-content').removeClass('hidden');
            $('#isEditHiddenVal').val('1');
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

            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(data.onsiteStartHH);
            var startHH = $premSelect.find('option[value="' + data.onsiteStartHH + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);

            $premSelect.find('select[name="'+premisesType+'StartMM"]').val(data.onsiteStartMM);
            var startMM = $premSelect.find('option[value="' + data.onsiteStartMM + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);

            $premSelect.find('select[name="'+premisesType+'EndHH"]').val(data.onsiteEndHH);
            var endHH = $premSelect.find('option[value="' + data.onsiteEndHH + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);

            $premSelect.find('select[name="'+premisesType+'EndMM"]').val(data.onsiteEndMM);
            var endMM = $premSelect.find('option[value="' + data.onsiteEndMM + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);
        }else if('conveyance' == premisesType || 'offSite' == premisesType){
            $premSelect.find('input[name="'+premisesType+'VehicleNo"]').val(data.conveyanceVehicleNo);
            $premSelect.find('input[name="'+premisesType+'BlockNo"]').val(data.conveyanceBlockNo);
            $premSelect.find('input[name="'+premisesType+'PostalCode"]').val(data.conveyancePostalCode);
            $premSelect.find('input[name="'+premisesType+'FloorNo"]').val(data.conveyanceFloorNo);
            $premSelect.find('input[name="'+premisesType+'UnitNo"]').val(data.conveyanceUnitNo);
            $premSelect.find('input[name="'+premisesType+'BuildingName"]').val(data.conveyanceBuildingName);
            $premSelect.find('input[name="'+premisesType+'StreetName"]').val(data.conveyanceStreetName);

            $premSelect.find('select[name="'+premisesType+'StartHH"]').val(data.conStartHH);
            var startHH = $premSelect.find('option[value="' + data.conStartHH + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartHH"]').next().find('.current').html(startHH);
            $premSelect.find('select[name="'+premisesType+'StartMM"]').val(data.conStartMM);
            var startMM = $premSelect.find('option[value="' + data.conStartMM + '"]').html();
            $premSelect.find('select[name="'+premisesType+'StartMM"]').next().find('.current').html(startMM);
            $premSelect.find('select[name="'+premisesType+'EndHH"]').val(data.conEndHH);
            var endHH = $premSelect.find('option[value="' + data.conEndHH + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndHH"]').next().find('.current').html(endHH);
            $premSelect.find('select[name="'+premisesType+'EndMM"]').val(data.conEndMM);
            var endMM = $premSelect.find('option[value="' + data.conEndMM + '"]').html();
            $premSelect.find('select[name="'+premisesType+'EndMM"]').next().find('.current').html(endMM);
        }
    }

    var fillPhForm = function (premisesType,phList,$Ele) {
        var currentPremVal = $Ele.find('input[name="premValue"]').val()+premisesType;
        $.each(phList,function (k,v) {
            if(k != 0){
                <!--add html and fill  -->
                var $contentDivEle;

                if(premisesType == 'onSite'){
                    $contentDivEle = $Ele.find('div.new-premise-form-on-site');
                }else if(premisesType == 'conveyanceSel'){
                    $contentDivEle = $Ele.find('div.new-premise-form-conv');
                }
                //genPubHolDayFun($Ele,$contentDivEle,'N','N');
            }
            $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PubHoliday'+k+'"]').val(v.phDateStr);
            var publicHoliday = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.phDateStr+ '"]').html();
            $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PubHoliday'+k+'"]').next().find('.current').html(publicHoliday);

            if(premisesType == 'onSite'){
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartHH'+k+'"]').val(v.onsiteStartFromHH);
                var StartFromHH = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.onsiteStartFromHH+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartHH'+k+'"]').next().find('.current').html(StartFromHH);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartMM'+k+'"]').val(v.onsiteStartFromMM);
                var StartFromMM = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.onsiteStartFromMM+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartMM'+k+'"]').next().find('.current').html(StartFromMM);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndHH'+k+'"]').val(v.onsiteEndToHH);
                var EndToHH = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.onsiteEndToHH+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndHH'+k+'"]').next().find('.current').html(EndToHH);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndMM'+k+'"]').val(v.onsiteEndToMM);
                var EndToMM = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.onsiteEndToMM+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndMM'+k+'"]').next().find('.current').html(EndToMM);

            }else if(premisesType == 'conveyance'){
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartHH'+k+'"]').val(v.convStartFromHH);
                var StartFromHH = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.convStartFromHH+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartHH'+k+'"]').next().find('.current').html(StartFromHH);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartMM'+k+'"]').val(v.convStartFromMM);
                var StartFromMM = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.convStartFromMM+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayStartMM'+k+'"]').next().find('.current').html(StartFromMM);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndHH'+k+'"]').val(v.convEndToHH);
                var EndToHH = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.convEndToHH+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndHH'+k+'"]').next().find('.current').html(EndToHH);

                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndMM'+k+'"]').val(v.convEndToMM);
                var EndToMM = $Ele.find('div.pubHolidayContent').eq(k).find('option[value="' +v.convEndToMM+ '"]').html();
                $Ele.find('div.pubHolidayContent').eq(k).find('select[name="'+currentPremVal+'PbHolDayEndMM'+k+'"]').next().find('.current').html(EndToMM);
            }
        });
    }

    var initPhForm = function (premDivName,$premContentEle) {
        <!--init ph length -->
        $premContentEle.find('input.phLength').val('1');
        <!--remove ph div -->
        $premContentEle.find('div.'+premDivName+'').find('div.pubHolidayContent').remove();

        var $contentDivEle = $premContentEle.find('div.'+premDivName+'');
        genPubHolDayFun($premContentEle,$contentDivEle,'N','Y');
    }




</script>