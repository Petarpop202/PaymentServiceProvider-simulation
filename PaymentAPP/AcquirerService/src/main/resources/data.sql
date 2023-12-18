insert into bank_accounts(account_number, balance) values ('845000000040484987', 12345), ('845000000050584987', 12445 );

insert into agencies(merchant_id, merchant_password, bank_account_id) values ('merchant_id-1699895821153', '$2a$12$IJGAeaOD9OloL73IYj6fE.u9n1RmkCMP4EvT.U0DydU3RJ/tWmsaO', 1);

insert into credit_cards(pan, card_holder_name, security_code, expiration_date, bank_account_id) values ('$2a$12$EfWoGiOnxcXRW6lfzXhJjuI9FegE4ZQtAQ1o.UMb3CXcukD0eFmD2', 'Thopok Bergrsson', '$2a$12$2l9AzLMaWiXmktQG4k4fB.f/hhQTZuRKDGR1lOMf8Tu7nGvzXFpoG', '2026-08-01 00:00:00', 2); /*5258388321604651,Thopok Bergrsson,547,2030-12-01 00:00:00*/

insert into card_prefixes(prefix) values ('511867'), ('525838'), ('535152'), ('558671');