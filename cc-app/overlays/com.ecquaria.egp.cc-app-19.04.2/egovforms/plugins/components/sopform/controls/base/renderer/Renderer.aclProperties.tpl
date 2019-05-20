<div class="condition-panel">
	{if showVisibility && showEditability}
	<ul class="properties-tab">
		<li>
			<a href="#tab-properties-acl-visibility"><span>Visibility</span></a>
		</li>
		<li>
			<a href="#tab-properties-acl-editability"><span>Editability</span></a>
		</li>
	</ul>
	{/if}
	{if showVisibility}
	<div id="tab-properties-acl-visibility">
		<div id="properties-group-acl-visibility">
			<div class="content">
			<div>
				<label class="page-edit-label">Mode</label>
				<span class="properties-placeholder">acl-visibility-mode</span>
			</div>
			<div class="page-content-spacer"></div>
			<div id="acl-visibility-property-stages">
				<label class="page-edit-label">Stages</label>
				<span class="properties-placeholder">acl-visibility-stages</span>
			</div>
			</div>
		</div>
	</div>
	
	{/if}
	{if showEditability}
	<div id="tab-properties-acl-editability">
		<div id="properties-group-acl-editability">
			<div class="content">
			<div>
				<label class="page-edit-label">Mode</label>
				<span class="properties-placeholder">acl-editability-mode</span>
			</div>
			<div class="page-content-spacer"></div>
			<div id="acl-editability-property-stages">
				<label class="page-edit-label">Stages</label>
				<span class="properties-placeholder">acl-editability-stages</span>
			</div>
			</div>
		</div>
	</div>
	{/if}
</div>