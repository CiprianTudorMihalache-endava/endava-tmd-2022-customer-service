--\prompt 'Enter the password for the pgsys user: ' pgsys_pw
--\prompt 'Enter the name of the database: ' db_name
\set pgsys_pw 'Super_User_Pass'
\set db_name 'local'


\set owner_name 'customer_service'
\set owner_pw 'pass_o'
\set user_name 'customer_service_user'
\set user_pw 'pass_u'

\set ON_ERROR_STOP on

\echo Testing passwords
\c postgres://pgsys::pgsys_pw@:HOST/:db_name
\echo Passwords tested OK

\set ON_ERROR_STOP off

\echo Connected to database :DBNAME
\echo as user: :USER

CREATE ROLE :owner_name WITH PASSWORD :'owner_pw' LOGIN;
CREATE ROLE :user_name WITH PASSWORD :'user_pw' LOGIN;

ALTER ROLE :owner_name SET search_path TO "$user";
ALTER ROLE :user_name SET search_path TO :owner_name;

GRANT CREATE ON DATABASE :db_name TO :owner_name;

\echo Connecting as :owner_name
\c postgres://:owner_name::owner_pw@:HOST/:db_name
\echo Connected to database :DBNAME
\echo as user: :USER

CREATE SCHEMA AUTHORIZATION :owner_name;

GRANT USAGE ON SCHEMA :owner_name TO :user_name;

\echo Created Roles and permissions
