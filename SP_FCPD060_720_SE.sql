USE [rep_post_dia]
GO
/****** Object:  StoredProcedure [dbo].[SP_FCPD060_720_SE]    Script Date: 10/10/2017 11:07:05 a.m. ******/
SET ANSI_NULLS OFF
GO
SET QUOTED_IDENTIFIER OFF
GO

ALTER PROCEDURE [dbo].[SP_FCPD060_720_SE] 
	@F_ini datetime ,
	@F_fin datetime
AS
BEGIN

	SELECT DISTINCT 
		t1.sponsor_bank, 
		t1.sink_node_conversion_rate, 
		t1.sink_node_fee, 
		t1.source_node_amount_approved, 
		t1.pan, 
		t1.currency_code_tran, 
		t1.sink_node_amount_approved, 
		t1.msg_type, 
		tran_type, 
		t1.from_account, 
		t1.merchant_type, 
		t1.ret_ref_no,
		t1.auth_id_rsp, 
		inreq= isnull(t1.in_req, t1.in_adv), 
		t1.in_rev, 
		t1.card_acceptor_name_loc, 
		t1.card_acceptor_id_code,
		case t1.sink_node_req_sys_trace 
			when NULL then substring(t1.sink_node_echo_data, 29, 6)
			else rtrim(ltrim(t1.sink_node_req_sys_trace))
		end
		as sink_node_req_sys_trace, 
		t1.in_adv, 
		t1.card_acceptor_term_id 
	FROM  
		REP_POST_DIA.dbo.tm_trans_720 as t1 
	WHERE  
		t1.source_node in ( 'PosTerm' , 'PosTerm720' ) 
		and t1.sponsor_bank = '720' 
		and ( msg_type <> '544' or state = 50 )
		and t1.rsp_code_req_rsp = '00' and msg_type <> 256  and state not in (4, 55, 56, 100)
		and SubString(pan,1,6) in ('423691', '476515', '415366', '430906', '499929', '442267', '472324', '405891') 
		and t1.abort_rsp_code IS NULL 
		and isnull( t1.in_req, isnull(t1.in_rev,t1.in_adv) ) between @F_ini and @F_fin 

		-- [BEGIN - INC. Se esta reportado las transacciones originales, 
		-- cuyos reversos no pudieron ser procesados (estatus 4). - O Rojas - 05/2017
		/*and t1.rsp_code_req_rsp = '00' and t1.msg_type = 512  and t1.state = 99*/ and t1.tran_nr not in
		(
		  SELECT 
				t2.tran_nr_prev
			FROM  
				REP_POST_DIA.dbo.tm_trans_720 as t2 
			WHERE  
				t2.source_node in ( 'PosTerm' , 'PosTerm720' ) 
				and t2.sponsor_bank = '720' 
				and t2.rsp_code_req_rsp = '00' and t2.msg_type = 1056  and t2.state = 4
				and SubString(t2.pan,1,6) in ('423691', '476515', '415366', '430906', '499929', '442267', '472324',  '405891') 
				and t2.abort_rsp_code IS NULL 
				and isnull( t2.in_req, isnull(t2.in_rev,t2.in_adv) ) between @F_ini and @F_fin 
		)
		-- [END]
	ORDER BY 
		t1.card_acceptor_id_code
	
END