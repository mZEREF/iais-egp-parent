
<c:forEach var="declItem" items="${selfAssessmentDetail.selfAssessmentConfig}" varStatus="status">

    <c:if test="${declItem.configId eq requestScope.tabIndex}">
    <c:forEach var="item" items="${declItem.question}" varStatus="status">
            <tr>
                <td>
                    <p class="visible-xs visible-sm table-row-title"></p>
                    <p>${status.index + 1}</p>
                </td>

                <td>
                    <p class="visible-xs visible-sm table-row-title"></p>
                    <p>${item.regulation}</p>
                </td>


                <td>
                    <p class="visible-xs visible-sm table-row-title"></p>
                    <p>${item.checklistItem}</p>
                </td>

                <td>
                    <p><input name="${item.answerKey}" type="radio" <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if> <c:if test="${item.answer == 'YES'}">checked="checked" value="YES" </c:if> onclick="javascript:draftAnswer('YES', '${item.answerKey}')"/></p>
                </td>

                <td>
                    <p><input name="${item.answerKey}" type="radio"  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if> <c:if test="${item.answer == 'NO'}">checked="checked" value="NO"</c:if> onclick="javascript:draftAnswer('NO', '${item.answerKey}')"/></p>
                </td>

                <td>
                    <p><input name="${item.answerKey}" type="radio"  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if> <c:if test="${item.answer == 'NA'}">checked="checked" value="NA"</c:if> onclick="javascript:draftAnswer('NA', '${item.answerKey}')"/></p>
                </td>
            </tr>
    </c:forEach>
    </c:if>
</c:forEach>






