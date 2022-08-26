<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="personnel-content"/>
</c:if>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>
<script type="text/javascript">
    $(function () {
        let psnContent = '.${psnContent}';
        removePersonEvent(psnContent);
        psnEditEvent(psnContent);
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
                $(v).find('.psnHeader').html('');
            }
            $(v).find('div.personnel-content').each(function (i, x) {
                var flag=isEmpty($(x).find('input.profRegNo').val())?false:true;
                disablePrsInfo($(x),flag,true);
            })

        });

    }

    var psnEditEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.psnEdit').unbind('click');
        $target.find('.psnEdit').on('click', function () {
            showWaiting();
            var $currContent = $(this).closest(target);
            $currContent.find('input.isPartEdit').val('1');
            $(target + '-edit').val('1');
            hideTag($(this).closest('.edit-content'));
            unDisableContent($currContent);
            dismissWaiting();
        });
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.psnHeader').html(k + 1);
        resetIndex($target, k);
    }

    function addPersonnel(target,dis) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        var maxCount=0;
        if (dis=='Di'){
            maxCount=$target.closest('div.panel-group').find('input.DirMaxCount').val();
        }else if (dis=='Nu'){
            maxCount=$target.closest('div.panel-group').find('input.NurMaxCount').val();
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
        removePersonEvent();
        profRegNoEvent($currContent);

        var length =  $target.find('div.personnel-content').length;
        if(length >= maxCount){
            $target.find('.addDiv').addClass('hidden');
        }
        $target.find('input.length').val(length);
        dismissWaiting();
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            var dis=$Content.find('input.disDiOrNu').val();
            var maxCount;
            if (dis=='Di'){
                maxCount=$Content.closest('div.panel-group').find('input.DirMaxCount').val();
            }else if (dis=='Nu'){
                maxCount=$Content.closest('div.panel-group').find('input.NurMaxCount').val();
            }
            $(this).closest('div.personnel-content').remove();
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshPerson($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.psnHeader').html('');
            }
            var len =  $Content.find('div.personnel-content').length;
            $Content.find('input.length').val(len);
            if(len < maxCount){
                $Content.find('.addDiv').removeClass('hidden');
            }
        });
    }
</script>