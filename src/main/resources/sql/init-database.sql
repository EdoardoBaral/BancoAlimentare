-- RESET DATABASE
drop table if exists BANCOALIMENTAREDB.TRANSAZIONI;
drop table if exists BANCOALIMENTAREDB.PRODOTTI;

-- CREAZIONE DELLA TABELLA PRODOTTI --
create table BANCOALIMENTAREDB.PRODOTTI
(
    NOME     varchar(50) primary key,
    GIACENZA smallint not null,
    check (GIACENZA >= 0)
);

-- CREAZIONE DELLA TABELLA TRANSAZIONI --
create table BANCOALIMENTAREDB.TRANSAZIONI
(
    ID           int primary key auto_increment,
    PRODOTTO     varchar(50) not null,
    QUANTITA     tinyint not null,
    DESTINATARIO varchar(50),
    DATA         date not null,
    TIPO         varchar(8) not null,
    foreign key (PRODOTTO) references BANCOALIMENTAREDB.PRODOTTI(NOME) on delete cascade on update cascade,
    check (QUANTITA > 0),
    check (TIPO = 'INGRESSO' or TIPO = 'USCITA')
);

-- GESTIONE PERMESSI UTENZA BADB_OWN
grant select, insert, delete, update on BANCOALIMENTAREDB.PRODOTTI to BADB_OWN;
grant select, insert, delete, update on BANCOALIMENTAREDB.TRANSAZIONI to BADB_OWN;