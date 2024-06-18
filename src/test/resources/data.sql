insert into tb_shop_cart
    (id, login)
values
    ('dabb5525-d775-4347-be1f-876dea84ced3', 'anderson.wagner'),
    ('52d54140-9763-4208-939f-d4dae1429205', 'kaiby.santos'),
    ('f158f211-1900-4201-abb4-87ba2369acbb', 'janaina.alvares'),
    ('d3d62da5-8828-4423-8df0-30abf3aabec9', 'usuario.comum');

insert into tb_shop_cart_item
    (id, id_shop_cart, quantidade)
values
    ('93b7e9d1-3632-4cae-8e66-4a22505e7470', '52d54140-9763-4208-939f-d4dae1429205', 15),
    ('21451bd1-d1f6-4b0e-95a6-fc1bce955121', 'f158f211-1900-4201-abb4-87ba2369acbb', 15),
    ('1e33307b-4c47-4407-8ebc-c60ef45d7f76', 'd3d62da5-8828-4423-8df0-30abf3aabec9', 15);