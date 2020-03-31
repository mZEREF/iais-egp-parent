<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/22/2019
  Time: 9:37 AM
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="selfDesc" items="${selfDeclQueryAttr}" varStatus="status">
    <c:if test="${tabIndex == null && selfDesc.common == true}">
      <c:forEach var="answerMap" items="${selfDesc.eachPremQuestion}">
        <c:forEach items="${answerMap.value}" var="list" varStatus="status">
          <td><input type="hidden" name="${list.answerKey}" value=""/></td>
          <tr>
            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${status.index + 1}</p>
            </td>

            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${list.regulation}</p>
            </td>

            <td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${list.checklistItem}</p>
            </td>

            <%--<td>
              <p class="visible-xs visible-sm table-row-title"></p>
              <p>${list.address}</p>
            </td>--%>

            <td>
              <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'YES'}">checked="checked"</c:if> onclick="javascript:selectAnswer('YES', '${list.answerKey}')"/></p>
            </td>

            <td>
              <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'NO'}">checked="checked"</c:if> onclick="javascript:selectAnswer('NO', '${list.answerKey}')"/></p>
            </td>

            <td>
              <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'NA'}">checked="checked"</c:if> onclick="javascript:selectAnswer('NA', '${list.answerKey}')"/></p>
            </td>
          </tr>

        </c:forEach>
      </c:forEach>
    </c:if>

  <c:if test="${tabIndex != null && selfDesc.common == false && tabIndex eq selfDesc.svcId}">
    <c:forEach var="answerMap" items="${selfDesc.eachPremQuestion}">
      <c:forEach items="${answerMap.value}" var="list" varStatus="status">
        <td><input type="hidden" name="${list.answerKey}" value=""/></td>
        <tr>
          <td>
            <p class="visible-xs visible-sm table-row-title"></p>
            <p>${status.index + 1}</p>
          </td>

          <td>
            <p class="visible-xs visible-sm table-row-title"></p>
            <p>${list.regulation}</p>
          </td>

          <td>
            <p class="visible-xs visible-sm table-row-title"></p>
            <p>${list.checklistItem}</p>
          </td>

         <%-- <td>
            <p class="visible-xs visible-sm table-row-title"></p>
            <p>${list.address}</p>
          </td>--%>

          <td>
            <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'YES'}">checked="checked"</c:if> onclick="javascript:selectAnswer('YES', '${list.answerKey}')"/></p>
          </td>

          <td>
            <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'NO'}">checked="checked"</c:if> onclick="javascript:selectAnswer('NO', '${list.answerKey}')"/></p>
          </td>

          <td>
            <p><input name="${list.answerKey}" type="radio"  <c:if test="${list.answer == 'NA'}">checked="checked"</c:if> onclick="javascript:selectAnswer('NA', '${list.answerKey}')"/></p>
          </td>
        </tr>

      </c:forEach>
    </c:forEach>
  </c:if>


</c:forEach>

<script type="text/javascript">
    function selectAnswer(answer, val){
        $("[name ="+val+ "]").val(answer);
    }
</script>
