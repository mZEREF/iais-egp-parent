<script type="text/javascript">

    function initItem() {
        $('[data-curr]').each(function (k, v) {
            let $tag = $(v);
            let conditionItemId = $tag.data('conditionItemId');
            let specialConditionType = $tag.data('specialConditionType');
            let parentItemId = $tag.data('parent');
            let mandatory = $tag.data('mandatory');
            let curr = $tag.data('curr');
            let seq = $tag.data('seq');
            let $conNodes = $('[data-conditionItemId="' + conditionItemId + '"][data-seq="' + seq + '"]');
        });
    }

    function refreshRemoveBtn() {
        let rmv_ary = [];
        $('.removeBtn[data-addMoreBatchNum]').each(function(){
            let addMoreBatchNum = $(this).data('addMoreBatchNum');
            if (!rmv_ary.includes(addMoreBatchNum)) {
                rmv_ary.push(addMoreBatchNum);
            }
        });
        rmv_ary.forEach(function(currentValue){
            hideTag($('.removeBtn[data-addMoreBatchNum="' + currentValue + '"]:first'));
            showTag($('.removeBtn[data-addMoreBatchNum="' + currentValue + '"]:not(first)'));
        });
    }

    var removeBtnEvent = function(){
        $('.removeBtn[data-addMoreBatchNum] .removeBtn')
    }

    function resetItemIndex($itemRecords, index) {
        if (isEmptyNode($itemRecords)) {
            return;
        }
        $itemRecords.each(function(){
            let $ele = $(this).find('[data-seq]');
            if (isEmptyNode($ele)) {
                return;
            }
            let odlIndex = $ele.data('seq');
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
        });
    }

    var addMoreEvent = function () {
        $('.addMoreDiv .addMoreBtn').on('click', function() {
            let $tag = $(this);
            let addMoreBatchNum = $tag.data('addMoreBatchNum');
            let $itemRecords = $('[data-addMoreBatchNum="' + conditionItemId + '"][data-seq="0"]').closest('.item-record').clone();
            let oldIndex = parseInt($('input[name="' + addMoreBatchNum + '"]').val());
            let index = oldIndex + 1;
            $itemRecords.each(function(){
                let $ele = $(this).find('[data-seq="' + oldIndex + '"]');
                if (isEmptyNode($ele)) {
                    return;
                }
                $ele.data('seq', index);
            });
            $('.addMoreDiv').before($itemRecords);
            $('input[name="' + addMoreBatchNum + '"]').val(index);
        });
    }

    var checkMandatoryEvent = function() {
        $('.item-record [data-curr]').on('click change', function(){
            let $tag = $(this);
            let conditionItemId = $tag.data('conditionItemId');
            /*let specialConditionType = $tag.data('specialConditionType');
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
                    let conVal = $v.data('specialConditionType');
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

    var checkMandatoryEvent = function() {
        $('[data-curr]').on('click change', function(){

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