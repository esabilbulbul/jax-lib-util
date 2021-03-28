/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.persistence.misc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CacheQueryResultSet 
{
    public long timeStamp = -1;//if -1 always will fetch data
    public List<List<RowColumn>>  RowSet = new ArrayList<List<RowColumn>>();
    public ArrayList<QueryRunParam> params = new ArrayList<QueryRunParam>();
}
