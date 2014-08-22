//@Sean
//Note this interface is used for class that requires to send content to web client.
//Here uses harded coded generated connection string to increase performance.
//Use GSON as alternative for function use now.
package com.hitchride;

public interface ISerializetoJson {
	public String getJson();
}
