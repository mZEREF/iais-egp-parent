<script type="text/javascript">
    $(function () {
        refreshRemoveBtn();
        refreshAddBtn();
        removeBtnEvent();
        addMoreEvent();
        checkMandatoryEvent();
        $('.item-record [data-curr]').trigger('click');
    });

    function refreshAddBtn() {
        $('.addMoreDiv').each(function() {
            let $tag = $(this);
            let group = $tag.data('group');
            let count = parseInt($('input[name="' + group + '"]').val());
            let maxCount = parseInt($('input[name="' + group + '-max"]').val());
            console.info("-----------" + count + "-----" + maxCount + "--------------");
            //toggleTag($tag, count < maxCount);
        });
    }

    function refreshRemoveBtn() {
        let rmv_ary = [];
        $('.removeEditDiv[data-group]').each(function () {
            let $tag = $(this);
            let group = $tag.data('group');
            if (!rmv_ary.includes(group)) {
                rmv_ary.push(group);
            }
        });
        rmv_ary.forEach(function (item) {
            let $removeTag = $('.removeEditDiv[data-group="' + item + '"]');
            $removeTag.each(function (index, ele) {
                let $tag = $(ele);
                toggleTag($tag.closest('.removeEditRow'), index != 0);
                $tag.attr('data-seq', index);
            });
            $('input[name="' + item + '"]').val($removeTag.length);
        });

    }

    var removeBtnEvent = function () {
        $('.removeEditDiv[data-group] .removeBtn').unbind('click');
        $('.removeEditDiv[data-group] .removeBtn').on('click', function () {
            let $v = $(this);
            let $tag = $v.closest('.removeEditDiv');
            let group = $tag.data('group');
            let index = $tag.data('seq');
            $('.removeEditRow [data-group="' + group + '"][data-seq="' + index + '"]').closest('.removeEditRow').remove();
            $('[data-group="' + group + '"][data-seq="' + index + '"]:not(.removeEditDiv)').closest('.item-record').remove();
            resetAllItemIndex(group);
        });
    }

    function resetAllItemIndex(group) {
        refreshRemoveBtn();
        refreshAddBtn();
        let rowAry = [];
        $('[data-group="' + group + '"]:not(.removeEditDiv)').closest('.item-record').each(function () {
            let data = $(this).attr('class');
            if (!rowAry.includes(data)) {
                rowAry.push(data);
            }
        });
        rowAry.forEach(function (item) {
            $('div[class="' + item + '"]').each(function (index, ele) {
                resetItem($(ele), index);
            });
        });
    }

    function resetItemIndex($itemRecords, index) {
        if (isEmptyNode($itemRecords)) {
            return;
        }
        $itemRecords.each(function () {
            resetItem($(this).find('[data-seq]'), index);
        });
    }

    function resetItem($ele, index) {
        if (isEmptyNode($ele)) {
            return;
        }
        //let odlIndex = $ele.data('seq');
        $ele.attr('data-seq', index);
        let baseName = $ele.data('base');
        if (!isEmpty(baseName)) {
            $ele.attr('name', baseName + index);
        }
        let oldId = $ele.attr('id');
        if (!isEmpty(oldId)) {
            let baseId = $ele.data('id');
            if (!isEmpty(baseId)) {
                $ele.attr('id', baseId + index);
            }
            let $eleLabel = $('label[for="' + oldId + '"]');
            if (!isEmptyNode($eleLabel)) {
                $eleLabel.attr('for', baseId + index);
            }
        }
    }

    var addMoreEvent = function () {
        $('.addMoreDiv .addMoreBtn').unbind('click');
        $('.addMoreDiv .addMoreBtn').on('click', function () {
            let $tag = $(this);
            let $target = $tag.closest('.addMoreDiv');
            let group = $target.data('group');
            let index = parseInt($('input[name="' + group + '"]').val());
            // remove the group error message
            $('.error_' + group).remove();
            let $romveRow = $('.removeEditRow [data-group="' + group + '"][data-seq="0"]').closest('.removeEditRow').clone();
            resetItemIndex($romveRow, index);
            showTag($romveRow);
            $target.before($romveRow);
            // main content
            let $itemRecords = $('[data-group="' + group + '"][data-seq="0"]:not(.removeEditDiv)').closest('.item-record').clone();
            clearFields($itemRecords);
            resetItemIndex($itemRecords, index);
            $target.before($itemRecords);
            $('input[name="' + group + '"]').val(index + 1);
            removeBtnEvent();
            refreshAddBtn();
            checkMandatoryEvent();
            $('.item-record [data-curr]').trigger('click');
        });
    }

    var checkMandatoryEvent = function () {
        $('.item-record [data-curr]').unbind('click change');
        $('.item-record [data-curr]').on('click change', function () {
            let $tag = $(this);
            //let conditionItemId = $tag.data('conditionitemid');
            let seq = $tag.data('seq');
            let curr = $tag.data('curr');
            let $conNodes = $('[data-conditionitemid="' + curr + '"][data-seq="' + seq + '"]');
            let currVal = getValue($tag);
            if (!isEmptyNode($conNodes)) {
                let ary = isEmpty(currVal) ? [] : currVal.split('#');
                $conNodes.each(function () {
                    let $v = $(this);
                    let $targetLabel = $v.closest('.item-record').find('.item-label');
                    if (isEmptyNode($targetLabel)) {
                        return;
                    }
                    $targetLabel.find('.mandatory').remove();
                    let conVal = $v.data('specialcondition');
                    if (isEmpty(conVal)) {
                        return;
                    }
                    let isIncluded = false;
                    conVal.split('#').forEach(function (currentValue) {
                        if (ary.includes(currentValue)) {
                            isIncluded = true;
                        }
                    });
                    if (isIncluded) {
                        $targetLabel.append('<span class="mandatory">*</span>');
                    }
                });
            }
        });
    }

    function getValue(target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return null;
        }
        if ($target.is(":input")) {
            let type = $target[0].type;
            if (type == 'radio') {
                return $target.filter(':checked').val();
            } else if (type == 'checkbox') {
                let chk_value = [];
                $target.filter(':checked').each(function () {
                    chk_value.push($(this).val());
                });
                return chk_value.join("#");
            } else {
                return $target.val();
            }
        } else {
            return null;
        }
    }
</script>