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
      <p><input name="${item.itemId}" type="radio" value="Yes" /></p>
    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <p><input name="${item.itemId}" type="radio" value="No" /></p>
    </td>

    <td>
      <p class="visible-xs visible-sm table-row-title"></p>
      <p><input name="${item.itemId}" type="radio" value="NA" /></p>
    </td>
  </tr>
</c:forEach>
