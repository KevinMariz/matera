/*Exercício 1 - Você recebeu uma procedure que realiza a inserção de apostas no banco de dados. O
objetivo deste teste é analisar o código existente, identificar possíveis problemas de performance e
propor melhorias.
*/

create or replace procedure processa_apostas is

	cursor c_apostas is 
	select aposta_id, usuario_id, valor, data_aposta 
	  from apostas_temp
     where valor > 0 
	   and data_aposta is not null; 

    type aposta_tab is table of c_apostas%rowtype;
    v_apostas aposta_tab;
    
    v_error_message varchar2(4000);
    
    v_bulk_errors exception;
    pragma exception_init(v_bulk_errors, -24381);

BEGIN
    dbms_output.put_line('Início do processamento das apostas.');

	open c_apostas;

	loop

		fetch c_apostas
		bulk collect into v_apostas
		limit 1000;

		exit when v_apostas.count = 0;

			begin
				forall i in v_apostas.first .. v_apostas.last save exceptions   
					insert into apostas_final 
					values v_apostas(i);
			exception
				when v_bulk_errors then
					dbms_output.put_line('Algumas linhas falharam. Total de erros: ' || sql%bulk_exceptions.count);
			end;

		commit;
		dbms_output.put_line('Processamento concluído com sucesso.');
	end loop;
	close c_apostas;

exception 
    when others then
        rollback;
        v_error_message := sqlerrm;
        dbms_output.put_line('Erro crítico: ' || v_error_message);

end processa_apostas;