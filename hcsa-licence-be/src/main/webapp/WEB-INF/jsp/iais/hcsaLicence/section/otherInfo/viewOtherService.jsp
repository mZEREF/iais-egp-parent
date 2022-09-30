<c:if test="${otherInfo.existCheckedRels || oldOtherInfo.existCheckedRels}">
    <div class="col-xs-12">
        <div class="app-title">Other Service</div>
    </div>
        <c:forEach var="item" items="${otherInfo.allAppPremSubSvcRelDtoList}" varStatus="status">
            <c:set var="olditem" value="${oldOtherInfo.allAppPremSubSvcRelDtoList[status.index]}"/>
            <c:if test="${item.checked || olditem.checked}">
               <table>
                   <tr>
                       <td>
                    <div class="col-xs-10 form-check active">
                        <div class="newVal" attr="${item.checked}<c:out value="${item.svcName}"/>">
                            <c:if test="${item.checked}">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span> <c:out value="${item.svcName}" />
                                    </p>
                            </c:if>
                        </div>
                    </div>
                       </td>
                       <td>
                    <div class="col-xs-10 form-check active">
                        <div class="oldVal " attr="${olditem.checked}<c:out value="${olditem.svcName}" />">
                            <c:if test="${olditem.checked}">
                                    <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <c:out value="${olditem.svcName}" />
                                    </div>
                            </c:if>
                        </div>
                    </div>
                       </td>
                   </tr>
               </table>
            </c:if>
        </c:forEach>
</c:if>