<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div class="row">
    <input type="hidden" id="historyKey" name="historyKey" value="">
    <div class="col-xs-12">
        <div class="components">
            <h3>
                <span>Search Results</span>
            </h3>
            <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
            <div class="table-responsive">
                <div class="table-gp">
                    <c:if test="${count == 'agent'}">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                            </tr>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Facility Name"
                                                     value="Facility Name"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Biological Agent/Toxin"
                                                     value="Biological Agent/Toxin"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Quantity of Toxin in Possession"
                                                     value="Quantity of Toxin in Possession"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Physical Possession of Biological Agent"
                                                     value="Physical Possession of Biological Agent"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Type of Transaction"
                                                     value="Type of Transaction"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Transaction History"
                                                     value="Transaction History"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty inventoryResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${inventoryResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out  value="${status.index + 1}"/></td>
                                            <td>${item.facName}</td>
                                            <td>${item.bat}</td>
                                            <td>${item.toxinQty}</td>
                                            <td>${item.batPossession}</td>
                                            <td>${item.transactionType}</td>
                                            <td><a onclick="doHisInfo('${MaskUtil.maskValue("key",item.dataSubBatId)}')">${item.dataSubNo}</a></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${count == 'date'}">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                            </tr>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Name of Biological Agent/Toxin"
                                                     value="Name of Biological Agent/Toxin"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Sending Facility"
                                                     value="Sending Facility"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Receiving Facility"
                                                     value="Receiving Facility"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Transaction type"
                                                     value="Transaction type"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Transaction Date"
                                                     value="Transaction Date"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Transaction Status"
                                                     value="Transaction Status"/>
                                <iais:sortableHeader needSort="false"
                                                     field="Transaction History"
                                                     value="Transaction History"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty inventoryResult}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${inventoryResult}" varStatus="status">
                                        <tr name="basicData">
                                            <td><c:out  value="${status.index + 1}"/></td>
                                            <td>${item.batName}</td>
                                            <td>${item.sendFacility}</td>
                                            <td>${item.recFacility}</td>
                                            <td>${item.transactionType}</td>
                                            <td>${item.transactionDt}</td>
                                            <td><a onclick="doHisInfo('${MaskUtil.maskValue("key",item.dataSubBatId)}')">${item.transactionStatus}</a></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>