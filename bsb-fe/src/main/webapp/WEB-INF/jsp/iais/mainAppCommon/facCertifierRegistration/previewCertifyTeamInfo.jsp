<div class="row" style="overflow-y: scroll">
    <table class="table" aria-labelledby="">
        <thead>
        <tr>
            <th id="no">S/N</th>
            <th id="salutation">Salutation</th>
            <th id="name">Name</th>
            <th id="idType">ID Type</th>
            <th id="idNo">ID Number</th>
            <th id="birthDate">Date of Birth</th>
            <th id="nationality">Nationality</th>
            <th id="contactNo">Contact No.</th>
            <th id="jobDesignation">Job Designation</th>
            <th id="leadCertifier">Lead Certifier</th>
            <th id="role">Role</th>
            <th id="certExp">Experience in certification of BSL-3/4 facility</th>
            <th id="commissionExp">Experience in commissioning of BSL-3/4 facility</th>
            <th id="otherExp">Experience in other BSL-3/4 related activities</th>
            <th id="highestLevelEdu">Highest level of education completed</th>
            <th id="professionalRegistration">Relevant professional registration and certificates</th>
            <th id="relatedAchievement">Other related achievements/activities</th>
        </tr>
        </thead>
        <%--@elvariable id="DATA_LIST" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto>"--%>
        <c:forEach var="teamMember" items="${DATA_LIST}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${teamMember.salutation}</td>
                <td>${teamMember.name}</td>
                <td>${teamMember.idType}</td>
                <td>${teamMember.idNumber}</td>
                <td>${teamMember.birthDate}</td>
                <td>${teamMember.nationality}</td>
                <td>${teamMember.contactNo}</td>
                <td>${teamMember.jobDesignation}</td>
                <td>${teamMember.leadCertifier}</td>
                <td>${teamMember.role}</td>
                <td>${teamMember.certBSL3Exp}</td>
                <td>${teamMember.commBSL34Exp}</td>
                <td>${teamMember.otherBSL34Exp}</td>
                <td>${teamMember.highestEdu}</td>
                <td>${teamMember.proCertifications}</td>
                <td>${teamMember.otherRelatedAchievement}</td>
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