
<div class="row">
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <c:if test="${count == 'agent'}">
                                <table class="table">
                                    <thead>
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
                                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="items" items="${inventoryResult}" varStatus="status">
                                                <tr name="basicData">
                                                    <td><c:out  value="${status.index + 1}"/></td>
                                                    <td></td>
                                                    <td></td>
                                                    <td>15000.000</td>
                                                    <td>
                                                    </td>
                                                    <td>Export</td>
                                                    <td><a onclick="javascript:doHisInfo()">Transaction 01</a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                                </c:if>
                                <c:if test="${count == 'date'}">
                                    <table class="table">
                                        <thead>
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
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="items" items="${inventoryResult}" varStatus="status">
                                                    <tr name="basicData">
                                                        <td><c:out  value="${status.index + 1}"/></td>
                                                        <td></td>
                                                        <td>BBC</td>
                                                        <td>NPA</td>
                                                        <td>complete</td>
                                                        <td>06/18/2021</td>
                                                        <td>active</td>
                                                        <td><a onclick="javascript:doHisInfo()">Transaction 01</a></td>
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
        </div>
    </div>
</div>