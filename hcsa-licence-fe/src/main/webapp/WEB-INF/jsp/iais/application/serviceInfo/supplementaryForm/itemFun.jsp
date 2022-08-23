<script type="text/javascript">
    $(function () {
        refreshRemoveBtn();
        refreshAddBtn();
        removeBtnEvent();
        addMoreEvent();
        checkItemEvent();
        $('.item-record [data-curr]').each(function () {
            checkItemMandatory($(this));
            checkItemTotal($(this));
        });
    });

    function refreshAddBtn() {
        $('.addMoreDiv').each(function () {
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
        refreshLabel($ele, index);
    }

    var addMoreEvent = function () {
        $('.addMoreDiv .addMoreBtn').unbind('click');
        $('.addMoreDiv .addMoreBtn').on('click', function () {
            clearErrorMsg();
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
            resetItemIndex($itemRecords, index);
            $target.before($itemRecords);
            $('input[name="' + group + '"]').val(index + 1);
            removeBtnEvent();
            refreshAddBtn();
            clearFields($('[data-group="' + group + '"][data-seq="' + index + '"]'));
            checkItemEvent();
            $('.item-record [data-curr]').each(function () {
                checkItemMandatory($(this));
            });
        });
    }

    function refreshLabel($tag, index) {
        if (isEmptyNode($tag)) {
            return;
        }
        let $targetLabel = $tag.closest('.item-record').find('.item-label');
        if (isEmptyNode($targetLabel)) {
            return;
        }
        let part = $targetLabel.data('label');
        if (isEmpty(part)) {
            return;
        }
        let newVal = toRomanNum(index + 1, true) + part;
        let $span = $targetLabel.find('span');
        if ($span.length > 0) {
            $targetLabel.html(newVal + "&nbsp;" + $span.prop('outerHTML'));
        } else {
            $targetLabel.html(newVal);
        }
    }

    var checkItemEvent = function () {
        var $target = $('.item-record [data-curr]');
        $target.unbind('click change blur');
        $target.on('click change', function () {
            checkItemMandatory($(this));
        });
        $target.on('blur', function () {
            checkItem($(this));
        });
    }

    function checkItemMandatory($tag) {
        if (isEmptyNode($tag)) {
            return;
        }
        let seq = $tag.data('seq');
        let curr = $tag.data('curr');
        let $conNodes = $('[data-parent="' + curr + '"][data-seq="' + seq + '"]');
        let currVal = getValue($tag);
        if (!isEmptyNode($conNodes)) {
            if ($tag.is(':hidden')) {
                $conNodes.each(function () {
                    let $v = $(this);
                    let $target = $v.closest('.item-record');
                    hideTag($target);
                    checkItemMandatory($v);
                });
                return;
            }
            let ary = isEmpty(currVal) ? [] : currVal.split('#');
            $conNodes.each(function () {
                let $v = $(this);
                let mandatory = $v.data('mandatory');
                if ('2' == mandatory) {
                    let $targetLabel = $v.closest('.item-record').find('.item-label');
                    if (isEmptyNode($targetLabel)) {
                        return;
                    }
                    $targetLabel.find('.mandatory').remove();
                    let conVal = $v.data('mandatory-cond');
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
                } else if ('3' == mandatory) {
                    let $target = $v.closest('.item-record');
                    let $targetLabel = $target.find('.item-label');
                    if (!isEmptyNode($targetLabel)) {
                        $targetLabel.find('.mandatory').remove();
                        $targetLabel.append('<span class="mandatory">*</span>');
                    }
                    let conVal = $v.data('mandatory-cond');
                    if (isEmpty(conVal)) {
                        return;
                    }
                    let isIncluded = false;

                    conVal.split('#').forEach(function (currentValue) {
                        if (ary.includes(currentValue)) {
                            isIncluded = true;
                        }
                    });
                    toggleTag($target, isIncluded);
                    checkItemMandatory($v);
                } else if ('4' == mandatory) {
                    let $target = $v.closest('.item-record');
                    let $targetLabel = $target.find('.item-label');
                    if (!isEmptyNode($targetLabel)) {
                        $targetLabel.find('.mandatory').remove();
                    }
                    let conVal = $v.data('mandatory-cond');
                    if (isEmpty(conVal)) {
                        return;
                    }
                    let isIncluded = false;
                    conVal.split('#').forEach(function (currentValue) {
                        if (ary.includes(currentValue)) {
                            isIncluded = true;
                        }
                    });
                    toggleTag($target, isIncluded);
                    checkItemMandatory($v);
                }
            });
        }
    }

    function checkItem($tag) {
        if (isEmptyNode($tag)) {
            return;
        }
        let targetId = $tag.data('condition');
        if (isEmpty(targetId)) {
            return;
        }
        let seq = $tag.data('seq');
        let $target = $('[data-curr=' + targetId + '][data-seq=' + seq + ']');
        checkItemTotal($target);
    }

    function checkItemTotal($tag) {
        if (isEmptyNode($tag)) {
            return -1;
        }
        let specialcondition = $tag.data('specialcondition');
        if ('SPECCON01' !== specialcondition) {
            return -1;
        }
        let seq = $tag.data('seq');
        let curr = $tag.data('curr');
        let $conNodes = $('[data-condition="' + curr + '"][data-seq="' + seq + '"]');
        let total = 0;
        if (!isEmptyNode($conNodes)) {
            // calculate total
            $conNodes.each(function () {
                let $v = $(this);
                let conVal = $v.data('specialcondition');
                let currVal;
                if ('SPECCON01' === conVal) {
                    currVal = checkItemTotal($v);
                } else {
                    let x = getValue($v);
                    if (isNaN(x)) {
                        currVal = 0;
                    } else {
                        currVal = Number(x);
                    }
                }
                total += currVal;
            });
        }
        if ($tag.is(':input')) {
            fillValue($tag, total);
        } else {
            var $target = $tag.find('p');
            if (isEmptyNode($target)) {
                $target = $tag;
            }
            $('[name="' + curr + seq + '"]').val(total);
            $target.html(total);
        }
        return total;
    }

    function toRomanNum(i, withLower) {
        if (isNaN(i)) {
            return "";
        }
        let num = Number(i);
        if (num < 1 || num > 5999) {
            return "";
        }
        const RN_I = ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"];
        const RN_X = ["", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"];
        const RN_C = ["", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"];
        const RN_M = ["", "M", "MM", "MMM", "MMMM", "MMMMM"];
        let result = RN_M[parseInt(num / 1000)] + RN_C[parseInt(num % 1000 / 100)] + RN_X[parseInt(num % 100 / 10)] + RN_I[num % 10];
        if (withLower) {
            result = result.toLowerCase();
        }
        return result;
    }

</script>