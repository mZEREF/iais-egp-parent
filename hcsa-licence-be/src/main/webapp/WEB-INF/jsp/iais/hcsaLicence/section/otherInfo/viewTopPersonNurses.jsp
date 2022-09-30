<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name of trained nurses
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${nurses.name}">
                   <iais:code code="${nurses.name}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldNurses.name}" style="display: none">
                     <iais:code code="${oldNurses.name}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Qualifications
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${nurses.qualification}">
                   <iais:code code="${nurses.qualification}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldNurses.qualification}" style="display: none">
                     <iais:code code="${oldNurses.qualification}"></iais:code>
                </span>
            </div>
        </td>
    </tr>
</table>
