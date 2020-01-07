<div class="tab-pane" id="tabLic" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="licenceForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="crud_action_type" value="">
            <input type="hidden" name="form_pageTab" value="">
            <input type="hidden" name="licFrom_pageNo" value="">
            <input type="hidden" name="licFrom_pageSize" value="">
            <input type="hidden" name="crud_action_value" value="">
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="licNoPath">Search By Licence No or Path of:</label>
                        <div class="col-xs-5 col-md-56">
                            <input id="licNoPath" name="licNoPath" type="text">
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-12" >
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="licType">Service Type:</label>
                        <div class="col-xs-5 col-md-5">
                            <iais:select name="licType" id="licType" options="licType" firstOption="All"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="licStatus">Licence Status:</label>
                        <div class="col-xs-5 col-md-5">
                            <iais:select name="licStatus" id="licStatus" options="licStatus" firstOption="All"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-md-3" for="fStartDate">Licence Start Date:</label>
                        <div class="col-md-3" >
                            <iais:datePicker id="fStartDate" name="fStartDate"/>
                        </div>
                        <div class="col-xs-1 col-md-1">
                            <span>TO</span>
                        </div>
                        <div class="col-md-3">
                            <iais:datePicker id="eStartDate" name="eStartDate" />
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3">Licence Expiry Date:</label>
                        <div class="col-xs-3 col-md-3">
                            <iais:datePicker id="fExpiryDate" name="fExpiryDate"/>
                        </div>
                    </iais:value>
                    <div class="col-xs-1 col-md-1">
                        <span>TO</span>
                    </div>
                    <iais:value>
                        <div class="col-xs-3 col-md-3">
                            <iais:datePicker id="eExpiryDate" name="eExpiryDate" />
                        </div>
                    </iais:value>
                    <div class="col-xs-2 col-md-2">
                        <button type="button" class="btn btn-primary" onclick="doSearchLic()">SUBMIT</button>
                    </div>
                </div>
            </div>
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
                                            <input class="form-check-input licenceCheck" id="licence1" type="checkbox" name="licenceNo" value="${licenceQuery.licenceNo}" aria-invalid="false">
                                            <label class="form-check-label" for="licence1"><span class="check-square"></span><a href="#">${licenceQuery.licenceNo}</a></label>
                                        </div>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <p>${licenceQuery.svcName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p>${licenceQuery.status}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                        <p>${licenceQuery.premise}</p>
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
                    <tfoot>
                    <div class="row table-info-display">
                        <div class="col-md-4 text-left">
                            <p class="col-md-5 count table-count" style="margin-top:7px;">${licResult.rowCount} out
                                of ${licParam.pageNo}</p>
                            <div class="col-md-1">
                                <select class="table-select" id="licContentSelect">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="30">30</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-8 text-right">
                            <div class="nav">
                                <ul class="pagination" style="margin-top:7px;">
                                    <li>
                                        <a href="#" aria-label="Previous">
                                                <span aria-hidden="true">
                                                    <i class="fa fa-chevron-left" onclick="doSubPageNo('lic')"></i>
                                                </span>
                                        </a>
                                    </li>
                                    <li><a href="#">${licPageNo}</a></li>
                                    <c:if test="${licPageCount > 1}">
                                        <li><a href="#">${licPageNo + 1}</a></li>
                                    </c:if>
                                    <c:if test="${licPageCount > 2}">
                                        <li><a href="#">${licPageNo + 2}</a></li>
                                    </c:if>
                                    <li>
                                        <a href="#" aria-label="Next">
                                                <span aria-hidden="true">
                                                    <i class="fa fa-chevron-right" onclick="doAddPageNo('lic')"></i>
                                                </span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>