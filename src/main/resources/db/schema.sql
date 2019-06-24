create table users
(
  id integer not null,
  email varchar(255) not null,
  password varchar(255) not null,
  wallet_address varchar(255) not null,
  private_key varchar(255) not null,
  public_key varchar(255) not null,
  primary key(id)
);