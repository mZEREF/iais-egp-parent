$(function (){
    $("select[data-cascade-dropdown=authPersonnel-detail]").change(function (){
        var id = $(this).attr("id");
        var selectVal = $(this).val();
        var idx = id.substring("personnel--v--".length, id.length);
        if(selectVal === ""){
            $("#authPersonnelInfo--v--"+idx).hide();
        }else{
            $("#authPersonnelInfo--v--"+idx).show();
            if(authPersonnelDetailDataJson[selectVal] != null){
                modifyAuthPersonnelDetails(idx,authPersonnelDetailDataJson[selectVal]);
            }
        }
    })
})



function modifyAuthPersonnelDetails(idx,singleDataJson){
    var key = 'authPersonnelInfo'+idx;
    $("label[data-cascade-label ="+key+"]").each(function (){
       var labelId =  $(this).attr("id");
       var name = labelId.split("--v--")[0];
       $(this).html(singleDataJson[name]);
    })
}
