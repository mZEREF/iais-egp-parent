$(document).ready(function () {
    toggleOnSelect("#ethnicGroup",'ETHG005', 'ethnicOthers');

    toggleSelect("#idType",'AR_IT_004', 'nationalityStar');
    toggleSelect("#addrType",'ADDTY001', 'blkNoStar');
    toggleSelect("#addrType",'ADDTY001', 'floorNoStar');
});
function  test(sel,val,id1,id2){
    toggleSelect(sel,val,id1);
    toggleSelect(sel,val,id2);
}
function toggleSelect(sel, val, elem) {
    if (isEmpty(sel)) {
        return;
    }
    var $selector = $(sel);
    if ($selector.length == 0) {
        $selector = $('#' + sel);
    } else if ($selector.length == 0) {
        $selector = $('.' + sel);
    }
    var $target = $(elem);
    if ($target.length == 0) {
        $target = $('#' + elem);
    } else if ($target.length == 0) {
        $target = $('.' + sel);
    }
    if ($selector.length == 0 || $target.length == 0) {
        return;
    }
    if ($selector.val() == val) {
        $target.text("*")
    } else {
        $target.text("")
    }
}