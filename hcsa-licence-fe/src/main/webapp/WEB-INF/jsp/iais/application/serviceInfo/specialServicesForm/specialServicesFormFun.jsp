<c:if test="${empty psnContent}">
    <c:set var="spsnContent" value="personnel-content"/>
</c:if>
<%--<%@include file="../svcPersonnel/servicePersonnelFun.jsp" %>--%>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>
<script type="text/javascript">
    $(function () {
        removePersonEvent();
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
        }else if (dis=='Nic'){
            maxCount=$target.closest('div.panel-group').find('input.NICMaxCount').val();
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
        $('.removeBtns').unbind('click');
        $('.removeBtns').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            var dis=$Content.find('input.psnType').val();
            var maxCount;
            if (dis=='Di'){
                maxCount=$Content.closest('div.panel-group').find('input.DirMaxCount').val();
            }else if (dis=='Nu'){
                maxCount=$Content.closest('div.panel-group').find('input.NurMaxCount').val();
            }else if (dis=='Nic'){
                maxCount=$Content.closest('div.panel-group').find('input.NICMaxCount').val();
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

    function toggleOther(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }
</script>