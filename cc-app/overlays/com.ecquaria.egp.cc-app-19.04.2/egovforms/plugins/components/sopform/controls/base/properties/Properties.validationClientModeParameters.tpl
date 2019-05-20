<div id="pred_client_parameters_${index}" style="display: none">
	<table width="100%">
		{for row in parameters}
			<tr>
				<td style="text-align: right; padding-right: 5px;">${row.description|escape}</td>
				<td>
					<select id="vald_client_param_${index}_${row_index}">
						{for row2 in operandOptions}
							<option value="${row2.code|escape}">${row2.description|escape}</option>
						{/for}
					</select>
				</td>
			</tr>
		{/for}
	</table>
</div>