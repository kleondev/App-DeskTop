USE [rep_post_dia]
GO
/****** Object:  StoredProcedure [dbo].[SP_FCPD060_REVERSOS]    Script Date: 1/8/2017 4:20:48 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER procedure [dbo].[SP_FCPD060_REVERSOS] @org int, @fechaInicio datetime, @fechaFin datetime
as
set concat_null_yields_null off 

/*
declare @Org int,
        @fechaInicio datetime,
		@fechaFin datetime


set @org = 720
set @fechaInicio = '2017-05-30'
set @fechaFin = '2017-05-31'
*/

select X  from (

select X = 'TRANRED                                                                                                                                          ' + convert( varchar,  getdate() , 103), Ind = -600 union all
select X = '                                                                                                                                                                     ', Ind = -500.9 union all
select X = 'LEYENDA ESTADO:                                                                                                                                                      ', Ind = -500.8 union all
select X = '99	COMPLETADO                                                                                                                                                       ', Ind = -500.7 union all
select X = '4	NO COMPLETADO                                                                                                                                                    ', Ind = -500.6 union all
Select X = '100	TRANSACCION ABORTADA                                                                                                                                             ', Ind = -500.6 union all
select X = '                                                                                                                                                                     ', Ind = -500.5 union all
select X = '                                                               REPORTE REVERSOS MAESTRO                                                                              ', Ind = -500.4 union all
select X = '                                                                                                                                                                     ', Ind = -500.3 union all
select X = 'AFILIADO   TERMINAL  NOMBRE_COMERCIO                            NRO_TARJETA       MONTO_TRANS  AUTORIZACION  ESTADO  COD_RESP  FECHA_REVERSO      FECHA_TRANS_ORIG   ', Ind = -500.2 union all
select X = '                                                                                                                                                           ', Ind = -150.3  union all
select X = substring(a.card_acceptor_id_code,7,9) + '  ' + a.card_acceptor_term_id + '  ' 
+ cast( isnull( a.card_acceptor_name_loc, space(40)) as char( 37 ) ) 
+ '  ' + 
--RIGHT('                    ' + Ltrim(Rtrim(substring(a.pan,1,6) + replicate('*',len(a.pan)-10) + substring(a.pan,len(a.pan)-3,len(a.pan)))),20) 
RIGHT('                    ' + Ltrim(Rtrim(a.pan)),20) 
+ '  ' + 
right('           ' + Ltrim(Rtrim(cast(cast(a.source_node_amount_requested/100 as decimal(20,2)) as char(11)))),11)
+ '  ' + right('            ' + Ltrim(Rtrim(cast(isnull(a.auth_id_rsp,space(12)) as char(12)))),12)
 + '  ' + right('      ' + CAST(a.state AS CHAR(3)),6) + '  ' + right('        ' + cast(a.rsp_code_req_rsp as char(3)),8)
 + '  ' + convert( varchar, a.in_rev, 3) + ' ' + convert( varchar, a.in_rev, 108)
+ '  ' + convert( varchar, b.in_req, 3) + ' ' + convert( varchar, b.in_req, 108)
, Ind = -490+cast( a.in_rev - getdate()   as float )/100
--from postilion.dbo.tm_trans A inner join postilion.dbo.tm_trans b on a.tran_nr_prev = b.tran_nr
from rep_post_dia.dbo.tm_trans A inner join rep_post_dia.dbo.tm_trans b on a.tran_nr_prev = b.tran_nr
where a.msg_type = '1056'
--and a.state not in ('100','99')
and a.sink_node in ('skmds','skmds0128B','skmds720','skmds7208B')
--and a.rsp_code_req_rsp not in ('96')
--and dateadd (day , datediff ( day ,0 , in_rev), 0 ) = dateadd (day , datediff ( day ,0 , getdate()-1), 0 )
and a.in_rev between @fechaInicio and @fechaFin
and substring(a.card_acceptor_id_code,7,3) = @Org
and ((a.state = '99' and a.rsp_code_req_rsp <> '00') or a.state <> '99')
) as YY order by Ind



