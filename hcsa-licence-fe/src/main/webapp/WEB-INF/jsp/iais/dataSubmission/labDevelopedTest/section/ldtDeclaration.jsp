<strong style="font-size: 2rem;">Declarations</strong>
<c:if test="${empty declaration}">
    <c:set var="declaration" value="${LdtSuperDataSubmissionDto.dataSubmissionDto.declaration}"/>
</c:if>
<iais:row>
    <div class="form-check">
        <c:if test="${DeclarationsCheckBox != 'hide'}">
            <input class="form-check-input" id="declaration" type="checkbox" name="declaration" aria-invalid="false"
                   value="1"
                    <c:if test="${!empty LdtSuperDataSubmissionDto.dataSubmissionDto.declaration}">
                        checked="checked"
                    </c:if>
                   <c:if test="${formPriview == 1}">disabled="disabled"</c:if>
            />
        </c:if>
        <label class="form-check-label" for="declaration">
            <c:if test="${DeclarationsCheckBox != 'hide'}">
                <span class="check-square"></span>
            </c:if>
            <iais:message key="DS_DEC001" escape="false"/>
        </label>
        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
    </div>
</iais:row>