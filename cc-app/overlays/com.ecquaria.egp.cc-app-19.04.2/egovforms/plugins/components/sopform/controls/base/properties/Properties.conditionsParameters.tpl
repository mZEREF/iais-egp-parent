<div id="condition_parameters_${type}" style="display: none">
	<table>
		{for row in parameters}
			<tr>
				<td>${row.description}</td>
				<td>
					<select id="cond_param_${type}_${row_index}">
						{for row2 in operandOptions}
							<option value="${row2.code|escape}">${row2.description|escape}</option>
						{/for}
					</select>
				</td>
			</tr>
		{/for}
	</table>
</div>