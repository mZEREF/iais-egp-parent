<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="tab-pane" id="tabApplication" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline">
            <div class="form-group">
                <label class="control-label" for="applicationType">Type</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <select id="applicationType">
                        <option>Select an type</option>
                        <option selected>All</option>
                        <option>Renewal</option>
                        <option>New Licence</option>
                        <option>Group</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="applicationStatus">Status</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <select id="applicationStatus">
                        <option>Select an status</option>
                        <option selected>All</option>
                        <option>Approved</option>
                        <option>Pending</option>
                        <option>Draft</option>
                    </select>
                </div>
            </div>
            <div class="form-group large right-side">
                <div class="search-wrap">
                    <div class="input-group">
                        <input class="form-control" id="applicationAdvancedSearch" type="text" placeholder="Application no." name="applicationAdvancedSearch" aria-label="applicationAdvancedSearch"><span class="input-group-btn">
                              <button class="btn btn-default buttonsearch" title="Search by keywords"><i class="fa fa-search"></i></button></span>
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
                        <th>Application No.</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Service</th>
                        <th>Date Submitted <span class="sort"></span></th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${applicationResult.rows}" var="list">
                        <tr>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                <p><a href="#">${list.applicationNo}</a></p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                <p>${list.applicationType}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Service</p>
                                <p>${list.svcName}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                <p>${list.status}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                <p><fmt:formatDate value="${list.createDate}" pattern="MM/dd/yyyy HH:mm:ss" /></p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title" for="selectApplication1">Actions</p>
                                <select class="table-select" id="selectApplication1" aria-label="selectApplication1">
                                    <option>Select</option>
                                    <option>Option one</option>
                                    <option>Option two</option>
                                    <option>Option three</option>
                                    <option>Option four</option>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>