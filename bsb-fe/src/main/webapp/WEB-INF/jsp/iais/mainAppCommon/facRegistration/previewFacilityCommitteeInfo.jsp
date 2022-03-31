<div>
    <table class="table">
        <thead>
        <tr>
            <th>S/N</th>
            <th>Salutation</th>
            <th>Name</th>
            <th>ID Type</th>
            <th>ID Number</th>
            <th>Nationality</th>
            <th>Contact No.</th>
            <th>Email</th>
            <th>Employment Start Date</th>
            <th>Designation</th>
            <th>Area of Expertise</th>
            <th>Role</th>
            <th>Is This Person an Employee of This Company</th>
            <th>Company Name</th>
        </tr>
        </thead>
        <%--@elvariable id="DATA_LIST" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto>"--%>
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
                <td>${personnel.expertiseArea}</td>
                <td>${personnel.role}</td>
                <td>${personnel.employee}</td>
                <td>${personnel.externalCompName}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="application-tab-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6">
            <a class="back" href="javascript:void(0)" data-step-key="${srcNodePath}"><em class="fa fa-angle-left"></em> Previous</a>
        </div>
    </div>
</div>