<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                    <span style="font-size: 2rem;" id="prsErrorMsg">
                      <iais:message key="GENERAL_ERR0048" escape="false"/>
                    </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal"
                        onclick="$('#PRS_SERVICE_DOWN').modal('hide');">OK
                </button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT">
<script type="text/javascript">
    $(function () {
        refreshRemoveBtn();
        refreshAddBtn();
        removeBtnEvent();
        addMoreEvent();
        checkItemEvent();
        refreshGroupIndex();
        $('.item-record [data-curr]').each(function () {
            let $target = $(this);
            checkItemMandatory($target);
            checkItemTotal($target);
            let index = $target.data('seq');
            refreshLabel($target, index);
        });
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            disableContent($('.person-content'));
        </c:if>
        console.log('${isRfi}','===================>>>');
        <c:if test="${isRfi}">
            disableContent($('.person-content'));
        </c:if>
    });

    function refreshAddBtn() {
        $('.addMoreDiv').each(function () {
            let $tag = $(this);
            let group = $tag.data('group');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let count = parseInt($('input[name="' + prefix + group + '"]').val());
            let maxCount = parseInt($('input[name="' + prefix + group + '-max"]').val());
            console.info("-----------" + count + "-----" + maxCount + "--------------");
            if (maxCount <= 0) {
                return;
            }
            let $target = $('[data-group="' + group + '"][data-prefix="' + prefix + '"]:input');
            let visibled = false;
            if (!isEmptyNode($target)) {
                $target.each(function () {
                    if (!$(this).hasClass('hidden')) {
                        visibled = true;
                    }
                });
            }
            toggleTag($tag, visibled && count < maxCount);
        });
    }

    function refreshRemoveBtn() {
        let rmv_ary = [];
        $('.removeEditDiv[data-group]').each(function () {
            let $tag = $(this);
            let group = $tag.data('group');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let includes = false;
            let len = rmv_ary.length;
            for (let i = 0; i < len; i++) {
                let obj = rmv_ary[i];
                if (obj.prefix == prefix && obj.group == group) {
                    includes = true;
                    break;
                }
            }
            if (!includes) {
                rmv_ary.push({prefix: prefix, group: group});
            }
        });
        rmv_ary.forEach(function (item) {
            let $removeTag = $('.removeEditDiv[data-group="' + item.group + '"][data-prefix="' + item.prefix + '"]');
            $removeTag.each(function (index, ele) {
                let $tag = $(ele);
                toggleTag($tag.closest('.removeEditRow'), index != 0);
                $tag.attr('data-seq', index);
            });
            $('input[name="' + item.prefix + item.group + '"]').val($removeTag.length);
        });

    }

    var removeBtnEvent = function () {
        $('.removeEditDiv[data-group] .removeBtn').unbind('click');
        $('.removeEditDiv[data-group] .removeBtn').on('click', function () {
            let $v = $(this);
            let $tag = $v.closest('.removeEditDiv');
            let group = $tag.data('group');
            let index = $tag.data('seq');
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            $('.removeEditRow [data-group="' + group + '"][data-seq="' + index + '"][data-prefix="' + prefix + '"]').closest('.removeEditRow').remove();
            $('[data-group="' + group + '"][data-seq="' + index + '"][data-prefix="' + prefix + '"]:not(.removeEditDiv)').closest('.item-record').remove();
            resetAllItemIndex(group, prefix);
            let $cal4 = $('[data-prefix="' + prefix + '"][data-condition="' + group + '"][data-specialcondition="SPECCON04"]');
            if (!isEmptyNode($cal4)) {
                checkItemTotal($cal4);
            }
            unDisableContent($('.person-content'))
            $('.person-content').find('.isPartEdit').val('1')
            $('#isEditHiddenVal').val('1')
            refreshGroupIndex();
        });
    }

    function resetAllItemIndex(group, prefix) {
        if (isEmpty(prefix)) {
            prefix = "";
        }
        refreshRemoveBtn();
        refreshAddBtn();
        let rowAry = [];
        $('[data-group="' + group + '"][data-prefix="' + prefix + '"]:not(.removeEditDiv)').closest('.item-record').each(function () {
            let data = $(this).attr('class');
            if (!rowAry.includes(data)) {
                rowAry.push(data);
            }
        });
        rowAry.forEach(function (item) {
            $('div[class="' + item + '"]').each(function (index, ele) {
                resetItem($(ele).find('[data-seq]'), index);
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
        let prefix = $ele.data('prefix');
        if (isEmpty(prefix)) {
            prefix = "";
        }
        //let odlIndex = $ele.data('seq');
        $ele.attr('data-seq', index);
        let baseName = $ele.data('base');
        if (!isEmpty(baseName)) {
            $ele.attr('name', prefix + baseName + index);
        }
        let oldId = $ele.attr('id');
        if (!isEmpty(oldId)) {
            let baseId = $ele.data('id');
            if (!isEmpty(baseId)) {
                $ele.attr('id', prefix + baseId + index);
            }
            let $eleLabel = $('label[for="' + oldId + '"]');
            if (!isEmptyNode($eleLabel)) {
                $eleLabel.attr('for', prefix + baseId + index);
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
            let prefix = $target.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            let index = parseInt($('input[name="' + prefix + group + '"]').val());
            // remove the group error message
            $('.error_' + prefix + group).remove();
            let $romveRow = $('.removeEditRow [data-group="' + group + '"][data-seq="0"][data-prefix="' + prefix + '"]').closest('.removeEditRow').clone();
            resetItemIndex($romveRow, index);
            showTag($romveRow);
            $target.before($romveRow);
            // main content
            let $itemRecords = $('[data-group="' + group + '"][data-seq="0"][data-prefix="' + prefix + '"]:not(.removeEditDiv)').closest('.item-record').clone();
            resetItemIndex($itemRecords, index);
            $target.before($itemRecords);
            $('input[name="' + prefix + group + '"]').val(index + 1);
            let $cal4 = $('[data-prefix="' + prefix + '"][data-condition="' + group + '"][data-specialcondition="SPECCON04"]');
            if (!isEmptyNode($cal4)) {
                checkItemTotal($cal4);
            }
            removeBtnEvent();
            refreshAddBtn();

            clearFields($('[data-group="' + group + '"][data-seq="' + index + '"][data-prefix="' + prefix + '"]'));

            unDisableContent($('.person-content'))

            $(".isPartEdit").val('1')
            $("#isEditHiddenVal").val('1')
            checkItemEvent();
            $('.item-record [data-curr][data-prefix="' + prefix + '"]').each(function () {
                checkItemMandatory($(this));
            });
            refreshGroupIndex();
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
            checkItemTotalCondition($(this));
            checkPrsNo($(this));
        });
    }

    function checkItemMandatory($tag) {
        if (isEmptyNode($tag)) {
            return;
        }
        let prefix = $tag.data('prefix');
        if (isEmpty(prefix)) {
            prefix = "";
        }
        let seq = $tag.data('seq');
        let curr = $tag.data('curr');
        let $conNodes = $('[data-parent*="' + curr + '"][data-seq="' + seq + '"][data-prefix="' + prefix + '"]');
        let currVal = getValue($tag);
        if (!isEmptyNode($conNodes)) {
            // only check hidden class
            // if use is(':hidden') to check it, and the target node is under collapsed pannel,
            // the behavior is wrong
            if ($tag.hasClass('hidden')) {
                $conNodes.each(function () {
                    let $v = $(this);
                    let $target = $v.closest('.item-record');
                    hideTag($target);
                    // check add more button
                    let group = $v.data('group');
                    if (!isEmpty(group)) {
                        checkAddMore(group, prefix);
                    }
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
                    // check add more button
                    let group = $v.data('group');
                    checkAddMore(group, prefix);
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
                } else if ('5' == mandatory) {
                    let group = $v.data('group');
                    if (isEmpty(group)) {
                        return;
                    }
                    let total = $('input[name="' + prefix + group + '"]').val();
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
                    let v = $v.data('curr');
                    for (let i = 0; i < total; i++) {
                        let $newV = $('[data-curr="' + v + '"][data-seq="' + i + '"][data-prefix="' + prefix + '"]');
                        let $target = $newV.closest('.item-record');
                        let $targetLabel = $target.find('.item-label');
                        if (!isEmptyNode($targetLabel)) {
                            $targetLabel.find('.mandatory').remove();
                            $targetLabel.append('<span class="mandatory">*</span>');
                        }
                        toggleTag($target, isIncluded);
                        checkAddMore(group, prefix);
                        checkItemMandatory($newV);
                    }
                    // check add more button
                    checkAddMore(group, prefix);
                } else if ('6' == mandatory && $tag.is('select')) {
                    // selection others
                    let $target = $v.closest('.item-record');
                    let parentVal = $tag.val();
                    let conVal = $v.data('mandatory-cond');
                    toggleTag($target, parentVal == conVal);
                }
            });
        }
    }

    function checkAddMore(group, prefix) {
        let $items = $('[data-group="' + group + '"][data-prefix="' + prefix + '"]').closest('.item-record');
        if (isEmptyNode($items)) {
            return;
        }
        let show = false;
        $items.each(function (idx, ele) {
            if (!$(ele).hasClass('hidden')) {
                show = true;
            }
        });
        if (!show) {
            hideTag($('.addMoreDiv[data-group="' + group + '"][data-prefix="' + prefix + '"]'));
            $('.removeEditRow [data-group="' + group + '"][data-prefix="' + prefix + '"]:not([data-seq="0"])').closest('.removeEditRow').remove();
            $('[data-group="' + group + '"][data-prefix="' + prefix + '"]:not([data-seq="0"])').closest('.item-record').remove();
            $('input[name="' + prefix + group + '"]').val(1);
        } else {
            showTag($('.addMoreDiv[data-group="' + group + '"][data-prefix="' + prefix + '"]'));
        }
    }

    function checkItemTotalCondition($tag) {
        if (isEmptyNode($tag)) {
            return -1;
        }
        let $target = getTotalParentNode($tag);
        if (isEmptyNode($target)) {
            return -1;
        }
        let $v = getTotalParentNode($target);
        if (!isEmptyNode($v)) {
            $target = $v;
        }
        checkItemTotal($target);
    }

    function getTotalParentNode($tag) {
        let targetId = $tag.data('condition');
        if (isEmpty(targetId)) {
            return null;
        }
        let prefix = $tag.data('prefix');
        if (isEmpty(prefix)) {
            prefix = "";
        }
        let seq = $tag.data('seq');
        return $('[data-curr="' + targetId + '"][data-seq="' + seq + '"][data-prefix="' + prefix + '"]');
    }

    function checkItemTotal($tag) {
        if (isEmptyNode($tag)) {
            return -1;
        }
        let prefix = $tag.data('prefix');
        if (isEmpty(prefix)) {
            prefix = "";
        }
        let condition = $tag.data('specialcondition');
        if ('SPECCON01' === condition) {
            let seq = $tag.data('seq');
            let curr = $tag.data('curr');
            let $conNodes = $('[data-condition*="' + curr + '"][data-seq="' + seq + '"][data-prefix="' + prefix + '"]');
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
                $('[name="' + prefix + curr + seq + '"]').val(total);
                $target.html(total);
            }
            return total;
        } else if ('SPECCON04' === condition) {// group count
            let seq = $tag.data('seq');
            let curr = $tag.data('curr');
            let group = $tag.data('condition');
            let total = $('input[name="' + prefix + group + '"]').val();
            if ($tag.is(':input')) {
                fillValue($tag, total);
            } else {
                var $target = $tag.find('p');
                if (isEmptyNode($target)) {
                    $target = $tag;
                }
                $('[name="' + prefix + curr + seq + '"]').val(total);
                $target.html(total);
            }
            return total;
        }
        return -1;
    }

    function checkPrsNo($tag) {
        if (isEmptyNode($tag)) {
            return;
        }
        let specialcondition = $tag.data('specialcondition');
        if ('SPECCON03' !== specialcondition) {
            return;
        }
        showWaiting();
        let regNo = getValue($tag);
        if (isEmpty(regNo)) {
            fillPrsData($tag, null);
            dismissWaiting();
            return;
        }
        let jsonData = {
            'prgNo': regNo
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                var canFill = false;
                if (isEmpty(data)) {
                    console.log("The return data is null for PRS");
                } else if ('-1' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if ('-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else {
                    canFill = true;
                }
                console.log("data=====>",data)
                fillPrsData($tag, canFill ? data : null);
                dismissWaiting();
            },
            'error': function () {
                fillPrsData($tag, null);
                dismissWaiting();
            }
        });
    }

    function fillPrsData($prsRegNo, data) {
        let condId = $prsRegNo.data('curr');
        if (isEmpty(condId)) {
            return;
        }
        let seq = $prsRegNo.data('seq');
        let prefix = $prsRegNo.data('prefix');
        if (isEmpty(prefix)) {
            prefix = "";
        }
        let $target = $('[data-condition*="' + condId + '"][data-seq="' + seq + '"][data-prefix="' + prefix + '"]');
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function () {
            let $tag = $(this);
            let condition = $tag.data('specialcondition');
            if (isEmpty(condId)) {
                return;
            }
            let v = "";
            console.log("condition===>",condition)
            if (!isEmpty(data)) {
                v = data[condition];
                if (isEmpty(v) && !isEmpty(data.registration)) {
                    let registration = data.registration[0];
                    v = registration[condition];
                }
            }
            console.info("PRS Data - " + condition + " : " + v);
            if ($tag.is(':input')) {
                fillValue($tag, v);
            } else {
                $tag.find('p').html(v);
            }
        });
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

    function refreshGroupIndex() {
        let $groupTitle = $('.item-record .group-title');
        if (isEmptyNode($groupTitle)) {
            return;
        }
        let indexGroupAry = [];
        $groupTitle.each(function () {
            let group = $(this).data('group');
            let prefix = $(this).data('prefix');
            let spanClass = 'span.' + prefix + '-' + group;
            if ($(this).find(spanClass).length > 0) {
                indexGroupAry.push(spanClass);
            }
        });
        if (indexGroupAry.length <= 0) {
            return;
        }
        console.log(indexGroupAry,'=========indexGroupAry=========>>>>')
        let i = 0;
        let target = indexGroupAry[0];
        for (let x of indexGroupAry) {
            if (x != target) {
                refreshItemGroupIndex(target, i);
                // init
                i = 1;
                target = x;
            } else {
                i++;
            }
        }
        refreshItemGroupIndex(target, i);
    }

    function refreshItemGroupIndex(target, max) {
        if (max <= 1) {
            $(target).html('');
        } else {
            let i = 1;
            $(target).each(function () {
                $(this).html(i++);
            });
        }
    }

</script>