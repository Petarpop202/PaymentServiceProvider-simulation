insert into agencies(merchant_id, merchant_password) values ('merchant_id-1699895821153', '$2a$12$IJGAeaOD9OloL73IYj6fE.u9n1RmkCMP4EvT.U0DydU3RJ/tWmsaO'); /*1699895821153 */
insert into agencies(merchant_id, merchant_password) values ('merchant_id-1706716847617', '$2a$10$NQX3tov9HigXxYzxBQSeJ.HeHRI.KhKA//4WLomoS3yv.TQJValWm'); /*1699895821153 */

insert into payment_methods(name) values ('CREDIT CARD'), ('QR CODE'), ('PAY PAL'), ('CRYPTO');

insert into subscriptions(id, agency_id) values ('1', '2');

insert into agency_subscriptions(subscription_id, method_id) values ('1', '1'), ('1', '2'), ('1', '3'), ('1', '4');
