<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name of trained nurses
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${nurses.name}">
                   <c:out value="${nurses.name}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldNurses.name}" style="display: none">
                     <c:out value="${oldNurses.name}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Qualifications
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${nurses.qualification}">
                   <c:out value="${nurses.qualification}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldNurses.qualification}" style="display: none">
                     <c:out value="${oldNurses.qualification}"/>
                </span>
            </div>
        </td>
    </tr>
</table>
