
<div class="row">
    <style>
        .nice-select {
            /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
            background: #efefef;
            /* Old browsers */
            background: -moz-linear-gradient(left, #efefef 0%, white 100%);
            /* FF3.6-15 */
            background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
            /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to right, #efefef 0%, white 100%);
            border: 1px solid #6c6c6c;
            border-radius: 14px;
            font-size: 1.6rem;
            padding: 14px 40px 14px 15px;
            height: auto;
            line-height: 22px;
            width: 100%;
            -webkit-transition: all 0.1s ease;
            -moz-transition: all 0.1s ease;
            -ms-transition: all 0.1s ease;
            -o-transition: all 0.1s ease;
            transition: all 0.1s ease;
            white-space: normal;
            text-overflow: inherit;
        }
    </style>
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>

                        <iais:pagination param="SearchParam" result="SearchResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <c:if test="${count == '1'}">
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
                                    <tr name="basicData">
                                        <td>1</td>
                                        <td>Biological Agent/Toxin</td>
                                        <td>Complete</td>
                                        <td>ERFWH-AJEW-IQO1</td>
                                        <td>Yes</td>
                                        <td>Export</td>
                                        <td><a onclick="javascript:doHisInfo()">Transaction 01</a></td>
                                    </tr>
                                    </tbody>
                                </table>
                                </c:if>
                                <c:if test="${count == '2'}">
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
                                        <tr name="basicData">
                                            <td>1</td>
                                            <td>Biological Agent/Toxin</td>
                                            <td>BBC</td>
                                            <td>NPA</td>
                                            <td>complete</td>
                                            <td>06/18/2021</td>
                                            <td>active</td>
                                            <td><a onclick="javascript:doHisInfo()">Transaction 01</a></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${count == '0'}">
                                    <iais:message key="GENERAL_ACK018"
                                                  escape="true"/>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>