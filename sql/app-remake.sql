alter table en_aluguel
	add status boolean default true not null;

alter table en_cliente
	add status boolean default true not null;

alter table en_filme
	add status boolean default true not null;