<c:if test="${empty declaration}">
<c:set var="declaration" value="${arSuperDataSubmissionDto.dataSubmissionDto.declaration}" />
</c:if>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#ar-declaration">
                Declarations
            </a>
        </h4>
    </div>
    <div id="ar-declaration" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <div class="form-check">
                        <input class="form-check-input" id="declaration" type="checkbox" name="declaration" aria-invalid="false" value="1"  >
                        <label class="form-check-label" for="declaration"><span class="check-square"></span><iais:message key="DS_DEC001" escape="false" /></label>
                        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
                    </div>

                    <%--<div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${declaration == 'Yes'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="Yes" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                    </div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${declaration == 'No'}">checked="checked"</c:if>
                               type="radio" name="declaration" value="No" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span>No</label>
                    </div>
                    <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>--%>
                </iais:row>
            </div>
        </div>
    </div>
</div>