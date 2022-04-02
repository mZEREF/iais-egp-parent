<%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="answerErrorMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<div class="panel-body">
    <div class="col-xs-12 form-group">
        <h4 style="font-size: 16px">I, hereby declare the following:</h4>
        <br/>
        <ol style="padding-left: 16px">
        <c:forEach var="item" items="${configList}">
            <c:set var="maskedId"><iais:mask name="declaration" value="${item.id}"/></c:set>
            <li class="col-xs-12" style="padding-left:0">
                <div class="col-xs-8 form-group" style="padding-left: 0">${item.statement}</div>
                <div class="form-check col-xs-2">
                    <input class="form-check-input" type="radio" name="MID${maskedId}" id="MID${maskedId}Yes" value="Y" <c:if test="${'Y' eq answerMap.get(item.id)}">checked="checked"</c:if>>
                    <label for="MID${maskedId}Yes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                </div>
                <div class="form-check col-xs-2">
                    <input class="form-check-input" type="radio" name="MID${maskedId}" id="MID${maskedId}No" value="N" <c:if test="${'N' eq answerMap.get(item.id)}">checked="checked"</c:if>>
                    <label for="MID${maskedId}No" class="form-check-label"><span class="check-circle"></span>No</label>
                </div>
                <span class="error-msg col-xs-12 form-group">${answerErrorMap.get(item.id)}</span>
            </li>
        </c:forEach>
        </ol>
    </div>
</div>