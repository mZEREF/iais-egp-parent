<script>

    function addWeekly(target){
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find("div.weeklyDiv").last();
        var src = $tgt.clone();
        $tgt.after(src);

        var $currContent = $(target).find("div.weeklyDiv").last();
        $currContent.find('select.onSiteWeekly').next('.multi-select-container').remove();
        clearFields($currContent);
        refreshContent($currContent, $target.find('div.weeklyDiv').length - 1);
        removeWeekly();
        clickAllDay();
        var length =  $target.find('div.weeklyDiv').length;
        $target.find('input.weeklyLength').val(length);
        if(length >= '${maxCount}'){
            $target.find('.addWeeklyDiv').addClass('hidden');
        }
        $target.find('select.onSiteWeekly').each(function () {
            $(this).multiSelect();
        });
        dismissWaiting();
    }

    var removeWeekly = function () {
        $('.weeklyDel').unbind('click');
        $('.weeklyDel').click(function () {
            var $weeklyContent = $(this).closest('.weeklyContent');
            $(this).closest('.weeklyDiv').remove();
            //reset name
            refreshIndex($weeklyContent.find('div.weeklyDiv'));
            $('a.businessEdit').trigger('click');
            var weeklyLength = $weeklyContent.find('.weeklyDiv').length;
            if (weeklyLength==0){
                $weeklyContent.find('input.weeklyLength').val(1);
            }else {
                $weeklyContent.find('input.weeklyLength').val(weeklyLength);
            }
            if(weeklyLength < '${maxCount}'){
                $weeklyContent.find('.addWeeklyDiv').removeClass('hidden');
            }
        });
    }

    function addPubHolDay(target){
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find("div.pubHolidayDiv").last();
        var src = $tgt.clone();
        $tgt.after(src);
        var $currContent = $(target).find("div.pubHolidayDiv").last();
        $currContent.find('select.onSitePubHoliday').next('.multi-select-container').remove();
        clearFields($currContent);
        refreshContent($currContent, $target.find('div.pubHolidayDiv').length - 1);
        removePh();
        clickAllDay();
        var length =  $target.find('div.pubHolidayDiv').length;
        $target.find('input.phLength').val(length);
        if(length >= '${maxCount}'){
            $target.find('.addPhDiv').addClass('hidden');
        }
        $target.find('select.onSitePubHoliday').each(function () {
            $(this).multiSelect();
        });
        dismissWaiting();
    }

    var removePh = function () {
        $('.pubHolidayDel').unbind('click');
        $('.pubHolidayDel').click(function () {
            var $phContent = $(this).closest('.pubHolDayContent');
            $(this).closest('.pubHolidayDiv').remove();
            //reset name
            refreshIndex($phContent.find('div.pubHolidayDiv'));
            $('a.businessEdit').trigger('click');
            var phLength = $phContent.find('.pubHolidayDiv').length;
            if(phLength==0){
                $phContent.find('input.phLength').val(1);
            } else{
                $phContent.find('input.phLength').val(phLength);
            }
            if(phLength < '${maxCount}'){
                $phContent.find('.addPhDiv').removeClass('hidden');
            }
        });
    }

    function addEvent (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find("div.eventDiv").last();
        var src = $tgt.clone();
        $tgt.after(src);
        var $currContent = $(target).find("div.eventDiv").last();
        $currContent.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        clearFields($currContent);
        refreshContent($currContent, $target.find('div.eventDiv').length - 1);
        removeEvent();
        var length =  $target.find('div.eventDiv').length;
        $target.find('input.eventLength').val(length);
        console.log($target.find('input.eventLength').val());
        if(length >= '${maxCount}'){
            $target.find('.addEventDiv').addClass('hidden');
        }
        dismissWaiting();
    }

    var removeEvent = function () {
        $('.eventDel').unbind('click');
        $('.eventDel').click(function () {
            var $eventContent = $(this).closest('.eventContent');
            $(this).closest('.eventDiv').remove();
            //reset name
            refreshIndex($eventContent.find('div.eventDiv'));
            $('a.businessEdit').trigger('click');
            var eventLength = $eventContent.find('.eventDiv').length;
            if(eventLength==0){
                $eventContent.find('input.eventLength').val(1);
            }else {
                $eventContent.find('input.eventLength').val(eventLength);
            }
            if(eventLength < '${maxCount}'){
                $eventContent.find('.addEventDiv').removeClass('hidden');
            }
        });
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

    function refreshContent($target, k) {
        toggleTag($target.find('div.weeklyDelDiv'), k != 0);
        toggleTag($target.find('div.delpubHolidayDiv'), k != 0);
        toggleTag($target.find('div.eventDelDiv'), k != 0);

        var x=$target.find('.allDay').is(':checked');
        if(!x){
            unDisableContent($target.find('div.start-div'));
            unDisableContent($target.find('div.end-div'));
        }
        resetIndex($target, k);
    }

    var disabeleForAllDay = function ($allDayDiv) {
        clearFields($allDayDiv.siblings('.start-div'));
        disableContent($allDayDiv.siblings('.start-div'));

        clearFields($allDayDiv.siblings('.end-div'));
        disableContent($allDayDiv.siblings('.end-div'));
    }
</script>