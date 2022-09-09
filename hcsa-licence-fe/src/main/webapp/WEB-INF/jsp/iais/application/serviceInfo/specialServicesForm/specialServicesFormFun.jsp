<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="personnel-content"/>
</c:if>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>
<script type="text/javascript">
    $(function () {
        removePersonnelEvent();
        initPerson($('div.panel-main-content'));
    });

    function initPerson(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                if ($(v).find('.error-msg:not(:empty)').length > 0) {
                    $(v).find('.psnEdit').trigger("click");
                }
            }
            if ($(v).find('div.personnel-content').length == 1) {
                $(v).find('.assign-psn-item').html('');
            }
            $(v).find('div.personnel-content').each(function (i, x) {
                var flag=isEmpty($(x).find('input.profRegNo').val())?false:true;
                disablePrsInfo($(x),flag,true);
            })

        });
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.assign-psn-item').html(k + 1);
        resetIndex($target, k);
    }

    function addPerson(target,type,maxCount) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find('div.personnel-content').last();
        var src = $tgt.clone();
        $tgt.after(src);
        var $currContent = $(target).find('div.personnel-content').last();
        $currContent.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        clearFields($currContent);
        $currContent.find('.speciality').html('');
        $currContent.find('.subSpeciality').html('');
        $currContent.find('.othersubSpeciality').html('');
        $currContent.find('.qualification').html('');

        refreshPerson($currContent, $(target).find('div.personnel-content').length - 1);
        disablePrsInfo($currContent, false,true);
        $(target).find('div.personnel-content').first().find('.psnHeader').html('1');
        removePersonnelEvent();
        profRegNoEvent($currContent);
        var length =  $target.find('div.personnel-content').length;
        if(length >= maxCount){
            $target.find('.addDiv').addClass('hidden');
        }
        $target.find('input.length').val(length);
        dismissWaiting();
    }

    var removePersonnelEvent = function () {
        $('.removeBtns').unbind('click');
        $('.removeBtns').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            var maxCount=$Content.find('input.MaxCount').val();
            $(this).closest('div.personnel-content').remove();
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshPerson($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.assign-psn-item').html('');
            }
            var len =  $Content.find('div.personnel-content').length;
            $Content.find('input.length').val(len);
            if(len < maxCount){
                $Content.find('.addDiv').removeClass('hidden');
            }
        });
    }

    function toggleOther(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }
</script>