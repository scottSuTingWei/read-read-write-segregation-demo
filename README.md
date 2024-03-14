# Spring Bootで、DBのPrimary/ReplicationインスタンスにSQLを振り分ける3つの方法
- DynamicDataSoruce + @transactional(readOnly=true)
- DynamicDataSoruce + Custom AOP
- Multiple EntityManagers


## DB環境の作成
###  MySQLコンテナを起動する
```
docker run \                                  
  --name mysql \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -v <foler path to be volumn>:/var/lib/mysql \
  -p 3306:3306 \
  -d \
  mysql
```

### DBを作る

```
-- ユーザー作成
CREATE USER 'user1'@'%' IDENTIFIED BY 'pw@1234';
GRANT ALL PRIVILEGES ON *.* TO 'user1'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- DB作成
CREATE DATABASE IF NOT EXISTS primary_db;
CREATE DATABASE IF NOT EXISTS replica_db;

-- テーブル作成(primary db)
use primary_db;
CREATE TABLE IF NOT EXISTS users ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE );

-- テーブル作成(replica db)
use replica_db;
CREATE TABLE IF NOT EXISTS users ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE );
```


## 実装ケース別使用方法と注意事項

### ケース1. DynamicDataSoruce + @transactional(readOnly=true)
1. 下記のファイルのアノテーションをコメントアウトする。
    - **DataSourceConfig.java:**
       - @Configuration
       - @EnableTransactionManagement
    - **DataSourceConfigForDynamicDataSourceAndCustomAOP.java:**
       - @Configuration
2. 下記のファイルのアノテーションをアンコメントする。
    - **DataSourceConfigForDynamicDataSourceAndTransactional.java:**
       - @Configuration
       
3. postmanでリクエストします。
    ```
    http://localhost:8080/separateByTransactional/readFromReplica
    http://localhost:8080/separateByTransactional/updateToPrimary
    ```
---
### ケース2. DynamicDataSoruce + Custom AOP
1. 下記のファイルのアノテーションをコメントアウトする。
   - **DataSourceConfig.java:**
       - @Configuration
       - @EnableTransactionManagement
    - **DataSourceConfigForDynamicDataSourceAndTransactional.java:**
       - @Configuration
2. 下記のファイルのアノテーションをアンコメントする。
    - **DataSourceConfigForDynamicDataSourceAndCustomAOP.java:**
       - @Configuration
       
3. postmanでリクエストします。
    ```
    http://localhost:8080/separateByAOP/readFromReplica
    http://localhost:8080/separateByAOP/updateToPrimary
    ```
---
### ケース3. Multiple EntityManagers

1. 下記のファイルのアノテーションをコメントアウトする。
    - **DataSourceConfigForDynamicDataSourceAndCustomAOP.java:**
       - @Configuration
    - **DataSourceConfigForDynamicDataSourceAndTransactional.java:**
       - @Configuration
2. 下記のファイルのアノテーションをアンコメントする。

    - **DataSourceConfig.java:**
       - @Configuration
       - @EnableTransactionManagement   
3. postmanでリクエストします。
    ```
    http://localhost:8080/separateByEntityManager/readFromReplica
    http://localhost:8080/separateByEntityManager/updateToPrimary
