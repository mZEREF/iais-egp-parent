{eval}
	calculationManager.doSetup({ id: id });
{/eval}
<div class="property-container">
	<div class="content">
		<div>
			<label class="page-edit-label">Field</label>
			<select class="calc_field_a">			
				{for row in operandOptions}
					<option value="${row.code}">${row.description}</option>
				{/for}			
			</select>
		</div>
		<div class="page-content-spacer"></div>
		<div>
			<label class="page-edit-label">Operation</label>
			<select class="calc_field_operation">
				<option value="-1"> - Please Select - </option>
				<option value="+"> + (Add)</option>
				<option value="-"> - (Minus)</option>
				<option value="*"> * (Multiply)</option>
				<option value="/"> / (Divide)</option>
				<option value="%"> % (Modulus)</option>
			</select>
		</div>
		<div class="page-content-spacer"></div>
		<div>
			<label class="page-edit-label">Field</label>
			<select class="calc_field_b">	
				{for row in operandOptions}
					<option value="${row.code}">${row.description}</option>
				{/for}						
			</select>
		</div>
		<div class="page-content-spacer"></div>
	</div>
</div>