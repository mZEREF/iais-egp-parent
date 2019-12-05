<div class="tab-pane" id="tabLicence" role="tabpanel">
    <div class="tab-search license-search clearfix">
        <form class="form-inline" method="post" id="licenceForm" action=<%=process.runtime.continueURL()%>>
            <div class="licence-btns">
                <a class="btn btn-primary disabled" href="javascript:;">Renew</a>
                <a class="btn btn-secondary disabled" href="javascript:;">Cease</a>
                <a class="btn btn-secondary disabled" href="javascript:;">Amend</a>
            </div>
            <div class="search-wrap">
                <div class="input-group">
                    <input class="form-control" id="licenseAdvancedSearch" type="text" placeholder="Licence no." name="licenseAdvancedSearch" aria-label="licenseAdvancedSearch"><span class="input-group-btn">
                              <button class="btn btn-default buttonsearch" title="Search by keywords"><em class="fa fa-search"></em></button></span>
                </div>
            </div><a class="btn btn-default advanced-search" href="#">Advanced Search</a>
        </form>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Licence No.</th>
                        <th class="sorting">Type <span class="column-sort"></span></th>
                        <th>Status <span class="sort"></span></th>
                        <th>Premises <span class="sort"></span></th>
                        <th>Start Date <span class="desc"></span></th>
                        <th>Expiry Date <span class="sort"></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty licResult.rows}">
                            <tr>
                                <td colspan="6">
                                    No Record!!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var = "licenceQuery" items = "${licResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        <div class="form-check">
                                            <input class="form-check-input licenceCheck" id="licence1" type="checkbox" name="licence1" aria-invalid="false">
                                            <label class="form-check-label" for="licence1"><span class="check-square"></span><a href="#">${licenceQuery.licenceNo}</a></label>
                                        </div>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <p>${licenceQuery.svcName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p>Active</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                        <p>111 North Bridge Rd. <br> # 07-04, 179098</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.startDate}" pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.expiryDate}" pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <div class="table-footnote">
                    <div class="row">
                        <div class="col-xs-6 col-md-4">
                            <p class="count">${licResult.rowCount} out of ${licParam.pageNo}</p>
                        </div>
                        <div class="col-xs-6 col-md-8 text-right">
                            <div class="nav">
                                <ul class="pagination">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>