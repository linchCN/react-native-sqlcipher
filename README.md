# react-native-sqlcipher
SQLCipher plugin for React Native. Based on the react-native-sqlite-storage project.

Version 0.01 is forked from [andpor/react-native-sqlite-storage](https://github.com/andpor/react-native-sqlite-storage/) #4.1.0

## Getting started

`$ npm install react-native-sqlcipher --save`

### Mostly automatic installation

With autolinking (react-native 0.60+)

    `cd ios && pod install`
    

## Usage
#### Example:

```javascript
import DB from 'react-native-sqlcipher';

errorCB(err) {
  console.log("SQL Error: " + err);
}

successCB() {
  console.log("SQL executed fine");
}

openCB() {
  console.log("Database OPENED");
}

let db = DB.openDatabase({"name": "test.db", "key": "password",createFromLocation:1}, openCB, errorCB);
db.transaction((tx) => {
  tx.executeSql('SELECT * FROM Employees a, Departments b WHERE a.department = b.department_id', [], (tx, results) => {
      console.log("Query completed");

      // Get rows with Web SQL Database spec compliance.

      var len = results.rows.length;
      for (let i = 0; i < len; i++) {
        let row = results.rows.item(i);
        console.log(`Employee name: ${row.name}, Dept Name: ${row.deptName}`);
      }

      // Alternatively, you can use the non-standard raw method.

      /*
        let rows = results.rows.raw(); // shallow copy of rows Array

        rows.map(row => console.log(`Employee name: ${row.name}, Dept Name: ${row.deptName}`));
      */
    });
});

// Manually overwrite the DB file
DB.copyDBFile({name : 'test.db',createFromLocation : 1},() => console.info('copy completed'))

```

## Documentation
See [react-native-sqlite-storage](https://github.com/andpor/react-native-sqlite-storage/blob/master/README.md)  for more details.


## References
  - [sqlcipher](https://github.com/sqlcipher/sqlcipher)
  - [android-database-sqlcipher](https://github.com/sqlcipher/android-database-sqlcipher)

