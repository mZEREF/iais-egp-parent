<div class="form-check-gp">
    <p class="form-check-title">Please select the type of data that you will be submitting:</p>
    <div class="form-check progress-step-check" style="width: 65%">
        <input class="form-check-input" id="submitDateMohLab"
               type="radio" name="submitDateMohLab"
               aria-invalid="false">
        <label class="form-check-label" for="submitDateMohLab">
            <span class="check-circle"></span>
            <span class="left-content">Laboratory Developed Tests (LDT)</span>
        </label>
    </div>
    <a class="btn btn-primary " onclick="submitDataMoh()" href="javascript:void(0);">NEXT</a>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
    $('#submitDataMoh').click(function () {
        $('.submitDataMoh').removeClass('disabled');
        $('#submitDateMohLab').prop('checked', true);
    })

</script>