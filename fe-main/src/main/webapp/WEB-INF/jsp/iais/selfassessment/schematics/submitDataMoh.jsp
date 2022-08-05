<div class="form-check-gp">
    <p class="form-check-title">Please select the type of data that you will be submitting:</p>
    <input type="radio" name="test" > sdfsf
    <br>
    <c:if test="${dataSubARTPrivilege == 1}">
        <div class="form-check progress-step-check" style="width: 65%">
            <input class="form-check-input" id="submitDateMohArt"
                   type="radio" name="submitDateMohLab" <c:if test="${DsModleSelect=='AR'}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label" for="submitDateMohArt">
                <span class="check-circle"></span>
                <span class="left-content">Assisted Reproduction (ART)</span>
            </label>
        </div>
    </c:if>
    <c:if test="${dataSubDPPrivilege == 1}">
        <div class="form-check progress-step-check" style="width: 65%">
            <input class="form-check-input" id="submitDateMohDrp"
                   type="radio" name="submitDateMohLab" <c:if test="${DsModleSelect=='DP'}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label" for="submitDateMohDrp">
                <span class="check-circle"></span>
                <span class="left-content">Drug Practices (DP)</span>
            </label>
        </div>
    </c:if>
    <c:if test="${dataSubLDTPrivilege == 1}">
        <div class="form-check progress-step-check" style="width: 65%">
            <input class="form-check-input" id="submitDateMohLab"
                   type="radio" name="submitDateMohLab" <c:if test="${DsModleSelect=='LDT'}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label" for="submitDateMohLab">
                <span class="check-circle"></span>
                <span class="left-content">Laboratory Developed Tests (LDT)</span>
            </label>
        </div>
    </c:if>
    <c:if test="${dataSubTOPPrivilege == 1}">
        <div class="form-check progress-step-check" style="width: 65%">
            <input class="form-check-input" id="submitDateMohTop"
                   type="radio" name="submitDateMohLab" <c:if test="${DsModleSelect=='TP'}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label" for="submitDateMohTop">
                <span class="check-circle"></span>
                <span class="left-content">Termination of Pregnancy (TOP)</span>
            </label>
        </div>
    </c:if>
    <c:if test="${dataSubVSSPrivilege == 1}">
        <div class="form-check progress-step-check" style="width: 65%">
            <input class="form-check-input" id="submitDateMohVss"
                   type="radio" name="submitDateMohLab" <c:if test="${DsModleSelect=='VS'}">checked</c:if>
                   aria-invalid="false">
            <label class="form-check-label" for="submitDateMohVss">
                <span class="check-circle"></span>
                <span class="left-content">Voluntary Sterilization (VS)</span>
            </label>
        </div>
    </c:if>
    <a class="btn btn-primary " onclick="submitDataMoh()" href="javascript:void(0);">NEXT</a>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
    $("#submitDataMoh").attr('checked', 'true');
</script>