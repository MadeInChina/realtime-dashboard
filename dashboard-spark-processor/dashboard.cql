//Create keyspace
CREATE KEYSPACE IF NOT EXISTS dashboard WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

//Create table
CREATE TABLE dashboard.new_user_counts
  (total_count counter,
  created_time text,
  PRIMARY KEY (created_time)
);

CREATE TABLE dashboard.deposit_amount
  (user_id text,
  amounts text,
  created_time text,
  PRIMARY KEY (created_time)
);

CREATE TABLE dashboard.deposit_amount
  (id UUID , product INT, amount DECIMAL,
   PRIMARY KEY id
);

//Select from table
SELECT * FROM dashboard.new_user_counts;

//Truncate table
TRUNCATE TABLE dashboard.new_user_counts;