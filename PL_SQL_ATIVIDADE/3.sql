/*
Exercício 3 - Dada a procedure atualizar_valores_aposta, que calcula o valor de uma aposta
com base em duas variáveis principais: o tempo de uma partida e os pontos feitos pelo time. Sua
tarefa é identificar e corrigir qualquer bug na lógica de cálculo da procedure.
*/
create or replace procedure atualizar_valores_aposta is 

    v_fator_tempo  number := 1.5; 
    v_fator_pontos number := 2.0; 
    v_novo_valor   apostas.valor_aposta%type;

    cursor cur_apostas is 
        select apostas.id, 
               apostas.valor_aposta, 
               times.pontos,
               (partidas.data_hora - systimestamp) as tempo_restante
        from apostas
        join partidas
		  on apostas.id_partida = partidas.id
        join times 
		  on apostas.id_time = times.id;

begin 
    for aposta in cur_apostas loop 
        if aposta.tempo_restante < interval '1' hour and aposta.tempo_restante > interval '0' second then 
            
            v_novo_valor := aposta.valor_aposta * v_fator_tempo * (1 + (aposta.pontos * v_fator_pontos / 100));

            update apostas 
               set valor_aposta = v_novo_valor 
             where id = aposta.id;
        end if; 
    end loop; 

    commit; 

exception 
    when others then 
        rollback; 
        dbms_output.put_line('Erro ao atualizar apostas: ' || sqlerrm);
        raise; 
end;

