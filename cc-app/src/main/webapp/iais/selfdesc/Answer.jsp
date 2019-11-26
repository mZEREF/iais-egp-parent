<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/22/2019
  Time: 9:37 AM
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="item" items="${renderResultAttr.rows}" varStatus="status">
  <tr>
    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <p><a href="#">${item.configId}</a></p>
    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <p>${item.regClauseNo}</p>
    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <p>${item.checklistItem}</p>
    </td>


    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <c:choose>
        <c:when test="${item.answer eq 'YES'}">
          <p><input name="${item.itemId}" type="radio"  checked="checked" onclick="javascript:selectAnswer('YES', '${item.itemId}')"/></p>
        </c:when>
        <c:otherwise>
          <p><input name="${item.itemId}" type="radio" onclick="javascript:selectAnswer('YES', '${item.itemId}')"/></p>
        </c:otherwise>
      </c:choose>

    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <c:choose>
        <c:when test="${item.answer eq 'NO'}">
          <p><input name="${item.itemId}" type="radio" checked="checked" onclick="javascript:selectAnswer('NO', '${item.itemId}')"/></p>
        </c:when>
        <c:otherwise>
          <p><input name="${item.itemId}" type="radio"  onclick="javascript:selectAnswer('NO', '${item.itemId}')"/></p>
        </c:otherwise>
      </c:choose>
    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <c:choose>
        <c:when test="${item.answer eq 'NA'}">
          <p><input name="${item.itemId}" type="radio" checked="checked" onclick="javascript:selectAnswer('NA', '${item.itemId}')"/></p>
        </c:when>
        <c:otherwise>
          <p><input name="${item.itemId}" type="radio" onclick="javascript:selectAnswer('NA', '${item.itemId}')"/></p>
        </c:otherwise>
      </c:choose>
    </td>
    <input type="hidden" name="${item.itemId}" value="<c:out value="${item.answer}"/>"/>
  </tr>


</c:forEach>

<script>
    function selectAnswer(answer, val){
        $("[name ="+val+ "]").val(answer);
    }

</script>
