-- One way to execute this script is to run the following command in the bin dir of postgresql:
-- psql -U pgsys -d postgres -f <path_to_script_file>


--\prompt 'Enter the password for the pgsys user: ' pgsys_pw
--\prompt 'Enter the desired name for the new database: ' db_name
\set pgsys_pw 'Super_User_Pass'
\set db_name 'local'

\set ON_ERROR_STOP on

CREATE DATABASE :db_name;

\echo Testing pgsys password
\c postgres://pgsys::pgsys_pw@:HOST/:db_name
\echo pgsys password tested OK

\set ON_ERROR_STOP off

\echo Connected to database :DBNAME
\echo as user: :USER

DROP SCHEMA public;
\echo Removed public schema
