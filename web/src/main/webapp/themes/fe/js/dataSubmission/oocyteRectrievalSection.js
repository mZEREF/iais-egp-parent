$(document).ready(function () {
    $('input[type="checkbox"]').click(function () {
        let setVal = $(this).prop('checked');
        if (this.name == "isFromPatient" || this.name == "isFromPatientTissue"){
            $("#isFromDonor").attr("disabled",setVal);
            $("#isFromDonorTissue").attr("disabled",setVal);
        }else if (this.name == "isFromDonor" || this.name == "isFromDonorTissue"){
            $("#isFromPatient").attr("disabled",setVal);
            $("#isFromPatientTissue").attr("disabled",setVal);
        }
    });

    $('input[type="text"]').change(function () {
        var totalNum = 0;
        $('input[type="text"]').each(function (){
            let val = $(this).val();
            if (val){
                var intNum = parseInt(val);
                if (intNum){
                    totalNum += intNum;
                }
            }
        })
        $('#totalRetrievedNum').html(totalNum);
    });
});