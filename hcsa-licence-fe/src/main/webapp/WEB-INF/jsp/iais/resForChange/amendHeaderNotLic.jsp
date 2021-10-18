<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<div class="col-xs-12">
    <div class="dashboard-page-title">
    <input type="hidden" value="${transfer}" name="transfer" id="transfer">
    <h1>Amendment</h1>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        if($('#transfer').val()=='transfer'){
            $('#transfer').closest('div').attr("style","margin-left:5.5%");
        }
    });

</script>