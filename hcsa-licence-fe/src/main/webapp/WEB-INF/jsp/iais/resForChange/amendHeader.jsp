<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<div class="col-xs-12">
    <input type="hidden" value="${transfer}" name="transfer" id="transfer">
    <h1>Amendment</h1>
    <c:if test="${AppSubmissionDto.licenceNo ne null}">
    <p class="center">You are amending the <strong>${SvcName}  licence (Licence No. ${AppSubmissionDto.licenceNo}</strong>)</p>
    </c:if>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        if($('#transfer').val()=='transfer'){
            $('#transfer').closest('div').attr("style","margin-left:5.5%");
        }
    });

</script>