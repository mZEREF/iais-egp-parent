        <li>
            <a href="/hcsa-licence-web/eservice/INTERNET/MohDataSubmission">Submit Data</a>
        </li>
       <c:if test="${dataSubARTPrivilege == 1}">
        <li>
            <a href="/hcsa-licence-web/eservice/INTERNET/MohOnlineEnquiryAssistedReproduction">AR Online Enquiry</a>
        </li>
        <li>
            <a href="/hcsa-licence-web/eservice/INTERNET/MohOnlineEnquiryDonorSample">Donor Sample Enquiry</a>
        </li>
        </c:if>

        <c:if test="${dataSubLDTPrivilege == 1}">
            <li>
                <a href="/hcsa-licence-web/eservice/INTERNET/MohLabDevelopedTestsEnquiry">LDT Online Enquiry</a>
            </li>
        </c:if>

        <li>
            <a href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">Amend a Submission</a>
        </li>
        <li>
            <a href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">Edit a Draft Submission</a>
        </li>
        <li>
            <a href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">Withdraw Submission</a>
        </li>
























</li>