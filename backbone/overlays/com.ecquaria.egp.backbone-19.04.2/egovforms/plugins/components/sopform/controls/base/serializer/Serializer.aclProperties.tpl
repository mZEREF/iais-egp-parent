{if control.properties.acl}
<Entry name="acl">
	<Map>
		{if control.properties.acl.visibility}
		<Entry name="visibility">
			<Map>
				{if control.properties.acl.visibility.mode != undefined}
				<Entry name="mode"><String>${control.properties.acl.visibility.mode|escape}</String></Entry>
				{/if}
				{if control.properties.acl.visibility.stages != undefined && control.properties.acl.visibility.stages.length > 0}
				<Entry name="stages">
					<List>
						{for stage in control.properties.acl.visibility.stages}
						<String>${stage|escape}</String>
						{/for}
					</List>
				</Entry>
				{/if}
			</Map>
		</Entry>
		{/if}
		{if control.properties.acl.editability}
		<Entry name="editability">
			<Map>
				{if control.properties.acl.editability.mode != undefined}
				<Entry name="mode"><String>${control.properties.acl.editability.mode|escape}</String></Entry>
				{/if}
				{if control.properties.acl.editability.stages != undefined && control.properties.acl.editability.stages.length > 0}
				<Entry name="stages">
					<List>
						{for stage in control.properties.acl.editability.stages}
						<String>${stage|escape}</String>
						{/for}
					</List>
				</Entry>
				{/if}
			</Map>
		</Entry>
		{/if}
	</Map>
</Entry>
{/if}
