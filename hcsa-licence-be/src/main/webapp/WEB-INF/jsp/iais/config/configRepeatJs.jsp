<script type="text/javascript">
    function showNEW() {
        var jQuery = JSON.stringify($('#APTY002').attr("style"));
        $('#APTY001').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        if(jQuery.indexOf('display: block') >=0 ){
            $('#APTY002').attr("style","display: none");
        }else if(jQuery.indexOf('display: none') >=0){
            $('#APTY002').attr("style","display: block");
        }
    }

    function showRENEW() {
        var jQuery = JSON.stringify($('#APTY004').attr("style"));
        $('#APTY001').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY002').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        if(jQuery.indexOf('display: block')>=0 ){
            $('#APTY004').attr("style","display: none");
        }else if(jQuery.indexOf('display: none')>=0 ){
            $('#APTY004').attr("style","display: block");
        }
    }

    function showAPPEAL(){
        var jQuery = JSON.stringify($('#APTY001').attr("style"));
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        if(jQuery.indexOf('display: block')>=0 ){
            $('#APTY001').attr("style","display: none");
        }else if(jQuery.indexOf('display: none')>=0 ){
            $('#APTY001').attr("style","display: block");
        }
    }

    function showRFC(){
        var jQuery = JSON.stringify($('#APTY005').attr("style"));
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery.indexOf('display: block')>=0 ){
            $('#APTY005').attr("style","display: none");
        }else if(jQuery.indexOf('display: none')>=0){
            $('#APTY005').attr("style","display: block");
        }

    }

    function showCESSATION(){
        var jQuery =  JSON.stringify($('#APTY008').attr("style"));
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery.indexOf('display: block')>=0 ){
            $('#APTY008').attr("style","display: none");
        }else if(jQuery.indexOf('display: none')>=0){
            $('#APTY008').attr("style","display: block");
        }
    }

    /*  function showSUSPENSION(){
          let jQuery = $('#APTY010').attr("style");
          $('#APTY002').attr("style","display: none");
          $('#APTY004').attr("style","display: none");
          $('#APTY006').attr("style","display: none");
          $('#APTY008').attr("style","display: none");
          $('#APTY005').attr("style","display: none");
          $('#APTY001').attr("style","display: none");
          if(jQuery=='display: block'){
              $('#APTY010').attr("style","display: none");
          }else if(jQuery=='display: none'){
              $('#APTY010').attr("style","display: block");
          }
      }
  */
    function  showWITHDRAWAL(){
        var jQuery = JSON.stringify($('#APTY006').attr("style"));
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery.indexOf('display: block')>=0 ){
            $('#APTY006').attr("style","display: none");
        }else if(jQuery.indexOf('display: none')>=0 ){
            $('#APTY006').attr("style","display: block");
        }
    }
    function showREVOCATION(){

    }

</script>