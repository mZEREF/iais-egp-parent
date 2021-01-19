
delete licensee_individual from organization orgn 
left join licensee lice on orgn.id = lice.ORGANIZATION_ID
left join licensee_entity lice_ent on lice.id = lice_ent.id
left join  licensee_key_appt_person lice_key_appt on lice_key_appt.LICENSEE_ID  = lice.ID 
left join licensee_individual lice_indiv on lice.id = lice_indiv.id
where orgn.ID = 'AN200512000809C'

delete licensee_key_appt_person from organization orgn 
left join licensee lice on orgn.id = lice.ORGANIZATION_ID
left join licensee_entity lice_ent on lice.id = lice_ent.id
left join  licensee_key_appt_person lice_key_appt on lice_key_appt.LICENSEE_ID  = lice.ID 
left join licensee_individual lice_indiv on lice.id = lice_indiv.id
where orgn.ID = 'AN200512000809C'


delete licensee_entity from organization orgn 
left join licensee lice on orgn.id = lice.ORGANIZATION_ID
left join licensee_entity lice_ent on lice.id = lice_ent.id
left join  licensee_key_appt_person lice_key_appt on lice_key_appt.LICENSEE_ID  = lice.ID 
left join licensee_individual lice_indiv on lice.id = lice_indiv.id
where orgn.ID = 'AN200512000809C'

delete licensee from organization orgn 
left join licensee lice on orgn.id = lice.ORGANIZATION_ID
left join licensee_entity lice_ent on lice.id = lice_ent.id
left join  licensee_key_appt_person lice_key_appt on lice_key_appt.LICENSEE_ID  = lice.ID 
left join licensee_individual lice_indiv on lice.id = lice_indiv.id
where orgn.ID = 'AN200512000809C'

delete user_role from organization orgn 
left join user_account ua on orgn.id = ua.ORGANIZATION_ID 
left join user_role ur on ua.ID = ur.USER_ACCT_ID where orgn.ID = 'AN200512000809C'

delete user_account from organization orgn 
left join user_account ua on orgn.id = ua.ORGANIZATION_ID 
left join user_role ur on ua.ID = ur.USER_ACCT_ID where orgn.ID = 'AN200512000809C'


delete organization from organization orgn 
left join user_account ua on orgn.id = ua.ORGANIZATION_ID 
left join user_role ur on ua.ID = ur.USER_ACCT_ID where orgn.ID = 'AN200512000809C'
