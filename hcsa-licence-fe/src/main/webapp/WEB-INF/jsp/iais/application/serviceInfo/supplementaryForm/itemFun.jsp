<script type="text/javascript">
    $(function () {
        refreshRemoveBtn();
        removeBtnEvent();
        addMoreEvent();
        checkMandatoryEvent();
    });

    /*function initItem() {
        $('[data-curr]').each(function (k, v) {
            let $tag = $(v);
            let conditionItemId = $tag.data('conditionItemId');
            let specialCondition = $tag.data('specialCondition');
            let parentItemId = $tag.data('parent');
            let mandatory = $tag.data('mandatory');
            let curr = $tag.data('curr');
            let seq = $tag.data('seq');
            let $conNodes = $('[data-conditionItemId="' + conditionItemId + '"][data-seq="' + seq + '"]');
        });
    }*/

    function refreshRemoveBtn() {
        let rmv_ary = [];
        $('.removeBtn[data-group]').each(function(){
            let group = $(this).data('group');
            if (!rmv_ary.includes(group)) {
                rmv_ary.push(group);
            }
        });
        rmv_ary.forEach(function(currentValue){
            hideTag($('.removeBtn[data-group="' + currentValue + '"]:first'));
            showTag($('.removeBtn[data-group="' + currentValue + '"]:not(first)'));
        });

    }

    var removeBtnEvent = function(){
        $('.removeEditDiv[data-group] .removeBtn').unbind('click');
        $('.removeEditDiv[data-group] .removeBtn').on('click', function () {
            let $v = $(this);
            let $tag = $v.closest('removeEditDiv');
            let group = $tag.data('group');
            let index = $tag.data('seq');
            $('.removeEditRow [data-group="' + group + '"][data-seq="' + index + '"]').closest('.removeEditRow').remove();
            $('[data-group="' + group + '"][data-seq="' + index + '"]:not(.removeEditDiv)').closest('.item-record').remove();
            resetAllItemIndex(group);
        });
    }

    function resetAllItemIndex(group) {
        $('.removeEditRow [data-group="' + group + '"]').each(function(index, ele){
            let $ele = $(ele);
            $ele.data('seq', index);
        });
        let rowAry = [];
        $('[data-group="' + group + '"]:not(.removeEditDiv)').closest('.item-record').each(function(){
            let data = $(this).attr('class');
            if (!rowAry.rowAry(data)) {
                rowAry.push(data);
            }
        });
        rowAry.forEach(function(item){
            $('div[class="' + item + '"]').each(function(index, ele) {
                resetItem($(ele), index);
            });
        });
        $('input[name="' + group + '"]').val(rowAry.length);
    }

    function resetItemIndex($itemRecords, index) {
        if (isEmptyNode($itemRecords)) {
            return;
        }
        $itemRecords.each(function(){
            resetItem($(this).find('[data-seq]'), index);
        });
    }

    function resetItem($ele, index) {
        if (isEmptyNode($ele)) {
            return;
        }
        //let odlIndex = $ele.data('seq');
        $ele.data('seq', index);
        let baseName = $ele.data('base');
        $ele.attr('name', baseName + index);
        let oldId = $ele.attr('id');
        if (!isEmpty(oldId)) {
            let baseId = $ele.data('id');
            if (!isEmpty(idVal)) {
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
        $('.addMoreDiv .addMoreBtn').on('click', function() {
            let $tag = $(this);
            let $addMoreDiv = $tag.closest('.addMoreDiv');
            let index = parseInt($('input[name="' + group + '"]').val());
            let group = $tag.data('group');
            // remove the group error message
            $('.error_' + group).remove();
            let $romveRow = $('.removeEditRow [data-group="' + group + '"][data-seq="0"]').closest('.removeEditRow').clone();
            resetItemIndex($romveRow, index);
            showTag($romveRow);
            $addMoreDiv.before($romveRow);
            let $itemRecords = $('[data-group="' + group + '"][data-seq="0"]:not(.removeEditDiv)').closest('.item-record').clone();
            resetItemIndex($itemRecords, index);
            $addMoreDiv.before($itemRecords);
            $('input[name="' + group + '"]').val(index + 1);
        });
    }

    var checkMandatoryEvent = function() {
        $('.item-record [data-curr]').unbind('click change');
        $('.item-record [data-curr]').on('click change', function(){
            let $tag = $(this);
            let conditionItemId = $tag.data('conditionItemId');
            /*let specialCondition = $tag.data('specialCondition');
            let parentItemId = $tag.data('parent');
            let mandatory = $tag.data('mandatory');
            let curr = $tag.data('curr');*/
            let seq = $tag.data('seq');
            let $conNodes = $('[data-conditionItemId="' + conditionItemId + '"][data-seq="' + seq + '"]');
            let currVal = getValue($tag);
            if (!isEmptyNode($conNodes)) {
                let ary = isEmpty(currVal) ? [] : currVal.split('#');
                $conNodes.each(function(){
                    let $v = $(this);
                    let $targetLabel = $(v).closest('.item-record').find('.item-label');
                    if (isEmptyNode($targetLabel)) {
                        return;
                    }
                    $targetLabel.find('.mandatory').remove();
                    let conVal = $v.data('specialCondition');
                    if (isEmpty(conVal)) {
                        return;
                    }
                    let isIncluded = false;
                    conVal.split('#').forEach(function(currentValue){
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
            var type = $target[0].type, tag = $target[0].tagName.toLowerCase(), len = $target.length;
            if (type == 'radio') {
                return $target.filter(':checked').val();
            } else if (type == 'checkbox') {
                var chk_value = [];
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