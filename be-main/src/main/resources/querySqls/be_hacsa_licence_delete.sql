
DELETE FROM hcsal.dbo.lic_base_specified_correlation where BASE_LIC_ID in
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C') 
OR SPEC_LIC_ID in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C') ;

DELETE FROM hcsal.dbo.lic_app_correlation where LICENCE_ID in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C');

DELETE FROM hcsal.dbo.lic_fee_group_item where LI_FEE_GRP_ID in (select id from hcsal.dbo.lic_fee_group) 
and LICENCE_ID in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C');

DELETE FROM hcsal.dbo.lic_document WHERE DOC_ID in (select id from hcsal.dbo.document) 
and LIC_PREM_ID in (SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID 
in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.document where ID NOT IN (SELECT DOC_ID FROM hcsal.dbo.lic_document);

DELETE FROM hcsal.dbo.lic_prem_insp_grp_correlation WHERE INS_GRP_ID in (select id from hcsal.dbo.lic_inspection_group) 
and LIC_PREM_ID in (SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));	

DELETE FROM hcsal.dbo.lic_inspection_group WHERE ID NOT IN (SELECT INS_GRP_ID FROM hcsal.dbo.lic_prem_insp_grp_correlation);

DELETE FROM hcsal.dbo.lic_premises_scope_allocation 
WHERE LIC_PREM_SCOPE_ID IN 
(SELECT ID FROM hcsal.dbo.lic_premises_scope WHERE LIC_PREM_ID IN (SELECT ID FROM hcsal.dbo.lic_premises
WHERE LICENCE_ID in (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C')));

DELETE FROM hcsal.dbo.lic_premises_scope WHERE ID NOT IN (SELECT LIC_PREM_SCOPE_ID FROM hcsal.dbo.lic_premises_scope_allocation);

DELETE FROM hcsal.dbo.lic_premises_scope_allocation WHERE LIC_CGO_ID IN 
(SELECT ID FROM hcsal.dbo.lic_key_personnel WHERE LICENCE_ID IN (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));


DELETE FROM hcsal.dbo.lic_key_personnel WHERE LICENCE_ID IN (select ID FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C');

DELETE FROM hcsal.dbo.key_personnel_ext WHERE ID NOT IN 
(SELECT PSN_EXT_ID FROM hcsal.dbo.lic_key_personnel);

DELETE FROM hcsal.dbo.key_personnel WHERE ID NOT IN 
(SELECT PERSON_ID FROM hcsal.dbo.lic_key_personnel) AND ID NOT IN (SELECT PERSON_ID FROM hcsal.dbo.key_personnel_ext) ;


DELETE FROM hcsal.dbo.lic_prem_app_prem_correlation WHERE LIC_PREM_ID IN 
(SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID in 
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.lic_premises_req_for_info_doc 
WHERE REQ_INFO_ID IN (SELECT ID FROM hcsal.dbo.lic_premises_req_for_info WHERE LIC_PREM_ID IN 
(SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID in 
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C')));

DELETE FROM hcsal.dbo.lic_premises_req_for_info_reply 
WHERE REQ_INFO_ID IN (SELECT ID FROM hcsal.dbo.lic_premises_req_for_info WHERE LIC_PREM_ID IN 
(SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID in 
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C')));

DELETE FROM hcsal.dbo.lic_premises_req_for_info WHERE LIC_PREM_ID IN (SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID IN (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.lic_svc_specific_personnel WHERE LICENCE_ID IN (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C');


DELETE FROM hcsal.dbo.lic_prem_ph_open_period WHERE PREM_ID IN 
(SELECT PREM_ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID IN
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.premises_operational_unit WHERE PREMISES_ID IN 
(SELECT PREMISES_ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID IN
(select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.premises WHERE ID NOT IN 
(SELECT PREM_ID FROM hcsal.dbo.lic_prem_ph_open_period UNION SELECT PREMISES_ID FROM hcsal.dbo.lic_premises UNION SELECT PREMISES_ID FROM hcsal.dbo.premises_operational_unit) ;

DELETE FROM hcsal.dbo.lic_premises_audit WHERE LIC_PREM_ID IN (SELECT ID FROM hcsal.dbo.lic_premises WHERE LICENCE_ID IN (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C'));

DELETE FROM hcsal.dbo.lic_premises WHERE LICENCE_ID IN (select id FROM hcsal.dbo.licence where LICENCE_NO = 'AN200512000809C');

DELETE FROM hcsal.dbo.licence WHERE LICENCE_NO ='AN200512000809C';





