/*
Exercício 2 - Você foi designado para criar um fluxo de processo para um sistema de apostas. Sua
tarefa é desenvolver uma procedure em PL/SQL que:
2.1. Receba as seguintes informações de entrada:
- Nome do apostador (nome)
- Idade do apostador (idade)
- E-mail do apostador (email)
- Valor da aposta (valor_aposta)
2.2. Realize as seguintes validações e ações:
- Verifique se o email do apostador já existe na tabela de apostadores e caso não exista, siga com
os próximos passos de validação e insert.
- Caso o email não exista na base, verifique se a idade do apostador é maior de 18 anos. Se não
for, a procedure deve retornar uma mensagem de erro apropriada e não prosseguir com o processo.
- Caso o email do apostador não exista na base e o mesmo seja maior de idade, insira suas
informações em uma tabela de apostadores e gere um ID sequencial para ele. Para isso, você deve
usar uma sequência existente no banco de dados para garantir a unicidade do ID.
- Após o cadastro do apostador, faça um insert na tabela apostas_temp criando um id de aposta
sequencial para o campo aposta_id, id gerado para o apostador para o campo usuario_id, valor da
aposta para o campo valor e data_aposta com data e hora do momento do insert.
- Por último, faça a chamada da procedure existente processa_aposta.sql para que as apostas
sejam processadas.
*/

create or replace procedure p_registrar_fluxo_aposta (
    p_nome         in apostadores.nome%type,
    p_idade        in number,
    p_email        in apostadores.email%type,
    p_valor_aposta in apostas_temp.valor%type
) is

    v_usuario_id      number;
    v_aposta_id       number;
    v_numero_aposta     number;
    v_numero_apostador  number;
    v_existe_email    number;

begin

    select count(*)
    into v_existe_email
    from apostadores
    where email = p_email;

    if v_existe_email = 0 then

        if p_idade < 18 then
            raise_application_error(-20001, 'Erro: O apostador deve ser maior de 18 anos.');
        end if;
        
        select nvl(max(id), 0)+1 into v_numero_apostador from apostadores;

        insert into apostadores (id, nome, idade, email)
        values (v_numero_apostador, p_nome, p_idade, p_email)
        returning id into v_usuario_id;
        
        select nvl(max(aposta_id), 0)+1 into v_numero_aposta from apostas_temp;
        
        insert into apostas_temp (aposta_id, usuario_id, valor, data_aposta)
        values (v_numero_aposta, v_usuario_id, p_valor_aposta, sysdate)
        returning aposta_id into v_aposta_id;

        processa_apostas; 

        commit;

    end if;

exception
    when others then
        rollback; 
        raise_application_error(-20002, 'Erro no processamento: ' || sqlerrm);

end p_registrar_fluxo_aposta;