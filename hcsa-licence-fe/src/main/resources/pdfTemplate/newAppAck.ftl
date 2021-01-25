<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
        .ack-font-16{
            font-size: 16px;
        }
        .ack-font-20{
            font-size: 20px;
        }
    </style>
</head>
<body>
<div class="navigation-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="dashboard-page-title">
                <h1>New Licence Application</h1>
                <br/><br/><h3 id="newSvc">
                You are applying for ${serviceNameTitle}
            </h3>
            </div>
        </div>
    </div>
</div>

<div class="main-content">
    <div class="container">

        <div class="col-xs-12">
            <br/>
            <p class="ack-font-20"><strong>Submission successful</strong></p>
        </div>

        ${serviceName}
        <div class="ack-font-16">
            <div class="col-xs-12">
                A confirmation email will be sent to ${emailAddress}.
                <br/>
                <br/>
            </div>
            <div class="col-xs-12">
                ${NEW_ACK005}
                <br/>
                <br/>
            </div>
            <div class="col-xs-12">
                Transactional details:
            </div>
            <div class="col-xs-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Application No.</th>
                        ${txnRefNoColumn!""}
                        <th>${dateColumn}</th>
                        <th>Amount Deducted</th>
                        <th>Payment Method</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${appGrpNo}</td>
                        ${txnRefNo!""}
                        <td>${txnDt}</td>
                        <td>${amountStr}</td>
                        <td>${paymentMethod}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

</body>
</html>















