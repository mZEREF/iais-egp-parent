<div class="arQuickView">

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col">AR Centre</th>
                <th scope="col">Frozen Oocytes</th>
                <th scope="col">Thawed Oocytes</th>
                <th scope="col">Fresh Oocytes</th>
                <th scope="col">Frozen Embryos</th>
                <th scope="col">Thawed Embryos</th>
                <th scope="col">Fresh Embryos</th>
                <th scope="col">Frozen Sperms</th>
            </tr>
            </thead>
            <tbody class="form-horizontal">
            <c:forEach var="patientInventory"
                       items="${patientInventoryDtos}"
                       varStatus="status">
                <tr>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.key}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentFrozenOocytes}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentThawedOocytes}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentFreshOocytes}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentFrozenEmbryos}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentThawedEmbryos}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentFreshEmbryos}"/>
                    </td>
                    <td style="vertical-align:middle;">
                        <c:out value="${patientInventory.value.currentFrozenSperms}"/>
                    </td>

                </tr>
            </c:forEach>
            <tr>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.key}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentFrozenOocytes}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentThawedOocytes}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentFreshOocytes}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentFrozenEmbryos}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentThawedEmbryos}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentFreshEmbryos}"/>
                </td>
                <td style="vertical-align:middle;">
                    <c:out value="${patientInventory.value.currentFrozenSperms}"/>
                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>