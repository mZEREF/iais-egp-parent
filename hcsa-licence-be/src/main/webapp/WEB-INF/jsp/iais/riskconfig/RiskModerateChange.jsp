<script type="text/javascript">
     function doChangeVal(moId,id,left) {
        if(left){

            var   moIdVal =  $(moId).val();
            if(moIdVal != null && moIdVal != ""){
                try{
                    var reg = /^[0-9]*$/;
                    if( reg.test(moIdVal) && parseInt(moIdVal) != 0 && parseInt(moIdVal) <=998){
                        $(id).val(parseInt(moIdVal)-1);
                    }else if( reg.test(moIdVal) &&parseInt(moIdVal) == 0 ){
                        $(id).val(0);
                    } else if(reg.test(moIdVal) && parseInt(moIdVal) == 999){
                        $(moId).val(998);
                        $(id).val(997);
                    }
                }catch (e) {
                }
            }
        }else {
            var   moIdVal =  $(moId).val();
            if(moIdVal != null && moIdVal != ""){
                try{
                    var reg = /^[0-9]*$/;
                    if( reg.test(moIdVal) && parseInt(moIdVal) <= 998)
                        $(id).val(parseInt(moIdVal)+1);
                    else if(reg.test(moIdVal) && parseInt(moIdVal) == 999){
                        $(moId).val(998);
                        $(id).val(999);
                    }
                }catch (e) {
                }
            }
        }
    }
    </script>