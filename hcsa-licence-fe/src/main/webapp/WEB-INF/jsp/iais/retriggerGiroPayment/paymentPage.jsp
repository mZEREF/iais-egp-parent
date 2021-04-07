<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/10/27
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../newApplication/payment.jsp"%>

<script>
    $(document).ready(function () {
        $('#nav-tabs-ul').css('display','none');
        $('#paymentTab').css('margin-top','3%');

        $('#premisesli').removeClass('incomplete');
        $('#premisesli').addClass('complete');

        $('#documentsli').removeClass('incomplete');
        $('#documentsli').addClass('complete');

        $('#serviceFormsli').removeClass('incomplete');
        $('#serviceFormsli').addClass('complete');

        $('#previewli').removeClass('incomplete');
        $('#previewli').addClass('complete');
    });
</script>