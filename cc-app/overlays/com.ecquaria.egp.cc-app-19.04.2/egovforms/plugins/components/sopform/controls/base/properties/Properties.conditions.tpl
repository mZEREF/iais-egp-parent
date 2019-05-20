{eval}
	conditionManager.doSetup({ id: id });
{/eval}
<div id="conditionalPanel" class="condition-panel">
	{if showVisibility && showEditability}
		<ul>
			<li><a href="#visibilityPanel_0"><span>Visibility</span></a></li>
			<li><a href="#editabilityPanel_1"><span>Editability</span></a></li>
		</ul>
	{/if}
	{if showVisibility}
	<div id="visibilityPanel_0">
		<div class="content">
		<div>
			<label class="page-edit-label">Option</label>
			<select id="cond_select_0" class="property-input">
				{for row in condVisSelectOptions}
					<option value="${row.code|escape}">${row.description|escape}</option>
				{/for}
			</select>
		</div>
		<div class="page-content-spacer"></div>
		<div class="cond_predefined_option" style="display: none;">
			<label class="page-edit-label">Predefined</label>
			<input type="checkbox" value="predefined" id="cond_type_radio_0" />
		</div>
		<!--<div class="page-content-spacer"></div>	
		<div id="condition_parameters_0" style="display: none">
			<label class="page-edit-label"></label>
			<div></div>
		</div>-->
		<div class="page-content-spacer"></div>
		<div class="cond_predefined" style="display: none;">
			<label class="page-edit-label">Predefined Condition</label>
			<select id="select_predefined_0"  class="property-input">
				{for row in condPredefinedtype}
					<option value="${row.code}">${row.description}</option>
				{/for}
			</select>
			<div id="condition_parameters_0" style="display: none">

			</div>
		</div>
		<div class="page-content-spacer"></div>
		<div class="cond_manual" style="display: none;">
			<label class="page-edit-label">Subject Field</label>
			<select id="select_firstField_0"  class="property-input">
				{for row in operandOptions}
					<option value="${row.code}">${row.description}</option>
				{/for}
			</select>
		</div>
		<div class="page-content-spacer"></div>	
		<div class="cond_manual" style="display: none;">
			<label class="page-edit-label"></label>
			<select id="select_operator_0" class="property-input">
				{for row in operatorOptions}
					<option value="${row.code}">${row.description}</option>
				{/for}
			</select>
		</div>
		<div class="page-content-spacer"></div>	
		<div class="cond_manual" style="display: none;">
			<label class="page-edit-label">Compare with field</label>
			<input type="checkbox" name="cond_compare_field_0"  class="property-input"/>
		</div>
		<div class="page-content-spacer"></div>	
		<div class="row_secondField" style="display: none;">
			<label class="page-edit-label">Subject Field</label>
			<select id="select_secondField_0"  class="property-input">
				{for row in operandOptions}
					<option value="${row.code}">${row.description}</option>
				{/for}
			</select>
		</div>
		<div class="page-content-spacer"></div>
		<div class="row_text" style="display: none;">
			<label class="page-edit-label">Value</label>
			<input type="text" size="10" id="single_text_0"  class="property-input"/>
		</div>
		</div>
	</div>
	{/if}
	{if showEditability}
	<div id="editabilityPanel_1">
		<div class="content">
			<div>
				<label class="page-edit-label">Option</label>
				<select id="cond_select_1" class="property-input">
						{for row in condEdiSelectOptions}
							<option value="${row.code|escape}">${row.description|escape}</option>
						{/for}
					</select>
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="cond_predefined_option" style="display: none;">
				<label class="page-edit-label">Predefined</label>
				<input type="checkbox" value="predefined" id="cond_type_radio_1"  class="property-input"/>
			</div>
			<div class="page-content-spacer"></div>
			
			<!--<div style="display: none;">
				<label class="page-edit-label"></label>
				<div id="condition_parameters_1" style="display: none" class="property-input">
				</div>
			</div>-->
			<div class="page-content-spacer"></div>
			
			<div class="cond_predefined" style="display: none;">
				<label class="page-edit-label">Predefined Condition</label>
				<select id="select_predefined_1">
					{for row in condPredefinedtype}
						<option value="${row.code}">${row.description}</option>
					{/for}
				</select>
				<div id="condition_parameters_1" style="display: none">

				</div>
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="cond_manual" style="display: none;">
				<label class="page-edit-label">Subject Field</label>
				<select id="select_firstField_1" class="property-input">
						{for row in operandOptions}
							<option value="${row.code}">${row.description}</option>
						{/for}
				</select>
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="cond_manual" style="display: none;">
				<label class="page-edit-label"></label>
				<select id="select_operator_1" class="property-input">
					{for row in operatorOptions}
						<option value="${row.code}">${row.description}</option>
					{/for}
				</select>
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="cond_manual" style="display: none;">
				<label class="page-edit-label">Compare with field</label>
				<input type="checkbox" name="cond_compare_field_1" class="property-input" />
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="row_secondField" style="display: none;">
				<label class="page-edit-label">Subject Field</label>
				<select id="select_secondField_1" class="property-input">
					{for row in operandOptions}
						<option value="${row.code}">${row.description}</option>
					{/for}
				</select>
			</div>
			<div class="page-content-spacer"></div>
			
			<div class="row_text" style="display: none;">
				<label class="page-edit-label">Value</label>
				<input type="text" size="10" id="single_text_1" class="property-input" />
			</div>
			
		</div>
	</div><!-- editability panel ends -->
	{/if}
</div>