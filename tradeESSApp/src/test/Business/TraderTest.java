package test.Business; 

import Business.Trader;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before; 
import org.junit.After; 

/** 
* Trader Tester. 
* 
* @author <Authors name> 
* @since <pre>Oct 18, 2019</pre> 
* @version 1.0 
*/ 
public class TraderTest { 

/** 
* 
* Method: addMoney(float valor) 
* 
*/ 
@Test
public void testAddMoney() throws Exception {
    Trader trader = new Trader(1, "email", "username", "pass", "RUA", 20, 922044955, 0);
    trader.addMoney(50);
    assertEquals(trader.getSaldoConta(), 50, 0.0000001);
} 

/** 
* 
* Method: removeMoney(float valor) 
* 
*/ 
@Test
public void testRemoveMoney() throws Exception { 
//TODO: Test goes here...
    Trader trader = new Trader(1, "email", "username", "pass", "RUA", 20, 922044955, 50);
    trader.removeMoney(25);
    assertEquals(trader.getSaldoConta(), 25, 0.0000001);

}

} 
