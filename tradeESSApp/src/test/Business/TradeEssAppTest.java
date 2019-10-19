package test.Business; 

import Business.TradeEssApp;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before; 
import org.junit.After; 

/** 
* TradeEssApp Tester. 
* 
* @author <inês>
* @since <pre>Oct 17, 2019</pre> 
* @version 1.0 
*/ 
public class TradeEssAppTest {

/** 
* 
* Method: registarTrader(String email, String username, String pass, String morada, int age, int contacto, float saldoConta) 
* 
*/ 
@Test
public void testRegistarTrader() throws Exception {
    TradeEssApp tradeEssApp = new TradeEssApp();
    tradeEssApp.registarTrader("ines@gmail.com", "ines", "pass1234", "rua", 20, 966911988, 0);
    tradeEssApp.registarTrader("inesalves@gmail.com", "inesalves", "pass1234", "rua", 20, 966911987, 0);
    assertTrue(tradeEssApp.getUsers().size() == 2);
    assertTrue(tradeEssApp.idUserGivenUsername("ines") == 1);
    assertTrue(tradeEssApp.idUserGivenUsername("inesalves") == 2);
} 

/** 
* 
* Method: iniciarSessao(String username, String password) 
* 
*/ 
@Test
public void testIniciarSessao() throws Exception {
    TradeEssApp tradeEssApp = new TradeEssApp();
    tradeEssApp.registarTrader("ines@gmail.com", "ines", "pass1234", "rua", 20, 966911988, 0);
    tradeEssApp.iniciarSessao("ines", "pass1234");
    assertTrue(tradeEssApp.getUser().getEmail().equals("ines@gmail.com"));
} 


} 
