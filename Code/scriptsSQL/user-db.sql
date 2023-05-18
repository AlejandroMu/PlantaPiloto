DROP DATABASE IF EXISTS assets;
drop user if exists :user;
create user :user password :'password';
create database assets owner :user;
grant connect on database assets to :user;