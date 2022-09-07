<script type="text/javascript">
    $(document).ready(function() {
        initSectionLeader();
        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}" var="isSpecial">
        disableContent('div.sectionLaderContent');
        <c:if test="${!canEdit}">
        $('.removeBtn').closest('div').remove();
        </c:if>
        </c:if>
        if("${errormapIs}"=='error'){
            $('.edit').trigger('click');
        }
    });

    function initSectionLeader() {
        addSectionLeaderBtn();
        removeSectionLeader();
        doEditeSectionLeader();
        refreshSectionLeaderBtn();
    }

    function refreshSectionLeaderIndex() {
        var slLength = $('.sectionLaderContent').length;
        $('input[name="slLength"]').val(slLength);
        console.info("length: " + slLength)
        var $content = $('div.sectionLaderContent');
        refreshIndex($content);
        $content.each(function (k,v) {
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
        var isEdit =  $('#isEditHiddenVal').val();
        $content.each(function (index,v) {
            if (index < '${sectionLeaderConfig.mandatoryCount}') {
                $(v).find('.removeSectionLeaderDiv').hide();
            } else {
                $(v).find('.removeSectionLeaderDiv').show();
            }
        });
        <c:if test="${!isRfi && (AppSubmissionDto.appType == 'APTY002' || canEdit)}" var="canShowAddBtn">
        // display add more
        if (slLength < '${sectionLeaderConfig.maximumCount}') {
            $('.addSectionLeaderDiv').show();
        } else {//hidden add more
            $('.addSectionLeaderDiv').hide();
        }
        </c:if>
        <c:if test="${!canShowAddBtn}">
        $('.addSectionLeaderDiv').remove();
        </c:if>
    }

    function addSectionLeaderBtn () {
        $('.addSectionLeaderBtn').unbind('click');
        $('.addSectionLeaderBtn').click(function () {
            showWaiting();
            let target =  $('div.sectionLaderContent:last')
            let src = target.clone();
            clearFields(src);
            $('div.addSectionLeaderDiv').before(src);
            refreshSectionLeaderBtn()
            refreshSectionLeaderIndex()
            $('#isEditHiddenVal').val('1');
            removeSectionLeader()
            dismissWaiting();
        });
    };

    function removeSectionLeader () {
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
            refreshSectionLeaderIndex();
            initSectionLeader();
            dismissWaiting();
        });
    }

    var doEditeSectionLeader = function () {
        $('.edit-content a').unbind('click');
        $('.edit-content a').click(function () {
            var $currContent = $(this).closest('div.sectionLaderContent');
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
            $currContent.find('.isPartEdit').val('1');
            refreshSectionLeaderBtn();
            $(this).hide();
        });
    }
</script>