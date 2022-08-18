$(document).ready(function () {

    $('#country').change(function (){
        countryStar();
    });
    countryStar();

    var appType = $('input[name="appType"]').val();
    var isNew = $('input[name="isNew"]').val();
    if('DSTY_005'==appType && 'Y' != isNew){
        disableContent('div.patient');
    }
    toggleOnSelect("#ethnicGroup",'ETHG005', 'ethnicOthers');

    toggleSelect("#idType",'DTV_IT003', 'nationalityStar');
    toggleSelect("#addrType",'ADDTY001', 'blkNoStar');
    toggleSelect("#addrType",'ADDTY001', 'floorNoStar');
});
function  test(sel,val,id1,id2){
    toggleSelect(sel,val,id1);
    toggleSelect(sel,val,id2);
}
function countryStar(){
    var country= $('#country option:selected').val();
    if(isEmpty(country) || "SG" == country){
        $('#cityStar').text("");
        $('#stateStar').text("");
        if("SG" == country){
            $('#postalCode').prop('maxLength',6);
            var postalCode=$('#postalCode').val();
            $('#postalCode').val(postalCode.slice(0,6))
        }
    }else {
        $('#cityStar').text("*");
        $('#stateStar').text("*");
        $('#postalCode').prop('maxLength',20);
    }
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