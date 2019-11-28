<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/22/2019
  Time: 9:37 AM
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="selfDesc" items="${selfDeclQueryAttr}" varStatus="status">
    <c:if test="${tabIndex == null && selfDesc.common == true}">
      <c:forEach var="answerMap" items="${selfDesc.premAnswerMap}">
        <c:forEach items="${answerMap.value}" var="list"><br>
          <input type="hidden" name="${list.checklistItemId}" value="<c:out value="${list.answer}"/>"/>　
          <tr>
            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>1</p>
            </td>

            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${list.regulation}</p>
            </td>

            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${list.checklistItem}</p>
            </td>

            <td>
              <c:if test="${list.answer == 'YES'}">
                <p><input name="${list.checklistItemId}" type="radio"  checked="checked" onclick="javascript:selectAnswer('YES', '${list.checklistItemId}')"/></p>
              </c:if>
              <c:if test="${list.answer == null}">
                <p><input name="${list.checklistItemId}" type="radio" onclick="javascript:selectAnswer('YES', '${list.checklistItemId}')"/></p>
              </c:if>

            </td>
          </tr>

        </c:forEach>
      </c:forEach>
    </c:if>


</c:forEach>

<script>
    function selectAnswer(answer, val){
        $("[name ="+val+ "]").val(answer);
    }

</script>
