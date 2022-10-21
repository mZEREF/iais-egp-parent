<script type="text/javascript">
    $(document).ready(function () {
        initSectionLeader();
        <c:if test="${AppSubmissionDto.needEditController}" var="isSpecial">
        disableContent('div.sectionLaderContent');
        <c:if test="${!canEdit}">
        $('.removeBtn').closest('div').remove();
        </c:if>
        </c:if>

        $('div.sectionLaderContent').each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(v).not(':empty')) {
                        $(v).find('.isPartEdit').val(1);
                        $('#isEditHiddenVal').val('1');
                        unDisableContent($(v))
                    }
                });
            }
        });

    });

    function initSectionLeader() {
        addSectionLeaderBtnEvent();
        removeSectionLeaderEvent();
        doEditeSectionLeaderEvent();
        refreshSectionLeaderBtn();
    }

    function refreshSectionLeaderIndex($target,size) {
        var slLength = $('.sectionLaderContent').length;
        $('input[name="slLength"]').val(slLength);
        var $content = $('div.sectionLaderContent');
        refreshIndex($content);
        toggleTag($target.find('.removeSectionLeaderDiv'), size != 0);
        $content.each(function (k, v) {
            if (slLength <= 1 && k == 0) {
                $(this).find('.assign-psn-item').html('');
            } else {
                $(this).find('.assign-psn-item').html(k + 1);
            }
        });
    }

    function refreshSectionLeaderBtn() {
        var $content = $('div.sectionLaderContent');
        var slLength = $content.length;
        $('input[name="slLength"]').val(slLength);
        var isEdit = $('#isEditHiddenVal').val();
        $content.each(function (index, v) {
            if (index < '${sectionLeaderConfig.mandatoryCount}') {
                $(v).find('.removeSectionLeaderDiv').hide();
            } else {
                $(v).find('.removeSectionLeaderDiv').show();
            }
        });
        refreshPersonOthers();
    }

    function addSectionLeaderBtnEvent() {
        $('.addSectionLeaderBtn').unbind('click');
        $('.addSectionLeaderBtn').click(function () {
            showWaiting();
            let target = $('div.sectionLaderContent:last')
            let src = target.clone();
            clearFields(src);
            $('div.addSectionLeaderDiv').before(src);
            refreshSectionLeaderBtn()
            refreshSectionLeaderIndex($('div.sectionLaderContent:last'),$('.sectionLaderContent').length-1)
            $('#isEditHiddenVal').val('1');
            let $currContent = $('.sectionLaderContent:last')
            $currContent.find('.isPartEdit').val(1)
            unDisableContent($currContent)
            removeSectionLeaderEvent()
            dismissWaiting();
        });
    };

    function removeSectionLeaderEvent() {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            showWaiting();
            var slLength = $('.sectionLaderContent').length;
            if (slLength <= '${sectionLeaderConfig.mandatoryCount}') {
                dismissWaiting();
                return;
            }
            var $currContent = $(this).closest('div.sectionLaderContent');
            $currContent.remove();
            $('#isEditHiddenVal').val('1');
            refreshSectionLeaderIndex($currContent,$('.sectionLaderContent').length-1);
            initSectionLeader();
            dismissWaiting();
        });
    }

    var doEditeSectionLeaderEvent = function () {
        let $target = $('.edit-content');
        $target.unbind('click');
        $target.click(function () {
            let $currContent = $(this).closest('div.sectionLaderContent');
            doEditeSectionLeader($currContent);
        });
    }

    function doEditeSectionLeader($currContent) {
        unDisableContent($($currContent));
        $('#isEditHiddenVal').val('1');
        $currContent.find('.isPartEdit').val('1');
        refreshSectionLeaderBtn();
        hideTag($('.edit-content'));
    }

    function refreshPersonOthers() {
        let maxCount = eval('${sectionLeaderConfig.maximumCount}');
        toggleTag('.addSectionLeaderDiv', $('div.sectionLaderContent').length < maxCount);
    }
</script>