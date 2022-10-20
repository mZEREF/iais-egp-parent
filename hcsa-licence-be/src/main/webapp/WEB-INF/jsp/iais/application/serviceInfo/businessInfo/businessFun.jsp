<script>

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
        var prefix=$target.closest('div.panel-group').index();
        resetIndexNo($target, k,prefix);
    }

    var disabeleForAllDay = function ($allDayDiv) {
        clearFields($allDayDiv.siblings('.start-div'));
        disableContent($allDayDiv.siblings('.start-div'));

        clearFields($allDayDiv.siblings('.end-div'));
        disableContent($allDayDiv.siblings('.end-div'));
    }

    function refreshIndex(targetSelector) {
        var $target = getJqueryNode(targetSelector);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            var $ele = $(v);
            var $selector;
            if ($ele.is(':input')) {
                $selector = $ele;
            } else {
                $selector = $ele.find(':input');
            }
            if ($selector.length == 0) {
                return;
            }
            $selector.each(function () {
                var prefix=$target.closest('div.panel-group').index();
                resetIndexNo(this, k,prefix);
            });
        });
    }

    function resetIndexNo(targetTag, index, prefix) {
        var $target = getJqueryNode(targetTag);
        if (isEmptyNode($target) || $target.hasClass('not-refresh')) {
            return;
        }
        var tag = $target[0].tagName.toLowerCase();
        if ($target.is(':input')) {
            var orgName = $target.attr('name');
            var orgId = $target.attr('id');
            if (isEmpty(orgName)) {
                orgName = orgId;
            }
            if (isEmpty(orgName)) {
                return;
            }
            if (isEmpty(prefix)) {
                prefix = "";
            }
            var base = $target.data('base');
            if (isEmpty(base)) {
                var result;
                if (isEmpty(prefix)) {
                    prefix = "";
                    result = /(.*\D+)/g.exec(orgName);
                } else {
                    result = /(\D+.*\D+)/g.exec(orgName);
                }
                base = !isEmpty(result) && result.length > 0 ? result[0] : orgName;
            }
            var newName =base + prefix  + index;
            $target.prop('name', newName);
            if (orgName == orgId || base == orgId || !isEmpty(orgId) && $('#' + orgId).length > 1) {
                $target.prop('id', newName);
            }
            var $errorSpan = $target.closest('.form-group').find('span[name="iaisErrorMsg"][id="error_' + orgName + '"]');
            if ($errorSpan.length > 0) {
                $errorSpan.prop('id', 'error_' + newName);
            }
            if (tag == 'select') {
                updateSelectTag($target);
            }
        } else {
            $target.find(':input').each(function () {
                resetIndexNo(this, index, prefix);
            });
        }
    }

</script>