# react-native-sqlcipher
SQLCipher plugin for React Native. Based on the react-native-sqlite-storage project.

Version 0.0.1 is forked from [andpor/react-native-sqlite-storage](https://github.com/andpor/react-native-sqlite-storage/) #4.1.0, Sqlcipher#4.3.0

## Getting started

`$ npm install react-native-sqlcipher --save`

### Mostly automatic installation

With autolinking (react-native 0.60+)

`$ cd ios && pod install`
    
    
## Setting up your project to import a pre-populated Sqlcipher database from application for iOS

#### Step 1 - Create 'www' folder.

Create a folder called 'www' (yes must be called precisely that else things won't work) in the project folder via Finder

#### Step 2 - Create the database file

Copy/paste your pre-populated database file into the 'www' folder. Give it the same name you are going to use in openDatabase call in your application

#### Step 3 - Add file to project

in XCode, right click on the main folder and select Add Files to 'your project name'

![alt tag](https://raw.github.com/andpor/react-native-sqlite-storage/master/instructions/addFilesToProject.png)

#### Step 4 - Choose files to add

In the Add Files dialog, navigate to the 'www' directory you created in Step 1, select it, make sure you check the option to Create Folder Reference

![alt tag](https://raw.github.com/andpor/react-native-sqlite-storage/master/instructions/addFilesToProjectSelect.png)

#### Step 5 - Verify project structure

Ensure your project structure after previous steps are executed looks like this

![alt tag](https://raw.github.com/andpor/react-native-sqlite-storage/master/instructions/projectStructureAfter.png)

### Step 6 - Adjust openDatabase call

Modify you openDatabase call in your application adding createFromLocation param. If you named your database file in step 2 'testDB' the openDatabase call should look like something like this:
```js

  ...
  import DB from 'react-native-sqlcipher';

  1.DB.openDatabase({name : "testDB", createFromLocation : 1}, okCallback,errorCallback);
  // default - if your folder is called www and data file is named the same as the dbName - testDB in this example
  2.DB.openDatabase({name : "testDB", createFromLocation : "~data/mydbfile.sqlite"}, okCallback,errorCallback);
  // if your folder is called data rather than www or your filename does not match the name of the db
  3.DB.openDatabase({name : "testDB", createFromLocation : "/data/mydbfile.sqlite"}, okCallback,errorCallback);
  // if your folder is not in app bundle but in app sandbox i.e. downloaded from some remote location.
  ...

```
For Android, the www directory is always relative to the assets directory for the app: src/main/assets

Enjoy!

## Opening a database

Opening a database is slightly different between iOS and Android. Where as on Android the location of the database file is fixed, there are three choices of where the database file can be located on iOS. The 'location' parameter you provide to openDatabase call indicated where you would like the file to be created. This parameter is neglected on Android.

WARNING: the default location on iOS has changed in version 3.0.0 - it is now a no-sync location as mandated by Apple so the release is backward incompatible.


To open a database in default no-sync location (affects iOS *only*)::

```js
DB.openDatabase({name: 'my.db', location: 'default'}, successcb, errorcb);
```

To specify a different location (affects iOS *only*):

```js
DB.openDatabase({name: 'my.db', location: 'Library'}, successcb, errorcb);
```

where the `location` option may be set to one of the following choices:
- `default`: `Library/LocalDatabase` subdirectory - *NOT* visible to iTunes and *NOT* backed up by iCloud
- `Library`: `Library` subdirectory - backed up by iCloud, *NOT* visible to iTunes
- `Documents`: `Documents` subdirectory - visible to iTunes and backed up by iCloud

The original webSql style openDatabase still works and the location will implicitly default to 'default' option:

```js
DB.openDatabase("myDatabase.db", "1.0", "Demo", -1);
```


## Importing a pre-populated database.

You can import an existing - prepopulated database file into your application. Depending on your instructions in openDatabase call, the sqlite-storage will look at different places to locate you pre-populated database file.


Use this flavor of openDatabase call, if your folder is called www and data file is named the same as the dbName - testDB in this example

```js
DB.openDatabase({name : "testDB", createFromLocation : 1}, okCallback,errorCallback);
```

Use this flavor of openDatabase call if your folder is called data rather than www or your filename does not match the name of the db. In this case db is named testDB but the file is mydbfile.sqlite which is located in a data subdirectory of www

```js
DB.openDatabase({name : "testDB", createFromLocation : "~data/mydbfile.sqlite"}, okCallback,errorCallback);
```

Use this flavor of openDatabase call if your folder is not in application bundle but in app sandbox i.e. downloaded from some remote location. In this case the source file is located in data subdirectory of Documents location (iOS) or FilesDir (Android).

```js
DB.openDatabase({name : "testDB", createFromLocation : "/data/mydbfile.sqlite"}, okCallback,errorCallback);
```

## Additional options for pre-populated database file

You can provide additional instructions to sqlite-storage to tell it how to handle your pre-populated database file. By default, the source file is copied over to the internal location which works in most cases but sometimes this is not really an option particularly when the source db file is large. In such situations you can tell sqlite-storage you do not want to copy the file but rather use it in read-only fashion via direct access. You accomplish this by providing an additional optional readOnly parameter to openDatabase call

```js
DB.openDatabase({name : "testDB", readOnly: true, createFromLocation : "/data/mydbfile.sqlite"}, okCallback,errorCallback);
```

Note that in this case, the source db file will be open in read-only mode and no updates will be allowed. You cannot delete a database that was open with readOnly option. For Android, the read only option works with pre-populated db files located in FilesDir directory because all other assets are never physically located on the file system but rather read directly from the app bundle.

## Attaching another database

Sqlite3 offers the capability to attach another database to an existing database-instance, i.e. for making cross database JOINs available.
This feature allows to SELECT and JOIN tables over multiple databases with only one statement and only one database connection.
To archieve this, you need to open both databases and to call the attach()-method of the destination (or master) -database to the other ones.

```js
let dbMaster, dbSecond;

dbSecond = DB.openDatabase({name: 'second'},
  (db) => {
    dbMaster = DB.openDatabase({name: 'master'},
      (db) => {
        dbMaster.attach( "second", "second", () => console.log("Database attached successfully"), () => console.log("ERROR"))
      },
      (err) => console.log("Error on opening database 'master'", err)
    );
  },
  (err) => console.log("Error on opening database 'second'", err)
);
```

The first argument of attach() is the name of the database, which is used in DB.openDatabase(). The second argument is the alias, that is used to query on tables of the attached database.

The following statement would select data from the master database and include the "second"-database within a simple SELECT/JOIN-statement:

```sql
SELECT * FROM user INNER JOIN second.subscriptions s ON s.user_id = user.id
```

To detach a database, just use the detach()-method:

```js
dbMaster.detach( 'second', successCallback, errorCallback );
```

To manually overwrite a database
```javascript
DB.copyDBFile({name : 'test.db',createFromLocation : 1},() => console.info('copy completed'))

```

## Documentation
See [react-native-sqlite-storage](https://github.com/andpor/react-native-sqlite-storage/blob/master/README.md)  for more details.


## References
  - [sqlcipher](https://github.com/sqlcipher/sqlcipher)
  - [android-database-sqlcipher](https://github.com/sqlcipher/android-database-sqlcipher)

