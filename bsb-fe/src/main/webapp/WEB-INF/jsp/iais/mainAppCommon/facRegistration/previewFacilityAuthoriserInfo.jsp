<div>
    <p style="font-weight: bold; padding-left: 8px;">Preview Personnel Authorised to Access the Facility Information</p>
</div>

<div>
    <table class="table" aria-describedby="">
        <thead>
        <tr>
            <th scope="col">S/N</th>
            <th scope="col">Salutation</th>
            <th scope="col">Name</th>
            <th scope="col">ID Type</th>
            <th scope="col">ID Number</th>
            <th scope="col">Nationality</th>
            <th scope="col">Contact No.</th>
            <th scope="col">Email</th>
            <th scope="col">Employment Start Date</th>
            <th scope="col">Designation</th>
            <th scope="col">Employment Period</th>
            <th scope="col">Work Area</th>
            <th scope="col">Security Clearance Date</th>
        </tr>
        </thead>
        <%--@elvariable id="DATA_LIST" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto>"--%>
        <c:forEach var="personnel" items="${DATA_LIST}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${personnel.salutation}</td>
                <td>${personnel.name}</td>
                <td>${personnel.idType}</td>
                <td>${personnel.idNumber}</td>
                <td>${personnel.nationality}</td>
                <td>${personnel.contactNo}</td>
                <td>${personnel.email}</td>
                <td>${personnel.employmentStartDt}</td>
                <td>${personnel.designation}</td>
                <td>${personnel.employmentPeriod}</td>
                <td>${personnel.workArea}</td>
                <td>${personnel.securityClearanceDt}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="application-tab-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6 ">
            <a class="back" href="javascript:void(0)" data-step-key="${srcNodePath}"><em class="fa fa-angle-left"></em> Previous</a>
        </div>
    </div>
</div>