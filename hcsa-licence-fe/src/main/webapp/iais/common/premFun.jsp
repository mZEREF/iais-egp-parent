<script>
    var premType = function () {
        $('.premTypeRadio').click(function () {
            var checkedType = $(this).val();
            var $premSelect = $(this).closest('div.premContent');
            var $premSelctDivEle = $(this).closest('div.premisesTypeDiv');
            if('ONSITE'==checkedType){
                $premSelect.find('.onSiteSelect').removeClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-on-site div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);

            }else if('CONVEYANCE' == checkedType){
                $premSelect.find('.conveyanceSelect').removeClass('hidden');
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-conv div.pubHolidayContent').length;
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
            }
            initPhForm(premDivName,$premContent);

            if("newPremise" == premSelectVal){
                $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                if("onSiteSel" == thisId){
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                    var data = {};
                    fillForm('onSite',data,$premContent);
                    setAddress('',$premContent);
                }else if ("conveyanceSel" == thisId) {
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
                    var data = {};
                    fillForm('conveyanceSel',data,$premContent);
                    setAddress('',$premContent);
                }
            }else if("-1" == premSelectVal){
                $premContent.find('.new-premise-form-conv').addClass('hidden');
                $premContent.find('.new-premise-form-on-site').addClass('hidden');
                var data = {};
                fillForm('onSite',data,$premContent);
                fillForm('conveyanceSel',data,$premContent);
                setAddress('',$premContent);
            }else{
                <!--choose already exist premises -->
                var premisesType = '';
                if("onSiteSel" == thisId){
                    premisesType = 'onSite';
                    $premContent.find('.new-premise-form-on-site').removeClass('hidden');
                    $premContent.find('.new-premise-form-conv').addClass('hidden');
                }else if ("conveyanceSel" == thisId) {
                    premisesType = 'conveyance';
                    $premContent.find('.new-premise-form-conv').removeClass('hidden');
                    $premContent.find('.new-premise-form-on-site').addClass('hidden');
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
                            setAddress(data.addrType,$premContent);
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
                    if("conveyance" == thisId){
                        $premContent.find('.conveyanceBlockNo').val(data.blkHseNo);
                        $premContent.find('.conveyanceStreetName').val(data.streetName);
                        $premContent.find('.conveyanceBuildingName').val(data.buildingName);
                    }else{
                        $premContent.find('.siteBlockNo').val(data.blkHseNo);
                        $premContent.find('.siteStreetName').val(data.streetName);
                        $premContent.find('.siteBuildingName').val(data.buildingName);
                        if(null == data.addressType || ''== data.addressType){


                        }else{


                        }

                        $premContent.find('.siteBlockNo').prop('readonly',true);
                        $premContent.find('.siteStreetName').prop('readonly',true);
                        $premContent.find('.siteBuildingName').prop('readonly',true);
                    }

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
            'currentLength':$('.premContent').length
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

    <!--$contentDivEle:cuurent nearest new-premise-form-on-site/ new-premise-form-on-site->form-horizontal -->
    var genPubHolDayFun = function ($premContentEle,$contentDivEle,canDel,canEdit) {
        var name = $premContentEle.find('.premTypeValue').val();
        var premVal = $premContentEle.find('input[name="premValue"]').val();
        if('ONSITE' == name){
            name = premVal+'onSite';
        }else if('CONVEYANCE' == name){
            name = premVal+"conveyance";
        }
        var currentLength = $contentDivEle.find('div.pubHolidayContent').length;

        //onsite conv
        var pubHolDayHtml = "<div class=\"pubHolidayContent\">"
            + " <div class=\"form-group\">"
            + "<label class=\"col-xs-12 col-md-4 control-label\">Select Public Holiday</label>"
            + "<div class=\" col-xs-7 col-sm-4 col-md-5\">"
            + "<input type=\"text\" autocomplete=\"off\" class=\"date_picker form-control form_datetime public-holiday \"  name=\""+name+"PubHoliday"+currentLength+"\" data-date-start-date=\"01/01/1900\" placeholder=\"dd/mm/yyyy\" maxlength=\"10\"><span id=\"error_onsitePubHoliday\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>"
            + "</div>"
            + "<div class=\" col-xs-7 col-sm-4 col-md-3\">"
            + "<div class=\"form-check\">";
        if('Y'==canDel){
            pubHolDayHtml = pubHolDayHtml+ "<strong class=\"removePhBtn\">X</strong>";
        }
        pubHolDayHtml = pubHolDayHtml + "</div>"
            + "</div>"
            + "<div class=\"clear\"></div></div>"
            + "<div class=\"form-group\">"
            + "<label class=\"col-xs-12 col-md-4 control-label\">"
            + " Public Holidays Operating Hours (Start)"
            + "</label>"
            + "<div class=\"col-xs-9 col-sm-5 col-md-4\">"
            + "<input type=\"text\" class=\"PbHolDayStartHH\"  name=\""+name+"PbHolDayStartHH"+currentLength+"\" value=\"\" maxlength=\"2\" style=\"width: 60px;;margin-right: 2%\">(HH) :"
            + "<input type=\"text\" class=\"PbHolDayStartMM\"  name=\""+name+"PbHolDayStartMM"+currentLength+"\" value=\"\" maxlength=\"2\" style=\"width: 60px;margin-right: 2%;margin-left: 2%\">(MM)"
            + " </div>"
            + "</div>"
            + "<div class=\"form-group\">"
            + "<label class=\"col-xs-12 col-md-4 control-label\">"
            + "Public Holidays Operating Hours (End)"
            + "</label>"
            + "<div class=\"col-xs-9 col-sm-5 col-md-4\">"
            + "<input type=\"text\" class=\"PbHolDayEndHH\"  name=\""+name+"PbHolDayEndHH"+currentLength+"\" value=\"\" maxlength=\"2\" style=\"width: 60px;margin-right: 2%\">(HH) :"
            + "<input type=\"text\" class=\"PbHolDayEndMM\"  name=\""+name+"PbHolDayEndMM"+currentLength+"\" value=\"\" maxlength=\"2\" style=\"width: 60px;margin-right: 2%;margin-left: 2%\">(MM)"
            + "</div>"
            + "</div>"
            + "</div>";

        if("Y" ==canEdit){

        }

        <!--use ph mark point -->
        $contentDivEle.find('div.phFormMarkPoint').addClass('pubHolidayContent');
        <!--add html -->
        $contentDivEle.find('div.pubHolidayContent:last').after(pubHolDayHtml);
        <!--init ph mark point -->
        $contentDivEle.find('div.phFormMarkPoint').removeClass('pubHolidayContent');

        $('.date_picker').datepicker({
            format:"dd/mm/yyyy"
        });

        <!--change hidden length value -->
        var length = $contentDivEle.find('div.pubHolidayContent').length;
        $premContentEle.find('.phLength').val(length);

        //Prevent duplicate binding
        $('.removePhBtn').unbind('click');
        removePH();

    }

    var addPubHolDay = function () {
        $('.addPubHolDay').click(function () {
            var $premContentEle = $(this).closest('div.premContent');
            var $contentDivEle = $(this).closest('div.form-horizontal');
            genPubHolDayFun($premContentEle,$contentDivEle,'Y');
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


    var setAddress = function(data,$Ele){
        var $AddrEle = $Ele;
        $AddrEle.find('select[name="onSiteAddressType"]').val(data);
        var addressVal = $AddrEle.find('option[value="' + data + '"]').html();
        $AddrEle.find('select[name="onSiteAddressType"]').next().find('.current').html(addressVal);
        $AddrEle.find('select[name="conveyanceAddressType"]').next().find('.current').html(addressVal);
    }

    var fillForm = function (premisesType,data,$Ele) {
        var $premSelect = $Ele;
        $premSelect.find('input[name="'+premisesType+'VehicleNo"]').val(data.hciName);
        $premSelect.find('input[name="'+premisesType+'BlockNo"]').val(data.blkNo);
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
        $premSelect.find('input[name="'+premisesType+'StartHH"]').val(data.onsiteStartHH);
        $premSelect.find('input[name="'+premisesType+'StartMM"]').val(data.onsiteStartMM);
        $premSelect.find('input[name="'+premisesType+'EndHH"]').val(data.onsiteEndHH);
        $premSelect.find('input[name="'+premisesType+'EndMM"]').val(data.onsiteEndMM);

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
                genPubHolDayFun($Ele,$contentDivEle,'N','N');
            }
            $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PubHoliday"+k+'"]').val(v.phDateStr);
            if(premisesType == 'onSite'){
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayStartHH"+k+'"]').val(v.onsiteStartFromHH);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayStartMM"+k+'"]').val(v.onsiteStartFromMM);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayEndHH"+k+'"]').val(v.onsiteEndToHH);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayEndMM"+k+'"]').val(v.onsiteEndToMM);
            }else if(premisesType == 'conveyance'){
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayStartHH"+k+'"]').val(v.convStartFromHH);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayStartMM"+k+'"]').val(v.convStartFromMM);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayEndHH"+k+'"]').val(v.convEndToHH);
                $Ele.find('div.pubHolidayContent').eq(k).find('input[name="'+currentPremVal+"PbHolDayEndMM"+k+'"]').val(v.convEndToMM);
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