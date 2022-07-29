<c:if test="${empty declaration}">
    <c:set var="declaration" value="${topSuperDataSubmissionDto.dataSubmissionDto.declaration}" />
</c:if>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#top-amendment">
                Declarations
            </a>
        </h4>
    </div>
    <div id="top-amendment" class="panel-collapse collapse ">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <div class="form-check">
                        <c:if test="${DeclarationsCheckBox != 'hide'}">
                            <input class="form-check-input" id="declaration" type="checkbox" name="declaration" aria-invalid="false" value="1"
                            <c:if test="${!empty topSuperDataSubmissionDto.dataSubmissionDto.declaration}">
                                   checked="checked"
                            </c:if>
                                   <c:if test="${formPriview == 1}">disabled="disabled"</c:if>
                            >
                        </c:if>
                        <label class="form-check-label" for="declaration">
                            <c:if test="${DeclarationsCheckBox != 'hide'}">
                                <span class="check-square"></span>
                            </c:if>
                            <iais:message key="DS_DEC001" escape="false"/></label>
                        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
                    </div>
                </iais:row>
            </div>
        </div>
    </div>
</div>