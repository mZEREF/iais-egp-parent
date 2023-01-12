<%--
  Created by IntelliJ IDEA.
  User: ECQ
  Date: 2023/1/12
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<script>
    function batchUpload(idForm,fileAppendId){
        showWaiting();
        var form = new FormData();
        var file = $('#uploadFile')[0].files[0];
        form.append('fileAppendId',fileAppendId);
        form.append('selectedFile',file);
        form.append('uploadFormId',idForm);
        form.append('reloadIndex','-1');
        form.append('needGlobalMaxIndex','false');

        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/ajax-batchUpload-file?stamp="+new Date().getTime(),
            data: form,
            async:true,
            dataType: "json",
            processData: false,
            contentType: false,
            success: function (data) {
                // if(data != null && data.description != null){
                //     alert(data.description);
                // }
                dismissWaiting();
            },
            error: function (msg) {
                // alert("error");
                dismissWaiting();
            }
        });
    }

</script>
