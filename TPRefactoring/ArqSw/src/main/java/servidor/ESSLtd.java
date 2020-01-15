package servidor;
import daos.*;


import java.io.IOException;
import java.math.BigDecimal;

import java.util.*;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class ESSLtd {

	private UtilizadorDAO utilizadores;
	private AtivoDAO ativos;
	private ContratoDAO contratos;
	private RegistoDAO registos;
	private PedidoDAO pedidos;


	public ESSLtd() {
		this.utilizadores = new UtilizadorDAO();
		this.ativos = new AtivoDAO();
		this.contratos = new ContratoDAO();
		this.registos = new RegistoDAO();
		this.pedidos = new PedidoDAO();
	}

	public  Utilizador iniciarSessao(String username, String password) throws UtilizadorInvalidoException {
		Utilizador u = verficaUtilizador(username);
		if (u != null) {
			String pass = u.getPassword();
			if (pass.equals(password))
				return u;
			else
				throw new UtilizadorInvalidoException("Username ou password errada");
		}
		else throw new UtilizadorInvalidoException("Username ou password errada");
	}

	public  void registarUtilizador(String username, String password, int saldo) throws UsernameInvalidoException {
		if ((verficaUtilizador(username)) != null)
			throw new UsernameInvalidoException("Username inválido");
		else {
			int id = utilizadores.size() + 1;

			Utilizador u = new Utilizador(id, username, password, saldo);
			utilizadores.put(id, u);
		}
	}

	public  Utilizador verficaUtilizador(String nome) {
		Utilizador user = null;
		for (Utilizador u : this.utilizadores.values()) {
			if (u.getUsername().equals(nome)) {
				user = u;
				break;
			}
		}

		return user;
	}

	public  float saldo(Utilizador u) {
		return this.utilizadores.get(u.getId()).getPlafom();
	}

	public  List<Ativo> listarAtivos() {
		List<Ativo> ativosList = new ArrayList<>();
		for (Ativo a : this.ativos.values())
			ativosList.add(a.copy());

		return ativosList;
	}

	public  void criarContratoVenda(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {
		criaContrato(u, idAtivo, takeprofit, stoploss, quantidade, false);
	}

	public  void criarContratoCompra(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {
		criaContrato(u, idAtivo, takeprofit, stoploss, quantidade, true);
	}

	public void criaContrato(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade, boolean b) throws AtivoInvalidoException, SaldoInsuficienteException{
		Ativo a = this.ativos.get(idAtivo).copy();
		if (a == null)
			throw new AtivoInvalidoException("Ativo nao existe");
		else {
			addContratoAux(a, u, idAtivo, takeprofit, stoploss, quantidade, b);
		}
	}

	public void addContratoAux(Ativo a, Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade, boolean b) throws SaldoInsuficienteException {
		float preco = a.getPrecoVenda();
		float valorTotal = preco * quantidade;
		float saldo = u.getPlafom();
		int size = this.contratos.size() + 1;
		if (saldo < valorTotal)
			throw new SaldoInsuficienteException("Saldo Insuficiente");
		u.setPlafom(saldo - valorTotal);
		this.utilizadores.put(u.getId(), u);
		Contrato c = new Contrato(size, idAtivo, u.getId(), preco, takeprofit, stoploss, quantidade, b, false);
		this.contratos.put(size, c);//poe na lista total de contratos
	}

	public  List<Contrato> listarPortefolio(Utilizador u) {
		List<Contrato> contratosUtilizador = new ArrayList<>();
		Collection<Contrato> contratosCollection = this.contratos.values();
		for(Contrato c : contratosCollection)
			if(c.getIdUtilizador()==u.getId())
				contratosUtilizador.add(c);
		return contratosUtilizador;
	}

	public  void fecharContrato(Utilizador u, int idContrato) throws ContratoInvalidoException {
		Contrato c = this.contratos.get(idContrato,u.getId());
			if (c != null && !c.isEncerrado()) {
				if (c.isCompra())
					fecharContratoCompra(u, c);
				else
					fecharContratoVenda(u, c);
				return;
			}
			throw new ContratoInvalidoException("Este contrato nao existe ou nao pertence ao utilizador");
	}


	public  Ativo criarAtivo(String ativo, int id) throws IOException {
		float compra;
		float venda;
		BigDecimal zero= new BigDecimal("0.0");
		Stock stock = YahooFinance.get(ativo);
		BigDecimal precoVenda = stock.getQuote().getBid();
		BigDecimal precoCompra = stock.getQuote().getAsk();
		if(precoCompra!=null && precoCompra.compareTo(zero)!=0)
			compra = precoCompra.floatValue();
		else
			return null;
		if(precoVenda!=null &&  precoVenda.compareTo(zero)!= 0)
			venda = precoVenda.floatValue();
		else
			return null;
		Ativo a = new Ativo(id, venda, compra, ativo);
		if (!this.ativos.containsValue(a))
			this.ativos.put(id, a);
		return a;
	}


	public  void fecharContratoCompra(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int idAtivo = c.getIdAtivo();
		Ativo a = ativos.get(idAtivo).copy();// temos que ir ver o valor atual do ativo
		float valorAtual = a.getPrecoCompra() * c.getQuantidade();
		float valorCompra = c.getPreco() * c.getQuantidade();
		float lucro = valorAtual - valorCompra;
		fecharContratoAux(a, u, c, lucro, valorCompra, size);
	}

	public  void fecharContratoVenda(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int idAtivo = c.getIdAtivo();
		Ativo a = ativos.get(idAtivo).copy();// temos que ir ver o valor atual do ativo
		float valorAtual = a.getPrecoVenda() * c.getQuantidade();
		float valorVenda = c.getPreco() * c.getQuantidade();
		float lucro = valorVenda - valorAtual;
		fecharContratoAux(a, u, c, lucro, valorVenda, size);
	}

	public void fecharContratoAux(Ativo a, Utilizador u, Contrato c, float lucro, float valor, int size){
		u.setPlafom(u.getPlafom() + valor + lucro);
		this.utilizadores.put(u.getId(), u);
		Registo r = new Registo(size, u.getId(), a.getId(), lucro, c.getQuantidade());
		this.registos.put(size, r);
		c.setEncerrado(true);
		this.contratos.put(c.getId(), c);
	}

	public List<Registo> listaRegistos(Utilizador u) {
		List<Registo> registosList = new ArrayList<>();
		for (Registo r : this.registos.values())
			if (r.getIdUtilizador() == u.getId())
				registosList.add(r);
		return registosList;
	}

	public  void fecharContratosComLimites(Contrato c ) throws ContratoInvalidoException {
		Utilizador u;
		u = this.utilizadores.get(c.getIdUtilizador());
		fecharContrato(u, c.getId());
		ativos.get(c.getIdAtivo()).removeObserver(c);
	}


	public  void updateEstadoPedido(Pedido p) {
		int id = p.getEstado().getIdentificador();
		this.pedidos.put(id, p);
	}

	public int sizePedidos(){
		return this.pedidos.size();
	}

    public  Ativo getAtivo(String descricao){
	        return this.ativos.get(descricao,this);
    }

    public AtivoDAO getAtivos() {
        return ativos;
    }


/************* NOVO REQUISITO********************/
    public void novaNotificacao(Utilizador u , String notificaco,Ativo a){
		this.utilizadores.putNotificacao(u.getId(),notificaco);
		seguirAtivo(u ,a.getId());
	}

	public List<String> veNotificacoes(Utilizador u ){
		return this.utilizadores.getNotificacoes(u.getId());
	}

	public void seguirAtivo(Utilizador u,  int idAtivo){

		Ativo a = this.ativos.get(idAtivo);
		this.ativos.putSeguidor(u.getId(),a);
	}
}



