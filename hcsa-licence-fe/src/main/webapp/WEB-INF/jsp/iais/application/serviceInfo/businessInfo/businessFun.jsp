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
        testclearFields($currContent);
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
        $('.weeklyDel').click(function () {
            var $weeklyContent = $(this).closest('.weeklyContent');
            $(this).closest('.weeklyDiv').remove();
            //reset name
            refreshIndex($weeklyContent.find('div.weeklyDiv'));
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
        testclearFields($currContent);
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
        $('.pubHolidayDel').click(function () {
            var $phContent = $(this).closest('.pubHolDayContent');
            $(this).closest('.pubHolidayDiv').remove();
            //reset name
            refreshIndex($phContent.find('div.pubHolidayDiv'));
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
        testclearFields($currContent);
        refreshContent($currContent, $target.find('div.eventDiv').length - 1);
        removeEvent();
        var length =  $target.find('div.eventDiv').length;
        $target.find('input.eventLength').val(length);
        if(length >= '${maxCount}'){
            $target.find('.addEventDiv').addClass('hidden');
        }
        dismissWaiting();
    }

    var removeEvent = function () {
        $('.eventDel').click(function () {
            var $eventContent = $(this).closest('.eventContent');
            $(this).closest('.eventDiv').remove();
            //reset name
            refreshIndex($eventContent.find('div.eventDiv'));
            var eventLength = $eventContent.find('.eventDiv').length;
            if(eventLength==0){
                $eventContent.find('input.eventLength').val(1);
            }else {
                $eventContent.find('input.eventLength').val(length);
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
        resetindex($target, k,prefix);
    }

    var disabeleForAllDay = function ($allDayDiv) {
        clearFields($allDayDiv.siblings('.start-div'));
        disableContent($allDayDiv.siblings('.start-div'));

        clearFields($allDayDiv.siblings('.end-div'));
        disableContent($allDayDiv.siblings('.end-div'));
    }

    function testclearFields(targetSelector, withoutClearError) {
        var $selector = getJqueryNode(targetSelector);
        if (isEmptyNode($selector)) {
            return;
        }
        if (!$selector.is(":input")) {
            if (isEmpty(withoutClearError) || !withoutClearError) {
                $selector.find("span[name='iaisErrorMsg']").each(function () {
                    $(this).html("");
                });
            }
            $selector = $selector.find(':input[class!="not-clear"]');
        }
        if ($selector.length <= 0) {
            return;
        }
        $selector.each(function () {
            var type = this.type, tag = this.tagName.toLowerCase();
            if (!$(this).hasClass('not-clear')) {
                if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea') {
                    this.value = '';
                } else if (type == 'checkbox') {
                    this.checked = false;
                } else if (type == 'radio') {
                    this.checked = false;
                } else if (tag == 'select') {
                    this.selectedIndex = 0;
                    if (this.multiple) {
                        this.selectedIndex = -1;
                    }
                    updateSelectTag($(this));
                }
            }
        });
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
                resetindex(this, k,prefix);
            });
        });
    }

    function resetindex(targetTag, index, prefix) {
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
                resetindex(this, index, prefix);
            });
        }
    }

</script>