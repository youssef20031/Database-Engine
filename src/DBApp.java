
/** * @author Wael Abouelsaadat */ 

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;


public class DBApp {



	public DBApp( ){
		
	}

	// this does whatever initialization you would like 
	// or leave it empty if there is no code you want to 
	// execute at application startup 
	public void init( ){
		
		
	}


	// following method creates one table only
	// strClusteringKeyColumn is the name of the column that will be the primary
	// key and the clustering column as well. The data type of that column will
	// be passed in htblColNameType
	// htblColNameValue will have the column name as key and the data 
	// type as value
	public void createTable(String strTableName, 
							String strClusteringKeyColumn,  
							Hashtable<String,String> htblColNameType) throws DBAppException{
								
		throw new DBAppException("not implemented yet");
	}


	// following method creates a B+tree index 
	public void createIndex(String   strTableName,
							String   strColName,
							String   strIndexName) throws DBAppException{
		
		throw new DBAppException("not implemented yet");
	}


	// following method inserts one row only. 
	// htblColNameValue must include a value for the primary key
	public void insertIntoTable(String strTableName, 
								Hashtable<String,Object>  htblColNameValue) throws DBAppException{
		if (searchMetadata(strTableName)){
			Vector column = columnNameReader(strTableName);
		}
		else {
			throw new DBAppException("Table Not Found");
		}

	}
	public boolean searchMetadata(String strTableName) {
		String csvFile = "metadata.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				if (values[0].equals(strTableName)) {
					return true;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}
	public Vector columnNameReader (String strTableName){
		String csvFile = "metadata.csv";

		Vector<String []> column = new Vector<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				if (values[0].equals(strTableName)) {
					String [] Store = {values[1],values[2]};
					column.add(Store);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return column;
	}


	// following method updates one row only
	// htblColNameValue holds the key and new value 
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName, 
							String strClusteringKeyValue,
							Hashtable<String,Object> htblColNameValue   )  throws DBAppException{
	
		throw new DBAppException("not implemented yet");
	}


	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search 
	// to identify which rows/tuples to delete. 	
	// htblColNameValue enteries are ANDED together
	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
	    if (searchMetadata(strTableName)) {
	        Vector<String[]> columns = columnNameReader(strTableName);
	        validateColumnsAndValues(htblColNameValue, columns);
	        performDeletion(strTableName, htblColNameValue);
	    } else {
	        throw new DBAppException("Table Not Found");
	    }
	}

	private void performDeletion(String strTableName, Hashtable<String, Object> htblColNameValue) {
	    Vector<Hashtable<String, Object>> tableData = loadTableData(strTableName);
	    for (int i = 0; i < tableData.size(); i++) {
	        Hashtable<String, Object> row = tableData.get(i);
	        boolean match = true;
	        for (String colName : htblColNameValue.keySet()) {
	            Object colValue = htblColNameValue.get(colName);
	            if (!row.containsKey(colName) || !row.get(colName).equals(colValue)) {
	                match = false;
	                break;
	            }
	        }
	        if (match) {
	            tableData.remove(i);
	            i--;
	        }
	    }
	    saveTableData(strTableName, tableData);
	    System.out.println("Deletion completed successfully.");
	}


	private void validateColumnsAndValues(Hashtable<String, Object> htblColNameValue, Vector<String[]> columns) throws DBAppException {
	    for (String colName : htblColNameValue.keySet()) {
	        boolean found = false;
	        for (String[] column : columns) {
	            if (column[0].equals(colName)) {
	                found = true;
	                break;
	            }
	        }
	        if (!found) {
	            throw new DBAppException("Column '" + colName + "' does not exist in the table.");
	        }
	    }
	    for (String colName : htblColNameValue.keySet()) {
	        Object colValue = htblColNameValue.get(colName);
	        for (String[] column : columns) {
	            if (column[0].equals(colName)) {
	                String colType = column[1];
	                if (!isDataTypeMatching(colValue, colType)) {
	                    throw new DBAppException("Invalid data type for column '" + colName + "'. Expected: " + colType);
	                }
	                break;
	            }
	        }
	    }
	}

	private boolean isDataTypeMatching(Object value, String colType) {
	    String valueType = value.getClass().getName();
	    switch (colType) {
	        case "Integer":
	            colType = "java.lang.Integer";
	            break;
	        case "String":
	            colType = "java.lang.String";
	            break;
	        case "Double":
	            colType = "java.lang.Double";
	            break;
	    }
	    return colType.equals(valueType);
	}


	public Iterator selectFromTable(SQLTerm[] arrSQLTerms, 
									String[]  strarrOperators) throws DBAppException{
										
		return null;
	}


	public static void main( String[] args ){
	
	try{
			String strTableName = "Student";
			DBApp	dbApp = new DBApp( );
			
			Hashtable htblColNameType = new Hashtable( );
			htblColNameType.put("id", "java.lang.Integer");
			htblColNameType.put("name", "java.lang.String");
			htblColNameType.put("gpa", "java.lang.double");
			dbApp.createTable( strTableName, "id", htblColNameType );
			dbApp.createIndex( strTableName, "gpa", "gpaIndex" );

			Hashtable htblColNameValue = new Hashtable( );
			htblColNameValue.put("id", new Integer( 2343432 ));
			htblColNameValue.put("name", new String("Ahmed Noor" ) );
			htblColNameValue.put("gpa", new Double( 0.95 ) );
			dbApp.insertIntoTable( strTableName , htblColNameValue );

			htblColNameValue.clear( );
			htblColNameValue.put("id", new Integer( 453455 ));
			htblColNameValue.put("name", new String("Ahmed Noor" ) );
			htblColNameValue.put("gpa", new Double( 0.95 ) );
			dbApp.insertIntoTable( strTableName , htblColNameValue );

			htblColNameValue.clear( );
			htblColNameValue.put("id", new Integer( 5674567 ));
			htblColNameValue.put("name", new String("Dalia Noor" ) );
			htblColNameValue.put("gpa", new Double( 1.25 ) );
			dbApp.insertIntoTable( strTableName , htblColNameValue );

			htblColNameValue.clear( );
			htblColNameValue.put("id", new Integer( 23498 ));
			htblColNameValue.put("name", new String("John Noor" ) );
			htblColNameValue.put("gpa", new Double( 1.5 ) );
			dbApp.insertIntoTable( strTableName , htblColNameValue );

			htblColNameValue.clear( );
			htblColNameValue.put("id", new Integer( 78452 ));
			htblColNameValue.put("name", new String("Zaky Noor" ) );
			htblColNameValue.put("gpa", new Double( 0.88 ) );
			dbApp.insertIntoTable( strTableName , htblColNameValue );


			SQLTerm[] arrSQLTerms;
			arrSQLTerms = new SQLTerm[2];
			arrSQLTerms[0]._strTableName =  "Student";
			arrSQLTerms[0]._strColumnName=  "name";
			arrSQLTerms[0]._strOperator  =  "=";
			arrSQLTerms[0]._objValue     =  "John Noor";

			arrSQLTerms[1]._strTableName =  "Student";
			arrSQLTerms[1]._strColumnName=  "gpa";
			arrSQLTerms[1]._strOperator  =  "=";
			arrSQLTerms[1]._objValue     =  new Double( 1.5 );

			String[]strarrOperators = new String[1];
			strarrOperators[0] = "OR";
			// select * from Student where name = "John Noor" or gpa = 1.5;
			Iterator resultSet = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
		}
		catch(Exception exp){
			exp.printStackTrace( );
		}
	}

}
