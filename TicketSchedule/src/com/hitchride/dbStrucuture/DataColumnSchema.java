package com.hitchride.dbStrucuture;

//Subset of MySQL schema table. For those function not used, treated at controllar layer.
//Used as a centralized structure to generate tb.
public class DataColumnSchema {
	String _columnName;
	String _fieldDefine;
	public DataColumnSchema(String columnName,String fieldDefine)
	{
		this._columnName= columnName;
		this._fieldDefine = fieldDefine; //Here the field type also contains constrain info.
	}
}
