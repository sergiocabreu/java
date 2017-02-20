package com.sca.derby;

import com.sca.derby.dao.BancoDao;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	BancoDao.createConnection();
    	BancoDao.insertRestaurants(5, "LaVals", "Berkeley");
    	BancoDao.selectRestaurants();
    	BancoDao.shutdown();
    }
}
